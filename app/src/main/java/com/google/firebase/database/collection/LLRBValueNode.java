/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.LLRBBlackValueNode;
import com.google.firebase.database.collection.LLRBEmptyNode;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.LLRBRedValueNode;
import java.util.Comparator;

public abstract class LLRBValueNode<K, V>
implements LLRBNode<K, V> {
    private final K key;
    private LLRBNode<K, V> left;
    private final LLRBNode<K, V> right;
    private final V value;

    LLRBValueNode(K object, V v, LLRBNode<K, V> lLRBNode, LLRBNode<K, V> lLRBNode2) {
        this.key = object;
        this.value = v;
        if (lLRBNode == null) {
            lLRBNode = LLRBEmptyNode.getInstance();
        }
        this.left = lLRBNode;
        object = lLRBNode2 == null ? LLRBEmptyNode.getInstance() : lLRBNode2;
        this.right = object;
    }

    private LLRBValueNode<K, V> colorFlip() {
        LLRBNode<Object, Object> lLRBNode = this.left;
        lLRBNode = lLRBNode.copy(null, null, LLRBValueNode.oppositeColor(lLRBNode), null, null);
        LLRBNode<Object, Object> lLRBNode2 = this.right;
        lLRBNode2 = lLRBNode2.copy(null, null, LLRBValueNode.oppositeColor(lLRBNode2), null, null);
        return this.copy((Object)null, (Object)null, LLRBValueNode.oppositeColor(this), (LLRBNode)lLRBNode, (LLRBNode)lLRBNode2);
    }

    private LLRBValueNode<K, V> fixUp() {
        LLRBValueNode<K, V> lLRBValueNode;
        LLRBValueNode<K, V> lLRBValueNode2 = lLRBValueNode = this;
        if (lLRBValueNode.right.isRed()) {
            lLRBValueNode2 = lLRBValueNode;
            if (!lLRBValueNode.left.isRed()) {
                lLRBValueNode2 = lLRBValueNode.rotateLeft();
            }
        }
        lLRBValueNode = lLRBValueNode2;
        if (lLRBValueNode2.left.isRed()) {
            lLRBValueNode = lLRBValueNode2;
            if (((LLRBValueNode)lLRBValueNode2.left).left.isRed()) {
                lLRBValueNode = super.rotateRight();
            }
        }
        lLRBValueNode2 = lLRBValueNode;
        if (lLRBValueNode.left.isRed()) {
            lLRBValueNode2 = lLRBValueNode;
            if (lLRBValueNode.right.isRed()) {
                lLRBValueNode2 = super.colorFlip();
            }
        }
        return lLRBValueNode2;
    }

    private LLRBValueNode<K, V> moveRedLeft() {
        LLRBValueNode<K, V> lLRBValueNode;
        LLRBValueNode<Object, Object> lLRBValueNode2 = lLRBValueNode = this.colorFlip();
        if (lLRBValueNode.getRight().getLeft().isRed()) {
            lLRBValueNode2 = super.colorFlip();
        }
        return lLRBValueNode2;
    }

    private LLRBValueNode<K, V> moveRedRight() {
        LLRBValueNode<K, V> lLRBValueNode;
        LLRBValueNode<K, V> lLRBValueNode2 = lLRBValueNode = this.colorFlip();
        if (lLRBValueNode.getLeft().getLeft().isRed()) {
            lLRBValueNode2 = super.colorFlip();
        }
        return lLRBValueNode2;
    }

    private static LLRBNode.Color oppositeColor(LLRBNode object) {
        object = object.isRed() ? LLRBNode.Color.BLACK : LLRBNode.Color.RED;
        return object;
    }

    private LLRBNode<K, V> removeMin() {
        LLRBValueNode<K, V> lLRBValueNode;
        if (this.left.isEmpty()) {
            return LLRBEmptyNode.getInstance();
        }
        LLRBValueNode<K, V> lLRBValueNode2 = lLRBValueNode = this;
        if (!lLRBValueNode.getLeft().isRed()) {
            lLRBValueNode2 = lLRBValueNode;
            if (!lLRBValueNode.getLeft().getLeft().isRed()) {
                lLRBValueNode2 = lLRBValueNode.moveRedLeft();
            }
        }
        return super.fixUp();
    }

    private LLRBValueNode<K, V> rotateLeft() {
        LLRBNode lLRBNode = this.copy((Object)null, (Object)null, LLRBNode.Color.RED, (LLRBNode)null, (LLRBNode)((LLRBValueNode)this.right).left);
        return (LLRBValueNode)this.right.copy(null, null, this.getColor(), lLRBNode, null);
    }

    private LLRBValueNode<K, V> rotateRight() {
        LLRBNode lLRBNode = this.copy((Object)null, (Object)null, LLRBNode.Color.RED, (LLRBNode)((LLRBValueNode)this.left).right, (LLRBNode)null);
        return (LLRBValueNode)this.left.copy(null, null, this.getColor(), null, lLRBNode);
    }

    @Override
    public LLRBValueNode<K, V> copy(K k, V v, LLRBNode.Color color2, LLRBNode<K, V> lLRBNode, LLRBNode<K, V> lLRBNode2) {
        if (k == null) {
            k = this.key;
        }
        if (v == null) {
            v = this.value;
        }
        if (lLRBNode == null) {
            lLRBNode = this.left;
        }
        if (lLRBNode2 == null) {
            lLRBNode2 = this.right;
        }
        if (color2 == LLRBNode.Color.RED) {
            return new LLRBRedValueNode<K, V>(k, v, lLRBNode, lLRBNode2);
        }
        return new LLRBBlackValueNode<K, V>(k, v, lLRBNode, lLRBNode2);
    }

    protected abstract LLRBValueNode<K, V> copy(K var1, V var2, LLRBNode<K, V> var3, LLRBNode<K, V> var4);

    protected abstract LLRBNode.Color getColor();

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public LLRBNode<K, V> getLeft() {
        return this.left;
    }

    @Override
    public LLRBNode<K, V> getMax() {
        if (this.right.isEmpty()) {
            return this;
        }
        return this.right.getMax();
    }

    @Override
    public LLRBNode<K, V> getMin() {
        if (this.left.isEmpty()) {
            return this;
        }
        return this.left.getMin();
    }

    @Override
    public LLRBNode<K, V> getRight() {
        return this.right;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public void inOrderTraversal(LLRBNode.NodeVisitor<K, V> nodeVisitor) {
        this.left.inOrderTraversal(nodeVisitor);
        nodeVisitor.visitEntry(this.key, this.value);
        this.right.inOrderTraversal(nodeVisitor);
    }

    @Override
    public LLRBNode<K, V> insert(K object, V v, Comparator<K> comparator) {
        int n = comparator.compare(object, this.key);
        object = n < 0 ? this.copy(null, null, this.left.insert(object, v, comparator), null) : (n == 0 ? this.copy(object, v, null, null) : this.copy(null, null, null, this.right.insert(object, v, comparator)));
        return super.fixUp();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public LLRBNode<K, V> remove(K object, Comparator<K> comparator) {
        LLRBValueNode<Object, Object> lLRBValueNode = this;
        if (comparator.compare(object, lLRBValueNode.key) < 0) {
            LLRBValueNode<Object, Object> lLRBValueNode2 = lLRBValueNode;
            if (!lLRBValueNode.left.isEmpty()) {
                lLRBValueNode2 = lLRBValueNode;
                if (!lLRBValueNode.left.isRed()) {
                    lLRBValueNode2 = lLRBValueNode;
                    if (!((LLRBValueNode)lLRBValueNode.left).left.isRed()) {
                        lLRBValueNode2 = super.moveRedLeft();
                    }
                }
            }
            object = lLRBValueNode2.copy(null, null, lLRBValueNode2.left.remove(object, comparator), null);
        } else {
            LLRBNode<K, V> lLRBNode = lLRBValueNode;
            if (lLRBValueNode.left.isRed()) {
                lLRBNode = lLRBValueNode.rotateRight();
            }
            lLRBValueNode = lLRBNode;
            if (!((LLRBValueNode)lLRBNode).right.isEmpty()) {
                lLRBValueNode = lLRBNode;
                if (!((LLRBValueNode)lLRBNode).right.isRed()) {
                    lLRBValueNode = lLRBNode;
                    if (!((LLRBValueNode)((LLRBValueNode)lLRBNode).right).left.isRed()) {
                        lLRBValueNode = super.moveRedRight();
                    }
                }
            }
            lLRBNode = lLRBValueNode;
            if (comparator.compare(object, lLRBValueNode.key) == 0) {
                if (lLRBValueNode.right.isEmpty()) {
                    return LLRBEmptyNode.getInstance();
                }
                lLRBNode = lLRBValueNode.right.getMin();
                lLRBNode = lLRBValueNode.copy(lLRBNode.getKey(), lLRBNode.getValue(), null, ((LLRBValueNode)lLRBValueNode.right).removeMin());
            }
            object = ((LLRBValueNode)lLRBNode).copy(null, null, null, ((LLRBValueNode)lLRBNode).right.remove(object, comparator));
        }
        return super.fixUp();
    }

    void setLeft(LLRBNode<K, V> lLRBNode) {
        this.left = lLRBNode;
    }

    @Override
    public boolean shortCircuitingInOrderTraversal(LLRBNode.ShortCircuitingNodeVisitor<K, V> shortCircuitingNodeVisitor) {
        if (this.left.shortCircuitingInOrderTraversal(shortCircuitingNodeVisitor) && shortCircuitingNodeVisitor.shouldContinue(this.key, this.value)) {
            return this.right.shortCircuitingInOrderTraversal(shortCircuitingNodeVisitor);
        }
        return false;
    }

    @Override
    public boolean shortCircuitingReverseOrderTraversal(LLRBNode.ShortCircuitingNodeVisitor<K, V> shortCircuitingNodeVisitor) {
        if (this.right.shortCircuitingReverseOrderTraversal(shortCircuitingNodeVisitor) && shortCircuitingNodeVisitor.shouldContinue(this.key, this.value)) {
            return this.left.shortCircuitingReverseOrderTraversal(shortCircuitingNodeVisitor);
        }
        return false;
    }
}

