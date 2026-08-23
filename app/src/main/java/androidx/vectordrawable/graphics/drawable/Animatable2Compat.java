/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.Animatable2$AnimationCallback
 *  android.graphics.drawable.Drawable
 */
package androidx.vectordrawable.graphics.drawable;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;

public interface Animatable2Compat
extends Animatable {
    public void clearAnimationCallbacks();

    public void registerAnimationCallback(AnimationCallback var1);

    public boolean unregisterAnimationCallback(AnimationCallback var1);

    public static abstract class AnimationCallback {
        Animatable2.AnimationCallback mPlatformCallback;

        Animatable2.AnimationCallback getPlatformCallback() {
            if (this.mPlatformCallback == null) {
                this.mPlatformCallback = new Animatable2.AnimationCallback(this){
                    final AnimationCallback this$0;
                    {
                        this.this$0 = animationCallback;
                    }

                    public void onAnimationEnd(Drawable drawable2) {
                        this.this$0.onAnimationEnd(drawable2);
                    }

                    public void onAnimationStart(Drawable drawable2) {
                        this.this$0.onAnimationStart(drawable2);
                    }
                };
            }
            return this.mPlatformCallback;
        }

        public void onAnimationEnd(Drawable drawable2) {
        }

        public void onAnimationStart(Drawable drawable2) {
        }
    }
}

