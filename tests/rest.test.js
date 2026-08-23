const request = require('supertest');
const app = require('../src/index');

describe('REST API', () => {
  describe('GET /api/v1/items', () => {
    it('should get all items', async () => {
      const response = await request(app)
        .get('/api/v1/items')
        .expect(200);

      expect(response.body.success).toBe(true);
      expect(Array.isArray(response.body.data)).toBe(true);
    });
  });

  describe('GET /api/v1/items/:id', () => {
    it('should get single item', async () => {
      const response = await request(app)
        .get('/api/v1/items/1')
        .expect(200);

      expect(response.body.success).toBe(true);
      expect(response.body.data.id).toBe(1);
    });

    it('should return 404 for non-existent item', async () => {
      const response = await request(app)
        .get('/api/v1/items/999')
        .expect(404);

      expect(response.body.success).toBe(false);
    });
  });

  describe('POST /api/v1/items', () => {
    it('should create new item', async () => {
      const response = await request(app)
        .post('/api/v1/items')
        .set('Authorization', 'Bearer token')
        .send({ name: 'Test Item', description: 'Test' })
        .expect(201);

      expect(response.body.success).toBe(true);
      expect(response.body.data.name).toBe('Test Item');
    });
  });
});
