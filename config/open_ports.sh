#!/bin/bash

# Detectar la primera VM y su resource group
VM_INFO=$(az vm list --query "[0]" -o json)
VM_NAME=$(echo "$VM_INFO" | jq -r .name)
RESOURCE_GROUP=$(echo "$VM_INFO" | jq -r .resourceGroup)

echo "VM: $VM_NAME"
echo "Resource Group: $RESOURCE_GROUP"

# Lista de puertos a abrir
PUERTOS=(9000 5433 8080 5432 3001 15672 8081 5434)

for PORT in "${PUERTOS[@]}"; do
  echo "Abriendo puerto $PORT..."
  az vm open-port --port $PORT --resource-group "$RESOURCE_GROUP" --name "$VM_NAME" --only-show-errors
done

echo "¡Puertos abiertos!"
