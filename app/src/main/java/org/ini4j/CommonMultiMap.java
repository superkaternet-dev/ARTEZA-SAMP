/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.ini4j.BasicMultiMap;
import org.ini4j.CommentedMap;

public class CommonMultiMap<K, V>
extends BasicMultiMap<K, V>
implements CommentedMap<K, V> {
    private static final String FIRST_CATEGORY = "";
    private static final String LAST_CATEGORY = "zzzzzzzzzzzzzzzzzzzzzz";
    private static final String META_COMMENT = "comment";
    private static final String SEPARATOR = ";#;";
    private static final long serialVersionUID = 3012579878005541746L;
    private SortedMap<String, Object> _meta;

    private String makeKey(String string2, Object object) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(object));
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    private Map<String, Object> meta() {
        if (this._meta == null) {
            this._meta = new TreeMap<String, Object>();
        }
        return this._meta;
    }

    @Override
    public void clear() {
        super.clear();
        SortedMap<String, Object> sortedMap = this._meta;
        if (sortedMap != null) {
            sortedMap.clear();
        }
    }

    @Override
    public String getComment(Object object) {
        return (String)this.getMeta(META_COMMENT, object);
    }

    Object getMeta(String string2, Object object) {
        SortedMap<String, Object> sortedMap = this._meta;
        string2 = sortedMap == null ? null : sortedMap.get(this.makeKey(string2, object));
        return string2;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        super.putAll(map);
        if (map instanceof CommonMultiMap && (map = ((CommonMultiMap)map)._meta) != null) {
            this.meta().putAll(map);
        }
    }

    @Override
    public String putComment(K k, String string2) {
        return (String)this.putMeta(META_COMMENT, k, string2);
    }

    Object putMeta(String string2, K k, Object object) {
        return this.meta().put(this.makeKey(string2, k), object);
    }

    @Override
    public V remove(Object object) {
        Object v = super.remove(object);
        this.removeMeta(object);
        return v;
    }

    @Override
    public V remove(Object object, int n) {
        Object v = super.remove(object, n);
        if (this.length(object) == 0) {
            this.removeMeta(object);
        }
        return v;
    }

    @Override
    public String removeComment(Object object) {
        return (String)this.removeMeta(META_COMMENT, object);
    }

    Object removeMeta(String string2, Object object) {
        SortedMap<String, Object> sortedMap = this._meta;
        string2 = sortedMap == null ? null : sortedMap.remove(this.makeKey(string2, object));
        return string2;
    }

    void removeMeta(Object object) {
        SortedMap<String, Object> sortedMap = this._meta;
        if (sortedMap != null) {
            sortedMap.subMap(this.makeKey(FIRST_CATEGORY, object), this.makeKey(LAST_CATEGORY, object)).clear();
        }
    }
}

