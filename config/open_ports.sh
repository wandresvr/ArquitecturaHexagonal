#!/bin/bash

set -e

# ============ VALIDAR DEPENDENCIAS ============

if ! command -v az &> /dev/null; then
  echo "🔧 Azure CLI no está instalado. Instalando..."
  curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash
else
  echo "✅ Azure CLI ya está instalado."
fi

# ============ PARSEAR ARGUMENTOS ============

while [[ "$#" -gt 0 ]]; do
  case $1 in
    --vm-name) VM_NAME="$2"; shift ;;
    --resource-group) RESOURCE_GROUP="$2"; shift ;;
    *) echo "❌ Opción desconocida: $1" && exit 1 ;;
  esac
  shift
done

if [[ -z "$VM_NAME" || -z "$RESOURCE_GROUP" ]]; then
  echo "❗ Uso: $0 --vm-name <nombre_vm> --resource-group <nombre_grupo>"
  exit 1
fi

# ============ LISTA DE PUERTOS A ABRIR ============

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

# ============ OBTENER NSG ASOCIADO ============

NSG_NAME=$(az network nic list \
  --resource-group "$RESOURCE_GROUP" \
  --query "[?virtualMachine.id.contains('$VM_NAME')].ipConfigurations[0].networkSecurityGroup.id" \
  -o tsv | awk -F/ '{print $NF}')

if [[ -z "$NSG_NAME" ]]; then
  echo "❌ No se pudo obtener el NSG para la VM '$VM_NAME' en el grupo '$RESOURCE_GROUP'."
  exit 1
fi

echo "🔍 NSG detectado: $NSG_NAME"

# ============ ABRIR PUERTOS SI NO EXISTEN ============

for nombre in "${!PUERTOS[@]}"; do
  puerto=${PUERTOS[$nombre]}
  regla="permitir-$nombre"

  existe=$(az network nsg rule list \
    --nsg-name "$NSG_NAME" \
    --resource-group "$RESOURCE_GROUP" \
    --query "[?destinationPortRange=='$puerto']" -o tsv)

  if [[ -z "$existe" ]]; then
    echo "➕ Abriendo puerto $puerto ($nombre)..."
    az network nsg rule create \
      --nsg-name "$NSG_NAME" \
      --resource-group "$RESOURCE_GROUP" \
      --name "$regla" \
      --priority $((1000 + puerto % 1000)) \
      --access Allow \
      --direction Inbound \
      --protocol Tcp \
      --destination-port-range "$puerto" \
      --source-address-prefixes "*" \
      --output none
  else
    echo "✅ Puerto $puerto ($nombre) ya está abierto."
  fi
done

echo "🎉 Listo: Todos los puertos requeridos han sido verificados y abiertos si era necesario."
