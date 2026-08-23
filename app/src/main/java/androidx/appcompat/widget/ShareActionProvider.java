/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.os.Build$VERSION
 *  android.util.TypedValue
 *  android.view.MenuItem
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.SubMenu
 *  android.view.View
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.ActivityChooserModel;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.view.ActionProvider;

public class ShareActionProvider
extends ActionProvider {
    private static final int DEFAULT_INITIAL_ACTIVITY_COUNT = 4;
    public static final String DEFAULT_SHARE_HISTORY_FILE_NAME = "share_history.xml";
    final Context mContext;
    private int mMaxShownActivityCount = 4;
    private ActivityChooserModel.OnChooseActivityListener mOnChooseActivityListener;
    private final ShareMenuItemOnMenuItemClickListener mOnMenuItemClickListener = new ShareMenuItemOnMenuItemClickListener(this);
    OnShareTargetSelectedListener mOnShareTargetSelectedListener;
    String mShareHistoryFileName = "share_history.xml";

    public ShareActionProvider(Context context) {
        super(context);
        this.mContext = context;
    }

    private void setActivityChooserPolicyIfNeeded() {
        if (this.mOnShareTargetSelectedListener == null) {
            return;
        }
        if (this.mOnChooseActivityListener == null) {
            this.mOnChooseActivityListener = new ShareActivityChooserModelPolicy(this);
        }
        ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName).setOnChooseActivityListener(this.mOnChooseActivityListener);
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @Override
    public View onCreateActionView() {
        ActivityChooserView activityChooserView = new ActivityChooserView(this.mContext);
        if (!activityChooserView.isInEditMode()) {
            activityChooserView.setActivityChooserModel(ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName));
        }
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(R.attr.actionModeShareDrawable, typedValue, true);
        activityChooserView.setExpandActivityOverflowButtonDrawable(AppCompatResources.getDrawable(this.mContext, typedValue.resourceId));
        activityChooserView.setProvider(this);
        activityChooserView.setDefaultActionButtonContentDescription(R.string.abc_shareactionprovider_share_with_application);
        activityChooserView.setExpandActivityOverflowButtonContentDescription(R.string.abc_shareactionprovider_share_with);
        return activityChooserView;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        ResolveInfo resolveInfo;
        int n;
        subMenu.clear();
        ActivityChooserModel activityChooserModel = ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName);
        PackageManager packageManager = this.mContext.getPackageManager();
        int n2 = activityChooserModel.getActivityCount();
        int n3 = Math.min(n2, this.mMaxShownActivityCount);
        for (n = 0; n < n3; ++n) {
            resolveInfo = activityChooserModel.getActivity(n);
            subMenu.add(0, n, n, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener)this.mOnMenuItemClickListener);
        }
        if (n3 < n2) {
            resolveInfo = subMenu.addSubMenu(0, n3, n3, (CharSequence)this.mContext.getString(R.string.abc_activity_chooser_view_see_all));
            for (n = 0; n < n2; ++n) {
                subMenu = activityChooserModel.getActivity(n);
                resolveInfo.add(0, n, n, subMenu.loadLabel(packageManager)).setIcon(subMenu.loadIcon(packageManager)).setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener)this.mOnMenuItemClickListener);
            }
        }
    }

    public void setOnShareTargetSelectedListener(OnShareTargetSelectedListener onShareTargetSelectedListener) {
        this.mOnShareTargetSelectedListener = onShareTargetSelectedListener;
        this.setActivityChooserPolicyIfNeeded();
    }

    public void setShareHistoryFileName(String string2) {
        this.mShareHistoryFileName = string2;
        this.setActivityChooserPolicyIfNeeded();
    }

    public void setShareIntent(Intent intent) {
        String string2;
        if (intent != null && ("android.intent.action.SEND".equals(string2 = intent.getAction()) || "android.intent.action.SEND_MULTIPLE".equals(string2))) {
            this.updateIntent(intent);
        }
        ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName).setIntent(intent);
    }

    void updateIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= 21) {
            intent.addFlags(0x8080000);
        } else {
            intent.addFlags(524288);
        }
    }

    public static interface OnShareTargetSelectedListener {
        public boolean onShareTargetSelected(ShareActionProvider var1, Intent var2);
    }

    private class ShareActivityChooserModelPolicy
    implements ActivityChooserModel.OnChooseActivityListener {
        final ShareActionProvider this$0;

        ShareActivityChooserModelPolicy(ShareActionProvider shareActionProvider) {
            this.this$0 = shareActionProvider;
        }

        @Override
        public boolean onChooseActivity(ActivityChooserModel activityChooserModel, Intent intent) {
            if (this.this$0.mOnShareTargetSelectedListener != null) {
                this.this$0.mOnShareTargetSelectedListener.onShareTargetSelected(this.this$0, intent);
            }
            return false;
        }
    }

    private class ShareMenuItemOnMenuItemClickListener
    implements MenuItem.OnMenuItemClickListener {
        final ShareActionProvider this$0;

        ShareMenuItemOnMenuItemClickListener(ShareActionProvider shareActionProvider) {
            this.this$0 = shareActionProvider;
        }

        public boolean onMenuItemClick(MenuItem object) {
            Intent intent = ActivityChooserModel.get(this.this$0.mContext, this.this$0.mShareHistoryFileName).chooseActivity(object.getItemId());
            if (intent != null) {
                object = intent.getAction();
                if ("android.intent.action.SEND".equals(object) || "android.intent.action.SEND_MULTIPLE".equals(object)) {
                    this.this$0.updateIntent(intent);
                }
                this.this$0.mContext.startActivity(intent);
            }
            return true;
        }
    }
}

