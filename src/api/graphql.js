const { gql } = require('apollo-server-express');

// GraphQL Schema
const typeDefs = gql`
  type Item {
    id: ID!
    name: String!
    description: String
    created: String!
    updated: String
  }

  type Query {
    items: [Item!]!
    item(id: ID!): Item
  }

  type Mutation {
    createItem(name: String!, description: String): Item!
    updateItem(id: ID!, name: String, description: String): Item
    deleteItem(id: ID!): Boolean!
  }

  type Subscription {
    itemCreated: Item!
    itemUpdated: Item!
    itemDeleted: ID!
  }
`;

// Mock data
const items = [
  { id: '1', name: 'Item 1', description: 'Description 1', created: new Date().toISOString() },
  { id: '2', name: 'Item 2', description: 'Description 2', created: new Date().toISOString() },
];

let nextId = 3;

// Resolvers
const resolvers = {
  Query: {
    items: () => items,
    item: (_, { id }) => items.find(item => item.id === id),
  },

  Mutation: {
    createItem: (_, { name, description }) => {
      const newItem = {
        id: String(nextId++),
        name,
        description: description || '',
        created: new Date().toISOString(),
      };
      items.push(newItem);
      return newItem;
    },

    updateItem: (_, { id, name, description }) => {
      const item = items.find(i => i.id === id);
      if (!item) return null;

      if (name) item.name = name;
      if (description !== undefined) item.description = description;
      item.updated = new Date().toISOString();

      return item;
    },

    deleteItem: (_, { id }) => {
      const index = items.findIndex(i => i.id === id);
      if (index === -1) return false;
      items.splice(index, 1);
      return true;
    },
  },

  Subscription: {
    itemCreated: {
      subscribe: () => {
        // Implementation for subscriptions
      },
    },
    itemUpdated: {
      subscribe: () => {
        // Implementation for subscriptions
      },
    },
    itemDeleted: {
      subscribe: () => {
        // Implementation for subscriptions
      },
    },
  },
};

module.exports = { typeDefs, resolvers };
