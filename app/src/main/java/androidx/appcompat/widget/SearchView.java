/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.app.SearchableInfo
 *  android.content.ActivityNotFoundException
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.database.Cursor
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.text.Editable
 *  android.text.SpannableStringBuilder
 *  android.text.TextUtils
 *  android.text.TextWatcher
 *  android.text.style.ImageSpan
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.KeyEvent
 *  android.view.KeyEvent$DispatcherState
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.TouchDelegate
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.View$OnFocusChangeListener
 *  android.view.View$OnKeyListener
 *  android.view.View$OnLayoutChangeListener
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.inputmethod.EditorInfo
 *  android.view.inputmethod.InputConnection
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.AutoCompleteTextView
 *  android.widget.ImageView
 *  android.widget.ListAdapter
 *  android.widget.TextView
 *  android.widget.TextView$OnEditorActionListener
 */
package androidx.appcompat.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SuggestionsAdapter;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.TooltipCompat;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.view.ViewCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.customview.view.AbsSavedState;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView
extends LinearLayoutCompat
implements CollapsibleActionView {
    static final boolean DBG = false;
    private static final String IME_OPTION_NO_MICROPHONE = "nm";
    static final String LOG_TAG = "SearchView";
    static final PreQAutoCompleteTextViewReflector PRE_API_29_HIDDEN_METHOD_INVOKER;
    private Bundle mAppSearchData;
    private boolean mClearingFocus;
    final ImageView mCloseButton;
    private final ImageView mCollapsedIcon;
    private int mCollapsedImeOptions;
    private final CharSequence mDefaultQueryHint;
    private final View mDropDownAnchor;
    private boolean mExpandedInActionView;
    final ImageView mGoButton;
    private boolean mIconified;
    private boolean mIconifiedByDefault;
    private int mMaxWidth;
    private CharSequence mOldQueryText;
    private final View.OnClickListener mOnClickListener;
    private OnCloseListener mOnCloseListener;
    private final TextView.OnEditorActionListener mOnEditorActionListener;
    private final AdapterView.OnItemClickListener mOnItemClickListener;
    private final AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    private OnQueryTextListener mOnQueryChangeListener;
    View.OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private View.OnClickListener mOnSearchClickListener;
    private OnSuggestionListener mOnSuggestionListener;
    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
    private CharSequence mQueryHint;
    private boolean mQueryRefinement;
    private Runnable mReleaseCursorRunnable;
    final ImageView mSearchButton;
    private final View mSearchEditFrame;
    private final Drawable mSearchHintIcon;
    private final View mSearchPlate;
    final SearchAutoComplete mSearchSrcTextView;
    private Rect mSearchSrcTextViewBounds = new Rect();
    private Rect mSearchSrtTextViewBoundsExpanded = new Rect();
    SearchableInfo mSearchable;
    private final View mSubmitArea;
    private boolean mSubmitButtonEnabled;
    private final int mSuggestionCommitIconResId;
    private final int mSuggestionRowLayout;
    CursorAdapter mSuggestionsAdapter;
    private int[] mTemp = new int[2];
    private int[] mTemp2 = new int[2];
    View.OnKeyListener mTextKeyListener;
    private TextWatcher mTextWatcher;
    private UpdatableTouchDelegate mTouchDelegate;
    private final Runnable mUpdateDrawableStateRunnable = new Runnable(this){
        final SearchView this$0;
        {
            this.this$0 = searchView;
        }

        @Override
        public void run() {
            this.this$0.updateFocusedState();
        }
    };
    private CharSequence mUserQuery;
    private final Intent mVoiceAppSearchIntent;
    final ImageView mVoiceButton;
    private boolean mVoiceButtonEnabled;
    private final Intent mVoiceWebSearchIntent;

    static {
        PreQAutoCompleteTextViewReflector preQAutoCompleteTextViewReflector = Build.VERSION.SDK_INT < 29 ? new PreQAutoCompleteTextViewReflector() : null;
        PRE_API_29_HIDDEN_METHOD_INVOKER = preQAutoCompleteTextViewReflector;
    }

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.searchViewStyle);
    }

    public SearchView(Context object, AttributeSet object2, int n) {
        super((Context)object, (AttributeSet)object2, n);
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;
        View view;
        View view2;
        AdapterView.OnItemSelectedListener onItemSelectedListener;
        AdapterView.OnItemClickListener onItemClickListener;
        TextView.OnEditorActionListener onEditorActionListener;
        View.OnClickListener onClickListener;
        this.mReleaseCursorRunnable = new Runnable(this){
            final SearchView this$0;
            {
                this.this$0 = searchView;
            }

            @Override
            public void run() {
                if (this.this$0.mSuggestionsAdapter instanceof SuggestionsAdapter) {
                    this.this$0.mSuggestionsAdapter.changeCursor(null);
                }
            }
        };
        this.mOutsideDrawablesCache = new WeakHashMap();
        this.mOnClickListener = onClickListener = new View.OnClickListener(this){
            final SearchView this$0;
            {
                this.this$0 = searchView;
            }

            public void onClick(View view) {
                if (view == this.this$0.mSearchButton) {
                    this.this$0.onSearchClicked();
                } else if (view == this.this$0.mCloseButton) {
                    this.this$0.onCloseClicked();
                } else if (view == this.this$0.mGoButton) {
                    this.this$0.onSubmitQuery();
                } else if (view == this.this$0.mVoiceButton) {
                    this.this$0.onVoiceClicked();
                } else if (view == this.this$0.mSearchSrcTextView) {
                    this.this$0.forceSuggestionQuery();
                }
            }
        };
        this.mTextKeyListener = new View.OnKeyListener(this){
            final SearchView this$0;
            {
                this.this$0 = searchView;
            }

            public boolean onKey(View object, int n, KeyEvent keyEvent) {
                if (this.this$0.mSearchable == null) {
                    return false;
                }
                if (this.this$0.mSearchSrcTextView.isPopupShowing() && this.this$0.mSearchSrcTextView.getListSelection() != -1) {
                    return this.this$0.onSuggestionsKey((View)object, n, keyEvent);
                }
                if (!this.this$0.mSearchSrcTextView.isEmpty() && keyEvent.hasNoModifiers() && keyEvent.getAction() == 1 && n == 66) {
                    object.cancelLongPress();
                    object = this.this$0;
                    ((SearchView)object).launchQuerySearch(0, null, ((SearchView)object).mSearchSrcTextView.getText().toString());
                    return true;
                }
                return false;
            }
        };
        this.mOnEditorActionListener = onEditorActionListener = new TextView.OnEditorActionListener(this){
            final SearchView this$0;
            {
                this.this$0 = searchView;
            }

            public boolean onEditorAction(TextView textView, int n, KeyEvent keyEvent) {
                this.this$0.onSubmitQuery();
                return true;
            }
        };
        this.mOnItemClickListener = onItemClickListener = new AdapterView.OnItemClickListener(this){
            final SearchView this$0;
            {
                this.this$0 = searchView;
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
                this.this$0.onItemClicked(n, 0, null);
            }
        };
        this.mOnItemSelectedListener = onItemSelectedListener = new AdapterView.OnItemSelectedListener(this){
            final SearchView this$0;
            {
                this.this$0 = searchView;
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int n, long l) {
                this.this$0.onItemSelected(n);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        this.mTextWatcher = new TextWatcher(this){
            final SearchView this$0;
            {
                this.this$0 = searchView;
            }

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
            }

            public void onTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                this.this$0.onTextChanged(charSequence);
            }
        };
        object2 = TintTypedArray.obtainStyledAttributes((Context)object, (AttributeSet)object2, R.styleable.SearchView, n, 0);
        LayoutInflater.from((Context)object).inflate(((TintTypedArray)object2).getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view), (ViewGroup)this, true);
        object = (SearchAutoComplete)this.findViewById(R.id.search_src_text);
        this.mSearchSrcTextView = object;
        ((SearchAutoComplete)object).setSearchView(this);
        this.mSearchEditFrame = this.findViewById(R.id.search_edit_frame);
        this.mSearchPlate = view2 = this.findViewById(R.id.search_plate);
        this.mSubmitArea = view = this.findViewById(R.id.submit_area);
        this.mSearchButton = imageView5 = (ImageView)this.findViewById(R.id.search_button);
        this.mGoButton = imageView4 = (ImageView)this.findViewById(R.id.search_go_btn);
        this.mCloseButton = imageView3 = (ImageView)this.findViewById(R.id.search_close_btn);
        this.mVoiceButton = imageView2 = (ImageView)this.findViewById(R.id.search_voice_btn);
        this.mCollapsedIcon = imageView = (ImageView)this.findViewById(R.id.search_mag_icon);
        ViewCompat.setBackground(view2, ((TintTypedArray)object2).getDrawable(R.styleable.SearchView_queryBackground));
        ViewCompat.setBackground(view, ((TintTypedArray)object2).getDrawable(R.styleable.SearchView_submitBackground));
        imageView5.setImageDrawable(((TintTypedArray)object2).getDrawable(R.styleable.SearchView_searchIcon));
        imageView4.setImageDrawable(((TintTypedArray)object2).getDrawable(R.styleable.SearchView_goIcon));
        imageView3.setImageDrawable(((TintTypedArray)object2).getDrawable(R.styleable.SearchView_closeIcon));
        imageView2.setImageDrawable(((TintTypedArray)object2).getDrawable(R.styleable.SearchView_voiceIcon));
        imageView.setImageDrawable(((TintTypedArray)object2).getDrawable(R.styleable.SearchView_searchIcon));
        this.mSearchHintIcon = ((TintTypedArray)object2).getDrawable(R.styleable.SearchView_searchHintIcon);
        TooltipCompat.setTooltipText((View)imageView5, this.getResources().getString(R.string.abc_searchview_description_search));
        this.mSuggestionRowLayout = ((TintTypedArray)object2).getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
        this.mSuggestionCommitIconResId = ((TintTypedArray)object2).getResourceId(R.styleable.SearchView_commitIcon, 0);
        imageView5.setOnClickListener(onClickListener);
        imageView3.setOnClickListener(onClickListener);
        imageView4.setOnClickListener(onClickListener);
        imageView2.setOnClickListener(onClickListener);
        object.setOnClickListener(onClickListener);
        object.addTextChangedListener(this.mTextWatcher);
        object.setOnEditorActionListener(onEditorActionListener);
        object.setOnItemClickListener(onItemClickListener);
        object.setOnItemSelectedListener(onItemSelectedListener);
        object.setOnKeyListener(this.mTextKeyListener);
        object.setOnFocusChangeListener(new View.OnFocusChangeListener(this){
            final SearchView this$0;
            {
                this.this$0 = searchView;
            }

            public void onFocusChange(View view, boolean bl) {
                if (this.this$0.mOnQueryTextFocusChangeListener != null) {
                    this.this$0.mOnQueryTextFocusChangeListener.onFocusChange((View)this.this$0, bl);
                }
            }
        });
        this.setIconifiedByDefault(((TintTypedArray)object2).getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
        n = ((TintTypedArray)object2).getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
        if (n != -1) {
            this.setMaxWidth(n);
        }
        this.mDefaultQueryHint = ((TintTypedArray)object2).getText(R.styleable.SearchView_defaultQueryHint);
        this.mQueryHint = ((TintTypedArray)object2).getText(R.styleable.SearchView_queryHint);
        n = ((TintTypedArray)object2).getInt(R.styleable.SearchView_android_imeOptions, -1);
        if (n != -1) {
            this.setImeOptions(n);
        }
        if ((n = ((TintTypedArray)object2).getInt(R.styleable.SearchView_android_inputType, -1)) != -1) {
            this.setInputType(n);
        }
        this.setFocusable(((TintTypedArray)object2).getBoolean(R.styleable.SearchView_android_focusable, true));
        ((TintTypedArray)object2).recycle();
        object2 = new Intent("android.speech.action.WEB_SEARCH");
        this.mVoiceWebSearchIntent = object2;
        object2.addFlags(0x10000000);
        object2.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        object2 = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.mVoiceAppSearchIntent = object2;
        object2.addFlags(0x10000000);
        object = this.findViewById(object.getDropDownAnchor());
        this.mDropDownAnchor = object;
        if (object != null) {
            object.addOnLayoutChangeListener(new View.OnLayoutChangeListener(this){
                final SearchView this$0;
                {
                    this.this$0 = searchView;
                }

                public void onLayoutChange(View view, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
                    this.this$0.adjustDropDownSizeAndPosition();
                }
            });
        }
        this.updateViewsVisibility(this.mIconifiedByDefault);
        this.updateQueryHint();
    }

    private Intent createIntent(String string2, Uri uri, String string3, String string4, int n, String string5) {
        string2 = new Intent(string2);
        string2.addFlags(0x10000000);
        if (uri != null) {
            string2.setData(uri);
        }
        string2.putExtra("user_query", this.mUserQuery);
        if (string4 != null) {
            string2.putExtra("query", string4);
        }
        if (string3 != null) {
            string2.putExtra("intent_extra_data_key", string3);
        }
        if ((uri = this.mAppSearchData) != null) {
            string2.putExtra("app_data", (Bundle)uri);
        }
        if (n != 0) {
            string2.putExtra("action_key", n);
            string2.putExtra("action_msg", string5);
        }
        string2.setComponent(this.mSearchable.getSearchActivity());
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Intent createIntentFromSuggestion(Cursor object, int n, String string2) {
        try {
            String string3;
            String string4;
            String string5 = string4 = SuggestionsAdapter.getColumnString((Cursor)object, "suggest_intent_action");
            if (string4 == null) {
                string5 = this.mSearchable.getSuggestIntentAction();
            }
            string4 = string5;
            if (string5 == null) {
                string4 = "android.intent.action.SEARCH";
            }
            CharSequence charSequence = SuggestionsAdapter.getColumnString((Cursor)object, "suggest_intent_data");
            string5 = charSequence;
            if (charSequence == null) {
                string5 = this.mSearchable.getSuggestIntentData();
            }
            if (string5 != null && (string3 = SuggestionsAdapter.getColumnString((Cursor)object, "suggest_intent_data_id")) != null) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(string5);
                ((StringBuilder)charSequence).append("/");
                ((StringBuilder)charSequence).append(Uri.encode((String)string3));
                string5 = ((StringBuilder)charSequence).toString();
            }
            string5 = string5 == null ? null : Uri.parse((String)string5);
            charSequence = SuggestionsAdapter.getColumnString((Cursor)object, "suggest_intent_query");
            return this.createIntent(string4, (Uri)string5, SuggestionsAdapter.getColumnString((Cursor)object, "suggest_intent_extra_data"), (String)charSequence, n, string2);
        }
        catch (RuntimeException runtimeException) {
            try {
                n = object.getPosition();
            }
            catch (RuntimeException runtimeException2) {
                n = -1;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Search suggestions cursor at row ");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" returned exception.");
            Log.w((String)LOG_TAG, (String)((StringBuilder)object).toString(), (Throwable)runtimeException);
            return null;
        }
    }

    private Intent createVoiceAppSearchIntent(Intent object, SearchableInfo searchableInfo) {
        ComponentName componentName = searchableInfo.getSearchActivity();
        Object object2 = new Intent("android.intent.action.SEARCH");
        object2.setComponent(componentName);
        PendingIntent pendingIntent = PendingIntent.getActivity((Context)this.getContext(), (int)0, (Intent)object2, (int)0x40000000);
        Bundle bundle = new Bundle();
        object2 = this.mAppSearchData;
        if (object2 != null) {
            bundle.putParcelable("app_data", (Parcelable)object2);
        }
        Intent intent = new Intent(object);
        object = "free_form";
        object2 = null;
        String string2 = null;
        int n = 1;
        Resources resources = this.getResources();
        if (searchableInfo.getVoiceLanguageModeId() != 0) {
            object = resources.getString(searchableInfo.getVoiceLanguageModeId());
        }
        if (searchableInfo.getVoicePromptTextId() != 0) {
            object2 = resources.getString(searchableInfo.getVoicePromptTextId());
        }
        if (searchableInfo.getVoiceLanguageId() != 0) {
            string2 = resources.getString(searchableInfo.getVoiceLanguageId());
        }
        if (searchableInfo.getVoiceMaxResults() != 0) {
            n = searchableInfo.getVoiceMaxResults();
        }
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", (String)object);
        intent.putExtra("android.speech.extra.PROMPT", (String)object2);
        intent.putExtra("android.speech.extra.LANGUAGE", string2);
        intent.putExtra("android.speech.extra.MAX_RESULTS", n);
        object = componentName == null ? null : componentName.flattenToShortString();
        intent.putExtra("calling_package", (String)object);
        intent.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", (Parcelable)pendingIntent);
        intent.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
        return intent;
    }

    private Intent createVoiceWebSearchIntent(Intent object, SearchableInfo searchableInfo) {
        Intent intent = new Intent(object);
        object = searchableInfo.getSearchActivity();
        object = object == null ? null : object.flattenToShortString();
        intent.putExtra("calling_package", (String)object);
        return intent;
    }

    private void dismissSuggestions() {
        this.mSearchSrcTextView.dismissDropDown();
    }

    private void getChildBoundsWithinSearchView(View view, Rect rect) {
        view.getLocationInWindow(this.mTemp);
        this.getLocationInWindow(this.mTemp2);
        int[] nArray = this.mTemp;
        int n = nArray[1];
        int[] nArray2 = this.mTemp2;
        int n2 = n - nArray2[1];
        n = nArray[0] - nArray2[0];
        rect.set(n, n2, view.getWidth() + n, view.getHeight() + n2);
    }

    private CharSequence getDecoratedHint(CharSequence charSequence) {
        if (this.mIconifiedByDefault && this.mSearchHintIcon != null) {
            double d = this.mSearchSrcTextView.getTextSize();
            Double.isNaN(d);
            int n = (int)(d * 1.25);
            this.mSearchHintIcon.setBounds(0, 0, n, n);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)"   ");
            spannableStringBuilder.setSpan((Object)new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
            spannableStringBuilder.append(charSequence);
            return spannableStringBuilder;
        }
        return charSequence;
    }

    private int getPreferredHeight() {
        return this.getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_height);
    }

    private int getPreferredWidth() {
        return this.getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
    }

    private boolean hasVoiceSearch() {
        SearchableInfo searchableInfo = this.mSearchable;
        boolean bl = false;
        if (searchableInfo != null && searchableInfo.getVoiceSearchEnabled()) {
            searchableInfo = null;
            if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
                searchableInfo = this.mVoiceWebSearchIntent;
            } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
                searchableInfo = this.mVoiceAppSearchIntent;
            }
            if (searchableInfo != null) {
                if (this.getContext().getPackageManager().resolveActivity((Intent)searchableInfo, 65536) != null) {
                    bl = true;
                }
                return bl;
            }
        }
        return false;
    }

    static boolean isLandscapeMode(Context context) {
        boolean bl = context.getResources().getConfiguration().orientation == 2;
        return bl;
    }

    private boolean isSubmitAreaEnabled() {
        boolean bl = (this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !this.isIconified();
        return bl;
    }

    private void launchIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            this.getContext().startActivity(intent);
        }
        catch (RuntimeException runtimeException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed launch activity: ");
            stringBuilder.append(intent);
            Log.e((String)LOG_TAG, (String)stringBuilder.toString(), (Throwable)runtimeException);
        }
    }

    private boolean launchSuggestion(int n, int n2, String string2) {
        Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor != null && cursor.moveToPosition(n)) {
            this.launchIntent(this.createIntentFromSuggestion(cursor, n2, string2));
            return true;
        }
        return false;
    }

    private void postUpdateFocusedState() {
        this.post(this.mUpdateDrawableStateRunnable);
    }

    private void rewriteQueryFromSuggestion(int n) {
        Editable editable = this.mSearchSrcTextView.getText();
        Object object = this.mSuggestionsAdapter.getCursor();
        if (object == null) {
            return;
        }
        if (object.moveToPosition(n)) {
            if ((object = this.mSuggestionsAdapter.convertToString((Cursor)object)) != null) {
                this.setQuery((CharSequence)object);
            } else {
                this.setQuery((CharSequence)editable);
            }
        } else {
            this.setQuery((CharSequence)editable);
        }
    }

    private void setQuery(CharSequence charSequence) {
        this.mSearchSrcTextView.setText(charSequence);
        SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
        int n = TextUtils.isEmpty((CharSequence)charSequence) ? 0 : charSequence.length();
        searchAutoComplete.setSelection(n);
    }

    private void updateCloseButton() {
        boolean bl = TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText());
        int n = 1;
        boolean bl2 = bl ^ true;
        int n2 = 0;
        int n3 = n;
        if (!bl2) {
            n3 = this.mIconifiedByDefault && !this.mExpandedInActionView ? n : 0;
        }
        Object object = this.mCloseButton;
        n3 = n3 != 0 ? n2 : 8;
        object.setVisibility(n3);
        Drawable drawable2 = this.mCloseButton.getDrawable();
        if (drawable2 != null) {
            object = bl2 ? (Object)ENABLED_STATE_SET : (Object)EMPTY_STATE_SET;
            drawable2.setState((int[])object);
        }
    }

    private void updateQueryHint() {
        CharSequence charSequence = this.getQueryHint();
        SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
        if (charSequence == null) {
            charSequence = "";
        }
        searchAutoComplete.setHint(this.getDecoratedHint(charSequence));
    }

    private void updateSearchAutoComplete() {
        this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
        this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
        int n = this.mSearchable.getInputType();
        int n2 = 1;
        int n3 = n;
        if ((n & 0xF) == 1) {
            n3 = n &= 0xFFFEFFFF;
            if (this.mSearchable.getSuggestAuthority() != null) {
                n3 = n | 0x10000 | 0x80000;
            }
        }
        this.mSearchSrcTextView.setInputType(n3);
        CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
        if (cursorAdapter != null) {
            cursorAdapter.changeCursor(null);
        }
        if (this.mSearchable.getSuggestAuthority() != null) {
            this.mSuggestionsAdapter = cursorAdapter = new SuggestionsAdapter(this.getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
            this.mSearchSrcTextView.setAdapter((ListAdapter)cursorAdapter);
            cursorAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
            n3 = this.mQueryRefinement ? 2 : n2;
            ((SuggestionsAdapter)cursorAdapter).setQueryRefinement(n3);
        }
    }

    private void updateSubmitArea() {
        int n;
        block2: {
            block3: {
                int n2;
                n = n2 = 8;
                if (!this.isSubmitAreaEnabled()) break block2;
                if (this.mGoButton.getVisibility() == 0) break block3;
                n = n2;
                if (this.mVoiceButton.getVisibility() != 0) break block2;
            }
            n = 0;
        }
        this.mSubmitArea.setVisibility(n);
    }

    private void updateSubmitButton(boolean bl) {
        int n;
        block2: {
            block3: {
                int n2;
                n = n2 = 8;
                if (!this.mSubmitButtonEnabled) break block2;
                n = n2;
                if (!this.isSubmitAreaEnabled()) break block2;
                n = n2;
                if (!this.hasFocus()) break block2;
                if (bl) break block3;
                n = n2;
                if (this.mVoiceButtonEnabled) break block2;
            }
            n = 0;
        }
        this.mGoButton.setVisibility(n);
    }

    private void updateViewsVisibility(boolean bl) {
        this.mIconified = bl;
        int n = 8;
        boolean bl2 = false;
        int n2 = bl ? 0 : 8;
        boolean bl3 = TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText()) ^ true;
        this.mSearchButton.setVisibility(n2);
        this.updateSubmitButton(bl3);
        View view = this.mSearchEditFrame;
        n2 = bl ? n : 0;
        view.setVisibility(n2);
        n2 = this.mCollapsedIcon.getDrawable() != null && !this.mIconifiedByDefault ? 0 : 8;
        this.mCollapsedIcon.setVisibility(n2);
        this.updateCloseButton();
        bl = bl2;
        if (!bl3) {
            bl = true;
        }
        this.updateVoiceButton(bl);
        this.updateSubmitArea();
    }

    private void updateVoiceButton(boolean bl) {
        int n;
        int n2 = n = 8;
        if (this.mVoiceButtonEnabled) {
            n2 = n;
            if (!this.isIconified()) {
                n2 = n;
                if (bl) {
                    n2 = 0;
                    this.mGoButton.setVisibility(8);
                }
            }
        }
        this.mVoiceButton.setVisibility(n2);
    }

    void adjustDropDownSizeAndPosition() {
        if (this.mDropDownAnchor.getWidth() > 1) {
            Resources resources = this.getContext().getResources();
            int n = this.mSearchPlate.getPaddingLeft();
            Rect rect = new Rect();
            boolean bl = ViewUtils.isLayoutRtl((View)this);
            int n2 = this.mIconifiedByDefault ? resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left) : 0;
            this.mSearchSrcTextView.getDropDownBackground().getPadding(rect);
            int n3 = bl ? -rect.left : n - (rect.left + n2);
            this.mSearchSrcTextView.setDropDownHorizontalOffset(n3);
            n3 = this.mDropDownAnchor.getWidth();
            int n4 = rect.left;
            int n5 = rect.right;
            this.mSearchSrcTextView.setDropDownWidth(n3 + n4 + n5 + n2 - n);
        }
    }

    public void clearFocus() {
        this.mClearingFocus = true;
        super.clearFocus();
        this.mSearchSrcTextView.clearFocus();
        this.mSearchSrcTextView.setImeVisibility(false);
        this.mClearingFocus = false;
    }

    void forceSuggestionQuery() {
        if (Build.VERSION.SDK_INT >= 29) {
            this.mSearchSrcTextView.refreshAutoCompleteResults();
        } else {
            PreQAutoCompleteTextViewReflector preQAutoCompleteTextViewReflector = PRE_API_29_HIDDEN_METHOD_INVOKER;
            preQAutoCompleteTextViewReflector.doBeforeTextChanged(this.mSearchSrcTextView);
            preQAutoCompleteTextViewReflector.doAfterTextChanged(this.mSearchSrcTextView);
        }
    }

    public int getImeOptions() {
        return this.mSearchSrcTextView.getImeOptions();
    }

    public int getInputType() {
        return this.mSearchSrcTextView.getInputType();
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public CharSequence getQuery() {
        return this.mSearchSrcTextView.getText();
    }

    public CharSequence getQueryHint() {
        CharSequence charSequence;
        charSequence = this.mQueryHint != null ? this.mQueryHint : ((charSequence = this.mSearchable) != null && charSequence.getHintId() != 0 ? this.getContext().getText(this.mSearchable.getHintId()) : this.mDefaultQueryHint);
        return charSequence;
    }

    int getSuggestionCommitIconResId() {
        return this.mSuggestionCommitIconResId;
    }

    int getSuggestionRowLayout() {
        return this.mSuggestionRowLayout;
    }

    public CursorAdapter getSuggestionsAdapter() {
        return this.mSuggestionsAdapter;
    }

    public boolean isIconfiedByDefault() {
        return this.mIconifiedByDefault;
    }

    public boolean isIconified() {
        return this.mIconified;
    }

    public boolean isQueryRefinementEnabled() {
        return this.mQueryRefinement;
    }

    public boolean isSubmitButtonEnabled() {
        return this.mSubmitButtonEnabled;
    }

    void launchQuerySearch(int n, String string2, String string3) {
        string2 = this.createIntent("android.intent.action.SEARCH", null, null, string3, n, string2);
        this.getContext().startActivity((Intent)string2);
    }

    @Override
    public void onActionViewCollapsed() {
        this.setQuery("", false);
        this.clearFocus();
        this.updateViewsVisibility(true);
        this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
        this.mExpandedInActionView = false;
    }

    @Override
    public void onActionViewExpanded() {
        int n;
        if (this.mExpandedInActionView) {
            return;
        }
        this.mExpandedInActionView = true;
        this.mCollapsedImeOptions = n = this.mSearchSrcTextView.getImeOptions();
        this.mSearchSrcTextView.setImeOptions(n | 0x2000000);
        this.mSearchSrcTextView.setText("");
        this.setIconified(false);
    }

    void onCloseClicked() {
        if (TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText())) {
            OnCloseListener onCloseListener;
            if (this.mIconifiedByDefault && ((onCloseListener = this.mOnCloseListener) == null || !onCloseListener.onClose())) {
                this.clearFocus();
                this.updateViewsVisibility(true);
            }
        } else {
            this.mSearchSrcTextView.setText("");
            this.mSearchSrcTextView.requestFocus();
            this.mSearchSrcTextView.setImeVisibility(true);
        }
    }

    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mUpdateDrawableStateRunnable);
        this.post(this.mReleaseCursorRunnable);
        super.onDetachedFromWindow();
    }

    boolean onItemClicked(int n, int n2, String object) {
        object = this.mOnSuggestionListener;
        if (object != null && object.onSuggestionClick(n)) {
            return false;
        }
        this.launchSuggestion(n, 0, null);
        this.mSearchSrcTextView.setImeVisibility(false);
        this.dismissSuggestions();
        return true;
    }

    boolean onItemSelected(int n) {
        OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
        if (onSuggestionListener != null && onSuggestionListener.onSuggestionSelect(n)) {
            return false;
        }
        this.rewriteQueryFromSuggestion(n);
        return true;
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        if (bl) {
            this.getChildBoundsWithinSearchView((View)this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
            this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, n4 - n2);
            UpdatableTouchDelegate updatableTouchDelegate = this.mTouchDelegate;
            if (updatableTouchDelegate == null) {
                this.mTouchDelegate = updatableTouchDelegate = new UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, (View)this.mSearchSrcTextView);
                this.setTouchDelegate(updatableTouchDelegate);
            } else {
                updatableTouchDelegate.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
            }
        }
    }

    @Override
    protected void onMeasure(int n, int n2) {
        if (this.isIconified()) {
            super.onMeasure(n, n2);
            return;
        }
        int n3 = View.MeasureSpec.getMode((int)n);
        int n4 = View.MeasureSpec.getSize((int)n);
        switch (n3) {
            default: {
                n = n4;
                break;
            }
            case 0x40000000: {
                n3 = this.mMaxWidth;
                n = n4;
                if (n3 <= 0) break;
                n = Math.min(n3, n4);
                break;
            }
            case 0: {
                n = this.mMaxWidth;
                if (n > 0) break;
                n = this.getPreferredWidth();
                break;
            }
            case -2147483648: {
                n = this.mMaxWidth;
                n = n > 0 ? Math.min(n, n4) : Math.min(this.getPreferredWidth(), n4);
            }
        }
        n4 = View.MeasureSpec.getMode((int)n2);
        n2 = View.MeasureSpec.getSize((int)n2);
        switch (n4) {
            default: {
                break;
            }
            case 0: {
                n2 = this.getPreferredHeight();
                break;
            }
            case -2147483648: {
                n2 = Math.min(this.getPreferredHeight(), n2);
            }
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec((int)n, (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)n2, (int)0x40000000));
    }

    void onQueryRefine(CharSequence charSequence) {
        this.setQuery(charSequence);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.updateViewsVisibility(parcelable.isIconified);
        this.requestLayout();
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isIconified = this.isIconified();
        return savedState;
    }

    void onSearchClicked() {
        this.updateViewsVisibility(false);
        this.mSearchSrcTextView.requestFocus();
        this.mSearchSrcTextView.setImeVisibility(true);
        View.OnClickListener onClickListener = this.mOnSearchClickListener;
        if (onClickListener != null) {
            onClickListener.onClick((View)this);
        }
    }

    void onSubmitQuery() {
        OnQueryTextListener onQueryTextListener;
        Editable editable = this.mSearchSrcTextView.getText();
        if (!(editable == null || TextUtils.getTrimmedLength((CharSequence)editable) <= 0 || (onQueryTextListener = this.mOnQueryChangeListener) != null && onQueryTextListener.onQueryTextSubmit(editable.toString()))) {
            if (this.mSearchable != null) {
                this.launchQuerySearch(0, null, editable.toString());
            }
            this.mSearchSrcTextView.setImeVisibility(false);
            this.dismissSuggestions();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    boolean onSuggestionsKey(View view, int n, KeyEvent keyEvent) {
        if (this.mSearchable == null) {
            return false;
        }
        if (this.mSuggestionsAdapter == null) {
            return false;
        }
        if (keyEvent.getAction() != 0 || !keyEvent.hasNoModifiers()) return false;
        if (n == 66 || n == 84 || n == 61) return this.onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, null);
        if (n != 21 && n != 22) {
            if (n != 19) return false;
            this.mSearchSrcTextView.getListSelection();
            return false;
        }
        n = n == 21 ? 0 : this.mSearchSrcTextView.length();
        this.mSearchSrcTextView.setSelection(n);
        this.mSearchSrcTextView.setListSelection(0);
        this.mSearchSrcTextView.clearListSelection();
        this.mSearchSrcTextView.ensureImeVisible();
        return true;
    }

    void onTextChanged(CharSequence charSequence) {
        Editable editable = this.mSearchSrcTextView.getText();
        this.mUserQuery = editable;
        boolean bl = TextUtils.isEmpty((CharSequence)editable);
        boolean bl2 = true;
        this.updateSubmitButton(bl ^= true);
        if (bl) {
            bl2 = false;
        }
        this.updateVoiceButton(bl2);
        this.updateCloseButton();
        this.updateSubmitArea();
        if (this.mOnQueryChangeListener != null && !TextUtils.equals((CharSequence)charSequence, (CharSequence)this.mOldQueryText)) {
            this.mOnQueryChangeListener.onQueryTextChange(charSequence.toString());
        }
        this.mOldQueryText = charSequence.toString();
    }

    void onTextFocusChanged() {
        this.updateViewsVisibility(this.isIconified());
        this.postUpdateFocusedState();
        if (this.mSearchSrcTextView.hasFocus()) {
            this.forceSuggestionQuery();
        }
    }

    void onVoiceClicked() {
        if (this.mSearchable == null) {
            return;
        }
        SearchableInfo searchableInfo = this.mSearchable;
        try {
            if (searchableInfo.getVoiceSearchLaunchWebSearch()) {
                searchableInfo = this.createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, searchableInfo);
                this.getContext().startActivity((Intent)searchableInfo);
            } else if (searchableInfo.getVoiceSearchLaunchRecognizer()) {
                searchableInfo = this.createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, searchableInfo);
                this.getContext().startActivity((Intent)searchableInfo);
            }
        }
        catch (ActivityNotFoundException activityNotFoundException) {
            Log.w((String)LOG_TAG, (String)"Could not find voice search activity");
        }
    }

    public void onWindowFocusChanged(boolean bl) {
        super.onWindowFocusChanged(bl);
        this.postUpdateFocusedState();
    }

    public boolean requestFocus(int n, Rect rect) {
        if (this.mClearingFocus) {
            return false;
        }
        if (!this.isFocusable()) {
            return false;
        }
        if (!this.isIconified()) {
            boolean bl = this.mSearchSrcTextView.requestFocus(n, rect);
            if (bl) {
                this.updateViewsVisibility(false);
            }
            return bl;
        }
        return super.requestFocus(n, rect);
    }

    public void setAppSearchData(Bundle bundle) {
        this.mAppSearchData = bundle;
    }

    public void setIconified(boolean bl) {
        if (bl) {
            this.onCloseClicked();
        } else {
            this.onSearchClicked();
        }
    }

    public void setIconifiedByDefault(boolean bl) {
        if (this.mIconifiedByDefault == bl) {
            return;
        }
        this.mIconifiedByDefault = bl;
        this.updateViewsVisibility(bl);
        this.updateQueryHint();
    }

    public void setImeOptions(int n) {
        this.mSearchSrcTextView.setImeOptions(n);
    }

    public void setInputType(int n) {
        this.mSearchSrcTextView.setInputType(n);
    }

    public void setMaxWidth(int n) {
        this.mMaxWidth = n;
        this.requestLayout();
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.mOnQueryTextFocusChangeListener = onFocusChangeListener;
    }

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.mOnQueryChangeListener = onQueryTextListener;
    }

    public void setOnSearchClickListener(View.OnClickListener onClickListener) {
        this.mOnSearchClickListener = onClickListener;
    }

    public void setOnSuggestionListener(OnSuggestionListener onSuggestionListener) {
        this.mOnSuggestionListener = onSuggestionListener;
    }

    public void setQuery(CharSequence charSequence, boolean bl) {
        this.mSearchSrcTextView.setText(charSequence);
        if (charSequence != null) {
            SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
            searchAutoComplete.setSelection(searchAutoComplete.length());
            this.mUserQuery = charSequence;
        }
        if (bl && !TextUtils.isEmpty((CharSequence)charSequence)) {
            this.onSubmitQuery();
        }
    }

    public void setQueryHint(CharSequence charSequence) {
        this.mQueryHint = charSequence;
        this.updateQueryHint();
    }

    public void setQueryRefinementEnabled(boolean bl) {
        this.mQueryRefinement = bl;
        CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
        if (cursorAdapter instanceof SuggestionsAdapter) {
            cursorAdapter = (SuggestionsAdapter)cursorAdapter;
            int n = bl ? 2 : 1;
            ((SuggestionsAdapter)cursorAdapter).setQueryRefinement(n);
        }
    }

    public void setSearchableInfo(SearchableInfo searchableInfo) {
        boolean bl;
        this.mSearchable = searchableInfo;
        if (searchableInfo != null) {
            this.updateSearchAutoComplete();
            this.updateQueryHint();
        }
        this.mVoiceButtonEnabled = bl = this.hasVoiceSearch();
        if (bl) {
            this.mSearchSrcTextView.setPrivateImeOptions(IME_OPTION_NO_MICROPHONE);
        }
        this.updateViewsVisibility(this.isIconified());
    }

    public void setSubmitButtonEnabled(boolean bl) {
        this.mSubmitButtonEnabled = bl;
        this.updateViewsVisibility(this.isIconified());
    }

    public void setSuggestionsAdapter(CursorAdapter cursorAdapter) {
        this.mSuggestionsAdapter = cursorAdapter;
        this.mSearchSrcTextView.setAdapter((ListAdapter)cursorAdapter);
    }

    void updateFocusedState() {
        int[] nArray = this.mSearchSrcTextView.hasFocus() ? FOCUSED_STATE_SET : EMPTY_STATE_SET;
        Drawable drawable2 = this.mSearchPlate.getBackground();
        if (drawable2 != null) {
            drawable2.setState(nArray);
        }
        if ((drawable2 = this.mSubmitArea.getBackground()) != null) {
            drawable2.setState(nArray);
        }
        this.invalidate();
    }

    public static interface OnCloseListener {
        public boolean onClose();
    }

    public static interface OnQueryTextListener {
        public boolean onQueryTextChange(String var1);

        public boolean onQueryTextSubmit(String var1);
    }

    public static interface OnSuggestionListener {
        public boolean onSuggestionClick(int var1);

        public boolean onSuggestionSelect(int var1);
    }

    private static class PreQAutoCompleteTextViewReflector {
        private Method mDoAfterTextChanged = null;
        private Method mDoBeforeTextChanged = null;
        private Method mEnsureImeVisible = null;

        PreQAutoCompleteTextViewReflector() {
            Method method;
            PreQAutoCompleteTextViewReflector.preApi29Check();
            try {
                this.mDoBeforeTextChanged = method = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            try {
                this.mDoAfterTextChanged = method = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            try {
                this.mEnsureImeVisible = method = AutoCompleteTextView.class.getMethod("ensureImeVisible", Boolean.TYPE);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
        }

        private static void preApi29Check() {
            if (Build.VERSION.SDK_INT < 29) {
                return;
            }
            throw new UnsupportedClassVersionError("This function can only be used for API Level < 29.");
        }

        void doAfterTextChanged(AutoCompleteTextView autoCompleteTextView) {
            PreQAutoCompleteTextViewReflector.preApi29Check();
            Method method = this.mDoAfterTextChanged;
            if (method != null) {
                try {
                    method.invoke((Object)autoCompleteTextView, new Object[0]);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }

        void doBeforeTextChanged(AutoCompleteTextView autoCompleteTextView) {
            PreQAutoCompleteTextViewReflector.preApi29Check();
            Method method = this.mDoBeforeTextChanged;
            if (method != null) {
                try {
                    method.invoke((Object)autoCompleteTextView, new Object[0]);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }

        void ensureImeVisible(AutoCompleteTextView autoCompleteTextView) {
            PreQAutoCompleteTextViewReflector.preApi29Check();
            Method method = this.mEnsureImeVisible;
            if (method != null) {
                try {
                    method.invoke((Object)autoCompleteTextView, true);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        boolean isIconified;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.isIconified = (Boolean)parcel.readValue(null);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SearchView.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" isIconified=");
            stringBuilder.append(this.isIconified);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeValue((Object)this.isIconified);
        }
    }

    public static class SearchAutoComplete
    extends AppCompatAutoCompleteTextView {
        private boolean mHasPendingShowSoftInputRequest;
        final Runnable mRunShowSoftInputIfNecessary = new Runnable(this){
            final SearchAutoComplete this$0;
            {
                this.this$0 = searchAutoComplete;
            }

            @Override
            public void run() {
                this.this$0.showSoftInputIfNecessary();
            }
        };
        private SearchView mSearchView;
        private int mThreshold = this.getThreshold();

        public SearchAutoComplete(Context context) {
            this(context, null);
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, R.attr.autoCompleteTextViewStyle);
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet, int n) {
            super(context, attributeSet, n);
        }

        private int getSearchViewTextMinWidthDp() {
            Configuration configuration = this.getResources().getConfiguration();
            int n = configuration.screenWidthDp;
            int n2 = configuration.screenHeightDp;
            if (n >= 960 && n2 >= 720 && configuration.orientation == 2) {
                return 256;
            }
            if (n < 600 && (n < 640 || n2 < 480)) {
                return 160;
            }
            return 192;
        }

        public boolean enoughToFilter() {
            boolean bl = this.mThreshold <= 0 || super.enoughToFilter();
            return bl;
        }

        void ensureImeVisible() {
            if (Build.VERSION.SDK_INT >= 29) {
                this.setInputMethodMode(1);
                if (this.enoughToFilter()) {
                    this.showDropDown();
                }
            } else {
                PRE_API_29_HIDDEN_METHOD_INVOKER.ensureImeVisible(this);
            }
        }

        boolean isEmpty() {
            boolean bl = TextUtils.getTrimmedLength((CharSequence)this.getText()) == 0;
            return bl;
        }

        @Override
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            editorInfo = super.onCreateInputConnection(editorInfo);
            if (this.mHasPendingShowSoftInputRequest) {
                this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
                this.post(this.mRunShowSoftInputIfNecessary);
            }
            return editorInfo;
        }

        protected void onFinishInflate() {
            super.onFinishInflate();
            DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            this.setMinWidth((int)TypedValue.applyDimension((int)1, (float)this.getSearchViewTextMinWidthDp(), (DisplayMetrics)displayMetrics));
        }

        protected void onFocusChanged(boolean bl, int n, Rect rect) {
            super.onFocusChanged(bl, n, rect);
            this.mSearchView.onTextFocusChanged();
        }

        public boolean onKeyPreIme(int n, KeyEvent keyEvent) {
            if (n == 4) {
                if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState dispatcherState = this.getKeyDispatcherState();
                    if (dispatcherState != null) {
                        dispatcherState.startTracking(keyEvent, (Object)this);
                    }
                    return true;
                }
                if (keyEvent.getAction() == 1) {
                    KeyEvent.DispatcherState dispatcherState = this.getKeyDispatcherState();
                    if (dispatcherState != null) {
                        dispatcherState.handleUpEvent(keyEvent);
                    }
                    if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                        this.mSearchView.clearFocus();
                        this.setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(n, keyEvent);
        }

        public void onWindowFocusChanged(boolean bl) {
            super.onWindowFocusChanged(bl);
            if (bl && this.mSearchView.hasFocus() && this.getVisibility() == 0) {
                this.mHasPendingShowSoftInputRequest = true;
                if (SearchView.isLandscapeMode(this.getContext())) {
                    this.ensureImeVisible();
                }
            }
        }

        public void performCompletion() {
        }

        protected void replaceText(CharSequence charSequence) {
        }

        void setImeVisibility(boolean bl) {
            InputMethodManager inputMethodManager = (InputMethodManager)this.getContext().getSystemService("input_method");
            if (!bl) {
                this.mHasPendingShowSoftInputRequest = false;
                this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
                inputMethodManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
                return;
            }
            if (inputMethodManager.isActive((View)this)) {
                this.mHasPendingShowSoftInputRequest = false;
                this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
                inputMethodManager.showSoftInput((View)this, 0);
                return;
            }
            this.mHasPendingShowSoftInputRequest = true;
        }

        void setSearchView(SearchView searchView) {
            this.mSearchView = searchView;
        }

        public void setThreshold(int n) {
            super.setThreshold(n);
            this.mThreshold = n;
        }

        void showSoftInputIfNecessary() {
            if (this.mHasPendingShowSoftInputRequest) {
                ((InputMethodManager)this.getContext().getSystemService("input_method")).showSoftInput((View)this, 0);
                this.mHasPendingShowSoftInputRequest = false;
            }
        }
    }

    private static class UpdatableTouchDelegate
    extends TouchDelegate {
        private final Rect mActualBounds;
        private boolean mDelegateTargeted;
        private final View mDelegateView;
        private final int mSlop;
        private final Rect mSlopBounds;
        private final Rect mTargetBounds;

        public UpdatableTouchDelegate(Rect rect, Rect rect2, View view) {
            super(rect, view);
            this.mSlop = ViewConfiguration.get((Context)view.getContext()).getScaledTouchSlop();
            this.mTargetBounds = new Rect();
            this.mSlopBounds = new Rect();
            this.mActualBounds = new Rect();
            this.setBounds(rect, rect2);
            this.mDelegateView = view;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean bl;
            int n = (int)motionEvent.getX();
            int n2 = (int)motionEvent.getY();
            boolean bl2 = false;
            boolean bl3 = true;
            boolean bl4 = false;
            switch (motionEvent.getAction()) {
                default: {
                    bl = bl3;
                    break;
                }
                case 3: {
                    bl2 = this.mDelegateTargeted;
                    this.mDelegateTargeted = false;
                    bl = bl3;
                    break;
                }
                case 1: 
                case 2: {
                    boolean bl5;
                    bl2 = bl5 = this.mDelegateTargeted;
                    bl = bl3;
                    if (!bl5) break;
                    bl2 = bl5;
                    bl = bl3;
                    if (this.mSlopBounds.contains(n, n2)) break;
                    bl = false;
                    bl2 = bl5;
                    break;
                }
                case 0: {
                    bl = bl3;
                    if (!this.mTargetBounds.contains(n, n2)) break;
                    this.mDelegateTargeted = true;
                    bl2 = true;
                    bl = bl3;
                }
            }
            if (bl2) {
                if (bl && !this.mActualBounds.contains(n, n2)) {
                    motionEvent.setLocation((float)(this.mDelegateView.getWidth() / 2), (float)(this.mDelegateView.getHeight() / 2));
                } else {
                    motionEvent.setLocation((float)(n - this.mActualBounds.left), (float)(n2 - this.mActualBounds.top));
                }
                bl4 = this.mDelegateView.dispatchTouchEvent(motionEvent);
            }
            return bl4;
        }

        public void setBounds(Rect rect, Rect rect2) {
            this.mTargetBounds.set(rect);
            this.mSlopBounds.set(rect);
            rect = this.mSlopBounds;
            int n = this.mSlop;
            rect.inset(-n, -n);
            this.mActualBounds.set(rect2);
        }
    }
}

