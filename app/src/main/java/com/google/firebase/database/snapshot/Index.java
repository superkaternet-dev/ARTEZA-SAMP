/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.KeyIndex;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PathIndex;
import com.google.firebase.database.snapshot.ValueIndex;
import java.util.Comparator;

public abstract class Index
implements Comparator<NamedNode> {
    public static Index fromQueryDefinition(String string2) {
        if (string2.equals(".value")) {
            return ValueIndex.getInstance();
        }
        if (string2.equals(".key")) {
            return KeyIndex.getInstance();
        }
        if (!string2.equals(".priority")) {
            return new PathIndex(new Path(string2));
        }
        throw new IllegalStateException("queryDefinition shouldn't ever be .priority since it's the default");
    }

    public int compare(NamedNode namedNode, NamedNode namedNode2, boolean bl) {
        if (bl) {
            return this.compare(namedNode2, namedNode);
        }
        return this.compare(namedNode, namedNode2);
    }

    public abstract String getQueryDefinition();

    public boolean indexedValueChanged(Node node, Node node2) {
        boolean bl = this.compare(new NamedNode(ChildKey.getMinName(), node), new NamedNode(ChildKey.getMinName(), node2)) != 0;
        return bl;
    }

    public abstract boolean isDefinedOn(Node var1);

    public abstract NamedNode makePost(ChildKey var1, Node var2);

    public abstract NamedNode maxPost();

    public NamedNode minPost() {
        return NamedNode.getMinNode();
    }
}

