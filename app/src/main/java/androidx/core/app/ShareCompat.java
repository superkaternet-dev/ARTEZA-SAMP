/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 *  android.text.Html
 *  android.text.Spanned
 *  android.util.Log
 *  android.view.ActionProvider
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.widget.ShareActionProvider
 */
package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import androidx.core.util.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;

public final class ShareCompat {
    public static final String EXTRA_CALLING_ACTIVITY = "androidx.core.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_ACTIVITY_INTEROP = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_PACKAGE = "androidx.core.app.EXTRA_CALLING_PACKAGE";
    public static final String EXTRA_CALLING_PACKAGE_INTEROP = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
    private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";

    private ShareCompat() {
    }

    public static void configureMenuItem(Menu object, int n, IntentBuilder intentBuilder) {
        if ((object = object.findItem(n)) != null) {
            ShareCompat.configureMenuItem((MenuItem)object, intentBuilder);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Could not find menu item with id ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append(" in the supplied menu");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder) {
        ActionProvider actionProvider = menuItem.getActionProvider();
        actionProvider = !(actionProvider instanceof ShareActionProvider) ? new ShareActionProvider(intentBuilder.getContext()) : (ShareActionProvider)actionProvider;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HISTORY_FILENAME_PREFIX);
        stringBuilder.append(intentBuilder.getContext().getClass().getName());
        actionProvider.setShareHistoryFileName(stringBuilder.toString());
        actionProvider.setShareIntent(intentBuilder.getIntent());
        menuItem.setActionProvider(actionProvider);
        if (Build.VERSION.SDK_INT < 16 && !menuItem.hasSubMenu()) {
            menuItem.setIntent(intentBuilder.createChooserIntent());
        }
    }

    public static ComponentName getCallingActivity(Activity activity) {
        Intent intent = activity.getIntent();
        ComponentName componentName = activity.getCallingActivity();
        activity = componentName;
        if (componentName == null) {
            activity = ShareCompat.getCallingActivity(intent);
        }
        return activity;
    }

    static ComponentName getCallingActivity(Intent intent) {
        ComponentName componentName;
        ComponentName componentName2 = componentName = (ComponentName)intent.getParcelableExtra(EXTRA_CALLING_ACTIVITY);
        if (componentName == null) {
            componentName2 = (ComponentName)intent.getParcelableExtra(EXTRA_CALLING_ACTIVITY_INTEROP);
        }
        return componentName2;
    }

    public static String getCallingPackage(Activity object) {
        Intent intent = object.getIntent();
        String string2 = object.getCallingPackage();
        object = string2;
        if (string2 == null) {
            object = string2;
            if (intent != null) {
                object = ShareCompat.getCallingPackage(intent);
            }
        }
        return object;
    }

    static String getCallingPackage(Intent intent) {
        String string2;
        String string3 = string2 = intent.getStringExtra(EXTRA_CALLING_PACKAGE);
        if (string2 == null) {
            string3 = intent.getStringExtra(EXTRA_CALLING_PACKAGE_INTEROP);
        }
        return string3;
    }

    public static class IntentBuilder {
        private ArrayList<String> mBccAddresses;
        private ArrayList<String> mCcAddresses;
        private CharSequence mChooserTitle;
        private final Context mContext;
        private final Intent mIntent;
        private ArrayList<Uri> mStreams;
        private ArrayList<String> mToAddresses;

        private IntentBuilder(Context context, ComponentName componentName) {
            Intent intent;
            this.mContext = Preconditions.checkNotNull(context);
            this.mIntent = intent = new Intent().setAction("android.intent.action.SEND");
            intent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE, context.getPackageName());
            intent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE_INTEROP, context.getPackageName());
            intent.putExtra(ShareCompat.EXTRA_CALLING_ACTIVITY, (Parcelable)componentName);
            intent.putExtra(ShareCompat.EXTRA_CALLING_ACTIVITY_INTEROP, (Parcelable)componentName);
            intent.addFlags(524288);
        }

        private void combineArrayExtra(String string2, ArrayList<String> arrayList) {
            String[] stringArray = this.mIntent.getStringArrayExtra(string2);
            int n = stringArray != null ? stringArray.length : 0;
            String[] stringArray2 = new String[arrayList.size() + n];
            arrayList.toArray(stringArray2);
            if (stringArray != null) {
                System.arraycopy(stringArray, 0, stringArray2, arrayList.size(), n);
            }
            this.mIntent.putExtra(string2, stringArray2);
        }

        private void combineArrayExtra(String string2, String[] stringArray) {
            Intent intent = this.getIntent();
            String[] stringArray2 = intent.getStringArrayExtra(string2);
            int n = stringArray2 != null ? stringArray2.length : 0;
            String[] stringArray3 = new String[stringArray.length + n];
            if (stringArray2 != null) {
                System.arraycopy(stringArray2, 0, stringArray3, 0, n);
            }
            System.arraycopy(stringArray, 0, stringArray3, n, stringArray.length);
            intent.putExtra(string2, stringArray3);
        }

        public static IntentBuilder from(Activity activity) {
            return IntentBuilder.from((Context)Preconditions.checkNotNull(activity), activity.getComponentName());
        }

        private static IntentBuilder from(Context context, ComponentName componentName) {
            return new IntentBuilder(context, componentName);
        }

        public IntentBuilder addEmailBcc(String string2) {
            if (this.mBccAddresses == null) {
                this.mBccAddresses = new ArrayList();
            }
            this.mBccAddresses.add(string2);
            return this;
        }

        public IntentBuilder addEmailBcc(String[] stringArray) {
            this.combineArrayExtra("android.intent.extra.BCC", stringArray);
            return this;
        }

        public IntentBuilder addEmailCc(String string2) {
            if (this.mCcAddresses == null) {
                this.mCcAddresses = new ArrayList();
            }
            this.mCcAddresses.add(string2);
            return this;
        }

        public IntentBuilder addEmailCc(String[] stringArray) {
            this.combineArrayExtra("android.intent.extra.CC", stringArray);
            return this;
        }

        public IntentBuilder addEmailTo(String string2) {
            if (this.mToAddresses == null) {
                this.mToAddresses = new ArrayList();
            }
            this.mToAddresses.add(string2);
            return this;
        }

        public IntentBuilder addEmailTo(String[] stringArray) {
            this.combineArrayExtra("android.intent.extra.EMAIL", stringArray);
            return this;
        }

        public IntentBuilder addStream(Uri uri) {
            Uri uri2 = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            ArrayList<Uri> arrayList = this.mStreams;
            if (arrayList == null && uri2 == null) {
                return this.setStream(uri);
            }
            if (arrayList == null) {
                this.mStreams = new ArrayList();
            }
            if (uri2 != null) {
                this.mIntent.removeExtra("android.intent.extra.STREAM");
                this.mStreams.add(uri2);
            }
            this.mStreams.add(uri);
            return this;
        }

        public Intent createChooserIntent() {
            return Intent.createChooser((Intent)this.getIntent(), (CharSequence)this.mChooserTitle);
        }

        Context getContext() {
            return this.mContext;
        }

        public Intent getIntent() {
            ArrayList<String> arrayList = this.mToAddresses;
            if (arrayList != null) {
                this.combineArrayExtra("android.intent.extra.EMAIL", arrayList);
                this.mToAddresses = null;
            }
            if ((arrayList = this.mCcAddresses) != null) {
                this.combineArrayExtra("android.intent.extra.CC", arrayList);
                this.mCcAddresses = null;
            }
            if ((arrayList = this.mBccAddresses) != null) {
                this.combineArrayExtra("android.intent.extra.BCC", arrayList);
                this.mBccAddresses = null;
            }
            arrayList = this.mStreams;
            boolean bl = true;
            if (arrayList == null || arrayList.size() <= 1) {
                bl = false;
            }
            boolean bl2 = "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
            if (!bl && bl2) {
                this.mIntent.setAction("android.intent.action.SEND");
                arrayList = this.mStreams;
                if (arrayList != null && !arrayList.isEmpty()) {
                    this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)this.mStreams.get(0));
                } else {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                }
                this.mStreams = null;
            }
            if (bl && !bl2) {
                this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
                arrayList = this.mStreams;
                if (arrayList != null && !arrayList.isEmpty()) {
                    this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mStreams);
                } else {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                }
            }
            return this.mIntent;
        }

        public IntentBuilder setChooserTitle(int n) {
            return this.setChooserTitle(this.mContext.getText(n));
        }

        public IntentBuilder setChooserTitle(CharSequence charSequence) {
            this.mChooserTitle = charSequence;
            return this;
        }

        public IntentBuilder setEmailBcc(String[] stringArray) {
            this.mIntent.putExtra("android.intent.extra.BCC", stringArray);
            return this;
        }

        public IntentBuilder setEmailCc(String[] stringArray) {
            this.mIntent.putExtra("android.intent.extra.CC", stringArray);
            return this;
        }

        public IntentBuilder setEmailTo(String[] stringArray) {
            if (this.mToAddresses != null) {
                this.mToAddresses = null;
            }
            this.mIntent.putExtra("android.intent.extra.EMAIL", stringArray);
            return this;
        }

        public IntentBuilder setHtmlText(String string2) {
            this.mIntent.putExtra("android.intent.extra.HTML_TEXT", string2);
            if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
                this.setText((CharSequence)Html.fromHtml((String)string2));
            }
            return this;
        }

        public IntentBuilder setStream(Uri uri) {
            if (!"android.intent.action.SEND".equals(this.mIntent.getAction())) {
                this.mIntent.setAction("android.intent.action.SEND");
            }
            this.mStreams = null;
            this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)uri);
            return this;
        }

        public IntentBuilder setSubject(String string2) {
            this.mIntent.putExtra("android.intent.extra.SUBJECT", string2);
            return this;
        }

        public IntentBuilder setText(CharSequence charSequence) {
            this.mIntent.putExtra("android.intent.extra.TEXT", charSequence);
            return this;
        }

        public IntentBuilder setType(String string2) {
            this.mIntent.setType(string2);
            return this;
        }

        public void startChooser() {
            this.mContext.startActivity(this.createChooserIntent());
        }
    }

    public static class IntentReader {
        private static final String TAG = "IntentReader";
        private final ComponentName mCallingActivity;
        private final String mCallingPackage;
        private final Context mContext;
        private final Intent mIntent;
        private ArrayList<Uri> mStreams;

        private IntentReader(Context context, Intent intent) {
            this.mContext = Preconditions.checkNotNull(context);
            this.mIntent = Preconditions.checkNotNull(intent);
            this.mCallingPackage = ShareCompat.getCallingPackage(intent);
            this.mCallingActivity = ShareCompat.getCallingActivity(intent);
        }

        public static IntentReader from(Activity activity) {
            return IntentReader.from((Context)Preconditions.checkNotNull(activity), activity.getIntent());
        }

        private static IntentReader from(Context context, Intent intent) {
            return new IntentReader(context, intent);
        }

        private static void withinStyle(StringBuilder stringBuilder, CharSequence charSequence, int n, int n2) {
            while (n < n2) {
                char c = charSequence.charAt(n);
                if (c == '<') {
                    stringBuilder.append("&lt;");
                } else if (c == '>') {
                    stringBuilder.append("&gt;");
                } else if (c == '&') {
                    stringBuilder.append("&amp;");
                } else if (c <= '~' && c >= ' ') {
                    if (c == ' ') {
                        while (n + 1 < n2 && charSequence.charAt(n + 1) == ' ') {
                            stringBuilder.append("&nbsp;");
                            ++n;
                        }
                        stringBuilder.append(' ');
                    } else {
                        stringBuilder.append(c);
                    }
                } else {
                    stringBuilder.append("&#");
                    stringBuilder.append((int)c);
                    stringBuilder.append(";");
                }
                ++n;
            }
        }

        public ComponentName getCallingActivity() {
            return this.mCallingActivity;
        }

        public Drawable getCallingActivityIcon() {
            if (this.mCallingActivity == null) {
                return null;
            }
            PackageManager packageManager = this.mContext.getPackageManager();
            try {
                packageManager = packageManager.getActivityIcon(this.mCallingActivity);
                return packageManager;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.e((String)TAG, (String)"Could not retrieve icon for calling activity", (Throwable)nameNotFoundException);
                return null;
            }
        }

        public Drawable getCallingApplicationIcon() {
            if (this.mCallingPackage == null) {
                return null;
            }
            PackageManager packageManager = this.mContext.getPackageManager();
            try {
                packageManager = packageManager.getApplicationIcon(this.mCallingPackage);
                return packageManager;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.e((String)TAG, (String)"Could not retrieve icon for calling application", (Throwable)nameNotFoundException);
                return null;
            }
        }

        public CharSequence getCallingApplicationLabel() {
            if (this.mCallingPackage == null) {
                return null;
            }
            Object object = this.mContext.getPackageManager();
            try {
                object = object.getApplicationLabel(object.getApplicationInfo(this.mCallingPackage, 0));
                return object;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.e((String)TAG, (String)"Could not retrieve label for calling application", (Throwable)nameNotFoundException);
                return null;
            }
        }

        public String getCallingPackage() {
            return this.mCallingPackage;
        }

        public String[] getEmailBcc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
        }

        public String[] getEmailCc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
        }

        public String[] getEmailTo() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        }

        public String getHtmlText() {
            String string2 = this.mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
            CharSequence charSequence = string2;
            if (string2 == null) {
                CharSequence charSequence2 = this.getText();
                if (charSequence2 instanceof Spanned) {
                    charSequence = Html.toHtml((Spanned)((Spanned)charSequence2));
                } else {
                    charSequence = string2;
                    if (charSequence2 != null) {
                        if (Build.VERSION.SDK_INT >= 16) {
                            charSequence = Html.escapeHtml((CharSequence)charSequence2);
                        } else {
                            charSequence = new StringBuilder();
                            IntentReader.withinStyle((StringBuilder)charSequence, charSequence2, 0, charSequence2.length());
                            charSequence = ((StringBuilder)charSequence).toString();
                        }
                    }
                }
            }
            return charSequence;
        }

        public Uri getStream() {
            return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        }

        public Uri getStream(int n) {
            Serializable serializable;
            if (this.mStreams == null && this.isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if ((serializable = this.mStreams) != null) {
                return ((ArrayList)serializable).get(n);
            }
            if (n == 0) {
                return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            }
            serializable = new StringBuilder();
            ((StringBuilder)serializable).append("Stream items available: ");
            ((StringBuilder)serializable).append(this.getStreamCount());
            ((StringBuilder)serializable).append(" index requested: ");
            ((StringBuilder)serializable).append(n);
            throw new IndexOutOfBoundsException(((StringBuilder)serializable).toString());
        }

        public int getStreamCount() {
            ArrayList<Uri> arrayList;
            if (this.mStreams == null && this.isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if ((arrayList = this.mStreams) != null) {
                return arrayList.size();
            }
            return this.mIntent.hasExtra("android.intent.extra.STREAM") ? 1 : 0;
        }

        public String getSubject() {
            return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
        }

        public CharSequence getText() {
            return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
        }

        public String getType() {
            return this.mIntent.getType();
        }

        public boolean isMultipleShare() {
            return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
        }

        public boolean isShareIntent() {
            String string2 = this.mIntent.getAction();
            boolean bl = "android.intent.action.SEND".equals(string2) || "android.intent.action.SEND_MULTIPLE".equals(string2);
            return bl;
        }

        public boolean isSingleShare() {
            return "android.intent.action.SEND".equals(this.mIntent.getAction());
        }
    }
}

