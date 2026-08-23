/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.persistence;

import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.database.core.persistence.CachePolicy;
import com.google.firebase.database.core.persistence.PersistenceManager;
import com.google.firebase.database.core.persistence.PersistenceStorageEngine;
import com.google.firebase.database.core.persistence.PruneForest;
import com.google.firebase.database.core.persistence.TrackedQuery;
import com.google.firebase.database.core.persistence.TrackedQueryManager;
import com.google.firebase.database.core.utilities.Clock;
import com.google.firebase.database.core.utilities.DefaultClock;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.Node;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class DefaultPersistenceManager
implements PersistenceManager {
    private final CachePolicy cachePolicy;
    private final LogWrapper logger;
    private long serverCacheUpdatesSinceLastPruneCheck = 0L;
    private final PersistenceStorageEngine storageLayer;
    private final TrackedQueryManager trackedQueryManager;

    public DefaultPersistenceManager(Context context, PersistenceStorageEngine persistenceStorageEngine, CachePolicy cachePolicy) {
        this(context, persistenceStorageEngine, cachePolicy, new DefaultClock());
    }

    public DefaultPersistenceManager(Context object, PersistenceStorageEngine persistenceStorageEngine, CachePolicy cachePolicy, Clock clock) {
        this.storageLayer = persistenceStorageEngine;
        this.logger = object = ((Context)object).getLogger("Persistence");
        this.trackedQueryManager = new TrackedQueryManager(persistenceStorageEngine, (LogWrapper)object, clock);
        this.cachePolicy = cachePolicy;
    }

    private void doPruneCheckAfterServerUpdate() {
        long l;
        this.serverCacheUpdatesSinceLastPruneCheck = l = this.serverCacheUpdatesSinceLastPruneCheck + 1L;
        if (this.cachePolicy.shouldCheckCacheSize(l)) {
            Object object;
            Object object2;
            if (this.logger.logsDebug()) {
                this.logger.debug("Reached prune check threshold.", new Object[0]);
            }
            this.serverCacheUpdatesSinceLastPruneCheck = 0L;
            boolean bl = true;
            long l2 = this.storageLayer.serverCacheEstimatedSizeInBytes();
            boolean bl2 = bl;
            l = l2;
            if (this.logger.logsDebug()) {
                object2 = this.logger;
                object = new StringBuilder();
                ((StringBuilder)object).append("Cache size: ");
                ((StringBuilder)object).append(l2);
                ((LogWrapper)object2).debug(((StringBuilder)object).toString(), new Object[0]);
                l = l2;
                bl2 = bl;
            }
            while (bl2 && this.cachePolicy.shouldPrune(l, this.trackedQueryManager.countOfPrunableQueries())) {
                object2 = this.trackedQueryManager.pruneOldQueries(this.cachePolicy);
                if (((PruneForest)object2).prunesAnything()) {
                    this.storageLayer.pruneCache(Path.getEmptyPath(), (PruneForest)object2);
                } else {
                    bl2 = false;
                }
                l = this.storageLayer.serverCacheEstimatedSizeInBytes();
                if (!this.logger.logsDebug()) continue;
                object = this.logger;
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Cache size after prune: ");
                ((StringBuilder)object2).append(l);
                ((LogWrapper)object).debug(((StringBuilder)object2).toString(), new Object[0]);
            }
        }
    }

    @Override
    public void applyUserWriteToServerCache(Path path, CompoundWrite object) {
        Iterator<Map.Entry<Path, Node>> iterator2 = ((CompoundWrite)object).iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            this.applyUserWriteToServerCache(path.child((Path)object.getKey()), (Node)object.getValue());
        }
    }

    @Override
    public void applyUserWriteToServerCache(Path path, Node node) {
        if (!this.trackedQueryManager.hasActiveDefaultQuery(path)) {
            this.storageLayer.overwriteServerCache(path, node);
            this.trackedQueryManager.ensureCompleteTrackedQuery(path);
        }
    }

    @Override
    public List<UserWriteRecord> loadUserWrites() {
        return this.storageLayer.loadUserWrites();
    }

    @Override
    public void removeAllUserWrites() {
        this.storageLayer.removeAllUserWrites();
    }

    @Override
    public void removeUserWrite(long l) {
        this.storageLayer.removeUserWrite(l);
    }

    @Override
    public <T> T runInTransaction(Callable<T> callable) {
        this.storageLayer.beginTransaction();
        try {
            callable = callable.call();
            this.storageLayer.setTransactionSuccessful();
            this.storageLayer.endTransaction();
        }
        catch (Throwable throwable) {
            try {
                this.logger.error("Caught Throwable.", throwable);
                RuntimeException runtimeException = new RuntimeException(throwable);
                throw runtimeException;
            }
            catch (Throwable throwable2) {
                this.storageLayer.endTransaction();
                throw throwable2;
            }
        }
        return (T)callable;
    }

    @Override
    public void saveUserMerge(Path path, CompoundWrite compoundWrite, long l) {
        this.storageLayer.saveUserMerge(path, compoundWrite, l);
    }

    @Override
    public void saveUserOverwrite(Path path, Node node, long l) {
        this.storageLayer.saveUserOverwrite(path, node, l);
    }

    @Override
    public CacheNode serverCache(QuerySpec querySpec) {
        Iterable<ChildKey> iterable;
        boolean bl;
        if (this.trackedQueryManager.isQueryComplete(querySpec)) {
            bl = true;
            iterable = this.trackedQueryManager.findTrackedQuery(querySpec);
            iterable = !querySpec.loadsAllData() && iterable != null && ((TrackedQuery)((Object)iterable)).complete ? this.storageLayer.loadTrackedQueryKeys(((TrackedQuery)((Object)iterable)).id) : null;
        } else {
            bl = false;
            iterable = this.trackedQueryManager.getKnownCompleteChildren(querySpec.getPath());
        }
        Node node = this.storageLayer.serverCache(querySpec.getPath());
        if (iterable != null) {
            Comparable<Node> comparable = EmptyNode.Empty();
            Iterator iterator2 = iterable.iterator();
            iterable = comparable;
            while (iterator2.hasNext()) {
                comparable = (ChildKey)iterator2.next();
                iterable = iterable.updateImmediateChild((ChildKey)comparable, node.getImmediateChild((ChildKey)comparable));
            }
            return new CacheNode(IndexedNode.from(iterable, querySpec.getIndex()), bl, true);
        }
        return new CacheNode(IndexedNode.from(node, querySpec.getIndex()), bl, false);
    }

    @Override
    public void setQueryActive(QuerySpec querySpec) {
        this.trackedQueryManager.setQueryActive(querySpec);
    }

    @Override
    public void setQueryComplete(QuerySpec querySpec) {
        if (querySpec.loadsAllData()) {
            this.trackedQueryManager.setQueriesComplete(querySpec.getPath());
        } else {
            this.trackedQueryManager.setQueryCompleteIfExists(querySpec);
        }
    }

    @Override
    public void setQueryInactive(QuerySpec querySpec) {
        this.trackedQueryManager.setQueryInactive(querySpec);
    }

    @Override
    public void setTrackedQueryKeys(QuerySpec object, Set<ChildKey> set) {
        boolean bl = ((QuerySpec)object).loadsAllData();
        boolean bl2 = true;
        Utilities.hardAssert(bl ^ true, "We should only track keys for filtered queries.");
        object = this.trackedQueryManager.findTrackedQuery((QuerySpec)object);
        if (object == null || !((TrackedQuery)object).active) {
            bl2 = false;
        }
        Utilities.hardAssert(bl2, "We only expect tracked keys for currently-active queries.");
        this.storageLayer.saveTrackedQueryKeys(((TrackedQuery)object).id, set);
    }

    @Override
    public void updateServerCache(Path path, CompoundWrite compoundWrite) {
        this.storageLayer.mergeIntoServerCache(path, compoundWrite);
        this.doPruneCheckAfterServerUpdate();
    }

    @Override
    public void updateServerCache(QuerySpec querySpec, Node node) {
        if (querySpec.loadsAllData()) {
            this.storageLayer.overwriteServerCache(querySpec.getPath(), node);
        } else {
            this.storageLayer.mergeIntoServerCache(querySpec.getPath(), node);
        }
        this.setQueryComplete(querySpec);
        this.doPruneCheckAfterServerUpdate();
    }

    @Override
    public void updateTrackedQueryKeys(QuerySpec object, Set<ChildKey> set, Set<ChildKey> set2) {
        boolean bl = ((QuerySpec)object).loadsAllData();
        boolean bl2 = true;
        Utilities.hardAssert(bl ^ true, "We should only track keys for filtered queries.");
        object = this.trackedQueryManager.findTrackedQuery((QuerySpec)object);
        if (object == null || !((TrackedQuery)object).active) {
            bl2 = false;
        }
        Utilities.hardAssert(bl2, "We only expect tracked keys for currently-active queries.");
        this.storageLayer.updateTrackedQueryKeys(((TrackedQuery)object).id, set, set2);
    }
}

