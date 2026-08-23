/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActionBar
 *  android.app.Activity
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.ViewGroup
 *  android.widget.ImageView
 */
package androidx.appcompat.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

class ActionBarDrawerToggleHoneycomb {
    private static final String TAG = "ActionBarDrawerToggleHC";
    private static final int[] THEME_ATTRS = new int[]{16843531};

    private ActionBarDrawerToggleHoneycomb() {
    }

    public static Drawable getThemeUpIndicator(Activity activity) {
        TypedArray typedArray = activity.obtainStyledAttributes(THEME_ATTRS);
        activity = typedArray.getDrawable(0);
        typedArray.recycle();
        return activity;
    }

    public static SetIndicatorInfo setActionBarDescription(SetIndicatorInfo setIndicatorInfo, Activity activity, int n) {
        SetIndicatorInfo setIndicatorInfo2 = setIndicatorInfo;
        if (setIndicatorInfo == null) {
            setIndicatorInfo2 = new SetIndicatorInfo(activity);
        }
        if (setIndicatorInfo2.setHomeAsUpIndicator != null) {
            try {
                setIndicatorInfo = activity.getActionBar();
                setIndicatorInfo2.setHomeActionContentDescription.invoke((Object)setIndicatorInfo, n);
                if (Build.VERSION.SDK_INT <= 19) {
                    setIndicatorInfo.setSubtitle(setIndicatorInfo.getSubtitle());
                }
            }
            catch (Exception exception) {
                Log.w((String)TAG, (String)"Couldn't set content description via JB-MR2 API", (Throwable)exception);
            }
        }
        return setIndicatorInfo2;
    }

    public static SetIndicatorInfo setActionBarUpIndicator(Activity activity, Drawable drawable2, int n) {
        SetIndicatorInfo setIndicatorInfo = new SetIndicatorInfo(activity);
        if (setIndicatorInfo.setHomeAsUpIndicator != null) {
            try {
                activity = activity.getActionBar();
                setIndicatorInfo.setHomeAsUpIndicator.invoke((Object)activity, drawable2);
                setIndicatorInfo.setHomeActionContentDescription.invoke((Object)activity, n);
            }
            catch (Exception exception) {
                Log.w((String)TAG, (String)"Couldn't set home-as-up indicator via JB-MR2 API", (Throwable)exception);
            }
        } else if (setIndicatorInfo.upIndicatorView != null) {
            setIndicatorInfo.upIndicatorView.setImageDrawable(drawable2);
        } else {
            Log.w((String)TAG, (String)"Couldn't set home-as-up indicator");
        }
        return setIndicatorInfo;
    }

    static class SetIndicatorInfo {
        public Method setHomeActionContentDescription;
        public Method setHomeAsUpIndicator;
        public ImageView upIndicatorView;

        SetIndicatorInfo(Activity activity) {
            try {
                this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
                this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
                return;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                activity = activity.findViewById(16908332);
                if (activity == null) {
                    return;
                }
                ViewGroup viewGroup = (ViewGroup)activity.getParent();
                if (viewGroup.getChildCount() != 2) {
                    return;
                }
                activity = viewGroup.getChildAt(0);
                viewGroup = viewGroup.getChildAt(1);
                if (activity.getId() == 16908332) {
                    activity = viewGroup;
                }
                if (activity instanceof ImageView) {
                    this.upIndicatorView = (ImageView)activity;
                }
                return;
            }
        }
    }
}

