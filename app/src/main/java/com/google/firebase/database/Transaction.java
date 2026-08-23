/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.snapshot.Node;

public class Transaction {
    public static Result abort() {
        return new Result(false, null);
    }

    public static Result success(MutableData mutableData) {
        return new Result(true, mutableData.getNode());
    }

    public static interface Handler {
        public Result doTransaction(MutableData var1);

        public void onComplete(DatabaseError var1, boolean var2, DataSnapshot var3);
    }

    public static class Result {
        private Node data;
        private boolean success;

        private Result(boolean bl, Node node) {
            this.success = bl;
            this.data = node;
        }

        public Node getNode() {
            return this.data;
        }

        public boolean isSuccess() {
            return this.success;
        }
    }
}

