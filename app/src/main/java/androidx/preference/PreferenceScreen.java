/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 */
package androidx.preference;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.R;

public final class PreferenceScreen
extends PreferenceGroup {
    private boolean mShouldUseGeneratedIds = true;

    public PreferenceScreen(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.preferenceScreenStyle, 16842891));
    }

    @Override
    protected boolean isOnSameScreenAsChildren() {
        return false;
    }

    @Override
    protected void onClick() {
        if (this.getIntent() == null && this.getFragment() == null && this.getPreferenceCount() != 0) {
            PreferenceManager.OnNavigateToScreenListener onNavigateToScreenListener = this.getPreferenceManager().getOnNavigateToScreenListener();
            if (onNavigateToScreenListener != null) {
                onNavigateToScreenListener.onNavigateToScreen(this);
            }
            return;
        }
    }

    public void setShouldUseGeneratedIds(boolean bl) {
        if (!this.isAttached()) {
            this.mShouldUseGeneratedIds = bl;
            return;
        }
        throw new IllegalStateException("Cannot change the usage of generated IDs while attached to the preference hierarchy");
    }

    public boolean shouldUseGeneratedIds() {
        return this.mShouldUseGeneratedIds;
    }
}

