/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.app.Fragment
 *  android.app.FragmentManager
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 *  android.view.View
 */
package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideExperiments;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import com.bumptech.glide.manager.ApplicationLifecycle;
import com.bumptech.glide.manager.DoNothingFirstFrameWaiter;
import com.bumptech.glide.manager.EmptyRequestManagerTreeNode;
import com.bumptech.glide.manager.FirstFrameAndAfterTrimMemoryWaiter;
import com.bumptech.glide.manager.FirstFrameWaiter;
import com.bumptech.glide.manager.FrameWaiter;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerFragment;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestManagerRetriever
implements Handler.Callback {
    private static final RequestManagerFactory DEFAULT_FACTORY = new RequestManagerFactory(){

        @Override
        public RequestManager build(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode, Context context) {
            return new RequestManager(glide, lifecycle, requestManagerTreeNode, context);
        }
    };
    private static final String FRAGMENT_INDEX_KEY = "key";
    static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
    private static final int HAS_ATTEMPTED_TO_ADD_FRAGMENT_TWICE = 1;
    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    private static final String TAG = "RMRetriever";
    private volatile RequestManager applicationManager;
    private final RequestManagerFactory factory;
    private final FrameWaiter frameWaiter;
    private final Handler handler;
    final Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments = new HashMap<FragmentManager, RequestManagerFragment>();
    final Map<androidx.fragment.app.FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments = new HashMap<androidx.fragment.app.FragmentManager, SupportRequestManagerFragment>();
    private final Bundle tempBundle;
    private final ArrayMap<View, Fragment> tempViewToFragment;
    private final ArrayMap<View, androidx.fragment.app.Fragment> tempViewToSupportFragment = new ArrayMap();

    public RequestManagerRetriever(RequestManagerFactory requestManagerFactory, GlideExperiments glideExperiments) {
        this.tempViewToFragment = new ArrayMap();
        this.tempBundle = new Bundle();
        if (requestManagerFactory == null) {
            requestManagerFactory = DEFAULT_FACTORY;
        }
        this.factory = requestManagerFactory;
        this.handler = new Handler(Looper.getMainLooper(), (Handler.Callback)this);
        this.frameWaiter = RequestManagerRetriever.buildFrameWaiter(glideExperiments);
    }

    private static void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    private static FrameWaiter buildFrameWaiter(GlideExperiments object) {
        if (HardwareConfigState.HARDWARE_BITMAPS_SUPPORTED && HardwareConfigState.BLOCK_HARDWARE_BITMAPS_WHEN_GL_CONTEXT_MIGHT_NOT_BE_INITIALIZED) {
            object = ((GlideExperiments)object).isEnabled(GlideBuilder.WaitForFramesAfterTrimMemory.class) ? new FirstFrameAndAfterTrimMemoryWaiter() : new FirstFrameWaiter();
            return object;
        }
        return new DoNothingFirstFrameWaiter();
    }

    private static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity)context;
        }
        if (context instanceof ContextWrapper) {
            return RequestManagerRetriever.findActivity(((ContextWrapper)context).getBaseContext());
        }
        return null;
    }

    @Deprecated
    private void findAllFragmentsWithViews(FragmentManager fragmentManager2, ArrayMap<View, Fragment> arrayMap) {
        if (Build.VERSION.SDK_INT >= 26) {
            for (FragmentManager fragmentManager2 : fragmentManager2.getFragments()) {
                if (fragmentManager2.getView() == null) continue;
                arrayMap.put(fragmentManager2.getView(), (Fragment)fragmentManager2);
                this.findAllFragmentsWithViews(fragmentManager2.getChildFragmentManager(), arrayMap);
            }
        } else {
            this.findAllFragmentsWithViewsPreO(fragmentManager2, arrayMap);
        }
    }

    @Deprecated
    private void findAllFragmentsWithViewsPreO(FragmentManager fragmentManager, ArrayMap<View, Fragment> arrayMap) {
        int n = 0;
        while (true) {
            this.tempBundle.putInt(FRAGMENT_INDEX_KEY, n);
            Fragment fragment = null;
            try {
                Fragment fragment2;
                fragment = fragment2 = fragmentManager.getFragment(this.tempBundle, FRAGMENT_INDEX_KEY);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (fragment == null) {
                return;
            }
            if (fragment.getView() != null) {
                arrayMap.put(fragment.getView(), fragment);
                if (Build.VERSION.SDK_INT >= 17) {
                    this.findAllFragmentsWithViews(fragment.getChildFragmentManager(), arrayMap);
                }
            }
            ++n;
        }
    }

    private static void findAllSupportFragmentsWithViews(Collection<androidx.fragment.app.Fragment> object, Map<View, androidx.fragment.app.Fragment> map) {
        if (object == null) {
            return;
        }
        Iterator<androidx.fragment.app.Fragment> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            if (object == null || ((androidx.fragment.app.Fragment)object).getView() == null) continue;
            map.put(((androidx.fragment.app.Fragment)object).getView(), (androidx.fragment.app.Fragment)object);
            RequestManagerRetriever.findAllSupportFragmentsWithViews(((androidx.fragment.app.Fragment)object).getChildFragmentManager().getFragments(), map);
        }
    }

    @Deprecated
    private Fragment findFragment(View view, Activity activity) {
        this.tempViewToFragment.clear();
        this.findAllFragmentsWithViews(activity.getFragmentManager(), this.tempViewToFragment);
        View view2 = null;
        View view3 = activity.findViewById(0x1020002);
        activity = view;
        view = view2;
        while (true) {
            view2 = view;
            if (activity.equals(view3)) break;
            view = (Fragment)this.tempViewToFragment.get(activity);
            if (view != null) {
                view2 = view;
                break;
            }
            view2 = view;
            if (!(activity.getParent() instanceof View)) break;
            activity = (View)activity.getParent();
        }
        this.tempViewToFragment.clear();
        return view2;
    }

    private androidx.fragment.app.Fragment findSupportFragment(View object, FragmentActivity fragmentActivity) {
        this.tempViewToSupportFragment.clear();
        RequestManagerRetriever.findAllSupportFragmentsWithViews(fragmentActivity.getSupportFragmentManager().getFragments(), this.tempViewToSupportFragment);
        View view = null;
        View view2 = fragmentActivity.findViewById(0x1020002);
        fragmentActivity = object;
        object = view;
        while (true) {
            view = object;
            if (fragmentActivity.equals(view2)) break;
            object = (androidx.fragment.app.Fragment)this.tempViewToSupportFragment.get(fragmentActivity);
            if (object != null) {
                view = object;
                break;
            }
            view = object;
            if (!(fragmentActivity.getParent() instanceof View)) break;
            fragmentActivity = (View)fragmentActivity.getParent();
        }
        this.tempViewToSupportFragment.clear();
        return view;
    }

    @Deprecated
    private RequestManager fragmentGet(Context context, FragmentManager object, Fragment object2, boolean bl) {
        RequestManagerFragment requestManagerFragment = this.getRequestManagerFragment((FragmentManager)object, (Fragment)object2);
        object2 = requestManagerFragment.getRequestManager();
        object = object2;
        if (object2 == null) {
            object = Glide.get(context);
            object = this.factory.build((Glide)object, requestManagerFragment.getGlideLifecycle(), requestManagerFragment.getRequestManagerTreeNode(), context);
            if (bl) {
                ((RequestManager)object).onStart();
            }
            requestManagerFragment.setRequestManager((RequestManager)object);
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private RequestManager getApplicationManager(Context context) {
        if (this.applicationManager != null) return this.applicationManager;
        synchronized (this) {
            if (this.applicationManager != null) return this.applicationManager;
            Glide glide = Glide.get(context.getApplicationContext());
            RequestManagerFactory requestManagerFactory = this.factory;
            ApplicationLifecycle applicationLifecycle = new ApplicationLifecycle();
            EmptyRequestManagerTreeNode emptyRequestManagerTreeNode = new EmptyRequestManagerTreeNode();
            this.applicationManager = requestManagerFactory.build(glide, applicationLifecycle, emptyRequestManagerTreeNode, context.getApplicationContext());
            return this.applicationManager;
        }
    }

    private RequestManagerFragment getRequestManagerFragment(FragmentManager fragmentManager, Fragment fragment) {
        RequestManagerFragment requestManagerFragment;
        RequestManagerFragment requestManagerFragment2 = requestManagerFragment = this.pendingRequestManagerFragments.get(fragmentManager);
        if (requestManagerFragment == null) {
            requestManagerFragment2 = requestManagerFragment = (RequestManagerFragment)fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (requestManagerFragment == null) {
                requestManagerFragment2 = new RequestManagerFragment();
                requestManagerFragment2.setParentFragmentHint(fragment);
                this.pendingRequestManagerFragments.put(fragmentManager, requestManagerFragment2);
                fragmentManager.beginTransaction().add((Fragment)requestManagerFragment2, FRAGMENT_TAG).commitAllowingStateLoss();
                this.handler.obtainMessage(1, (Object)fragmentManager).sendToTarget();
            }
        }
        return requestManagerFragment2;
    }

    private SupportRequestManagerFragment getSupportRequestManagerFragment(androidx.fragment.app.FragmentManager fragmentManager, androidx.fragment.app.Fragment fragment) {
        SupportRequestManagerFragment supportRequestManagerFragment;
        SupportRequestManagerFragment supportRequestManagerFragment2 = supportRequestManagerFragment = this.pendingSupportRequestManagerFragments.get(fragmentManager);
        if (supportRequestManagerFragment == null) {
            supportRequestManagerFragment2 = supportRequestManagerFragment = (SupportRequestManagerFragment)fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (supportRequestManagerFragment == null) {
                supportRequestManagerFragment2 = new SupportRequestManagerFragment();
                supportRequestManagerFragment2.setParentFragmentHint(fragment);
                this.pendingSupportRequestManagerFragments.put(fragmentManager, supportRequestManagerFragment2);
                fragmentManager.beginTransaction().add(supportRequestManagerFragment2, FRAGMENT_TAG).commitAllowingStateLoss();
                this.handler.obtainMessage(2, (Object)fragmentManager).sendToTarget();
            }
        }
        return supportRequestManagerFragment2;
    }

    private static boolean isActivityVisible(Context context) {
        boolean bl = (context = RequestManagerRetriever.findActivity(context)) == null || !context.isFinishing();
        return bl;
    }

    private RequestManager supportFragmentGet(Context context, androidx.fragment.app.FragmentManager object, androidx.fragment.app.Fragment object2, boolean bl) {
        SupportRequestManagerFragment supportRequestManagerFragment = this.getSupportRequestManagerFragment((androidx.fragment.app.FragmentManager)object, (androidx.fragment.app.Fragment)object2);
        object = object2 = supportRequestManagerFragment.getRequestManager();
        if (object2 == null) {
            object = Glide.get(context);
            object = this.factory.build((Glide)object, supportRequestManagerFragment.getGlideLifecycle(), supportRequestManagerFragment.getRequestManagerTreeNode(), context);
            if (bl) {
                ((RequestManager)object).onStart();
            }
            supportRequestManagerFragment.setRequestManager((RequestManager)object);
        }
        return object;
    }

    private boolean verifyOurFragmentWasAddedOrCantBeAdded(FragmentManager object, boolean bl) {
        RequestManagerFragment requestManagerFragment = this.pendingRequestManagerFragments.get(object);
        RequestManagerFragment requestManagerFragment2 = (RequestManagerFragment)object.findFragmentByTag(FRAGMENT_TAG);
        if (requestManagerFragment2 == requestManagerFragment) {
            return true;
        }
        if (requestManagerFragment2 != null && requestManagerFragment2.getRequestManager() != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append("We've added two fragments with requests! Old: ");
            ((StringBuilder)object).append((Object)requestManagerFragment2);
            ((StringBuilder)object).append(" New: ");
            ((StringBuilder)object).append((Object)requestManagerFragment);
            throw new IllegalStateException(((StringBuilder)object).toString());
        }
        if (!bl && !object.isDestroyed()) {
            requestManagerFragment = object.beginTransaction().add((Fragment)requestManagerFragment, FRAGMENT_TAG);
            if (requestManagerFragment2 != null) {
                requestManagerFragment.remove(requestManagerFragment2);
            }
            requestManagerFragment.commitAllowingStateLoss();
            this.handler.obtainMessage(1, 1, 0, object).sendToTarget();
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"We failed to add our Fragment the first time around, trying again...");
            }
            return false;
        }
        if (Log.isLoggable((String)TAG, (int)5)) {
            if (object.isDestroyed()) {
                Log.w((String)TAG, (String)"Parent was destroyed before our Fragment could be added");
            } else {
                Log.w((String)TAG, (String)"Tried adding Fragment twice and failed twice, giving up!");
            }
        }
        requestManagerFragment.getGlideLifecycle().onDestroy();
        return true;
    }

    private boolean verifyOurSupportFragmentWasAddedOrCantBeAdded(androidx.fragment.app.FragmentManager object, boolean bl) {
        Object object2 = this.pendingSupportRequestManagerFragments.get(object);
        SupportRequestManagerFragment supportRequestManagerFragment = (SupportRequestManagerFragment)((androidx.fragment.app.FragmentManager)object).findFragmentByTag(FRAGMENT_TAG);
        if (supportRequestManagerFragment == object2) {
            return true;
        }
        if (supportRequestManagerFragment != null && supportRequestManagerFragment.getRequestManager() != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append("We've added two fragments with requests! Old: ");
            ((StringBuilder)object).append(supportRequestManagerFragment);
            ((StringBuilder)object).append(" New: ");
            ((StringBuilder)object).append(object2);
            throw new IllegalStateException(((StringBuilder)object).toString());
        }
        if (!bl && !((androidx.fragment.app.FragmentManager)object).isDestroyed()) {
            object2 = ((androidx.fragment.app.FragmentManager)object).beginTransaction().add((androidx.fragment.app.Fragment)object2, FRAGMENT_TAG);
            if (supportRequestManagerFragment != null) {
                ((FragmentTransaction)object2).remove(supportRequestManagerFragment);
            }
            ((FragmentTransaction)object2).commitNowAllowingStateLoss();
            this.handler.obtainMessage(2, 1, 0, object).sendToTarget();
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"We failed to add our Fragment the first time around, trying again...");
            }
            return false;
        }
        if (((androidx.fragment.app.FragmentManager)object).isDestroyed()) {
            if (Log.isLoggable((String)TAG, (int)5)) {
                Log.w((String)TAG, (String)"Parent was destroyed before our Fragment could be added, all requests for the destroyed parent are cancelled");
            }
        } else if (Log.isLoggable((String)TAG, (int)6)) {
            Log.e((String)TAG, (String)"ERROR: Tried adding Fragment twice and failed twice, giving up and cancelling all associated requests! This probably means you're starting loads in a unit test with an Activity that you haven't created and never create. If you're using Robolectric, create the Activity as part of your test setup");
        }
        ((SupportRequestManagerFragment)object2).getGlideLifecycle().onDestroy();
        return true;
    }

    public RequestManager get(Activity activity) {
        if (Util.isOnBackgroundThread()) {
            return this.get(activity.getApplicationContext());
        }
        if (activity instanceof FragmentActivity) {
            return this.get((FragmentActivity)activity);
        }
        RequestManagerRetriever.assertNotDestroyed(activity);
        this.frameWaiter.registerSelf(activity);
        return this.fragmentGet((Context)activity, activity.getFragmentManager(), null, RequestManagerRetriever.isActivityVisible((Context)activity));
    }

    @Deprecated
    public RequestManager get(Fragment fragment) {
        if (fragment.getActivity() != null) {
            if (!Util.isOnBackgroundThread() && Build.VERSION.SDK_INT >= 17) {
                if (fragment.getActivity() != null) {
                    this.frameWaiter.registerSelf(fragment.getActivity());
                }
                FragmentManager fragmentManager = fragment.getChildFragmentManager();
                return this.fragmentGet((Context)fragment.getActivity(), fragmentManager, fragment, fragment.isVisible());
            }
            return this.get(fragment.getActivity().getApplicationContext());
        }
        throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
    }

    public RequestManager get(Context context) {
        if (context != null) {
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity) {
                    return this.get((FragmentActivity)context);
                }
                if (context instanceof Activity) {
                    return this.get((Activity)context);
                }
                if (context instanceof ContextWrapper && ((ContextWrapper)context).getBaseContext().getApplicationContext() != null) {
                    return this.get(((ContextWrapper)context).getBaseContext());
                }
            }
            return this.getApplicationManager(context);
        }
        throw new IllegalArgumentException("You cannot start a load on a null Context");
    }

    public RequestManager get(View object) {
        if (Util.isOnBackgroundThread()) {
            return this.get(object.getContext().getApplicationContext());
        }
        Preconditions.checkNotNull(object);
        Preconditions.checkNotNull(object.getContext(), "Unable to obtain a request manager for a view without a Context");
        Activity activity = RequestManagerRetriever.findActivity(object.getContext());
        if (activity == null) {
            return this.get(object.getContext().getApplicationContext());
        }
        if (activity instanceof FragmentActivity) {
            object = (object = this.findSupportFragment((View)object, (FragmentActivity)activity)) != null ? this.get((androidx.fragment.app.Fragment)object) : this.get((FragmentActivity)activity);
            return object;
        }
        if ((object = this.findFragment((View)object, activity)) == null) {
            return this.get(activity);
        }
        return this.get((Fragment)object);
    }

    public RequestManager get(androidx.fragment.app.Fragment fragment) {
        Preconditions.checkNotNull(fragment.getContext(), "You cannot start a load on a fragment before it is attached or after it is destroyed");
        if (Util.isOnBackgroundThread()) {
            return this.get(fragment.getContext().getApplicationContext());
        }
        if (fragment.getActivity() != null) {
            this.frameWaiter.registerSelf(fragment.getActivity());
        }
        androidx.fragment.app.FragmentManager fragmentManager = fragment.getChildFragmentManager();
        return this.supportFragmentGet(fragment.getContext(), fragmentManager, fragment, fragment.isVisible());
    }

    public RequestManager get(FragmentActivity fragmentActivity) {
        if (Util.isOnBackgroundThread()) {
            return this.get(fragmentActivity.getApplicationContext());
        }
        RequestManagerRetriever.assertNotDestroyed(fragmentActivity);
        this.frameWaiter.registerSelf(fragmentActivity);
        return this.supportFragmentGet((Context)fragmentActivity, fragmentActivity.getSupportFragmentManager(), null, RequestManagerRetriever.isActivityVisible((Context)fragmentActivity));
    }

    @Deprecated
    RequestManagerFragment getRequestManagerFragment(Activity activity) {
        return this.getRequestManagerFragment(activity.getFragmentManager(), null);
    }

    SupportRequestManagerFragment getSupportRequestManagerFragment(androidx.fragment.app.FragmentManager fragmentManager) {
        return this.getSupportRequestManagerFragment(fragmentManager, null);
    }

    public boolean handleMessage(Message object) {
        boolean bl;
        boolean bl2 = true;
        boolean bl3 = false;
        Object object2 = null;
        Object var9_5 = null;
        int n = object.arg1;
        boolean bl4 = true;
        if (n != 1) {
            bl4 = false;
        }
        switch (object.what) {
            default: {
                bl = false;
                object = var9_5;
                break;
            }
            case 2: {
                androidx.fragment.app.FragmentManager fragmentManager = (androidx.fragment.app.FragmentManager)object.obj;
                bl = bl2;
                object = var9_5;
                if (!this.verifyOurSupportFragmentWasAddedOrCantBeAdded(fragmentManager, bl4)) break;
                bl3 = true;
                object = fragmentManager;
                object2 = this.pendingSupportRequestManagerFragments.remove(fragmentManager);
                bl = bl2;
                break;
            }
            case 1: {
                FragmentManager fragmentManager = (FragmentManager)object.obj;
                bl = bl2;
                object = var9_5;
                if (!this.verifyOurFragmentWasAddedOrCantBeAdded(fragmentManager, bl4)) break;
                bl3 = true;
                object = fragmentManager;
                object2 = this.pendingRequestManagerFragments.remove(fragmentManager);
                bl = bl2;
            }
        }
        if (Log.isLoggable((String)TAG, (int)5) && bl3 && object2 == null) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Failed to remove expected request manager fragment, manager: ");
            ((StringBuilder)object2).append(object);
            Log.w((String)TAG, (String)((StringBuilder)object2).toString());
        }
        return bl;
    }

    public static interface RequestManagerFactory {
        public RequestManager build(Glide var1, Lifecycle var2, RequestManagerTreeNode var3, Context var4);
    }
}

