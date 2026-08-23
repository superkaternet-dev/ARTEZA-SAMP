# GraphQL API Documentation

## Endpoint
```
http://localhost:3000/graphql
```

## Queries

### Get All Items
```graphql
query {
  items {
    id
    name
    description
    created
  }
}
```

### Get Single Item
```graphql
query {
  item(id: "1") {
    id
    name
    description
    created
  }
}
```

## Mutations

### Create Item
```graphql
mutation {
  createItem(name: "New Item", description: "Description") {
    id
    name
    description
    created
  }
}
```

### Update Item
```graphql
mutation {
  updateItem(id: "1", name: "Updated", description: "Updated desc") {
    id
    name
    description
    updated
  }
}
```

### Delete Item
```graphql
mutation {
  deleteItem(id: "1")
}
```

## Subscriptions

```graphql
subscription {
  itemCreated {
    id
    name
    created
  }
}
```
