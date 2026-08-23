/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 */
package androidx.activity.result;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class ActivityResultRegistry {
    private static final int INITIAL_REQUEST_CODE_VALUE = 65536;
    private static final String KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS = "KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS";
    private static final String KEY_COMPONENT_ACTIVITY_PENDING_RESULTS = "KEY_COMPONENT_ACTIVITY_PENDING_RESULT";
    private static final String KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT = "KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT";
    private static final String KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS = "KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS";
    private static final String KEY_COMPONENT_ACTIVITY_REGISTERED_RCS = "KEY_COMPONENT_ACTIVITY_REGISTERED_RCS";
    private static final String LOG_TAG = "ActivityResultRegistry";
    final transient Map<String, CallbackAndContract<?>> mKeyToCallback;
    private final Map<String, LifecycleContainer> mKeyToLifecycleContainers;
    private final Map<String, Integer> mKeyToRc;
    ArrayList<String> mLaunchedKeys;
    final Map<String, Object> mParsedPendingResults;
    final Bundle mPendingResults;
    private Random mRandom = new Random();
    private final Map<Integer, String> mRcToKey = new HashMap<Integer, String>();

    public ActivityResultRegistry() {
        this.mKeyToRc = new HashMap<String, Integer>();
        this.mKeyToLifecycleContainers = new HashMap<String, LifecycleContainer>();
        this.mLaunchedKeys = new ArrayList();
        this.mKeyToCallback = new HashMap();
        this.mParsedPendingResults = new HashMap<String, Object>();
        this.mPendingResults = new Bundle();
    }

    private void bindRcKey(int n, String string2) {
        this.mRcToKey.put(n, string2);
        this.mKeyToRc.put(string2, n);
    }

    private <O> void doDispatch(String string2, int n, Intent intent, CallbackAndContract<O> callbackAndContract) {
        if (callbackAndContract != null && callbackAndContract.mCallback != null) {
            callbackAndContract.mCallback.onActivityResult(callbackAndContract.mContract.parseResult(n, intent));
        } else {
            this.mParsedPendingResults.remove(string2);
            this.mPendingResults.putParcelable(string2, (Parcelable)new ActivityResult(n, intent));
        }
    }

    private int generateRandomNumber() {
        int n = this.mRandom.nextInt(0x7FFF0000) + 65536;
        while (this.mRcToKey.containsKey(n)) {
            n = this.mRandom.nextInt(0x7FFF0000) + 65536;
        }
        return n;
    }

    private int registerKey(String string2) {
        Integer n = this.mKeyToRc.get(string2);
        if (n != null) {
            return n;
        }
        int n2 = this.generateRandomNumber();
        this.bindRcKey(n2, string2);
        return n2;
    }

    public final boolean dispatchResult(int n, int n2, Intent intent) {
        String string2 = this.mRcToKey.get(n);
        if (string2 == null) {
            return false;
        }
        this.mLaunchedKeys.remove(string2);
        this.doDispatch(string2, n2, intent, this.mKeyToCallback.get(string2));
        return true;
    }

    public final <O> boolean dispatchResult(int n, O o) {
        String string2 = this.mRcToKey.get(n);
        if (string2 == null) {
            return false;
        }
        this.mLaunchedKeys.remove(string2);
        CallbackAndContract<?> callbackAndContract = this.mKeyToCallback.get(string2);
        if (callbackAndContract != null && callbackAndContract.mCallback != null) {
            callbackAndContract.mCallback.onActivityResult(o);
        } else {
            this.mPendingResults.remove(string2);
            this.mParsedPendingResults.put(string2, o);
        }
        return true;
    }

    public abstract <I, O> void onLaunch(int var1, ActivityResultContract<I, O> var2, I var3, ActivityOptionsCompat var4);

    public final void onRestoreInstanceState(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        ArrayList arrayList = bundle.getIntegerArrayList(KEY_COMPONENT_ACTIVITY_REGISTERED_RCS);
        ArrayList arrayList2 = bundle.getStringArrayList(KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS);
        if (arrayList2 != null && arrayList != null) {
            int n = arrayList2.size();
            for (int i = 0; i < n; ++i) {
                this.bindRcKey((Integer)arrayList.get(i), (String)arrayList2.get(i));
            }
            this.mLaunchedKeys = bundle.getStringArrayList(KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS);
            this.mRandom = (Random)bundle.getSerializable(KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT);
            this.mPendingResults.putAll(bundle.getBundle(KEY_COMPONENT_ACTIVITY_PENDING_RESULTS));
            return;
        }
    }

    public final void onSaveInstanceState(Bundle bundle) {
        bundle.putIntegerArrayList(KEY_COMPONENT_ACTIVITY_REGISTERED_RCS, new ArrayList<Integer>(this.mRcToKey.keySet()));
        bundle.putStringArrayList(KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS, new ArrayList<String>(this.mRcToKey.values()));
        bundle.putStringArrayList(KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS, new ArrayList<String>(this.mLaunchedKeys));
        bundle.putBundle(KEY_COMPONENT_ACTIVITY_PENDING_RESULTS, (Bundle)this.mPendingResults.clone());
        bundle.putSerializable(KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT, (Serializable)this.mRandom);
    }

    public final <I, O> ActivityResultLauncher<I> register(String string2, ActivityResultContract<I, O> activityResultContract, ActivityResultCallback<O> activityResultCallback) {
        Object object;
        int n = this.registerKey(string2);
        this.mKeyToCallback.put(string2, new CallbackAndContract<O>(activityResultCallback, activityResultContract));
        if (this.mParsedPendingResults.containsKey(string2)) {
            object = this.mParsedPendingResults.get(string2);
            this.mParsedPendingResults.remove(string2);
            activityResultCallback.onActivityResult(object);
        }
        if ((object = (ActivityResult)this.mPendingResults.getParcelable(string2)) != null) {
            this.mPendingResults.remove(string2);
            activityResultCallback.onActivityResult(activityResultContract.parseResult(((ActivityResult)object).getResultCode(), ((ActivityResult)object).getData()));
        }
        return new ActivityResultLauncher<I>(this, string2, n, activityResultContract){
            final ActivityResultRegistry this$0;
            final ActivityResultContract val$contract;
            final String val$key;
            final int val$requestCode;
            {
                this.this$0 = activityResultRegistry;
                this.val$key = string2;
                this.val$requestCode = n;
                this.val$contract = activityResultContract;
            }

            @Override
            public ActivityResultContract<I, ?> getContract() {
                return this.val$contract;
            }

            @Override
            public void launch(I i, ActivityOptionsCompat activityOptionsCompat) {
                this.this$0.mLaunchedKeys.add(this.val$key);
                this.this$0.onLaunch(this.val$requestCode, this.val$contract, i, activityOptionsCompat);
            }

            @Override
            public void unregister() {
                this.this$0.unregister(this.val$key);
            }
        };
    }

    public final <I, O> ActivityResultLauncher<I> register(String charSequence, LifecycleOwner object, ActivityResultContract<I, O> activityResultContract, ActivityResultCallback<O> activityResultCallback) {
        Lifecycle lifecycle = object.getLifecycle();
        if (!lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            int n = this.registerKey((String)charSequence);
            LifecycleContainer lifecycleContainer = this.mKeyToLifecycleContainers.get(charSequence);
            object = lifecycleContainer;
            if (lifecycleContainer == null) {
                object = new LifecycleContainer(lifecycle);
            }
            ((LifecycleContainer)object).addObserver(new LifecycleEventObserver(this, (String)charSequence, activityResultCallback, activityResultContract){
                final ActivityResultRegistry this$0;
                final ActivityResultCallback val$callback;
                final ActivityResultContract val$contract;
                final String val$key;
                {
                    this.this$0 = activityResultRegistry;
                    this.val$key = string2;
                    this.val$callback = activityResultCallback;
                    this.val$contract = activityResultContract;
                }

                @Override
                public void onStateChanged(LifecycleOwner object, Lifecycle.Event event) {
                    if (Lifecycle.Event.ON_START.equals((Object)event)) {
                        this.this$0.mKeyToCallback.put(this.val$key, new CallbackAndContract(this.val$callback, this.val$contract));
                        if (this.this$0.mParsedPendingResults.containsKey(this.val$key)) {
                            object = this.this$0.mParsedPendingResults.get(this.val$key);
                            this.this$0.mParsedPendingResults.remove(this.val$key);
                            this.val$callback.onActivityResult(object);
                        }
                        if ((object = (ActivityResult)this.this$0.mPendingResults.getParcelable(this.val$key)) != null) {
                            this.this$0.mPendingResults.remove(this.val$key);
                            this.val$callback.onActivityResult(this.val$contract.parseResult(((ActivityResult)object).getResultCode(), ((ActivityResult)object).getData()));
                        }
                    } else if (Lifecycle.Event.ON_STOP.equals((Object)event)) {
                        this.this$0.mKeyToCallback.remove(this.val$key);
                    } else if (Lifecycle.Event.ON_DESTROY.equals((Object)event)) {
                        this.this$0.unregister(this.val$key);
                    }
                }
            });
            this.mKeyToLifecycleContainers.put((String)charSequence, (LifecycleContainer)object);
            return new ActivityResultLauncher<I>(this, (String)charSequence, n, activityResultContract){
                final ActivityResultRegistry this$0;
                final ActivityResultContract val$contract;
                final String val$key;
                final int val$requestCode;
                {
                    this.this$0 = activityResultRegistry;
                    this.val$key = string2;
                    this.val$requestCode = n;
                    this.val$contract = activityResultContract;
                }

                @Override
                public ActivityResultContract<I, ?> getContract() {
                    return this.val$contract;
                }

                @Override
                public void launch(I i, ActivityOptionsCompat activityOptionsCompat) {
                    this.this$0.mLaunchedKeys.add(this.val$key);
                    this.this$0.onLaunch(this.val$requestCode, this.val$contract, i, activityOptionsCompat);
                }

                @Override
                public void unregister() {
                    this.this$0.unregister(this.val$key);
                }
            };
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("LifecycleOwner ");
        ((StringBuilder)charSequence).append(object);
        ((StringBuilder)charSequence).append(" is attempting to register while current state is ");
        ((StringBuilder)charSequence).append((Object)lifecycle.getCurrentState());
        ((StringBuilder)charSequence).append(". LifecycleOwners must call register before they are STARTED.");
        throw new IllegalStateException(((StringBuilder)charSequence).toString());
    }

    final void unregister(String string2) {
        Object object;
        if (!this.mLaunchedKeys.contains(string2) && (object = this.mKeyToRc.remove(string2)) != null) {
            this.mRcToKey.remove(object);
        }
        this.mKeyToCallback.remove(string2);
        if (this.mParsedPendingResults.containsKey(string2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Dropping pending result for request ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(": ");
            ((StringBuilder)object).append(this.mParsedPendingResults.get(string2));
            Log.w((String)LOG_TAG, (String)((StringBuilder)object).toString());
            this.mParsedPendingResults.remove(string2);
        }
        if (this.mPendingResults.containsKey(string2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Dropping pending result for request ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(": ");
            ((StringBuilder)object).append(this.mPendingResults.getParcelable(string2));
            Log.w((String)LOG_TAG, (String)((StringBuilder)object).toString());
            this.mPendingResults.remove(string2);
        }
        if ((object = this.mKeyToLifecycleContainers.get(string2)) != null) {
            ((LifecycleContainer)object).clearObservers();
            this.mKeyToLifecycleContainers.remove(string2);
        }
    }

    private static class CallbackAndContract<O> {
        final ActivityResultCallback<O> mCallback;
        final ActivityResultContract<?, O> mContract;

        CallbackAndContract(ActivityResultCallback<O> activityResultCallback, ActivityResultContract<?, O> activityResultContract) {
            this.mCallback = activityResultCallback;
            this.mContract = activityResultContract;
        }
    }

    private static class LifecycleContainer {
        final Lifecycle mLifecycle;
        private final ArrayList<LifecycleEventObserver> mObservers;

        LifecycleContainer(Lifecycle lifecycle) {
            this.mLifecycle = lifecycle;
            this.mObservers = new ArrayList();
        }

        void addObserver(LifecycleEventObserver lifecycleEventObserver) {
            this.mLifecycle.addObserver(lifecycleEventObserver);
            this.mObservers.add(lifecycleEventObserver);
        }

        void clearObservers() {
            for (LifecycleEventObserver lifecycleEventObserver : this.mObservers) {
                this.mLifecycle.removeObserver(lifecycleEventObserver);
            }
            this.mObservers.clear();
        }
    }
}

