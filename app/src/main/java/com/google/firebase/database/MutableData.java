/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.SnapshotHolder;
import com.google.firebase.database.core.ValidationPath;
import com.google.firebase.database.core.utilities.Validation;
import com.google.firebase.database.core.utilities.encoding.CustomClassMapper;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MutableData {
    private final SnapshotHolder holder;
    private final Path prefixPath;

    private MutableData(SnapshotHolder snapshotHolder, Path path) {
        this.holder = snapshotHolder;
        this.prefixPath = path;
        ValidationPath.validateWithObject(path, this.getValue());
    }

    MutableData(Node node) {
        this(new SnapshotHolder(node), new Path(""));
    }

    public MutableData child(String string2) {
        Validation.validatePathString(string2);
        return new MutableData(this.holder, this.prefixPath.child(new Path(string2)));
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof MutableData && this.holder.equals(((MutableData)object).holder) && this.prefixPath.equals(((MutableData)object).prefixPath);
        return bl;
    }

    public Iterable<MutableData> getChildren() {
        Node node = this.getNode();
        if (!node.isEmpty() && !node.isLeafNode()) {
            return new Iterable<MutableData>(this, IndexedNode.from(node).iterator()){
                final MutableData this$0;
                final Iterator val$iter;
                {
                    this.this$0 = mutableData;
                    this.val$iter = iterator2;
                }

                @Override
                public Iterator<MutableData> iterator() {
                    return new Iterator<MutableData>(this){
                        final 2 this$1;
                        {
                            this.this$1 = var1_1;
                        }

                        @Override
                        public boolean hasNext() {
                            return this.this$1.val$iter.hasNext();
                        }

                        @Override
                        public MutableData next() {
                            NamedNode namedNode = (NamedNode)this.this$1.val$iter.next();
                            return new MutableData(this.this$1.this$0.holder, this.this$1.this$0.prefixPath.child(namedNode.getName()));
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove called on immutable collection");
                        }
                    };
                }
            };
        }
        return new Iterable<MutableData>(this){
            final MutableData this$0;
            {
                this.this$0 = mutableData;
            }

            @Override
            public Iterator<MutableData> iterator() {
                return new Iterator<MutableData>(this){
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public MutableData next() {
                        throw new NoSuchElementException();
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
        return this.getNode().getChildCount();
    }

    public String getKey() {
        String string2 = this.prefixPath.getBack() != null ? this.prefixPath.getBack().asString() : null;
        return string2;
    }

    Node getNode() {
        return this.holder.getNode(this.prefixPath);
    }

    public Object getPriority() {
        return this.getNode().getPriority().getValue();
    }

    public Object getValue() {
        return this.getNode().getValue();
    }

    public <T> T getValue(GenericTypeIndicator<T> genericTypeIndicator) {
        return CustomClassMapper.convertToCustomClass(this.getNode().getValue(), genericTypeIndicator);
    }

    public <T> T getValue(Class<T> clazz) {
        return CustomClassMapper.convertToCustomClass(this.getNode().getValue(), clazz);
    }

    public boolean hasChild(String string2) {
        return this.getNode().getChild(new Path(string2)).isEmpty() ^ true;
    }

    public boolean hasChildren() {
        Node node = this.getNode();
        boolean bl = !node.isLeafNode() && !node.isEmpty();
        return bl;
    }

    public void setPriority(Object object) {
        this.holder.update(this.prefixPath, this.getNode().updatePriority(PriorityUtilities.parsePriority(this.prefixPath, object)));
    }

    public void setValue(Object object) throws DatabaseException {
        ValidationPath.validateWithObject(this.prefixPath, object);
        object = CustomClassMapper.convertToPlainJavaTypes(object);
        Validation.validateWritableObject(object);
        this.holder.update(this.prefixPath, NodeUtilities.NodeFromJSON(object));
    }

    public String toString() {
        Object object = this.prefixPath.getFront();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MutableData { key = ");
        object = object != null ? ((ChildKey)object).asString() : "<none>";
        stringBuilder.append((String)object);
        stringBuilder.append(", value = ");
        stringBuilder.append(this.holder.getRootNode().getValue(true));
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}

