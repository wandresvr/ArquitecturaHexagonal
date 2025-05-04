// rabbitmq-publisher.js

const amqp = require('amqplib');

const RABBITMQ_URL = process.env.RABBITMQ_URL || 'amqp://guest:guest@localhost:5672';
const EXCHANGE      = 'order.exchange';
const ROUTING_KEY   = 'order.key';
const QUEUE_NAME    = 'order.queue';  // Nombre de la cola

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
    channel    = await connection.createChannel();

    // Asegura que el exchange exista
    await channel.assertExchange(EXCHANGE, 'direct', { durable: true });

    // Asegura que la cola exista
    await channel.assertQueue(QUEUE_NAME, { 
      durable: true  // La cola sobrevivirá a reinicios del broker
    });

    // Vincula la cola al exchange
    await channel.bindQueue(QUEUE_NAME, EXCHANGE, ROUTING_KEY);

    // Publica el mensaje
    const payloadBuffer = Buffer.from(JSON.stringify(orderPayload));
    channel.publish(EXCHANGE, ROUTING_KEY, payloadBuffer, { persistent: true });

    console.log('✅ Mensaje enviado a RabbitMQ:', orderPayload);
  } catch (err) {
    console.error('❌ Error publicando en RabbitMQ:', err);
  } finally {
    if (channel)    await channel.close();
    if (connection) await connection.close();
  }
}

module.exports = { publishOrder };
