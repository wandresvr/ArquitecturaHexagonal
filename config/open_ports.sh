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

# ====== Detectar primera VM y grupo ======
VM_INFO=$(az vm list --query "[0]" -o json)
VM_NAME=$(echo "$VM_INFO" | jq -r .name)
RESOURCE_GROUP=$(echo "$VM_INFO" | jq -r .resourceGroup)

if [[ -z "$VM_NAME" || -z "$RESOURCE_GROUP" ]]; then
  echo "❌ No se encontró ninguna VM."
  exit 1
fi

echo "✅ VM detectada: $VM_NAME"
echo "✅ Grupo de recursos: $RESOURCE_GROUP"

# ====== Obtener NIC y nombre ======
NIC_ID=$(az vm show --resource-group "$RESOURCE_GROUP" --name "$VM_NAME" --query "networkProfile.networkInterfaces[0].id" -o tsv)
NIC_NAME=$(basename "$NIC_ID")

# ====== Obtener o crear NSG ======
NSG_ID=$(az network nic show --resource-group "$RESOURCE_GROUP" --name "$NIC_NAME" --query "ipConfigurations[0].networkSecurityGroup.id" -o tsv || true)
if [[ -z "$NSG_ID" ]]; then
  NSG_NAME="${VM_NAME}-NSG"
  echo "⚠️ No se encontró un NSG asociado. Creando: $NSG_NAME..."
  az network nsg create --resource-group "$RESOURCE_GROUP" --name "$NSG_NAME" --location "$(az vm show --resource-group "$RESOURCE_GROUP" --name "$VM_NAME" --query "location" -o tsv)"
  echo "🔗 Asociando NSG a la NIC $NIC_NAME..."
  az network nic update --resource-group "$RESOURCE_GROUP" --name "$NIC_NAME" --network-security-group "$NSG_NAME"
else
  NSG_NAME=$(basename "$NSG_ID")
  echo "✅ NSG detectado: $NSG_NAME"
fi

# ====== Puertos a verificar ======
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
  echo "🔍 Verificando puerto $puerto ($nombre)..."

  ya_existe=$(az network nsg rule list \
    --resource-group "$RESOURCE_GROUP" \
    --nsg-name "$NSG_NAME" \
    --query "[?destinationPortRange=='$puerto'] | length(@)" \
    -o tsv)

  if [[ "$ya_existe" -gt 0 ]]; then
    echo "✅ Ya existe una regla para el puerto $puerto."
  else
    PRIORIDAD=$((1000 + puerto % 1000))
    RULE_NAME="permitir-${nombre}"

    echo "➕ Creando regla '$RULE_NAME' para puerto $puerto con prioridad $PRIORIDAD..."
    az network nsg rule create \
      --resource-group "$RESOURCE_GROUP" \
      --nsg-name "$NSG_NAME" \
      --name "$RULE_NAME" \
      --priority "$PRIORIDAD" \
      --access Allow \
      --direction Inbound \
      --protocol Tcp \
      --destination-port-range "$puerto" \
      --source-address-prefixes "*" \
      --only-show-errors
  fi
done

echo "🎉 Todos los puertos han sido verificados y abiertos si fue necesario."
