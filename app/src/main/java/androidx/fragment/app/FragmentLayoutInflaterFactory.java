/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.LayoutInflater$Factory2
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStateManager;
import androidx.fragment.app.SpecialEffectsController;

class FragmentLayoutInflaterFactory
implements LayoutInflater.Factory2 {
    private static final String TAG = "FragmentManager";
    final FragmentManager mFragmentManager;

    FragmentLayoutInflaterFactory(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public View onCreateView(View object, String object2, Context object3, AttributeSet object4) {
        block18: {
            int n;
            String string2;
            int n2;
            String string3;
            block21: {
                block20: {
                    Object object5;
                    block19: {
                        if (FragmentContainerView.class.getName().equals(object2)) {
                            return new FragmentContainerView((Context)object3, (AttributeSet)object4, this.mFragmentManager);
                        }
                        boolean bl = "fragment".equals(object2);
                        object2 = null;
                        if (!bl) {
                            return null;
                        }
                        object5 = object4.getAttributeValue(null, "class");
                        TypedArray typedArray = object3.obtainStyledAttributes((AttributeSet)object4, R.styleable.Fragment);
                        string3 = object5;
                        if (object5 == null) {
                            string3 = typedArray.getString(R.styleable.Fragment_android_name);
                        }
                        n2 = typedArray.getResourceId(R.styleable.Fragment_android_id, -1);
                        string2 = typedArray.getString(R.styleable.Fragment_android_tag);
                        typedArray.recycle();
                        if (string3 == null || !FragmentFactory.isFragmentClass(object3.getClassLoader(), string3)) break block18;
                        n = object != null ? object.getId() : 0;
                        if (n == -1 && n2 == -1 && string2 == null) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append(object4.getPositionDescription());
                            ((StringBuilder)object).append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
                            ((StringBuilder)object).append(string3);
                            throw new IllegalArgumentException(((StringBuilder)object).toString());
                        }
                        if (n2 != -1) {
                            object2 = this.mFragmentManager.findFragmentById(n2);
                        }
                        object5 = object2;
                        if (object2 == null) {
                            object5 = object2;
                            if (string2 != null) {
                                object5 = this.mFragmentManager.findFragmentByTag(string2);
                            }
                        }
                        object2 = object5;
                        if (object5 == null) {
                            object2 = object5;
                            if (n != -1) {
                                object2 = this.mFragmentManager.findFragmentById(n);
                            }
                        }
                        if (object2 != null) break block19;
                        object2 = this.mFragmentManager.getFragmentFactory().instantiate(object3.getClassLoader(), string3);
                        ((Fragment)object2).mFromLayout = true;
                        int n3 = n2 != 0 ? n2 : n;
                        ((Fragment)object2).mFragmentId = n3;
                        ((Fragment)object2).mContainerId = n;
                        ((Fragment)object2).mTag = string2;
                        ((Fragment)object2).mInLayout = true;
                        ((Fragment)object2).mFragmentManager = this.mFragmentManager;
                        ((Fragment)object2).mHost = this.mFragmentManager.getHost();
                        ((Fragment)object2).onInflate(this.mFragmentManager.getHost().getContext(), (AttributeSet)object4, ((Fragment)object2).mSavedFragmentState);
                        object5 = this.mFragmentManager.addFragment((Fragment)object2);
                        object3 = object2;
                        object4 = object5;
                        if (FragmentManager.isLoggingEnabled(2)) {
                            object3 = new StringBuilder();
                            ((StringBuilder)object3).append("Fragment ");
                            ((StringBuilder)object3).append(object2);
                            ((StringBuilder)object3).append(" has been inflated via the <fragment> tag: id=0x");
                            ((StringBuilder)object3).append(Integer.toHexString(n2));
                            Log.v((String)TAG, (String)((StringBuilder)object3).toString());
                            object3 = object2;
                            object4 = object5;
                        }
                        break block20;
                    }
                    if (((Fragment)object2).mInLayout) break block21;
                    ((Fragment)object2).mInLayout = true;
                    ((Fragment)object2).mFragmentManager = this.mFragmentManager;
                    ((Fragment)object2).mHost = this.mFragmentManager.getHost();
                    ((Fragment)object2).onInflate(this.mFragmentManager.getHost().getContext(), (AttributeSet)object4, ((Fragment)object2).mSavedFragmentState);
                    object5 = this.mFragmentManager.createOrGetFragmentStateManager((Fragment)object2);
                    object3 = object2;
                    object4 = object5;
                    if (FragmentManager.isLoggingEnabled(2)) {
                        object3 = new StringBuilder();
                        ((StringBuilder)object3).append("Retained Fragment ");
                        ((StringBuilder)object3).append(object2);
                        ((StringBuilder)object3).append(" has been re-attached via the <fragment> tag: id=0x");
                        ((StringBuilder)object3).append(Integer.toHexString(n2));
                        Log.v((String)TAG, (String)((StringBuilder)object3).toString());
                        object4 = object5;
                        object3 = object2;
                    }
                }
                ((Fragment)object3).mContainer = (ViewGroup)object;
                ((FragmentStateManager)object4).moveToExpectedState();
                ((FragmentStateManager)object4).ensureInflatedView();
                if (((Fragment)object3).mView != null) {
                    if (n2 != 0) {
                        ((Fragment)object3).mView.setId(n2);
                    }
                    if (((Fragment)object3).mView.getTag() == null) {
                        ((Fragment)object3).mView.setTag((Object)string2);
                    }
                    ((Fragment)object3).mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(this, (FragmentStateManager)object4){
                        final FragmentLayoutInflaterFactory this$0;
                        final FragmentStateManager val$fragmentStateManager;
                        {
                            this.this$0 = fragmentLayoutInflaterFactory;
                            this.val$fragmentStateManager = fragmentStateManager;
                        }

                        public void onViewAttachedToWindow(View object) {
                            object = this.val$fragmentStateManager.getFragment();
                            this.val$fragmentStateManager.moveToExpectedState();
                            SpecialEffectsController.getOrCreateController((ViewGroup)object.mView.getParent(), this.this$0.mFragmentManager).forceCompleteAllOperations();
                        }

                        public void onViewDetachedFromWindow(View view) {
                        }
                    });
                    return ((Fragment)object3).mView;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Fragment ");
                ((StringBuilder)object).append(string3);
                ((StringBuilder)object).append(" did not create a view.");
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append(object4.getPositionDescription());
            ((StringBuilder)object).append(": Duplicate id 0x");
            ((StringBuilder)object).append(Integer.toHexString(n2));
            ((StringBuilder)object).append(", tag ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(", or parent id 0x");
            ((StringBuilder)object).append(Integer.toHexString(n));
            ((StringBuilder)object).append(" with another fragment for ");
            ((StringBuilder)object).append(string3);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        return null;
    }

    public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
        return this.onCreateView(null, string2, context, attributeSet);
    }
}

