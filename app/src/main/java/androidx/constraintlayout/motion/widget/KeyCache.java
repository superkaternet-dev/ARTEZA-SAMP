/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.widget;

import java.util.Arrays;
import java.util.HashMap;

public class KeyCache {
    HashMap<Object, HashMap<String, float[]>> map = new HashMap();

    float getFloatValue(Object object, String string2, int n) {
        if (!this.map.containsKey(object)) {
            return Float.NaN;
        }
        if (!((HashMap)(object = this.map.get(object))).containsKey(string2)) {
            return Float.NaN;
        }
        if (((Object)(object = (Object)((HashMap)object).get(string2))).length > n) {
            return (float)object[n];
        }
        return Float.NaN;
    }

    void setFloatValue(Object object, String string2, int n, float f) {
        if (!this.map.containsKey(object)) {
            HashMap<String, float[]> hashMap = new HashMap<String, float[]>();
            float[] fArray = new float[n + 1];
            fArray[n] = f;
            hashMap.put(string2, fArray);
            this.map.put(object, hashMap);
        } else {
            HashMap<String, float[]> hashMap = this.map.get(object);
            if (!hashMap.containsKey(string2)) {
                float[] fArray = new float[n + 1];
                fArray[n] = f;
                hashMap.put(string2, fArray);
                this.map.put(object, hashMap);
            } else {
                float[] fArray = hashMap.get(string2);
                object = fArray;
                if (fArray.length <= n) {
                    object = Arrays.copyOf(fArray, n + 1);
                }
                object[n] = f;
                hashMap.put(string2, (float[])object);
            }
        }
    }
}

