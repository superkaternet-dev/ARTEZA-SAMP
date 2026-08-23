/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources$Theme
 *  android.view.LayoutInflater
 *  android.widget.SpinnerAdapter
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.SpinnerAdapter;
import androidx.appcompat.view.ContextThemeWrapper;

public interface ThemedSpinnerAdapter
extends SpinnerAdapter {
    public Resources.Theme getDropDownViewTheme();

    public void setDropDownViewTheme(Resources.Theme var1);

    public static final class Helper {
        private final Context mContext;
        private LayoutInflater mDropDownInflater;
        private final LayoutInflater mInflater;

        public Helper(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from((Context)context);
        }

        public LayoutInflater getDropDownViewInflater() {
            LayoutInflater layoutInflater = this.mDropDownInflater;
            if (layoutInflater == null) {
                layoutInflater = this.mInflater;
            }
            return layoutInflater;
        }

        public Resources.Theme getDropDownViewTheme() {
            Object object = this.mDropDownInflater;
            object = object == null ? null : object.getContext().getTheme();
            return object;
        }

        public void setDropDownViewTheme(Resources.Theme theme) {
            this.mDropDownInflater = theme == null ? null : (theme == this.mContext.getTheme() ? this.mInflater : LayoutInflater.from((Context)new ContextThemeWrapper(this.mContext, theme)));
        }
    }
}

