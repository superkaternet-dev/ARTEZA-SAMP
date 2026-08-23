/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.LLRBEmptyNode;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.LLRBValueNode;

public class LLRBRedValueNode<K, V>
extends LLRBValueNode<K, V> {
    LLRBRedValueNode(K k, V v) {
        super(k, v, LLRBEmptyNode.getInstance(), LLRBEmptyNode.getInstance());
    }

    LLRBRedValueNode(K k, V v, LLRBNode<K, V> lLRBNode, LLRBNode<K, V> lLRBNode2) {
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
        return new LLRBRedValueNode<K, V>(k, v, lLRBNode, lLRBNode2);
    }

    @Override
    protected LLRBNode.Color getColor() {
        return LLRBNode.Color.RED;
    }

    @Override
    public boolean isRed() {
        return true;
    }

    @Override
    public int size() {
        return this.getLeft().size() + 1 + this.getRight().size();
    }
}

