/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view.filter;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.view.filter.ChildChangeAccumulator;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;

public interface NodeFilter {
    public boolean filtersNodes();

    public Index getIndex();

    public NodeFilter getIndexedFilter();

    public IndexedNode updateChild(IndexedNode var1, ChildKey var2, Node var3, Path var4, CompleteChildSource var5, ChildChangeAccumulator var6);

    public IndexedNode updateFullNode(IndexedNode var1, IndexedNode var2, ChildChangeAccumulator var3);

    public IndexedNode updatePriority(IndexedNode var1, Node var2);

    public static interface CompleteChildSource {
        public NamedNode getChildAfterChild(Index var1, NamedNode var2, boolean var3);

        public Node getCompleteChild(ChildKey var1);
    }
}

