# gRPC API Documentation

## Protocol Definition

```protobuf
syntax = "proto3";

package arteza.samp;

service ItemService {
  rpc GetItems (Empty) returns (ItemList);
  rpc GetItem (ItemId) returns (Item);
  rpc CreateItem (CreateItemRequest) returns (Item);
  rpc UpdateItem (UpdateItemRequest) returns (Item);
  rpc DeleteItem (ItemId) returns (Empty);
  rpc StreamItems (Empty) returns (stream Item);
}

message Empty {}

message Item {
  string id = 1;
  string name = 2;
  string description = 3;
  string created = 4;
  string updated = 5;
}

message ItemList {
  repeated Item items = 1;
}

message ItemId {
  string id = 1;
}

message CreateItemRequest {
  string name = 1;
  string description = 2;
}

message UpdateItemRequest {
  string id = 1;
  string name = 2;
  string description = 3;
}
```

## Usage

### Install grpcurl
```bash
brew install grpcurl
```

### Call Service
```bash
grpcurl -plaintext localhost:50051 arteza.samp.ItemService.GetItems
```
