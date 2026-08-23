/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.util.Map;

public interface CommentedMap<K, V>
extends Map<K, V> {
    public String getComment(Object var1);

    public String putComment(K var1, String var2);

    public String removeComment(Object var1);
}

