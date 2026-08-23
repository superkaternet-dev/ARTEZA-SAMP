/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.persistence;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.persistence.CachePolicy;
import com.google.firebase.database.core.persistence.PersistenceStorageEngine;
import com.google.firebase.database.core.persistence.PruneForest;
import com.google.firebase.database.core.persistence.TrackedQuery;
import com.google.firebase.database.core.utilities.Clock;
import com.google.firebase.database.core.utilities.ImmutableTree;
import com.google.firebase.database.core.utilities.Predicate;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.QueryParams;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.snapshot.ChildKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TrackedQueryManager {
    private static final Predicate<Map<QueryParams, TrackedQuery>> HAS_ACTIVE_DEFAULT_PREDICATE;
    private static final Predicate<Map<QueryParams, TrackedQuery>> HAS_DEFAULT_COMPLETE_PREDICATE;
    private static final Predicate<TrackedQuery> IS_QUERY_PRUNABLE_PREDICATE;
    private static final Predicate<TrackedQuery> IS_QUERY_UNPRUNABLE_PREDICATE;
    private final Clock clock;
    private long currentQueryId = 0L;
    private final LogWrapper logger;
    private final PersistenceStorageEngine storageLayer;
    private ImmutableTree<Map<QueryParams, TrackedQuery>> trackedQueryTree;

    static {
        HAS_DEFAULT_COMPLETE_PREDICATE = new Predicate<Map<QueryParams, TrackedQuery>>(){

            @Override
            public boolean evaluate(Map<QueryParams, TrackedQuery> object) {
                boolean bl = (object = object.get(QueryParams.DEFAULT_PARAMS)) != null && ((TrackedQuery)object).complete;
                return bl;
            }
        };
        HAS_ACTIVE_DEFAULT_PREDICATE = new Predicate<Map<QueryParams, TrackedQuery>>(){

            @Override
            public boolean evaluate(Map<QueryParams, TrackedQuery> object) {
                boolean bl = (object = object.get(QueryParams.DEFAULT_PARAMS)) != null && ((TrackedQuery)object).active;
                return bl;
            }
        };
        IS_QUERY_PRUNABLE_PREDICATE = new Predicate<TrackedQuery>(){

            @Override
            public boolean evaluate(TrackedQuery trackedQuery) {
                return trackedQuery.active ^ true;
            }
        };
        IS_QUERY_UNPRUNABLE_PREDICATE = new Predicate<TrackedQuery>(){

            @Override
            public boolean evaluate(TrackedQuery trackedQuery) {
                return IS_QUERY_PRUNABLE_PREDICATE.evaluate(trackedQuery) ^ true;
            }
        };
    }

    /*
     * WARNING - void declaration
     */
    public TrackedQueryManager(PersistenceStorageEngine object, LogWrapper object22, Clock clock) {
        void var3_5;
        this.storageLayer = object;
        this.logger = object22;
        this.clock = var3_5;
        this.trackedQueryTree = new ImmutableTree<Object>(null);
        this.resetPreviouslyActiveTrackedQueries();
        for (TrackedQuery trackedQuery : object.loadTrackedQueries()) {
            this.currentQueryId = Math.max(trackedQuery.id + 1L, this.currentQueryId);
            this.cacheTrackedQuery(trackedQuery);
        }
    }

    private static void assertValidTrackedQuery(QuerySpec querySpec) {
        boolean bl = !querySpec.loadsAllData() || querySpec.isDefault();
        Utilities.hardAssert(bl, "Can't have tracked non-default query that loads all data");
    }

    private void cacheTrackedQuery(TrackedQuery trackedQuery) {
        TrackedQueryManager.assertValidTrackedQuery(trackedQuery.querySpec);
        Object object = this.trackedQueryTree.get(trackedQuery.querySpec.getPath());
        Map<QueryParams, TrackedQuery> map = object;
        if (object == null) {
            map = new HashMap<QueryParams, TrackedQuery>();
            this.trackedQueryTree = this.trackedQueryTree.set(trackedQuery.querySpec.getPath(), map);
        }
        boolean bl = (object = map.get(trackedQuery.querySpec.getParams())) == null || ((TrackedQuery)object).id == trackedQuery.id;
        Utilities.hardAssert(bl);
        map.put(trackedQuery.querySpec.getParams(), trackedQuery);
    }

    private static long calculateCountToPrune(CachePolicy cachePolicy, long l) {
        float f = cachePolicy.getPercentOfQueriesToPruneAtOnce();
        return l - Math.min((long)Math.floor((float)l * (1.0f - f)), cachePolicy.getMaxNumberOfQueriesToKeep());
    }

    private Set<Long> filteredQueryIdsAtPath(Path iterator2) {
        HashSet<Long> hashSet = new HashSet<Long>();
        if ((iterator2 = this.trackedQueryTree.get((Path)((Object)iterator2))) != null) {
            for (TrackedQuery trackedQuery : iterator2.values()) {
                if (trackedQuery.querySpec.loadsAllData()) continue;
                hashSet.add(trackedQuery.id);
            }
        }
        return hashSet;
    }

    private List<TrackedQuery> getQueriesMatching(Predicate<TrackedQuery> predicate) {
        ArrayList<TrackedQuery> arrayList = new ArrayList<TrackedQuery>();
        Iterator<Map.Entry<Path, Map<QueryParams, TrackedQuery>>> iterator2 = this.trackedQueryTree.iterator();
        while (iterator2.hasNext()) {
            for (TrackedQuery trackedQuery : iterator2.next().getValue().values()) {
                if (!predicate.evaluate(trackedQuery)) continue;
                arrayList.add(trackedQuery);
            }
        }
        return arrayList;
    }

    private boolean includedInDefaultCompleteQuery(Path path) {
        boolean bl = this.trackedQueryTree.findRootMostMatchingPath(path, HAS_DEFAULT_COMPLETE_PREDICATE) != null;
        return bl;
    }

    private static QuerySpec normalizeQuery(QuerySpec querySpec) {
        block0: {
            if (!querySpec.loadsAllData()) break block0;
            querySpec = QuerySpec.defaultQueryAtPath(querySpec.getPath());
        }
        return querySpec;
    }

    private void resetPreviouslyActiveTrackedQueries() {
        try {
            this.storageLayer.beginTransaction();
            this.storageLayer.resetPreviouslyActiveTrackedQueries(this.clock.millis());
            this.storageLayer.setTransactionSuccessful();
            return;
        }
        finally {
            this.storageLayer.endTransaction();
        }
    }

    private void saveTrackedQuery(TrackedQuery trackedQuery) {
        this.cacheTrackedQuery(trackedQuery);
        this.storageLayer.saveTrackedQuery(trackedQuery);
    }

    private void setQueryActiveFlag(QuerySpec object, boolean bl) {
        QuerySpec querySpec = TrackedQueryManager.normalizeQuery((QuerySpec)object);
        object = this.findTrackedQuery(querySpec);
        long l = this.clock.millis();
        if (object != null) {
            object = ((TrackedQuery)object).updateLastUse(l).setActiveState(bl);
        } else {
            Utilities.hardAssert(bl, "If we're setting the query to inactive, we should already be tracking it!");
            long l2 = this.currentQueryId;
            this.currentQueryId = 1L + l2;
            object = new TrackedQuery(l2, querySpec, l, false, bl);
        }
        this.saveTrackedQuery((TrackedQuery)object);
    }

    public long countOfPrunableQueries() {
        return this.getQueriesMatching(IS_QUERY_PRUNABLE_PREDICATE).size();
    }

    public void ensureCompleteTrackedQuery(Path object) {
        if (!this.includedInDefaultCompleteQuery((Path)object)) {
            TrackedQuery trackedQuery = this.findTrackedQuery((QuerySpec)(object = QuerySpec.defaultQueryAtPath((Path)object)));
            if (trackedQuery == null) {
                long l = this.currentQueryId;
                this.currentQueryId = 1L + l;
                object = new TrackedQuery(l, (QuerySpec)object, this.clock.millis(), true, false);
            } else {
                Utilities.hardAssert(trackedQuery.complete ^ true, "This should have been handled above!");
                object = trackedQuery.setComplete();
            }
            this.saveTrackedQuery((TrackedQuery)object);
        }
    }

    public TrackedQuery findTrackedQuery(QuerySpec object) {
        QuerySpec querySpec = TrackedQueryManager.normalizeQuery((QuerySpec)object);
        object = (object = this.trackedQueryTree.get(querySpec.getPath())) != null ? (TrackedQuery)object.get(querySpec.getParams()) : null;
        return object;
    }

    public Set<ChildKey> getKnownCompleteChildren(Path object) {
        Utilities.hardAssert(this.isQueryComplete(QuerySpec.defaultQueryAtPath((Path)((Object)object))) ^ true, "Path is fully complete.");
        HashSet<ChildKey> hashSet = new HashSet<ChildKey>();
        Object object2 = this.filteredQueryIdsAtPath((Path)((Object)object));
        if (!object2.isEmpty()) {
            hashSet.addAll(this.storageLayer.loadTrackedQueryKeys((Set<Long>)object2));
        }
        for (Map.Entry<ChildKey, ImmutableTree<Map<QueryParams, TrackedQuery>>> entry : this.trackedQueryTree.subtree((Path)((Object)object)).getChildren()) {
            object2 = entry.getKey();
            ImmutableTree<Map<QueryParams, TrackedQuery>> object3 = entry.getValue();
            if (object3.getValue() == null || !HAS_DEFAULT_COMPLETE_PREDICATE.evaluate(object3.getValue())) continue;
            hashSet.add((ChildKey)object2);
        }
        return hashSet;
    }

    public boolean hasActiveDefaultQuery(Path path) {
        boolean bl = this.trackedQueryTree.rootMostValueMatching(path, HAS_ACTIVE_DEFAULT_PREDICATE) != null;
        return bl;
    }

    public boolean isQueryComplete(QuerySpec querySpec) {
        boolean bl = this.includedInDefaultCompleteQuery(querySpec.getPath());
        boolean bl2 = true;
        if (bl) {
            return true;
        }
        if (querySpec.loadsAllData()) {
            return false;
        }
        Map<QueryParams, TrackedQuery> map = this.trackedQueryTree.get(querySpec.getPath());
        if (map == null || !map.containsKey(querySpec.getParams()) || !map.get((Object)querySpec.getParams()).complete) {
            bl2 = false;
        }
        return bl2;
    }

    public PruneForest pruneOldQueries(CachePolicy object) {
        Object object2;
        Object object3;
        Iterator iterator2 = this.getQueriesMatching(IS_QUERY_PRUNABLE_PREDICATE);
        long l = TrackedQueryManager.calculateCountToPrune((CachePolicy)object, iterator2.size());
        object = new PruneForest();
        if (this.logger.logsDebug()) {
            object3 = this.logger;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Pruning old queries.  Prunable: ");
            ((StringBuilder)object2).append(iterator2.size());
            ((StringBuilder)object2).append(" Count to prune: ");
            ((StringBuilder)object2).append(l);
            ((LogWrapper)object3).debug(((StringBuilder)object2).toString(), new Object[0]);
        }
        Collections.sort(iterator2, new Comparator<TrackedQuery>(this){
            final TrackedQueryManager this$0;
            {
                this.this$0 = trackedQueryManager;
            }

            @Override
            public int compare(TrackedQuery trackedQuery, TrackedQuery trackedQuery2) {
                return Utilities.compareLongs(trackedQuery.lastUse, trackedQuery2.lastUse);
            }
        });
        int n = 0;
        while ((long)n < l) {
            object2 = iterator2.get(n);
            object = ((PruneForest)object).prune(((TrackedQuery)object2).querySpec.getPath());
            this.removeTrackedQuery(((TrackedQuery)object2).querySpec);
            ++n;
        }
        for (n = (int)l; n < iterator2.size(); ++n) {
            object = ((PruneForest)object).keep(iterator2.get((int)n).querySpec.getPath());
        }
        object3 = this.getQueriesMatching(IS_QUERY_UNPRUNABLE_PREDICATE);
        if (this.logger.logsDebug()) {
            iterator2 = this.logger;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Unprunable queries: ");
            ((StringBuilder)object2).append(object3.size());
            ((LogWrapper)((Object)iterator2)).debug(((StringBuilder)object2).toString(), new Object[0]);
        }
        iterator2 = object3.iterator();
        while (iterator2.hasNext()) {
            object = ((PruneForest)object).keep(((TrackedQuery)iterator2.next()).querySpec.getPath());
        }
        return object;
    }

    public void removeTrackedQuery(QuerySpec querySpec) {
        Object object = this.findTrackedQuery(querySpec = TrackedQueryManager.normalizeQuery(querySpec));
        boolean bl = object != null;
        Utilities.hardAssert(bl, "Query must exist to be removed.");
        this.storageLayer.deleteTrackedQuery(((TrackedQuery)object).id);
        object = this.trackedQueryTree.get(querySpec.getPath());
        object.remove(querySpec.getParams());
        if (object.isEmpty()) {
            this.trackedQueryTree = this.trackedQueryTree.remove(querySpec.getPath());
        }
    }

    public void setQueriesComplete(Path path) {
        this.trackedQueryTree.subtree(path).foreach(new ImmutableTree.TreeVisitor<Map<QueryParams, TrackedQuery>, Void>(this){
            final TrackedQueryManager this$0;
            {
                this.this$0 = trackedQueryManager;
            }

            @Override
            public Void onNodeValue(Path object, Map<QueryParams, TrackedQuery> object2, Void void_) {
                object = object2.entrySet().iterator();
                while (object.hasNext()) {
                    object2 = (TrackedQuery)((Map.Entry)object.next()).getValue();
                    if (((TrackedQuery)object2).complete) continue;
                    this.this$0.saveTrackedQuery(((TrackedQuery)object2).setComplete());
                }
                return null;
            }
        });
    }

    public void setQueryActive(QuerySpec querySpec) {
        this.setQueryActiveFlag(querySpec, true);
    }

    public void setQueryCompleteIfExists(QuerySpec object) {
        if ((object = this.findTrackedQuery(TrackedQueryManager.normalizeQuery((QuerySpec)object))) != null && !((TrackedQuery)object).complete) {
            this.saveTrackedQuery(((TrackedQuery)object).setComplete());
        }
    }

    public void setQueryInactive(QuerySpec querySpec) {
        this.setQueryActiveFlag(querySpec, false);
    }

    void verifyCache() {
        List<TrackedQuery> list = this.storageLayer.loadTrackedQueries();
        ArrayList arrayList = new ArrayList();
        this.trackedQueryTree.foreach(new ImmutableTree.TreeVisitor<Map<QueryParams, TrackedQuery>, Void>(this, arrayList){
            final TrackedQueryManager this$0;
            final List val$trackedQueries;
            {
                this.this$0 = trackedQueryManager;
                this.val$trackedQueries = list;
            }

            @Override
            public Void onNodeValue(Path object3, Map<QueryParams, TrackedQuery> object2, Void void_) {
                Iterator iterator2;
                for (TrackedQuery trackedQuery : iterator2.values()) {
                    this.val$trackedQueries.add(trackedQuery);
                }
                return null;
            }
        });
        Collections.sort(arrayList, new Comparator<TrackedQuery>(this){
            final TrackedQueryManager this$0;
            {
                this.this$0 = trackedQueryManager;
            }

            @Override
            public int compare(TrackedQuery trackedQuery, TrackedQuery trackedQuery2) {
                return Utilities.compareLongs(trackedQuery.id, trackedQuery2.id);
            }
        });
        boolean bl = list.equals(arrayList);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tracked queries out of sync.  Tracked queries: ");
        stringBuilder.append(arrayList);
        stringBuilder.append(" Stored queries: ");
        stringBuilder.append(list);
        Utilities.hardAssert(bl, stringBuilder.toString());
    }
}

