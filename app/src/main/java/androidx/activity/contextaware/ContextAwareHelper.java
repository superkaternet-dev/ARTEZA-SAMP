/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package androidx.activity.contextaware;

import android.content.Context;
import androidx.activity.contextaware.OnContextAvailableListener;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class ContextAwareHelper {
    private volatile Context mContext;
    private final Set<OnContextAvailableListener> mListeners = new CopyOnWriteArraySet<OnContextAvailableListener>();

    public void addOnContextAvailableListener(OnContextAvailableListener onContextAvailableListener) {
        if (this.mContext != null) {
            onContextAvailableListener.onContextAvailable(this.mContext);
        }
        this.mListeners.add(onContextAvailableListener);
    }

    public void clearAvailableContext() {
        this.mContext = null;
    }

    public void dispatchOnContextAvailable(Context context) {
        this.mContext = context;
        Iterator<OnContextAvailableListener> iterator2 = this.mListeners.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onContextAvailable(context);
        }
    }

    public Context peekAvailableContext() {
        return this.mContext;
    }

    public void removeOnContextAvailableListener(OnContextAvailableListener onContextAvailableListener) {
        this.mListeners.remove(onContextAvailableListener);
    }
}

