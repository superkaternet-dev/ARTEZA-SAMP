/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Fragment
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Message
 *  android.util.TypedValue
 *  android.view.ContextThemeWrapper
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.preference;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.DialogPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.EditTextPreferenceDialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.ListPreferenceDialogFragment;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.MultiSelectListPreferenceDialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceRecyclerViewAccessibilityDelegate;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@Deprecated
public abstract class PreferenceFragment
extends Fragment
implements PreferenceManager.OnPreferenceTreeClickListener,
PreferenceManager.OnDisplayPreferenceDialogListener,
PreferenceManager.OnNavigateToScreenListener,
DialogPreference.TargetFragment {
    @Deprecated
    public static final String ARG_PREFERENCE_ROOT = "androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT";
    private static final String DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG";
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final String PREFERENCES_TAG = "android:preferences";
    private final DividerDecoration mDividerDecoration = new DividerDecoration(this);
    private final Handler mHandler;
    private boolean mHavePrefs;
    private boolean mInitDone;
    private int mLayoutResId = R.layout.preference_list_fragment;
    RecyclerView mList;
    private PreferenceManager mPreferenceManager;
    private final Runnable mRequestFocus;
    private Runnable mSelectPreferenceRunnable;
    private Context mStyledContext;

    public PreferenceFragment() {
        this.mHandler = new Handler(this){
            final PreferenceFragment this$0;
            {
                this.this$0 = preferenceFragment;
            }

            public void handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        break;
                    }
                    case 1: {
                        this.this$0.bindPreferences();
                    }
                }
            }
        };
        this.mRequestFocus = new Runnable(this){
            final PreferenceFragment this$0;
            {
                this.this$0 = preferenceFragment;
            }

            @Override
            public void run() {
                this.this$0.mList.focusableViewAvailable((View)this.this$0.mList);
            }
        };
    }

    private void postBindPreferences() {
        if (this.mHandler.hasMessages(1)) {
            return;
        }
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager != null) {
            return;
        }
        throw new RuntimeException("This should be called after super.onCreate.");
    }

    private void scrollToPreferenceInternal(Preference object, String string2) {
        object = new Runnable(this, (Preference)object, string2){
            final PreferenceFragment this$0;
            final String val$key;
            final Preference val$preference;
            {
                this.this$0 = preferenceFragment;
                this.val$preference = preference;
                this.val$key = string2;
            }

            @Override
            public void run() {
                RecyclerView.Adapter adapter = this.this$0.mList.getAdapter();
                if (!(adapter instanceof PreferenceGroup.PreferencePositionCallback)) {
                    if (adapter == null) {
                        return;
                    }
                    throw new IllegalStateException("Adapter must implement PreferencePositionCallback");
                }
                Preference preference = this.val$preference;
                int n = preference != null ? ((PreferenceGroup.PreferencePositionCallback)((Object)adapter)).getPreferenceAdapterPosition(preference) : ((PreferenceGroup.PreferencePositionCallback)((Object)adapter)).getPreferenceAdapterPosition(this.val$key);
                if (n != -1) {
                    this.this$0.mList.scrollToPosition(n);
                } else {
                    adapter.registerAdapterDataObserver(new ScrollToPreferenceObserver(adapter, this.this$0.mList, this.val$preference, this.val$key));
                }
            }
        };
        if (this.mList == null) {
            this.mSelectPreferenceRunnable = object;
        } else {
            object.run();
        }
    }

    private void unbindPreferences() {
        PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.onDetached();
        }
        this.onUnbindPreferences();
    }

    @Deprecated
    public void addPreferencesFromResource(int n) {
        this.requirePreferenceManager();
        this.setPreferenceScreen(this.mPreferenceManager.inflateFromResource(this.mStyledContext, n, this.getPreferenceScreen()));
    }

    void bindPreferences() {
        PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        if (preferenceScreen != null) {
            this.getListView().setAdapter(this.onCreateAdapter(preferenceScreen));
            preferenceScreen.onAttached();
        }
        this.onBindPreferences();
    }

    @Override
    @Deprecated
    public <T extends Preference> T findPreference(CharSequence charSequence) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager == null) {
            return null;
        }
        return preferenceManager.findPreference(charSequence);
    }

    public Fragment getCallbackFragment() {
        return null;
    }

    @Deprecated
    public final RecyclerView getListView() {
        return this.mList;
    }

    @Deprecated
    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    @Deprecated
    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceManager.getPreferenceScreen();
    }

    protected void onBindPreferences() {
    }

    public void onCreate(Bundle bundle) {
        int n;
        super.onCreate(bundle);
        Object object = new TypedValue();
        this.getActivity().getTheme().resolveAttribute(R.attr.preferenceTheme, (TypedValue)object, true);
        int n2 = n = ((TypedValue)object).resourceId;
        if (n == 0) {
            n2 = R.style.PreferenceThemeOverlay;
        }
        object = new ContextThemeWrapper((Context)this.getActivity(), n2);
        this.mStyledContext = object;
        object = new PreferenceManager((Context)object);
        this.mPreferenceManager = object;
        ((PreferenceManager)object).setOnNavigateToScreenListener(this);
        object = this.getArguments() != null ? this.getArguments().getString(ARG_PREFERENCE_ROOT) : null;
        this.onCreatePreferences(bundle, (String)object);
    }

    @Deprecated
    protected RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new PreferenceGroupAdapter(preferenceScreen);
    }

    @Deprecated
    public RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager((Context)this.getActivity());
    }

    @Deprecated
    public abstract void onCreatePreferences(Bundle var1, String var2);

    @Deprecated
    public RecyclerView onCreateRecyclerView(LayoutInflater object, ViewGroup viewGroup, Bundle object2) {
        if (this.mStyledContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive") && (object2 = (RecyclerView)viewGroup.findViewById(R.id.recycler_view)) != null) {
            return object2;
        }
        object = (RecyclerView)object.inflate(R.layout.preference_recyclerview, viewGroup, false);
        ((RecyclerView)object).setLayoutManager(this.onCreateLayoutManager());
        ((RecyclerView)object).setAccessibilityDelegateCompat(new PreferenceRecyclerViewAccessibilityDelegate((RecyclerView)object));
        return object;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle object) {
        TypedArray typedArray = this.mStyledContext.obtainStyledAttributes(null, R.styleable.PreferenceFragment, TypedArrayUtils.getAttr(this.mStyledContext, R.attr.preferenceFragmentStyle, 16844038), 0);
        this.mLayoutResId = typedArray.getResourceId(R.styleable.PreferenceFragment_android_layout, this.mLayoutResId);
        Drawable drawable2 = typedArray.getDrawable(R.styleable.PreferenceFragment_android_divider);
        int n = typedArray.getDimensionPixelSize(R.styleable.PreferenceFragment_android_dividerHeight, -1);
        boolean bl = typedArray.getBoolean(R.styleable.PreferenceFragment_allowDividerAfterLastItem, true);
        typedArray.recycle();
        typedArray = layoutInflater.cloneInContext(this.mStyledContext);
        layoutInflater = typedArray.inflate(this.mLayoutResId, viewGroup, false);
        viewGroup = layoutInflater.findViewById(16908351);
        if (viewGroup instanceof ViewGroup) {
            if ((object = this.onCreateRecyclerView((LayoutInflater)typedArray, viewGroup, (Bundle)object)) != null) {
                this.mList = object;
                ((RecyclerView)object).addItemDecoration(this.mDividerDecoration);
                this.setDivider(drawable2);
                if (n != -1) {
                    this.setDividerHeight(n);
                }
                this.mDividerDecoration.setAllowDividerAfterLastItem(bl);
                if (this.mList.getParent() == null) {
                    viewGroup.addView((View)this.mList);
                }
                this.mHandler.post(this.mRequestFocus);
                return layoutInflater;
            }
            throw new RuntimeException("Could not create RecyclerView");
        }
        throw new RuntimeException("Content has view with id attribute 'android.R.id.list_container' that is not a ViewGroup class");
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(1);
        if (this.mHavePrefs) {
            this.unbindPreferences();
        }
        this.mList = null;
        super.onDestroyView();
    }

    @Override
    @Deprecated
    public void onDisplayPreferenceDialog(Preference object) {
        block11: {
            block9: {
                block10: {
                    block8: {
                        boolean bl = false;
                        if (this.getCallbackFragment() instanceof OnPreferenceDisplayDialogCallback) {
                            bl = ((OnPreferenceDisplayDialogCallback)this.getCallbackFragment()).onPreferenceDisplayDialog(this, (Preference)object);
                        }
                        boolean bl2 = bl;
                        if (!bl) {
                            bl2 = bl;
                            if (this.getActivity() instanceof OnPreferenceDisplayDialogCallback) {
                                bl2 = ((OnPreferenceDisplayDialogCallback)this.getActivity()).onPreferenceDisplayDialog(this, (Preference)object);
                            }
                        }
                        if (bl2) {
                            return;
                        }
                        if (this.getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                            return;
                        }
                        if (!(object instanceof EditTextPreference)) break block8;
                        object = EditTextPreferenceDialogFragment.newInstance(object.getKey());
                        break block9;
                    }
                    if (!(object instanceof ListPreference)) break block10;
                    object = ListPreferenceDialogFragment.newInstance(object.getKey());
                    break block9;
                }
                if (!(object instanceof MultiSelectListPreference)) break block11;
                object = MultiSelectListPreferenceDialogFragment.newInstance(object.getKey());
            }
            object.setTargetFragment(this, 0);
            object.show(this.getFragmentManager(), DIALOG_FRAGMENT_TAG);
            return;
        }
        throw new IllegalArgumentException("Tried to display dialog for unknown preference type. Did you forget to override onDisplayPreferenceDialog()?");
    }

    @Override
    @Deprecated
    public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
        boolean bl = false;
        if (this.getCallbackFragment() instanceof OnPreferenceStartScreenCallback) {
            bl = ((OnPreferenceStartScreenCallback)this.getCallbackFragment()).onPreferenceStartScreen(this, preferenceScreen);
        }
        if (!bl && this.getActivity() instanceof OnPreferenceStartScreenCallback) {
            ((OnPreferenceStartScreenCallback)this.getActivity()).onPreferenceStartScreen(this, preferenceScreen);
        }
    }

    @Override
    @Deprecated
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getFragment() != null) {
            boolean bl = false;
            if (this.getCallbackFragment() instanceof OnPreferenceStartFragmentCallback) {
                bl = ((OnPreferenceStartFragmentCallback)this.getCallbackFragment()).onPreferenceStartFragment(this, preference);
            }
            boolean bl2 = bl;
            if (!bl) {
                bl2 = bl;
                if (this.getActivity() instanceof OnPreferenceStartFragmentCallback) {
                    bl2 = ((OnPreferenceStartFragmentCallback)this.getActivity()).onPreferenceStartFragment(this, preference);
                }
            }
            return bl2;
        }
        return false;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle bundle2 = new Bundle();
            preferenceScreen.saveHierarchyState(bundle2);
            bundle.putBundle(PREFERENCES_TAG, bundle2);
        }
    }

    public void onStart() {
        super.onStart();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(this);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(this);
    }

    public void onStop() {
        super.onStop();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(null);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(null);
    }

    protected void onUnbindPreferences() {
    }

    public void onViewCreated(View object, Bundle bundle) {
        super.onViewCreated((View)object, bundle);
        if (bundle != null && (bundle = bundle.getBundle(PREFERENCES_TAG)) != null && (object = this.getPreferenceScreen()) != null) {
            ((Preference)object).restoreHierarchyState(bundle);
        }
        if (this.mHavePrefs) {
            this.bindPreferences();
            object = this.mSelectPreferenceRunnable;
            if (object != null) {
                object.run();
                this.mSelectPreferenceRunnable = null;
            }
        }
        this.mInitDone = true;
    }

    @Deprecated
    public void scrollToPreference(Preference preference) {
        this.scrollToPreferenceInternal(preference, null);
    }

    @Deprecated
    public void scrollToPreference(String string2) {
        this.scrollToPreferenceInternal(null, string2);
    }

    @Deprecated
    public void setDivider(Drawable drawable2) {
        this.mDividerDecoration.setDivider(drawable2);
    }

    @Deprecated
    public void setDividerHeight(int n) {
        this.mDividerDecoration.setDividerHeight(n);
    }

    @Deprecated
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        if (this.mPreferenceManager.setPreferences(preferenceScreen) && preferenceScreen != null) {
            this.onUnbindPreferences();
            this.mHavePrefs = true;
            if (this.mInitDone) {
                this.postBindPreferences();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Deprecated
    public void setPreferencesFromResource(int n, String object) {
        this.requirePreferenceManager();
        Comparable<Preference> comparable = this.mPreferenceManager.inflateFromResource(this.mStyledContext, n, null);
        if (object != null) {
            if (!((comparable = ((PreferenceGroup)comparable).findPreference((CharSequence)object)) instanceof PreferenceScreen)) {
                comparable = new StringBuilder();
                ((StringBuilder)comparable).append("Preference object with key ");
                ((StringBuilder)comparable).append((String)object);
                ((StringBuilder)comparable).append(" is not a PreferenceScreen");
                throw new IllegalArgumentException(((StringBuilder)comparable).toString());
            }
            object = comparable;
        } else {
            object = comparable;
        }
        this.setPreferenceScreen((PreferenceScreen)object);
    }

    private class DividerDecoration
    extends RecyclerView.ItemDecoration {
        private boolean mAllowDividerAfterLastItem;
        private Drawable mDivider;
        private int mDividerHeight;
        final PreferenceFragment this$0;

        DividerDecoration(PreferenceFragment preferenceFragment) {
            this.this$0 = preferenceFragment;
            this.mAllowDividerAfterLastItem = true;
        }

        private boolean shouldDrawDividerBelow(View object, RecyclerView recyclerView) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder((View)object);
            boolean bl = viewHolder instanceof PreferenceViewHolder;
            boolean bl2 = false;
            int n = bl && ((PreferenceViewHolder)viewHolder).isDividerAllowedBelow() ? 1 : 0;
            if (n == 0) {
                return false;
            }
            bl = this.mAllowDividerAfterLastItem;
            n = recyclerView.indexOfChild((View)object);
            if (n < recyclerView.getChildCount() - 1) {
                object = recyclerView.getChildViewHolder(recyclerView.getChildAt(n + 1));
                bl = object instanceof PreferenceViewHolder && ((PreferenceViewHolder)object).isDividerAllowedAbove() ? true : bl2;
            }
            return bl;
        }

        @Override
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            if (this.shouldDrawDividerBelow(view, recyclerView)) {
                rect.bottom = this.mDividerHeight;
            }
        }

        @Override
        public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            if (this.mDivider == null) {
                return;
            }
            int n = recyclerView.getChildCount();
            int n2 = recyclerView.getWidth();
            for (int i = 0; i < n; ++i) {
                state = recyclerView.getChildAt(i);
                if (!this.shouldDrawDividerBelow((View)state, recyclerView)) continue;
                int n3 = (int)state.getY() + state.getHeight();
                this.mDivider.setBounds(0, n3, n2, this.mDividerHeight + n3);
                this.mDivider.draw(canvas);
            }
        }

        public void setAllowDividerAfterLastItem(boolean bl) {
            this.mAllowDividerAfterLastItem = bl;
        }

        public void setDivider(Drawable drawable2) {
            this.mDividerHeight = drawable2 != null ? drawable2.getIntrinsicHeight() : 0;
            this.mDivider = drawable2;
            this.this$0.mList.invalidateItemDecorations();
        }

        public void setDividerHeight(int n) {
            this.mDividerHeight = n;
            this.this$0.mList.invalidateItemDecorations();
        }
    }

    public static interface OnPreferenceDisplayDialogCallback {
        public boolean onPreferenceDisplayDialog(PreferenceFragment var1, Preference var2);
    }

    public static interface OnPreferenceStartFragmentCallback {
        public boolean onPreferenceStartFragment(PreferenceFragment var1, Preference var2);
    }

    public static interface OnPreferenceStartScreenCallback {
        public boolean onPreferenceStartScreen(PreferenceFragment var1, PreferenceScreen var2);
    }

    private static class ScrollToPreferenceObserver
    extends RecyclerView.AdapterDataObserver {
        private final RecyclerView.Adapter mAdapter;
        private final String mKey;
        private final RecyclerView mList;
        private final Preference mPreference;

        ScrollToPreferenceObserver(RecyclerView.Adapter adapter, RecyclerView recyclerView, Preference preference, String string2) {
            this.mAdapter = adapter;
            this.mList = recyclerView;
            this.mPreference = preference;
            this.mKey = string2;
        }

        private void scrollToPreference() {
            this.mAdapter.unregisterAdapterDataObserver(this);
            Preference preference = this.mPreference;
            int n = preference != null ? ((PreferenceGroup.PreferencePositionCallback)((Object)this.mAdapter)).getPreferenceAdapterPosition(preference) : ((PreferenceGroup.PreferencePositionCallback)((Object)this.mAdapter)).getPreferenceAdapterPosition(this.mKey);
            if (n != -1) {
                this.mList.scrollToPosition(n);
            }
        }

        @Override
        public void onChanged() {
            this.scrollToPreference();
        }

        @Override
        public void onItemRangeChanged(int n, int n2) {
            this.scrollToPreference();
        }

        @Override
        public void onItemRangeChanged(int n, int n2, Object object) {
            this.scrollToPreference();
        }

        @Override
        public void onItemRangeInserted(int n, int n2) {
            this.scrollToPreference();
        }

        @Override
        public void onItemRangeMoved(int n, int n2, int n3) {
            this.scrollToPreference();
        }

        @Override
        public void onItemRangeRemoved(int n, int n2) {
            this.scrollToPreference();
        }
    }
}

