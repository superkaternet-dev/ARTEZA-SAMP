/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util;

import com.google.android.gms.internal.common.zzo;
import com.google.android.gms.internal.common.zzx;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpUtils {
    private static final Pattern zza = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern zzb = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern zzc = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    private HttpUtils() {
    }

    public static Map<String, String> parse(URI list, String string2) {
        block3: {
            Map<String, String> map = Collections.emptyMap();
            Object object = ((URI)((Object)list)).getRawQuery();
            list = map;
            if (object != null) {
                list = map;
                if (((String)object).length() > 0) {
                    map = new HashMap<String, String>();
                    zzx zzx2 = zzx.zzc(zzo.zzb('='));
                    object = zzx.zzc(zzo.zzb('&')).zzb().zzd((CharSequence)object).iterator();
                    while (true) {
                        list = map;
                        if (!object.hasNext()) break block3;
                        list = zzx2.zzf((String)object.next());
                        if (list.isEmpty() || list.size() > 2) break;
                        String string3 = HttpUtils.zza((String)list.get(0), string2);
                        list = list.size() == 2 ? HttpUtils.zza((String)list.get(1), string2) : null;
                        map.put(string3, (String)((Object)list));
                    }
                    throw new IllegalArgumentException("bad parameter");
                }
            }
        }
        return list;
    }

    private static String zza(String string2, String string3) {
        String string4 = string3;
        if (string3 == null) {
            string4 = "ISO-8859-1";
        }
        try {
            string2 = URLDecoder.decode(string2, string4);
            return string2;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new IllegalArgumentException(unsupportedEncodingException);
        }
    }
}

