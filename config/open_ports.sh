#!/bin/bash
set -e

# ====== Verificar / Instalar Azure CLI ======
if ! command -v az &> /dev/null; then
  echo "🔧 Azure CLI no está instalado. Instalando..."
  curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash
else
  echo "✅ Azure CLI ya está instalado."
fi

# ====== Login si es necesario ======
if ! az account show &> /dev/null; then
  echo "🔐 Inicia sesión en Azure..."
  az login
fi

# ====== Detectar VM y Resource Group ======
VM_INFO=$(az vm list --query "[0]" -o json)
VM_NAME=$(echo "$VM_INFO" | jq -r .name)
RESOURCE_GROUP=$(echo "$VM_INFO" | jq -r .resourceGroup)

if [[ -z "$VM_NAME" || -z "$RESOURCE_GROUP" ]]; then
  echo "❌ No se encontró ninguna VM."
  exit 1
fi

echo "✅ VM detectada: $VM_NAME"
echo "✅ Grupo de recursos: $RESOURCE_GROUP"

# ====== Puertos a abrir ======
declare -A PUERTOS=(
  [sonarqube_9000]=9000
  [sonarqube_5433]=5433
  [pedidos_8080]=8080
  [pedidos_5432]=5432
  [rabbitmq_3001]=3001
  [rabbitmq_15672]=15672
  [inventario_8081]=8081
  [inventario_5434]=5434
)

# ====== Validar y abrir puertos ======
for nombre in "${!PUERTOS[@]}"; do
  puerto=${PUERTOS[$nombre]}
  echo "🔍 Validando puerto $puerto ($nombre)..."

  # Obtener NSG asociado a la VM (a través de NIC)
  NIC_ID=$(az vm show --resource-group "$RESOURCE_GROUP" --name "$VM_NAME" --query "networkProfile.networkInterfaces[0].id" -o tsv)
  NIC_NAME=$(basename "$NIC_ID")

  NSG_ID=$(az network nic show --resource-group "$RESOURCE_GROUP" --name "$NIC_NAME" --query "ipConfigurations[0].networkSecurityGroup.id" -o tsv)
  NSG_NAME=$(basename "$NSG_ID")

  if [[ -z "$NSG_NAME" ]]; then
    echo "⚠️ No se encontró NSG asociado a la VM para el puerto $puerto. Abriendo directamente con az vm open-port."
    az vm open-port --port $puerto --resource-group "$RESOURCE_GROUP" --name "$VM_NAME" --only-show-errors
    continue
  fi

  # Revisar si ya existe regla para ese puerto
  regla_existente=$(az network nsg rule list --resource-group "$RESOURCE_GROUP" --nsg-name "$NSG_NAME" \
    --query "[?destinationPortRange=='$puerto'] | length(@)" -o tsv)

  if [[ "$regla_existente" -gt 0 ]]; then
    echo "✅ Puerto $puerto ya está abierto en NSG $NSG_NAME."
  else
    echo "➕ Abriendo puerto $puerto en NSG $NSG_NAME..."
    az network nsg rule create --resource-group "$RESOURCE_GROUP" --nsg-name "$NSG_NAME" \
      --name "permitir-$nombre" --priority $((1000 + puerto % 1000)) --access Allow --direction Inbound \
      --protocol Tcp --destination-port-range "$puerto" --source-address-prefixes "*" --only-show-errors
  fi
done

echo "🎉 ¡Configuración finalizada!"
