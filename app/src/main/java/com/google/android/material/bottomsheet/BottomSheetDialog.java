/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.res.TypedArray
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.TypedValue
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout
 */
package com.google.android.material.bottomsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BottomSheetDialog
extends AppCompatDialog {
    private BottomSheetBehavior<FrameLayout> behavior;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback(this){
        final BottomSheetDialog this$0;
        {
            this.this$0 = bottomSheetDialog;
        }

        @Override
        public void onSlide(View view, float f) {
        }

        @Override
        public void onStateChanged(View view, int n) {
            if (n == 5) {
                this.this$0.cancel();
            }
        }
    };
    boolean cancelable = true;
    private boolean canceledOnTouchOutside = true;
    private boolean canceledOnTouchOutsideSet;

    public BottomSheetDialog(Context context) {
        this(context, 0);
    }

    public BottomSheetDialog(Context context, int n) {
        super(context, BottomSheetDialog.getThemeResId(context, n));
        this.supportRequestWindowFeature(1);
    }

    protected BottomSheetDialog(Context context, boolean bl, DialogInterface.OnCancelListener onCancelListener) {
        super(context, bl, onCancelListener);
        this.supportRequestWindowFeature(1);
        this.cancelable = bl;
    }

    private static int getThemeResId(Context context, int n) {
        int n2 = n;
        if (n == 0) {
            TypedValue typedValue = new TypedValue();
            n2 = context.getTheme().resolveAttribute(R.attr.bottomSheetDialogTheme, typedValue, true) ? typedValue.resourceId : R.style.Theme_Design_Light_BottomSheetDialog;
        }
        return n2;
    }

    private View wrapInBottomSheet(int n, View object, ViewGroup.LayoutParams layoutParams) {
        FrameLayout frameLayout = (FrameLayout)View.inflate((Context)this.getContext(), (int)R.layout.design_bottom_sheet_dialog, null);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)frameLayout.findViewById(R.id.coordinator);
        View view = object;
        if (n != 0) {
            view = object;
            if (object == null) {
                view = this.getLayoutInflater().inflate(n, (ViewGroup)coordinatorLayout, false);
            }
        }
        FrameLayout frameLayout2 = (FrameLayout)coordinatorLayout.findViewById(R.id.design_bottom_sheet);
        this.behavior = object = BottomSheetBehavior.from(frameLayout2);
        ((BottomSheetBehavior)object).setBottomSheetCallback(this.bottomSheetCallback);
        this.behavior.setHideable(this.cancelable);
        if (layoutParams == null) {
            frameLayout2.addView(view);
        } else {
            frameLayout2.addView(view, layoutParams);
        }
        coordinatorLayout.findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener(this){
            final BottomSheetDialog this$0;
            {
                this.this$0 = bottomSheetDialog;
            }

            public void onClick(View view) {
                if (this.this$0.cancelable && this.this$0.isShowing() && this.this$0.shouldWindowCloseOnTouchOutside()) {
                    this.this$0.cancel();
                }
            }
        });
        ViewCompat.setAccessibilityDelegate((View)frameLayout2, new AccessibilityDelegateCompat(this){
            final BottomSheetDialog this$0;
            {
                this.this$0 = bottomSheetDialog;
            }

            @Override
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                if (this.this$0.cancelable) {
                    accessibilityNodeInfoCompat.addAction(0x100000);
                    accessibilityNodeInfoCompat.setDismissable(true);
                } else {
                    accessibilityNodeInfoCompat.setDismissable(false);
                }
            }

            @Override
            public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
                if (n == 0x100000 && this.this$0.cancelable) {
                    this.this$0.cancel();
                    return true;
                }
                return super.performAccessibilityAction(view, n, bundle);
            }
        });
        frameLayout2.setOnTouchListener(new View.OnTouchListener(this){
            final BottomSheetDialog this$0;
            {
                this.this$0 = bottomSheetDialog;
            }

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return frameLayout;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        bundle = this.getWindow();
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                bundle.clearFlags(0x4000000);
                bundle.addFlags(Integer.MIN_VALUE);
            }
            bundle.setLayout(-1, -1);
        }
    }

    protected void onStart() {
        super.onStart();
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.behavior;
        if (bottomSheetBehavior != null && bottomSheetBehavior.getState() == 5) {
            this.behavior.setState(4);
        }
    }

    public void setCancelable(boolean bl) {
        super.setCancelable(bl);
        if (this.cancelable != bl) {
            this.cancelable = bl;
            BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.behavior;
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setHideable(bl);
            }
        }
    }

    public void setCanceledOnTouchOutside(boolean bl) {
        super.setCanceledOnTouchOutside(bl);
        if (bl && !this.cancelable) {
            this.cancelable = true;
        }
        this.canceledOnTouchOutside = bl;
        this.canceledOnTouchOutsideSet = true;
    }

    @Override
    public void setContentView(int n) {
        super.setContentView(this.wrapInBottomSheet(n, null, null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(this.wrapInBottomSheet(0, view, null));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        super.setContentView(this.wrapInBottomSheet(0, view, layoutParams));
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!this.canceledOnTouchOutsideSet) {
            TypedArray typedArray = this.getContext().obtainStyledAttributes(new int[]{16843611});
            this.canceledOnTouchOutside = typedArray.getBoolean(0, true);
            typedArray.recycle();
            this.canceledOnTouchOutsideSet = true;
        }
        return this.canceledOnTouchOutside;
    }
}

