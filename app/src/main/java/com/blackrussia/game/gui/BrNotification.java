/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.os.CountDownTimer
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.view.animation.AnimationUtils
 *  android.widget.Button
 *  android.widget.FrameLayout
 *  android.widget.LinearLayout
 *  android.widget.PopupWindow
 *  android.widget.ProgressBar
 *  android.widget.TextView
 *  org.json.JSONObject
 */
package com.blackrussia.game.gui;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.blackrussia.game.gui.util.Utils;
import com.nvidia.devtech.NvEventQueueActivity;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import org.json.JSONObject;

public class BrNotification {
    public static final int MAX_NOTIFICATIONS = 4;
    public static final int TYPE_BUTTON_TEXT_ORANGE = 5;
    public static final int TYPE_BUTTON_VECTOR_ORANGE = 4;
    public static final int TYPE_MONEY_GREEN = 1;
    public static final int TYPE_MONEY_RED = 0;
    public static final int TYPE_NEW_GUI_INTERACTIVE = 6;
    public static final int TYPE_TEXT_GREEN = 3;
    public static final int TYPE_TEXT_RED = 2;
    public static int mActiveNotifications = 0;
    public static boolean mHiddenAll = false;
    public static BrNotification[] mNotifications;
    public static LinkedList<BrNotification> mQueuedNotifications;
    private NvEventQueueActivity mActivity = NvEventQueueActivity.getInstance();
    private int mDuration = -1;
    private int mId = -1;
    private ProgressBar mProgressBar = null;
    public int mSubid = -1;
    private CountDownTimer mTimer = null;
    private View mView = null;
    private PopupWindow mWindow = null;

    public static void closeNotificationById(int n) {
        if (mNotifications != null) {
            for (int i = 0; i < 4; ++i) {
                BrNotification[] brNotificationArray = mNotifications;
                if (brNotificationArray[n] == null || brNotificationArray[n].mSubid != n) continue;
                brNotificationArray[n].close(2);
                BrNotification.mNotifications[n] = null;
            }
        }
    }

    public static void hideAllNotifications() {
        for (int i = 0; i < 4; ++i) {
            BrNotification[] brNotificationArray = mNotifications;
            if (brNotificationArray[i] == null) continue;
            if (brNotificationArray[i].mTimer != null) {
                brNotificationArray[i].mTimer.cancel();
            }
            BrNotification.mNotifications[i].mWindow.dismiss();
        }
        mHiddenAll = true;
    }

    public static BrNotification newInstance() {
        return new BrNotification();
    }

    public static void resumeNotifications() {
        BrNotification[] brNotificationArray;
        int n;
        for (n = 0; n < 4; ++n) {
            brNotificationArray = mNotifications;
            if (brNotificationArray[n] == null) continue;
            brNotificationArray[n].mWindow.showAtLocation((View)brNotificationArray[n].mActivity.getmRootFrame(), 81, 0, mNotifications[n].getYPosForNotification(n));
        }
        for (n = 0; n < 4; ++n) {
            brNotificationArray = mNotifications;
            if (brNotificationArray[n] == null) continue;
            brNotificationArray[n].startCountdown();
        }
    }

    public void close(int n) {
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (n != 6) {
            this.close(null);
        }
    }

    public void close(JSONObject object) {
        object = this.mWindow;
        if (object != null) {
            object.dismiss();
        }
        int n = 0;
        while (true) {
            block11: {
                block10: {
                    block9: {
                        if (n < ((BrNotification[])(object = mNotifications)).length) break block9;
                        n = -1;
                        break block10;
                    }
                    if (object[n] != this) break block11;
                    object[n] = null;
                }
                if (n != -1) {
                    while (true) {
                        if (n >= ((BrNotification[])(object = mNotifications)).length - 1) {
                            object[((BrNotification[])object).length - 1] = null;
                            if (mQueuedNotifications.size() <= 0) break;
                            object = mNotifications;
                            object[((BrNotification[])object).length - 1] = mQueuedNotifications.getFirst();
                            mQueuedNotifications.removeFirst();
                            object = mNotifications;
                            object = object[((BrNotification[])object).length - 1];
                            ((BrNotification)object).mWindow.showAtLocation((View)((BrNotification)object).mActivity.getmRootFrame(), 81, 0, ((BrNotification)object).getYPosForNotification(mNotifications.length - 1));
                            ((BrNotification)object).startCountdown();
                            break;
                        }
                        int n2 = n + 1;
                        object[n] = object[n2];
                        n = n2;
                    }
                }
                for (n = 0; n < ((BrNotification[])(object = mNotifications)).length; ++n) {
                    if (object[n] == null) continue;
                    object[n].mWindow.update(0, this.getYPosForNotification(n), -1, -1);
                }
                --mActiveNotifications;
                return;
            }
            ++n;
        }
    }

    int getFirstFreeSlot() {
        int n = 0;
        BrNotification[] brNotificationArray;
        while (n < (brNotificationArray = mNotifications).length) {
            if (brNotificationArray[n] == null) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    int getYPosForNotification(int n) {
        return NvEventQueueActivity.dpToPx(50.0f, (Context)this.mActivity) * n + (n + 1) * NvEventQueueActivity.dpToPx(10.0f, (Context)this.mActivity);
    }

    public void show(JSONObject object) {
        String string2;
        int n;
        if (mNotifications == null) {
            mQueuedNotifications = new LinkedList();
            mNotifications = new BrNotification[4];
            for (n = 0; n < 4; ++n) {
                BrNotification.mNotifications[n] = null;
            }
        }
        if (this.mWindow == null) {
            this.mView = ((LayoutInflater)this.mActivity.getSystemService("layout_inflater")).inflate(2131558435, (ViewGroup)null, false);
            string2 = new PopupWindow(this.mView, -2, NvEventQueueActivity.dpToPx(50.0f, (Context)this.mActivity), true);
            this.mWindow = string2;
            string2.setAnimationStyle(2131689689);
            this.mWindow.setSoftInputMode(16);
        }
        n = object.optInt("t");
        String string3 = object.optString("i");
        int n2 = object.optInt("d");
        String string4 = object.optString("a");
        string2 = object.optString("k");
        object = string2;
        if (string2.equalsIgnoreCase("")) {
            object = "\u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u044c";
        }
        this.mDuration = n2;
        string2 = (Button)this.mView.findViewById(2131361905);
        View view = this.mView.findViewById(2131361910);
        TextView textView = (TextView)this.mView.findViewById(2131361908);
        TextView textView2 = (TextView)this.mView.findViewById(2131361909);
        ProgressBar progressBar = (ProgressBar)this.mView.findViewById(2131361907);
        FrameLayout frameLayout = (FrameLayout)this.mView.findViewById(2131361906);
        this.mProgressBar = progressBar;
        n2 = this.mDuration;
        if (n2 != -1) {
            progressBar.setMax(n2 * 1000);
            progressBar.setProgress(this.mDuration * 1000);
        }
        switch (n) {
            default: {
                break;
            }
            case 6: {
                this.close(6);
                return;
            }
            case 5: {
                string2.setVisibility(0);
                textView.setVisibility(8);
                view.setBackground(ContextCompat.getDrawable((Context)this.mActivity, 2131230830));
                string2.setBackground(ContextCompat.getDrawable((Context)this.mActivity, 2131230925));
                break;
            }
            case 4: {
                string2.setVisibility(0);
                textView.setVisibility(8);
                view.setBackground(ContextCompat.getDrawable((Context)this.mActivity, 2131230830));
                string2.setBackground(ContextCompat.getDrawable((Context)this.mActivity, 2131230926));
                break;
            }
            case 3: {
                string2.setVisibility(8);
                textView.setVisibility(8);
                view.setBackground(ContextCompat.getDrawable((Context)this.mActivity, 2131230829));
                break;
            }
            case 2: {
                string2.setVisibility(8);
                textView.setVisibility(8);
                view.setBackground(ContextCompat.getDrawable((Context)this.mActivity, 2131230832));
                break;
            }
            case 1: {
                string2.setVisibility(8);
                textView.setVisibility(0);
                view.setBackground(ContextCompat.getDrawable((Context)this.mActivity, 2131230829));
                break;
            }
            case 0: {
                string2.setVisibility(8);
                textView.setVisibility(0);
                view.setBackground(ContextCompat.getDrawable((Context)this.mActivity, 2131230832));
            }
        }
        if (n == 5 || n == 4) {
            string2.setText((CharSequence)object);
            string2.setOnClickListener(new View.OnClickListener(this, string4){
                final BrNotification this$0;
                final String val$actionforBtn;
                {
                    this.this$0 = brNotification;
                    this.val$actionforBtn = string2;
                }

                public void onClick(View view) {
                    view.startAnimation(AnimationUtils.loadAnimation((Context)this.this$0.mActivity, (int)2130771992));
                    try {
                        NvEventQueueActivity.getInstance().sendCommand(this.val$actionforBtn.getBytes("windows-1251"));
                    }
                    catch (UnsupportedEncodingException unsupportedEncodingException) {
                        unsupportedEncodingException.printStackTrace();
                    }
                    this.this$0.close(1);
                }
            });
        }
        ((LinearLayout)this.mView.findViewById(2131362036)).setOnClickListener(new View.OnClickListener(this){
            final BrNotification this$0;
            {
                this.this$0 = brNotification;
            }

            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation((Context)this.this$0.mActivity, (int)2130771992));
                this.this$0.mView.postDelayed(new Runnable(this){
                    final 2 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void run() {
                        this.this$1.this$0.close(1);
                    }
                }, 100L);
            }
        });
        if (string3 != null) {
            textView2.setText((CharSequence)Utils.transfromColors(string3));
        }
        this.mWindow.setTouchable(true);
        this.mWindow.setFocusable(false);
        this.mWindow.setOutsideTouchable(false);
        ++mActiveNotifications;
        n = this.getFirstFreeSlot();
        if (n == -1) {
            mQueuedNotifications.push(this);
            return;
        }
        progressBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(this, progressBar){
            final BrNotification this$0;
            final ProgressBar val$progressBar;
            {
                this.this$0 = brNotification;
                this.val$progressBar = progressBar;
            }

            public void onGlobalLayout() {
                this.val$progressBar.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                this.this$0.startCountdown();
            }
        });
        n2 = this.getYPosForNotification(n);
        BrNotification.mNotifications[n] = this;
        this.mWindow.showAtLocation((View)this.mActivity.getmRootFrame(), 81, 0, n2);
    }

    public void startCountdown() {
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.mTimer = null;
        }
        if (this.mDuration != -1) {
            this.mTimer = countDownTimer = new CountDownTimer(this, this.mProgressBar.getProgress(), 100L){
                final BrNotification this$0;
                {
                    this.this$0 = brNotification;
                    super(l, l2);
                }

                public void onFinish() {
                    this.this$0.mProgressBar.setProgress(0);
                    this.this$0.close(1);
                }

                public void onTick(long l) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        this.this$0.mProgressBar.setProgress((int)l, true);
                    } else {
                        this.this$0.mProgressBar.setProgress((int)l);
                    }
                }
            };
            countDownTimer.start();
        }
    }
}

