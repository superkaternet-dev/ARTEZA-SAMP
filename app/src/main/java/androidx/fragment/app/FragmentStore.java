/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.util.Log;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManagerViewModel;
import androidx.fragment.app.FragmentState;
import androidx.fragment.app.FragmentStateManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class FragmentStore {
    private static final String TAG = "FragmentManager";
    private final HashMap<String, FragmentStateManager> mActive;
    private final ArrayList<Fragment> mAdded = new ArrayList();
    private FragmentManagerViewModel mNonConfig;

    FragmentStore() {
        this.mActive = new HashMap();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void addFragment(Fragment fragment) {
        if (!this.mAdded.contains(fragment)) {
            ArrayList<Fragment> arrayList = this.mAdded;
            synchronized (arrayList) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment already added: ");
        stringBuilder.append(fragment);
        throw new IllegalStateException(stringBuilder.toString());
    }

    void burpActive() {
        this.mActive.values().removeAll(Collections.singleton(null));
    }

    boolean containsActiveFragment(String string2) {
        boolean bl = this.mActive.get(string2) != null;
        return bl;
    }

    void dispatchStateChange(int n) {
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager == null) continue;
            fragmentStateManager.setFragmentManagerState(n);
        }
    }

    void dump(String string2, FileDescriptor object, PrintWriter printWriter, String[] stringArray) {
        int n;
        CharSequence charSequence = new StringBuilder();
        charSequence.append(string2);
        charSequence.append("    ");
        charSequence = charSequence.toString();
        if (!this.mActive.isEmpty()) {
            printWriter.print(string2);
            printWriter.print("Active Fragments:");
            for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
                printWriter.print(string2);
                if (fragmentStateManager != null) {
                    Fragment fragment = fragmentStateManager.getFragment();
                    printWriter.println(fragment);
                    fragment.dump((String)charSequence, (FileDescriptor)object, printWriter, stringArray);
                    continue;
                }
                printWriter.println("null");
            }
        }
        if ((n = this.mAdded.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Added Fragments:");
            for (int i = 0; i < n; ++i) {
                object = this.mAdded.get(i);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.println(((Fragment)object).toString());
            }
        }
    }

    Fragment findActiveFragment(String object) {
        if ((object = this.mActive.get(object)) != null) {
            return ((FragmentStateManager)object).getFragment();
        }
        return null;
    }

    Fragment findFragmentById(int n) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null || fragment.mFragmentId != n) continue;
            return fragment;
        }
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager == null) continue;
            Fragment fragment = fragmentStateManager.getFragment();
            if (fragment.mFragmentId != n) continue;
            return fragment;
        }
        return null;
    }

    Fragment findFragmentByTag(String string2) {
        if (string2 != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                Fragment fragment = this.mAdded.get(i);
                if (fragment == null || !string2.equals(fragment.mTag)) continue;
                return fragment;
            }
        }
        if (string2 != null) {
            for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
                if (fragmentStateManager == null) continue;
                Fragment fragment = fragmentStateManager.getFragment();
                if (!string2.equals(fragment.mTag)) continue;
                return fragment;
            }
        }
        return null;
    }

    Fragment findFragmentByWho(String string2) {
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            Fragment fragment;
            if (fragmentStateManager == null || (fragment = fragmentStateManager.getFragment().findFragmentByWho(string2)) == null) continue;
            return fragment;
        }
        return null;
    }

    int findFragmentIndexInContainer(Fragment fragment) {
        int n;
        ViewGroup viewGroup = fragment.mContainer;
        if (viewGroup == null) {
            return -1;
        }
        int n2 = this.mAdded.indexOf(fragment);
        for (n = n2 - 1; n >= 0; --n) {
            fragment = this.mAdded.get(n);
            if (fragment.mContainer != viewGroup || fragment.mView == null) continue;
            return viewGroup.indexOfChild(fragment.mView) + 1;
        }
        for (n = n2 + 1; n < this.mAdded.size(); ++n) {
            fragment = this.mAdded.get(n);
            if (fragment.mContainer != viewGroup || fragment.mView == null) continue;
            return viewGroup.indexOfChild(fragment.mView);
        }
        return -1;
    }

    int getActiveFragmentCount() {
        return this.mActive.size();
    }

    List<FragmentStateManager> getActiveFragmentStateManagers() {
        ArrayList<FragmentStateManager> arrayList = new ArrayList<FragmentStateManager>();
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager == null) continue;
            arrayList.add(fragmentStateManager);
        }
        return arrayList;
    }

    List<Fragment> getActiveFragments() {
        ArrayList<Fragment> arrayList = new ArrayList<Fragment>();
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager != null) {
                arrayList.add(fragmentStateManager.getFragment());
                continue;
            }
            arrayList.add(null);
        }
        return arrayList;
    }

    FragmentStateManager getFragmentStateManager(String string2) {
        return this.mActive.get(string2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<Fragment> arrayList = this.mAdded;
        synchronized (arrayList) {
            return new ArrayList<Fragment>(this.mAdded);
        }
    }

    FragmentManagerViewModel getNonConfig() {
        return this.mNonConfig;
    }

    void makeActive(FragmentStateManager object) {
        Fragment fragment = ((FragmentStateManager)object).getFragment();
        if (this.containsActiveFragment(fragment.mWho)) {
            return;
        }
        this.mActive.put(fragment.mWho, (FragmentStateManager)object);
        if (fragment.mRetainInstanceChangedWhileDetached) {
            if (fragment.mRetainInstance) {
                this.mNonConfig.addRetainedFragment(fragment);
            } else {
                this.mNonConfig.removeRetainedFragment(fragment);
            }
            fragment.mRetainInstanceChangedWhileDetached = false;
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Added fragment to active set ");
            ((StringBuilder)object).append(fragment);
            Log.v((String)TAG, (String)((StringBuilder)object).toString());
        }
    }

    void makeInactive(FragmentStateManager object) {
        object = ((FragmentStateManager)object).getFragment();
        if (((Fragment)object).mRetainInstance) {
            this.mNonConfig.removeRetainedFragment((Fragment)object);
        }
        if ((FragmentStateManager)this.mActive.put(((Fragment)object).mWho, null) == null) {
            return;
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Removed fragment from active set ");
            stringBuilder.append(object);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
    }

    void moveToExpectedState() {
        for (Fragment fragment : this.mAdded) {
            FragmentStateManager object = this.mActive.get(fragment.mWho);
            if (object == null) continue;
            object.moveToExpectedState();
        }
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager == null) continue;
            fragmentStateManager.moveToExpectedState();
            Fragment fragment = fragmentStateManager.getFragment();
            boolean bl = fragment.mRemoving && !fragment.isInBackStack();
            if (!bl) continue;
            this.makeInactive(fragmentStateManager);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void removeFragment(Fragment fragment) {
        ArrayList<Fragment> arrayList = this.mAdded;
        synchronized (arrayList) {
            this.mAdded.remove(fragment);
        }
        fragment.mAdded = false;
    }

    void resetActiveFragments() {
        this.mActive.clear();
    }

    void restoreAddedFragments(List<String> object) {
        this.mAdded.clear();
        if (object != null) {
            Iterator<String> iterator2 = object.iterator();
            while (iterator2.hasNext()) {
                object = iterator2.next();
                Object object2 = this.findActiveFragment((String)object);
                if (object2 != null) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("restoreSaveState: added (");
                        stringBuilder.append((String)object);
                        stringBuilder.append("): ");
                        stringBuilder.append(object2);
                        Log.v((String)TAG, (String)stringBuilder.toString());
                    }
                    this.addFragment((Fragment)object2);
                    continue;
                }
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("No instantiated fragment for (");
                ((StringBuilder)object2).append((String)object);
                ((StringBuilder)object2).append(")");
                throw new IllegalStateException(((StringBuilder)object2).toString());
            }
        }
    }

    ArrayList<FragmentState> saveActiveFragments() {
        ArrayList<FragmentState> arrayList = new ArrayList<FragmentState>(this.mActive.size());
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager == null) continue;
            Fragment fragment = fragmentStateManager.getFragment();
            FragmentState fragmentState = fragmentStateManager.saveState();
            arrayList.add(fragmentState);
            if (!FragmentManager.isLoggingEnabled(2)) continue;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Saved state of ");
            stringBuilder.append(fragment);
            stringBuilder.append(": ");
            stringBuilder.append(fragmentState.mSavedFragmentState);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    ArrayList<String> saveAddedFragments() {
        ArrayList<Fragment> arrayList = this.mAdded;
        synchronized (arrayList) {
            if (this.mAdded.isEmpty()) {
                return null;
            }
            ArrayList<String> arrayList2 = new ArrayList<String>(this.mAdded.size());
            Iterator<Fragment> iterator2 = this.mAdded.iterator();
            while (iterator2.hasNext()) {
                Fragment fragment = iterator2.next();
                arrayList2.add(fragment.mWho);
                if (!FragmentManager.isLoggingEnabled(2)) continue;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("saveAllState: adding fragment (");
                stringBuilder.append(fragment.mWho);
                stringBuilder.append("): ");
                stringBuilder.append(fragment);
                Log.v((String)TAG, (String)stringBuilder.toString());
            }
            return arrayList2;
        }
    }

    void setNonConfig(FragmentManagerViewModel fragmentManagerViewModel) {
        this.mNonConfig = fragmentManagerViewModel;
    }
}

