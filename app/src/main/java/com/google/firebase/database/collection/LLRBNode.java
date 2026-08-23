/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.collection;

import java.util.Comparator;

public interface LLRBNode<K, V> {
    public LLRBNode<K, V> copy(K var1, V var2, Color var3, LLRBNode<K, V> var4, LLRBNode<K, V> var5);

    public K getKey();

    public LLRBNode<K, V> getLeft();

    public LLRBNode<K, V> getMax();

    public LLRBNode<K, V> getMin();

    public LLRBNode<K, V> getRight();

    public V getValue();

    public void inOrderTraversal(NodeVisitor<K, V> var1);

    public LLRBNode<K, V> insert(K var1, V var2, Comparator<K> var3);

    public boolean isEmpty();

    public boolean isRed();

    public LLRBNode<K, V> remove(K var1, Comparator<K> var2);

    public boolean shortCircuitingInOrderTraversal(ShortCircuitingNodeVisitor<K, V> var1);

    public boolean shortCircuitingReverseOrderTraversal(ShortCircuitingNodeVisitor<K, V> var1);

    public int size();

    public static final class Color
    extends Enum<Color> {
        private static final Color[] $VALUES;
        public static final /* enum */ Color BLACK;
        public static final /* enum */ Color RED;

        static {
            Color color2;
            Color color3;
            RED = color3 = new Color();
            BLACK = color2 = new Color();
            $VALUES = new Color[]{color3, color2};
        }

        public static Color valueOf(String string2) {
            return Enum.valueOf(Color.class, string2);
        }

        public static Color[] values() {
            return (Color[])$VALUES.clone();
        }
    }

    public static abstract class NodeVisitor<K, V>
    implements ShortCircuitingNodeVisitor<K, V> {
        @Override
        public boolean shouldContinue(K k, V v) {
            this.visitEntry(k, v);
            return true;
        }

        public abstract void visitEntry(K var1, V var2);
    }

    public static interface ShortCircuitingNodeVisitor<K, V> {
        public boolean shouldContinue(K var1, V var2);
    }
}

