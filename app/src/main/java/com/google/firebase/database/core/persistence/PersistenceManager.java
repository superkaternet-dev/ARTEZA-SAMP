/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.persistence;

import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Node;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public interface PersistenceManager {
    public void applyUserWriteToServerCache(Path var1, CompoundWrite var2);

    public void applyUserWriteToServerCache(Path var1, Node var2);

    public List<UserWriteRecord> loadUserWrites();

    public void removeAllUserWrites();

    public void removeUserWrite(long var1);

    public <T> T runInTransaction(Callable<T> var1);

    public void saveUserMerge(Path var1, CompoundWrite var2, long var3);

    public void saveUserOverwrite(Path var1, Node var2, long var3);

    public CacheNode serverCache(QuerySpec var1);

    public void setQueryActive(QuerySpec var1);

    public void setQueryComplete(QuerySpec var1);

    public void setQueryInactive(QuerySpec var1);

    public void setTrackedQueryKeys(QuerySpec var1, Set<ChildKey> var2);

    public void updateServerCache(Path var1, CompoundWrite var2);

    public void updateServerCache(QuerySpec var1, Node var2);

    public void updateTrackedQueryKeys(QuerySpec var1, Set<ChildKey> var2, Set<ChildKey> var3);
}

