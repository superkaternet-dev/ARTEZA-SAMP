/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 */
package androidx.core.os;

import android.os.Build;
import android.os.LocaleList;
import androidx.core.os.LocaleListCompatWrapper;
import androidx.core.os.LocaleListInterface;
import androidx.core.os.LocaleListPlatformWrapper;
import java.util.Locale;

public final class LocaleListCompat {
    private static final LocaleListCompat sEmptyLocaleList = LocaleListCompat.create(new Locale[0]);
    private LocaleListInterface mImpl;

    private LocaleListCompat(LocaleListInterface localeListInterface) {
        this.mImpl = localeListInterface;
    }

    public static LocaleListCompat create(Locale ... localeArray) {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap(new LocaleList(localeArray));
        }
        return new LocaleListCompat(new LocaleListCompatWrapper(localeArray));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static Locale forLanguageTagCompat(String string2) {
        Object object;
        if (string2.contains("-")) {
            object = string2.split("-", -1);
            if (((String[])object).length > 2) {
                return new Locale(object[0], object[1], object[2]);
            }
            if (((String[])object).length > 1) {
                return new Locale(object[0], object[1]);
            }
            if (((String[])object).length == 1) {
                return new Locale(object[0]);
            }
        } else {
            if (!string2.contains("_")) return new Locale(string2);
            object = string2.split("_", -1);
            if (((String[])object).length > 2) {
                return new Locale(object[0], object[1], object[2]);
            }
            if (((String[])object).length > 1) {
                return new Locale(object[0], (String)object[1]);
            }
            if (((String[])object).length == 1) {
                return new Locale((String)object[0]);
            }
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Can not parse language tag: [");
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append("]");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static LocaleListCompat forLanguageTags(String object) {
        if (object != null && !((String)object).isEmpty()) {
            String[] stringArray = ((String)object).split(",", -1);
            Locale[] localeArray = new Locale[stringArray.length];
            for (int i = 0; i < localeArray.length; ++i) {
                object = Build.VERSION.SDK_INT >= 21 ? Locale.forLanguageTag(stringArray[i]) : LocaleListCompat.forLanguageTagCompat(stringArray[i]);
                localeArray[i] = object;
            }
            return LocaleListCompat.create(localeArray);
        }
        return LocaleListCompat.getEmptyLocaleList();
    }

    public static LocaleListCompat getAdjustedDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap(LocaleList.getAdjustedDefault());
        }
        return LocaleListCompat.create(Locale.getDefault());
    }

    public static LocaleListCompat getDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap(LocaleList.getDefault());
        }
        return LocaleListCompat.create(Locale.getDefault());
    }

    public static LocaleListCompat getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    public static LocaleListCompat wrap(LocaleList localeList) {
        return new LocaleListCompat(new LocaleListPlatformWrapper(localeList));
    }

    @Deprecated
    public static LocaleListCompat wrap(Object object) {
        return LocaleListCompat.wrap((LocaleList)object);
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof LocaleListCompat && this.mImpl.equals(((LocaleListCompat)object).mImpl);
        return bl;
    }

    public Locale get(int n) {
        return this.mImpl.get(n);
    }

    public Locale getFirstMatch(String[] stringArray) {
        return this.mImpl.getFirstMatch(stringArray);
    }

    public int hashCode() {
        return this.mImpl.hashCode();
    }

    public int indexOf(Locale locale) {
        return this.mImpl.indexOf(locale);
    }

    public boolean isEmpty() {
        return this.mImpl.isEmpty();
    }

    public int size() {
        return this.mImpl.size();
    }

    public String toLanguageTags() {
        return this.mImpl.toLanguageTags();
    }

    public String toString() {
        return this.mImpl.toString();
    }

    public Object unwrap() {
        return this.mImpl.getLocaleList();
    }
}

