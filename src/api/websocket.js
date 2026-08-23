const WebSocket = require('ws');

const clients = new Map();
let messageId = 0;

function handleWebSocket(wsServer) {
  wsServer.on('connection', (ws, request) => {
    const clientId = Date.now().toString();
    clients.set(clientId, ws);

    console.log(`✅ WebSocket connected: ${clientId}`);

    // Send welcome message
    ws.send(JSON.stringify({
      type: 'connection',
      message: 'Connected to WebSocket server',
      clientId,
      timestamp: new Date().toISOString(),
    }));

    // Handle incoming messages
    ws.on('message', (data) => {
      try {
        const message = JSON.parse(data);
        handleMessage(clientId, message, ws);
      } catch (error) {
        ws.send(JSON.stringify({
          type: 'error',
          error: 'Invalid JSON format',
        }));
      }
    });

    // Handle disconnection
    ws.on('close', () => {
      clients.delete(clientId);
      broadcastMessage({
        type: 'notification',
        message: `Client ${clientId} disconnected`,
        timestamp: new Date().toISOString(),
      });
      console.log(`❌ WebSocket disconnected: ${clientId}`);
    });

    ws.on('error', (error) => {
      console.error(`Error in WebSocket ${clientId}:`, error);
    });
  });
}

function handleMessage(clientId, message, ws) {
  const { type, payload } = message;
  const msgId = ++messageId;

  console.log(`📨 Message from ${clientId}:`, message);

  switch (type) {
    case 'ping':
      ws.send(JSON.stringify({
        type: 'pong',
        id: msgId,
        timestamp: new Date().toISOString(),
      }));
      break;

    case 'broadcast':
      broadcastMessage({
        type: 'broadcast',
        id: msgId,
        sender: clientId,
        payload,
        timestamp: new Date().toISOString(),
      });
      break;

    case 'echo':
      ws.send(JSON.stringify({
        type: 'echo',
        id: msgId,
        payload,
        timestamp: new Date().toISOString(),
      }));
      break;

    case 'notification':
      broadcastMessage({
        type: 'notification',
        id: msgId,
        sender: clientId,
        payload,
        timestamp: new Date().toISOString(),
      });
      break;

    default:
      ws.send(JSON.stringify({
        type: 'error',
        error: `Unknown message type: ${type}`,
      }));
  }
}

function broadcastMessage(message) {
  clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(message));
    }
  });
}

module.exports = handleWebSocket;
