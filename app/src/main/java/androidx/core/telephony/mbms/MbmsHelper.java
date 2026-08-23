/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.telephony.mbms.ServiceInfo
 */
package androidx.core.telephony.mbms;

import android.content.Context;
import android.os.Build;
import android.telephony.mbms.ServiceInfo;
import java.util.Iterator;
import java.util.Locale;

public final class MbmsHelper {
    private MbmsHelper() {
    }

    public static CharSequence getBestNameForService(Context object, ServiceInfo serviceInfo) {
        int n = Build.VERSION.SDK_INT;
        Object var3_3 = null;
        if (n < 28) {
            return null;
        }
        object = object.getResources().getConfiguration().getLocales();
        n = serviceInfo.getNamedContentLocales().size();
        if (n == 0) {
            return null;
        }
        String[] stringArray = new String[n];
        n = 0;
        Iterator iterator2 = serviceInfo.getNamedContentLocales().iterator();
        while (iterator2.hasNext()) {
            stringArray[n] = ((Locale)iterator2.next()).toLanguageTag();
            ++n;
        }
        object = (object = object.getFirstMatch(stringArray)) == null ? var3_3 : serviceInfo.getNameForLocale((Locale)object);
        return object;
    }
}

