/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.persistence;

import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.database.core.persistence.PruneForest;
import com.google.firebase.database.core.persistence.TrackedQuery;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Node;
import java.util.List;
import java.util.Set;

public interface PersistenceStorageEngine {
    public void beginTransaction();

    public void close();

    public void deleteTrackedQuery(long var1);

    public void endTransaction();

    public List<TrackedQuery> loadTrackedQueries();

    public Set<ChildKey> loadTrackedQueryKeys(long var1);

    public Set<ChildKey> loadTrackedQueryKeys(Set<Long> var1);

    public List<UserWriteRecord> loadUserWrites();

    public void mergeIntoServerCache(Path var1, CompoundWrite var2);

    public void mergeIntoServerCache(Path var1, Node var2);

    public void overwriteServerCache(Path var1, Node var2);

    public void pruneCache(Path var1, PruneForest var2);

    public void removeAllUserWrites();

    public void removeUserWrite(long var1);

    public void resetPreviouslyActiveTrackedQueries(long var1);

    public void saveTrackedQuery(TrackedQuery var1);

    public void saveTrackedQueryKeys(long var1, Set<ChildKey> var3);

    public void saveUserMerge(Path var1, CompoundWrite var2, long var3);

    public void saveUserOverwrite(Path var1, Node var2, long var3);

    public Node serverCache(Path var1);

    public long serverCacheEstimatedSizeInBytes();

    public void setTransactionSuccessful();

    public void updateTrackedQueryKeys(long var1, Set<ChildKey> var3, Set<ChildKey> var4);
}

