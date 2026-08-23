/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Person
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ShortcutInfo
 *  android.content.pm.ShortcutInfo$Builder
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 *  android.os.PersistableBundle
 *  android.text.TextUtils
 */
package androidx.core.content.pm;

import android.app.Person;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.text.TextUtils;
import androidx.core.graphics.drawable.IconCompat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ShortcutInfoCompat {
    private static final String EXTRA_LONG_LIVED = "extraLongLived";
    private static final String EXTRA_PERSON_ = "extraPerson_";
    private static final String EXTRA_PERSON_COUNT = "extraPersonCount";
    ComponentName mActivity;
    Set<String> mCategories;
    Context mContext;
    CharSequence mDisabledMessage;
    IconCompat mIcon;
    String mId;
    Intent[] mIntents;
    boolean mIsAlwaysBadged;
    boolean mIsLongLived;
    CharSequence mLabel;
    CharSequence mLongLabel;
    androidx.core.app.Person[] mPersons;
    int mRank;

    ShortcutInfoCompat() {
    }

    private PersistableBundle buildLegacyExtrasBundle() {
        PersistableBundle persistableBundle = new PersistableBundle();
        Object object = this.mPersons;
        if (object != null && ((androidx.core.app.Person[])object).length > 0) {
            persistableBundle.putInt(EXTRA_PERSON_COUNT, ((androidx.core.app.Person[])object).length);
            for (int i = 0; i < this.mPersons.length; ++i) {
                object = new StringBuilder();
                ((StringBuilder)object).append(EXTRA_PERSON_);
                ((StringBuilder)object).append(i + 1);
                persistableBundle.putPersistableBundle(((StringBuilder)object).toString(), this.mPersons[i].toPersistableBundle());
            }
        }
        persistableBundle.putBoolean(EXTRA_LONG_LIVED, this.mIsLongLived);
        return persistableBundle;
    }

    static boolean getLongLivedFromExtra(PersistableBundle persistableBundle) {
        if (persistableBundle != null && persistableBundle.containsKey(EXTRA_LONG_LIVED)) {
            return persistableBundle.getBoolean(EXTRA_LONG_LIVED);
        }
        return false;
    }

    static androidx.core.app.Person[] getPersonsFromExtra(PersistableBundle persistableBundle) {
        if (persistableBundle != null && persistableBundle.containsKey(EXTRA_PERSON_COUNT)) {
            int n = persistableBundle.getInt(EXTRA_PERSON_COUNT);
            androidx.core.app.Person[] personArray = new androidx.core.app.Person[n];
            for (int i = 0; i < n; ++i) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(EXTRA_PERSON_);
                stringBuilder.append(i + 1);
                personArray[i] = androidx.core.app.Person.fromPersistableBundle(persistableBundle.getPersistableBundle(stringBuilder.toString()));
            }
            return personArray;
        }
        return null;
    }

    Intent addToIntent(Intent intent) {
        Drawable drawable2 = this.mIntents;
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)drawable2[((Intent[])drawable2).length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        if (this.mIcon != null) {
            Drawable drawable3 = null;
            Object var4_5 = null;
            if (this.mIsAlwaysBadged) {
                PackageManager packageManager = this.mContext.getPackageManager();
                drawable3 = this.mActivity;
                drawable2 = var4_5;
                if (drawable3 != null) {
                    try {
                        drawable2 = packageManager.getActivityIcon((ComponentName)drawable3);
                    }
                    catch (PackageManager.NameNotFoundException nameNotFoundException) {
                        drawable2 = var4_5;
                    }
                }
                drawable3 = drawable2;
                if (drawable2 == null) {
                    drawable3 = this.mContext.getApplicationInfo().loadIcon(packageManager);
                }
            }
            this.mIcon.addToShortcutIntent(intent, drawable3, this.mContext);
        }
        return intent;
    }

    public ComponentName getActivity() {
        return this.mActivity;
    }

    public Set<String> getCategories() {
        return this.mCategories;
    }

    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public String getId() {
        return this.mId;
    }

    public Intent getIntent() {
        Intent[] intentArray = this.mIntents;
        return intentArray[intentArray.length - 1];
    }

    public Intent[] getIntents() {
        Intent[] intentArray = this.mIntents;
        return Arrays.copyOf(intentArray, intentArray.length);
    }

    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }

    public int getRank() {
        return this.mRank;
    }

    public CharSequence getShortLabel() {
        return this.mLabel;
    }

    public ShortcutInfo toShortcutInfo() {
        ShortcutInfo.Builder builder = new ShortcutInfo.Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        Object object = this.mIcon;
        if (object != null) {
            builder.setIcon(object.toIcon(this.mContext));
        }
        if (!TextUtils.isEmpty((CharSequence)this.mLongLabel)) {
            builder.setLongLabel(this.mLongLabel);
        }
        if (!TextUtils.isEmpty((CharSequence)this.mDisabledMessage)) {
            builder.setDisabledMessage(this.mDisabledMessage);
        }
        if ((object = this.mActivity) != null) {
            builder.setActivity((ComponentName)object);
        }
        if ((object = this.mCategories) != null) {
            builder.setCategories((Set)object);
        }
        builder.setRank(this.mRank);
        if (Build.VERSION.SDK_INT >= 29) {
            object = this.mPersons;
            if (object != null && ((androidx.core.app.Person[])object).length > 0) {
                object = new Person[((androidx.core.app.Person[])object).length];
                for (int i = 0; i < ((androidx.core.app.Person[])object).length; ++i) {
                    object[i] = this.mPersons[i].toAndroidPerson();
                }
                builder.setPersons((Person[])object);
            }
            builder.setLongLived(this.mIsLongLived);
        } else {
            builder.setExtras(this.buildLegacyExtrasBundle());
        }
        return builder.build();
    }

    public static class Builder {
        private final ShortcutInfoCompat mInfo;

        public Builder(Context intentArray, ShortcutInfo shortcutInfo) {
            ShortcutInfoCompat shortcutInfoCompat;
            this.mInfo = shortcutInfoCompat = new ShortcutInfoCompat();
            shortcutInfoCompat.mContext = intentArray;
            shortcutInfoCompat.mId = shortcutInfo.getId();
            intentArray = shortcutInfo.getIntents();
            shortcutInfoCompat.mIntents = Arrays.copyOf(intentArray, intentArray.length);
            shortcutInfoCompat.mActivity = shortcutInfo.getActivity();
            shortcutInfoCompat.mLabel = shortcutInfo.getShortLabel();
            shortcutInfoCompat.mLongLabel = shortcutInfo.getLongLabel();
            shortcutInfoCompat.mDisabledMessage = shortcutInfo.getDisabledMessage();
            shortcutInfoCompat.mCategories = shortcutInfo.getCategories();
            shortcutInfoCompat.mPersons = ShortcutInfoCompat.getPersonsFromExtra(shortcutInfo.getExtras());
            shortcutInfoCompat.mRank = shortcutInfo.getRank();
        }

        public Builder(Context context, String string2) {
            ShortcutInfoCompat shortcutInfoCompat;
            this.mInfo = shortcutInfoCompat = new ShortcutInfoCompat();
            shortcutInfoCompat.mContext = context;
            shortcutInfoCompat.mId = string2;
        }

        public Builder(ShortcutInfoCompat shortcutInfoCompat) {
            ShortcutInfoCompat shortcutInfoCompat2;
            this.mInfo = shortcutInfoCompat2 = new ShortcutInfoCompat();
            shortcutInfoCompat2.mContext = shortcutInfoCompat.mContext;
            shortcutInfoCompat2.mId = shortcutInfoCompat.mId;
            shortcutInfoCompat2.mIntents = Arrays.copyOf(shortcutInfoCompat.mIntents, shortcutInfoCompat.mIntents.length);
            shortcutInfoCompat2.mActivity = shortcutInfoCompat.mActivity;
            shortcutInfoCompat2.mLabel = shortcutInfoCompat.mLabel;
            shortcutInfoCompat2.mLongLabel = shortcutInfoCompat.mLongLabel;
            shortcutInfoCompat2.mDisabledMessage = shortcutInfoCompat.mDisabledMessage;
            shortcutInfoCompat2.mIcon = shortcutInfoCompat.mIcon;
            shortcutInfoCompat2.mIsAlwaysBadged = shortcutInfoCompat.mIsAlwaysBadged;
            shortcutInfoCompat2.mIsLongLived = shortcutInfoCompat.mIsLongLived;
            shortcutInfoCompat2.mRank = shortcutInfoCompat.mRank;
            if (shortcutInfoCompat.mPersons != null) {
                shortcutInfoCompat2.mPersons = Arrays.copyOf(shortcutInfoCompat.mPersons, shortcutInfoCompat.mPersons.length);
            }
            if (shortcutInfoCompat.mCategories != null) {
                shortcutInfoCompat2.mCategories = new HashSet<String>(shortcutInfoCompat.mCategories);
            }
        }

        public ShortcutInfoCompat build() {
            if (!TextUtils.isEmpty((CharSequence)this.mInfo.mLabel)) {
                if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                    return this.mInfo;
                }
                throw new IllegalArgumentException("Shortcut must have an intent");
            }
            throw new IllegalArgumentException("Shortcut must have a non-empty label");
        }

        public Builder setActivity(ComponentName componentName) {
            this.mInfo.mActivity = componentName;
            return this;
        }

        public Builder setAlwaysBadged() {
            this.mInfo.mIsAlwaysBadged = true;
            return this;
        }

        public Builder setCategories(Set<String> set) {
            this.mInfo.mCategories = set;
            return this;
        }

        public Builder setDisabledMessage(CharSequence charSequence) {
            this.mInfo.mDisabledMessage = charSequence;
            return this;
        }

        public Builder setIcon(IconCompat iconCompat) {
            this.mInfo.mIcon = iconCompat;
            return this;
        }

        public Builder setIntent(Intent intent) {
            return this.setIntents(new Intent[]{intent});
        }

        public Builder setIntents(Intent[] intentArray) {
            this.mInfo.mIntents = intentArray;
            return this;
        }

        public Builder setLongLabel(CharSequence charSequence) {
            this.mInfo.mLongLabel = charSequence;
            return this;
        }

        @Deprecated
        public Builder setLongLived() {
            this.mInfo.mIsLongLived = true;
            return this;
        }

        public Builder setLongLived(boolean bl) {
            this.mInfo.mIsLongLived = bl;
            return this;
        }

        public Builder setPerson(androidx.core.app.Person person) {
            return this.setPersons(new androidx.core.app.Person[]{person});
        }

        public Builder setPersons(androidx.core.app.Person[] personArray) {
            this.mInfo.mPersons = personArray;
            return this;
        }

        public Builder setRank(int n) {
            this.mInfo.mRank = n;
            return this;
        }

        public Builder setShortLabel(CharSequence charSequence) {
            this.mInfo.mLabel = charSequence;
            return this;
        }
    }
}

