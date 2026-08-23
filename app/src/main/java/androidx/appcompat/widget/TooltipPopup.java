/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup$LayoutParams
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.widget.TextView
 */
package androidx.appcompat.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.R;

class TooltipPopup {
    private static final String TAG = "TooltipPopup";
    private final View mContentView;
    private final Context mContext;
    private final WindowManager.LayoutParams mLayoutParams;
    private final TextView mMessageView;
    private final int[] mTmpAnchorPos;
    private final int[] mTmpAppPos;
    private final Rect mTmpDisplayFrame;

    TooltipPopup(Context context) {
        View view;
        WindowManager.LayoutParams layoutParams;
        this.mLayoutParams = layoutParams = new WindowManager.LayoutParams();
        this.mTmpDisplayFrame = new Rect();
        this.mTmpAnchorPos = new int[2];
        this.mTmpAppPos = new int[2];
        this.mContext = context;
        this.mContentView = view = LayoutInflater.from((Context)context).inflate(R.layout.abc_tooltip, null);
        this.mMessageView = (TextView)view.findViewById(R.id.message);
        layoutParams.setTitle((CharSequence)this.getClass().getSimpleName());
        layoutParams.packageName = context.getPackageName();
        layoutParams.type = 1002;
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.format = -3;
        layoutParams.windowAnimations = R.style.Animation_AppCompat_Tooltip;
        layoutParams.flags = 24;
    }

    private void computePosition(View object, int n, int object2, boolean bl, WindowManager.LayoutParams layoutParams) {
        Object object3;
        Object object4;
        layoutParams.token = object.getApplicationWindowToken();
        Object object5 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.tooltip_precise_anchor_threshold);
        if (object.getWidth() < object5) {
            n = object.getWidth() / 2;
        }
        if (object.getHeight() >= object5) {
            object5 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.tooltip_precise_anchor_extra_offset);
            object4 = object2 + object5;
            object5 = object2 - object5;
            object2 = object4;
        } else {
            object2 = object.getHeight();
            object5 = 0;
        }
        layoutParams.gravity = 49;
        Resources resources = this.mContext.getResources();
        object4 = bl ? R.dimen.tooltip_y_offset_touch : R.dimen.tooltip_y_offset_non_touch;
        int n2 = resources.getDimensionPixelOffset(object4);
        resources = TooltipPopup.getAppRootView(object);
        if (resources == null) {
            Log.e((String)TAG, (String)"Cannot find app view");
            return;
        }
        resources.getWindowVisibleDisplayFrame(this.mTmpDisplayFrame);
        if (this.mTmpDisplayFrame.left < 0 && this.mTmpDisplayFrame.top < 0) {
            object3 = this.mContext.getResources();
            object4 = object3.getIdentifier("status_bar_height", "dimen", "android");
            object4 = object4 != 0 ? object3.getDimensionPixelSize(object4) : 0;
            object3 = object3.getDisplayMetrics();
            this.mTmpDisplayFrame.set(0, object4, object3.widthPixels, object3.heightPixels);
        }
        resources.getLocationOnScreen(this.mTmpAppPos);
        object.getLocationOnScreen(this.mTmpAnchorPos);
        object3 = this.mTmpAnchorPos;
        object4 = object3[0];
        object = this.mTmpAppPos;
        object3[0] = (Resources)(object4 - object[0]);
        object3[1] = object3[1] - object[1];
        layoutParams.x = (int)(object3[0] + n - resources.getWidth() / 2);
        n = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        this.mContentView.measure(n, n);
        n = this.mContentView.getMeasuredHeight();
        object = this.mTmpAnchorPos;
        object5 = object[1] + object5 - n2 - n;
        object2 = object[1] + object2 + n2;
        layoutParams.y = bl ? (object5 >= 0 ? object5 : object2) : (object2 + n <= this.mTmpDisplayFrame.height() ? object2 : object5);
    }

    private static View getAppRootView(View view) {
        View view2 = view.getRootView();
        ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
        if (layoutParams instanceof WindowManager.LayoutParams && ((WindowManager.LayoutParams)layoutParams).type == 2) {
            return view2;
        }
        view = view.getContext();
        while (view instanceof ContextWrapper) {
            if (view instanceof Activity) {
                return ((Activity)view).getWindow().getDecorView();
            }
            view = ((ContextWrapper)view).getBaseContext();
        }
        return view2;
    }

    void hide() {
        if (!this.isShowing()) {
            return;
        }
        ((WindowManager)this.mContext.getSystemService("window")).removeView(this.mContentView);
    }

    boolean isShowing() {
        boolean bl = this.mContentView.getParent() != null;
        return bl;
    }

    void show(View view, int n, int n2, boolean bl, CharSequence charSequence) {
        if (this.isShowing()) {
            this.hide();
        }
        this.mMessageView.setText(charSequence);
        this.computePosition(view, n, n2, bl, this.mLayoutParams);
        ((WindowManager)this.mContext.getSystemService("window")).addView(this.mContentView, (ViewGroup.LayoutParams)this.mLayoutParams);
    }
}

