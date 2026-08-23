/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.WriteTreeRef;
import com.google.firebase.database.core.operation.Operation;
import com.google.firebase.database.core.persistence.PersistenceManager;
import com.google.firebase.database.core.utilities.Pair;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.DataEvent;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.core.view.QueryParams;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.core.view.View;
import com.google.firebase.database.core.view.ViewCache;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncPoint {
    private final PersistenceManager persistenceManager;
    private final Map<QueryParams, View> views = new HashMap<QueryParams, View>();

    public SyncPoint(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    private List<DataEvent> applyOperationToView(View view, Operation object4, WriteTreeRef object2, Node object3) {
        Event.EventType eventType;
        View.OperationResult operationResult;
        operationResult = view.applyOperation((Operation)object4, (WriteTreeRef)((Object)operationResult), (Node)((Object)eventType));
        if (!view.getQuery().loadsAllData()) {
            HashSet<ChildKey> hashSet = new HashSet<ChildKey>();
            HashSet<ChildKey> hashSet2 = new HashSet<ChildKey>();
            for (Change change : operationResult.changes) {
                eventType = change.getEventType();
                if (eventType == Event.EventType.CHILD_ADDED) {
                    hashSet2.add(change.getChildKey());
                    continue;
                }
                if (eventType != Event.EventType.CHILD_REMOVED) continue;
                hashSet.add(change.getChildKey());
            }
            if (!hashSet2.isEmpty() || !hashSet.isEmpty()) {
                this.persistenceManager.updateTrackedQueryKeys(view.getQuery(), hashSet2, hashSet);
            }
        }
        return operationResult.events;
    }

    public List<DataEvent> addEventRegistration(EventRegistration eventRegistration, WriteTreeRef object, CacheNode object2) {
        QuerySpec querySpec = eventRegistration.getQuerySpec();
        View view = this.getView(querySpec, (WriteTreeRef)object, (CacheNode)object2);
        if (!querySpec.loadsAllData()) {
            object = new HashSet();
            object2 = view.getEventCache().iterator();
            while (object2.hasNext()) {
                object.add(((NamedNode)object2.next()).getName());
            }
            this.persistenceManager.setTrackedQueryKeys(querySpec, (Set<ChildKey>)object);
        }
        if (!this.views.containsKey(querySpec.getParams())) {
            this.views.put(querySpec.getParams(), view);
        }
        this.views.put(querySpec.getParams(), view);
        view.addEventRegistration(eventRegistration);
        return view.getInitialEvents(eventRegistration);
    }

    public List<DataEvent> applyOperation(Operation operation, WriteTreeRef writeTreeRef, Node node) {
        Object object = operation.getSource().getQueryParams();
        if (object != null) {
            boolean bl = (object = this.views.get(object)) != null;
            Utilities.hardAssert(bl);
            return this.applyOperationToView((View)object, operation, writeTreeRef, node);
        }
        object = new ArrayList();
        Iterator<Map.Entry<QueryParams, View>> iterator2 = this.views.entrySet().iterator();
        while (iterator2.hasNext()) {
            object.addAll(this.applyOperationToView(iterator2.next().getValue(), operation, writeTreeRef, node));
        }
        return object;
    }

    public Node getCompleteServerCache(Path path) {
        for (View view : this.views.values()) {
            if (view.getCompleteServerCache(path) == null) continue;
            return view.getCompleteServerCache(path);
        }
        return null;
    }

    public View getCompleteView() {
        Iterator<Map.Entry<QueryParams, View>> iterator2 = this.views.entrySet().iterator();
        while (iterator2.hasNext()) {
            View view = iterator2.next().getValue();
            if (!view.getQuery().loadsAllData()) continue;
            return view;
        }
        return null;
    }

    public List<View> getQueryViews() {
        ArrayList<View> arrayList = new ArrayList<View>();
        Iterator<Map.Entry<QueryParams, View>> iterator2 = this.views.entrySet().iterator();
        while (iterator2.hasNext()) {
            View view = iterator2.next().getValue();
            if (view.getQuery().loadsAllData()) continue;
            arrayList.add(view);
        }
        return arrayList;
    }

    public View getView(QuerySpec querySpec, WriteTreeRef object, CacheNode cacheNode) {
        Object object2 = this.views.get(querySpec.getParams());
        if (object2 == null) {
            boolean bl;
            object2 = cacheNode.isFullyInitialized() ? cacheNode.getNode() : null;
            if ((object2 = ((WriteTreeRef)object).calcCompleteEventCache((Node)object2)) != null) {
                bl = true;
                object = object2;
            } else {
                object2 = cacheNode.getNode() != null ? cacheNode.getNode() : EmptyNode.Empty();
                object = ((WriteTreeRef)object).calcCompleteEventChildren((Node)object2);
                bl = false;
            }
            return new View(querySpec, new ViewCache(new CacheNode(IndexedNode.from((Node)object, querySpec.getIndex()), bl, false), cacheNode));
        }
        return object2;
    }

    Map<QueryParams, View> getViews() {
        return this.views;
    }

    public boolean hasCompleteView() {
        boolean bl = this.getCompleteView() != null;
        return bl;
    }

    public boolean isEmpty() {
        return this.views.isEmpty();
    }

    public Pair<List<QuerySpec>, List<Event>> removeEventRegistration(QuerySpec querySpec, EventRegistration eventRegistration, DatabaseError databaseError) {
        ArrayList<QuerySpec> arrayList = new ArrayList<QuerySpec>();
        ArrayList<Event> arrayList2 = new ArrayList<Event>();
        boolean bl = this.hasCompleteView();
        if (querySpec.isDefault()) {
            Iterator<Map.Entry<QueryParams, View>> iterator2 = this.views.entrySet().iterator();
            while (iterator2.hasNext()) {
                View view = iterator2.next().getValue();
                arrayList2.addAll(view.removeEventRegistration(eventRegistration, databaseError));
                if (!view.isEmpty()) continue;
                iterator2.remove();
                if (view.getQuery().loadsAllData()) continue;
                arrayList.add(view.getQuery());
            }
        } else {
            View view = this.views.get(querySpec.getParams());
            if (view != null) {
                arrayList2.addAll(view.removeEventRegistration(eventRegistration, databaseError));
                if (view.isEmpty()) {
                    this.views.remove(querySpec.getParams());
                    if (!view.getQuery().loadsAllData()) {
                        arrayList.add(view.getQuery());
                    }
                }
            }
        }
        if (bl && !this.hasCompleteView()) {
            arrayList.add(QuerySpec.defaultQueryAtPath(querySpec.getPath()));
        }
        return new Pair<List<QuerySpec>, List<Event>>(arrayList, arrayList2);
    }

    public boolean viewExistsForQuery(QuerySpec querySpec) {
        boolean bl = this.viewForQuery(querySpec) != null;
        return bl;
    }

    public View viewForQuery(QuerySpec querySpec) {
        if (querySpec.loadsAllData()) {
            return this.getCompleteView();
        }
        return this.views.get(querySpec.getParams());
    }
}

