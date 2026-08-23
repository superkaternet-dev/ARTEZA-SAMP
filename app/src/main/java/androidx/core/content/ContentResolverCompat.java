/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 *  android.os.OperationCanceledException
 */
package androidx.core.content;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.OperationCanceledException;
import androidx.core.os.CancellationSignal;

public final class ContentResolverCompat {
    private ContentResolverCompat() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Cursor query(ContentResolver contentResolver, Uri uri, String[] stringArray, String string2, String[] stringArray2, String string3, CancellationSignal object) {
        if (Build.VERSION.SDK_INT >= 16) {
            Exception exception2;
            block4: {
                if (object != null) {
                    try {
                        object = ((CancellationSignal)object).getCancellationSignalObject();
                        return contentResolver.query(uri, stringArray, string2, stringArray2, string3, (android.os.CancellationSignal)object);
                    }
                    catch (Exception exception2) {
                        break block4;
                    }
                }
                object = null;
                return contentResolver.query(uri, stringArray, string2, stringArray2, string3, (android.os.CancellationSignal)object);
            }
            if (!(exception2 instanceof OperationCanceledException)) throw exception2;
            throw new androidx.core.os.OperationCanceledException();
        }
        if (object == null) return contentResolver.query(uri, stringArray, string2, stringArray2, string3);
        ((CancellationSignal)object).throwIfCanceled();
        return contentResolver.query(uri, stringArray, string2, stringArray2, string3);
    }
}

