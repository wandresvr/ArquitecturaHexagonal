#!/bin/bash

set -e

# ============ VERIFICAR / INSTALAR AZURE CLI ============
if ! command -v az &> /dev/null; then
  echo "🔧 Azure CLI no está instalado. Instalando..."
  curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash
else
  echo "✅ Azure CLI ya está instalado."
fi

# ============ AUTENTICACIÓN ============

if ! az account show &> /dev/null; then
  echo "🔐 Inicia sesión en Azure..."
  az login
fi

# ============ DETECTAR IP PÚBLICA DEL USUARIO ============
IP_PUBLICA=$(curl -s ifconfig.me)
echo "🌐 Tu IP pública es: $IP_PUBLICA"

# ============ DETECTAR PRIMERA VM Y GRUPO ============
echo "🔍 Buscando primera máquina virtual disponible..."

VM_INFO=$(az vm list --query "[0]" -o json)
VM_NAME=$(echo "$VM_INFO" | jq -r .name)
RESOURCE_GROUP=$(echo "$VM_INFO" | jq -r .resourceGroup)

if [[ -z "$VM_NAME" || -z "$RESOURCE_GROUP" ]]; then
  echo "❌ No se encontró ninguna máquina virtual en tu suscripción."
  exit 1
fi

echo "✅ VM detectada: $VM_NAME"
echo "✅ Grupo de recursos: $RESOURCE_GROUP"

# ============ OBTENER NSG ASOCIADO CORRECTAMENTE ============
NIC_ID=$(az vm show --resource-group "$RESOURCE_GROUP" --name "$VM_NAME" --query "networkProfile.networkInterfaces[0].id" -o tsv)
NIC_NAME=$(basename "$NIC_ID")

NSG_ID=$(az network nic show --resource-group "$RESOURCE_GROUP" --name "$NIC_NAME" --query "ipConfigurations[0].networkSecurityGroup.id" -o tsv)
NSG_NAME=$(basename "$NSG_ID")

if [[ -z "$NSG_NAME" ]]; then
  echo "❌ No se pudo detectar un NSG para la VM."
  exit 1
fi

echo "🔐 NSG asociado: $NSG_NAME"

# ============ PUERTOS A ABRIR ============
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

# ============ ABRIR PUERTOS SI NO EXISTEN ============
for nombre in "${!PUERTOS[@]}"; do
  puerto=${PUERTOS[$nombre]}
  regla="permitir-$nombre"

  existe=$(az network nsg rule list \
    --nsg-name "$NSG_NAME" \
    --resource-group "$RESOURCE_GROUP" \
    --query "[?destinationPortRange=='$puerto']" -o tsv)

  if [[ -z "$existe" ]]; then
    echo "➕ Abriendo puerto $puerto ($nombre) solo para IP $IP_PUBLICA..."
    az network nsg rule create \
      --nsg-name "$NSG_NAME" \
      --resource-group "$RESOURCE_GROUP" \
      --name "$regla" \
      --priority $((1000 + puerto % 1000)) \
      --access Allow \
      --direction Inbound \
      --protocol Tcp \
      --destination-port-range "$puerto" \
      --source-address-prefixes "$IP_PUBLICA" \
      --output none
  else
    echo "✅ Puerto $puerto ($nombre) ya está permitido."
  fi
done

echo "🎉 Puertos abiertos correctamente y seguros para tu IP: $IP_PUBLICA"
