/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package androidx.fragment.app;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManagerNonConfig;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

final class FragmentManagerViewModel
extends ViewModel {
    private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory(){

        @Override
        public <T extends ViewModel> T create(Class<T> clazz) {
            return (T)new FragmentManagerViewModel(true);
        }
    };
    private static final String TAG = "FragmentManager";
    private final HashMap<String, FragmentManagerViewModel> mChildNonConfigs;
    private boolean mHasBeenCleared = false;
    private boolean mHasSavedSnapshot = false;
    private boolean mIsStateSaved = false;
    private final HashMap<String, Fragment> mRetainedFragments = new HashMap();
    private final boolean mStateAutomaticallySaved;
    private final HashMap<String, ViewModelStore> mViewModelStores;

    FragmentManagerViewModel(boolean bl) {
        this.mChildNonConfigs = new HashMap();
        this.mViewModelStores = new HashMap();
        this.mStateAutomaticallySaved = bl;
    }

    static FragmentManagerViewModel getInstance(ViewModelStore viewModelStore) {
        return new ViewModelProvider(viewModelStore, FACTORY).get(FragmentManagerViewModel.class);
    }

    void addRetainedFragment(Fragment fragment) {
        if (this.mIsStateSaved) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v((String)TAG, (String)"Ignoring addRetainedFragment as the state is already saved");
            }
            return;
        }
        if (this.mRetainedFragments.containsKey(fragment.mWho)) {
            return;
        }
        this.mRetainedFragments.put(fragment.mWho, fragment);
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Updating retained Fragments: Added ");
            stringBuilder.append(fragment);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
    }

    void clearNonConfigState(Fragment fragment) {
        Object object;
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Clearing non-config state for ");
            ((StringBuilder)object).append(fragment);
            Log.d((String)TAG, (String)((StringBuilder)object).toString());
        }
        if ((object = this.mChildNonConfigs.get(fragment.mWho)) != null) {
            ((FragmentManagerViewModel)object).onCleared();
            this.mChildNonConfigs.remove(fragment.mWho);
        }
        if ((object = this.mViewModelStores.get(fragment.mWho)) != null) {
            ((ViewModelStore)object).clear();
            this.mViewModelStores.remove(fragment.mWho);
        }
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (FragmentManagerViewModel)object;
            if (!(this.mRetainedFragments.equals(((FragmentManagerViewModel)object).mRetainedFragments) && this.mChildNonConfigs.equals(((FragmentManagerViewModel)object).mChildNonConfigs) && this.mViewModelStores.equals(((FragmentManagerViewModel)object).mViewModelStores))) {
                bl = false;
            }
            return bl;
        }
        return false;
    }

    Fragment findRetainedFragmentByWho(String string2) {
        return this.mRetainedFragments.get(string2);
    }

    FragmentManagerViewModel getChildNonConfig(Fragment fragment) {
        FragmentManagerViewModel fragmentManagerViewModel;
        FragmentManagerViewModel fragmentManagerViewModel2 = fragmentManagerViewModel = this.mChildNonConfigs.get(fragment.mWho);
        if (fragmentManagerViewModel == null) {
            fragmentManagerViewModel2 = new FragmentManagerViewModel(this.mStateAutomaticallySaved);
            this.mChildNonConfigs.put(fragment.mWho, fragmentManagerViewModel2);
        }
        return fragmentManagerViewModel2;
    }

    Collection<Fragment> getRetainedFragments() {
        return new ArrayList<Fragment>(this.mRetainedFragments.values());
    }

    @Deprecated
    FragmentManagerNonConfig getSnapshot() {
        if (this.mRetainedFragments.isEmpty() && this.mChildNonConfigs.isEmpty() && this.mViewModelStores.isEmpty()) {
            return null;
        }
        HashMap<String, FragmentManagerNonConfig> hashMap = new HashMap<String, FragmentManagerNonConfig>();
        for (Map.Entry<String, FragmentManagerViewModel> entry : this.mChildNonConfigs.entrySet()) {
            FragmentManagerNonConfig fragmentManagerNonConfig = entry.getValue().getSnapshot();
            if (fragmentManagerNonConfig == null) continue;
            hashMap.put(entry.getKey(), fragmentManagerNonConfig);
        }
        this.mHasSavedSnapshot = true;
        if (this.mRetainedFragments.isEmpty() && hashMap.isEmpty() && this.mViewModelStores.isEmpty()) {
            return null;
        }
        return new FragmentManagerNonConfig(new ArrayList<Fragment>(this.mRetainedFragments.values()), hashMap, new HashMap<String, ViewModelStore>(this.mViewModelStores));
    }

    ViewModelStore getViewModelStore(Fragment fragment) {
        ViewModelStore viewModelStore;
        ViewModelStore viewModelStore2 = viewModelStore = this.mViewModelStores.get(fragment.mWho);
        if (viewModelStore == null) {
            viewModelStore2 = new ViewModelStore();
            this.mViewModelStores.put(fragment.mWho, viewModelStore2);
        }
        return viewModelStore2;
    }

    public int hashCode() {
        return (this.mRetainedFragments.hashCode() * 31 + this.mChildNonConfigs.hashCode()) * 31 + this.mViewModelStores.hashCode();
    }

    boolean isCleared() {
        return this.mHasBeenCleared;
    }

    @Override
    protected void onCleared() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onCleared called for ");
            stringBuilder.append(this);
            Log.d((String)TAG, (String)stringBuilder.toString());
        }
        this.mHasBeenCleared = true;
    }

    void removeRetainedFragment(Fragment fragment) {
        if (this.mIsStateSaved) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v((String)TAG, (String)"Ignoring removeRetainedFragment as the state is already saved");
            }
            return;
        }
        boolean bl = this.mRetainedFragments.remove(fragment.mWho) != null;
        if (bl && FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Updating retained Fragments: Removed ");
            stringBuilder.append(fragment);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
    }

    @Deprecated
    void restoreFromSnapshot(FragmentManagerNonConfig object) {
        this.mRetainedFragments.clear();
        this.mChildNonConfigs.clear();
        this.mViewModelStores.clear();
        if (object != null) {
            Object object2 = ((FragmentManagerNonConfig)((Object)object)).getFragments();
            if (object2 != null) {
                Iterator<Fragment> object3 = object2.iterator();
                while (object3.hasNext()) {
                    object2 = object3.next();
                    if (object2 == null) continue;
                    this.mRetainedFragments.put(((Fragment)object2).mWho, (Fragment)object2);
                }
            }
            if ((object2 = ((FragmentManagerNonConfig)((Object)object)).getChildNonConfigs()) != null) {
                for (Map.Entry entry : object2.entrySet()) {
                    FragmentManagerViewModel fragmentManagerViewModel = new FragmentManagerViewModel(this.mStateAutomaticallySaved);
                    fragmentManagerViewModel.restoreFromSnapshot((FragmentManagerNonConfig)entry.getValue());
                    this.mChildNonConfigs.put((String)entry.getKey(), fragmentManagerViewModel);
                }
            }
            if ((object = ((FragmentManagerNonConfig)((Object)object)).getViewModelStores()) != null) {
                this.mViewModelStores.putAll(object);
            }
        }
        this.mHasSavedSnapshot = false;
    }

    void setIsStateSaved(boolean bl) {
        this.mIsStateSaved = bl;
    }

    boolean shouldDestroy(Fragment fragment) {
        if (!this.mRetainedFragments.containsKey(fragment.mWho)) {
            return true;
        }
        if (this.mStateAutomaticallySaved) {
            return this.mHasBeenCleared;
        }
        return this.mHasSavedSnapshot ^ true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("FragmentManagerViewModel{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append("} Fragments (");
        Iterator<Object> iterator2 = this.mRetainedFragments.values().iterator();
        while (iterator2.hasNext()) {
            stringBuilder.append(iterator2.next());
            if (!iterator2.hasNext()) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(") Child Non Config (");
        iterator2 = this.mChildNonConfigs.keySet().iterator();
        while (iterator2.hasNext()) {
            stringBuilder.append((String)iterator2.next());
            if (!iterator2.hasNext()) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(") ViewModelStores (");
        iterator2 = this.mViewModelStores.keySet().iterator();
        while (iterator2.hasNext()) {
            stringBuilder.append((String)iterator2.next());
            if (!iterator2.hasNext()) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}

