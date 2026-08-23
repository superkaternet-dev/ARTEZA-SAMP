/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.LLRBValueNode;

public class LLRBBlackValueNode<K, V>
extends LLRBValueNode<K, V> {
    private int size = -1;

    LLRBBlackValueNode(K k, V v, LLRBNode<K, V> lLRBNode, LLRBNode<K, V> lLRBNode2) {
        super(k, v, lLRBNode, lLRBNode2);
    }

    @Override
    protected LLRBValueNode<K, V> copy(K k, V v, LLRBNode<K, V> lLRBNode, LLRBNode<K, V> lLRBNode2) {
        if (k == null) {
            k = this.getKey();
        }
        if (v == null) {
            v = this.getValue();
        }
        if (lLRBNode == null) {
            lLRBNode = this.getLeft();
        }
        if (lLRBNode2 == null) {
            lLRBNode2 = this.getRight();
        }
        return new LLRBBlackValueNode<K, V>(k, v, lLRBNode, lLRBNode2);
    }

    @Override
    protected LLRBNode.Color getColor() {
        return LLRBNode.Color.BLACK;
    }

    @Override
    public boolean isRed() {
        return false;
    }

    @Override
    void setLeft(LLRBNode<K, V> lLRBNode) {
        if (this.size == -1) {
            super.setLeft(lLRBNode);
            return;
        }
        throw new IllegalStateException("Can't set left after using size");
    }

    @Override
    public int size() {
        if (this.size == -1) {
            this.size = this.getLeft().size() + 1 + this.getRight().size();
        }
        return this.size;
    }
}

