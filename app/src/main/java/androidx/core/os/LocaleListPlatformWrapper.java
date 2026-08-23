/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.LocaleList
 */
package androidx.core.os;

import android.os.LocaleList;
import androidx.core.os.LocaleListInterface;
import java.util.Locale;

final class LocaleListPlatformWrapper
implements LocaleListInterface {
    private final LocaleList mLocaleList;

    LocaleListPlatformWrapper(LocaleList localeList) {
        this.mLocaleList = localeList;
    }

    public boolean equals(Object object) {
        return this.mLocaleList.equals(((LocaleListInterface)object).getLocaleList());
    }

    @Override
    public Locale get(int n) {
        return this.mLocaleList.get(n);
    }

    @Override
    public Locale getFirstMatch(String[] stringArray) {
        return this.mLocaleList.getFirstMatch(stringArray);
    }

    @Override
    public Object getLocaleList() {
        return this.mLocaleList;
    }

    public int hashCode() {
        return this.mLocaleList.hashCode();
    }

    @Override
    public int indexOf(Locale locale) {
        return this.mLocaleList.indexOf(locale);
    }

    @Override
    public boolean isEmpty() {
        return this.mLocaleList.isEmpty();
    }

    @Override
    public int size() {
        return this.mLocaleList.size();
    }

    @Override
    public String toLanguageTags() {
        return this.mLocaleList.toLanguageTags();
    }

    public String toString() {
        return this.mLocaleList.toString();
    }
}

