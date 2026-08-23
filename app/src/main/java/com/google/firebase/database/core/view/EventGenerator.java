/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.DataEvent;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class EventGenerator {
    private final Index index;
    private final QuerySpec query;

    public EventGenerator(QuerySpec querySpec) {
        this.query = querySpec;
        this.index = querySpec.getIndex();
    }

    private Comparator<Change> changeComparator() {
        return new Comparator<Change>(this){
            final EventGenerator this$0;
            {
                this.this$0 = eventGenerator;
            }

            @Override
            public int compare(Change object, Change object2) {
                boolean bl = ((Change)object).getChildKey() != null && ((Change)object2).getChildKey() != null;
                Utilities.hardAssert(bl);
                object = new NamedNode(((Change)object).getChildKey(), ((Change)object).getIndexedNode().getNode());
                object2 = new NamedNode(((Change)object2).getChildKey(), ((Change)object2).getIndexedNode().getNode());
                return this.this$0.index.compare(object, object2);
            }
        };
    }

    private DataEvent generateEvent(Change change, EventRegistration eventRegistration, IndexedNode indexedNode) {
        block0: {
            if (change.getEventType().equals((Object)Event.EventType.VALUE) || change.getEventType().equals((Object)Event.EventType.CHILD_REMOVED)) break block0;
            change = change.changeWithPrevName(indexedNode.getPredecessorChildName(change.getChildKey(), change.getIndexedNode().getNode(), this.index));
        }
        return eventRegistration.createEvent(change, this.query);
    }

    /*
     * WARNING - void declaration
     */
    private void generateEventsForType(List<DataEvent> list, Event.EventType eventType, List<Change> object3, List<EventRegistration> list2, IndexedNode indexedNode) {
        ArrayList<Change> arrayList = new ArrayList<Change>();
        Object object = object3.iterator();
        while (object.hasNext()) {
            Change change = (Change)object.next();
            if (!change.getEventType().equals((Object)eventType)) continue;
            arrayList.add(change);
        }
        Collections.sort(arrayList, this.changeComparator());
        Iterator iterator2 = arrayList.iterator();
        while (iterator2.hasNext()) {
            void var4_8;
            object = (Change)iterator2.next();
            for (EventRegistration eventRegistration : var4_8) {
                void var5_9;
                if (!eventRegistration.respondsTo(eventType)) continue;
                list.add(this.generateEvent((Change)object, eventRegistration, (IndexedNode)var5_9));
            }
        }
    }

    public List<DataEvent> generateEventsForChanges(List<Change> list, IndexedNode indexedNode, List<EventRegistration> list2) {
        ArrayList<DataEvent> arrayList = new ArrayList<DataEvent>();
        ArrayList<Change> arrayList2 = new ArrayList<Change>();
        for (Change change : list) {
            if (!change.getEventType().equals((Object)Event.EventType.CHILD_CHANGED) || !this.index.indexedValueChanged(change.getOldIndexedNode().getNode(), change.getIndexedNode().getNode())) continue;
            arrayList2.add(Change.childMovedChange(change.getChildKey(), change.getIndexedNode()));
        }
        this.generateEventsForType(arrayList, Event.EventType.CHILD_REMOVED, list, list2, indexedNode);
        this.generateEventsForType(arrayList, Event.EventType.CHILD_ADDED, list, list2, indexedNode);
        this.generateEventsForType(arrayList, Event.EventType.CHILD_MOVED, arrayList2, list2, indexedNode);
        this.generateEventsForType(arrayList, Event.EventType.CHILD_CHANGED, list, list2, indexedNode);
        this.generateEventsForType(arrayList, Event.EventType.VALUE, list, list2, indexedNode);
        return arrayList;
    }
}

