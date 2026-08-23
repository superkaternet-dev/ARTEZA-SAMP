/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.app.UiModeManager
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.media.AudioManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.LocaleList
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.os.PowerManager
 *  android.text.TextUtils
 *  android.util.AndroidRuntimeException
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.ContextThemeWrapper
 *  android.view.KeyCharacterMap
 *  android.view.KeyEvent
 *  android.view.KeyboardShortcutGroup
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory2
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.Window
 *  android.view.Window$Callback
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.PopupWindow
 *  android.widget.TextView
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.app.ResourcesFlusher;
import androidx.appcompat.app.ToolbarActionBar;
import androidx.appcompat.app.TwilightManager;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.StandaloneActionMode;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.DecorContentParent;
import androidx.appcompat.widget.FitWindowsViewGroup;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.appcompat.widget.ViewStubCompat;
import androidx.appcompat.widget.ViewUtils;
import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

class AppCompatDelegateImpl
extends AppCompatDelegate
implements MenuBuilder.Callback,
LayoutInflater.Factory2 {
    static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
    private static final boolean IS_PRE_LOLLIPOP;
    private static final boolean sCanApplyOverrideConfiguration;
    private static final boolean sCanReturnDifferentContext;
    private static boolean sInstalledExceptionHandler;
    private static final SimpleArrayMap<String, Integer> sLocalNightModes;
    private static final int[] sWindowBackgroundStyleable;
    ActionBar mActionBar;
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    private boolean mActivityHandlesUiMode;
    private boolean mActivityHandlesUiModeChecked;
    final AppCompatCallback mAppCompatCallback;
    private AppCompatViewInflater mAppCompatViewInflater;
    private AppCompatWindowCallback mAppCompatWindowCallback;
    private AutoNightModeManager mAutoBatteryNightModeManager;
    private AutoNightModeManager mAutoTimeNightModeManager;
    private boolean mBaseContextAttached;
    private boolean mClosingActionMenu;
    final Context mContext;
    private boolean mCreated;
    private DecorContentParent mDecorContentParent;
    private boolean mEnableDefaultActionBarUp;
    ViewPropertyAnimatorCompat mFadeAnim = null;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    private boolean mHandleNativeActionModes = true;
    boolean mHasActionBar;
    final Object mHost;
    int mInvalidatePanelMenuFeatures;
    boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable = new Runnable(this){
        final AppCompatDelegateImpl this$0;
        {
            this.this$0 = appCompatDelegateImpl;
        }

        @Override
        public void run() {
            if ((this.this$0.mInvalidatePanelMenuFeatures & 1) != 0) {
                this.this$0.doInvalidatePanelMenu(0);
            }
            if ((this.this$0.mInvalidatePanelMenuFeatures & 0x1000) != 0) {
                this.this$0.doInvalidatePanelMenu(108);
            }
            this.this$0.mInvalidatePanelMenuPosted = false;
            this.this$0.mInvalidatePanelMenuFeatures = 0;
        }
    };
    boolean mIsDestroyed;
    boolean mIsFloating;
    private int mLocalNightMode = -100;
    private boolean mLongPressBackDown;
    MenuInflater mMenuInflater;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private boolean mStarted;
    private View mStatusGuard;
    ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private int mThemeResId;
    private CharSequence mTitle;
    private TextView mTitleView;
    Window mWindow;
    boolean mWindowNoTitle;

    static {
        sLocalNightModes = new SimpleArrayMap();
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        boolean bl2 = n < 21;
        IS_PRE_LOLLIPOP = bl2;
        sWindowBackgroundStyleable = new int[]{16842836};
        sCanReturnDifferentContext = "robolectric".equals(Build.FINGERPRINT) ^ true;
        if (Build.VERSION.SDK_INT >= 17) {
            bl = true;
        }
        sCanApplyOverrideConfiguration = bl;
        if (bl2 && !sInstalledExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()){
                final Thread.UncaughtExceptionHandler val$defHandler;
                {
                    this.val$defHandler = uncaughtExceptionHandler;
                }

                private boolean shouldWrapException(Throwable object) {
                    boolean bl = object instanceof Resources.NotFoundException;
                    boolean bl2 = false;
                    if (bl) {
                        if ((object = ((Throwable)object).getMessage()) != null && (((String)object).contains("drawable") || ((String)object).contains("Drawable"))) {
                            bl2 = true;
                        }
                        return bl2;
                    }
                    return false;
                }

                @Override
                public void uncaughtException(Thread thread2, Throwable throwable) {
                    if (this.shouldWrapException(throwable)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(throwable.getMessage());
                        stringBuilder.append(AppCompatDelegateImpl.EXCEPTION_HANDLER_MESSAGE_SUFFIX);
                        stringBuilder = new Resources.NotFoundException(stringBuilder.toString());
                        ((Throwable)((Object)stringBuilder)).initCause(throwable.getCause());
                        ((Throwable)((Object)stringBuilder)).setStackTrace(throwable.getStackTrace());
                        this.val$defHandler.uncaughtException(thread2, (Throwable)((Object)stringBuilder));
                    } else {
                        this.val$defHandler.uncaughtException(thread2, throwable);
                    }
                }
            });
            sInstalledExceptionHandler = true;
        }
    }

    AppCompatDelegateImpl(Activity activity, AppCompatCallback appCompatCallback) {
        this((Context)activity, null, appCompatCallback, activity);
    }

    AppCompatDelegateImpl(Dialog dialog, AppCompatCallback appCompatCallback) {
        this(dialog.getContext(), dialog.getWindow(), appCompatCallback, dialog);
    }

    AppCompatDelegateImpl(Context context, Activity activity, AppCompatCallback appCompatCallback) {
        this(context, null, appCompatCallback, activity);
    }

    AppCompatDelegateImpl(Context context, Window window, AppCompatCallback appCompatCallback) {
        this(context, window, appCompatCallback, context);
    }

    private AppCompatDelegateImpl(Context object, Window window, AppCompatCallback object2, Object object3) {
        this.mContext = object;
        this.mAppCompatCallback = object2;
        this.mHost = object3;
        if (this.mLocalNightMode == -100 && object3 instanceof Dialog && (object = this.tryUnwrapContext()) != null) {
            this.mLocalNightMode = ((AppCompatActivity)object).getDelegate().getLocalNightMode();
        }
        if (this.mLocalNightMode == -100 && (object2 = (Integer)((SimpleArrayMap)(object = sLocalNightModes)).get(object3.getClass().getName())) != null) {
            this.mLocalNightMode = (Integer)object2;
            ((SimpleArrayMap)object).remove(object3.getClass().getName());
        }
        if (window != null) {
            this.attachToWindow(window);
        }
        AppCompatDrawableManager.preload();
    }

    private boolean applyDayNight(boolean bl) {
        AutoNightModeManager autoNightModeManager;
        if (this.mIsDestroyed) {
            return false;
        }
        int n = this.calculateNightMode();
        bl = this.updateForNightMode(this.mapNightMode(this.mContext, n), bl);
        if (n == 0) {
            this.getAutoTimeNightModeManager(this.mContext).setup();
        } else {
            autoNightModeManager = this.mAutoTimeNightModeManager;
            if (autoNightModeManager != null) {
                autoNightModeManager.cleanup();
            }
        }
        if (n == 3) {
            this.getAutoBatteryNightModeManager(this.mContext).setup();
        } else {
            autoNightModeManager = this.mAutoBatteryNightModeManager;
            if (autoNightModeManager != null) {
                autoNightModeManager.cleanup();
            }
        }
        return bl;
    }

    private void applyFixedSizeWindow() {
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(0x1020002);
        View view = this.mWindow.getDecorView();
        contentFrameLayout.setDecorPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        view = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        view.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        view.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        if (view.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
            view.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor());
        }
        if (view.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
            view.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor());
        }
        if (view.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
            view.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor());
        }
        if (view.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
            view.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor());
        }
        view.recycle();
        contentFrameLayout.requestLayout();
    }

    private void attachToWindow(Window window) {
        if (this.mWindow == null) {
            Object object = window.getCallback();
            if (!(object instanceof AppCompatWindowCallback)) {
                object = new AppCompatWindowCallback(this, (Window.Callback)object);
                this.mAppCompatWindowCallback = object;
                window.setCallback((Window.Callback)object);
                object = TintTypedArray.obtainStyledAttributes(this.mContext, null, sWindowBackgroundStyleable);
                Drawable drawable2 = ((TintTypedArray)object).getDrawableIfKnown(0);
                if (drawable2 != null) {
                    window.setBackgroundDrawable(drawable2);
                }
                ((TintTypedArray)object).recycle();
                this.mWindow = window;
                return;
            }
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        throw new IllegalStateException("AppCompat has already installed itself into the Window");
    }

    private int calculateNightMode() {
        int n = this.mLocalNightMode;
        if (n == -100) {
            n = AppCompatDelegateImpl.getDefaultNightMode();
        }
        return n;
    }

    private void cleanupAutoManagers() {
        AutoNightModeManager autoNightModeManager = this.mAutoTimeNightModeManager;
        if (autoNightModeManager != null) {
            autoNightModeManager.cleanup();
        }
        if ((autoNightModeManager = this.mAutoBatteryNightModeManager) != null) {
            autoNightModeManager.cleanup();
        }
    }

    private Configuration createOverrideConfigurationForDayNight(Context context, int n, Configuration configuration) {
        switch (n) {
            default: {
                n = context.getApplicationContext().getResources().getConfiguration().uiMode & 0x30;
                break;
            }
            case 2: {
                n = 32;
                break;
            }
            case 1: {
                n = 16;
            }
        }
        context = new Configuration();
        context.fontScale = 0.0f;
        if (configuration != null) {
            context.setTo(configuration);
        }
        context.uiMode = context.uiMode & 0xFFFFFFCF | n;
        return context;
    }

    private ViewGroup createSubDecor() {
        Object object = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (object.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            if (object.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
                this.requestWindowFeature(1);
            } else if (object.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
                this.requestWindowFeature(108);
            }
            if (object.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
                this.requestWindowFeature(109);
            }
            if (object.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
                this.requestWindowFeature(10);
            }
            this.mIsFloating = object.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
            object.recycle();
            this.ensureWindow();
            this.mWindow.getDecorView();
            Object object2 = LayoutInflater.from((Context)this.mContext);
            object = null;
            if (!this.mWindowNoTitle) {
                if (this.mIsFloating) {
                    object = (ViewGroup)object2.inflate(R.layout.abc_dialog_title_material, null);
                    this.mOverlayActionBar = false;
                    this.mHasActionBar = false;
                } else if (this.mHasActionBar) {
                    object = new TypedValue();
                    this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, (TypedValue)object, true);
                    object = object.resourceId != 0 ? new ContextThemeWrapper(this.mContext, object.resourceId) : this.mContext;
                    object = (ViewGroup)LayoutInflater.from((Context)object).inflate(R.layout.abc_screen_toolbar, null);
                    object2 = (DecorContentParent)object.findViewById(R.id.decor_content_parent);
                    this.mDecorContentParent = object2;
                    object2.setWindowCallback(this.getWindowCallback());
                    if (this.mOverlayActionBar) {
                        this.mDecorContentParent.initFeature(109);
                    }
                    if (this.mFeatureProgress) {
                        this.mDecorContentParent.initFeature(2);
                    }
                    if (this.mFeatureIndeterminateProgress) {
                        this.mDecorContentParent.initFeature(5);
                    }
                }
            } else {
                object = this.mOverlayActionMode ? (ViewGroup)object2.inflate(R.layout.abc_screen_simple_overlay_action_mode, null) : (ViewGroup)object2.inflate(R.layout.abc_screen_simple, null);
            }
            if (object != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    ViewCompat.setOnApplyWindowInsetsListener((View)object, new OnApplyWindowInsetsListener(this){
                        final AppCompatDelegateImpl this$0;
                        {
                            this.this$0 = appCompatDelegateImpl;
                        }

                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                            int n = windowInsetsCompat.getSystemWindowInsetTop();
                            int n2 = this.this$0.updateStatusGuard(windowInsetsCompat, null);
                            WindowInsetsCompat windowInsetsCompat2 = windowInsetsCompat;
                            if (n != n2) {
                                windowInsetsCompat2 = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), n2, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                            }
                            return ViewCompat.onApplyWindowInsets(view, windowInsetsCompat2);
                        }
                    });
                } else if (object instanceof FitWindowsViewGroup) {
                    ((FitWindowsViewGroup)object).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener(this){
                        final AppCompatDelegateImpl this$0;
                        {
                            this.this$0 = appCompatDelegateImpl;
                        }

                        @Override
                        public void onFitSystemWindows(Rect rect) {
                            rect.top = this.this$0.updateStatusGuard(null, rect);
                        }
                    });
                }
                if (this.mDecorContentParent == null) {
                    this.mTitleView = (TextView)object.findViewById(R.id.title);
                }
                ViewUtils.makeOptionalFitsSystemWindows((View)object);
                ContentFrameLayout contentFrameLayout = (ContentFrameLayout)object.findViewById(R.id.action_bar_activity_content);
                object2 = (ViewGroup)this.mWindow.findViewById(0x1020002);
                if (object2 != null) {
                    while (object2.getChildCount() > 0) {
                        View view = object2.getChildAt(0);
                        object2.removeViewAt(0);
                        contentFrameLayout.addView(view);
                    }
                    object2.setId(-1);
                    contentFrameLayout.setId(0x1020002);
                    if (object2 instanceof FrameLayout) {
                        ((FrameLayout)object2).setForeground(null);
                    }
                }
                this.mWindow.setContentView((View)object);
                contentFrameLayout.setAttachListener(new ContentFrameLayout.OnAttachListener(this){
                    final AppCompatDelegateImpl this$0;
                    {
                        this.this$0 = appCompatDelegateImpl;
                    }

                    @Override
                    public void onAttachedFromWindow() {
                    }

                    @Override
                    public void onDetachedFromWindow() {
                        this.this$0.dismissPopups();
                    }
                });
                return object;
            }
            object = new StringBuilder();
            object.append("AppCompat does not support the current theme features: { windowActionBar: ");
            object.append(this.mHasActionBar);
            object.append(", windowActionBarOverlay: ");
            object.append(this.mOverlayActionBar);
            object.append(", android:windowIsFloating: ");
            object.append(this.mIsFloating);
            object.append(", windowActionModeOverlay: ");
            object.append(this.mOverlayActionMode);
            object.append(", windowNoTitle: ");
            object.append(this.mWindowNoTitle);
            object.append(" }");
            throw new IllegalArgumentException(object.toString());
        }
        object.recycle();
        object = new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        throw object;
    }

    private void ensureSubDecor() {
        if (!this.mSubDecorInstalled) {
            this.mSubDecor = this.createSubDecor();
            Object object = this.getTitle();
            if (!TextUtils.isEmpty((CharSequence)object)) {
                DecorContentParent decorContentParent = this.mDecorContentParent;
                if (decorContentParent != null) {
                    decorContentParent.setWindowTitle((CharSequence)object);
                } else if (this.peekSupportActionBar() != null) {
                    this.peekSupportActionBar().setWindowTitle((CharSequence)object);
                } else {
                    decorContentParent = this.mTitleView;
                    if (decorContentParent != null) {
                        decorContentParent.setText((CharSequence)object);
                    }
                }
            }
            this.applyFixedSizeWindow();
            this.onSubDecorInstalled(this.mSubDecor);
            this.mSubDecorInstalled = true;
            object = this.getPanelState(0, false);
            if (!(this.mIsDestroyed || object != null && ((PanelFeatureState)object).menu != null)) {
                this.invalidatePanelMenu(108);
            }
        }
    }

    private void ensureWindow() {
        Object object;
        if (this.mWindow == null && (object = this.mHost) instanceof Activity) {
            this.attachToWindow(((Activity)object).getWindow());
        }
        if (this.mWindow != null) {
            return;
        }
        throw new IllegalStateException("We have not been given a Window");
    }

    private static Configuration generateConfigDelta(Configuration configuration, Configuration configuration2) {
        Configuration configuration3 = new Configuration();
        configuration3.fontScale = 0.0f;
        if (configuration2 != null && configuration.diff(configuration2) != 0) {
            if (configuration.fontScale != configuration2.fontScale) {
                configuration3.fontScale = configuration2.fontScale;
            }
            if (configuration.mcc != configuration2.mcc) {
                configuration3.mcc = configuration2.mcc;
            }
            if (configuration.mnc != configuration2.mnc) {
                configuration3.mnc = configuration2.mnc;
            }
            if (Build.VERSION.SDK_INT >= 24) {
                ConfigurationImplApi24.generateConfigDelta_locale(configuration, configuration2, configuration3);
            } else if (!ObjectsCompat.equals(configuration.locale, configuration2.locale)) {
                configuration3.locale = configuration2.locale;
            }
            if (configuration.touchscreen != configuration2.touchscreen) {
                configuration3.touchscreen = configuration2.touchscreen;
            }
            if (configuration.keyboard != configuration2.keyboard) {
                configuration3.keyboard = configuration2.keyboard;
            }
            if (configuration.keyboardHidden != configuration2.keyboardHidden) {
                configuration3.keyboardHidden = configuration2.keyboardHidden;
            }
            if (configuration.navigation != configuration2.navigation) {
                configuration3.navigation = configuration2.navigation;
            }
            if (configuration.navigationHidden != configuration2.navigationHidden) {
                configuration3.navigationHidden = configuration2.navigationHidden;
            }
            if (configuration.orientation != configuration2.orientation) {
                configuration3.orientation = configuration2.orientation;
            }
            if ((configuration.screenLayout & 0xF) != (configuration2.screenLayout & 0xF)) {
                configuration3.screenLayout |= configuration2.screenLayout & 0xF;
            }
            if ((configuration.screenLayout & 0xC0) != (configuration2.screenLayout & 0xC0)) {
                configuration3.screenLayout |= configuration2.screenLayout & 0xC0;
            }
            if ((configuration.screenLayout & 0x30) != (configuration2.screenLayout & 0x30)) {
                configuration3.screenLayout |= configuration2.screenLayout & 0x30;
            }
            if ((configuration.screenLayout & 0x300) != (configuration2.screenLayout & 0x300)) {
                configuration3.screenLayout |= configuration2.screenLayout & 0x300;
            }
            if (Build.VERSION.SDK_INT >= 26) {
                ConfigurationImplApi26.generateConfigDelta_colorMode(configuration, configuration2, configuration3);
            }
            if ((configuration.uiMode & 0xF) != (configuration2.uiMode & 0xF)) {
                configuration3.uiMode |= configuration2.uiMode & 0xF;
            }
            if ((configuration.uiMode & 0x30) != (configuration2.uiMode & 0x30)) {
                configuration3.uiMode |= configuration2.uiMode & 0x30;
            }
            if (configuration.screenWidthDp != configuration2.screenWidthDp) {
                configuration3.screenWidthDp = configuration2.screenWidthDp;
            }
            if (configuration.screenHeightDp != configuration2.screenHeightDp) {
                configuration3.screenHeightDp = configuration2.screenHeightDp;
            }
            if (configuration.smallestScreenWidthDp != configuration2.smallestScreenWidthDp) {
                configuration3.smallestScreenWidthDp = configuration2.smallestScreenWidthDp;
            }
            if (Build.VERSION.SDK_INT >= 17) {
                ConfigurationImplApi17.generateConfigDelta_densityDpi(configuration, configuration2, configuration3);
            }
            return configuration3;
        }
        return configuration3;
    }

    private AutoNightModeManager getAutoBatteryNightModeManager(Context context) {
        if (this.mAutoBatteryNightModeManager == null) {
            this.mAutoBatteryNightModeManager = new AutoBatteryNightModeManager(this, context);
        }
        return this.mAutoBatteryNightModeManager;
    }

    private AutoNightModeManager getAutoTimeNightModeManager(Context context) {
        if (this.mAutoTimeNightModeManager == null) {
            this.mAutoTimeNightModeManager = new AutoTimeNightModeManager(this, TwilightManager.getInstance(context));
        }
        return this.mAutoTimeNightModeManager;
    }

    private void initWindowDecorActionBar() {
        this.ensureSubDecor();
        if (this.mHasActionBar && this.mActionBar == null) {
            Object object = this.mHost;
            if (object instanceof Activity) {
                this.mActionBar = new WindowDecorActionBar((Activity)this.mHost, this.mOverlayActionBar);
            } else if (object instanceof Dialog) {
                this.mActionBar = new WindowDecorActionBar((Dialog)this.mHost);
            }
            object = this.mActionBar;
            if (object != null) {
                ((ActionBar)object).setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }
            return;
        }
    }

    private boolean initializePanelContent(PanelFeatureState panelFeatureState) {
        View view = panelFeatureState.createdPanelView;
        boolean bl = true;
        if (view != null) {
            panelFeatureState.shownPanelView = panelFeatureState.createdPanelView;
            return true;
        }
        if (panelFeatureState.menu == null) {
            return false;
        }
        if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback(this);
        }
        panelFeatureState.shownPanelView = (View)panelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
        if (panelFeatureState.shownPanelView == null) {
            bl = false;
        }
        return bl;
    }

    private boolean initializePanelDecor(PanelFeatureState panelFeatureState) {
        panelFeatureState.setStyle(this.getActionBarThemedContext());
        panelFeatureState.decorView = new ListMenuDecorView(this, panelFeatureState.listPresenterContext);
        panelFeatureState.gravity = 81;
        return true;
    }

    private boolean initializePanelMenu(PanelFeatureState panelFeatureState) {
        Object object;
        block10: {
            Context context;
            block9: {
                context = this.mContext;
                if (panelFeatureState.featureId == 0) break block9;
                object = context;
                if (panelFeatureState.featureId != 108) break block10;
            }
            object = context;
            if (this.mDecorContentParent != null) {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.actionBarTheme, typedValue, true);
                object = null;
                if (typedValue.resourceId != 0) {
                    object = context.getResources().newTheme();
                    object.setTo(theme);
                    object.applyStyle(typedValue.resourceId, true);
                    object.resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                } else {
                    theme.resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                }
                Object object2 = object;
                if (typedValue.resourceId != 0) {
                    object2 = object;
                    if (object == null) {
                        object2 = context.getResources().newTheme();
                        object2.setTo(theme);
                    }
                    object2.applyStyle(typedValue.resourceId, true);
                }
                object = context;
                if (object2 != null) {
                    object = new ContextThemeWrapper(context, 0);
                    object.getTheme().setTo((Resources.Theme)object2);
                }
            }
        }
        object = new MenuBuilder((Context)object);
        object.setCallback(this);
        panelFeatureState.setMenu((MenuBuilder)object);
        return true;
    }

    private void invalidatePanelMenu(int n) {
        this.mInvalidatePanelMenuFeatures |= 1 << n;
        if (!this.mInvalidatePanelMenuPosted) {
            ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuPosted = true;
        }
    }

    private boolean isActivityManifestHandlingUiMode() {
        if (!this.mActivityHandlesUiModeChecked && this.mHost instanceof Activity) {
            boolean bl;
            block11: {
                block10: {
                    int n;
                    PackageManager packageManager;
                    block9: {
                        block8: {
                            packageManager = this.mContext.getPackageManager();
                            if (packageManager == null) {
                                return false;
                            }
                            n = 0;
                            if (Build.VERSION.SDK_INT < 29) break block8;
                            n = 0x100C0000;
                            break block9;
                        }
                        if (Build.VERSION.SDK_INT < 24) break block9;
                        n = 786432;
                    }
                    ComponentName componentName = new ComponentName(this.mContext, this.mHost.getClass());
                    packageManager = packageManager.getActivityInfo(componentName, n);
                    if (packageManager == null) break block10;
                    if ((packageManager.configChanges & 0x200) == 0) break block10;
                    bl = true;
                    break block11;
                }
                bl = false;
            }
            try {
                this.mActivityHandlesUiMode = bl;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.d((String)"AppCompatDelegate", (String)"Exception while getting ActivityInfo", (Throwable)nameNotFoundException);
                this.mActivityHandlesUiMode = false;
            }
        }
        this.mActivityHandlesUiModeChecked = true;
        return this.mActivityHandlesUiMode;
    }

    private boolean onKeyDownPanel(int n, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() == 0) {
            PanelFeatureState panelFeatureState = this.getPanelState(n, true);
            if (!panelFeatureState.isOpen) {
                return this.preparePanel(panelFeatureState, keyEvent);
            }
        }
        return false;
    }

    private boolean onKeyUpPanel(int n, KeyEvent keyEvent) {
        boolean bl;
        DecorContentParent decorContentParent;
        if (this.mActionMode != null) {
            return false;
        }
        boolean bl2 = false;
        PanelFeatureState panelFeatureState = this.getPanelState(n, true);
        if (n == 0 && (decorContentParent = this.mDecorContentParent) != null && decorContentParent.canShowOverflowMenu() && !ViewConfiguration.get((Context)this.mContext).hasPermanentMenuKey()) {
            if (!this.mDecorContentParent.isOverflowMenuShowing()) {
                bl = bl2;
                if (!this.mIsDestroyed) {
                    bl = bl2;
                    if (this.preparePanel(panelFeatureState, keyEvent)) {
                        bl = this.mDecorContentParent.showOverflowMenu();
                    }
                }
            } else {
                bl = this.mDecorContentParent.hideOverflowMenu();
            }
        } else if (!panelFeatureState.isOpen && !panelFeatureState.isHandled) {
            bl = bl2;
            if (panelFeatureState.isPrepared) {
                boolean bl3 = true;
                if (panelFeatureState.refreshMenuContent) {
                    panelFeatureState.isPrepared = false;
                    bl3 = this.preparePanel(panelFeatureState, keyEvent);
                }
                bl = bl2;
                if (bl3) {
                    this.openPanel(panelFeatureState, keyEvent);
                    bl = true;
                }
            }
        } else {
            bl = panelFeatureState.isOpen;
            this.closePanel(panelFeatureState, true);
        }
        if (bl) {
            keyEvent = (AudioManager)this.mContext.getApplicationContext().getSystemService("audio");
            if (keyEvent != null) {
                keyEvent.playSoundEffect(0);
            } else {
                Log.w((String)"AppCompatDelegate", (String)"Couldn't get audio manager");
            }
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void openPanel(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        block25: {
            WindowManager windowManager;
            int n;
            block24: {
                int n2;
                block23: {
                    Window.Callback callback;
                    block22: {
                        if (panelFeatureState.isOpen) return;
                        if (this.mIsDestroyed) {
                            return;
                        }
                        if (panelFeatureState.featureId == 0) {
                            if ((this.mContext.getResources().getConfiguration().screenLayout & 0xF) == 4) {
                                return;
                            }
                            n = 0;
                            if (n != 0) {
                                return;
                            }
                        }
                        if ((callback = this.getWindowCallback()) != null && !callback.onMenuOpened(panelFeatureState.featureId, (Menu)panelFeatureState.menu)) {
                            this.closePanel(panelFeatureState, true);
                            return;
                        }
                        windowManager = (WindowManager)this.mContext.getSystemService("window");
                        if (windowManager == null) {
                            return;
                        }
                        if (!this.preparePanel(panelFeatureState, keyEvent)) {
                            return;
                        }
                        n2 = -2;
                        if (panelFeatureState.decorView == null || panelFeatureState.refreshDecorView) break block22;
                        if (panelFeatureState.createdPanelView == null) break block23;
                        keyEvent = panelFeatureState.createdPanelView.getLayoutParams();
                        n = n2;
                        if (keyEvent != null) {
                            n = n2;
                            if (keyEvent.width == -1) {
                                n = -1;
                            }
                        }
                        break block24;
                    }
                    if (panelFeatureState.decorView == null) {
                        if (!this.initializePanelDecor(panelFeatureState)) return;
                        if (panelFeatureState.decorView == null) {
                            return;
                        }
                    } else if (panelFeatureState.refreshDecorView && panelFeatureState.decorView.getChildCount() > 0) {
                        panelFeatureState.decorView.removeAllViews();
                    }
                    if (!this.initializePanelContent(panelFeatureState) || !panelFeatureState.hasPanelItems()) break block25;
                    callback = panelFeatureState.shownPanelView.getLayoutParams();
                    keyEvent = callback;
                    if (callback == null) {
                        keyEvent = new ViewGroup.LayoutParams(-2, -2);
                    }
                    n = panelFeatureState.background;
                    panelFeatureState.decorView.setBackgroundResource(n);
                    callback = panelFeatureState.shownPanelView.getParent();
                    if (callback instanceof ViewGroup) {
                        ((ViewGroup)callback).removeView(panelFeatureState.shownPanelView);
                    }
                    panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, (ViewGroup.LayoutParams)keyEvent);
                    if (!panelFeatureState.shownPanelView.hasFocus()) {
                        panelFeatureState.shownPanelView.requestFocus();
                    }
                }
                n = n2;
            }
            panelFeatureState.isHandled = false;
            keyEvent = new WindowManager.LayoutParams(n, -2, panelFeatureState.x, panelFeatureState.y, 1002, 0x820000, -3);
            keyEvent.gravity = panelFeatureState.gravity;
            keyEvent.windowAnimations = panelFeatureState.windowAnimations;
            windowManager.addView((View)panelFeatureState.decorView, (ViewGroup.LayoutParams)keyEvent);
            panelFeatureState.isOpen = true;
            return;
        }
        panelFeatureState.refreshDecorView = true;
    }

    private boolean performPanelShortcut(PanelFeatureState panelFeatureState, int n, KeyEvent keyEvent, int n2) {
        boolean bl;
        block7: {
            boolean bl2;
            block6: {
                if (keyEvent.isSystem()) {
                    return false;
                }
                bl2 = false;
                if (panelFeatureState.isPrepared) break block6;
                bl = bl2;
                if (!this.preparePanel(panelFeatureState, keyEvent)) break block7;
            }
            bl = bl2;
            if (panelFeatureState.menu != null) {
                bl = panelFeatureState.menu.performShortcut(n, keyEvent, n2);
            }
        }
        if (bl && (n2 & 1) == 0 && this.mDecorContentParent == null) {
            this.closePanel(panelFeatureState, true);
        }
        return bl;
    }

    private boolean preparePanel(PanelFeatureState object, KeyEvent object2) {
        Window.Callback callback;
        if (this.mIsDestroyed) {
            return false;
        }
        if (((PanelFeatureState)object).isPrepared) {
            return true;
        }
        Object object3 = this.mPreparedPanel;
        if (object3 != null && object3 != object) {
            this.closePanel((PanelFeatureState)object3, false);
        }
        if ((callback = this.getWindowCallback()) != null) {
            ((PanelFeatureState)object).createdPanelView = callback.onCreatePanelView(((PanelFeatureState)object).featureId);
        }
        int n = ((PanelFeatureState)object).featureId != 0 && ((PanelFeatureState)object).featureId != 108 ? 0 : 1;
        if (n != 0 && (object3 = this.mDecorContentParent) != null) {
            object3.setMenuPrepared();
        }
        if (!(((PanelFeatureState)object).createdPanelView != null || n != 0 && this.peekSupportActionBar() instanceof ToolbarActionBar)) {
            if (((PanelFeatureState)object).menu == null || ((PanelFeatureState)object).refreshMenuContent) {
                if (!(((PanelFeatureState)object).menu != null || this.initializePanelMenu((PanelFeatureState)object) && ((PanelFeatureState)object).menu != null)) {
                    return false;
                }
                if (n != 0 && this.mDecorContentParent != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback(this);
                    }
                    this.mDecorContentParent.setMenu(((PanelFeatureState)object).menu, this.mActionMenuPresenterCallback);
                }
                ((PanelFeatureState)object).menu.stopDispatchingItemsChanged();
                if (!callback.onCreatePanelMenu(((PanelFeatureState)object).featureId, (Menu)((PanelFeatureState)object).menu)) {
                    ((PanelFeatureState)object).setMenu(null);
                    if (n != 0 && (object = this.mDecorContentParent) != null) {
                        object.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                    return false;
                }
                ((PanelFeatureState)object).refreshMenuContent = false;
            }
            ((PanelFeatureState)object).menu.stopDispatchingItemsChanged();
            if (((PanelFeatureState)object).frozenActionViewState != null) {
                ((PanelFeatureState)object).menu.restoreActionViewStates(((PanelFeatureState)object).frozenActionViewState);
                ((PanelFeatureState)object).frozenActionViewState = null;
            }
            if (!callback.onPreparePanel(0, ((PanelFeatureState)object).createdPanelView, (Menu)((PanelFeatureState)object).menu)) {
                if (n != 0 && (object2 = this.mDecorContentParent) != null) {
                    object2.setMenu(null, this.mActionMenuPresenterCallback);
                }
                ((PanelFeatureState)object).menu.startDispatchingItemsChanged();
                return false;
            }
            n = object2 != null ? object2.getDeviceId() : -1;
            boolean bl = KeyCharacterMap.load((int)n).getKeyboardType() != 1;
            ((PanelFeatureState)object).qwertyMode = bl;
            ((PanelFeatureState)object).menu.setQwertyMode(((PanelFeatureState)object).qwertyMode);
            ((PanelFeatureState)object).menu.startDispatchingItemsChanged();
        }
        ((PanelFeatureState)object).isPrepared = true;
        ((PanelFeatureState)object).isHandled = false;
        this.mPreparedPanel = object;
        return true;
    }

    private void reopenMenu(boolean bl) {
        Object object = this.mDecorContentParent;
        if (object != null && object.canShowOverflowMenu() && (!ViewConfiguration.get((Context)this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
            Window.Callback callback = this.getWindowCallback();
            if (this.mDecorContentParent.isOverflowMenuShowing() && bl) {
                this.mDecorContentParent.hideOverflowMenu();
                if (!this.mIsDestroyed) {
                    callback.onPanelClosed(108, (Menu)this.getPanelState((int)0, (boolean)true).menu);
                }
            } else if (callback != null && !this.mIsDestroyed) {
                if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
                    this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
                    this.mInvalidatePanelMenuRunnable.run();
                }
                object = this.getPanelState(0, true);
                if (((PanelFeatureState)object).menu != null && !((PanelFeatureState)object).refreshMenuContent && callback.onPreparePanel(0, ((PanelFeatureState)object).createdPanelView, (Menu)((PanelFeatureState)object).menu)) {
                    callback.onMenuOpened(108, (Menu)((PanelFeatureState)object).menu);
                    this.mDecorContentParent.showOverflowMenu();
                }
            }
            return;
        }
        object = this.getPanelState(0, true);
        ((PanelFeatureState)object).refreshDecorView = true;
        this.closePanel((PanelFeatureState)object, false);
        this.openPanel((PanelFeatureState)object, null);
    }

    private int sanitizeWindowFeatureId(int n) {
        if (n == 8) {
            Log.i((String)"AppCompatDelegate", (String)"You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        }
        if (n == 9) {
            Log.i((String)"AppCompatDelegate", (String)"You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            return 109;
        }
        return n;
    }

    private boolean shouldInheritContext(ViewParent viewParent) {
        if (viewParent == null) {
            return false;
        }
        View view = this.mWindow.getDecorView();
        while (true) {
            if (viewParent == null) {
                return true;
            }
            if (viewParent == view || !(viewParent instanceof View) || ViewCompat.isAttachedToWindow((View)viewParent)) break;
            viewParent = viewParent.getParent();
        }
        return false;
    }

    private void throwFeatureRequestIfSubDecorInstalled() {
        if (!this.mSubDecorInstalled) {
            return;
        }
        throw new AndroidRuntimeException("Window feature must be requested before adding content");
    }

    private AppCompatActivity tryUnwrapContext() {
        Context context = this.mContext;
        while (context != null) {
            if (context instanceof AppCompatActivity) {
                return (AppCompatActivity)context;
            }
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper)context).getBaseContext();
                continue;
            }
            return null;
        }
        return null;
    }

    private boolean updateForNightMode(int n, boolean bl) {
        boolean bl2;
        int n2;
        int n3;
        boolean bl3;
        Object object;
        block8: {
            boolean bl4;
            block9: {
                bl4 = false;
                object = this.createOverrideConfigurationForDayNight(this.mContext, n, null);
                bl3 = this.isActivityManifestHandlingUiMode();
                n3 = this.mContext.getResources().getConfiguration().uiMode & 0x30;
                n2 = object.uiMode & 0x30;
                bl2 = bl4;
                if (n3 == n2) break block8;
                bl2 = bl4;
                if (!bl) break block8;
                bl2 = bl4;
                if (bl3) break block8;
                bl2 = bl4;
                if (!this.mBaseContextAttached) break block8;
                if (sCanReturnDifferentContext) break block9;
                bl2 = bl4;
                if (!this.mCreated) break block8;
            }
            object = this.mHost;
            bl2 = bl4;
            if (object instanceof Activity) {
                bl2 = bl4;
                if (!((Activity)object).isChild()) {
                    ActivityCompat.recreate((Activity)this.mHost);
                    bl2 = true;
                }
            }
        }
        bl = bl2;
        if (!bl2) {
            bl = bl2;
            if (n3 != n2) {
                this.updateResourcesConfigurationForNightMode(n2, bl3, null);
                bl = true;
            }
        }
        if (bl && (object = this.mHost) instanceof AppCompatActivity) {
            ((AppCompatActivity)object).onNightModeChanged(n);
        }
        return bl;
    }

    private void updateResourcesConfigurationForNightMode(int n, boolean bl, Configuration object) {
        Resources resources = this.mContext.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        if (object != null) {
            configuration.updateFrom(object);
        }
        configuration.uiMode = resources.getConfiguration().uiMode & 0xFFFFFFCF | n;
        resources.updateConfiguration(configuration, null);
        if (Build.VERSION.SDK_INT < 26) {
            ResourcesFlusher.flush(resources);
        }
        if ((n = this.mThemeResId) != 0) {
            this.mContext.setTheme(n);
            if (Build.VERSION.SDK_INT >= 23) {
                this.mContext.getTheme().applyStyle(this.mThemeResId, true);
            }
        }
        if (bl && (object = this.mHost) instanceof Activity) {
            if ((object = (Activity)object) instanceof LifecycleOwner) {
                if (((LifecycleOwner)object).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    object.onConfigurationChanged(configuration);
                }
            } else if (this.mStarted) {
                object.onConfigurationChanged(configuration);
            }
        }
    }

    private void updateStatusGuardColor(View view) {
        int n = (ViewCompat.getWindowSystemUiVisibility(view) & 0x2000) != 0 ? 1 : 0;
        n = n != 0 ? ContextCompat.getColor(this.mContext, R.color.abc_decor_view_status_guard_light) : ContextCompat.getColor(this.mContext, R.color.abc_decor_view_status_guard);
        view.setBackgroundColor(n);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.ensureSubDecor();
        ((ViewGroup)this.mSubDecor.findViewById(0x1020002)).addView(view, layoutParams);
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    @Override
    public boolean applyDayNight() {
        return this.applyDayNight(true);
    }

    @Override
    public Context attachBaseContext2(Context context) {
        Configuration configuration;
        boolean bl = true;
        this.mBaseContextAttached = true;
        int n = this.mapNightMode(context, this.calculateNightMode());
        if (sCanApplyOverrideConfiguration && context instanceof android.view.ContextThemeWrapper) {
            Configuration configuration2 = this.createOverrideConfigurationForDayNight(context, n, null);
            try {
                ContextThemeWrapperCompatApi17Impl.applyOverrideConfiguration((android.view.ContextThemeWrapper)context, configuration2);
                return context;
            }
            catch (IllegalStateException illegalStateException) {
                // empty catch block
            }
        }
        if (context instanceof ContextThemeWrapper) {
            Configuration configuration3 = this.createOverrideConfigurationForDayNight(context, n, null);
            try {
                ((ContextThemeWrapper)context).applyOverrideConfiguration(configuration3);
                return context;
            }
            catch (IllegalStateException illegalStateException) {
                // empty catch block
            }
        }
        if (!sCanReturnDifferentContext) {
            return super.attachBaseContext2(context);
        }
        try {
            configuration = context.getPackageManager().getResourcesForApplication(context.getApplicationInfo()).getConfiguration();
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            throw new RuntimeException("Application failed to obtain resources from itself", nameNotFoundException);
        }
        Object object = context.getResources().getConfiguration();
        object = !configuration.equals((Configuration)object) ? AppCompatDelegateImpl.generateConfigDelta(configuration, (Configuration)object) : null;
        configuration = this.createOverrideConfigurationForDayNight(context, n, (Configuration)object);
        object = new ContextThemeWrapper(context, R.style.Theme_AppCompat_Empty);
        ((ContextThemeWrapper)((Object)object)).applyOverrideConfiguration(configuration);
        try {
            context = context.getTheme();
            if (context == null) {
                bl = false;
            }
        }
        catch (NullPointerException nullPointerException) {
            bl = false;
        }
        if (bl) {
            ResourcesCompat.ThemeCompat.rebase(((ContextThemeWrapper)((Object)object)).getTheme());
        }
        return super.attachBaseContext2((Context)object);
    }

    void callOnPanelClosed(int n, PanelFeatureState panelFeatureState, Menu panelFeatureStateArray) {
        PanelFeatureState panelFeatureState2 = panelFeatureState;
        Object object = panelFeatureStateArray;
        if (panelFeatureStateArray == null) {
            PanelFeatureState panelFeatureState3 = panelFeatureState;
            if (panelFeatureState == null) {
                panelFeatureState3 = panelFeatureState;
                if (n >= 0) {
                    object = this.mPanels;
                    panelFeatureState3 = panelFeatureState;
                    if (n < ((PanelFeatureState[])object).length) {
                        panelFeatureState3 = object[n];
                    }
                }
            }
            panelFeatureState2 = panelFeatureState3;
            object = panelFeatureStateArray;
            if (panelFeatureState3 != null) {
                object = panelFeatureState3.menu;
                panelFeatureState2 = panelFeatureState3;
            }
        }
        if (panelFeatureState2 != null && !panelFeatureState2.isOpen) {
            return;
        }
        if (!this.mIsDestroyed) {
            this.mAppCompatWindowCallback.getWrapped().onPanelClosed(n, (Menu)object);
        }
    }

    void checkCloseActionMenu(MenuBuilder menuBuilder) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mDecorContentParent.dismissPopups();
        Window.Callback callback = this.getWindowCallback();
        if (callback != null && !this.mIsDestroyed) {
            callback.onPanelClosed(108, (Menu)menuBuilder);
        }
        this.mClosingActionMenu = false;
    }

    void closePanel(int n) {
        this.closePanel(this.getPanelState(n, true), true);
    }

    void closePanel(PanelFeatureState panelFeatureState, boolean bl) {
        DecorContentParent decorContentParent;
        if (bl && panelFeatureState.featureId == 0 && (decorContentParent = this.mDecorContentParent) != null && decorContentParent.isOverflowMenuShowing()) {
            this.checkCloseActionMenu(panelFeatureState.menu);
            return;
        }
        decorContentParent = (WindowManager)this.mContext.getSystemService("window");
        if (decorContentParent != null && panelFeatureState.isOpen && panelFeatureState.decorView != null) {
            decorContentParent.removeView((View)panelFeatureState.decorView);
            if (bl) {
                this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
            }
        }
        panelFeatureState.isPrepared = false;
        panelFeatureState.isHandled = false;
        panelFeatureState.isOpen = false;
        panelFeatureState.shownPanelView = null;
        panelFeatureState.refreshDecorView = true;
        if (this.mPreparedPanel == panelFeatureState) {
            this.mPreparedPanel = null;
        }
    }

    @Override
    public View createView(View view, String string2, Context context, AttributeSet attributeSet) {
        AppCompatViewInflater appCompatViewInflater = this.mAppCompatViewInflater;
        boolean bl = false;
        if (appCompatViewInflater == null) {
            String string3 = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme).getString(R.styleable.AppCompatTheme_viewInflaterClass);
            if (string3 == null) {
                this.mAppCompatViewInflater = new AppCompatViewInflater();
            } else {
                try {
                    this.mAppCompatViewInflater = (AppCompatViewInflater)Class.forName(string3).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                }
                catch (Throwable throwable) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to instantiate custom view inflater ");
                    stringBuilder.append(string3);
                    stringBuilder.append(". Falling back to default.");
                    Log.i((String)"AppCompatDelegate", (String)stringBuilder.toString(), (Throwable)throwable);
                    this.mAppCompatViewInflater = new AppCompatViewInflater();
                }
            }
        }
        boolean bl2 = false;
        boolean bl3 = IS_PRE_LOLLIPOP;
        if (bl3) {
            if (attributeSet instanceof XmlPullParser) {
                bl2 = bl;
                if (((XmlPullParser)attributeSet).getDepth() > 1) {
                    bl2 = true;
                }
            } else {
                bl2 = this.shouldInheritContext((ViewParent)view);
            }
        }
        return this.mAppCompatViewInflater.createView(view, string2, context, attributeSet, bl2, bl3, true, VectorEnabledTintResources.shouldBeUsed());
    }

    void dismissPopups() {
        Object object = this.mDecorContentParent;
        if (object != null) {
            object.dismissPopups();
        }
        if (this.mActionModePopup != null) {
            this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
            if (this.mActionModePopup.isShowing()) {
                try {
                    this.mActionModePopup.dismiss();
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    // empty catch block
                }
            }
            this.mActionModePopup = null;
        }
        this.endOnGoingFadeAnimation();
        object = this.getPanelState(0, false);
        if (object != null && ((PanelFeatureState)object).menu != null) {
            ((PanelFeatureState)object).menu.close();
        }
    }

    boolean dispatchKeyEvent(KeyEvent keyEvent) {
        Object object = this.mHost;
        boolean bl = object instanceof KeyEventDispatcher.Component;
        boolean bl2 = true;
        if ((bl || object instanceof AppCompatDialog) && (object = this.mWindow.getDecorView()) != null && KeyEventDispatcher.dispatchBeforeHierarchy((View)object, keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 82 && this.mAppCompatWindowCallback.getWrapped().dispatchKeyEvent(keyEvent)) {
            return true;
        }
        int n = keyEvent.getKeyCode();
        if (keyEvent.getAction() != 0) {
            bl2 = false;
        }
        bl = bl2 ? this.onKeyDown(n, keyEvent) : this.onKeyUp(n, keyEvent);
        return bl;
    }

    void doInvalidatePanelMenu(int n) {
        PanelFeatureState panelFeatureState = this.getPanelState(n, true);
        if (panelFeatureState.menu != null) {
            Bundle bundle = new Bundle();
            panelFeatureState.menu.saveActionViewStates(bundle);
            if (bundle.size() > 0) {
                panelFeatureState.frozenActionViewState = bundle;
            }
            panelFeatureState.menu.stopDispatchingItemsChanged();
            panelFeatureState.menu.clear();
        }
        panelFeatureState.refreshMenuContent = true;
        panelFeatureState.refreshDecorView = true;
        if ((n == 108 || n == 0) && this.mDecorContentParent != null && (panelFeatureState = this.getPanelState(0, false)) != null) {
            panelFeatureState.isPrepared = false;
            this.preparePanel(panelFeatureState, null);
        }
    }

    void endOnGoingFadeAnimation() {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mFadeAnim;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.cancel();
        }
    }

    PanelFeatureState findMenuPanel(Menu menu) {
        PanelFeatureState[] panelFeatureStateArray = this.mPanels;
        int n = panelFeatureStateArray != null ? panelFeatureStateArray.length : 0;
        for (int i = 0; i < n; ++i) {
            PanelFeatureState panelFeatureState = panelFeatureStateArray[i];
            if (panelFeatureState == null || panelFeatureState.menu != menu) continue;
            return panelFeatureState;
        }
        return null;
    }

    @Override
    public <T extends View> T findViewById(int n) {
        this.ensureSubDecor();
        return (T)this.mWindow.findViewById(n);
    }

    final Context getActionBarThemedContext() {
        Context context = null;
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            context = actionBar.getThemedContext();
        }
        actionBar = context;
        if (context == null) {
            actionBar = this.mContext;
        }
        return actionBar;
    }

    final AutoNightModeManager getAutoTimeNightModeManager() {
        return this.getAutoTimeNightModeManager(this.mContext);
    }

    @Override
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl(this);
    }

    @Override
    public int getLocalNightMode() {
        return this.mLocalNightMode;
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.initWindowDecorActionBar();
            ActionBar actionBar = this.mActionBar;
            actionBar = actionBar != null ? actionBar.getThemedContext() : this.mContext;
            this.mMenuInflater = new SupportMenuInflater((Context)actionBar);
        }
        return this.mMenuInflater;
    }

    protected PanelFeatureState getPanelState(int n, boolean bl) {
        Object object;
        PanelFeatureState[] panelFeatureStateArray;
        PanelFeatureState[] panelFeatureStateArray2;
        block6: {
            block5: {
                panelFeatureStateArray = panelFeatureStateArray2 = this.mPanels;
                if (panelFeatureStateArray2 == null) break block5;
                panelFeatureStateArray2 = panelFeatureStateArray;
                if (panelFeatureStateArray.length > n) break block6;
            }
            object = new PanelFeatureState[n + 1];
            if (panelFeatureStateArray != null) {
                System.arraycopy(panelFeatureStateArray, 0, object, 0, panelFeatureStateArray.length);
            }
            panelFeatureStateArray2 = object;
            this.mPanels = object;
        }
        object = panelFeatureStateArray2[n];
        panelFeatureStateArray = object;
        if (object == null) {
            object = new PanelFeatureState(n);
            panelFeatureStateArray = object;
            panelFeatureStateArray2[n] = object;
        }
        return panelFeatureStateArray;
    }

    ViewGroup getSubDecor() {
        return this.mSubDecor;
    }

    @Override
    public ActionBar getSupportActionBar() {
        this.initWindowDecorActionBar();
        return this.mActionBar;
    }

    final CharSequence getTitle() {
        Object object = this.mHost;
        if (object instanceof Activity) {
            return ((Activity)object).getTitle();
        }
        return this.mTitle;
    }

    final Window.Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }

    @Override
    public boolean hasWindowFeature(int n) {
        boolean bl = false;
        switch (this.sanitizeWindowFeatureId(n)) {
            default: {
                break;
            }
            case 109: {
                bl = this.mOverlayActionBar;
                break;
            }
            case 108: {
                bl = this.mHasActionBar;
                break;
            }
            case 10: {
                bl = this.mOverlayActionMode;
                break;
            }
            case 5: {
                bl = this.mFeatureIndeterminateProgress;
                break;
            }
            case 2: {
                bl = this.mFeatureProgress;
                break;
            }
            case 1: {
                bl = this.mWindowNoTitle;
            }
        }
        bl = bl || this.mWindow.hasFeature(n);
        return bl;
    }

    @Override
    public void installViewFactory() {
        LayoutInflater layoutInflater = LayoutInflater.from((Context)this.mContext);
        if (layoutInflater.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(layoutInflater, this);
        } else if (!(layoutInflater.getFactory2() instanceof AppCompatDelegateImpl)) {
            Log.i((String)"AppCompatDelegate", (String)"The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    @Override
    public void invalidateOptionsMenu() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null && actionBar.invalidateOptionsMenu()) {
            return;
        }
        this.invalidatePanelMenu(0);
    }

    @Override
    public boolean isHandleNativeActionModesEnabled() {
        return this.mHandleNativeActionModes;
    }

    int mapNightMode(Context context, int n) {
        switch (n) {
            default: {
                throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
            }
            case 3: {
                return this.getAutoBatteryNightModeManager(context).getApplyableNightMode();
            }
            case 0: {
                if (Build.VERSION.SDK_INT >= 23 && ((UiModeManager)context.getApplicationContext().getSystemService(UiModeManager.class)).getNightMode() == 0) {
                    return -1;
                }
                return this.getAutoTimeNightModeManager(context).getApplyableNightMode();
            }
            case -1: 
            case 1: 
            case 2: {
                return n;
            }
            case -100: 
        }
        return -1;
    }

    boolean onBackPressed() {
        Object object = this.mActionMode;
        if (object != null) {
            ((ActionMode)object).finish();
            return true;
        }
        object = this.getSupportActionBar();
        return object != null && ((ActionBar)object).collapseActionView();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        ActionBar actionBar;
        if (this.mHasActionBar && this.mSubDecorInstalled && (actionBar = this.getSupportActionBar()) != null) {
            actionBar.onConfigurationChanged(configuration);
        }
        AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
        this.applyDayNight(false);
    }

    @Override
    public void onCreate(Bundle object) {
        this.mBaseContextAttached = true;
        this.applyDayNight(false);
        this.ensureWindow();
        Object object2 = this.mHost;
        if (object2 instanceof Activity) {
            object = null;
            try {
                object = object2 = NavUtils.getParentActivityName((Activity)object2);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
            if (object != null) {
                object = this.peekSupportActionBar();
                if (object == null) {
                    this.mEnableDefaultActionBarUp = true;
                } else {
                    ((ActionBar)object).setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
            AppCompatDelegateImpl.addActiveDelegate(this);
        }
        this.mCreated = true;
    }

    public final View onCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
        return this.createView(view, string2, context, attributeSet);
    }

    public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
        return this.onCreateView(null, string2, context, attributeSet);
    }

    @Override
    public void onDestroy() {
        Object object;
        if (this.mHost instanceof Activity) {
            AppCompatDelegateImpl.removeActivityDelegate(this);
        }
        if (this.mInvalidatePanelMenuPosted) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
        }
        this.mStarted = false;
        this.mIsDestroyed = true;
        if (this.mLocalNightMode != -100 && (object = this.mHost) instanceof Activity && ((Activity)object).isChangingConfigurations()) {
            sLocalNightModes.put(this.mHost.getClass().getName(), this.mLocalNightMode);
        } else {
            sLocalNightModes.remove(this.mHost.getClass().getName());
        }
        object = this.mActionBar;
        if (object != null) {
            ((ActionBar)object).onDestroy();
        }
        this.cleanupAutoManagers();
    }

    boolean onKeyDown(int n, KeyEvent keyEvent) {
        boolean bl = true;
        switch (n) {
            default: {
                break;
            }
            case 82: {
                this.onKeyDownPanel(0, keyEvent);
                return true;
            }
            case 4: {
                if ((keyEvent.getFlags() & 0x80) == 0) {
                    bl = false;
                }
                this.mLongPressBackDown = bl;
            }
        }
        return false;
    }

    boolean onKeyShortcut(int n, KeyEvent object) {
        Object object2 = this.getSupportActionBar();
        if (object2 != null && ((ActionBar)object2).onKeyShortcut(n, (KeyEvent)object)) {
            return true;
        }
        object2 = this.mPreparedPanel;
        if (object2 != null && this.performPanelShortcut((PanelFeatureState)object2, object.getKeyCode(), (KeyEvent)object, 1)) {
            object = this.mPreparedPanel;
            if (object != null) {
                object.isHandled = true;
            }
            return true;
        }
        if (this.mPreparedPanel == null) {
            object2 = this.getPanelState(0, true);
            this.preparePanel((PanelFeatureState)object2, (KeyEvent)object);
            boolean bl = this.performPanelShortcut((PanelFeatureState)object2, object.getKeyCode(), (KeyEvent)object, 1);
            ((PanelFeatureState)object2).isPrepared = false;
            if (bl) {
                return true;
            }
        }
        return false;
    }

    boolean onKeyUp(int n, KeyEvent object) {
        switch (n) {
            default: {
                break;
            }
            case 82: {
                this.onKeyUpPanel(0, (KeyEvent)object);
                return true;
            }
            case 4: {
                boolean bl = this.mLongPressBackDown;
                this.mLongPressBackDown = false;
                object = this.getPanelState(0, false);
                if (object != null && object.isOpen) {
                    if (!bl) {
                        this.closePanel((PanelFeatureState)object, true);
                    }
                    return true;
                }
                if (!this.onBackPressed()) break;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onMenuItemSelected(MenuBuilder object, MenuItem menuItem) {
        Window.Callback callback = this.getWindowCallback();
        if (callback != null && !this.mIsDestroyed && (object = this.findMenuPanel(((MenuBuilder)object).getRootMenu())) != null) {
            return callback.onMenuItemSelected(((PanelFeatureState)object).featureId, menuItem);
        }
        return false;
    }

    @Override
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        this.reopenMenu(true);
    }

    void onMenuOpened(int n) {
        ActionBar actionBar;
        if (n == 108 && (actionBar = this.getSupportActionBar()) != null) {
            actionBar.dispatchMenuVisibilityChanged(true);
        }
    }

    void onPanelClosed(int n) {
        block2: {
            block1: {
                if (n != 108) break block1;
                ActionBar actionBar = this.getSupportActionBar();
                if (actionBar == null) break block2;
                actionBar.dispatchMenuVisibilityChanged(false);
                break block2;
            }
            if (n != 0) break block2;
            PanelFeatureState panelFeatureState = this.getPanelState(n, true);
            if (panelFeatureState.isOpen) {
                this.closePanel(panelFeatureState, false);
            }
        }
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        this.ensureSubDecor();
    }

    @Override
    public void onPostResume() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
    }

    @Override
    public void onStart() {
        this.mStarted = true;
        this.applyDayNight();
    }

    @Override
    public void onStop() {
        this.mStarted = false;
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(false);
        }
    }

    void onSubDecorInstalled(ViewGroup viewGroup) {
    }

    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }

    @Override
    public boolean requestWindowFeature(int n) {
        n = this.sanitizeWindowFeatureId(n);
        if (this.mWindowNoTitle && n == 108) {
            return false;
        }
        if (this.mHasActionBar && n == 1) {
            this.mHasActionBar = false;
        }
        switch (n) {
            default: {
                return this.mWindow.requestFeature(n);
            }
            case 109: {
                this.throwFeatureRequestIfSubDecorInstalled();
                this.mOverlayActionBar = true;
                return true;
            }
            case 108: {
                this.throwFeatureRequestIfSubDecorInstalled();
                this.mHasActionBar = true;
                return true;
            }
            case 10: {
                this.throwFeatureRequestIfSubDecorInstalled();
                this.mOverlayActionMode = true;
                return true;
            }
            case 5: {
                this.throwFeatureRequestIfSubDecorInstalled();
                this.mFeatureIndeterminateProgress = true;
                return true;
            }
            case 2: {
                this.throwFeatureRequestIfSubDecorInstalled();
                this.mFeatureProgress = true;
                return true;
            }
            case 1: 
        }
        this.throwFeatureRequestIfSubDecorInstalled();
        this.mWindowNoTitle = true;
        return true;
    }

    @Override
    public void setContentView(int n) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(0x1020002);
        viewGroup.removeAllViews();
        LayoutInflater.from((Context)this.mContext).inflate(n, viewGroup);
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(0x1020002);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(0x1020002);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    @Override
    public void setHandleNativeActionModesEnabled(boolean bl) {
        this.mHandleNativeActionModes = bl;
    }

    @Override
    public void setLocalNightMode(int n) {
        if (this.mLocalNightMode != n) {
            this.mLocalNightMode = n;
            if (this.mBaseContextAttached) {
                this.applyDayNight();
            }
        }
    }

    @Override
    public void setSupportActionBar(Toolbar object) {
        if (!(this.mHost instanceof Activity)) {
            return;
        }
        ActionBar actionBar = this.getSupportActionBar();
        if (!(actionBar instanceof WindowDecorActionBar)) {
            this.mMenuInflater = null;
            if (actionBar != null) {
                actionBar.onDestroy();
            }
            if (object != null) {
                object = new ToolbarActionBar((Toolbar)((Object)object), this.getTitle(), this.mAppCompatWindowCallback);
                this.mActionBar = object;
                this.mWindow.setCallback(((ToolbarActionBar)object).getWrappedWindowCallback());
            } else {
                this.mActionBar = null;
                this.mWindow.setCallback((Window.Callback)this.mAppCompatWindowCallback);
            }
            this.invalidateOptionsMenu();
            return;
        }
        throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
    }

    @Override
    public void setTheme(int n) {
        this.mThemeResId = n;
    }

    @Override
    public final void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.setWindowTitle(charSequence);
        } else if (this.peekSupportActionBar() != null) {
            this.peekSupportActionBar().setWindowTitle(charSequence);
        } else {
            decorContentParent = this.mTitleView;
            if (decorContentParent != null) {
                decorContentParent.setText(charSequence);
            }
        }
    }

    final boolean shouldAnimateActionModeView() {
        ViewGroup viewGroup;
        boolean bl = this.mSubDecorInstalled && (viewGroup = this.mSubDecor) != null && ViewCompat.isLaidOut((View)viewGroup);
        return bl;
    }

    @Override
    public ActionMode startSupportActionMode(ActionMode.Callback callback) {
        if (callback != null) {
            Object object = this.mActionMode;
            if (object != null) {
                ((ActionMode)object).finish();
            }
            callback = new ActionModeCallbackWrapperV9(this, callback);
            object = this.getSupportActionBar();
            if (object != null) {
                ActionMode actionMode;
                this.mActionMode = actionMode = ((ActionBar)object).startActionMode(callback);
                if (actionMode != null && (object = this.mAppCompatCallback) != null) {
                    object.onSupportActionModeStarted(actionMode);
                }
            }
            if (this.mActionMode == null) {
                this.mActionMode = this.startSupportActionModeFromWindow(callback);
            }
            return this.mActionMode;
        }
        throw new IllegalArgumentException("ActionMode callback can not be null.");
    }

    ActionMode startSupportActionModeFromWindow(ActionMode.Callback object) {
        this.endOnGoingFadeAnimation();
        Object object2 = this.mActionMode;
        if (object2 != null) {
            ((ActionMode)object2).finish();
        }
        object2 = object;
        if (!(object instanceof ActionModeCallbackWrapperV9)) {
            object2 = new ActionModeCallbackWrapperV9(this, (ActionMode.Callback)object);
        }
        Object object3 = null;
        AppCompatCallback appCompatCallback = this.mAppCompatCallback;
        object = object3;
        if (appCompatCallback != null) {
            object = object3;
            if (!this.mIsDestroyed) {
                try {
                    object = appCompatCallback.onWindowStartingSupportActionMode((ActionMode.Callback)object2);
                }
                catch (AbstractMethodError abstractMethodError) {
                    object = object3;
                }
            }
        }
        if (object != null) {
            this.mActionMode = object;
        } else {
            object = this.mActionModeView;
            boolean bl = true;
            if (object == null) {
                if (this.mIsFloating) {
                    object3 = new TypedValue();
                    object = this.mContext.getTheme();
                    object.resolveAttribute(R.attr.actionBarTheme, object3, true);
                    if (object3.resourceId != 0) {
                        appCompatCallback = this.mContext.getResources().newTheme();
                        appCompatCallback.setTo((Resources.Theme)object);
                        appCompatCallback.applyStyle(object3.resourceId, true);
                        object = new ContextThemeWrapper(this.mContext, 0);
                        object.getTheme().setTo((Resources.Theme)appCompatCallback);
                    } else {
                        object = this.mContext;
                    }
                    this.mActionModeView = new ActionBarContextView((Context)object);
                    appCompatCallback = new PopupWindow((Context)object, null, R.attr.actionModePopupWindowStyle);
                    this.mActionModePopup = appCompatCallback;
                    PopupWindowCompat.setWindowLayoutType((PopupWindow)appCompatCallback, 2);
                    this.mActionModePopup.setContentView((View)this.mActionModeView);
                    this.mActionModePopup.setWidth(-1);
                    object.getTheme().resolveAttribute(R.attr.actionBarSize, object3, true);
                    int n = TypedValue.complexToDimensionPixelSize((int)object3.data, (DisplayMetrics)object.getResources().getDisplayMetrics());
                    this.mActionModeView.setContentHeight(n);
                    this.mActionModePopup.setHeight(-2);
                    this.mShowActionModePopup = new Runnable(this){
                        final AppCompatDelegateImpl this$0;
                        {
                            this.this$0 = appCompatDelegateImpl;
                        }

                        @Override
                        public void run() {
                            this.this$0.mActionModePopup.showAtLocation((View)this.this$0.mActionModeView, 55, 0, 0);
                            this.this$0.endOnGoingFadeAnimation();
                            if (this.this$0.shouldAnimateActionModeView()) {
                                this.this$0.mActionModeView.setAlpha(0.0f);
                                AppCompatDelegateImpl appCompatDelegateImpl = this.this$0;
                                appCompatDelegateImpl.mFadeAnim = ViewCompat.animate((View)appCompatDelegateImpl.mActionModeView).alpha(1.0f);
                                this.this$0.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter(this){
                                    final 6 this$1;
                                    {
                                        this.this$1 = var1_1;
                                    }

                                    @Override
                                    public void onAnimationEnd(View view) {
                                        this.this$1.this$0.mActionModeView.setAlpha(1.0f);
                                        this.this$1.this$0.mFadeAnim.setListener(null);
                                        this.this$1.this$0.mFadeAnim = null;
                                    }

                                    @Override
                                    public void onAnimationStart(View view) {
                                        this.this$1.this$0.mActionModeView.setVisibility(0);
                                    }
                                });
                            } else {
                                this.this$0.mActionModeView.setAlpha(1.0f);
                                this.this$0.mActionModeView.setVisibility(0);
                            }
                        }
                    };
                } else {
                    object = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
                    if (object != null) {
                        ((ViewStubCompat)((Object)object)).setLayoutInflater(LayoutInflater.from((Context)this.getActionBarThemedContext()));
                        this.mActionModeView = (ActionBarContextView)((ViewStubCompat)((Object)object)).inflate();
                    }
                }
            }
            if (this.mActionModeView != null) {
                this.endOnGoingFadeAnimation();
                this.mActionModeView.killMode();
                object = this.mActionModeView.getContext();
                object3 = this.mActionModeView;
                if (this.mActionModePopup != null) {
                    bl = false;
                }
                object = new StandaloneActionMode((Context)object, (ActionBarContextView)((Object)object3), (ActionMode.Callback)object2, bl);
                if (object2.onCreateActionMode((ActionMode)object, ((ActionMode)object).getMenu())) {
                    ((ActionMode)object).invalidate();
                    this.mActionModeView.initForMode((ActionMode)object);
                    this.mActionMode = object;
                    if (this.shouldAnimateActionModeView()) {
                        this.mActionModeView.setAlpha(0.0f);
                        object = ViewCompat.animate((View)this.mActionModeView).alpha(1.0f);
                        this.mFadeAnim = object;
                        ((ViewPropertyAnimatorCompat)object).setListener(new ViewPropertyAnimatorListenerAdapter(this){
                            final AppCompatDelegateImpl this$0;
                            {
                                this.this$0 = appCompatDelegateImpl;
                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                this.this$0.mActionModeView.setAlpha(1.0f);
                                this.this$0.mFadeAnim.setListener(null);
                                this.this$0.mFadeAnim = null;
                            }

                            @Override
                            public void onAnimationStart(View view) {
                                this.this$0.mActionModeView.setVisibility(0);
                                this.this$0.mActionModeView.sendAccessibilityEvent(32);
                                if (this.this$0.mActionModeView.getParent() instanceof View) {
                                    ViewCompat.requestApplyInsets((View)this.this$0.mActionModeView.getParent());
                                }
                            }
                        });
                    } else {
                        this.mActionModeView.setAlpha(1.0f);
                        this.mActionModeView.setVisibility(0);
                        this.mActionModeView.sendAccessibilityEvent(32);
                        if (this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent());
                        }
                    }
                    if (this.mActionModePopup != null) {
                        this.mWindow.getDecorView().post(this.mShowActionModePopup);
                    }
                } else {
                    this.mActionMode = null;
                }
            }
        }
        object = this.mActionMode;
        if (object != null && (object2 = this.mAppCompatCallback) != null) {
            object2.onSupportActionModeStarted((ActionMode)object);
        }
        return this.mActionMode;
    }

    final int updateStatusGuard(WindowInsetsCompat windowInsetsCompat, Rect rect) {
        int n;
        int n2 = 0;
        if (windowInsetsCompat != null) {
            n2 = windowInsetsCompat.getSystemWindowInsetTop();
        } else if (rect != null) {
            n2 = rect.top;
        }
        int n3 = 0;
        int n4 = 0;
        ActionBarContextView actionBarContextView = this.mActionModeView;
        if (actionBarContextView != null && actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            actionBarContextView = (ViewGroup.MarginLayoutParams)this.mActionModeView.getLayoutParams();
            n = 0;
            int n5 = 0;
            if (this.mActionModeView.isShown()) {
                if (this.mTempRect1 == null) {
                    this.mTempRect1 = new Rect();
                    this.mTempRect2 = new Rect();
                }
                Rect rect2 = this.mTempRect1;
                Rect rect3 = this.mTempRect2;
                if (windowInsetsCompat == null) {
                    rect2.set(rect);
                } else {
                    rect2.set(windowInsetsCompat.getSystemWindowInsetLeft(), windowInsetsCompat.getSystemWindowInsetTop(), windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                }
                ViewUtils.computeFitSystemWindows((View)this.mSubDecor, rect2, rect3);
                n3 = rect2.top;
                int n6 = rect2.left;
                int n7 = rect2.right;
                windowInsetsCompat = ViewCompat.getRootWindowInsets((View)this.mSubDecor);
                n4 = windowInsetsCompat == null ? 0 : windowInsetsCompat.getSystemWindowInsetLeft();
                n = windowInsetsCompat == null ? 0 : windowInsetsCompat.getSystemWindowInsetRight();
                if (((ViewGroup.MarginLayoutParams)actionBarContextView).topMargin != n3 || ((ViewGroup.MarginLayoutParams)actionBarContextView).leftMargin != n6 || ((ViewGroup.MarginLayoutParams)actionBarContextView).rightMargin != n7) {
                    ((ViewGroup.MarginLayoutParams)actionBarContextView).topMargin = n3;
                    ((ViewGroup.MarginLayoutParams)actionBarContextView).leftMargin = n6;
                    ((ViewGroup.MarginLayoutParams)actionBarContextView).rightMargin = n7;
                    n5 = 1;
                }
                if (n3 > 0 && this.mStatusGuard == null) {
                    windowInsetsCompat = new View(this.mContext);
                    this.mStatusGuard = windowInsetsCompat;
                    windowInsetsCompat.setVisibility(8);
                    windowInsetsCompat = new FrameLayout.LayoutParams(-1, ((ViewGroup.MarginLayoutParams)actionBarContextView).topMargin, 51);
                    ((FrameLayout.LayoutParams)windowInsetsCompat).leftMargin = n4;
                    ((FrameLayout.LayoutParams)windowInsetsCompat).rightMargin = n;
                    this.mSubDecor.addView(this.mStatusGuard, -1, (ViewGroup.LayoutParams)windowInsetsCompat);
                } else {
                    windowInsetsCompat = this.mStatusGuard;
                    if (windowInsetsCompat != null) {
                        windowInsetsCompat = (ViewGroup.MarginLayoutParams)windowInsetsCompat.getLayoutParams();
                        if (((ViewGroup.MarginLayoutParams)windowInsetsCompat).height != ((ViewGroup.MarginLayoutParams)actionBarContextView).topMargin || ((ViewGroup.MarginLayoutParams)windowInsetsCompat).leftMargin != n4 || ((ViewGroup.MarginLayoutParams)windowInsetsCompat).rightMargin != n) {
                            ((ViewGroup.MarginLayoutParams)windowInsetsCompat).height = ((ViewGroup.MarginLayoutParams)actionBarContextView).topMargin;
                            ((ViewGroup.MarginLayoutParams)windowInsetsCompat).leftMargin = n4;
                            ((ViewGroup.MarginLayoutParams)windowInsetsCompat).rightMargin = n;
                            this.mStatusGuard.setLayoutParams((ViewGroup.LayoutParams)windowInsetsCompat);
                        }
                    }
                }
                windowInsetsCompat = this.mStatusGuard;
                n4 = windowInsetsCompat != null ? 1 : 0;
                if (n4 != 0 && windowInsetsCompat.getVisibility() != 0) {
                    this.updateStatusGuardColor(this.mStatusGuard);
                }
                if (!this.mOverlayActionMode && n4 != 0) {
                    n2 = 0;
                }
            } else if (((ViewGroup.MarginLayoutParams)actionBarContextView).topMargin != 0) {
                n5 = 1;
                ((ViewGroup.MarginLayoutParams)actionBarContextView).topMargin = 0;
            } else {
                n5 = n;
            }
            n = n2;
            n3 = n4;
            if (n5 != 0) {
                this.mActionModeView.setLayoutParams((ViewGroup.LayoutParams)actionBarContextView);
                n = n2;
                n3 = n4;
            }
        } else {
            n = n2;
        }
        if ((windowInsetsCompat = this.mStatusGuard) != null) {
            n2 = n3 != 0 ? 0 : 8;
            windowInsetsCompat.setVisibility(n2);
        }
        return n;
    }

    private class ActionBarDrawableToggleImpl
    implements ActionBarDrawerToggle.Delegate {
        final AppCompatDelegateImpl this$0;

        ActionBarDrawableToggleImpl(AppCompatDelegateImpl appCompatDelegateImpl) {
            this.this$0 = appCompatDelegateImpl;
        }

        @Override
        public Context getActionBarThemedContext() {
            return this.this$0.getActionBarThemedContext();
        }

        @Override
        public Drawable getThemeUpIndicator() {
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.getActionBarThemedContext(), null, new int[]{R.attr.homeAsUpIndicator});
            Drawable drawable2 = tintTypedArray.getDrawable(0);
            tintTypedArray.recycle();
            return drawable2;
        }

        @Override
        public boolean isNavigationVisible() {
            ActionBar actionBar = this.this$0.getSupportActionBar();
            boolean bl = actionBar != null && (actionBar.getDisplayOptions() & 4) != 0;
            return bl;
        }

        @Override
        public void setActionBarDescription(int n) {
            ActionBar actionBar = this.this$0.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeActionContentDescription(n);
            }
        }

        @Override
        public void setActionBarUpIndicator(Drawable drawable2, int n) {
            ActionBar actionBar = this.this$0.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(drawable2);
                actionBar.setHomeActionContentDescription(n);
            }
        }
    }

    private final class ActionMenuPresenterCallback
    implements MenuPresenter.Callback {
        final AppCompatDelegateImpl this$0;

        ActionMenuPresenterCallback(AppCompatDelegateImpl appCompatDelegateImpl) {
            this.this$0 = appCompatDelegateImpl;
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
            this.this$0.checkCloseActionMenu(menuBuilder);
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback callback = this.this$0.getWindowCallback();
            if (callback != null) {
                callback.onMenuOpened(108, (Menu)menuBuilder);
            }
            return true;
        }
    }

    class ActionModeCallbackWrapperV9
    implements ActionMode.Callback {
        private ActionMode.Callback mWrapped;
        final AppCompatDelegateImpl this$0;

        public ActionModeCallbackWrapperV9(AppCompatDelegateImpl appCompatDelegateImpl, ActionMode.Callback callback) {
            this.this$0 = appCompatDelegateImpl;
            this.mWrapped = callback;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override
        public void onDestroyActionMode(ActionMode object) {
            this.mWrapped.onDestroyActionMode((ActionMode)object);
            if (this.this$0.mActionModePopup != null) {
                this.this$0.mWindow.getDecorView().removeCallbacks(this.this$0.mShowActionModePopup);
            }
            if (this.this$0.mActionModeView != null) {
                this.this$0.endOnGoingFadeAnimation();
                object = this.this$0;
                ((AppCompatDelegateImpl)object).mFadeAnim = ViewCompat.animate((View)((AppCompatDelegateImpl)object).mActionModeView).alpha(0.0f);
                this.this$0.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter(this){
                    final ActionModeCallbackWrapperV9 this$1;
                    {
                        this.this$1 = actionModeCallbackWrapperV9;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        this.this$1.this$0.mActionModeView.setVisibility(8);
                        if (this.this$1.this$0.mActionModePopup != null) {
                            this.this$1.this$0.mActionModePopup.dismiss();
                        } else if (this.this$1.this$0.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View)this.this$1.this$0.mActionModeView.getParent());
                        }
                        this.this$1.this$0.mActionModeView.removeAllViews();
                        this.this$1.this$0.mFadeAnim.setListener(null);
                        this.this$1.this$0.mFadeAnim = null;
                        ViewCompat.requestApplyInsets((View)this.this$1.this$0.mSubDecor);
                    }
                });
            }
            if (this.this$0.mAppCompatCallback != null) {
                this.this$0.mAppCompatCallback.onSupportActionModeFinished(this.this$0.mActionMode);
            }
            this.this$0.mActionMode = null;
            ViewCompat.requestApplyInsets((View)this.this$0.mSubDecor);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            ViewCompat.requestApplyInsets((View)this.this$0.mSubDecor);
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }
    }

    class AppCompatWindowCallback
    extends WindowCallbackWrapper {
        final AppCompatDelegateImpl this$0;

        AppCompatWindowCallback(AppCompatDelegateImpl appCompatDelegateImpl, Window.Callback callback) {
            this.this$0 = appCompatDelegateImpl;
            super(callback);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            boolean bl = this.this$0.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
            return bl;
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            boolean bl = super.dispatchKeyShortcutEvent(keyEvent) || this.this$0.onKeyShortcut(keyEvent.getKeyCode(), keyEvent);
            return bl;
        }

        @Override
        public void onContentChanged() {
        }

        @Override
        public boolean onCreatePanelMenu(int n, Menu menu) {
            if (n == 0 && !(menu instanceof MenuBuilder)) {
                return false;
            }
            return super.onCreatePanelMenu(n, menu);
        }

        @Override
        public boolean onMenuOpened(int n, Menu menu) {
            super.onMenuOpened(n, menu);
            this.this$0.onMenuOpened(n);
            return true;
        }

        @Override
        public void onPanelClosed(int n, Menu menu) {
            super.onPanelClosed(n, menu);
            this.this$0.onPanelClosed(n);
        }

        @Override
        public boolean onPreparePanel(int n, View view, Menu menu) {
            MenuBuilder menuBuilder = menu instanceof MenuBuilder ? (MenuBuilder)menu : null;
            if (n == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(true);
            }
            boolean bl = super.onPreparePanel(n, view, menu);
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(false);
            }
            return bl;
        }

        @Override
        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int n) {
            PanelFeatureState panelFeatureState = this.this$0.getPanelState(0, true);
            if (panelFeatureState != null && panelFeatureState.menu != null) {
                super.onProvideKeyboardShortcuts(list, panelFeatureState.menu, n);
            } else {
                super.onProvideKeyboardShortcuts(list, menu, n);
            }
        }

        @Override
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            if (Build.VERSION.SDK_INT >= 23) {
                return null;
            }
            if (this.this$0.isHandleNativeActionModesEnabled()) {
                return this.startAsSupportActionMode(callback);
            }
            return super.onWindowStartingActionMode(callback);
        }

        @Override
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int n) {
            if (this.this$0.isHandleNativeActionModesEnabled()) {
                switch (n) {
                    default: {
                        break;
                    }
                    case 0: {
                        return this.startAsSupportActionMode(callback);
                    }
                }
            }
            return super.onWindowStartingActionMode(callback, n);
        }

        final android.view.ActionMode startAsSupportActionMode(ActionMode.Callback object) {
            SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(this.this$0.mContext, (ActionMode.Callback)object);
            if ((object = this.this$0.startSupportActionMode(callbackWrapper)) != null) {
                return callbackWrapper.getActionModeWrapper((ActionMode)object);
            }
            return null;
        }
    }

    private class AutoBatteryNightModeManager
    extends AutoNightModeManager {
        private final PowerManager mPowerManager;
        final AppCompatDelegateImpl this$0;

        AutoBatteryNightModeManager(AppCompatDelegateImpl appCompatDelegateImpl, Context context) {
            this.this$0 = appCompatDelegateImpl;
            super(appCompatDelegateImpl);
            this.mPowerManager = (PowerManager)context.getApplicationContext().getSystemService("power");
        }

        @Override
        IntentFilter createIntentFilterForBroadcastReceiver() {
            if (Build.VERSION.SDK_INT >= 21) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
                return intentFilter;
            }
            return null;
        }

        @Override
        public int getApplyableNightMode() {
            int n = Build.VERSION.SDK_INT;
            int n2 = 1;
            if (n >= 21) {
                if (this.mPowerManager.isPowerSaveMode()) {
                    n2 = 2;
                }
                return n2;
            }
            return 1;
        }

        @Override
        public void onChange() {
            this.this$0.applyDayNight();
        }
    }

    abstract class AutoNightModeManager {
        private BroadcastReceiver mReceiver;
        final AppCompatDelegateImpl this$0;

        AutoNightModeManager(AppCompatDelegateImpl appCompatDelegateImpl) {
            this.this$0 = appCompatDelegateImpl;
        }

        void cleanup() {
            if (this.mReceiver != null) {
                try {
                    this.this$0.mContext.unregisterReceiver(this.mReceiver);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    // empty catch block
                }
                this.mReceiver = null;
            }
        }

        abstract IntentFilter createIntentFilterForBroadcastReceiver();

        abstract int getApplyableNightMode();

        boolean isListening() {
            boolean bl = this.mReceiver != null;
            return bl;
        }

        abstract void onChange();

        void setup() {
            this.cleanup();
            IntentFilter intentFilter = this.createIntentFilterForBroadcastReceiver();
            if (intentFilter != null && intentFilter.countActions() != 0) {
                if (this.mReceiver == null) {
                    this.mReceiver = new BroadcastReceiver(this){
                        final AutoNightModeManager this$1;
                        {
                            this.this$1 = autoNightModeManager;
                        }

                        public void onReceive(Context context, Intent intent) {
                            this.this$1.onChange();
                        }
                    };
                }
                this.this$0.mContext.registerReceiver(this.mReceiver, intentFilter);
                return;
            }
        }
    }

    private class AutoTimeNightModeManager
    extends AutoNightModeManager {
        private final TwilightManager mTwilightManager;
        final AppCompatDelegateImpl this$0;

        AutoTimeNightModeManager(AppCompatDelegateImpl appCompatDelegateImpl, TwilightManager twilightManager) {
            this.this$0 = appCompatDelegateImpl;
            super(appCompatDelegateImpl);
            this.mTwilightManager = twilightManager;
        }

        @Override
        IntentFilter createIntentFilterForBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.TIME_TICK");
            return intentFilter;
        }

        @Override
        public int getApplyableNightMode() {
            int n = this.mTwilightManager.isNight() ? 2 : 1;
            return n;
        }

        @Override
        public void onChange() {
            this.this$0.applyDayNight();
        }
    }

    static class ConfigurationImplApi17 {
        private ConfigurationImplApi17() {
        }

        static void generateConfigDelta_densityDpi(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            if (configuration.densityDpi != configuration2.densityDpi) {
                configuration3.densityDpi = configuration2.densityDpi;
            }
        }
    }

    static class ConfigurationImplApi24 {
        private ConfigurationImplApi24() {
        }

        static void generateConfigDelta_locale(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            LocaleList localeList;
            if (!(configuration = configuration.getLocales()).equals((Object)(localeList = configuration2.getLocales()))) {
                configuration3.setLocales(localeList);
                configuration3.locale = configuration2.locale;
            }
        }
    }

    static class ConfigurationImplApi26 {
        private ConfigurationImplApi26() {
        }

        static void generateConfigDelta_colorMode(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            if ((configuration.colorMode & 3) != (configuration2.colorMode & 3)) {
                configuration3.colorMode |= configuration2.colorMode & 3;
            }
            if ((configuration.colorMode & 0xC) != (configuration2.colorMode & 0xC)) {
                configuration3.colorMode |= configuration2.colorMode & 0xC;
            }
        }
    }

    private static class ContextThemeWrapperCompatApi17Impl {
        private ContextThemeWrapperCompatApi17Impl() {
        }

        static void applyOverrideConfiguration(android.view.ContextThemeWrapper contextThemeWrapper, Configuration configuration) {
            contextThemeWrapper.applyOverrideConfiguration(configuration);
        }
    }

    private class ListMenuDecorView
    extends ContentFrameLayout {
        final AppCompatDelegateImpl this$0;

        public ListMenuDecorView(AppCompatDelegateImpl appCompatDelegateImpl, Context context) {
            this.this$0 = appCompatDelegateImpl;
            super(context);
        }

        private boolean isOutOfBounds(int n, int n2) {
            boolean bl = n < -5 || n2 < -5 || n > this.getWidth() + 5 || n2 > this.getHeight() + 5;
            return bl;
        }

        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            boolean bl = this.this$0.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
            return bl;
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && this.isOutOfBounds((int)motionEvent.getX(), (int)motionEvent.getY())) {
                this.this$0.closePanel(0);
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        public void setBackgroundResource(int n) {
            this.setBackgroundDrawable(AppCompatResources.getDrawable(this.getContext(), n));
        }
    }

    protected static final class PanelFeatureState {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastOpen;
        int windowAnimations;
        int x;
        int y;

        PanelFeatureState(int n) {
            this.featureId = n;
            this.refreshDecorView = false;
        }

        void applyFrozenState() {
            Bundle bundle;
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null && (bundle = this.frozenMenuState) != null) {
                menuBuilder.restorePresenterStates(bundle);
                this.frozenMenuState = null;
            }
        }

        public void clearMenuPresenters() {
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null) {
                menuBuilder.removeMenuPresenter(this.listMenuPresenter);
            }
            this.listMenuPresenter = null;
        }

        MenuView getListMenuView(MenuPresenter.Callback callback) {
            if (this.menu == null) {
                return null;
            }
            if (this.listMenuPresenter == null) {
                ListMenuPresenter listMenuPresenter;
                this.listMenuPresenter = listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout);
                listMenuPresenter.setCallback(callback);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }

        public boolean hasPanelItems() {
            View view = this.shownPanelView;
            boolean bl = false;
            if (view == null) {
                return false;
            }
            if (this.createdPanelView != null) {
                return true;
            }
            if (this.listMenuPresenter.getAdapter().getCount() > 0) {
                bl = true;
            }
            return bl;
        }

        void onRestoreInstanceState(Parcelable parcelable) {
            parcelable = (SavedState)parcelable;
            this.featureId = parcelable.featureId;
            this.wasLastOpen = parcelable.isOpen;
            this.frozenMenuState = parcelable.menuState;
            this.shownPanelView = null;
            this.decorView = null;
        }

        Parcelable onSaveInstanceState() {
            SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return savedState;
        }

        void setMenu(MenuBuilder menuBuilder) {
            Object object = this.menu;
            if (menuBuilder == object) {
                return;
            }
            if (object != null) {
                ((MenuBuilder)object).removeMenuPresenter(this.listMenuPresenter);
            }
            this.menu = menuBuilder;
            if (menuBuilder != null && (object = this.listMenuPresenter) != null) {
                menuBuilder.addMenuPresenter((MenuPresenter)object);
            }
        }

        void setStyle(Context object) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = object.getResources().newTheme();
            theme.setTo(object.getTheme());
            theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            } else {
                theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
            }
            object = new ContextThemeWrapper((Context)object, 0);
            object.getTheme().setTo(theme);
            this.listPresenterContext = object;
            object = object.obtainStyledAttributes(R.styleable.AppCompatTheme);
            this.background = object.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
            this.windowAnimations = object.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            object.recycle();
        }

        private static class SavedState
        implements Parcelable {
            public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

                public SavedState createFromParcel(Parcel parcel) {
                    return SavedState.readFromParcel(parcel, null);
                }

                public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return SavedState.readFromParcel(parcel, classLoader);
                }

                public SavedState[] newArray(int n) {
                    return new SavedState[n];
                }
            };
            int featureId;
            boolean isOpen;
            Bundle menuState;

            SavedState() {
            }

            static SavedState readFromParcel(Parcel parcel, ClassLoader classLoader) {
                SavedState savedState = new SavedState();
                savedState.featureId = parcel.readInt();
                int n = parcel.readInt();
                boolean bl = true;
                if (n != 1) {
                    bl = false;
                }
                savedState.isOpen = bl;
                if (bl) {
                    savedState.menuState = parcel.readBundle(classLoader);
                }
                return savedState;
            }

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(Parcel parcel, int n) {
                parcel.writeInt(this.featureId);
                parcel.writeInt(this.isOpen ? 1 : 0);
                if (this.isOpen) {
                    parcel.writeBundle(this.menuState);
                }
            }
        }
    }

    private final class PanelMenuPresenterCallback
    implements MenuPresenter.Callback {
        final AppCompatDelegateImpl this$0;

        PanelMenuPresenterCallback(AppCompatDelegateImpl appCompatDelegateImpl) {
            this.this$0 = appCompatDelegateImpl;
        }

        @Override
        public void onCloseMenu(MenuBuilder object, boolean bl) {
            MenuBuilder menuBuilder = ((MenuBuilder)object).getRootMenu();
            boolean bl2 = menuBuilder != object;
            AppCompatDelegateImpl appCompatDelegateImpl = this.this$0;
            if (bl2) {
                object = menuBuilder;
            }
            if ((object = appCompatDelegateImpl.findMenuPanel((Menu)object)) != null) {
                if (bl2) {
                    this.this$0.callOnPanelClosed(((PanelFeatureState)object).featureId, (PanelFeatureState)object, menuBuilder);
                    this.this$0.closePanel((PanelFeatureState)object, true);
                } else {
                    this.this$0.closePanel((PanelFeatureState)object, bl);
                }
            }
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback callback;
            if (menuBuilder == menuBuilder.getRootMenu() && this.this$0.mHasActionBar && (callback = this.this$0.getWindowCallback()) != null && !this.this$0.mIsDestroyed) {
                callback.onMenuOpened(108, (Menu)menuBuilder);
            }
            return true;
        }
    }
}

