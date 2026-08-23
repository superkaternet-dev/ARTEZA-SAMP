# WebSocket API Documentation

## Connection
```
ws://localhost:3000/ws
```

## Message Types

### Ping
```json
{
  "type": "ping"
}
```

### Broadcast
```json
{
  "type": "broadcast",
  "payload": {
    "message": "Hello everyone!"
  }
}
```

### Echo
```json
{
  "type": "echo",
  "payload": {
    "message": "Test message"
  }
}
```

### Notification
```json
{
  "type": "notification",
  "payload": {
    "title": "New Update",
    "body": "Something happened"
  }
}
```

## Example Client (JavaScript)

```javascript
const ws = new WebSocket('ws://localhost:3000/ws');

ws.onopen = () => {
  console.log('Connected');
  ws.send(JSON.stringify({ type: 'ping' }));
};

ws.onmessage = (event) => {
  const message = JSON.parse(event.data);
  console.log('Received:', message);
};

ws.onerror = (error) => {
  console.error('Error:', error);
};

ws.onclose = () => {
  console.log('Disconnected');
};
```
