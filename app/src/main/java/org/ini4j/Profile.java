/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import org.ini4j.CommentedMap;
import org.ini4j.MultiMap;
import org.ini4j.OptionMap;

public interface Profile
extends MultiMap<String, Section>,
CommentedMap<String, Section> {
    public static final char PATH_SEPARATOR = '/';

    public Section add(String var1);

    public void add(String var1, String var2, Object var3);

    public <T> T as(Class<T> var1);

    public <T> T as(Class<T> var1, String var2);

    public <T> T fetch(Object var1, Object var2, Class<T> var3);

    public String fetch(Object var1, Object var2);

    public <T> T get(Object var1, Object var2, Class<T> var3);

    public String get(Object var1, Object var2);

    public String getComment();

    public String put(String var1, String var2, Object var3);

    public String remove(Object var1, Object var2);

    public Section remove(Section var1);

    public void setComment(String var1);

    public static interface Section
    extends OptionMap {
        public Section addChild(String var1);

        public String[] childrenNames();

        public Section getChild(String var1);

        public String getName();

        public Section getParent();

        public String getSimpleName();

        public Section lookup(String ... var1);

        public void removeChild(String var1);
    }
}

