const express = require('express');
const router = express.Router();
const { authenticate } = require('../middleware/auth');

// Mock database
const items = [
  { id: 1, name: 'Item 1', description: 'Description 1', created: new Date() },
  { id: 2, name: 'Item 2', description: 'Description 2', created: new Date() },
];

let nextId = 3;

// GET /api/v1/items - Get all items
router.get('/items', (req, res) => {
  res.json({
    success: true,
    data: items,
    count: items.length,
  });
});

// GET /api/v1/items/:id - Get single item
router.get('/items/:id', (req, res) => {
  const item = items.find(i => i.id === parseInt(req.params.id));
  if (!item) {
    return res.status(404).json({
      success: false,
      error: 'Item not found',
    });
  }
  res.json({
    success: true,
    data: item,
  });
});

// POST /api/v1/items - Create new item
router.post('/items', authenticate, (req, res) => {
  const { name, description } = req.body;

  if (!name) {
    return res.status(400).json({
      success: false,
      error: 'Name is required',
    });
  }

  const newItem = {
    id: nextId++,
    name,
    description: description || '',
    created: new Date(),
  };

  items.push(newItem);

  res.status(201).json({
    success: true,
    data: newItem,
    message: 'Item created successfully',
  });
});

// PUT /api/v1/items/:id - Update item
router.put('/items/:id', authenticate, (req, res) => {
  const item = items.find(i => i.id === parseInt(req.params.id));
  if (!item) {
    return res.status(404).json({
      success: false,
      error: 'Item not found',
    });
  }

  const { name, description } = req.body;
  if (name) item.name = name;
  if (description !== undefined) item.description = description;
  item.updated = new Date();

  res.json({
    success: true,
    data: item,
    message: 'Item updated successfully',
  });
});

// DELETE /api/v1/items/:id - Delete item
router.delete('/items/:id', authenticate, (req, res) => {
  const index = items.findIndex(i => i.id === parseInt(req.params.id));
  if (index === -1) {
    return res.status(404).json({
      success: false,
      error: 'Item not found',
    });
  }

  const deletedItem = items.splice(index, 1);

  res.json({
    success: true,
    data: deletedItem[0],
    message: 'Item deleted successfully',
  });
});

module.exports = router;
