/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.WindowId
 */
package androidx.transition;

import android.view.View;
import android.view.WindowId;
import androidx.transition.WindowIdImpl;

class WindowIdApi18
implements WindowIdImpl {
    private final WindowId mWindowId;

    WindowIdApi18(View view) {
        this.mWindowId = view.getWindowId();
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof WindowIdApi18 && ((WindowIdApi18)object).mWindowId.equals((Object)this.mWindowId);
        return bl;
    }

    public int hashCode() {
        return this.mWindowId.hashCode();
    }
}

