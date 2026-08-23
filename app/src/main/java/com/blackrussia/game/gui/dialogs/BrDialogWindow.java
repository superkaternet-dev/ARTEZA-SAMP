/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.text.Layout$Alignment
 *  android.text.StaticLayout
 *  android.text.TextPaint
 *  android.view.KeyEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnKeyListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.view.animation.AnimationUtils
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.Button
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 *  org.json.JSONObject
 */
package com.blackrussia.game.gui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import com.blackrussia.game.gui.util.Utils;
import com.nvidia.devtech.CustomEditText;
import com.nvidia.devtech.NvEventQueueActivity;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class BrDialogWindow {
    Activity aactivity;
    Button button;
    int i;
    private boolean ifOtherMethodClosed = false;
    private NvEventQueueActivity mActivity = NvEventQueueActivity.getInstance();
    private int mClickedButton = -1;
    int mCurrentDialogId;
    private CustomEditText mEditText = null;
    private boolean mIsTab = false;
    private int mListitemToSend = -1;
    private TextView mOrigButton = null;
    private View mSelectedButton = null;
    LinearLayout pon;
    String str;
    String str2;
    String str3;
    String str4;
    String str5;
    String[] strArr;
    String[] strArr2;
    TextView textView;
    TextView textView2;
    boolean z;
    boolean z2;
    boolean z3;
    boolean z4;

    public BrDialogWindow(Activity activity) {
        this.aactivity = activity;
        activity = (LinearLayout)activity.findViewById(2131362036);
        this.pon = activity;
        Utils.HideLayout((View)activity, false);
    }

    static /* synthetic */ boolean access$002(BrDialogWindow brDialogWindow, boolean bl) {
        brDialogWindow.ifOtherMethodClosed = bl;
        return bl;
    }

    static /* synthetic */ View access$102(BrDialogWindow brDialogWindow, View view) {
        brDialogWindow.mSelectedButton = view;
        return view;
    }

    static /* synthetic */ int access$302(BrDialogWindow brDialogWindow, int n) {
        brDialogWindow.mClickedButton = n;
        return n;
    }

    static /* synthetic */ int access$402(BrDialogWindow brDialogWindow, int n) {
        brDialogWindow.mListitemToSend = n;
        return n;
    }

    private TextView createButtonFromOrig(TextView textView, boolean bl, boolean bl2) {
        TextView textView2 = new TextView((Context)this.mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        if (bl) {
            layoutParams.topMargin = NvEventQueueActivity.dpToPx(6.0f, (Context)this.mActivity);
        }
        textView2.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        textView2.setBackground(textView.getBackground());
        textView2.setTypeface(textView.getTypeface());
        textView2.setGravity(textView.getGravity());
        if (!bl2) {
            textView2.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
        }
        textView2.setAllCaps(false);
        textView2.setTextColor(textView.getTextColors().getDefaultColor());
        textView2.setTextSize(0, textView.getTextSize());
        return textView2;
    }

    private float[][] getColumnsWidth(TextView textView, String[] stringArray) {
        float[][] fArrayArray = new float[stringArray.length][];
        for (int i = 0; i < stringArray.length; ++i) {
            String[] stringArray2 = this.getSplittedTabs(stringArray[i]);
            fArrayArray[i] = new float[stringArray2.length];
            for (int j = 0; j < stringArray2.length; ++j) {
                textView.setText((CharSequence)Utils.transfromColors(stringArray2[j]));
                fArrayArray[i][j] = Utils.getTextLength(textView) + NvEventQueueActivity.dpToPx(54.0f, (Context)this.mActivity);
            }
        }
        return fArrayArray;
    }

    private float[] getMaxWidths(float[][] fArray) {
        int n;
        float[] fArray2 = new float[5];
        for (n = 0; n < 5; ++n) {
            fArray2[n] = -1.0f;
        }
        for (n = 0; n < 5; ++n) {
            for (int i = 0; i < fArray.length; ++i) {
                if (n >= fArray[i].length || !(fArray[i][n] >= fArray2[n])) continue;
                fArray2[n] = fArray[i][n];
            }
        }
        return fArray2;
    }

    private String[] getSplittedStrings(String string2) {
        return string2.split(Pattern.quote("\n"));
    }

    private String[] getSplittedTabs(String string2) {
        return string2.split(Pattern.quote("\t"));
    }

    public float[] calcuteStringsLength(TextView textView, String[] stringArray) {
        float[] fArray = new float[stringArray.length];
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textView.getTextSize());
        textPaint.setTypeface(textView.getTypeface());
        for (int i = 0; i < stringArray.length; ++i) {
            fArray[i] = new StaticLayout((CharSequence)Utils.transfromColors(stringArray[i]), textPaint, 10000, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false).getLineWidth(0) + 5.0f;
        }
        return fArray;
    }

    public void close() {
        Utils.HideLayout((View)this.pon, true);
    }

    public void sendResponse(int n) {
        try {
            NvEventQueueActivity.getInstance().sendDialogResponse(n, this.mCurrentDialogId, this.mListitemToSend, this.mEditText.getText().toString().getBytes("windows-1251"));
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }
        this.mClickedButton = -1;
        this.close();
    }

    public void show(int n, int n2, String string2, String string3, String string4, String string5) {
        this.mCurrentDialogId = n;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("i", n2);
            jSONObject.put("l", (Object)string4);
            jSONObject.put("r", (Object)string5);
            jSONObject.put("c", (Object)string2);
            jSONObject.put("s", (Object)string3);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.show(jSONObject);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void show(JSONObject object) {
        block46: {
            block45: {
                this.mClickedButton = -1;
                this.mListitemToSend = -1;
                Utils.makeAllViewsVisible((ViewGroup)this.pon);
                try {
                    this.i = object.getInt("i");
                }
                catch (Exception exception) {
                    // empty catch block
                    break block45;
                }
                try {
                    this.str3 = object.getString("c");
                }
                catch (Exception exception) {}
            }
            try {
                this.str2 = object.getString("s");
            }
            catch (Exception exception) {
                // empty catch block
                break block46;
            }
            try {
                this.str = object.getString("l");
            }
            catch (Exception exception) {
                break block46;
            }
            try {
                this.str4 = object.getString("r");
            }
            catch (Exception exception) {}
        }
        Button button = (Button)this.aactivity.findViewById(2131362029);
        this.button = (Button)this.aactivity.findViewById(2131362028);
        String[] stringArray = (String[])this.aactivity.findViewById(2131362030);
        this.mEditText = (CustomEditText)this.aactivity.findViewById(2131362032);
        button.setText((CharSequence)Utils.transfromColors(this.str));
        this.button.setText((CharSequence)Utils.transfromColors(this.str4));
        stringArray.setText((CharSequence)Utils.transfromColors(this.str3));
        object = this.str4;
        if (object != null && object.length() == 0) {
            this.button.setVisibility(8);
        }
        if (this.i == 4) {
            this.z3 = false;
            this.z2 = true;
            this.i = 2;
        }
        if (this.i == 5) {
            this.z3 = true;
            this.i = 2;
            this.z2 = true;
        }
        button.setOnClickListener(new View.OnClickListener(this){
            final BrDialogWindow this$0;
            {
                this.this$0 = brDialogWindow;
            }

            public void onClick(View view) {
                BrDialogWindow.access$002(this.this$0, true);
                this.this$0.sendResponse(1);
            }
        });
        this.button.setOnClickListener(new View.OnClickListener(this){
            final BrDialogWindow this$0;
            {
                this.this$0 = brDialogWindow;
            }

            public void onClick(View view) {
                BrDialogWindow.access$002(this.this$0, true);
                this.this$0.sendResponse(0);
            }
        });
        Utils.changeTextViewWidth((TextView)stringArray);
        int n = this.i;
        if (n == 0) {
            this.mEditText.setVisibility(8);
            this.aactivity.findViewById(2131362040).setVisibility(8);
            object = (TextView)this.aactivity.findViewById(2131362034);
            object.setText((CharSequence)Utils.transfromColors(this.str2));
            Utils.changeTextViewWidth((TextView)object);
        } else if (n == 1) {
            this.aactivity.findViewById(2131362040).setVisibility(8);
            object = (TextView)this.aactivity.findViewById(2131362034);
            object.setText((CharSequence)Utils.transfromColors(this.str2));
            Utils.changeTextViewWidth((TextView)object);
            this.mEditText.getEditableText().clear();
        } else if (n == 3) {
            this.aactivity.findViewById(2131362040).setVisibility(8);
            object = (TextView)this.aactivity.findViewById(2131362034);
            object.setText((CharSequence)Utils.transfromColors(this.str2));
            Utils.changeTextViewWidth((TextView)object);
            this.mEditText.getEditableText().clear();
        } else if (n == 2) {
            TextView textView;
            LinearLayout.LayoutParams layoutParams;
            LinearLayout linearLayout = (LinearLayout)this.aactivity.findViewById(2131362033);
            this.aactivity.findViewById(2131362038).setVisibility(8);
            this.mEditText.setVisibility(8);
            if (!this.z3) {
                this.aactivity.findViewById(2131362031).setVisibility(8);
                linearLayout.setVisibility(8);
            }
            if (this.mOrigButton == null) {
                this.mOrigButton = (TextView)this.aactivity.findViewById(2131362035);
            }
            TextView textView2 = this.mOrigButton;
            String[] object22 = this.getSplittedStrings(this.str2);
            object = this.z2 ? (Object)this.getMaxWidths(this.getColumnsWidth(textView2, object22)) : null;
            if (this.z3) {
                this.str5 = object22[0];
                layoutParams = new String[object22.length - 1];
                System.arraycopy(object22, 1, layoutParams, 0, object22.length - 1);
                String[] stringArray2 = this.getSplittedTabs(this.str5);
                for (n = 0; n < linearLayout.getChildCount(); ++n) {
                    linearLayout.getChildAt(n).setVisibility(8);
                }
                for (n = 0; n < stringArray2.length; ++n) {
                    textView = (TextView)linearLayout.getChildAt(n);
                    textView.setText((CharSequence)Utils.transfromColors(stringArray2[n]));
                    textView.setVisibility(0);
                }
                this.strArr = stringArray2;
                this.strArr2 = layoutParams;
            } else {
                this.strArr2 = object22;
                this.str5 = null;
                this.strArr = null;
            }
            float[] fArray = this.calcuteStringsLength(textView2, this.strArr2);
            if (fArray != null && fArray.length != 0) {
                Object object2 = fArray[0];
                int n2 = fArray.length;
                for (n = 0; n < n2; ++n) {
                    float f = fArray[n];
                    float f2 = object2;
                    if (f > object2) {
                        f2 = f;
                    }
                    object2 = f2;
                }
                View view = this.aactivity.findViewById(2131362040);
                layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
                layoutParams.width = (int)object2 + NvEventQueueActivity.dpToPx(54.0f, (Context)this.mActivity);
                if (this.z2) {
                    layoutParams.width = 0;
                    n2 = ((JSONObject)object).length;
                    for (n = 0; n < n2; ++n) {
                        object2 = object[n];
                        layoutParams.width = (int)((float)layoutParams.width + object2);
                    }
                }
                if (layoutParams.width < (n = Utils.getTextLength((TextView)stringArray))) {
                    layoutParams.width = n;
                    this.z4 = true;
                } else {
                    this.z4 = false;
                }
                view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
                stringArray = (ViewGroup)this.aactivity.findViewById(2131362036);
                button = (LinearLayout)this.aactivity.findViewById(2131362039);
                layoutParams = stringArray.getViewTreeObserver();
                layoutParams.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(this, (ViewGroup)stringArray, view, (float[])object, linearLayout, (LinearLayout)button){
                    final BrDialogWindow this$0;
                    final View val$findViewById;
                    final LinearLayout val$linearLayout;
                    final LinearLayout val$linearLayout2;
                    final float[] val$maxWidths;
                    final ViewGroup val$viewGroup;
                    {
                        this.this$0 = brDialogWindow;
                        this.val$viewGroup = viewGroup;
                        this.val$findViewById = view;
                        this.val$maxWidths = fArray;
                        this.val$linearLayout = linearLayout;
                        this.val$linearLayout2 = linearLayout2;
                    }

                    public void onGlobalLayout() {
                        Object object;
                        this.val$viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                        if (this.val$viewGroup.getWidth() > this.val$findViewById.getWidth() && this.this$0.z4) {
                            object = this.val$findViewById.getLayoutParams();
                            object.width = -1;
                            this.val$findViewById.setLayoutParams(object);
                        }
                        if (this.val$viewGroup.findViewById(2131362037).getWidth() > this.val$findViewById.getWidth()) {
                            object = this.val$findViewById.getLayoutParams();
                            object.width = -1;
                            this.val$findViewById.setLayoutParams(object);
                        }
                        if ((object = (Object)this.val$maxWidths) != null) {
                            LinearLayout.LayoutParams layoutParams;
                            TextView textView;
                            int n;
                            int n2 = ((ViewGroup.LayoutParams)object).length;
                            object = new float[n2];
                            for (n = 0; n < n2; ++n) {
                                object[n] = (ViewGroup.LayoutParams)(this.val$maxWidths[n] / (float)this.val$findViewById.getWidth());
                            }
                            if (this.this$0.strArr != null) {
                                for (n = 0; n < this.this$0.strArr.length; ++n) {
                                    textView = (TextView)this.val$linearLayout.getChildAt(n);
                                    layoutParams = (LinearLayout.LayoutParams)textView.getLayoutParams();
                                    layoutParams.weight = 1.0f - object[n];
                                    textView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
                                }
                            }
                            for (n = 0; n < this.val$linearLayout2.getChildCount(); ++n) {
                                if (!(this.val$linearLayout2.getChildAt(n) instanceof LinearLayout)) continue;
                                layoutParams = (LinearLayout)this.val$linearLayout2.getChildAt(n);
                                for (n2 = 0; n2 < layoutParams.getChildCount(); ++n2) {
                                    textView = (LinearLayout.LayoutParams)layoutParams.getChildAt(n2).getLayoutParams();
                                    textView.weight = 1.0f - object[n2];
                                    layoutParams.getChildAt(n2).setLayoutParams((ViewGroup.LayoutParams)textView);
                                }
                            }
                        }
                    }
                });
                button.removeAllViews();
                object = button;
                button = textView2;
                for (n = 0; n < this.strArr2.length; ++n) {
                    if (this.z2) {
                        linearLayout = new LinearLayout((Context)this.mActivity);
                        textView2 = new LinearLayout.LayoutParams(-1, -2);
                        if (n >= 1) {
                            textView2.topMargin = NvEventQueueActivity.dpToPx(6.0f, (Context)this.mActivity);
                        }
                        linearLayout.setLayoutParams((ViewGroup.LayoutParams)textView2);
                        linearLayout.setOrientation(0);
                        linearLayout.setPadding(button.getPaddingLeft(), button.getPaddingTop(), button.getPaddingRight(), button.getPaddingBottom());
                        linearLayout.setBackground(ResourcesCompat.getDrawable(this.mActivity.getResources(), 2131230854, null));
                        object.addView((View)linearLayout);
                        for (String string2 : this.getSplittedTabs(this.strArr2[n])) {
                            textView = this.createButtonFromOrig(this.textView, true, false);
                            layoutParams = new TextView((Context)this.mActivity);
                            layoutParams.setText((CharSequence)Utils.transfromColors(string2));
                            layoutParams.setTypeface(textView.getTypeface());
                            layoutParams.setTextSize(13.0f);
                            linearLayout.addView((View)layoutParams);
                        }
                        if (n == 0) {
                            this.mSelectedButton = linearLayout;
                            this.mClickedButton = 1;
                            this.mListitemToSend = n;
                            linearLayout.setBackground(ResourcesCompat.getDrawable(this.mActivity.getResources(), 2131230853, null));
                        }
                        linearLayout.setOnClickListener(new View.OnClickListener(this, n){
                            final BrDialogWindow this$0;
                            final int val$finalI1;
                            {
                                this.this$0 = brDialogWindow;
                                this.val$finalI1 = n;
                            }

                            public void onClick(View view) {
                                if (this.this$0.mSelectedButton != view) {
                                    if (this.this$0.mSelectedButton != null) {
                                        this.this$0.mSelectedButton.setBackground(ResourcesCompat.getDrawable(this.this$0.mActivity.getResources(), 2131230854, null));
                                    }
                                    BrDialogWindow.access$102(this.this$0, view);
                                    BrDialogWindow.access$302(this.this$0, 1);
                                    BrDialogWindow.access$402(this.this$0, this.val$finalI1);
                                    this.this$0.mSelectedButton.setBackground(ResourcesCompat.getDrawable(this.this$0.mActivity.getResources(), 2131230853, null));
                                    this.this$0.mSelectedButton.startAnimation(AnimationUtils.loadAnimation((Context)this.this$0.mActivity, (int)2130771992));
                                    return;
                                }
                                BrDialogWindow.access$302(this.this$0, 1);
                                BrDialogWindow.access$002(this.this$0, true);
                                this.this$0.sendResponse(1);
                                BrDialogWindow.access$102(this.this$0, null);
                            }
                        });
                        this.textView = button;
                    } else {
                        this.textView = button;
                        if (n >= 1) {
                            this.textView2 = this.createButtonFromOrig((TextView)button, true, false);
                        }
                        if (n == 0) {
                            this.textView2 = this.createButtonFromOrig(this.textView, false, false);
                        }
                    }
                    if (!this.z2) {
                        this.textView2.setOnClickListener(new View.OnClickListener(this, n){
                            final BrDialogWindow this$0;
                            final int val$finalI;
                            {
                                this.this$0 = brDialogWindow;
                                this.val$finalI = n;
                            }

                            public void onClick(View view) {
                                if (this.this$0.mSelectedButton != view) {
                                    if (this.this$0.mSelectedButton != null) {
                                        this.this$0.mSelectedButton.setBackground(ResourcesCompat.getDrawable(this.this$0.mActivity.getResources(), 2131230854, null));
                                    }
                                    BrDialogWindow.access$102(this.this$0, view);
                                    BrDialogWindow.access$302(this.this$0, 1);
                                    BrDialogWindow.access$402(this.this$0, this.val$finalI);
                                    this.this$0.mSelectedButton.setBackground(ResourcesCompat.getDrawable(this.this$0.mActivity.getResources(), 2131230853, null));
                                    this.this$0.mSelectedButton.startAnimation(AnimationUtils.loadAnimation((Context)this.this$0.mActivity, (int)2130771992));
                                    return;
                                }
                                BrDialogWindow.access$302(this.this$0, 1);
                                BrDialogWindow.access$002(this.this$0, true);
                                this.this$0.sendResponse(1);
                                BrDialogWindow.access$102(this.this$0, null);
                            }
                        });
                    }
                    if (!this.z2) {
                        this.textView2.setText((CharSequence)Utils.transfromColors(this.strArr2[n]));
                        object.addView((View)this.textView2);
                        if (n == 0) {
                            button = this.textView2;
                            this.mSelectedButton = button;
                            this.mClickedButton = 1;
                            this.mListitemToSend = n;
                            button.setBackground(ResourcesCompat.getDrawable(this.mActivity.getResources(), 2131230853, null));
                        }
                    }
                    button = this.textView;
                }
                button.setVisibility(8);
            } else {
                return;
            }
        }
        this.mEditText.setOnKeyListener(new View.OnKeyListener(this){
            final BrDialogWindow this$0;
            {
                this.this$0 = brDialogWindow;
            }

            public boolean onKey(View view, int n, KeyEvent keyEvent) {
                if (n != 66) {
                    return false;
                }
                ((InputMethodManager)this.this$0.mActivity.getSystemService("input_method")).hideSoftInputFromWindow(this.this$0.mEditText.getWindowToken(), 0);
                this.this$0.mEditText.setFocusable(false);
                this.this$0.mEditText.setFocusableInTouchMode(true);
                return true;
            }
        });
        this.z2 = false;
        this.z3 = false;
        Utils.ShowLayout((View)this.pon, true);
    }
}

