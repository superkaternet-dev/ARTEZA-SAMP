/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.inputmethod.EditorInfo
 */
package androidx.core.view.inputmethod;

import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat {
    private static final String CONTENT_MIME_TYPES_INTEROP_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String CONTENT_MIME_TYPES_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 0x1000000;

    @Deprecated
    public EditorInfoCompat() {
    }

    public static String[] getContentMimeTypes(EditorInfo stringArray) {
        String[] stringArray2;
        if (Build.VERSION.SDK_INT >= 25) {
            stringArray = stringArray.contentMimeTypes;
            if (stringArray == null) {
                stringArray = EMPTY_STRING_ARRAY;
            }
            return stringArray;
        }
        if (stringArray.extras == null) {
            return EMPTY_STRING_ARRAY;
        }
        String[] stringArray3 = stringArray2 = stringArray.extras.getStringArray(CONTENT_MIME_TYPES_KEY);
        if (stringArray2 == null) {
            stringArray3 = stringArray.extras.getStringArray(CONTENT_MIME_TYPES_INTEROP_KEY);
        }
        if (stringArray3 == null) {
            stringArray3 = EMPTY_STRING_ARRAY;
        }
        return stringArray3;
    }

    static int getProtocol(EditorInfo editorInfo) {
        if (Build.VERSION.SDK_INT >= 25) {
            return 1;
        }
        if (editorInfo.extras == null) {
            return 0;
        }
        boolean bl = editorInfo.extras.containsKey(CONTENT_MIME_TYPES_KEY);
        boolean bl2 = editorInfo.extras.containsKey(CONTENT_MIME_TYPES_INTEROP_KEY);
        if (bl && bl2) {
            return 4;
        }
        if (bl) {
            return 3;
        }
        if (bl2) {
            return 2;
        }
        return 0;
    }

    public static void setContentMimeTypes(EditorInfo editorInfo, String[] stringArray) {
        if (Build.VERSION.SDK_INT >= 25) {
            editorInfo.contentMimeTypes = stringArray;
        } else {
            if (editorInfo.extras == null) {
                editorInfo.extras = new Bundle();
            }
            editorInfo.extras.putStringArray(CONTENT_MIME_TYPES_KEY, stringArray);
            editorInfo.extras.putStringArray(CONTENT_MIME_TYPES_INTEROP_KEY, stringArray);
        }
    }
}

