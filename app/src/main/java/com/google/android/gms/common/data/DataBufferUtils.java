/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.data;

import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.common.data.Freezable;
import java.util.ArrayList;
import java.util.Iterator;

public final class DataBufferUtils {
    public static final String KEY_NEXT_PAGE_TOKEN = "next_page_token";
    public static final String KEY_PREV_PAGE_TOKEN = "prev_page_token";

    private DataBufferUtils() {
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freezeAndClose(DataBuffer<E> dataBuffer) {
        ArrayList arrayList = new ArrayList(dataBuffer.getCount());
        try {
            Iterator<E> iterator2 = dataBuffer.iterator();
            while (iterator2.hasNext()) {
                arrayList.add(((Freezable)iterator2.next()).freeze());
            }
            dataBuffer.close();
            return arrayList;
        }
        catch (Throwable throwable) {
            dataBuffer.close();
            throw throwable;
        }
    }

    public static boolean hasData(DataBuffer<?> dataBuffer) {
        return dataBuffer != null && dataBuffer.getCount() > 0;
    }

    public static boolean hasNextPage(DataBuffer<?> bundle) {
        return (bundle = bundle.getMetadata()) != null && bundle.getString(KEY_NEXT_PAGE_TOKEN) != null;
    }

    public static boolean hasPrevPage(DataBuffer<?> bundle) {
        return (bundle = bundle.getMetadata()) != null && bundle.getString(KEY_PREV_PAGE_TOKEN) != null;
    }
}

