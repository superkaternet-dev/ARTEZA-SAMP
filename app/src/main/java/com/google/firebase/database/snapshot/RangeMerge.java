/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RangeMerge {
    private final Path optExclusiveStart;
    private final Path optInclusiveEnd;
    private final Node snap;

    public RangeMerge(com.google.firebase.database.connection.RangeMerge rangeMerge) {
        Iterable<String> iterable = rangeMerge.getOptExclusiveStart();
        Object var3_3 = null;
        iterable = iterable != null ? new Path((List<String>)iterable) : null;
        this.optExclusiveStart = iterable;
        List<String> list = rangeMerge.getOptInclusiveEnd();
        iterable = var3_3;
        if (list != null) {
            iterable = new Path(list);
        }
        this.optInclusiveEnd = iterable;
        this.snap = NodeUtilities.NodeFromJSON(rangeMerge.getSnap());
    }

    public RangeMerge(Path path, Path path2, Node node) {
        this.optExclusiveStart = path;
        this.optInclusiveEnd = path2;
        this.snap = node;
    }

    private Node updateRangeInNode(Path path, Node node, Node node2) {
        Object object = this.optExclusiveStart;
        boolean bl = true;
        int n = object == null ? 1 : path.compareTo((Path)object);
        object = this.optInclusiveEnd;
        int n2 = object == null ? -1 : path.compareTo((Path)object);
        object = this.optExclusiveStart;
        boolean bl2 = object != null && path.contains((Path)object);
        object = this.optInclusiveEnd;
        boolean bl3 = object != null && path.contains((Path)object);
        if (n > 0 && n2 < 0 && !bl3) {
            return node2;
        }
        if (n > 0 && bl3 && node2.isLeafNode()) {
            return node2;
        }
        if (n > 0 && n2 == 0) {
            Utilities.hardAssert(bl3);
            Utilities.hardAssert(true ^ node2.isLeafNode());
            if (node.isLeafNode()) {
                return EmptyNode.Empty();
            }
            return node;
        }
        if (!bl2 && !bl3) {
            bl3 = bl;
            if (n2 <= 0) {
                bl3 = n <= 0 ? bl : false;
            }
            Utilities.hardAssert(bl3);
            return node;
        }
        object = new HashSet();
        Object object2 = node.iterator();
        while (object2.hasNext()) {
            object.add(((NamedNode)object2.next()).getName());
        }
        object2 = node2.iterator();
        while (object2.hasNext()) {
            object.add((ChildKey)((NamedNode)object2.next()).getName());
        }
        Object object3 = new ArrayList<ChildKey>(object.size() + 1);
        object3.addAll(object);
        if (!node2.getPriority().isEmpty() || !node.getPriority().isEmpty()) {
            object3.add(ChildKey.getPriorityKey());
        }
        object2 = node;
        object3 = object3.iterator();
        while (object3.hasNext()) {
            ChildKey childKey = (ChildKey)object3.next();
            Node node3 = node.getImmediateChild(childKey);
            Node node4 = this.updateRangeInNode(path.child(childKey), node.getImmediateChild(childKey), node2.getImmediateChild(childKey));
            object = object2;
            if (node4 != node3) {
                object = object2.updateImmediateChild(childKey, node4);
            }
            object2 = object;
        }
        return object2;
    }

    public Node applyTo(Node node) {
        return this.updateRangeInNode(Path.getEmptyPath(), node, this.snap);
    }

    Path getEnd() {
        return this.optInclusiveEnd;
    }

    Path getStart() {
        return this.optExclusiveStart;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RangeMerge{optExclusiveStart=");
        stringBuilder.append(this.optExclusiveStart);
        stringBuilder.append(", optInclusiveEnd=");
        stringBuilder.append(this.optInclusiveEnd);
        stringBuilder.append(", snap=");
        stringBuilder.append(this.snap);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

