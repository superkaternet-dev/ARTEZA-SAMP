/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.core.DatabaseConfig;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.RepoInfo;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.Node;

public class InternalHelpers {
    public static DataSnapshot createDataSnapshot(DatabaseReference databaseReference, IndexedNode indexedNode) {
        return new DataSnapshot(databaseReference, indexedNode);
    }

    public static FirebaseDatabase createDatabaseForTests(FirebaseApp firebaseApp, RepoInfo repoInfo, DatabaseConfig databaseConfig) {
        return FirebaseDatabase.createForTests(firebaseApp, repoInfo, databaseConfig);
    }

    public static MutableData createMutableData(Node node) {
        return new MutableData(node);
    }

    public static DatabaseReference createReference(Repo repo, Path path) {
        return new DatabaseReference(repo, path);
    }
}

