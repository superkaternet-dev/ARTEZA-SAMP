# REST API Documentation

## Base URL
```
http://localhost:3000/api/v1
```

## Endpoints

### Get All Items
```
GET /items
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Item 1",
      "description": "Description 1",
      "created": "2024-01-01T00:00:00.000Z"
    }
  ],
  "count": 1
}
```

### Get Single Item
```
GET /items/:id
```

**Example:**
```bash
curl http://localhost:3000/api/v1/items/1
```

### Create Item
```
POST /items
Content-Type: application/json
Authorization: Bearer YOUR_TOKEN

{
  "name": "New Item",
  "description": "Item description"
}
```

### Update Item
```
PUT /items/:id
Content-Type: application/json
Authorization: Bearer YOUR_TOKEN

{
  "name": "Updated Item",
  "description": "Updated description"
}
```

### Delete Item
```
DELETE /items/:id
Authorization: Bearer YOUR_TOKEN
```

## Error Handling

All errors return appropriate HTTP status codes:
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `500` - Internal Server Error
