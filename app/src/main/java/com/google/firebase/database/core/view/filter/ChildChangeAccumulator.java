/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view.filter;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.snapshot.ChildKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildChangeAccumulator {
    private final Map<ChildKey, Change> changeMap = new HashMap<ChildKey, Change>();

    public List<Change> getChanges() {
        return new ArrayList<Change>(this.changeMap.values());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void trackChildChange(Change change) {
        Object object = change.getEventType();
        ChildKey childKey = change.getChildKey();
        boolean bl = object == Event.EventType.CHILD_ADDED || object == Event.EventType.CHILD_CHANGED || object == Event.EventType.CHILD_REMOVED;
        Utilities.hardAssert(bl, "Only child changes supported for tracking");
        Utilities.hardAssert(change.getChildKey().isPriorityChildName() ^ true);
        if (!this.changeMap.containsKey(childKey)) {
            this.changeMap.put(change.getChildKey(), change);
            return;
        }
        Change change2 = this.changeMap.get(childKey);
        Event.EventType eventType = change2.getEventType();
        if (object == Event.EventType.CHILD_ADDED && eventType == Event.EventType.CHILD_REMOVED) {
            this.changeMap.put(change.getChildKey(), Change.childChangedChange(childKey, change.getIndexedNode(), change2.getIndexedNode()));
            return;
        }
        if (object == Event.EventType.CHILD_REMOVED && eventType == Event.EventType.CHILD_ADDED) {
            this.changeMap.remove(childKey);
            return;
        }
        if (object == Event.EventType.CHILD_REMOVED && eventType == Event.EventType.CHILD_CHANGED) {
            this.changeMap.put(childKey, Change.childRemovedChange(childKey, change2.getOldIndexedNode()));
            return;
        }
        if (object == Event.EventType.CHILD_CHANGED && eventType == Event.EventType.CHILD_ADDED) {
            this.changeMap.put(childKey, Change.childAddedChange(childKey, change.getIndexedNode()));
            return;
        }
        if (object == Event.EventType.CHILD_CHANGED && eventType == Event.EventType.CHILD_CHANGED) {
            this.changeMap.put(childKey, Change.childChangedChange(childKey, change.getIndexedNode(), change2.getOldIndexedNode()));
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Illegal combination of changes: ");
        ((StringBuilder)object).append(change);
        ((StringBuilder)object).append(" occurred after ");
        ((StringBuilder)object).append(change2);
        throw new IllegalStateException(((StringBuilder)object).toString());
    }
}

