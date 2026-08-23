/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.graphics.Color
 *  android.graphics.PorterDuff$Mode
 *  android.view.View
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package com.blackrussia.game.gui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.blackrussia.game.gui.util.SeekArc;
import com.blackrussia.game.gui.util.Utils;
import java.util.Formatter;

public class Speedometer {
    public Activity activity;
    public ImageView mBG;
    public TextView mCarHP;
    public ImageView mEngine;
    public TextView mFuel;
    public ConstraintLayout mInputLayout;
    public ImageView mLight;
    public ImageView mLock;
    public TextView mMileage;
    public TextView mSpeed;
    public SeekArc mSpeedLine;

    public Speedometer(Activity activity) {
        ConstraintLayout constraintLayout;
        this.mInputLayout = constraintLayout = (ConstraintLayout)activity.findViewById(2131362404);
        this.mBG = (ImageView)activity.findViewById(2131362169);
        this.mSpeed = (TextView)activity.findViewById(2131362403);
        this.mFuel = (TextView)activity.findViewById(2131362400);
        this.mCarHP = (TextView)activity.findViewById(2131362398);
        this.mMileage = (TextView)activity.findViewById(2131362467);
        this.mSpeedLine = (SeekArc)activity.findViewById(2131362401);
        this.mEngine = (ImageView)activity.findViewById(2131362399);
        this.mLock = (ImageView)activity.findViewById(2131362402);
        Utils.HideLayout((View)constraintLayout, false);
    }

    public void HideSpeed() {
        Utils.HideLayout((View)this.mInputLayout, false);
    }

    public void ShowSpeed() {
        Utils.ShowLayout((View)this.mInputLayout, false);
    }

    public void UpdateSpeedInfo(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.mFuel.setText((CharSequence)new Formatter().format("%d", n2).toString());
        this.mMileage.setText((CharSequence)new Formatter().format("%06d", n4).toString());
        this.mCarHP.setText((CharSequence)new Formatter().format("%d%s", n3 /= 10, "%").toString());
        this.mSpeedLine.setProgress(n);
        this.mSpeed.setText((CharSequence)String.valueOf(n));
        if (n == 0) {
            this.mSpeed.setAlpha(0.4f);
            this.mSpeed.setText((CharSequence)"000");
            this.mSpeed.setTextColor(Color.parseColor((String)"#1a1a1a"));
        }
        if (n != 0) {
            this.mSpeed.setAlpha(1.0f);
            this.mSpeed.setText((CharSequence)String.valueOf(n));
            this.mSpeed.setTextColor(Color.parseColor((String)"#FFFFFF"));
        }
        if (n5 == 1) {
            this.mEngine.setColorFilter(Color.parseColor((String)"#00FF00"), PorterDuff.Mode.SRC_IN);
        } else {
            this.mEngine.setColorFilter(Color.parseColor((String)"#FF0000"), PorterDuff.Mode.SRC_IN);
        }
        if (n8 == 1) {
            this.mLock.setColorFilter(Color.parseColor((String)"#00FF00"), PorterDuff.Mode.SRC_IN);
        } else {
            this.mLock.setColorFilter(Color.parseColor((String)"#FF0000"), PorterDuff.Mode.SRC_IN);
        }
    }
}

