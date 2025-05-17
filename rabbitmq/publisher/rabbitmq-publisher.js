// rabbitmq-publisher.js

const amqp = require('amqplib');

const RABBITMQ_URL = process.env.RABBITMQ_URL || 'amqp://guest:guest@localhost:5672';

// Exchanges y routing keys
const ORDER_EXCHANGE = 'order.exchange';
const ORDER_ROUTING_KEY = 'order.key';
const ORDER_QUEUE = 'order.queue';

const STOCK_RESPONSE_EXCHANGE = 'stock.response.exchange';
const STOCK_RESPONSE_ROUTING_KEY = 'stock.response.key';
const STOCK_RESPONSE_QUEUE = 'stock.response.queue';

const STOCK_UPDATE_EXCHANGE = 'stock.update.exchange';
const STOCK_UPDATE_ROUTING_KEY = 'stock.update.key';
const STOCK_UPDATE_QUEUE = 'stock.update.queue';

/**
 * Publica un objeto como mensaje JSON en RabbitMQ.
 * @param {Object} orderPayload - El payload de la orden.
 *   Debe tener la forma que esperan tus DTOs en Java:
 *   {
 *     orderId: string,
 *     client: { name, email, phone },
 *     products: [ { productId: string, quantity: number }, ... ],
 *     shippingAddress: { street, city, state, zipCode, country }
 *   }
 */
async function publishOrder(orderPayload) {
  let connection, channel;
  try {
    connection = await amqp.connect(RABBITMQ_URL);
    channel = await connection.createChannel();

    // Asegura que los exchanges existan
    await channel.assertExchange(ORDER_EXCHANGE, 'direct', { durable: true });
    await channel.assertExchange(STOCK_RESPONSE_EXCHANGE, 'direct', { durable: true });
    await channel.assertExchange(STOCK_UPDATE_EXCHANGE, 'direct', { durable: true });

    // Asegura que las colas existan
    await channel.assertQueue(ORDER_QUEUE, { durable: true });
    await channel.assertQueue(STOCK_RESPONSE_QUEUE, { durable: true });
    await channel.assertQueue(STOCK_UPDATE_QUEUE, { durable: true });

    // Vincula las colas a sus exchanges
    await channel.bindQueue(ORDER_QUEUE, ORDER_EXCHANGE, ORDER_ROUTING_KEY);
    await channel.bindQueue(STOCK_RESPONSE_QUEUE, STOCK_RESPONSE_EXCHANGE, STOCK_RESPONSE_ROUTING_KEY);
    await channel.bindQueue(STOCK_UPDATE_QUEUE, STOCK_UPDATE_EXCHANGE, STOCK_UPDATE_ROUTING_KEY);

    // Publica el mensaje
    const payloadBuffer = Buffer.from(JSON.stringify(orderPayload));
    channel.publish(ORDER_EXCHANGE, ORDER_ROUTING_KEY, payloadBuffer, { persistent: true });

    console.log('✅ Mensaje enviado a RabbitMQ:', orderPayload);
  } catch (err) {
    console.error('❌ Error publicando en RabbitMQ:', err);
    throw err;
  } finally {
    if (channel) await channel.close();
    if (connection) await connection.close();
  }
}

module.exports = { publishOrder };
