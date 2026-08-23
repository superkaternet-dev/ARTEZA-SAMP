/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.firebase.database.core.persistence;

import android.util.Log;
import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.database.core.persistence.PersistenceManager;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.Node;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class NoopPersistenceManager
implements PersistenceManager {
    private static final String TAG = "NoopPersistenceManager";
    private boolean insideTransaction = false;

    private void verifyInsideTransaction() {
        Utilities.hardAssert(this.insideTransaction, "Transaction expected to already be in progress.");
    }

    @Override
    public void applyUserWriteToServerCache(Path path, CompoundWrite compoundWrite) {
        this.verifyInsideTransaction();
    }

    @Override
    public void applyUserWriteToServerCache(Path path, Node node) {
        this.verifyInsideTransaction();
    }

    @Override
    public List<UserWriteRecord> loadUserWrites() {
        return Collections.emptyList();
    }

    @Override
    public void removeAllUserWrites() {
        this.verifyInsideTransaction();
    }

    @Override
    public void removeUserWrite(long l) {
        this.verifyInsideTransaction();
    }

    @Override
    public <T> T runInTransaction(Callable<T> object) {
        Utilities.hardAssert(this.insideTransaction ^ true, "runInTransaction called when an existing transaction is already in progress.");
        this.insideTransaction = true;
        try {
            object = object.call();
            this.insideTransaction = false;
        }
        catch (Throwable throwable) {
            try {
                Log.e((String)TAG, (String)"Caught Throwable.", (Throwable)throwable);
                object = new RuntimeException(throwable);
                throw object;
            }
            catch (Throwable throwable2) {
                this.insideTransaction = false;
                throw throwable2;
            }
        }
        return (T)object;
    }

    @Override
    public void saveUserMerge(Path path, CompoundWrite compoundWrite, long l) {
        this.verifyInsideTransaction();
    }

    @Override
    public void saveUserOverwrite(Path path, Node node, long l) {
        this.verifyInsideTransaction();
    }

    @Override
    public CacheNode serverCache(QuerySpec querySpec) {
        return new CacheNode(IndexedNode.from(EmptyNode.Empty(), querySpec.getIndex()), false, false);
    }

    @Override
    public void setQueryActive(QuerySpec querySpec) {
        this.verifyInsideTransaction();
    }

    @Override
    public void setQueryComplete(QuerySpec querySpec) {
        this.verifyInsideTransaction();
    }

    @Override
    public void setQueryInactive(QuerySpec querySpec) {
        this.verifyInsideTransaction();
    }

    @Override
    public void setTrackedQueryKeys(QuerySpec querySpec, Set<ChildKey> set) {
        this.verifyInsideTransaction();
    }

    @Override
    public void updateServerCache(Path path, CompoundWrite compoundWrite) {
        this.verifyInsideTransaction();
    }

    @Override
    public void updateServerCache(QuerySpec querySpec, Node node) {
        this.verifyInsideTransaction();
    }

    @Override
    public void updateTrackedQueryKeys(QuerySpec querySpec, Set<ChildKey> set, Set<ChildKey> set2) {
        this.verifyInsideTransaction();
    }
}

