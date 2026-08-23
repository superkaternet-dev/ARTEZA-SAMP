/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.content.res.Configuration
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.Window
 */
package androidx.fragment.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.savedstate.SavedStateRegistry;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class FragmentActivity
extends ComponentActivity
implements ActivityCompat.OnRequestPermissionsResultCallback,
ActivityCompat.RequestPermissionsRequestCodeValidator {
    static final String FRAGMENTS_TAG = "android:support:fragments";
    boolean mCreated;
    final LifecycleRegistry mFragmentLifecycleRegistry;
    final FragmentController mFragments = FragmentController.createController(new HostCallbacks(this));
    boolean mResumed;
    boolean mStopped = true;

    public FragmentActivity() {
        this.mFragmentLifecycleRegistry = new LifecycleRegistry(this);
        this.init();
    }

    public FragmentActivity(int n) {
        super(n);
        this.mFragmentLifecycleRegistry = new LifecycleRegistry(this);
        this.init();
    }

    private void init() {
        this.getSavedStateRegistry().registerSavedStateProvider(FRAGMENTS_TAG, new SavedStateRegistry.SavedStateProvider(this){
            final FragmentActivity this$0;
            {
                this.this$0 = fragmentActivity;
            }

            @Override
            public Bundle saveState() {
                Bundle bundle = new Bundle();
                this.this$0.markFragmentsCreated();
                this.this$0.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
                Parcelable parcelable = this.this$0.mFragments.saveAllState();
                if (parcelable != null) {
                    bundle.putParcelable(FragmentActivity.FRAGMENTS_TAG, parcelable);
                }
                return bundle;
            }
        });
        this.addOnContextAvailableListener(new OnContextAvailableListener(this){
            final FragmentActivity this$0;
            {
                this.this$0 = fragmentActivity;
            }

            @Override
            public void onContextAvailable(Context context) {
                this.this$0.mFragments.attachHost(null);
                context = this.this$0.getSavedStateRegistry().consumeRestoredStateForKey(FragmentActivity.FRAGMENTS_TAG);
                if (context != null) {
                    context = context.getParcelable(FragmentActivity.FRAGMENTS_TAG);
                    this.this$0.mFragments.restoreSaveState((Parcelable)context);
                }
            }
        });
    }

    /*
     * WARNING - void declaration
     */
    private static boolean markState(FragmentManager object2, Lifecycle.State state) {
        boolean bl = false;
        for (Fragment fragment : ((FragmentManager)object2).getFragments()) {
            void var1_3;
            if (fragment == null) continue;
            boolean bl2 = bl;
            if (fragment.getHost() != null) {
                bl2 = bl | FragmentActivity.markState(fragment.getChildFragmentManager(), (Lifecycle.State)var1_3);
            }
            bl = bl2;
            if (fragment.mViewLifecycleOwner != null) {
                bl = bl2;
                if (fragment.mViewLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    fragment.mViewLifecycleOwner.setCurrentState((Lifecycle.State)var1_3);
                    bl = true;
                }
            }
            if (!fragment.mLifecycleRegistry.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) continue;
            fragment.mLifecycleRegistry.setCurrentState((Lifecycle.State)var1_3);
            bl = true;
        }
        return bl;
    }

    final View dispatchFragmentsOnCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
        return this.mFragments.onCreateView(view, string2, context, attributeSet);
    }

    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        super.dump(string2, fileDescriptor, printWriter, stringArray);
        printWriter.print(string2);
        printWriter.print("Local FragmentActivity ");
        printWriter.print(Integer.toHexString(System.identityHashCode(this)));
        printWriter.println(" State:");
        CharSequence charSequence = new StringBuilder();
        charSequence.append(string2);
        charSequence.append("  ");
        charSequence = charSequence.toString();
        printWriter.print((String)charSequence);
        printWriter.print("mCreated=");
        printWriter.print(this.mCreated);
        printWriter.print(" mResumed=");
        printWriter.print(this.mResumed);
        printWriter.print(" mStopped=");
        printWriter.print(this.mStopped);
        if (this.getApplication() != null) {
            LoaderManager.getInstance(this).dump((String)charSequence, fileDescriptor, printWriter, stringArray);
        }
        this.mFragments.getSupportFragmentManager().dump(string2, fileDescriptor, printWriter, stringArray);
    }

    public FragmentManager getSupportFragmentManager() {
        return this.mFragments.getSupportFragmentManager();
    }

    @Deprecated
    public LoaderManager getSupportLoaderManager() {
        return LoaderManager.getInstance(this);
    }

    void markFragmentsCreated() {
        while (FragmentActivity.markState(this.getSupportFragmentManager(), Lifecycle.State.CREATED)) {
        }
    }

    @Override
    protected void onActivityResult(int n, int n2, Intent intent) {
        this.mFragments.noteStateNotSaved();
        super.onActivityResult(n, n2, intent);
    }

    @Deprecated
    public void onAttachFragment(Fragment fragment) {
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mFragments.noteStateNotSaved();
        this.mFragments.dispatchConfigurationChanged(configuration);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        this.mFragments.dispatchCreate();
    }

    public boolean onCreatePanelMenu(int n, Menu menu) {
        if (n == 0) {
            return super.onCreatePanelMenu(n, menu) | this.mFragments.dispatchCreateOptionsMenu(menu, this.getMenuInflater());
        }
        return super.onCreatePanelMenu(n, menu);
    }

    public View onCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
        View view2 = this.dispatchFragmentsOnCreateView(view, string2, context, attributeSet);
        if (view2 == null) {
            return super.onCreateView(view, string2, context, attributeSet);
        }
        return view2;
    }

    public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
        View view = this.dispatchFragmentsOnCreateView(null, string2, context, attributeSet);
        if (view == null) {
            return super.onCreateView(string2, context, attributeSet);
        }
        return view;
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mFragments.dispatchDestroy();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mFragments.dispatchLowMemory();
    }

    public boolean onMenuItemSelected(int n, MenuItem menuItem) {
        if (super.onMenuItemSelected(n, menuItem)) {
            return true;
        }
        switch (n) {
            default: {
                return false;
            }
            case 6: {
                return this.mFragments.dispatchContextItemSelected(menuItem);
            }
            case 0: 
        }
        return this.mFragments.dispatchOptionsItemSelected(menuItem);
    }

    public void onMultiWindowModeChanged(boolean bl) {
        this.mFragments.dispatchMultiWindowModeChanged(bl);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mFragments.noteStateNotSaved();
    }

    public void onPanelClosed(int n, Menu menu) {
        if (n == 0) {
            this.mFragments.dispatchOptionsMenuClosed(menu);
        }
        super.onPanelClosed(n, menu);
    }

    protected void onPause() {
        super.onPause();
        this.mResumed = false;
        this.mFragments.dispatchPause();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    public void onPictureInPictureModeChanged(boolean bl) {
        this.mFragments.dispatchPictureInPictureModeChanged(bl);
    }

    protected void onPostResume() {
        super.onPostResume();
        this.onResumeFragments();
    }

    @Deprecated
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        return super.onPreparePanel(0, view, menu);
    }

    public boolean onPreparePanel(int n, View view, Menu menu) {
        if (n == 0) {
            return this.onPrepareOptionsPanel(view, menu) | this.mFragments.dispatchPrepareOptionsMenu(menu);
        }
        return super.onPreparePanel(n, view, menu);
    }

    @Override
    public void onRequestPermissionsResult(int n, String[] stringArray, int[] nArray) {
        this.mFragments.noteStateNotSaved();
        super.onRequestPermissionsResult(n, stringArray, nArray);
    }

    protected void onResume() {
        super.onResume();
        this.mResumed = true;
        this.mFragments.noteStateNotSaved();
        this.mFragments.execPendingActions();
    }

    protected void onResumeFragments() {
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        this.mFragments.dispatchResume();
    }

    protected void onStart() {
        super.onStart();
        this.mStopped = false;
        if (!this.mCreated) {
            this.mCreated = true;
            this.mFragments.dispatchActivityCreated();
        }
        this.mFragments.noteStateNotSaved();
        this.mFragments.execPendingActions();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        this.mFragments.dispatchStart();
    }

    public void onStateNotSaved() {
        this.mFragments.noteStateNotSaved();
    }

    protected void onStop() {
        super.onStop();
        this.mStopped = true;
        this.markFragmentsCreated();
        this.mFragments.dispatchStop();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    public void setEnterSharedElementCallback(SharedElementCallback sharedElementCallback) {
        ActivityCompat.setEnterSharedElementCallback(this, sharedElementCallback);
    }

    public void setExitSharedElementCallback(SharedElementCallback sharedElementCallback) {
        ActivityCompat.setExitSharedElementCallback(this, sharedElementCallback);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int n) {
        this.startActivityFromFragment(fragment, intent, n, null);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int n, Bundle bundle) {
        if (n == -1) {
            ActivityCompat.startActivityForResult(this, intent, -1, bundle);
            return;
        }
        fragment.startActivityForResult(intent, n, bundle);
    }

    @Deprecated
    public void startIntentSenderFromFragment(Fragment fragment, IntentSender intentSender, int n, Intent intent, int n2, int n3, int n4, Bundle bundle) throws IntentSender.SendIntentException {
        if (n == -1) {
            ActivityCompat.startIntentSenderForResult(this, intentSender, n, intent, n2, n3, n4, bundle);
            return;
        }
        fragment.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
    }

    public void supportFinishAfterTransition() {
        ActivityCompat.finishAfterTransition(this);
    }

    @Deprecated
    public void supportInvalidateOptionsMenu() {
        this.invalidateOptionsMenu();
    }

    public void supportPostponeEnterTransition() {
        ActivityCompat.postponeEnterTransition(this);
    }

    public void supportStartPostponedEnterTransition() {
        ActivityCompat.startPostponedEnterTransition(this);
    }

    @Override
    @Deprecated
    public final void validateRequestPermissionsRequestCode(int n) {
    }

    class HostCallbacks
    extends FragmentHostCallback<FragmentActivity>
    implements ViewModelStoreOwner,
    OnBackPressedDispatcherOwner,
    ActivityResultRegistryOwner,
    FragmentOnAttachListener {
        final FragmentActivity this$0;

        public HostCallbacks(FragmentActivity fragmentActivity) {
            this.this$0 = fragmentActivity;
            super(fragmentActivity);
        }

        @Override
        public ActivityResultRegistry getActivityResultRegistry() {
            return this.this$0.getActivityResultRegistry();
        }

        @Override
        public Lifecycle getLifecycle() {
            return this.this$0.mFragmentLifecycleRegistry;
        }

        @Override
        public OnBackPressedDispatcher getOnBackPressedDispatcher() {
            return this.this$0.getOnBackPressedDispatcher();
        }

        @Override
        public ViewModelStore getViewModelStore() {
            return this.this$0.getViewModelStore();
        }

        @Override
        public void onAttachFragment(FragmentManager fragmentManager, Fragment fragment) {
            this.this$0.onAttachFragment(fragment);
        }

        @Override
        public void onDump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
            this.this$0.dump(string2, fileDescriptor, printWriter, stringArray);
        }

        @Override
        public View onFindViewById(int n) {
            return this.this$0.findViewById(n);
        }

        @Override
        public FragmentActivity onGetHost() {
            return this.this$0;
        }

        @Override
        public LayoutInflater onGetLayoutInflater() {
            return this.this$0.getLayoutInflater().cloneInContext((Context)this.this$0);
        }

        @Override
        public int onGetWindowAnimations() {
            Window window = this.this$0.getWindow();
            int n = window == null ? 0 : window.getAttributes().windowAnimations;
            return n;
        }

        @Override
        public boolean onHasView() {
            Window window = this.this$0.getWindow();
            boolean bl = window != null && window.peekDecorView() != null;
            return bl;
        }

        @Override
        public boolean onHasWindowAnimations() {
            boolean bl = this.this$0.getWindow() != null;
            return bl;
        }

        @Override
        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return this.this$0.isFinishing() ^ true;
        }

        @Override
        public boolean onShouldShowRequestPermissionRationale(String string2) {
            return ActivityCompat.shouldShowRequestPermissionRationale(this.this$0, string2);
        }

        @Override
        public void onSupportInvalidateOptionsMenu() {
            this.this$0.supportInvalidateOptionsMenu();
        }
    }
}

