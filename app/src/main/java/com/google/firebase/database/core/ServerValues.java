/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.SnapshotHolder;
import com.google.firebase.database.core.SyncTree;
import com.google.firebase.database.core.ValueProvider;
import com.google.firebase.database.core.utilities.Clock;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerValues {
    public static final String NAME_OP_INCREMENT = "increment";
    public static final String NAME_OP_TIMESTAMP = "timestamp";
    public static final String NAME_SUBKEY_SERVERVALUE = ".sv";

    private static boolean canBeRepresentedAsLong(Number number) {
        boolean bl = !(number instanceof Double) && !(number instanceof Float);
        return bl;
    }

    public static Map<String, Object> generateServerValues(Clock clock) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(NAME_OP_TIMESTAMP, clock.millis());
        return hashMap;
    }

    static Object resolveComplexDeferredValue(Map<String, Object> object, ValueProvider object2, Map<String, Object> map) {
        if (!object.containsKey(NAME_OP_INCREMENT)) {
            return null;
        }
        if (!((object = object.get(NAME_OP_INCREMENT)) instanceof Number)) {
            return null;
        }
        object = (Number)object;
        if ((object2 = ((ValueProvider)object2).node()).isLeafNode() && object2.getValue() instanceof Number) {
            long l;
            long l2;
            long l3;
            object2 = (Number)object2.getValue();
            if (ServerValues.canBeRepresentedAsLong((Number)object) && ServerValues.canBeRepresentedAsLong((Number)object2) && (((l3 = ((Number)object).longValue()) ^ (l2 = l3 + (l = ((Number)object2).longValue()))) & (l ^ l2)) >= 0L) {
                return l2;
            }
            return ((Number)object).doubleValue() + ((Number)object2).doubleValue();
        }
        return object;
    }

    public static Object resolveDeferredLeafValue(Object object, ValueProvider valueProvider, Map<String, Object> map) {
        if (!(object instanceof Map)) {
            return object;
        }
        Object object2 = (Map)object;
        if (!object2.containsKey(NAME_SUBKEY_SERVERVALUE)) {
            return object;
        }
        Object v = object2.get(NAME_SUBKEY_SERVERVALUE);
        object2 = null;
        if (v instanceof String) {
            object2 = ServerValues.resolveScalarDeferredValue((String)v, map);
        } else if (v instanceof Map) {
            object2 = ServerValues.resolveComplexDeferredValue((Map)v, valueProvider, map);
        }
        if (object2 == null) {
            return object;
        }
        return object2;
    }

    public static CompoundWrite resolveDeferredValueMerge(CompoundWrite compoundWrite, SyncTree syncTree, Path path, Map<String, Object> map) {
        Object object = CompoundWrite.emptyWrite();
        Iterator<Map.Entry<Path, Node>> iterator2 = compoundWrite.iterator();
        compoundWrite = object;
        while (iterator2.hasNext()) {
            Map.Entry<Path, Node> entry = iterator2.next();
            object = new ValueProvider.DeferredValueProvider(syncTree, path.child(entry.getKey()));
            compoundWrite = compoundWrite.addWrite(entry.getKey(), ServerValues.resolveDeferredValueSnapshot(entry.getValue(), (ValueProvider)object, map));
        }
        return compoundWrite;
    }

    public static Node resolveDeferredValueSnapshot(Node node, SyncTree syncTree, Path path, Map<String, Object> map) {
        return ServerValues.resolveDeferredValueSnapshot(node, new ValueProvider.DeferredValueProvider(syncTree, path), map);
    }

    private static Node resolveDeferredValueSnapshot(Node node, ValueProvider object, Map<String, Object> map) {
        Object object2 = node.getPriority().getValue();
        Object object3 = ServerValues.resolveDeferredLeafValue(object2, ((ValueProvider)object).getImmediateChild(ChildKey.fromString(".priority")), map);
        if (node.isLeafNode()) {
            object = ServerValues.resolveDeferredLeafValue(node.getValue(), (ValueProvider)object, map);
            if (object.equals(node.getValue()) && Utilities.equals(object3, object2)) {
                return node;
            }
            return NodeUtilities.NodeFromJSON(object, PriorityUtilities.parsePriority(object3));
        }
        if (node.isEmpty()) {
            return node;
        }
        node = (ChildrenNode)node;
        object2 = new SnapshotHolder(node);
        ((ChildrenNode)node).forEachChild(new ChildrenNode.ChildVisitor((ValueProvider)object, map, (SnapshotHolder)object2){
            final ValueProvider val$existing;
            final SnapshotHolder val$holder;
            final Map val$serverValues;
            {
                this.val$existing = valueProvider;
                this.val$serverValues = map;
                this.val$holder = snapshotHolder;
            }

            @Override
            public void visitChild(ChildKey childKey, Node node) {
                Node node2 = ServerValues.resolveDeferredValueSnapshot(node, this.val$existing.getImmediateChild(childKey), (Map<String, Object>)this.val$serverValues);
                if (node2 != node) {
                    this.val$holder.update(new Path(childKey.asString()), node2);
                }
            }
        });
        if (!((SnapshotHolder)object2).getRootNode().getPriority().equals(object3)) {
            return ((SnapshotHolder)object2).getRootNode().updatePriority(PriorityUtilities.parsePriority(object3));
        }
        return ((SnapshotHolder)object2).getRootNode();
    }

    public static Node resolveDeferredValueSnapshot(Node node, Node node2, Map<String, Object> map) {
        return ServerValues.resolveDeferredValueSnapshot(node, new ValueProvider.ExistingValueProvider(node2), map);
    }

    static Object resolveScalarDeferredValue(String string2, Map<String, Object> map) {
        if (NAME_OP_TIMESTAMP.equals(string2) && map.containsKey(string2)) {
            return map.get(string2);
        }
        return null;
    }
}

