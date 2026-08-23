/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.res.ColorStateList
 *  android.content.res.Resources$Theme
 *  android.database.DataSetObserver
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.ArrayAdapter
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.PopupWindow$OnDismissListener
 *  android.widget.Spinner
 *  android.widget.SpinnerAdapter
 *  android.widget.ThemedSpinnerAdapter
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import androidx.appcompat.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.widget.AppCompatBackgroundHelper;
import androidx.appcompat.widget.ForwardingListener;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.ThemeUtils;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;

public class AppCompatSpinner
extends Spinner
implements TintableBackgroundView {
    private static final int[] ATTRS_ANDROID_SPINNERMODE = new int[]{16843505};
    private static final int MAX_ITEMS_MEASURED = 15;
    private static final int MODE_DIALOG = 0;
    private static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "AppCompatSpinner";
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    int mDropDownWidth;
    private ForwardingListener mForwardingListener;
    private SpinnerPopup mPopup;
    private final Context mPopupContext;
    private final boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    final Rect mTempRect;

    public AppCompatSpinner(Context context) {
        this(context, null);
    }

    public AppCompatSpinner(Context context, int n) {
        this(context, null, R.attr.spinnerStyle, n);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.spinnerStyle);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, -1);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int n, int n2) {
        this(context, attributeSet, n, n2, null);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public AppCompatSpinner(Context var1_1, AttributeSet var2_3, int var3_4, int var4_5, Resources.Theme var5_6) {
        block15: {
            super(var1_1, var2_3, var3_4);
            this.mTempRect = new Rect();
            ThemeUtils.checkAppCompatTheme((View)this, this.getContext());
            var10_7 = TintTypedArray.obtainStyledAttributes(var1_1, var2_3, R.styleable.Spinner, var3_4, 0);
            this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this);
            this.mPopupContext = var5_6 != null ? new ContextThemeWrapper(var1_1, (Resources.Theme)var5_6) : ((var6_8 = var10_7.getResourceId(R.styleable.Spinner_popupTheme, 0)) != 0 ? new ContextThemeWrapper(var1_1, var6_8) : var1_1);
            var7_9 = var4_5;
            if (var4_5 == -1) {
                var5_6 = null;
                var8_10 = null;
                var9_11 = var1_1.obtainStyledAttributes(var2_3, AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE, var3_4, 0);
                var6_8 = var4_5;
                var8_10 = var9_11;
                var5_6 = var9_11;
                if (var9_11.hasValue(0)) {
                    var8_10 = var9_11;
                    var5_6 = var9_11;
                    var6_8 = var9_11.getInt(0, 0);
                }
                var7_9 = var6_8;
                if (var9_11 == null) break block15;
                var4_5 = var6_8;
                var5_6 = var9_11;
lbl24:
                // 2 sources

                while (true) {
                    var5_6.recycle();
                    var7_9 = var4_5;
                    break block15;
                    break;
                }
                {
                    catch (Throwable var1_2) {
                    }
                    catch (Exception var9_12) {}
                    var8_10 = var5_6;
                    {
                        Log.i((String)"AppCompatSpinner", (String)"Could not read android:spinnerMode", (Throwable)var9_12);
                        var7_9 = var4_5;
                        if (var5_6 != null) {
                            ** continue;
                        }
                        break block15;
                    }
                }
                if (var8_10 != null) {
                    var8_10.recycle();
                }
                throw var1_2;
            }
        }
        switch (var7_9) {
            default: {
                break;
            }
            case 1: {
                var8_10 = new DropdownPopup(this, this.mPopupContext, var2_3, var3_4);
                var5_6 = TintTypedArray.obtainStyledAttributes(this.mPopupContext, var2_3, R.styleable.Spinner, var3_4, 0);
                this.mDropDownWidth = var5_6.getLayoutDimension(R.styleable.Spinner_android_dropDownWidth, -2);
                var8_10.setBackgroundDrawable(var5_6.getDrawable(R.styleable.Spinner_android_popupBackground));
                var8_10.setPromptText(var10_7.getString(R.styleable.Spinner_android_prompt));
                var5_6.recycle();
                this.mPopup = var8_10;
                this.mForwardingListener = new ForwardingListener(this, (View)this, (DropdownPopup)var8_10){
                    final AppCompatSpinner this$0;
                    final DropdownPopup val$popup;
                    {
                        this.this$0 = appCompatSpinner;
                        this.val$popup = dropdownPopup;
                        super(view);
                    }

                    @Override
                    public ShowableListMenu getPopup() {
                        return this.val$popup;
                    }

                    @Override
                    public boolean onForwardingStarted() {
                        if (!this.this$0.getInternalPopup().isShowing()) {
                            this.this$0.showPopup();
                        }
                        return true;
                    }
                };
                break;
            }
            case 0: {
                this.mPopup = var5_6 = new DialogPopup(this);
                var5_6.setPromptText(var10_7.getString(R.styleable.Spinner_android_prompt));
            }
        }
        var5_6 = var10_7.getTextArray(R.styleable.Spinner_android_entries);
        if (var5_6 != null) {
            var1_1 = new ArrayAdapter(var1_1, 17367048, (Object[])var5_6);
            var1_1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            this.setAdapter((SpinnerAdapter)var1_1);
        }
        var10_7.recycle();
        this.mPopupSet = true;
        var1_1 = this.mTempAdapter;
        if (var1_1 != null) {
            this.setAdapter((SpinnerAdapter)var1_1);
            this.mTempAdapter = null;
        }
        this.mBackgroundTintHelper.loadFromAttributes(var2_3, var3_4);
    }

    int compatMeasureContentWidth(SpinnerAdapter spinnerAdapter, Drawable drawable2) {
        if (spinnerAdapter == null) {
            return 0;
        }
        int n = 0;
        View view = null;
        int n2 = 0;
        int n3 = View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)0);
        int n4 = View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredHeight(), (int)0);
        int n5 = Math.max(0, this.getSelectedItemPosition());
        int n6 = Math.min(spinnerAdapter.getCount(), n5 + 15);
        for (n5 = Math.max(0, n5 - (15 - (n6 - n5))); n5 < n6; ++n5) {
            int n7 = spinnerAdapter.getItemViewType(n5);
            int n8 = n2;
            if (n7 != n2) {
                n8 = n7;
                view = null;
            }
            if ((view = spinnerAdapter.getView(n5, view, (ViewGroup)this)).getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            }
            view.measure(n3, n4);
            n = Math.max(n, view.getMeasuredWidth());
            n2 = n8;
        }
        n5 = n;
        if (drawable2 != null) {
            drawable2.getPadding(this.mTempRect);
            n5 = n + (this.mTempRect.left + this.mTempRect.right);
        }
        return n5;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
    }

    public int getDropDownHorizontalOffset() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getHorizontalOffset();
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getDropDownHorizontalOffset();
        }
        return 0;
    }

    public int getDropDownVerticalOffset() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getVerticalOffset();
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getDropDownVerticalOffset();
        }
        return 0;
    }

    public int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getDropDownWidth();
        }
        return 0;
    }

    final SpinnerPopup getInternalPopup() {
        return this.mPopup;
    }

    public Drawable getPopupBackground() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getBackground();
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getPopupBackground();
        }
        return null;
    }

    public Context getPopupContext() {
        return this.mPopupContext;
    }

    public CharSequence getPrompt() {
        Object object = this.mPopup;
        object = object != null ? object.getHintText() : super.getPrompt();
        return object;
    }

    @Override
    public ColorStateList getSupportBackgroundTintList() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        appCompatBackgroundHelper = appCompatBackgroundHelper != null ? appCompatBackgroundHelper.getSupportBackgroundTintList() : null;
        return appCompatBackgroundHelper;
    }

    @Override
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        appCompatBackgroundHelper = appCompatBackgroundHelper != null ? appCompatBackgroundHelper.getSupportBackgroundTintMode() : null;
        return appCompatBackgroundHelper;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null && spinnerPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        if (this.mPopup != null && View.MeasureSpec.getMode((int)n) == Integer.MIN_VALUE) {
            n2 = this.getMeasuredWidth();
            this.setMeasuredDimension(Math.min(Math.max(n2, this.compatMeasureContentWidth(this.getAdapter(), this.getBackground())), View.MeasureSpec.getSize((int)n)), this.getMeasuredHeight());
        }
    }

    public void onRestoreInstanceState(Parcelable object) {
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        if (object.mShowDropdown && (object = this.getViewTreeObserver()) != null) {
            object.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(this){
                final AppCompatSpinner this$0;
                {
                    this.this$0 = appCompatSpinner;
                }

                public void onGlobalLayout() {
                    ViewTreeObserver viewTreeObserver;
                    if (!this.this$0.getInternalPopup().isShowing()) {
                        this.this$0.showPopup();
                    }
                    if ((viewTreeObserver = this.this$0.getViewTreeObserver()) != null) {
                        if (Build.VERSION.SDK_INT >= 16) {
                            viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                        } else {
                            viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                        }
                    }
                }
            });
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SpinnerPopup spinnerPopup = this.mPopup;
        boolean bl = spinnerPopup != null && spinnerPopup.isShowing();
        savedState.mShowDropdown = bl;
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        ForwardingListener forwardingListener = this.mForwardingListener;
        if (forwardingListener != null && forwardingListener.onTouch((View)this, motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean performClick() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            if (!spinnerPopup.isShowing()) {
                this.showPopup();
            }
            return true;
        }
        return super.performClick();
    }

    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        if (!this.mPopupSet) {
            this.mTempAdapter = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        if (this.mPopup != null) {
            Context context;
            Context context2 = context = this.mPopupContext;
            if (context == null) {
                context2 = this.getContext();
            }
            this.mPopup.setAdapter(new DropDownAdapter(spinnerAdapter, context2.getTheme()));
        }
    }

    public void setBackgroundDrawable(Drawable drawable2) {
        super.setBackgroundDrawable(drawable2);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable(drawable2);
        }
    }

    public void setBackgroundResource(int n) {
        super.setBackgroundResource(n);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(n);
        }
    }

    public void setDropDownHorizontalOffset(int n) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setHorizontalOriginalOffset(n);
            this.mPopup.setHorizontalOffset(n);
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setDropDownHorizontalOffset(n);
        }
    }

    public void setDropDownVerticalOffset(int n) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setVerticalOffset(n);
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setDropDownVerticalOffset(n);
        }
    }

    public void setDropDownWidth(int n) {
        if (this.mPopup != null) {
            this.mDropDownWidth = n;
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setDropDownWidth(n);
        }
    }

    public void setPopupBackgroundDrawable(Drawable drawable2) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setBackgroundDrawable(drawable2);
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setPopupBackgroundDrawable(drawable2);
        }
    }

    public void setPopupBackgroundResource(int n) {
        this.setPopupBackgroundDrawable(AppCompatResources.getDrawable(this.getPopupContext(), n));
    }

    public void setPrompt(CharSequence charSequence) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setPromptText(charSequence);
        } else {
            super.setPrompt(charSequence);
        }
    }

    @Override
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintList(colorStateList);
        }
    }

    @Override
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintMode(mode);
        }
    }

    void showPopup() {
        if (Build.VERSION.SDK_INT >= 17) {
            this.mPopup.show(this.getTextDirection(), this.getTextAlignment());
        } else {
            this.mPopup.show(-1, -1);
        }
    }

    class DialogPopup
    implements SpinnerPopup,
    DialogInterface.OnClickListener {
        private ListAdapter mListAdapter;
        AlertDialog mPopup;
        private CharSequence mPrompt;
        final AppCompatSpinner this$0;

        DialogPopup(AppCompatSpinner appCompatSpinner) {
            this.this$0 = appCompatSpinner;
        }

        @Override
        public void dismiss() {
            AlertDialog alertDialog = this.mPopup;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.mPopup = null;
            }
        }

        @Override
        public Drawable getBackground() {
            return null;
        }

        @Override
        public CharSequence getHintText() {
            return this.mPrompt;
        }

        @Override
        public int getHorizontalOffset() {
            return 0;
        }

        @Override
        public int getHorizontalOriginalOffset() {
            return 0;
        }

        @Override
        public int getVerticalOffset() {
            return 0;
        }

        @Override
        public boolean isShowing() {
            AlertDialog alertDialog = this.mPopup;
            boolean bl = alertDialog != null ? alertDialog.isShowing() : false;
            return bl;
        }

        public void onClick(DialogInterface dialogInterface, int n) {
            this.this$0.setSelection(n);
            if (this.this$0.getOnItemClickListener() != null) {
                this.this$0.performItemClick(null, n, this.mListAdapter.getItemId(n));
            }
            this.dismiss();
        }

        @Override
        public void setAdapter(ListAdapter listAdapter) {
            this.mListAdapter = listAdapter;
        }

        @Override
        public void setBackgroundDrawable(Drawable drawable2) {
            Log.e((String)AppCompatSpinner.TAG, (String)"Cannot set popup background for MODE_DIALOG, ignoring");
        }

        @Override
        public void setHorizontalOffset(int n) {
            Log.e((String)AppCompatSpinner.TAG, (String)"Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        @Override
        public void setHorizontalOriginalOffset(int n) {
            Log.e((String)AppCompatSpinner.TAG, (String)"Cannot set horizontal (original) offset for MODE_DIALOG, ignoring");
        }

        @Override
        public void setPromptText(CharSequence charSequence) {
            this.mPrompt = charSequence;
        }

        @Override
        public void setVerticalOffset(int n) {
            Log.e((String)AppCompatSpinner.TAG, (String)"Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        @Override
        public void show(int n, int n2) {
            if (this.mListAdapter == null) {
                return;
            }
            Object object = new AlertDialog.Builder(this.this$0.getPopupContext());
            CharSequence charSequence = this.mPrompt;
            if (charSequence != null) {
                ((AlertDialog.Builder)object).setTitle(charSequence);
            }
            this.mPopup = object = ((AlertDialog.Builder)object).setSingleChoiceItems(this.mListAdapter, this.this$0.getSelectedItemPosition(), (DialogInterface.OnClickListener)this).create();
            object = ((AlertDialog)object).getListView();
            if (Build.VERSION.SDK_INT >= 17) {
                object.setTextDirection(n);
                object.setTextAlignment(n2);
            }
            this.mPopup.show();
        }
    }

    private static class DropDownAdapter
    implements ListAdapter,
    SpinnerAdapter {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        public DropDownAdapter(SpinnerAdapter spinnerAdapter, Resources.Theme theme) {
            this.mAdapter = spinnerAdapter;
            if (spinnerAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter)spinnerAdapter;
            }
            if (theme != null) {
                if (Build.VERSION.SDK_INT >= 23 && spinnerAdapter instanceof android.widget.ThemedSpinnerAdapter) {
                    if ((spinnerAdapter = (android.widget.ThemedSpinnerAdapter)spinnerAdapter).getDropDownViewTheme() != theme) {
                        spinnerAdapter.setDropDownViewTheme(theme);
                    }
                } else if (spinnerAdapter instanceof ThemedSpinnerAdapter && (spinnerAdapter = (ThemedSpinnerAdapter)spinnerAdapter).getDropDownViewTheme() == null) {
                    spinnerAdapter.setDropDownViewTheme(theme);
                }
            }
        }

        public boolean areAllItemsEnabled() {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.areAllItemsEnabled();
            }
            return true;
        }

        public int getCount() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            int n = spinnerAdapter == null ? 0 : spinnerAdapter.getCount();
            return n;
        }

        public View getDropDownView(int n, View object, ViewGroup viewGroup) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            object = spinnerAdapter == null ? null : spinnerAdapter.getDropDownView(n, object, viewGroup);
            return object;
        }

        public Object getItem(int n) {
            Object object = this.mAdapter;
            object = object == null ? null : object.getItem(n);
            return object;
        }

        public long getItemId(int n) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            long l = spinnerAdapter == null ? -1L : spinnerAdapter.getItemId(n);
            return l;
        }

        public int getItemViewType(int n) {
            return 0;
        }

        public View getView(int n, View view, ViewGroup viewGroup) {
            return this.getDropDownView(n, view, viewGroup);
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean hasStableIds() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            boolean bl = spinnerAdapter != null && spinnerAdapter.hasStableIds();
            return bl;
        }

        public boolean isEmpty() {
            boolean bl = this.getCount() == 0;
            return bl;
        }

        public boolean isEnabled(int n) {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.isEnabled(n);
            }
            return true;
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.registerDataSetObserver(dataSetObserver);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }

    class DropdownPopup
    extends ListPopupWindow
    implements SpinnerPopup {
        ListAdapter mAdapter;
        private CharSequence mHintText;
        private int mOriginalHorizontalOffset;
        private final Rect mVisibleRect;
        final AppCompatSpinner this$0;

        public DropdownPopup(AppCompatSpinner appCompatSpinner, Context context, AttributeSet attributeSet, int n) {
            this.this$0 = appCompatSpinner;
            super(context, attributeSet, n);
            this.mVisibleRect = new Rect();
            this.setAnchorView((View)appCompatSpinner);
            this.setModal(true);
            this.setPromptPosition(0);
            this.setOnItemClickListener(new AdapterView.OnItemClickListener(this, appCompatSpinner){
                final DropdownPopup this$1;
                final AppCompatSpinner val$this$0;
                {
                    this.this$1 = dropdownPopup;
                    this.val$this$0 = appCompatSpinner;
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
                    this.this$1.this$0.setSelection(n);
                    if (this.this$1.this$0.getOnItemClickListener() != null) {
                        this.this$1.this$0.performItemClick(view, n, this.this$1.mAdapter.getItemId(n));
                    }
                    this.this$1.dismiss();
                }
            });
        }

        void computeContentWidth() {
            Drawable drawable2 = this.getBackground();
            int n = 0;
            if (drawable2 != null) {
                drawable2.getPadding(this.this$0.mTempRect);
                n = ViewUtils.isLayoutRtl((View)this.this$0) ? this.this$0.mTempRect.right : -this.this$0.mTempRect.left;
            } else {
                drawable2 = this.this$0.mTempRect;
                this.this$0.mTempRect.right = 0;
                drawable2.left = 0;
            }
            int n2 = this.this$0.getPaddingLeft();
            int n3 = this.this$0.getPaddingRight();
            int n4 = this.this$0.getWidth();
            if (this.this$0.mDropDownWidth == -2) {
                int n5 = this.this$0.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, this.getBackground());
                int n6 = this.this$0.getContext().getResources().getDisplayMetrics().widthPixels - this.this$0.mTempRect.left - this.this$0.mTempRect.right;
                int n7 = n5;
                if (n5 > n6) {
                    n7 = n6;
                }
                this.setContentWidth(Math.max(n7, n4 - n2 - n3));
            } else if (this.this$0.mDropDownWidth == -1) {
                this.setContentWidth(n4 - n2 - n3);
            } else {
                this.setContentWidth(this.this$0.mDropDownWidth);
            }
            n = ViewUtils.isLayoutRtl((View)this.this$0) ? (n += n4 - n3 - this.getWidth() - this.getHorizontalOriginalOffset()) : (n += this.getHorizontalOriginalOffset() + n2);
            this.setHorizontalOffset(n);
        }

        @Override
        public CharSequence getHintText() {
            return this.mHintText;
        }

        @Override
        public int getHorizontalOriginalOffset() {
            return this.mOriginalHorizontalOffset;
        }

        boolean isVisibleToUser(View view) {
            boolean bl = ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
            return bl;
        }

        @Override
        public void setAdapter(ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }

        @Override
        public void setHorizontalOriginalOffset(int n) {
            this.mOriginalHorizontalOffset = n;
        }

        @Override
        public void setPromptText(CharSequence charSequence) {
            this.mHintText = charSequence;
        }

        @Override
        public void show(int n, int n2) {
            boolean bl = this.isShowing();
            this.computeContentWidth();
            this.setInputMethodMode(2);
            super.show();
            ListView listView = this.getListView();
            listView.setChoiceMode(1);
            if (Build.VERSION.SDK_INT >= 17) {
                listView.setTextDirection(n);
                listView.setTextAlignment(n2);
            }
            this.setSelection(this.this$0.getSelectedItemPosition());
            if (bl) {
                return;
            }
            listView = this.this$0.getViewTreeObserver();
            if (listView != null) {
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(this){
                    final DropdownPopup this$1;
                    {
                        this.this$1 = dropdownPopup;
                    }

                    public void onGlobalLayout() {
                        DropdownPopup dropdownPopup = this.this$1;
                        if (!dropdownPopup.isVisibleToUser((View)dropdownPopup.this$0)) {
                            this.this$1.dismiss();
                        } else {
                            this.this$1.computeContentWidth();
                            DropdownPopup.super.show();
                        }
                    }
                };
                listView.addOnGlobalLayoutListener(onGlobalLayoutListener);
                this.setOnDismissListener(new PopupWindow.OnDismissListener(this, onGlobalLayoutListener){
                    final DropdownPopup this$1;
                    final ViewTreeObserver.OnGlobalLayoutListener val$layoutListener;
                    {
                        this.this$1 = dropdownPopup;
                        this.val$layoutListener = onGlobalLayoutListener;
                    }

                    public void onDismiss() {
                        ViewTreeObserver viewTreeObserver = this.this$1.this$0.getViewTreeObserver();
                        if (viewTreeObserver != null) {
                            viewTreeObserver.removeGlobalOnLayoutListener(this.val$layoutListener);
                        }
                    }
                });
            }
        }
    }

    static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        boolean mShowDropdown;

        SavedState(Parcel parcel) {
            super(parcel);
            boolean bl = parcel.readByte() != 0;
            this.mShowDropdown = bl;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeByte((byte)(this.mShowDropdown ? 1 : 0));
        }
    }

    static interface SpinnerPopup {
        public void dismiss();

        public Drawable getBackground();

        public CharSequence getHintText();

        public int getHorizontalOffset();

        public int getHorizontalOriginalOffset();

        public int getVerticalOffset();

        public boolean isShowing();

        public void setAdapter(ListAdapter var1);

        public void setBackgroundDrawable(Drawable var1);

        public void setHorizontalOffset(int var1);

        public void setHorizontalOriginalOffset(int var1);

        public void setPromptText(CharSequence var1);

        public void setVerticalOffset(int var1);

        public void show(int var1, int var2);
    }
}

