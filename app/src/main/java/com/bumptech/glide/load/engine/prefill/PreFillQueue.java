/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine.prefill;

import com.bumptech.glide.load.engine.prefill.PreFillType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class PreFillQueue {
    private final Map<PreFillType, Integer> bitmapsPerType;
    private int bitmapsRemaining;
    private int keyIndex;
    private final List<PreFillType> keyList;

    public PreFillQueue(Map<PreFillType, Integer> object2) {
        this.bitmapsPerType = object2;
        this.keyList = new ArrayList(object2.keySet());
        for (Integer n : object2.values()) {
            this.bitmapsRemaining += n.intValue();
        }
    }

    public int getSize() {
        return this.bitmapsRemaining;
    }

    public boolean isEmpty() {
        boolean bl = this.bitmapsRemaining == 0;
        return bl;
    }

    public PreFillType remove() {
        PreFillType preFillType = this.keyList.get(this.keyIndex);
        Integer n = this.bitmapsPerType.get(preFillType);
        if (n == 1) {
            this.bitmapsPerType.remove(preFillType);
            this.keyList.remove(this.keyIndex);
        } else {
            this.bitmapsPerType.put(preFillType, n - 1);
        }
        --this.bitmapsRemaining;
        int n2 = this.keyList.isEmpty() ? 0 : (this.keyIndex + 1) % this.keyList.size();
        this.keyIndex = n2;
        return preFillType;
    }
}

