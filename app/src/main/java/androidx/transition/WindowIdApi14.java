/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 */
package androidx.transition;

import android.os.IBinder;
import androidx.transition.WindowIdImpl;

class WindowIdApi14
implements WindowIdImpl {
    private final IBinder mToken;

    WindowIdApi14(IBinder iBinder) {
        this.mToken = iBinder;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof WindowIdApi14 && ((WindowIdApi14)object).mToken.equals(this.mToken);
        return bl;
    }

    public int hashCode() {
        return this.mToken.hashCode();
    }
}

