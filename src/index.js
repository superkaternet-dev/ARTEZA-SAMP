const express = require('express');
const cors = require('cors');
const { ApolloServer } = require('apollo-server-express');
const WebSocket = require('ws');
require('dotenv').config();

const restRouter = require('./api/rest');
const { typeDefs, resolvers } = require('./api/graphql');
const websocketHandler = require('./api/websocket');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// REST API
app.use('/api/v1', restRouter);

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'OK', timestamp: new Date().toISOString() });
});

// GraphQL
const startApolloServer = async () => {
  const server = new ApolloServer({
    typeDefs,
    resolvers,
  });

  await server.start();
  server.applyMiddleware({ app });
};

// WebSocket Server
const wsServer = new WebSocket.Server({ noServer: true });
websocketHandler(wsServer);

const server = app.listen(PORT, () => {
  console.log(`🚀 Server running on http://localhost:${PORT}`);
  console.log(`📊 GraphQL running on http://localhost:${PORT}/graphql`);
  console.log(`🔌 WebSocket ready on ws://localhost:${PORT}`);
});

// WebSocket upgrade
server.on('upgrade', (request, socket, head) => {
  if (request.url === '/ws') {
    wsServer.handleUpgrade(request, socket, head, (ws) => {
      wsServer.emit('connection', ws, request);
    });
  }
});

startApolloServer();

module.exports = app;
