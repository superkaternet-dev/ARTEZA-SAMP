/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.database.DataSetObserver
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$AccessibilityDelegate
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.View$OnLongClickListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.BaseAdapter
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.ListAdapter
 *  android.widget.PopupWindow$OnDismissListener
 *  android.widget.TextView
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.widget.ActivityChooserModel;
import androidx.appcompat.widget.ForwardingListener;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ActionProvider;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

public class ActivityChooserView
extends ViewGroup
implements ActivityChooserModel.ActivityChooserModelClient {
    private final View mActivityChooserContent;
    private final Drawable mActivityChooserContentBackground;
    final ActivityChooserViewAdapter mAdapter;
    private final Callbacks mCallbacks;
    private int mDefaultActionButtonContentDescription;
    final FrameLayout mDefaultActivityButton;
    private final ImageView mDefaultActivityButtonImage;
    final FrameLayout mExpandActivityOverflowButton;
    private final ImageView mExpandActivityOverflowButtonImage;
    int mInitialActivityCount = 4;
    private boolean mIsAttachedToWindow;
    boolean mIsSelectingDefaultActivity;
    private final int mListPopupMaxWidth;
    private ListPopupWindow mListPopupWindow;
    final DataSetObserver mModelDataSetObserver = new DataSetObserver(this){
        final ActivityChooserView this$0;
        {
            this.this$0 = activityChooserView;
        }

        public void onChanged() {
            super.onChanged();
            this.this$0.mAdapter.notifyDataSetChanged();
        }

        public void onInvalidated() {
            super.onInvalidated();
            this.this$0.mAdapter.notifyDataSetInvalidated();
        }
    };
    PopupWindow.OnDismissListener mOnDismissListener;
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(this){
        final ActivityChooserView this$0;
        {
            this.this$0 = activityChooserView;
        }

        public void onGlobalLayout() {
            if (this.this$0.isShowingPopup()) {
                if (!this.this$0.isShown()) {
                    this.this$0.getListPopupWindow().dismiss();
                } else {
                    this.this$0.getListPopupWindow().show();
                    if (this.this$0.mProvider != null) {
                        this.this$0.mProvider.subUiVisibilityChanged(true);
                    }
                }
            }
        }
    };
    ActionProvider mProvider;

    public ActivityChooserView(Context context) {
        this(context, null);
    }

    public ActivityChooserView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActivityChooserView(Context context, AttributeSet object, int n) {
        super(context, object, n);
        View view;
        Object object2 = context.obtainStyledAttributes(object, R.styleable.ActivityChooserView, n, 0);
        ViewCompat.saveAttributeDataForStyleable((View)this, context, R.styleable.ActivityChooserView, object, object2, n, 0);
        this.mInitialActivityCount = object2.getInt(R.styleable.ActivityChooserView_initialActivityCount, 4);
        object = object2.getDrawable(R.styleable.ActivityChooserView_expandActivityOverflowButtonDrawable);
        object2.recycle();
        LayoutInflater.from((Context)this.getContext()).inflate(R.layout.abc_activity_chooser_view, (ViewGroup)this, true);
        object2 = new Callbacks(this);
        this.mCallbacks = object2;
        this.mActivityChooserContent = view = this.findViewById(R.id.activity_chooser_view_content);
        this.mActivityChooserContentBackground = view.getBackground();
        view = (FrameLayout)this.findViewById(R.id.default_activity_button);
        this.mDefaultActivityButton = view;
        view.setOnClickListener((View.OnClickListener)object2);
        view.setOnLongClickListener((View.OnLongClickListener)object2);
        this.mDefaultActivityButtonImage = (ImageView)view.findViewById(R.id.image);
        view = (FrameLayout)this.findViewById(R.id.expand_activities_button);
        view.setOnClickListener((View.OnClickListener)object2);
        view.setAccessibilityDelegate(new View.AccessibilityDelegate(this){
            final ActivityChooserView this$0;
            {
                this.this$0 = activityChooserView;
            }

            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCanOpenPopup(true);
            }
        });
        view.setOnTouchListener((View.OnTouchListener)new ForwardingListener(this, view){
            final ActivityChooserView this$0;
            {
                this.this$0 = activityChooserView;
                super(view);
            }

            @Override
            public ShowableListMenu getPopup() {
                return this.this$0.getListPopupWindow();
            }

            @Override
            protected boolean onForwardingStarted() {
                this.this$0.showPopup();
                return true;
            }

            @Override
            protected boolean onForwardingStopped() {
                this.this$0.dismissPopup();
                return true;
            }
        });
        this.mExpandActivityOverflowButton = view;
        object2 = (ImageView)view.findViewById(R.id.image);
        this.mExpandActivityOverflowButtonImage = object2;
        object2.setImageDrawable((Drawable)object);
        object = new ActivityChooserViewAdapter(this);
        this.mAdapter = object;
        object.registerDataSetObserver(new DataSetObserver(this){
            final ActivityChooserView this$0;
            {
                this.this$0 = activityChooserView;
            }

            public void onChanged() {
                super.onChanged();
                this.this$0.updateAppearance();
            }
        });
        context = context.getResources();
        this.mListPopupMaxWidth = Math.max(context.getDisplayMetrics().widthPixels / 2, context.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
    }

    public boolean dismissPopup() {
        if (this.isShowingPopup()) {
            this.getListPopupWindow().dismiss();
            ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
            }
        }
        return true;
    }

    public ActivityChooserModel getDataModel() {
        return this.mAdapter.getDataModel();
    }

    ListPopupWindow getListPopupWindow() {
        if (this.mListPopupWindow == null) {
            ListPopupWindow listPopupWindow;
            this.mListPopupWindow = listPopupWindow = new ListPopupWindow(this.getContext());
            listPopupWindow.setAdapter((ListAdapter)this.mAdapter);
            this.mListPopupWindow.setAnchorView((View)this);
            this.mListPopupWindow.setModal(true);
            this.mListPopupWindow.setOnItemClickListener(this.mCallbacks);
            this.mListPopupWindow.setOnDismissListener(this.mCallbacks);
        }
        return this.mListPopupWindow;
    }

    public boolean isShowingPopup() {
        return this.getListPopupWindow().isShowing();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ActivityChooserModel activityChooserModel = this.mAdapter.getDataModel();
        if (activityChooserModel != null) {
            activityChooserModel.registerObserver(this.mModelDataSetObserver);
        }
        this.mIsAttachedToWindow = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActivityChooserModel activityChooserModel = this.mAdapter.getDataModel();
        if (activityChooserModel != null) {
            activityChooserModel.unregisterObserver(this.mModelDataSetObserver);
        }
        if ((activityChooserModel = this.getViewTreeObserver()).isAlive()) {
            activityChooserModel.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
        }
        if (this.isShowingPopup()) {
            this.dismissPopup();
        }
        this.mIsAttachedToWindow = false;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        this.mActivityChooserContent.layout(0, 0, n3 - n, n4 - n2);
        if (!this.isShowingPopup()) {
            this.dismissPopup();
        }
    }

    protected void onMeasure(int n, int n2) {
        View view = this.mActivityChooserContent;
        int n3 = n2;
        if (this.mDefaultActivityButton.getVisibility() != 0) {
            n3 = View.MeasureSpec.makeMeasureSpec((int)View.MeasureSpec.getSize((int)n2), (int)0x40000000);
        }
        this.measureChild(view, n, n3);
        this.setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @Override
    public void setActivityChooserModel(ActivityChooserModel activityChooserModel) {
        this.mAdapter.setDataModel(activityChooserModel);
        if (this.isShowingPopup()) {
            this.dismissPopup();
            this.showPopup();
        }
    }

    public void setDefaultActionButtonContentDescription(int n) {
        this.mDefaultActionButtonContentDescription = n;
    }

    public void setExpandActivityOverflowButtonContentDescription(int n) {
        String string2 = this.getContext().getString(n);
        this.mExpandActivityOverflowButtonImage.setContentDescription((CharSequence)string2);
    }

    public void setExpandActivityOverflowButtonDrawable(Drawable drawable2) {
        this.mExpandActivityOverflowButtonImage.setImageDrawable(drawable2);
    }

    public void setInitialActivityCount(int n) {
        this.mInitialActivityCount = n;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setProvider(ActionProvider actionProvider) {
        this.mProvider = actionProvider;
    }

    public boolean showPopup() {
        if (!this.isShowingPopup() && this.mIsAttachedToWindow) {
            this.mIsSelectingDefaultActivity = false;
            this.showPopupUnchecked(this.mInitialActivityCount);
            return true;
        }
        return false;
    }

    void showPopupUnchecked(int n) {
        if (this.mAdapter.getDataModel() != null) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
            boolean bl = this.mDefaultActivityButton.getVisibility() == 0;
            int n2 = this.mAdapter.getActivityCount();
            int n3 = bl ? 1 : 0;
            if (n != Integer.MAX_VALUE && n2 > n + n3) {
                this.mAdapter.setShowFooterView(true);
                this.mAdapter.setMaxActivityCount(n - 1);
            } else {
                this.mAdapter.setShowFooterView(false);
                this.mAdapter.setMaxActivityCount(n);
            }
            ListPopupWindow listPopupWindow = this.getListPopupWindow();
            if (!listPopupWindow.isShowing()) {
                if (!this.mIsSelectingDefaultActivity && bl) {
                    this.mAdapter.setShowDefaultActivity(false, false);
                } else {
                    this.mAdapter.setShowDefaultActivity(true, bl);
                }
                listPopupWindow.setContentWidth(Math.min(this.mAdapter.measureContentWidth(), this.mListPopupMaxWidth));
                listPopupWindow.show();
                ActionProvider actionProvider = this.mProvider;
                if (actionProvider != null) {
                    actionProvider.subUiVisibilityChanged(true);
                }
                listPopupWindow.getListView().setContentDescription((CharSequence)this.getContext().getString(R.string.abc_activitychooserview_choose_application));
                listPopupWindow.getListView().setSelector((Drawable)new ColorDrawable(0));
            }
            return;
        }
        throw new IllegalStateException("No data model. Did you call #setDataModel?");
    }

    void updateAppearance() {
        if (this.mAdapter.getCount() > 0) {
            this.mExpandActivityOverflowButton.setEnabled(true);
        } else {
            this.mExpandActivityOverflowButton.setEnabled(false);
        }
        int n = this.mAdapter.getActivityCount();
        int n2 = this.mAdapter.getHistorySize();
        if (n != 1 && (n <= 1 || n2 <= 0)) {
            this.mDefaultActivityButton.setVisibility(8);
        } else {
            this.mDefaultActivityButton.setVisibility(0);
            ResolveInfo resolveInfo = this.mAdapter.getDefaultActivity();
            Object object = this.getContext().getPackageManager();
            this.mDefaultActivityButtonImage.setImageDrawable(resolveInfo.loadIcon(object));
            if (this.mDefaultActionButtonContentDescription != 0) {
                object = resolveInfo.loadLabel(object);
                object = this.getContext().getString(this.mDefaultActionButtonContentDescription, new Object[]{object});
                this.mDefaultActivityButton.setContentDescription((CharSequence)object);
            }
        }
        if (this.mDefaultActivityButton.getVisibility() == 0) {
            this.mActivityChooserContent.setBackgroundDrawable(this.mActivityChooserContentBackground);
        } else {
            this.mActivityChooserContent.setBackgroundDrawable(null);
        }
    }

    private class ActivityChooserViewAdapter
    extends BaseAdapter {
        private static final int ITEM_VIEW_TYPE_ACTIVITY = 0;
        private static final int ITEM_VIEW_TYPE_COUNT = 3;
        private static final int ITEM_VIEW_TYPE_FOOTER = 1;
        public static final int MAX_ACTIVITY_COUNT_DEFAULT = 4;
        public static final int MAX_ACTIVITY_COUNT_UNLIMITED = Integer.MAX_VALUE;
        private ActivityChooserModel mDataModel;
        private boolean mHighlightDefaultActivity;
        private int mMaxActivityCount;
        private boolean mShowDefaultActivity;
        private boolean mShowFooterView;
        final ActivityChooserView this$0;

        ActivityChooserViewAdapter(ActivityChooserView activityChooserView) {
            this.this$0 = activityChooserView;
            this.mMaxActivityCount = 4;
        }

        public int getActivityCount() {
            return this.mDataModel.getActivityCount();
        }

        public int getCount() {
            int n;
            int n2 = n = this.mDataModel.getActivityCount();
            if (!this.mShowDefaultActivity) {
                n2 = n;
                if (this.mDataModel.getDefaultActivity() != null) {
                    n2 = n - 1;
                }
            }
            n2 = n = Math.min(n2, this.mMaxActivityCount);
            if (this.mShowFooterView) {
                n2 = n + 1;
            }
            return n2;
        }

        public ActivityChooserModel getDataModel() {
            return this.mDataModel;
        }

        public ResolveInfo getDefaultActivity() {
            return this.mDataModel.getDefaultActivity();
        }

        public int getHistorySize() {
            return this.mDataModel.getHistorySize();
        }

        public Object getItem(int n) {
            switch (this.getItemViewType(n)) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 1: {
                    return null;
                }
                case 0: 
            }
            int n2 = n;
            if (!this.mShowDefaultActivity) {
                n2 = n;
                if (this.mDataModel.getDefaultActivity() != null) {
                    n2 = n + 1;
                }
            }
            return this.mDataModel.getActivity(n2);
        }

        public long getItemId(int n) {
            return n;
        }

        public int getItemViewType(int n) {
            if (this.mShowFooterView && n == this.getCount() - 1) {
                return 1;
            }
            return 0;
        }

        public boolean getShowDefaultActivity() {
            return this.mShowDefaultActivity;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public View getView(int n, View view, ViewGroup viewGroup) {
            View view2;
            block11: {
                block10: {
                    switch (this.getItemViewType(n)) {
                        default: {
                            throw new IllegalArgumentException();
                        }
                        case 1: {
                            View view3;
                            if (view != null) {
                                view3 = view;
                                if (view.getId() == 1) return view3;
                            }
                            view3 = LayoutInflater.from((Context)this.this$0.getContext()).inflate(R.layout.abc_activity_chooser_view_list_item, viewGroup, false);
                            view3.setId(1);
                            ((TextView)view3.findViewById(R.id.title)).setText((CharSequence)this.this$0.getContext().getString(R.string.abc_activity_chooser_view_see_all));
                            return view3;
                        }
                        case 0: 
                    }
                    if (view == null) break block10;
                    view2 = view;
                    if (view.getId() == R.id.list_item) break block11;
                }
                view2 = LayoutInflater.from((Context)this.this$0.getContext()).inflate(R.layout.abc_activity_chooser_view_list_item, viewGroup, false);
            }
            view = this.this$0.getContext().getPackageManager();
            viewGroup = (ImageView)view2.findViewById(R.id.icon);
            ResolveInfo resolveInfo = (ResolveInfo)this.getItem(n);
            viewGroup.setImageDrawable(resolveInfo.loadIcon((PackageManager)view));
            ((TextView)view2.findViewById(R.id.title)).setText(resolveInfo.loadLabel((PackageManager)view));
            if (this.mShowDefaultActivity && n == 0 && this.mHighlightDefaultActivity) {
                view2.setActivated(true);
                return view2;
            } else {
                view2.setActivated(false);
            }
            return view2;
        }

        public int getViewTypeCount() {
            return 3;
        }

        public int measureContentWidth() {
            int n = this.mMaxActivityCount;
            this.mMaxActivityCount = Integer.MAX_VALUE;
            int n2 = 0;
            View view = null;
            int n3 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
            int n4 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
            int n5 = this.getCount();
            for (int i = 0; i < n5; ++i) {
                view = this.getView(i, view, null);
                view.measure(n3, n4);
                n2 = Math.max(n2, view.getMeasuredWidth());
            }
            this.mMaxActivityCount = n;
            return n2;
        }

        public void setDataModel(ActivityChooserModel activityChooserModel) {
            ActivityChooserModel activityChooserModel2 = this.this$0.mAdapter.getDataModel();
            if (activityChooserModel2 != null && this.this$0.isShown()) {
                activityChooserModel2.unregisterObserver(this.this$0.mModelDataSetObserver);
            }
            this.mDataModel = activityChooserModel;
            if (activityChooserModel != null && this.this$0.isShown()) {
                activityChooserModel.registerObserver(this.this$0.mModelDataSetObserver);
            }
            this.notifyDataSetChanged();
        }

        public void setMaxActivityCount(int n) {
            if (this.mMaxActivityCount != n) {
                this.mMaxActivityCount = n;
                this.notifyDataSetChanged();
            }
        }

        public void setShowDefaultActivity(boolean bl, boolean bl2) {
            if (this.mShowDefaultActivity != bl || this.mHighlightDefaultActivity != bl2) {
                this.mShowDefaultActivity = bl;
                this.mHighlightDefaultActivity = bl2;
                this.notifyDataSetChanged();
            }
        }

        public void setShowFooterView(boolean bl) {
            if (this.mShowFooterView != bl) {
                this.mShowFooterView = bl;
                this.notifyDataSetChanged();
            }
        }
    }

    private class Callbacks
    implements AdapterView.OnItemClickListener,
    View.OnClickListener,
    View.OnLongClickListener,
    PopupWindow.OnDismissListener {
        final ActivityChooserView this$0;

        Callbacks(ActivityChooserView activityChooserView) {
            this.this$0 = activityChooserView;
        }

        private void notifyOnDismissListener() {
            if (this.this$0.mOnDismissListener != null) {
                this.this$0.mOnDismissListener.onDismiss();
            }
        }

        public void onClick(View object) {
            block7: {
                block6: {
                    block5: {
                        if (object != this.this$0.mDefaultActivityButton) break block5;
                        this.this$0.dismissPopup();
                        object = this.this$0.mAdapter.getDefaultActivity();
                        int n = this.this$0.mAdapter.getDataModel().getActivityIndex((ResolveInfo)object);
                        object = this.this$0.mAdapter.getDataModel().chooseActivity(n);
                        if (object != null) {
                            object.addFlags(524288);
                            this.this$0.getContext().startActivity((Intent)object);
                        }
                        break block6;
                    }
                    if (object != this.this$0.mExpandActivityOverflowButton) break block7;
                    this.this$0.mIsSelectingDefaultActivity = false;
                    object = this.this$0;
                    ((ActivityChooserView)object).showPopupUnchecked(((ActivityChooserView)object).mInitialActivityCount);
                }
                return;
            }
            throw new IllegalArgumentException();
        }

        public void onDismiss() {
            this.notifyOnDismissListener();
            if (this.this$0.mProvider != null) {
                this.this$0.mProvider.subUiVisibilityChanged(false);
            }
        }

        public void onItemClick(AdapterView<?> intent, View view, int n, long l) {
            switch (((ActivityChooserViewAdapter)intent.getAdapter()).getItemViewType(n)) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 1: {
                    this.this$0.showPopupUnchecked(Integer.MAX_VALUE);
                    break;
                }
                case 0: {
                    this.this$0.dismissPopup();
                    if (this.this$0.mIsSelectingDefaultActivity) {
                        if (n <= 0) break;
                        this.this$0.mAdapter.getDataModel().setDefaultActivity(n);
                        break;
                    }
                    if (!this.this$0.mAdapter.getShowDefaultActivity()) {
                        ++n;
                    }
                    intent = this.this$0.mAdapter.getDataModel().chooseActivity(n);
                    if (intent == null) break;
                    intent.addFlags(524288);
                    this.this$0.getContext().startActivity(intent);
                }
            }
        }

        public boolean onLongClick(View object) {
            if (object == this.this$0.mDefaultActivityButton) {
                if (this.this$0.mAdapter.getCount() > 0) {
                    this.this$0.mIsSelectingDefaultActivity = true;
                    object = this.this$0;
                    ((ActivityChooserView)object).showPopupUnchecked(((ActivityChooserView)object).mInitialActivityCount);
                }
                return true;
            }
            throw new IllegalArgumentException();
        }
    }

    public static class InnerLayout
    extends LinearLayout {
        private static final int[] TINT_ATTRS = new int[]{16842964};

        public InnerLayout(Context object, AttributeSet attributeSet) {
            super((Context)object, attributeSet);
            object = TintTypedArray.obtainStyledAttributes((Context)object, attributeSet, TINT_ATTRS);
            this.setBackgroundDrawable(((TintTypedArray)object).getDrawable(0));
            ((TintTypedArray)object).recycle();
        }
    }
}

