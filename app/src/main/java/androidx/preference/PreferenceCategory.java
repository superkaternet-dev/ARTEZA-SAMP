/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.widget.TextView
 */
package androidx.preference;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;

public class PreferenceCategory
extends PreferenceGroup {
    public PreferenceCategory(Context context) {
        this(context, null);
    }

    public PreferenceCategory(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.preferenceCategoryStyle, 16842892));
    }

    public PreferenceCategory(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public PreferenceCategory(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        if (Build.VERSION.SDK_INT >= 28) {
            preferenceViewHolder.itemView.setAccessibilityHeading(true);
        } else if (Build.VERSION.SDK_INT < 21) {
            TypedValue typedValue = new TypedValue();
            if (!this.getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true)) {
                return;
            }
            if ((preferenceViewHolder = (TextView)preferenceViewHolder.findViewById(16908310)) == null) {
                return;
            }
            int n = ContextCompat.getColor(this.getContext(), R.color.preference_fallback_accent_color);
            if (preferenceViewHolder.getCurrentTextColor() != n) {
                return;
            }
            preferenceViewHolder.setTextColor(typedValue.data);
        }
    }

    @Override
    @Deprecated
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat);
        if (Build.VERSION.SDK_INT < 28) {
            AccessibilityNodeInfoCompat.CollectionItemInfoCompat collectionItemInfoCompat = accessibilityNodeInfoCompat.getCollectionItemInfo();
            if (collectionItemInfoCompat == null) {
                return;
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(collectionItemInfoCompat.getRowIndex(), collectionItemInfoCompat.getRowSpan(), collectionItemInfoCompat.getColumnIndex(), collectionItemInfoCompat.getColumnSpan(), true, collectionItemInfoCompat.isSelected()));
        }
    }

    @Override
    public boolean shouldDisableDependents() {
        return super.isEnabled() ^ true;
    }
}

