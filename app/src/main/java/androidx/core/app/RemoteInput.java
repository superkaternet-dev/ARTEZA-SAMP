/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.RemoteInput
 *  android.app.RemoteInput$Builder
 *  android.content.ClipData
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 */
package androidx.core.app;

import android.app.RemoteInput;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class RemoteInput {
    public static final int EDIT_CHOICES_BEFORE_SENDING_AUTO = 0;
    public static final int EDIT_CHOICES_BEFORE_SENDING_DISABLED = 1;
    public static final int EDIT_CHOICES_BEFORE_SENDING_ENABLED = 2;
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    private static final String EXTRA_RESULTS_SOURCE = "android.remoteinput.resultsSource";
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    public static final int SOURCE_CHOICE = 1;
    public static final int SOURCE_FREE_FORM_INPUT = 0;
    private static final String TAG = "RemoteInput";
    private final boolean mAllowFreeFormTextInput;
    private final Set<String> mAllowedDataTypes;
    private final CharSequence[] mChoices;
    private final int mEditChoicesBeforeSending;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;

    RemoteInput(String string2, CharSequence charSequence, CharSequence[] charSequenceArray, boolean bl, int n, Bundle bundle, Set<String> set) {
        this.mResultKey = string2;
        this.mLabel = charSequence;
        this.mChoices = charSequenceArray;
        this.mAllowFreeFormTextInput = bl;
        this.mEditChoicesBeforeSending = n;
        this.mExtras = bundle;
        this.mAllowedDataTypes = set;
        if (this.getEditChoicesBeforeSending() == 2 && !this.getAllowFreeFormInput()) {
            throw new IllegalArgumentException("setEditChoicesBeforeSending requires setAllowFreeFormInput");
        }
    }

    /*
     * WARNING - void declaration
     */
    public static void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> object2) {
        if (Build.VERSION.SDK_INT >= 26) {
            android.app.RemoteInput.addDataResultToIntent((android.app.RemoteInput)RemoteInput.fromCompat(remoteInput), (Intent)intent, (Map)object2);
        } else if (Build.VERSION.SDK_INT >= 16) {
            Intent intent2;
            Intent intent3 = intent2 = RemoteInput.getClipDataIntentFromIntent(intent);
            if (intent2 == null) {
                intent3 = new Intent();
            }
            for (Map.Entry entry : object2.entrySet()) {
                void var2_7;
                String string2 = (String)entry.getKey();
                Uri uri = (Uri)entry.getValue();
                if (string2 == null) continue;
                Intent intent4 = intent2 = intent3.getBundleExtra(RemoteInput.getExtraResultsKeyForData(string2));
                if (intent2 == null) {
                    Bundle bundle = new Bundle();
                }
                var2_7.putString(remoteInput.getResultKey(), uri.toString());
                intent3.putExtra(RemoteInput.getExtraResultsKeyForData(string2), (Bundle)var2_7);
            }
            intent.setClipData(ClipData.newIntent((CharSequence)RESULTS_CLIP_LABEL, (Intent)intent3));
        }
    }

    /*
     * WARNING - void declaration
     */
    public static void addResultsToIntent(RemoteInput[] remoteInputArray, Intent intent, Bundle bundle) {
        block7: {
            void var6_10;
            Intent intent2;
            int n;
            int n2;
            block8: {
                block6: {
                    if (Build.VERSION.SDK_INT < 26) break block6;
                    android.app.RemoteInput.addResultsToIntent((android.app.RemoteInput[])RemoteInput.fromCompat(remoteInputArray), (Intent)intent, (Bundle)bundle);
                    break block7;
                }
                n2 = Build.VERSION.SDK_INT;
                n = 0;
                if (n2 < 20) break block8;
                Bundle object2 = RemoteInput.getResultsFromIntent(intent);
                int n3 = RemoteInput.getResultsSource(intent);
                if (object2 != null) {
                    object2.putAll(bundle);
                    bundle = object2;
                }
                for (RemoteInput remoteInput : remoteInputArray) {
                    Map<String, Uri> map = RemoteInput.getDataResultsFromIntent(intent, remoteInput.getResultKey());
                    android.app.RemoteInput.addResultsToIntent((android.app.RemoteInput[])RemoteInput.fromCompat(new RemoteInput[]{remoteInput}), (Intent)intent, (Bundle)bundle);
                    if (map == null) continue;
                    RemoteInput.addDataResultToIntent(remoteInput, intent, map);
                }
                RemoteInput.setResultsSource(intent, n3);
                break block7;
            }
            if (Build.VERSION.SDK_INT < 16) break block7;
            Intent intent3 = intent2 = RemoteInput.getClipDataIntentFromIntent(intent);
            if (intent2 == null) {
                Intent intent4 = new Intent();
            }
            Object object = var6_10.getBundleExtra(EXTRA_RESULTS_DATA);
            intent2 = object;
            if (object == null) {
                intent2 = new Bundle();
            }
            n2 = remoteInputArray.length;
            while (n < n2) {
                RemoteInput remoteInput = remoteInputArray[n];
                object = bundle.get(remoteInput.getResultKey());
                if (object instanceof CharSequence) {
                    intent2.putCharSequence(remoteInput.getResultKey(), (CharSequence)object);
                }
                ++n;
            }
            var6_10.putExtra(EXTRA_RESULTS_DATA, (Bundle)intent2);
            intent.setClipData(ClipData.newIntent((CharSequence)RESULTS_CLIP_LABEL, (Intent)var6_10));
        }
    }

    static android.app.RemoteInput fromCompat(RemoteInput remoteInput) {
        RemoteInput.Builder builder = new RemoteInput.Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras());
        if (Build.VERSION.SDK_INT >= 29) {
            builder.setEditChoicesBeforeSending(remoteInput.getEditChoicesBeforeSending());
        }
        return builder.build();
    }

    static android.app.RemoteInput[] fromCompat(RemoteInput[] remoteInputArray) {
        if (remoteInputArray == null) {
            return null;
        }
        android.app.RemoteInput[] remoteInputArray2 = new android.app.RemoteInput[remoteInputArray.length];
        for (int i = 0; i < remoteInputArray.length; ++i) {
            remoteInputArray2[i] = RemoteInput.fromCompat(remoteInputArray[i]);
        }
        return remoteInputArray2;
    }

    private static Intent getClipDataIntentFromIntent(Intent intent) {
        ClipData clipData = intent.getClipData();
        if (clipData == null) {
            return null;
        }
        intent = clipData.getDescription();
        if (!intent.hasMimeType("text/vnd.android.intent")) {
            return null;
        }
        if (!intent.getLabel().toString().contentEquals(RESULTS_CLIP_LABEL)) {
            return null;
        }
        return clipData.getItemAt(0).getIntent();
    }

    public static Map<String, Uri> getDataResultsFromIntent(Intent object, String string2) {
        if (Build.VERSION.SDK_INT >= 26) {
            return android.app.RemoteInput.getDataResultsFromIntent((Intent)object, (String)string2);
        }
        int n = Build.VERSION.SDK_INT;
        Object var3_3 = null;
        if (n >= 16) {
            Intent intent = RemoteInput.getClipDataIntentFromIntent((Intent)object);
            if (intent == null) {
                return null;
            }
            object = new HashMap();
            for (String string3 : intent.getExtras().keySet()) {
                String string4;
                if (!string3.startsWith(EXTRA_DATA_TYPE_RESULTS_DATA) || (string4 = string3.substring(EXTRA_DATA_TYPE_RESULTS_DATA.length())).isEmpty() || (string3 = intent.getBundleExtra(string3).getString(string2)) == null || string3.isEmpty()) continue;
                object.put(string4, Uri.parse((String)string3));
            }
            if (object.isEmpty()) {
                object = var3_3;
            }
            return object;
        }
        return null;
    }

    private static String getExtraResultsKeyForData(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EXTRA_DATA_TYPE_RESULTS_DATA);
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    public static Bundle getResultsFromIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= 20) {
            return android.app.RemoteInput.getResultsFromIntent((Intent)intent);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            if ((intent = RemoteInput.getClipDataIntentFromIntent(intent)) == null) {
                return null;
            }
            return (Bundle)intent.getExtras().getParcelable(EXTRA_RESULTS_DATA);
        }
        return null;
    }

    public static int getResultsSource(Intent intent) {
        if (Build.VERSION.SDK_INT >= 28) {
            return android.app.RemoteInput.getResultsSource((Intent)intent);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            if ((intent = RemoteInput.getClipDataIntentFromIntent(intent)) == null) {
                return 0;
            }
            return intent.getExtras().getInt(EXTRA_RESULTS_SOURCE, 0);
        }
        return 0;
    }

    public static void setResultsSource(Intent intent, int n) {
        if (Build.VERSION.SDK_INT >= 28) {
            android.app.RemoteInput.setResultsSource((Intent)intent, (int)n);
        } else if (Build.VERSION.SDK_INT >= 16) {
            Intent intent2;
            Intent intent3 = intent2 = RemoteInput.getClipDataIntentFromIntent(intent);
            if (intent2 == null) {
                intent3 = new Intent();
            }
            intent3.putExtra(EXTRA_RESULTS_SOURCE, n);
            intent.setClipData(ClipData.newIntent((CharSequence)RESULTS_CLIP_LABEL, (Intent)intent3));
        }
    }

    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormTextInput;
    }

    public Set<String> getAllowedDataTypes() {
        return this.mAllowedDataTypes;
    }

    public CharSequence[] getChoices() {
        return this.mChoices;
    }

    public int getEditChoicesBeforeSending() {
        return this.mEditChoicesBeforeSending;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public String getResultKey() {
        return this.mResultKey;
    }

    public boolean isDataOnly() {
        boolean bl = !this.getAllowFreeFormInput() && (this.getChoices() == null || this.getChoices().length == 0) && this.getAllowedDataTypes() != null && !this.getAllowedDataTypes().isEmpty();
        return bl;
    }

    public static final class Builder {
        private boolean mAllowFreeFormTextInput = true;
        private final Set<String> mAllowedDataTypes = new HashSet<String>();
        private CharSequence[] mChoices;
        private int mEditChoicesBeforeSending = 0;
        private final Bundle mExtras = new Bundle();
        private CharSequence mLabel;
        private final String mResultKey;

        public Builder(String string2) {
            if (string2 != null) {
                this.mResultKey = string2;
                return;
            }
            throw new IllegalArgumentException("Result key can't be null");
        }

        public Builder addExtras(Bundle bundle) {
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            return this;
        }

        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mEditChoicesBeforeSending, this.mExtras, this.mAllowedDataTypes);
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public Builder setAllowDataType(String string2, boolean bl) {
            if (bl) {
                this.mAllowedDataTypes.add(string2);
            } else {
                this.mAllowedDataTypes.remove(string2);
            }
            return this;
        }

        public Builder setAllowFreeFormInput(boolean bl) {
            this.mAllowFreeFormTextInput = bl;
            return this;
        }

        public Builder setChoices(CharSequence[] charSequenceArray) {
            this.mChoices = charSequenceArray;
            return this;
        }

        public Builder setEditChoicesBeforeSending(int n) {
            this.mEditChoicesBeforeSending = n;
            return this;
        }

        public Builder setLabel(CharSequence charSequence) {
            this.mLabel = charSequence;
            return this;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface EditChoicesBeforeSending {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Source {
    }
}

