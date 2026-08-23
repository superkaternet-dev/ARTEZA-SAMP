/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.LLRBValueNode;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class ImmutableSortedMapIterator<K, V>
implements Iterator<Map.Entry<K, V>> {
    private final boolean isReverse;
    private final ArrayDeque<LLRBValueNode<K, V>> nodeStack = new ArrayDeque();

    ImmutableSortedMapIterator(LLRBNode<K, V> lLRBNode, K k, Comparator<K> comparator, boolean bl) {
        this.isReverse = bl;
        while (!lLRBNode.isEmpty()) {
            int n = k != null ? (bl ? comparator.compare(k, lLRBNode.getKey()) : comparator.compare(lLRBNode.getKey(), k)) : 1;
            if (n < 0) {
                if (bl) {
                    lLRBNode = lLRBNode.getLeft();
                    continue;
                }
                lLRBNode = lLRBNode.getRight();
                continue;
            }
            if (n == 0) {
                this.nodeStack.push((LLRBValueNode)lLRBNode);
                break;
            }
            this.nodeStack.push((LLRBValueNode)lLRBNode);
            if (bl) {
                lLRBNode = lLRBNode.getRight();
                continue;
            }
            lLRBNode = lLRBNode.getLeft();
        }
    }

    @Override
    public boolean hasNext() {
        boolean bl = this.nodeStack.size() > 0;
        return bl;
    }

    @Override
    public Map.Entry<K, V> next() {
        try {
            LLRBNode<K, V> lLRBNode = this.nodeStack.pop();
            AbstractMap.SimpleEntry<K, V> simpleEntry = new AbstractMap.SimpleEntry<K, V>(((LLRBValueNode)lLRBNode).getKey(), ((LLRBValueNode)lLRBNode).getValue());
            if (this.isReverse) {
                lLRBNode = ((LLRBValueNode)lLRBNode).getLeft();
                while (!lLRBNode.isEmpty()) {
                    this.nodeStack.push((LLRBValueNode)lLRBNode);
                    lLRBNode = lLRBNode.getRight();
                }
            } else {
                lLRBNode = ((LLRBValueNode)lLRBNode).getRight();
                while (!lLRBNode.isEmpty()) {
                    this.nodeStack.push((LLRBValueNode)lLRBNode);
                    lLRBNode = lLRBNode.getLeft();
                }
            }
            return simpleEntry;
        }
        catch (EmptyStackException emptyStackException) {
            NoSuchElementException noSuchElementException = new NoSuchElementException();
            throw noSuchElementException;
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove called on immutable collection");
    }
}

