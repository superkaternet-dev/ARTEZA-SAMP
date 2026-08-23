/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package androidx.constraintlayout.motion.utils;

import android.util.Log;
import androidx.constraintlayout.motion.widget.MotionInterpolator;

public class StopLogic
extends MotionInterpolator {
    private boolean mBackwards = false;
    private float mLastPosition;
    private int mNumberOfStages;
    private float mStage1Duration;
    private float mStage1EndPosition;
    private float mStage1Velocity;
    private float mStage2Duration;
    private float mStage2EndPosition;
    private float mStage2Velocity;
    private float mStage3Duration;
    private float mStage3EndPosition;
    private float mStage3Velocity;
    private float mStartPosition;
    private String mType;

    private float calcY(float f) {
        float f2 = this.mStage1Duration;
        if (f <= f2) {
            float f3 = this.mStage1Velocity;
            return f3 * f + (this.mStage2Velocity - f3) * f * f / (f2 * 2.0f);
        }
        int n = this.mNumberOfStages;
        if (n == 1) {
            return this.mStage1EndPosition;
        }
        if ((f -= f2) < (f2 = this.mStage2Duration)) {
            float f4 = this.mStage1EndPosition;
            float f5 = this.mStage2Velocity;
            return f4 + f5 * f + (this.mStage3Velocity - f5) * f * f / (f2 * 2.0f);
        }
        if (n == 2) {
            return this.mStage2EndPosition;
        }
        float f6 = f - f2;
        if (f6 < (f = this.mStage3Duration)) {
            float f7 = this.mStage2EndPosition;
            f2 = this.mStage3Velocity;
            return f7 + f2 * f6 - f2 * f6 * f6 / (f * 2.0f);
        }
        return this.mStage3EndPosition;
    }

    private void setup(float f, float f2, float f3, float f4, float f5) {
        if (f == 0.0f) {
            f = 1.0E-4f;
        }
        this.mStage1Velocity = f;
        float f6 = f / f3;
        float f7 = f6 * f / 2.0f;
        if (f < 0.0f) {
            f5 = (float)Math.sqrt(f3 * (f2 - -f / f3 * f / 2.0f));
            if (f5 < f4) {
                this.mType = "backward accelerate, decelerate";
                this.mNumberOfStages = 2;
                this.mStage1Velocity = f;
                this.mStage2Velocity = f5;
                this.mStage3Velocity = 0.0f;
                this.mStage1Duration = f4 = (f5 - f) / f3;
                this.mStage2Duration = f5 / f3;
                this.mStage1EndPosition = (f + f5) * f4 / 2.0f;
                this.mStage2EndPosition = f2;
                this.mStage3EndPosition = f2;
                return;
            }
            this.mType = "backward accelerate cruse decelerate";
            this.mNumberOfStages = 3;
            this.mStage1Velocity = f;
            this.mStage2Velocity = f4;
            this.mStage3Velocity = f4;
            this.mStage1Duration = f5 = (f4 - f) / f3;
            this.mStage3Duration = f3 = f4 / f3;
            f = (f + f4) * f5 / 2.0f;
            f3 = f4 * f3 / 2.0f;
            this.mStage2Duration = (f2 - f - f3) / f4;
            this.mStage1EndPosition = f;
            this.mStage2EndPosition = f2 - f3;
            this.mStage3EndPosition = f2;
            return;
        }
        if (f7 >= f2) {
            this.mType = "hard stop";
            f3 = 2.0f * f2 / f;
            this.mNumberOfStages = 1;
            this.mStage1Velocity = f;
            this.mStage2Velocity = 0.0f;
            this.mStage1EndPosition = f2;
            this.mStage1Duration = f3;
            return;
        }
        float f8 = (f7 = f2 - f7) / f;
        if (f8 + f6 < f5) {
            this.mType = "cruse decelerate";
            this.mNumberOfStages = 2;
            this.mStage1Velocity = f;
            this.mStage2Velocity = f;
            this.mStage3Velocity = 0.0f;
            this.mStage1EndPosition = f7;
            this.mStage2EndPosition = f2;
            this.mStage1Duration = f8;
            this.mStage2Duration = f / f3;
            return;
        }
        f5 = (float)Math.sqrt(f3 * f2 + f * f / 2.0f);
        this.mStage1Duration = (f5 - f) / f3;
        this.mStage2Duration = f5 / f3;
        if (f5 < f4) {
            this.mType = "accelerate decelerate";
            this.mNumberOfStages = 2;
            this.mStage1Velocity = f;
            this.mStage2Velocity = f5;
            this.mStage3Velocity = 0.0f;
            this.mStage1Duration = f4 = (f5 - f) / f3;
            this.mStage2Duration = f5 / f3;
            this.mStage1EndPosition = (f + f5) * f4 / 2.0f;
            this.mStage2EndPosition = f2;
            return;
        }
        this.mType = "accelerate cruse decelerate";
        this.mNumberOfStages = 3;
        this.mStage1Velocity = f;
        this.mStage2Velocity = f4;
        this.mStage3Velocity = f4;
        this.mStage1Duration = f5 = (f4 - f) / f3;
        this.mStage3Duration = f3 = f4 / f3;
        f = (f + f4) * f5 / 2.0f;
        f3 = f4 * f3 / 2.0f;
        this.mStage2Duration = (f2 - f - f3) / f4;
        this.mStage1EndPosition = f;
        this.mStage2EndPosition = f2 - f3;
        this.mStage3EndPosition = f2;
    }

    public void config(float f, float f2, float f3, float f4, float f5, float f6) {
        this.mStartPosition = f;
        boolean bl = f > f2;
        this.mBackwards = bl;
        if (bl) {
            this.setup(-f3, f - f2, f5, f6, f4);
        } else {
            this.setup(f3, f2 - f, f5, f6, f4);
        }
    }

    public void debug(String string2, String string3, float f) {
        float f2;
        CharSequence charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append(string3);
        ((StringBuilder)charSequence).append(" ===== ");
        ((StringBuilder)charSequence).append(this.mType);
        Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string3);
        charSequence = this.mBackwards ? "backwards" : "forward ";
        stringBuilder.append((String)charSequence);
        stringBuilder.append(" time = ");
        stringBuilder.append(f);
        stringBuilder.append("  stages ");
        stringBuilder.append(this.mNumberOfStages);
        Log.v((String)string2, (String)stringBuilder.toString());
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append(string3);
        ((StringBuilder)charSequence).append(" dur ");
        ((StringBuilder)charSequence).append(this.mStage1Duration);
        ((StringBuilder)charSequence).append(" vel ");
        ((StringBuilder)charSequence).append(this.mStage1Velocity);
        ((StringBuilder)charSequence).append(" pos ");
        ((StringBuilder)charSequence).append(this.mStage1EndPosition);
        Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
        if (this.mNumberOfStages > 1) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string3);
            ((StringBuilder)charSequence).append(" dur ");
            ((StringBuilder)charSequence).append(this.mStage2Duration);
            ((StringBuilder)charSequence).append(" vel ");
            ((StringBuilder)charSequence).append(this.mStage2Velocity);
            ((StringBuilder)charSequence).append(" pos ");
            ((StringBuilder)charSequence).append(this.mStage2EndPosition);
            Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
        }
        if (this.mNumberOfStages > 2) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string3);
            ((StringBuilder)charSequence).append(" dur ");
            ((StringBuilder)charSequence).append(this.mStage3Duration);
            ((StringBuilder)charSequence).append(" vel ");
            ((StringBuilder)charSequence).append(this.mStage3Velocity);
            ((StringBuilder)charSequence).append(" pos ");
            ((StringBuilder)charSequence).append(this.mStage3EndPosition);
            Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
        }
        if (f <= (f2 = this.mStage1Duration)) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string3);
            ((StringBuilder)charSequence).append("stage 0");
            Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
            return;
        }
        int n = this.mNumberOfStages;
        if (n == 1) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string3);
            ((StringBuilder)charSequence).append("end stage 0");
            Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
            return;
        }
        if ((f2 = f - f2) < (f = this.mStage2Duration)) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string3);
            ((StringBuilder)charSequence).append(" stage 1");
            Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
            return;
        }
        if (n == 2) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string3);
            ((StringBuilder)charSequence).append("end stage 1");
            Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
            return;
        }
        if (f2 - f < this.mStage3Duration) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string3);
            ((StringBuilder)charSequence).append(" stage 2");
            Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
            return;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append(string3);
        ((StringBuilder)charSequence).append(" end stage 2");
        Log.v((String)string2, (String)((StringBuilder)charSequence).toString());
    }

    @Override
    public float getInterpolation(float f) {
        float f2 = this.calcY(f);
        this.mLastPosition = f;
        f = this.mBackwards ? this.mStartPosition - f2 : this.mStartPosition + f2;
        return f;
    }

    @Override
    public float getVelocity() {
        float f = this.mBackwards ? -this.getVelocity(this.mLastPosition) : this.getVelocity(this.mLastPosition);
        return f;
    }

    public float getVelocity(float f) {
        float f2 = this.mStage1Duration;
        if (f <= f2) {
            float f3 = this.mStage1Velocity;
            return f3 + (this.mStage2Velocity - f3) * f / f2;
        }
        int n = this.mNumberOfStages;
        if (n == 1) {
            return 0.0f;
        }
        if ((f -= f2) < (f2 = this.mStage2Duration)) {
            float f4 = this.mStage2Velocity;
            return f4 + (this.mStage3Velocity - f4) * f / f2;
        }
        if (n == 2) {
            return this.mStage2EndPosition;
        }
        float f5 = f - f2;
        if (f5 < (f2 = this.mStage3Duration)) {
            f = this.mStage3Velocity;
            return f - f * f5 / f2;
        }
        return this.mStage3EndPosition;
    }
}

