/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.text.TextUtils
 *  android.view.View
 *  android.view.ViewGroup$LayoutParams
 */
package androidx.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.ImmLeaksCleaner;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.contextaware.ContextAware;
import androidx.activity.contextaware.ContextAwareHelper;
import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;
import androidx.savedstate.ViewTreeSavedStateRegistryOwner;
import androidx.tracing.Trace;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentActivity
extends androidx.core.app.ComponentActivity
implements ContextAware,
LifecycleOwner,
ViewModelStoreOwner,
HasDefaultViewModelProviderFactory,
SavedStateRegistryOwner,
OnBackPressedDispatcherOwner,
ActivityResultRegistryOwner,
ActivityResultCaller {
    private final ActivityResultRegistry mActivityResultRegistry;
    private int mContentLayoutId;
    final ContextAwareHelper mContextAwareHelper = new ContextAwareHelper();
    private ViewModelProvider.Factory mDefaultFactory;
    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    private final AtomicInteger mNextLocalRequestCode;
    private final OnBackPressedDispatcher mOnBackPressedDispatcher;
    final SavedStateRegistryController mSavedStateRegistryController = SavedStateRegistryController.create(this);
    private ViewModelStore mViewModelStore;

    public ComponentActivity() {
        this.mOnBackPressedDispatcher = new OnBackPressedDispatcher(new Runnable(this){
            final ComponentActivity this$0;
            {
                this.this$0 = componentActivity;
            }

            @Override
            public void run() {
                IllegalStateException illegalStateException2;
                block2: {
                    try {
                        ComponentActivity.super.onBackPressed();
                    }
                    catch (IllegalStateException illegalStateException2) {
                        if (!TextUtils.equals((CharSequence)illegalStateException2.getMessage(), (CharSequence)"Can not perform this action after onSaveInstanceState")) break block2;
                    }
                    return;
                }
                throw illegalStateException2;
            }
        });
        this.mNextLocalRequestCode = new AtomicInteger();
        this.mActivityResultRegistry = new ActivityResultRegistry(this){
            final ComponentActivity this$0;
            {
                this.this$0 = componentActivity;
            }

            /*
             * WARNING - void declaration
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public <I, O> void onLaunch(int n, ActivityResultContract<I, O> object, I object2, ActivityOptionsCompat activityOptionsCompat) {
                void var2_5;
                block10: {
                    int n3;
                    int n2;
                    ComponentActivity componentActivity = this.this$0;
                    ActivityResultContract.SynchronousResult synchronousResult = ((ActivityResultContract)object).getSynchronousResult((Context)componentActivity, object2);
                    if (synchronousResult != null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable(this, n, synchronousResult){
                            final 2 this$1;
                            final int val$requestCode;
                            final ActivityResultContract.SynchronousResult val$synchronousResult;
                            {
                                this.this$1 = var1_1;
                                this.val$requestCode = n;
                                this.val$synchronousResult = synchronousResult;
                            }

                            @Override
                            public void run() {
                                this.this$1.dispatchResult(this.val$requestCode, this.val$synchronousResult.getValue());
                            }
                        });
                        return;
                    }
                    if ((object2 = ((ActivityResultContract)object).createIntent((Context)componentActivity, object2)).hasExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE")) {
                        object = object2.getBundleExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
                        object2.removeExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
                    } else {
                        object = activityOptionsCompat != null ? activityOptionsCompat.toBundle() : null;
                    }
                    if ("androidx.activity.result.contract.action.REQUEST_PERMISSIONS".equals(object2.getAction())) {
                        object = object2 = object2.getStringArrayExtra("androidx.activity.result.contract.extra.PERMISSIONS");
                        if (object2 == null) {
                            object = new String[]{};
                        }
                        ActivityCompat.requestPermissions(componentActivity, (String[])object, n);
                        return;
                    }
                    if (!"androidx.activity.result.contract.action.INTENT_SENDER_REQUEST".equals(object2.getAction())) {
                        ActivityCompat.startActivityForResult(componentActivity, (Intent)object2, n, (Bundle)object);
                        return;
                    }
                    synchronousResult = (IntentSenderRequest)object2.getParcelableExtra("androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST");
                    try {
                        activityOptionsCompat = ((IntentSenderRequest)((Object)synchronousResult)).getIntentSender();
                        object2 = ((IntentSenderRequest)((Object)synchronousResult)).getFillInIntent();
                        n2 = ((IntentSenderRequest)((Object)synchronousResult)).getFlagsMask();
                        n3 = ((IntentSenderRequest)((Object)synchronousResult)).getFlagsValues();
                    }
                    catch (IntentSender.SendIntentException sendIntentException) {
                        // empty catch block
                        break block10;
                    }
                    try {
                        ActivityCompat.startIntentSenderForResult(componentActivity, (IntentSender)activityOptionsCompat, n, (Intent)object2, n2, n3, 0, (Bundle)object);
                        return;
                    }
                    catch (IntentSender.SendIntentException sendIntentException) {}
                }
                new Handler(Looper.getMainLooper()).post(new Runnable(this, n, (IntentSender.SendIntentException)var2_5){
                    final 2 this$1;
                    final IntentSender.SendIntentException val$e;
                    final int val$requestCode;
                    {
                        this.this$1 = var1_1;
                        this.val$requestCode = n;
                        this.val$e = sendIntentException;
                    }

                    @Override
                    public void run() {
                        this.this$1.dispatchResult(this.val$requestCode, 0, new Intent().setAction("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST").putExtra("androidx.activity.result.contract.extra.SEND_INTENT_EXCEPTION", (Serializable)this.val$e));
                    }
                });
            }
        };
        if (this.getLifecycle() != null) {
            if (Build.VERSION.SDK_INT >= 19) {
                this.getLifecycle().addObserver(new LifecycleEventObserver(this){
                    final ComponentActivity this$0;
                    {
                        this.this$0 = componentActivity;
                    }

                    @Override
                    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                        if (event == Lifecycle.Event.ON_STOP && (lifecycleOwner = (lifecycleOwner = this.this$0.getWindow()) != null ? lifecycleOwner.peekDecorView() : null) != null) {
                            lifecycleOwner.cancelPendingInputEvents();
                        }
                    }
                });
            }
            this.getLifecycle().addObserver(new LifecycleEventObserver(this){
                final ComponentActivity this$0;
                {
                    this.this$0 = componentActivity;
                }

                @Override
                public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        this.this$0.mContextAwareHelper.clearAvailableContext();
                        if (!this.this$0.isChangingConfigurations()) {
                            this.this$0.getViewModelStore().clear();
                        }
                    }
                }
            });
            this.getLifecycle().addObserver(new LifecycleEventObserver(this){
                final ComponentActivity this$0;
                {
                    this.this$0 = componentActivity;
                }

                @Override
                public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                    this.this$0.ensureViewModelStore();
                    this.this$0.getLifecycle().removeObserver(this);
                }
            });
            if (19 <= Build.VERSION.SDK_INT && Build.VERSION.SDK_INT <= 23) {
                this.getLifecycle().addObserver(new ImmLeaksCleaner(this));
            }
            return;
        }
        throw new IllegalStateException("getLifecycle() returned null in ComponentActivity's constructor. Please make sure you are lazily constructing your Lifecycle in the first call to getLifecycle() rather than relying on field initialization.");
    }

    public ComponentActivity(int n) {
        this();
        this.mContentLayoutId = n;
    }

    private void initViewTreeOwners() {
        ViewTreeLifecycleOwner.set(this.getWindow().getDecorView(), this);
        ViewTreeViewModelStoreOwner.set(this.getWindow().getDecorView(), this);
        ViewTreeSavedStateRegistryOwner.set(this.getWindow().getDecorView(), this);
    }

    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.initViewTreeOwners();
        super.addContentView(view, layoutParams);
    }

    @Override
    public final void addOnContextAvailableListener(OnContextAvailableListener onContextAvailableListener) {
        this.mContextAwareHelper.addOnContextAvailableListener(onContextAvailableListener);
    }

    void ensureViewModelStore() {
        if (this.mViewModelStore == null) {
            NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)this.getLastNonConfigurationInstance();
            if (nonConfigurationInstances != null) {
                this.mViewModelStore = nonConfigurationInstances.viewModelStore;
            }
            if (this.mViewModelStore == null) {
                this.mViewModelStore = new ViewModelStore();
            }
        }
    }

    @Override
    public final ActivityResultRegistry getActivityResultRegistry() {
        return this.mActivityResultRegistry;
    }

    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        if (this.getApplication() != null) {
            if (this.mDefaultFactory == null) {
                Application application = this.getApplication();
                Bundle bundle = this.getIntent() != null ? this.getIntent().getExtras() : null;
                this.mDefaultFactory = new SavedStateViewModelFactory(application, this, bundle);
            }
            return this.mDefaultFactory;
        }
        throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
    }

    @Deprecated
    public Object getLastCustomNonConfigurationInstance() {
        Object object = (NonConfigurationInstances)this.getLastNonConfigurationInstance();
        object = object != null ? ((NonConfigurationInstances)object).custom : null;
        return object;
    }

    @Override
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @Override
    public final OnBackPressedDispatcher getOnBackPressedDispatcher() {
        return this.mOnBackPressedDispatcher;
    }

    @Override
    public final SavedStateRegistry getSavedStateRegistry() {
        return this.mSavedStateRegistryController.getSavedStateRegistry();
    }

    @Override
    public ViewModelStore getViewModelStore() {
        if (this.getApplication() != null) {
            this.ensureViewModelStore();
            return this.mViewModelStore;
        }
        throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
    }

    @Deprecated
    protected void onActivityResult(int n, int n2, Intent intent) {
        if (!this.mActivityResultRegistry.dispatchResult(n, n2, intent)) {
            super.onActivityResult(n, n2, intent);
        }
    }

    public void onBackPressed() {
        this.mOnBackPressedDispatcher.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        this.mSavedStateRegistryController.performRestore(bundle);
        this.mContextAwareHelper.dispatchOnContextAvailable((Context)this);
        super.onCreate(bundle);
        this.mActivityResultRegistry.onRestoreInstanceState(bundle);
        ReportFragment.injectIfNeededIn(this);
        int n = this.mContentLayoutId;
        if (n != 0) {
            this.setContentView(n);
        }
    }

    @Deprecated
    public void onRequestPermissionsResult(int n, String[] stringArray, int[] nArray) {
        if (!this.mActivityResultRegistry.dispatchResult(n, -1, new Intent().putExtra("androidx.activity.result.contract.extra.PERMISSIONS", stringArray).putExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS", nArray)) && Build.VERSION.SDK_INT >= 23) {
            super.onRequestPermissionsResult(n, stringArray, nArray);
        }
    }

    @Deprecated
    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    public final Object onRetainNonConfigurationInstance() {
        Object object = this.onRetainCustomNonConfigurationInstance();
        Object object2 = this.mViewModelStore;
        ViewModelStore viewModelStore = object2;
        if (object2 == null) {
            NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)this.getLastNonConfigurationInstance();
            viewModelStore = object2;
            if (nonConfigurationInstances != null) {
                viewModelStore = nonConfigurationInstances.viewModelStore;
            }
        }
        if (viewModelStore == null && object == null) {
            return null;
        }
        object2 = new NonConfigurationInstances();
        ((NonConfigurationInstances)object2).custom = object;
        ((NonConfigurationInstances)object2).viewModelStore = viewModelStore;
        return object2;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        Lifecycle lifecycle = this.getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            ((LifecycleRegistry)lifecycle).setCurrentState(Lifecycle.State.CREATED);
        }
        super.onSaveInstanceState(bundle);
        this.mSavedStateRegistryController.performSave(bundle);
        this.mActivityResultRegistry.onSaveInstanceState(bundle);
    }

    @Override
    public Context peekAvailableContext() {
        return this.mContextAwareHelper.peekAvailableContext();
    }

    @Override
    public final <I, O> ActivityResultLauncher<I> registerForActivityResult(ActivityResultContract<I, O> activityResultContract, ActivityResultCallback<O> activityResultCallback) {
        return this.registerForActivityResult(activityResultContract, this.mActivityResultRegistry, activityResultCallback);
    }

    @Override
    public final <I, O> ActivityResultLauncher<I> registerForActivityResult(ActivityResultContract<I, O> activityResultContract, ActivityResultRegistry activityResultRegistry, ActivityResultCallback<O> activityResultCallback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("activity_rq#");
        stringBuilder.append(this.mNextLocalRequestCode.getAndIncrement());
        return activityResultRegistry.register(stringBuilder.toString(), this, activityResultContract, activityResultCallback);
    }

    @Override
    public final void removeOnContextAvailableListener(OnContextAvailableListener onContextAvailableListener) {
        this.mContextAwareHelper.removeOnContextAvailableListener(onContextAvailableListener);
    }

    public void reportFullyDrawn() {
        try {
            if (Trace.isEnabled()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("reportFullyDrawn() for ");
                stringBuilder.append(this.getComponentName());
                Trace.beginSection(stringBuilder.toString());
            }
            if (Build.VERSION.SDK_INT > 19) {
                super.reportFullyDrawn();
            } else if (Build.VERSION.SDK_INT == 19 && ContextCompat.checkSelfPermission((Context)this, "android.permission.UPDATE_DEVICE_STATS") == 0) {
                super.reportFullyDrawn();
            }
            return;
        }
        finally {
            Trace.endSection();
        }
    }

    public void setContentView(int n) {
        this.initViewTreeOwners();
        super.setContentView(n);
    }

    public void setContentView(View view) {
        this.initViewTreeOwners();
        super.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.initViewTreeOwners();
        super.setContentView(view, layoutParams);
    }

    @Deprecated
    public void startActivityForResult(Intent intent, int n) {
        super.startActivityForResult(intent, n);
    }

    @Deprecated
    public void startActivityForResult(Intent intent, int n, Bundle bundle) {
        super.startActivityForResult(intent, n, bundle);
    }

    @Deprecated
    public void startIntentSenderForResult(IntentSender intentSender, int n, Intent intent, int n2, int n3, int n4) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4);
    }

    @Deprecated
    public void startIntentSenderForResult(IntentSender intentSender, int n, Intent intent, int n2, int n3, int n4, Bundle bundle) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
    }

    static final class NonConfigurationInstances {
        Object custom;
        ViewModelStore viewModelStore;

        NonConfigurationInstances() {
        }
    }
}

