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

# ====== Detectar primera VM y su grupo ======
VM_INFO=$(az vm list --query "[0]" -o json)
VM_NAME=$(echo "$VM_INFO" | jq -r .name)
RESOURCE_GROUP=$(echo "$VM_INFO" | jq -r .resourceGroup)

if [[ -z "$VM_NAME" || -z "$RESOURCE_GROUP" ]]; then
  echo "❌ No se encontró ninguna VM."
  exit 1
fi

echo "✅ VM detectada: $VM_NAME"
echo "✅ Grupo de recursos: $RESOURCE_GROUP"

# ====== Obtener NIC y NSG asociado ======
NIC_ID=$(az vm show --resource-group "$RESOURCE_GROUP" --name "$VM_NAME" --query "networkProfile.networkInterfaces[0].id" -o tsv)
NIC_NAME=$(basename "$NIC_ID")

NSG_ID=$(az network nic show --resource-group "$RESOURCE_GROUP" --name "$NIC_NAME" --query "ipConfigurations[0].networkSecurityGroup.id" -o tsv)
NSG_NAME=$(basename "$NSG_ID")

if [[ -z "$NSG_NAME" ]]; then
  echo "❌ No se detectó un NSG asociado a la VM ($VM_NAME)."
  echo "ℹ️ Puedes asociar uno con:"
  echo "   az network nsg create --resource-group $RESOURCE_GROUP --name ${VM_NAME}-NSG"
  echo "   az network nic update --resource-group $RESOURCE_GROUP --name $NIC_NAME --network-security-group ${VM_NAME}-NSG"
  exit 1
fi

echo "✅ NSG detectado: $NSG_NAME"

# ====== Puertos a validar/abrir ======
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

# ====== Recorrer los puertos ======
for nombre in "${!PUERTOS[@]}"; do
  puerto=${PUERTOS[$nombre]}
  echo "🔍 Verificando puerto $puerto ($nombre)..."

  regla_existente=$(az network nsg rule list \
    --resource-group "$RESOURCE_GROUP" \
    --nsg-name "$NSG_NAME" \
    --query "[?destinationPortRange=='$puerto'] | length(@)" \
    -o tsv)

  if [[ "$regla_existente" -gt 0 ]]; then
    echo "✅ Ya existe una regla para el puerto $puerto."
  else
    PRIORIDAD=$((1000 + puerto % 1000))  # Prioridades únicas y válidas
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
