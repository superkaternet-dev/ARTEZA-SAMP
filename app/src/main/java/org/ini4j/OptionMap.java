/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import org.ini4j.CommentedMap;
import org.ini4j.MultiMap;

public interface OptionMap
extends MultiMap<String, String>,
CommentedMap<String, String> {
    @Override
    public void add(String var1, Object var2);

    @Override
    public void add(String var1, Object var2, int var3);

    public <T> T as(Class<T> var1);

    public <T> T as(Class<T> var1, String var2);

    public <T> T fetch(Object var1, int var2, Class<T> var3);

    public <T> T fetch(Object var1, Class<T> var2);

    public <T> T fetch(Object var1, Class<T> var2, T var3);

    public String fetch(Object var1);

    public String fetch(Object var1, int var2);

    public String fetch(Object var1, String var2);

    public <T> T fetchAll(Object var1, Class<T> var2);

    public void from(Object var1);

    public void from(Object var1, String var2);

    public <T> T get(Object var1, int var2, Class<T> var3);

    public <T> T get(Object var1, Class<T> var2);

    public <T> T get(Object var1, Class<T> var2, T var3);

    public String get(Object var1, String var2);

    public <T> T getAll(Object var1, Class<T> var2);

    @Override
    public String put(String var1, Object var2);

    @Override
    public String put(String var1, Object var2, int var3);

    public void putAll(String var1, Object var2);

    public void to(Object var1);

    public void to(Object var1, String var2);
}

