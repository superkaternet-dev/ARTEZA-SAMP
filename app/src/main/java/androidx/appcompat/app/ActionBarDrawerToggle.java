/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActionBar
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$OnClickListener
 */
package androidx.appcompat.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBarDrawerToggleHoneycomb;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActionBarDrawerToggle
implements DrawerLayout.DrawerListener {
    private final Delegate mActivityImpl;
    private final int mCloseDrawerContentDescRes;
    boolean mDrawerIndicatorEnabled = true;
    private final DrawerLayout mDrawerLayout;
    private boolean mDrawerSlideAnimationEnabled = true;
    private boolean mHasCustomUpIndicator;
    private Drawable mHomeAsUpIndicator;
    private final int mOpenDrawerContentDescRes;
    private DrawerArrowDrawable mSlider;
    View.OnClickListener mToolbarNavigationClickListener;
    private boolean mWarnedForDisplayHomeAsUp = false;

    ActionBarDrawerToggle(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout, DrawerArrowDrawable drawerArrowDrawable, int n, int n2) {
        if (toolbar != null) {
            this.mActivityImpl = new ToolbarCompatDelegate(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(this){
                final ActionBarDrawerToggle this$0;
                {
                    this.this$0 = actionBarDrawerToggle;
                }

                public void onClick(View view) {
                    if (this.this$0.mDrawerIndicatorEnabled) {
                        this.this$0.toggle();
                    } else if (this.this$0.mToolbarNavigationClickListener != null) {
                        this.this$0.mToolbarNavigationClickListener.onClick(view);
                    }
                }
            });
        } else {
            this.mActivityImpl = activity instanceof DelegateProvider ? ((DelegateProvider)activity).getDrawerToggleDelegate() : new FrameworkActionBarDelegate(activity);
        }
        this.mDrawerLayout = drawerLayout;
        this.mOpenDrawerContentDescRes = n;
        this.mCloseDrawerContentDescRes = n2;
        this.mSlider = drawerArrowDrawable == null ? new DrawerArrowDrawable(this.mActivityImpl.getActionBarThemedContext()) : drawerArrowDrawable;
        this.mHomeAsUpIndicator = this.getThemeUpIndicator();
    }

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int n, int n2) {
        this(activity, null, drawerLayout, null, n, n2);
    }

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int n, int n2) {
        this(activity, toolbar, drawerLayout, null, n, n2);
    }

    private void setPosition(float f) {
        if (f == 1.0f) {
            this.mSlider.setVerticalMirror(true);
        } else if (f == 0.0f) {
            this.mSlider.setVerticalMirror(false);
        }
        this.mSlider.setProgress(f);
    }

    public DrawerArrowDrawable getDrawerArrowDrawable() {
        return this.mSlider;
    }

    Drawable getThemeUpIndicator() {
        return this.mActivityImpl.getThemeUpIndicator();
    }

    public View.OnClickListener getToolbarNavigationClickListener() {
        return this.mToolbarNavigationClickListener;
    }

    public boolean isDrawerIndicatorEnabled() {
        return this.mDrawerIndicatorEnabled;
    }

    public boolean isDrawerSlideAnimationEnabled() {
        return this.mDrawerSlideAnimationEnabled;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!this.mHasCustomUpIndicator) {
            this.mHomeAsUpIndicator = this.getThemeUpIndicator();
        }
        this.syncState();
    }

    @Override
    public void onDrawerClosed(View view) {
        this.setPosition(0.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.setActionBarDescription(this.mOpenDrawerContentDescRes);
        }
    }

    @Override
    public void onDrawerOpened(View view) {
        this.setPosition(1.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.setActionBarDescription(this.mCloseDrawerContentDescRes);
        }
    }

    @Override
    public void onDrawerSlide(View view, float f) {
        if (this.mDrawerSlideAnimationEnabled) {
            this.setPosition(Math.min(1.0f, Math.max(0.0f, f)));
        } else {
            this.setPosition(0.0f);
        }
    }

    @Override
    public void onDrawerStateChanged(int n) {
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem != null && menuItem.getItemId() == 16908332 && this.mDrawerIndicatorEnabled) {
            this.toggle();
            return true;
        }
        return false;
    }

    void setActionBarDescription(int n) {
        this.mActivityImpl.setActionBarDescription(n);
    }

    void setActionBarUpIndicator(Drawable drawable2, int n) {
        if (!this.mWarnedForDisplayHomeAsUp && !this.mActivityImpl.isNavigationVisible()) {
            Log.w((String)"ActionBarDrawerToggle", (String)"DrawerToggle may not show up because NavigationIcon is not visible. You may need to call actionbar.setDisplayHomeAsUpEnabled(true);");
            this.mWarnedForDisplayHomeAsUp = true;
        }
        this.mActivityImpl.setActionBarUpIndicator(drawable2, n);
    }

    public void setDrawerArrowDrawable(DrawerArrowDrawable drawerArrowDrawable) {
        this.mSlider = drawerArrowDrawable;
        this.syncState();
    }

    public void setDrawerIndicatorEnabled(boolean bl) {
        if (bl != this.mDrawerIndicatorEnabled) {
            if (bl) {
                DrawerArrowDrawable drawerArrowDrawable = this.mSlider;
                int n = this.mDrawerLayout.isDrawerOpen(0x800003) ? this.mCloseDrawerContentDescRes : this.mOpenDrawerContentDescRes;
                this.setActionBarUpIndicator(drawerArrowDrawable, n);
            } else {
                this.setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
            }
            this.mDrawerIndicatorEnabled = bl;
        }
    }

    public void setDrawerSlideAnimationEnabled(boolean bl) {
        this.mDrawerSlideAnimationEnabled = bl;
        if (!bl) {
            this.setPosition(0.0f);
        }
    }

    public void setHomeAsUpIndicator(int n) {
        Drawable drawable2 = null;
        if (n != 0) {
            drawable2 = this.mDrawerLayout.getResources().getDrawable(n);
        }
        this.setHomeAsUpIndicator(drawable2);
    }

    public void setHomeAsUpIndicator(Drawable drawable2) {
        if (drawable2 == null) {
            this.mHomeAsUpIndicator = this.getThemeUpIndicator();
            this.mHasCustomUpIndicator = false;
        } else {
            this.mHomeAsUpIndicator = drawable2;
            this.mHasCustomUpIndicator = true;
        }
        if (!this.mDrawerIndicatorEnabled) {
            this.setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
        }
    }

    public void setToolbarNavigationClickListener(View.OnClickListener onClickListener) {
        this.mToolbarNavigationClickListener = onClickListener;
    }

    public void syncState() {
        if (this.mDrawerLayout.isDrawerOpen(0x800003)) {
            this.setPosition(1.0f);
        } else {
            this.setPosition(0.0f);
        }
        if (this.mDrawerIndicatorEnabled) {
            DrawerArrowDrawable drawerArrowDrawable = this.mSlider;
            int n = this.mDrawerLayout.isDrawerOpen(0x800003) ? this.mCloseDrawerContentDescRes : this.mOpenDrawerContentDescRes;
            this.setActionBarUpIndicator(drawerArrowDrawable, n);
        }
    }

    void toggle() {
        int n = this.mDrawerLayout.getDrawerLockMode(0x800003);
        if (this.mDrawerLayout.isDrawerVisible(0x800003) && n != 2) {
            this.mDrawerLayout.closeDrawer(0x800003);
        } else if (n != 1) {
            this.mDrawerLayout.openDrawer(0x800003);
        }
    }

    public static interface Delegate {
        public Context getActionBarThemedContext();

        public Drawable getThemeUpIndicator();

        public boolean isNavigationVisible();

        public void setActionBarDescription(int var1);

        public void setActionBarUpIndicator(Drawable var1, int var2);
    }

    public static interface DelegateProvider {
        public Delegate getDrawerToggleDelegate();
    }

    private static class FrameworkActionBarDelegate
    implements Delegate {
        private final Activity mActivity;
        private ActionBarDrawerToggleHoneycomb.SetIndicatorInfo mSetIndicatorInfo;

        FrameworkActionBarDelegate(Activity activity) {
            this.mActivity = activity;
        }

        @Override
        public Context getActionBarThemedContext() {
            ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                return actionBar.getThemedContext();
            }
            return this.mActivity;
        }

        @Override
        public Drawable getThemeUpIndicator() {
            if (Build.VERSION.SDK_INT >= 18) {
                TypedArray typedArray = this.getActionBarThemedContext().obtainStyledAttributes(null, new int[]{16843531}, 16843470, 0);
                Drawable drawable2 = typedArray.getDrawable(0);
                typedArray.recycle();
                return drawable2;
            }
            return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(this.mActivity);
        }

        @Override
        public boolean isNavigationVisible() {
            ActionBar actionBar = this.mActivity.getActionBar();
            boolean bl = actionBar != null && (actionBar.getDisplayOptions() & 4) != 0;
            return bl;
        }

        @Override
        public void setActionBarDescription(int n) {
            if (Build.VERSION.SDK_INT >= 18) {
                ActionBar actionBar = this.mActivity.getActionBar();
                if (actionBar != null) {
                    actionBar.setHomeActionContentDescription(n);
                }
            } else {
                this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, n);
            }
        }

        @Override
        public void setActionBarUpIndicator(Drawable drawable2, int n) {
            ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                if (Build.VERSION.SDK_INT >= 18) {
                    actionBar.setHomeAsUpIndicator(drawable2);
                    actionBar.setHomeActionContentDescription(n);
                } else {
                    actionBar.setDisplayShowHomeEnabled(true);
                    this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(this.mActivity, drawable2, n);
                    actionBar.setDisplayShowHomeEnabled(false);
                }
            }
        }
    }

    static class ToolbarCompatDelegate
    implements Delegate {
        final CharSequence mDefaultContentDescription;
        final Drawable mDefaultUpIndicator;
        final Toolbar mToolbar;

        ToolbarCompatDelegate(Toolbar toolbar) {
            this.mToolbar = toolbar;
            this.mDefaultUpIndicator = toolbar.getNavigationIcon();
            this.mDefaultContentDescription = toolbar.getNavigationContentDescription();
        }

        @Override
        public Context getActionBarThemedContext() {
            return this.mToolbar.getContext();
        }

        @Override
        public Drawable getThemeUpIndicator() {
            return this.mDefaultUpIndicator;
        }

        @Override
        public boolean isNavigationVisible() {
            return true;
        }

        @Override
        public void setActionBarDescription(int n) {
            if (n == 0) {
                this.mToolbar.setNavigationContentDescription(this.mDefaultContentDescription);
            } else {
                this.mToolbar.setNavigationContentDescription(n);
            }
        }

        @Override
        public void setActionBarUpIndicator(Drawable drawable2, int n) {
            this.mToolbar.setNavigationIcon(drawable2);
            this.setActionBarDescription(n);
        }
    }
}

