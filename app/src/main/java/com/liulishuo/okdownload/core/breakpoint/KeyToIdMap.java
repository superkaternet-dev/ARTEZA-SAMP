/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.okdownload.core.breakpoint;

import android.util.SparseArray;
import com.liulishuo.okdownload.DownloadTask;
import java.util.HashMap;

public class KeyToIdMap {
    private final SparseArray<String> idToKeyMap;
    private final HashMap<String, Integer> keyToIdMap;

    KeyToIdMap() {
        this(new HashMap<String, Integer>(), (SparseArray<String>)new SparseArray());
    }

    KeyToIdMap(HashMap<String, Integer> hashMap, SparseArray<String> sparseArray) {
        this.keyToIdMap = hashMap;
        this.idToKeyMap = sparseArray;
    }

    public void add(DownloadTask object, int n) {
        object = this.generateKey((DownloadTask)object);
        this.keyToIdMap.put((String)object, n);
        this.idToKeyMap.put(n, object);
    }

    String generateKey(DownloadTask downloadTask) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(downloadTask.getUrl());
        stringBuilder.append(downloadTask.getUri());
        stringBuilder.append(downloadTask.getFilename());
        return stringBuilder.toString();
    }

    public Integer get(DownloadTask comparable) {
        if ((comparable = this.keyToIdMap.get(this.generateKey((DownloadTask)comparable))) != null) {
            return comparable;
        }
        return null;
    }

    public void remove(int n) {
        String string2 = (String)this.idToKeyMap.get(n);
        if (string2 != null) {
            this.keyToIdMap.remove(string2);
            this.idToKeyMap.remove(n);
        }
    }
}

