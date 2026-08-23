/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.snapshot.ChildKey;
import java.util.HashMap;
import java.util.Map;

public class TreeNode<T> {
    public Map<ChildKey, TreeNode<T>> children = new HashMap<ChildKey, TreeNode<T>>();
    public T value;

    String toString(String string2) {
        Object object = new StringBuilder();
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append("<value>: ");
        ((StringBuilder)object).append(this.value);
        ((StringBuilder)object).append("\n");
        object = ((StringBuilder)object).toString();
        if (this.children.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append(string2);
            stringBuilder.append("<empty>");
            return stringBuilder.toString();
        }
        for (Map.Entry<ChildKey, TreeNode<T>> entry : this.children.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append(string2);
            stringBuilder.append(entry.getKey());
            stringBuilder.append(":\n");
            object = entry.getValue();
            StringBuilder object2 = new StringBuilder();
            object2.append(string2);
            object2.append("\t");
            stringBuilder.append(((TreeNode)object).toString(object2.toString()));
            stringBuilder.append("\n");
            object = stringBuilder.toString();
        }
        return object;
    }
}

