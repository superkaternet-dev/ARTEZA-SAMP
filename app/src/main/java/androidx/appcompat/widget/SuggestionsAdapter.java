/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.SearchableInfo
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.database.Cursor
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.text.SpannableString
 *  android.text.TextUtils
 *  android.text.style.TextAppearanceSpan
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package androidx.appcompat.widget;

import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.ResourceCursorAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.WeakHashMap;

class SuggestionsAdapter
extends ResourceCursorAdapter
implements View.OnClickListener {
    private static final boolean DBG = false;
    static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = "SuggestionsAdapter";
    private static final int QUERY_LIMIT = 50;
    static final int REFINE_ALL = 2;
    static final int REFINE_BY_ENTRY = 1;
    static final int REFINE_NONE = 0;
    private boolean mClosed = false;
    private final int mCommitIconResId;
    private int mFlagsCol = -1;
    private int mIconName1Col = -1;
    private int mIconName2Col = -1;
    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
    private final Context mProviderContext;
    private int mQueryRefinement = 1;
    private final SearchView mSearchView;
    private final SearchableInfo mSearchable;
    private int mText1Col = -1;
    private int mText2Col = -1;
    private int mText2UrlCol = -1;
    private ColorStateList mUrlColor;

    public SuggestionsAdapter(Context context, SearchView searchView, SearchableInfo searchableInfo, WeakHashMap<String, Drawable.ConstantState> weakHashMap) {
        super(context, searchView.getSuggestionRowLayout(), null, true);
        this.mSearchView = searchView;
        this.mSearchable = searchableInfo;
        this.mCommitIconResId = searchView.getSuggestionCommitIconResId();
        this.mProviderContext = context;
        this.mOutsideDrawablesCache = weakHashMap;
    }

    private Drawable checkIconCache(String string2) {
        if ((string2 = this.mOutsideDrawablesCache.get(string2)) == null) {
            return null;
        }
        return string2.newDrawable();
    }

    private CharSequence formatUrl(CharSequence charSequence) {
        TypedValue typedValue;
        if (this.mUrlColor == null) {
            typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, typedValue, true);
            this.mUrlColor = this.mContext.getResources().getColorStateList(typedValue.resourceId);
        }
        typedValue = new SpannableString(charSequence);
        typedValue.setSpan((Object)new TextAppearanceSpan(null, 0, 0, this.mUrlColor, null), 0, charSequence.length(), 33);
        return typedValue;
    }

    private Drawable getActivityIcon(ComponentName componentName) {
        ActivityInfo activityInfo;
        Object object = this.mContext.getPackageManager();
        try {
            activityInfo = object.getActivityInfo(componentName, 128);
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.w((String)LOG_TAG, (String)nameNotFoundException.toString());
            return null;
        }
        int n = activityInfo.getIconResource();
        if (n == 0) {
            return null;
        }
        if ((object = object.getDrawable(componentName.getPackageName(), n, activityInfo.applicationInfo)) == null) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Invalid icon resource ");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" for ");
            ((StringBuilder)object).append(componentName.flattenToShortString());
            Log.w((String)LOG_TAG, (String)((StringBuilder)object).toString());
            return null;
        }
        return object;
    }

    private Drawable getActivityIconWithCache(ComponentName object) {
        String string2 = object.flattenToShortString();
        boolean bl = this.mOutsideDrawablesCache.containsKey(string2);
        Object var4_4 = null;
        Drawable drawable2 = null;
        if (bl) {
            object = this.mOutsideDrawablesCache.get(string2);
            object = object == null ? drawable2 : object.newDrawable(this.mProviderContext.getResources());
            return object;
        }
        drawable2 = this.getActivityIcon((ComponentName)object);
        object = drawable2 == null ? var4_4 : drawable2.getConstantState();
        this.mOutsideDrawablesCache.put(string2, (Drawable.ConstantState)object);
        return drawable2;
    }

    public static String getColumnString(Cursor cursor, String string2) {
        return SuggestionsAdapter.getStringOrNull(cursor, cursor.getColumnIndex(string2));
    }

    private Drawable getDefaultIcon1() {
        Drawable drawable2 = this.getActivityIconWithCache(this.mSearchable.getSearchActivity());
        if (drawable2 != null) {
            return drawable2;
        }
        return this.mContext.getPackageManager().getDefaultActivityIcon();
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Drawable getDrawable(Uri var1_1) {
        try {
            var2_2 = "android.resource".equals(var1_1.getScheme());
            if (var2_2) {
                try {
                    return this.getDrawableFromResourceUri(var1_1);
                }
                catch (Resources.NotFoundException var3_4) {
                    var4_10 = new StringBuilder();
                    var4_10.append("Resource does not exist: ");
                    var4_10.append(var1_1);
                    var3_5 = new FileNotFoundException(var4_10.toString());
                    throw var3_5;
                }
            }
            var4_11 = this.mProviderContext.getContentResolver().openInputStream(var1_1);
            if (var4_11 != null) {
            }
            ** GOTO lbl59
        }
        catch (FileNotFoundException var3_9) {
            var4_11 = new StringBuilder();
            var4_11.append("Icon not found: ");
            var4_11.append(var1_1);
            var4_11.append(", ");
            var4_11.append(var3_9.getMessage());
            Log.w((String)"SuggestionsAdapter", (String)var4_11.toString());
            return null;
        }
        var3_6 = Drawable.createFromStream((InputStream)var4_11, null);
        {
            catch (Throwable var3_7) {
                try {
                    var4_11.close();
                    throw var3_7;
                }
                catch (IOException var5_13) {
                    var4_11 = new StringBuilder();
                    var4_11.append("Error closing icon stream for ");
                    var4_11.append(var1_1);
                    Log.e((String)"SuggestionsAdapter", (String)var4_11.toString(), (Throwable)var5_13);
                }
                throw var3_7;
            }
            try {
                var4_11.close();
                return var3_6;
            }
            catch (IOException var5_12) {
                var4_11 = new StringBuilder();
                var4_11.append("Error closing icon stream for ");
                var4_11.append(var1_1);
                Log.e((String)"SuggestionsAdapter", (String)var4_11.toString(), (Throwable)var5_12);
            }
            return var3_6;
lbl59:
            // 1 sources

            var3_8 = new StringBuilder();
            var3_8.append("Failed to open ");
            var3_8.append(var1_1);
            var4_11 = new FileNotFoundException(var3_8.toString());
            throw var4_11;
        }
    }

    private Drawable getDrawableFromResourceValue(String string2) {
        if (string2 != null && !string2.isEmpty() && !"0".equals(string2)) {
            Drawable drawable2;
            CharSequence charSequence;
            int n;
            block6: {
                n = Integer.parseInt(string2);
                charSequence = new StringBuilder();
                charSequence.append("android.resource://");
                charSequence.append(this.mProviderContext.getPackageName());
                charSequence.append("/");
                charSequence.append(n);
                charSequence = charSequence.toString();
                drawable2 = this.checkIconCache((String)charSequence);
                if (drawable2 == null) break block6;
                return drawable2;
            }
            try {
                drawable2 = ContextCompat.getDrawable(this.mProviderContext, n);
                this.storeInIconCache((String)charSequence, drawable2);
                return drawable2;
            }
            catch (Resources.NotFoundException notFoundException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Icon resource not found: ");
                stringBuilder.append(string2);
                Log.w((String)LOG_TAG, (String)stringBuilder.toString());
                return null;
            }
            catch (NumberFormatException numberFormatException) {
                Drawable drawable3 = this.checkIconCache(string2);
                if (drawable3 != null) {
                    return drawable3;
                }
                drawable3 = this.getDrawable(Uri.parse((String)string2));
                this.storeInIconCache(string2, drawable3);
                return drawable3;
            }
        }
        return null;
    }

    private Drawable getIcon1(Cursor cursor) {
        int n = this.mIconName1Col;
        if (n == -1) {
            return null;
        }
        if ((cursor = this.getDrawableFromResourceValue(cursor.getString(n))) != null) {
            return cursor;
        }
        return this.getDefaultIcon1();
    }

    private Drawable getIcon2(Cursor cursor) {
        int n = this.mIconName2Col;
        if (n == -1) {
            return null;
        }
        return this.getDrawableFromResourceValue(cursor.getString(n));
    }

    private static String getStringOrNull(Cursor object, int n) {
        if (n == -1) {
            return null;
        }
        try {
            object = object.getString(n);
            return object;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"unexpected error retrieving valid column from cursor, did the remote process die?", (Throwable)exception);
            return null;
        }
    }

    private void setViewDrawable(ImageView imageView, Drawable drawable2, int n) {
        imageView.setImageDrawable(drawable2);
        if (drawable2 == null) {
            imageView.setVisibility(n);
        } else {
            imageView.setVisibility(0);
            drawable2.setVisible(false, false);
            drawable2.setVisible(true, false);
        }
    }

    private void setViewText(TextView textView, CharSequence charSequence) {
        textView.setText(charSequence);
        if (TextUtils.isEmpty((CharSequence)charSequence)) {
            textView.setVisibility(8);
        } else {
            textView.setVisibility(0);
        }
    }

    private void storeInIconCache(String string2, Drawable drawable2) {
        if (drawable2 != null) {
            this.mOutsideDrawablesCache.put(string2, drawable2.getConstantState());
        }
    }

    private void updateSpinnerState(Cursor object) {
        if ((object = object != null ? object.getExtras() : null) != null) {
            object.getBoolean("in_progress");
            return;
        }
    }

    @Override
    public void bindView(View object, Context object2, Cursor cursor) {
        object2 = (ChildViewCache)object.getTag();
        int n = 0;
        int n2 = this.mFlagsCol;
        if (n2 != -1) {
            n = cursor.getInt(n2);
        }
        if (object2.mText1 != null) {
            object = SuggestionsAdapter.getStringOrNull(cursor, this.mText1Col);
            this.setViewText(object2.mText1, (CharSequence)object);
        }
        if (object2.mText2 != null) {
            object = SuggestionsAdapter.getStringOrNull(cursor, this.mText2UrlCol);
            object = object != null ? this.formatUrl((CharSequence)object) : SuggestionsAdapter.getStringOrNull(cursor, this.mText2Col);
            if (TextUtils.isEmpty((CharSequence)object)) {
                if (object2.mText1 != null) {
                    object2.mText1.setSingleLine(false);
                    object2.mText1.setMaxLines(2);
                }
            } else if (object2.mText1 != null) {
                object2.mText1.setSingleLine(true);
                object2.mText1.setMaxLines(1);
            }
            this.setViewText(object2.mText2, (CharSequence)object);
        }
        if (object2.mIcon1 != null) {
            this.setViewDrawable(object2.mIcon1, this.getIcon1(cursor), 4);
        }
        if (object2.mIcon2 != null) {
            this.setViewDrawable(object2.mIcon2, this.getIcon2(cursor), 8);
        }
        if ((n2 = this.mQueryRefinement) != 2 && (n2 != 1 || (n & 1) == 0)) {
            object2.mIconRefine.setVisibility(8);
        } else {
            object2.mIconRefine.setVisibility(0);
            object2.mIconRefine.setTag((Object)object2.mText1.getText());
            object2.mIconRefine.setOnClickListener((View.OnClickListener)this);
        }
    }

    @Override
    public void changeCursor(Cursor cursor) {
        block5: {
            if (this.mClosed) {
                Log.w((String)LOG_TAG, (String)"Tried to change cursor after adapter was closed.");
                if (cursor != null) {
                    cursor.close();
                }
                return;
            }
            super.changeCursor(cursor);
            if (cursor == null) break block5;
            try {
                this.mText1Col = cursor.getColumnIndex("suggest_text_1");
                this.mText2Col = cursor.getColumnIndex("suggest_text_2");
                this.mText2UrlCol = cursor.getColumnIndex("suggest_text_2_url");
                this.mIconName1Col = cursor.getColumnIndex("suggest_icon_1");
                this.mIconName2Col = cursor.getColumnIndex("suggest_icon_2");
                this.mFlagsCol = cursor.getColumnIndex("suggest_flags");
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"error changing cursor and caching columns", (Throwable)exception);
            }
        }
    }

    public void close() {
        this.changeCursor(null);
        this.mClosed = true;
    }

    @Override
    public CharSequence convertToString(Cursor object) {
        if (object == null) {
            return null;
        }
        String string2 = SuggestionsAdapter.getColumnString(object, "suggest_intent_query");
        if (string2 != null) {
            return string2;
        }
        if (this.mSearchable.shouldRewriteQueryFromData() && (string2 = SuggestionsAdapter.getColumnString(object, "suggest_intent_data")) != null) {
            return string2;
        }
        if (this.mSearchable.shouldRewriteQueryFromText() && (object = SuggestionsAdapter.getColumnString(object, "suggest_text_1")) != null) {
            return object;
        }
        return null;
    }

    Drawable getDrawableFromResourceUri(Uri uri) throws FileNotFoundException {
        block10: {
            Object object;
            block11: {
                block12: {
                    Resources resources;
                    String string2 = uri.getAuthority();
                    if (TextUtils.isEmpty((CharSequence)string2)) break block10;
                    try {
                        resources = this.mContext.getPackageManager().getResourcesForApplication(string2);
                    }
                    catch (PackageManager.NameNotFoundException nameNotFoundException) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("No package found for authority: ");
                        stringBuilder.append(uri);
                        throw new FileNotFoundException(stringBuilder.toString());
                    }
                    object = uri.getPathSegments();
                    if (object == null) break block11;
                    int n = object.size();
                    if (n == 1) {
                        try {
                            n = Integer.parseInt((String)object.get(0));
                        }
                        catch (NumberFormatException numberFormatException) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Single path segment is not a resource ID: ");
                            stringBuilder.append(uri);
                            throw new FileNotFoundException(stringBuilder.toString());
                        }
                    }
                    if (n != 2) break block12;
                    n = resources.getIdentifier((String)object.get(1), (String)object.get(0), string2);
                    if (n != 0) {
                        return resources.getDrawable(n);
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("No resource found for: ");
                    ((StringBuilder)object).append(uri);
                    throw new FileNotFoundException(((StringBuilder)object).toString());
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("More than two path segments: ");
                ((StringBuilder)object).append(uri);
                throw new FileNotFoundException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("No path: ");
            ((StringBuilder)object).append(uri);
            throw new FileNotFoundException(((StringBuilder)object).toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No authority: ");
        stringBuilder.append(uri);
        throw new FileNotFoundException(stringBuilder.toString());
    }

    @Override
    public View getDropDownView(int n, View view, ViewGroup viewGroup) {
        try {
            view = super.getDropDownView(n, view, viewGroup);
            return view;
        }
        catch (RuntimeException runtimeException) {
            Log.w((String)LOG_TAG, (String)"Search suggestions cursor threw exception.", (Throwable)runtimeException);
            viewGroup = this.newDropDownView(this.mContext, this.mCursor, viewGroup);
            if (viewGroup != null) {
                ((ChildViewCache)viewGroup.getTag()).mText1.setText((CharSequence)runtimeException.toString());
            }
            return viewGroup;
        }
    }

    public int getQueryRefinement() {
        return this.mQueryRefinement;
    }

    Cursor getSearchManagerSuggestions(SearchableInfo stringArray, String string2, int n) {
        if (stringArray == null) {
            return null;
        }
        String string3 = stringArray.getSuggestAuthority();
        if (string3 == null) {
            return null;
        }
        string3 = new Uri.Builder().scheme("content").authority(string3).query("").fragment("");
        String string4 = stringArray.getSuggestPath();
        if (string4 != null) {
            string3.appendEncodedPath(string4);
        }
        string3.appendPath("search_suggest_query");
        string4 = stringArray.getSuggestSelection();
        if (string4 != null) {
            stringArray = new String[]{string2};
        } else {
            string3.appendPath(string2);
            stringArray = null;
        }
        if (n > 0) {
            string3.appendQueryParameter("limit", String.valueOf(n));
        }
        string2 = string3.build();
        return this.mContext.getContentResolver().query((Uri)string2, null, string4, stringArray, null);
    }

    @Override
    public View getView(int n, View view, ViewGroup viewGroup) {
        try {
            view = super.getView(n, view, viewGroup);
            return view;
        }
        catch (RuntimeException runtimeException) {
            Log.w((String)LOG_TAG, (String)"Search suggestions cursor threw exception.", (Throwable)runtimeException);
            viewGroup = this.newView(this.mContext, this.mCursor, viewGroup);
            if (viewGroup != null) {
                ((ChildViewCache)viewGroup.getTag()).mText1.setText((CharSequence)runtimeException.toString());
            }
            return viewGroup;
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        context = super.newView(context, cursor, viewGroup);
        context.setTag((Object)new ChildViewCache((View)context));
        ((ImageView)context.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
        return context;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.updateSpinnerState(this.getCursor());
    }

    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        this.updateSpinnerState(this.getCursor());
    }

    public void onClick(View object) {
        if ((object = object.getTag()) instanceof CharSequence) {
            this.mSearchView.onQueryRefine((CharSequence)object);
        }
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence charSequence) {
        charSequence = charSequence == null ? "" : charSequence.toString();
        if (this.mSearchView.getVisibility() == 0 && this.mSearchView.getWindowVisibility() == 0) {
            block4: {
                charSequence = this.getSearchManagerSuggestions(this.mSearchable, (String)charSequence, 50);
                if (charSequence == null) break block4;
                try {
                    charSequence.getCount();
                    return charSequence;
                }
                catch (RuntimeException runtimeException) {
                    Log.w((String)LOG_TAG, (String)"Search suggestions query threw an exception.", (Throwable)runtimeException);
                }
            }
            return null;
        }
        return null;
    }

    public void setQueryRefinement(int n) {
        this.mQueryRefinement = n;
    }

    private static final class ChildViewCache {
        public final ImageView mIcon1;
        public final ImageView mIcon2;
        public final ImageView mIconRefine;
        public final TextView mText1;
        public final TextView mText2;

        public ChildViewCache(View view) {
            this.mText1 = (TextView)view.findViewById(16908308);
            this.mText2 = (TextView)view.findViewById(16908309);
            this.mIcon1 = (ImageView)view.findViewById(16908295);
            this.mIcon2 = (ImageView)view.findViewById(16908296);
            this.mIconRefine = (ImageView)view.findViewById(R.id.edit_query);
        }
    }
}

