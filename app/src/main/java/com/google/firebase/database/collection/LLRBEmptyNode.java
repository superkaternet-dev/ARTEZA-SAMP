/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.LLRBRedValueNode;
import java.util.Comparator;

public class LLRBEmptyNode<K, V>
implements LLRBNode<K, V> {
    private static final LLRBEmptyNode INSTANCE = new LLRBEmptyNode();

    private LLRBEmptyNode() {
    }

    public static <K, V> LLRBEmptyNode<K, V> getInstance() {
        return INSTANCE;
    }

    @Override
    public LLRBNode<K, V> copy(K k, V v, LLRBNode.Color color2, LLRBNode<K, V> lLRBNode, LLRBNode<K, V> lLRBNode2) {
        return this;
    }

    @Override
    public K getKey() {
        return null;
    }

    @Override
    public LLRBNode<K, V> getLeft() {
        return this;
    }

    @Override
    public LLRBNode<K, V> getMax() {
        return this;
    }

    @Override
    public LLRBNode<K, V> getMin() {
        return this;
    }

    @Override
    public LLRBNode<K, V> getRight() {
        return this;
    }

    @Override
    public V getValue() {
        return null;
    }

    @Override
    public void inOrderTraversal(LLRBNode.NodeVisitor<K, V> nodeVisitor) {
    }

    @Override
    public LLRBNode<K, V> insert(K k, V v, Comparator<K> comparator) {
        return new LLRBRedValueNode<K, V>(k, v);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isRed() {
        return false;
    }

    @Override
    public LLRBNode<K, V> remove(K k, Comparator<K> comparator) {
        return this;
    }

    @Override
    public boolean shortCircuitingInOrderTraversal(LLRBNode.ShortCircuitingNodeVisitor<K, V> shortCircuitingNodeVisitor) {
        return true;
    }

    @Override
    public boolean shortCircuitingReverseOrderTraversal(LLRBNode.ShortCircuitingNodeVisitor<K, V> shortCircuitingNodeVisitor) {
        return true;
    }

    @Override
    public int size() {
        return 0;
    }
}

