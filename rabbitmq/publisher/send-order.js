// send-order.js
const { publishOrder } = require('./rabbitmq-publisher');

// Lee el payload JSON desde el primer argumento
const arg = process.argv[2];
if (!arg) {
  console.error('Uso: node send-order.js \'<payload-en-JSON>\'');
  process.exit(1);
}

let order;
try {
  order = JSON.parse(arg);
} catch {
  console.error('❌ JSON inválido');
  process.exit(1);
}

// Publica y sale
publishOrder(order)
  .then(() => process.exit(0))
  .catch(() => process.exit(1));
