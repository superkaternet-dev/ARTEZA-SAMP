/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.view.KeyEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.Button
 *  android.widget.EditText
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.TextView
 *  android.widget.TextView$OnEditorActionListener
 */
package com.nvidia.devtech;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nvidia.devtech.HeightProvider;
import java.util.ArrayList;

public class InputManager {
    private Runnable mAnimTask = null;
    private Button mButtonHistoryNext = null;
    private Button mButtonHistoryPrev = null;
    private Button mButtonSlash = null;
    private Activity mContext = null;
    private int mCurrentHistoryMessage = 0;
    private HeightProvider mHeightProvider = null;
    private EditText mInputEt = null;
    private ArrayList<String> mInputHistory = null;
    private LinearLayout mInputLayout = null;
    private boolean mIsShowing = false;
    private final int mMaxHistory;
    private String mSavedInput = null;

    public InputManager(Activity activity) {
        this.mMaxHistory = 20;
        this.mContext = activity;
        this.mInputLayout = (LinearLayout)activity.findViewById(2131362214);
        this.mInputEt = (EditText)this.mContext.findViewById(2131362212);
        this.mButtonSlash = (Button)this.mContext.findViewById(2131362211);
        this.mButtonHistoryPrev = (Button)this.mContext.findViewById(2131362210);
        this.mButtonHistoryNext = (Button)this.mContext.findViewById(2131362209);
        this.mInputHistory = new ArrayList();
        this.mButtonSlash.setOnClickListener(new View.OnClickListener(this){
            final InputManager this$0;
            {
                this.this$0 = inputManager;
            }

            public void onClick(View view) {
                this.this$0.mInputEt.getText().insert(this.this$0.mInputEt.getSelectionStart(), (CharSequence)"/");
            }
        });
        this.mButtonHistoryPrev.setOnClickListener(new View.OnClickListener(this){
            final InputManager this$0;
            {
                this.this$0 = inputManager;
            }

            public void onClick(View view) {
                InputManager.access$110(this.this$0);
                if (this.this$0.mCurrentHistoryMessage < 0) {
                    InputManager.access$102(this.this$0, 0);
                }
                if (this.this$0.mCurrentHistoryMessage <= 0) {
                    this.this$0.mInputEt.setText((CharSequence)"");
                    return;
                }
                this.this$0.mInputEt.setText((CharSequence)this.this$0.mInputHistory.get(this.this$0.mCurrentHistoryMessage - 1));
                this.this$0.mInputEt.setSelection(this.this$0.mInputEt.getText().length());
            }
        });
        this.mButtonHistoryNext.setOnClickListener(new View.OnClickListener(this){
            final InputManager this$0;
            {
                this.this$0 = inputManager;
            }

            public void onClick(View view) {
                InputManager.access$108(this.this$0);
                if (this.this$0.mCurrentHistoryMessage - 1 >= this.this$0.mInputHistory.size()) {
                    InputManager.access$110(this.this$0);
                }
                if (this.this$0.mCurrentHistoryMessage <= 0) {
                    return;
                }
                this.this$0.mInputEt.setText((CharSequence)this.this$0.mInputHistory.get(this.this$0.mCurrentHistoryMessage - 1));
                this.this$0.mInputEt.setSelection(this.this$0.mInputEt.getText().length());
            }
        });
        this.mInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener(this){
            final InputManager this$0;
            {
                this.this$0 = inputManager;
            }

            public boolean onEditorAction(TextView object, int n, KeyEvent keyEvent) {
                if ((n == 6 || n == 5) && (object = this.this$0.mInputEt.getText()) != null) {
                    object = object.toString();
                    this.this$0.mInputEt.setText((CharSequence)"");
                    this.this$0.OnInputEnd((String)object);
                }
                return false;
            }
        });
        this.HideInputLayout();
    }

    private void OnInputEnd(String string2) {
        if (this.mInputHistory.size() >= 20) {
            ArrayList<String> arrayList = this.mInputHistory;
            arrayList.remove(arrayList.size() - 1);
        }
        this.mInputHistory.add(0, string2);
        ((InputListener)this.mContext).OnInputEnd(string2);
    }

    static /* synthetic */ int access$102(InputManager inputManager, int n) {
        inputManager.mCurrentHistoryMessage = n;
        return n;
    }

    static /* synthetic */ int access$108(InputManager inputManager) {
        int n = inputManager.mCurrentHistoryMessage;
        inputManager.mCurrentHistoryMessage = n + 1;
        return n;
    }

    static /* synthetic */ int access$110(InputManager inputManager) {
        int n = inputManager.mCurrentHistoryMessage;
        inputManager.mCurrentHistoryMessage = n - 1;
        return n;
    }

    public void HideInputLayout() {
        Runnable runnable;
        this.mCurrentHistoryMessage = 0;
        if (this.mInputEt.getEditableText() != null) {
            this.mSavedInput = this.mInputEt.getEditableText().toString();
        }
        if ((runnable = this.mAnimTask) != null) {
            this.mInputLayout.removeCallbacks(runnable);
            this.mAnimTask = null;
        }
        if (this.mContext.getCurrentFocus() != null) {
            ((InputMethodManager)this.mContext.getSystemService("input_method")).hideSoftInputFromWindow(this.mContext.getCurrentFocus().getWindowToken(), 0);
        }
        this.mInputLayout.setVisibility(8);
        this.mIsShowing = false;
    }

    public boolean IsShowing() {
        return this.mIsShowing;
    }

    public void ShowInputLayout() {
        this.mIsShowing = true;
        this.mInputLayout.setVisibility(4);
        this.mInputEt.requestFocus();
        ((InputMethodManager)this.mContext.getSystemService("input_method")).showSoftInput((View)this.mInputEt, 1);
        Object object = this.mAnimTask;
        if (object != null) {
            this.mInputLayout.removeCallbacks((Runnable)object);
            this.mAnimTask = null;
        }
        object = new Runnable(this){
            final InputManager this$0;
            {
                this.this$0 = inputManager;
            }

            @Override
            public void run() {
                this.this$0.mInputLayout.setVisibility(0);
                this.this$0.mInputEt.requestFocus();
            }
        };
        this.mAnimTask = object;
        this.mInputLayout.postDelayed((Runnable)object, 60L);
        this.mCurrentHistoryMessage = 0;
        object = this.mSavedInput;
        if (object != null) {
            this.mInputEt.setText((CharSequence)object);
            object = this.mInputEt;
            object.setSelection(object.getText().length());
        }
    }

    public void onHeightChanged(int n) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mInputLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, n);
        this.mInputLayout.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public static interface InputListener {
        public void OnInputEnd(String var1);
    }
}

