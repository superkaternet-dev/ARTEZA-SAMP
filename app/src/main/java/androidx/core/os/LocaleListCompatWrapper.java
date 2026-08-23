/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package androidx.core.os;

import android.os.Build;
import androidx.core.os.LocaleListCompat;
import androidx.core.os.LocaleListInterface;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

final class LocaleListCompatWrapper
implements LocaleListInterface {
    private static final Locale EN_LATN;
    private static final Locale LOCALE_AR_XB;
    private static final Locale LOCALE_EN_XA;
    private static final Locale[] sEmptyList;
    private final Locale[] mList;
    private final String mStringRepresentation;

    static {
        sEmptyList = new Locale[0];
        LOCALE_EN_XA = new Locale("en", "XA");
        LOCALE_AR_XB = new Locale("ar", "XB");
        EN_LATN = LocaleListCompat.forLanguageTagCompat("en-Latn");
    }

    LocaleListCompatWrapper(Locale ... object) {
        if (((Locale[])object).length == 0) {
            this.mList = sEmptyList;
            this.mStringRepresentation = "";
        } else {
            Locale[] localeArray = new Locale[((Locale[])object).length];
            HashSet<Locale> hashSet = new HashSet<Locale>();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < ((Locale[])object).length; ++i) {
                Locale locale = object[i];
                if (locale != null) {
                    if (!hashSet.contains(locale)) {
                        localeArray[i] = locale = (Locale)locale.clone();
                        LocaleListCompatWrapper.toLanguageTag(stringBuilder, locale);
                        if (i < ((Object)object).length - 1) {
                            stringBuilder.append(',');
                        }
                        hashSet.add(locale);
                        continue;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("list[");
                    ((StringBuilder)object).append(i);
                    ((StringBuilder)object).append("] is a repetition");
                    throw new IllegalArgumentException(((StringBuilder)object).toString());
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("list[");
                ((StringBuilder)object).append(i);
                ((StringBuilder)object).append("] is null");
                throw new NullPointerException(((StringBuilder)object).toString());
            }
            this.mList = localeArray;
            this.mStringRepresentation = stringBuilder.toString();
        }
    }

    private Locale computeFirstMatch(Collection<String> object, boolean bl) {
        int n = this.computeFirstMatchIndex((Collection<String>)object, bl);
        object = n == -1 ? null : this.mList[n];
        return object;
    }

    private int computeFirstMatchIndex(Collection<String> object, boolean bl) {
        int n;
        int n2;
        Locale[] localeArray = this.mList;
        if (localeArray.length == 1) {
            return 0;
        }
        if (localeArray.length == 0) {
            return -1;
        }
        int n3 = n2 = Integer.MAX_VALUE;
        if (bl) {
            n = this.findFirstMatchIndex(EN_LATN);
            if (n == 0) {
                return 0;
            }
            n3 = n2;
            if (n < Integer.MAX_VALUE) {
                n3 = n;
            }
        }
        object = object.iterator();
        n = n3;
        while (object.hasNext()) {
            n2 = this.findFirstMatchIndex(LocaleListCompat.forLanguageTagCompat((String)object.next()));
            if (n2 == 0) {
                return 0;
            }
            n3 = n;
            if (n2 < n) {
                n3 = n2;
            }
            n = n3;
        }
        if (n == Integer.MAX_VALUE) {
            return 0;
        }
        return n;
    }

    private int findFirstMatchIndex(Locale locale) {
        Locale[] localeArray;
        for (int i = 0; i < (localeArray = this.mList).length; ++i) {
            if (LocaleListCompatWrapper.matchScore(locale, localeArray[i]) <= 0) continue;
            return i;
        }
        return Integer.MAX_VALUE;
    }

    private static String getLikelyScript(Locale object) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (!((String)(object = ((Locale)object).getScript())).isEmpty()) {
                return object;
            }
            return "";
        }
        return "";
    }

    private static boolean isPseudoLocale(Locale locale) {
        boolean bl = LOCALE_EN_XA.equals(locale) || LOCALE_AR_XB.equals(locale);
        return bl;
    }

    private static int matchScore(Locale object, Locale locale) {
        boolean bl = ((Locale)object).equals(locale);
        int n = 1;
        if (bl) {
            return 1;
        }
        if (!((Locale)object).getLanguage().equals(locale.getLanguage())) {
            return 0;
        }
        if (!LocaleListCompatWrapper.isPseudoLocale((Locale)object) && !LocaleListCompatWrapper.isPseudoLocale(locale)) {
            String string2 = LocaleListCompatWrapper.getLikelyScript((Locale)object);
            if (string2.isEmpty()) {
                if (!((String)(object = ((Locale)object).getCountry())).isEmpty() && !((String)object).equals(locale.getCountry())) {
                    n = 0;
                }
                return n;
            }
            return string2.equals(LocaleListCompatWrapper.getLikelyScript(locale)) ? 1 : 0;
        }
        return 0;
    }

    static void toLanguageTag(StringBuilder stringBuilder, Locale locale) {
        stringBuilder.append(locale.getLanguage());
        String string2 = locale.getCountry();
        if (string2 != null && !string2.isEmpty()) {
            stringBuilder.append('-');
            stringBuilder.append(locale.getCountry());
        }
    }

    public boolean equals(Object localeArray) {
        if (localeArray == this) {
            return true;
        }
        if (!(localeArray instanceof LocaleListCompatWrapper)) {
            return false;
        }
        Locale[] localeArray2 = ((LocaleListCompatWrapper)localeArray).mList;
        if (this.mList.length != localeArray2.length) {
            return false;
        }
        for (int i = 0; i < (localeArray = this.mList).length; ++i) {
            if (localeArray[i].equals(localeArray2[i])) continue;
            return false;
        }
        return true;
    }

    @Override
    public Locale get(int n) {
        Object object;
        object = n >= 0 && n < ((Locale[])(object = this.mList)).length ? object[n] : null;
        return object;
    }

    @Override
    public Locale getFirstMatch(String[] stringArray) {
        return this.computeFirstMatch(Arrays.asList(stringArray), false);
    }

    @Override
    public Object getLocaleList() {
        return null;
    }

    public int hashCode() {
        Locale[] localeArray;
        int n = 1;
        for (int i = 0; i < (localeArray = this.mList).length; ++i) {
            n = n * 31 + localeArray[i].hashCode();
        }
        return n;
    }

    @Override
    public int indexOf(Locale locale) {
        Locale[] localeArray;
        for (int i = 0; i < (localeArray = this.mList).length; ++i) {
            if (!localeArray[i].equals(locale)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        boolean bl = this.mList.length == 0;
        return bl;
    }

    @Override
    public int size() {
        return this.mList.length;
    }

    @Override
    public String toLanguageTags() {
        return this.mStringRepresentation;
    }

    public String toString() {
        Locale[] localeArray;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < (localeArray = this.mList).length; ++i) {
            stringBuilder.append(localeArray[i]);
            if (i >= this.mList.length - 1) continue;
            stringBuilder.append(',');
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}

