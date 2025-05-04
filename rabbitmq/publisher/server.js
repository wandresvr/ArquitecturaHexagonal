// server.js
const express    = require('express');
const bodyParser = require('body-parser');
const { publishOrder } = require('./rabbitmq-publisher');

const app = express();
const PORT = process.env.PORT || 3000;

// 1) Configura body-parser para leer JSON del body
app.use(bodyParser.json());

// 2) Define endpoint POST /publish-order
app.post('/publish-order', async (req, res) => {
  const orderPayload = req.body;

  // Validación mínima de esquema
  if (!orderPayload || !Array.isArray(orderPayload.products)) {
    return res.status(400).json({ error: 'Payload inválido' });
  }

  try {
    // 3) Publica en RabbitMQ
    await publishOrder(orderPayload);
    // 4) Responde al cliente
    res.status(200).json({ status: 'ok', message: 'Orden encolada correctamente' });
  } catch (err) {
    console.error('Error al publicar orden:', err);
    res.status(500).json({ status: 'error', message: err.message });
  }
});

// 5) Arranca el servidor
app.listen(PORT, () => {
  console.log(`🛫 Publisher HTTP API escuchando en puerto ${PORT}`);
});
