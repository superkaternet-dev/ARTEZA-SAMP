/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.Validation;
import com.google.firebase.database.core.utilities.encoding.CustomClassMapper;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import java.util.Iterator;

public class DataSnapshot {
    private final IndexedNode node;
    private final DatabaseReference query;

    DataSnapshot(DatabaseReference databaseReference, IndexedNode indexedNode) {
        this.node = indexedNode;
        this.query = databaseReference;
    }

    public DataSnapshot child(String string2) {
        return new DataSnapshot(this.query.child(string2), IndexedNode.from(this.node.getNode().getChild(new Path(string2))));
    }

    public boolean exists() {
        return this.node.getNode().isEmpty() ^ true;
    }

    public Iterable<DataSnapshot> getChildren() {
        return new Iterable<DataSnapshot>(this, this.node.iterator()){
            final DataSnapshot this$0;
            final Iterator val$iter;
            {
                this.this$0 = dataSnapshot;
                this.val$iter = iterator2;
            }

            @Override
            public Iterator<DataSnapshot> iterator() {
                return new Iterator<DataSnapshot>(this){
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public boolean hasNext() {
                        return this.this$1.val$iter.hasNext();
                    }

                    @Override
                    public DataSnapshot next() {
                        NamedNode namedNode = (NamedNode)this.this$1.val$iter.next();
                        return new DataSnapshot(this.this$1.this$0.query.child(namedNode.getName().asString()), IndexedNode.from(namedNode.getNode()));
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove called on immutable collection");
                    }
                };
            }
        };
    }

    public long getChildrenCount() {
        return this.node.getNode().getChildCount();
    }

    public String getKey() {
        return this.query.getKey();
    }

    public Object getPriority() {
        Object object = this.node.getNode().getPriority().getValue();
        if (object instanceof Long) {
            return (double)((Long)object).longValue();
        }
        return object;
    }

    public DatabaseReference getRef() {
        return this.query;
    }

    public Object getValue() {
        return this.node.getNode().getValue();
    }

    public <T> T getValue(GenericTypeIndicator<T> genericTypeIndicator) {
        return CustomClassMapper.convertToCustomClass(this.node.getNode().getValue(), genericTypeIndicator);
    }

    public <T> T getValue(Class<T> clazz) {
        return CustomClassMapper.convertToCustomClass(this.node.getNode().getValue(), clazz);
    }

    public Object getValue(boolean bl) {
        return this.node.getNode().getValue(bl);
    }

    public boolean hasChild(String string2) {
        if (this.query.getParent() == null) {
            Validation.validateRootPathString(string2);
        } else {
            Validation.validatePathString(string2);
        }
        return this.node.getNode().getChild(new Path(string2)).isEmpty() ^ true;
    }

    public boolean hasChildren() {
        boolean bl = this.node.getNode().getChildCount() > 0;
        return bl;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DataSnapshot { key = ");
        stringBuilder.append(this.query.getKey());
        stringBuilder.append(", value = ");
        stringBuilder.append(this.node.getNode().getValue(true));
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}

