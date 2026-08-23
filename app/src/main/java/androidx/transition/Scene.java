/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.R;

public class Scene {
    private Context mContext;
    private Runnable mEnterAction;
    private Runnable mExitAction;
    private View mLayout;
    private int mLayoutId = -1;
    private ViewGroup mSceneRoot;

    public Scene(ViewGroup viewGroup) {
        this.mSceneRoot = viewGroup;
    }

    private Scene(ViewGroup viewGroup, int n, Context context) {
        this.mContext = context;
        this.mSceneRoot = viewGroup;
        this.mLayoutId = n;
    }

    public Scene(ViewGroup viewGroup, View view) {
        this.mSceneRoot = viewGroup;
        this.mLayout = view;
    }

    static Scene getCurrentScene(View view) {
        return (Scene)view.getTag(R.id.transition_current_scene);
    }

    public static Scene getSceneForLayout(ViewGroup object, int n, Context context) {
        Object object2;
        SparseArray sparseArray = object2 = (SparseArray)object.getTag(R.id.transition_scene_layoutid_cache);
        if (object2 == null) {
            sparseArray = new SparseArray();
            object.setTag(R.id.transition_scene_layoutid_cache, (Object)sparseArray);
        }
        if ((object2 = (Scene)sparseArray.get(n)) != null) {
            return object2;
        }
        object = new Scene((ViewGroup)object, n, context);
        sparseArray.put(n, object);
        return object;
    }

    static void setCurrentScene(View view, Scene scene) {
        view.setTag(R.id.transition_current_scene, (Object)scene);
    }

    public void enter() {
        Runnable runnable;
        if (this.mLayoutId > 0 || this.mLayout != null) {
            this.getSceneRoot().removeAllViews();
            if (this.mLayoutId > 0) {
                LayoutInflater.from((Context)this.mContext).inflate(this.mLayoutId, this.mSceneRoot);
            } else {
                this.mSceneRoot.addView(this.mLayout);
            }
        }
        if ((runnable = this.mEnterAction) != null) {
            runnable.run();
        }
        Scene.setCurrentScene((View)this.mSceneRoot, this);
    }

    public void exit() {
        Runnable runnable;
        if (Scene.getCurrentScene((View)this.mSceneRoot) == this && (runnable = this.mExitAction) != null) {
            runnable.run();
        }
    }

    public ViewGroup getSceneRoot() {
        return this.mSceneRoot;
    }

    boolean isCreatedFromLayoutResource() {
        boolean bl = this.mLayoutId > 0;
        return bl;
    }

    public void setEnterAction(Runnable runnable) {
        this.mEnterAction = runnable;
    }

    public void setExitAction(Runnable runnable) {
        this.mExitAction = runnable;
    }
}

