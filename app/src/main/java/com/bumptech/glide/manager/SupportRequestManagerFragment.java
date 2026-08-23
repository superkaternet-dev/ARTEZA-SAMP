/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 */
package com.bumptech.glide.manager;

import android.content.Context;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.ActivityFragmentLifecycle;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SupportRequestManagerFragment
extends Fragment {
    private static final String TAG = "SupportRMFragment";
    private final Set<SupportRequestManagerFragment> childRequestManagerFragments;
    private final ActivityFragmentLifecycle lifecycle;
    private Fragment parentFragmentHint;
    private RequestManager requestManager;
    private final RequestManagerTreeNode requestManagerTreeNode = new SupportFragmentRequestManagerTreeNode(this);
    private SupportRequestManagerFragment rootRequestManagerFragment;

    public SupportRequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    public SupportRequestManagerFragment(ActivityFragmentLifecycle activityFragmentLifecycle) {
        this.childRequestManagerFragments = new HashSet<SupportRequestManagerFragment>();
        this.lifecycle = activityFragmentLifecycle;
    }

    private void addChildRequestManagerFragment(SupportRequestManagerFragment supportRequestManagerFragment) {
        this.childRequestManagerFragments.add(supportRequestManagerFragment);
    }

    private Fragment getParentFragmentUsingHint() {
        Fragment fragment = this.getParentFragment();
        if (fragment == null) {
            fragment = this.parentFragmentHint;
        }
        return fragment;
    }

    private static FragmentManager getRootFragmentManager(Fragment fragment) {
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment.getFragmentManager();
    }

    private boolean isDescendant(Fragment fragment) {
        Fragment fragment2;
        Fragment fragment3 = this.getParentFragmentUsingHint();
        while ((fragment2 = fragment.getParentFragment()) != null) {
            if (fragment2.equals(fragment3)) {
                return true;
            }
            fragment = fragment.getParentFragment();
        }
        return false;
    }

    private void registerFragmentWithRoot(Context object, FragmentManager fragmentManager) {
        this.unregisterFragmentWithRoot();
        object = Glide.get(object).getRequestManagerRetriever().getSupportRequestManagerFragment(fragmentManager);
        this.rootRequestManagerFragment = object;
        if (!this.equals(object)) {
            this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }

    private void removeChildRequestManagerFragment(SupportRequestManagerFragment supportRequestManagerFragment) {
        this.childRequestManagerFragments.remove(supportRequestManagerFragment);
    }

    private void unregisterFragmentWithRoot() {
        SupportRequestManagerFragment supportRequestManagerFragment = this.rootRequestManagerFragment;
        if (supportRequestManagerFragment != null) {
            supportRequestManagerFragment.removeChildRequestManagerFragment(this);
            this.rootRequestManagerFragment = null;
        }
    }

    Set<SupportRequestManagerFragment> getDescendantRequestManagerFragments() {
        SupportRequestManagerFragment supportRequestManagerFragment = this.rootRequestManagerFragment;
        if (supportRequestManagerFragment == null) {
            return Collections.emptySet();
        }
        if (this.equals(supportRequestManagerFragment)) {
            return Collections.unmodifiableSet(this.childRequestManagerFragments);
        }
        HashSet<SupportRequestManagerFragment> hashSet = new HashSet<SupportRequestManagerFragment>();
        for (SupportRequestManagerFragment supportRequestManagerFragment2 : this.rootRequestManagerFragment.getDescendantRequestManagerFragments()) {
            if (!this.isDescendant(supportRequestManagerFragment2.getParentFragmentUsingHint())) continue;
            hashSet.add(supportRequestManagerFragment2);
        }
        return Collections.unmodifiableSet(hashSet);
    }

    ActivityFragmentLifecycle getGlideLifecycle() {
        return this.lifecycle;
    }

    public RequestManager getRequestManager() {
        return this.requestManager;
    }

    public RequestManagerTreeNode getRequestManagerTreeNode() {
        return this.requestManagerTreeNode;
    }

    @Override
    public void onAttach(Context object) {
        block4: {
            super.onAttach((Context)object);
            object = SupportRequestManagerFragment.getRootFragmentManager(this);
            if (object == null) {
                if (Log.isLoggable((String)TAG, (int)5)) {
                    Log.w((String)TAG, (String)"Unable to register fragment with root, ancestor detached");
                }
                return;
            }
            try {
                this.registerFragmentWithRoot(this.getContext(), (FragmentManager)object);
            }
            catch (IllegalStateException illegalStateException) {
                if (!Log.isLoggable((String)TAG, (int)5)) break block4;
                Log.w((String)TAG, (String)"Unable to register fragment with root", (Throwable)illegalStateException);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.lifecycle.onDestroy();
        this.unregisterFragmentWithRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.parentFragmentHint = null;
        this.unregisterFragmentWithRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.lifecycle.onStop();
    }

    void setParentFragmentHint(Fragment fragment) {
        this.parentFragmentHint = fragment;
        if (fragment != null && fragment.getContext() != null) {
            FragmentManager fragmentManager = SupportRequestManagerFragment.getRootFragmentManager(fragment);
            if (fragmentManager == null) {
                return;
            }
            this.registerFragmentWithRoot(fragment.getContext(), fragmentManager);
            return;
        }
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("{parent=");
        stringBuilder.append(this.getParentFragmentUsingHint());
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private class SupportFragmentRequestManagerTreeNode
    implements RequestManagerTreeNode {
        final SupportRequestManagerFragment this$0;

        SupportFragmentRequestManagerTreeNode(SupportRequestManagerFragment supportRequestManagerFragment) {
            this.this$0 = supportRequestManagerFragment;
        }

        @Override
        public Set<RequestManager> getDescendants() {
            Object object = this.this$0.getDescendantRequestManagerFragments();
            HashSet<RequestManager> hashSet = new HashSet<RequestManager>(object.size());
            Iterator<SupportRequestManagerFragment> iterator2 = object.iterator();
            while (iterator2.hasNext()) {
                object = iterator2.next();
                if (((SupportRequestManagerFragment)object).getRequestManager() == null) continue;
                hashSet.add(((SupportRequestManagerFragment)object).getRequestManager());
            }
            return hashSet;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(super.toString());
            stringBuilder.append("{fragment=");
            stringBuilder.append(this.this$0);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}

