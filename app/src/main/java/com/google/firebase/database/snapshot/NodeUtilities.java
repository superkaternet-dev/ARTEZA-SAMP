/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.collection.ImmutableSortedMap;
import com.google.firebase.database.snapshot.BooleanNode;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.DeferredValueNode;
import com.google.firebase.database.snapshot.DoubleNode;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.LongNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PriorityUtilities;
import com.google.firebase.database.snapshot.StringNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeUtilities {
    public static Node NodeFromJSON(Object object) throws DatabaseException {
        return NodeUtilities.NodeFromJSON(object, PriorityUtilities.NullPriority());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Node NodeFromJSON(Object object, Node object2) throws DatabaseException {
        Object object32 = object;
        HashMap hashMap = object2;
        try {
            Object object4;
            if (object instanceof Map) {
                object4 = (Map)object;
                if (object4.containsKey(".priority")) {
                    object2 = PriorityUtilities.parsePriority(object4.get(".priority"));
                }
                object32 = object;
                hashMap = object2;
                if (object4.containsKey(".value")) {
                    object32 = object4.get(".value");
                    hashMap = object2;
                }
            }
            if (object32 == null) {
                return EmptyNode.Empty();
            }
            if (object32 instanceof String) {
                return new StringNode((String)object32, (Node)((Object)hashMap));
            }
            if (object32 instanceof Long) {
                return new LongNode((Long)object32, (Node)((Object)hashMap));
            }
            if (object32 instanceof Integer) {
                return new LongNode((long)((Integer)object32), (Node)((Object)hashMap));
            }
            if (object32 instanceof Double) {
                return new DoubleNode((Double)object32, (Node)((Object)hashMap));
            }
            if (object32 instanceof Boolean) {
                return new BooleanNode((Boolean)object32, (Node)((Object)hashMap));
            }
            if (!(object32 instanceof Map) && !(object32 instanceof List)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Failed to parse node with class ");
                ((StringBuilder)object).append(object32.getClass().toString());
                object2 = new DatabaseException(((StringBuilder)object).toString());
                throw object2;
            }
            if (object32 instanceof Map) {
                Map map = (Map)object32;
                if (map.containsKey(".sv")) {
                    return new DeferredValueNode(map, (Node)((Object)hashMap));
                }
                object = new HashMap(map.size());
                for (Object object32 : map.keySet()) {
                    if (((String)object32).startsWith(".") || (object2 = NodeUtilities.NodeFromJSON(map.get(object32))).isEmpty()) continue;
                    object.put(ChildKey.fromString((String)object32), object2);
                }
            } else {
                object32 = (List)object32;
                object2 = new HashMap(object32.size());
                int n = 0;
                while (true) {
                    object = object2;
                    if (n >= object32.size()) break;
                    object = new StringBuilder();
                    ((StringBuilder)object).append("");
                    ((StringBuilder)object).append(n);
                    object4 = ((StringBuilder)object).toString();
                    object = NodeUtilities.NodeFromJSON(object32.get(n));
                    if (!object.isEmpty()) {
                        object2.put(ChildKey.fromString((String)object4), object);
                    }
                    ++n;
                }
            }
            if (!object.isEmpty()) return new ChildrenNode(ImmutableSortedMap.Builder.fromMap(object, ChildrenNode.NAME_ONLY_COMPARATOR), (Node)((Object)hashMap));
            return EmptyNode.Empty();
        }
        catch (ClassCastException classCastException) {
            DatabaseException databaseException = new DatabaseException("Failed to parse node", classCastException);
            throw databaseException;
        }
    }

    public static int nameAndPriorityCompare(ChildKey childKey, Node node, ChildKey childKey2, Node node2) {
        int n = node.compareTo(node2);
        if (n != 0) {
            return n;
        }
        return childKey.compareTo(childKey2);
    }
}

