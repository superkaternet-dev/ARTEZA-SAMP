/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.WriteTreeRef;
import com.google.firebase.database.core.operation.Operation;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.core.view.CancelEvent;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.DataEvent;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.core.view.EventGenerator;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.core.view.ViewCache;
import com.google.firebase.database.core.view.ViewProcessor;
import com.google.firebase.database.core.view.filter.IndexedFilter;
import com.google.firebase.database.core.view.filter.NodeFilter;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class View {
    private final EventGenerator eventGenerator;
    private final List<EventRegistration> eventRegistrations;
    private final ViewProcessor processor;
    private final QuerySpec query;
    private ViewCache viewCache;

    public View(QuerySpec querySpec, ViewCache object) {
        this.query = querySpec;
        IndexedFilter indexedFilter = new IndexedFilter(querySpec.getIndex());
        NodeFilter nodeFilter = querySpec.getParams().getNodeFilter();
        this.processor = new ViewProcessor(nodeFilter);
        CacheNode cacheNode = ((ViewCache)object).getServerCache();
        object = ((ViewCache)object).getEventCache();
        IndexedNode indexedNode = IndexedNode.from(EmptyNode.Empty(), querySpec.getIndex());
        IndexedNode indexedNode2 = indexedFilter.updateFullNode(indexedNode, cacheNode.getIndexedNode(), null);
        indexedNode = nodeFilter.updateFullNode(indexedNode, ((CacheNode)object).getIndexedNode(), null);
        cacheNode = new CacheNode(indexedNode2, cacheNode.isFullyInitialized(), indexedFilter.filtersNodes());
        this.viewCache = new ViewCache(new CacheNode(indexedNode, ((CacheNode)object).isFullyInitialized(), nodeFilter.filtersNodes()), cacheNode);
        this.eventRegistrations = new ArrayList<EventRegistration>();
        this.eventGenerator = new EventGenerator(querySpec);
    }

    private List<DataEvent> generateEventsForChanges(List<Change> list, IndexedNode indexedNode, EventRegistration list2) {
        list2 = list2 == null ? this.eventRegistrations : Arrays.asList(list2);
        return this.eventGenerator.generateEventsForChanges(list, indexedNode, list2);
    }

    public void addEventRegistration(EventRegistration eventRegistration) {
        this.eventRegistrations.add(eventRegistration);
    }

    public OperationResult applyOperation(Operation object, WriteTreeRef writeTreeRef, Node node) {
        boolean bl;
        block5: {
            block4: {
                Operation.OperationType operationType = ((Operation)object).getType();
                Object object2 = Operation.OperationType.Merge;
                boolean bl2 = false;
                if (operationType == object2 && ((Operation)object).getSource().getQueryParams() != null) {
                    bl = this.viewCache.getCompleteServerSnap() != null;
                    Utilities.hardAssert(bl, "We should always have a full cache before handling merges");
                    bl = this.viewCache.getCompleteEventSnap() != null;
                    Utilities.hardAssert(bl, "Missing event cache, even though we have a server cache");
                }
                object2 = this.viewCache;
                object = this.processor.applyOperation((ViewCache)object2, (Operation)object, writeTreeRef, node);
                if (((ViewProcessor.ProcessorResult)object).viewCache.getServerCache().isFullyInitialized()) break block4;
                bl = bl2;
                if (((ViewCache)object2).getServerCache().isFullyInitialized()) break block5;
            }
            bl = true;
        }
        Utilities.hardAssert(bl, "Once a server snap is complete, it should never go back");
        this.viewCache = ((ViewProcessor.ProcessorResult)object).viewCache;
        return new OperationResult(this.generateEventsForChanges(((ViewProcessor.ProcessorResult)object).changes, ((ViewProcessor.ProcessorResult)object).viewCache.getEventCache().getIndexedNode(), null), ((ViewProcessor.ProcessorResult)object).changes);
    }

    public Node getCompleteNode() {
        return this.viewCache.getCompleteEventSnap();
    }

    public Node getCompleteServerCache(Path path) {
        Node node = this.viewCache.getCompleteServerSnap();
        if (node != null && (this.query.loadsAllData() || !path.isEmpty() && !node.getImmediateChild(path.getFront()).isEmpty())) {
            return node.getChild(path);
        }
        return null;
    }

    public Node getEventCache() {
        return this.viewCache.getEventCache().getNode();
    }

    List<EventRegistration> getEventRegistrations() {
        return this.eventRegistrations;
    }

    public List<DataEvent> getInitialEvents(EventRegistration eventRegistration) {
        CacheNode cacheNode = this.viewCache.getEventCache();
        ArrayList<Change> arrayList = new ArrayList<Change>();
        for (NamedNode namedNode : cacheNode.getNode()) {
            arrayList.add(Change.childAddedChange(namedNode.getName(), namedNode.getNode()));
        }
        if (cacheNode.isFullyInitialized()) {
            arrayList.add(Change.valueChange(cacheNode.getIndexedNode()));
        }
        return this.generateEventsForChanges(arrayList, cacheNode.getIndexedNode(), eventRegistration);
    }

    public QuerySpec getQuery() {
        return this.query;
    }

    public Node getServerCache() {
        return this.viewCache.getServerCache().getNode();
    }

    public boolean isEmpty() {
        return this.eventRegistrations.isEmpty();
    }

    public List<Event> removeEventRegistration(EventRegistration object, DatabaseError object2) {
        Object object3;
        if (object2 != null) {
            object3 = new ArrayList();
            boolean bl = object == null;
            Utilities.hardAssert(bl, "A cancel should cancel all event registrations");
            Path path = this.query.getPath();
            Iterator<EventRegistration> iterator2 = this.eventRegistrations.iterator();
            while (iterator2.hasNext()) {
                object3.add(new CancelEvent(iterator2.next(), (DatabaseError)object2, path));
            }
            object2 = object3;
        } else {
            object2 = Collections.emptyList();
        }
        if (object != null) {
            int n;
            int n2 = -1;
            int n3 = 0;
            while (true) {
                n = n2;
                if (n3 >= this.eventRegistrations.size()) break;
                object3 = this.eventRegistrations.get(n3);
                n = n2;
                if (((EventRegistration)object3).isSameListener((EventRegistration)object)) {
                    n = n2 = n3;
                    if (((EventRegistration)object3).isZombied()) {
                        n = n2;
                        break;
                    }
                }
                ++n3;
                n2 = n;
            }
            if (n != -1) {
                object = this.eventRegistrations.get(n);
                this.eventRegistrations.remove(n);
                ((EventRegistration)object).zombify();
            }
        } else {
            object = this.eventRegistrations.iterator();
            while (object.hasNext()) {
                ((EventRegistration)object.next()).zombify();
            }
            this.eventRegistrations.clear();
        }
        return object2;
    }

    public static class OperationResult {
        public final List<Change> changes;
        public final List<DataEvent> events;

        public OperationResult(List<DataEvent> list, List<Change> list2) {
            this.events = list;
            this.changes = list2;
        }
    }
}

