/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.text.Spannable
 *  android.text.SpannableString
 *  android.text.method.LinkMovementMethod
 *  android.text.style.URLSpan
 *  android.text.util.Linkify
 *  android.text.util.Linkify$MatchFilter
 *  android.text.util.Linkify$TransformFilter
 *  android.webkit.WebView
 *  android.widget.TextView
 */
package androidx.core.text.util;

import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.core.text.util.FindAddress;
import androidx.core.util.PatternsCompat;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
    private static final Comparator<LinkSpec> COMPARATOR;
    private static final String[] EMPTY_STRING;

    static {
        EMPTY_STRING = new String[0];
        COMPARATOR = new Comparator<LinkSpec>(){

            @Override
            public int compare(LinkSpec linkSpec, LinkSpec linkSpec2) {
                if (linkSpec.start < linkSpec2.start) {
                    return -1;
                }
                if (linkSpec.start > linkSpec2.start) {
                    return 1;
                }
                if (linkSpec.end < linkSpec2.end) {
                    return 1;
                }
                if (linkSpec.end > linkSpec2.end) {
                    return -1;
                }
                return 0;
            }
        };
    }

    private LinkifyCompat() {
    }

    private static void addLinkMovementMethod(TextView textView) {
        if (!(textView.getMovementMethod() instanceof LinkMovementMethod) && textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public static void addLinks(TextView textView, Pattern pattern, String string2) {
        if (LinkifyCompat.shouldAddLinksFallbackToFramework()) {
            Linkify.addLinks((TextView)textView, (Pattern)pattern, (String)string2);
            return;
        }
        LinkifyCompat.addLinks(textView, pattern, string2, null, null, null);
    }

    public static void addLinks(TextView textView, Pattern pattern, String string2, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        if (LinkifyCompat.shouldAddLinksFallbackToFramework()) {
            Linkify.addLinks((TextView)textView, (Pattern)pattern, (String)string2, (Linkify.MatchFilter)matchFilter, (Linkify.TransformFilter)transformFilter);
            return;
        }
        LinkifyCompat.addLinks(textView, pattern, string2, null, matchFilter, transformFilter);
    }

    public static void addLinks(TextView textView, Pattern pattern, String string2, String[] stringArray, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        if (LinkifyCompat.shouldAddLinksFallbackToFramework()) {
            Linkify.addLinks((TextView)textView, (Pattern)pattern, (String)string2, (String[])stringArray, (Linkify.MatchFilter)matchFilter, (Linkify.TransformFilter)transformFilter);
            return;
        }
        SpannableString spannableString = SpannableString.valueOf((CharSequence)textView.getText());
        if (LinkifyCompat.addLinks((Spannable)spannableString, pattern, string2, stringArray, matchFilter, transformFilter)) {
            textView.setText((CharSequence)spannableString);
            LinkifyCompat.addLinkMovementMethod(textView);
        }
    }

    public static boolean addLinks(Spannable spannable, int n) {
        if (LinkifyCompat.shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks((Spannable)spannable, (int)n);
        }
        if (n == 0) {
            return false;
        }
        Object object = (URLSpan[])spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (int i = ((URLSpan[])object).length - 1; i >= 0; --i) {
            spannable.removeSpan((Object)object[i]);
        }
        if ((n & 4) != 0) {
            Linkify.addLinks((Spannable)spannable, (int)4);
        }
        Object object2 = new ArrayList<LinkSpec>();
        if ((n & 1) != 0) {
            object = PatternsCompat.AUTOLINK_WEB_URL;
            Linkify.MatchFilter matchFilter = Linkify.sUrlMatchFilter;
            LinkifyCompat.gatherLinks(object2, spannable, (Pattern)object, new String[]{"http://", "https://", "rtsp://"}, matchFilter, null);
        }
        if ((n & 2) != 0) {
            LinkifyCompat.gatherLinks(object2, spannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{"mailto:"}, null, null);
        }
        if ((n & 8) != 0) {
            LinkifyCompat.gatherMapLinks(object2, spannable);
        }
        LinkifyCompat.pruneOverlaps(object2, spannable);
        if (((ArrayList)object2).size() == 0) {
            return false;
        }
        object2 = ((ArrayList)object2).iterator();
        while (object2.hasNext()) {
            object = (LinkSpec)object2.next();
            if (object.frameworkAddedSpan != null) continue;
            LinkifyCompat.applyLink(object.url, object.start, object.end, spannable);
        }
        return true;
    }

    public static boolean addLinks(Spannable spannable, Pattern pattern, String string2) {
        if (LinkifyCompat.shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks((Spannable)spannable, (Pattern)pattern, (String)string2);
        }
        return LinkifyCompat.addLinks(spannable, pattern, string2, null, null, null);
    }

    public static boolean addLinks(Spannable spannable, Pattern pattern, String string2, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        if (LinkifyCompat.shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks((Spannable)spannable, (Pattern)pattern, (String)string2, (Linkify.MatchFilter)matchFilter, (Linkify.TransformFilter)transformFilter);
        }
        return LinkifyCompat.addLinks(spannable, pattern, string2, null, matchFilter, transformFilter);
    }

    public static boolean addLinks(Spannable spannable, Pattern object, String stringArray, String[] object2, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        int n;
        String[] stringArray2;
        block9: {
            block8: {
                if (LinkifyCompat.shouldAddLinksFallbackToFramework()) {
                    return Linkify.addLinks((Spannable)spannable, (Pattern)object, (String)stringArray, (String[])object2, (Linkify.MatchFilter)matchFilter, (Linkify.TransformFilter)transformFilter);
                }
                stringArray2 = stringArray;
                if (stringArray == null) {
                    stringArray2 = "";
                }
                if (object2 == null) break block8;
                stringArray = object2;
                if (((String[])object2).length >= 1) break block9;
            }
            stringArray = EMPTY_STRING;
        }
        String[] stringArray3 = new String[stringArray.length + 1];
        stringArray3[0] = stringArray2.toLowerCase(Locale.ROOT);
        for (n = 0; n < stringArray.length; ++n) {
            object2 = stringArray[n];
            object2 = object2 == null ? "" : object2.toLowerCase(Locale.ROOT);
            stringArray3[n + 1] = object2;
        }
        boolean bl = false;
        object = ((Pattern)object).matcher((CharSequence)spannable);
        while (((Matcher)object).find()) {
            n = ((Matcher)object).start();
            int n2 = ((Matcher)object).end();
            boolean bl2 = true;
            if (matchFilter != null) {
                bl2 = matchFilter.acceptMatch((CharSequence)spannable, n, n2);
            }
            if (!bl2) continue;
            LinkifyCompat.applyLink(LinkifyCompat.makeUrl(((Matcher)object).group(0), stringArray3, (Matcher)object, transformFilter), n, n2, spannable);
            bl = true;
        }
        return bl;
    }

    public static boolean addLinks(TextView textView, int n) {
        if (LinkifyCompat.shouldAddLinksFallbackToFramework()) {
            return Linkify.addLinks((TextView)textView, (int)n);
        }
        if (n == 0) {
            return false;
        }
        CharSequence charSequence = textView.getText();
        if (charSequence instanceof Spannable) {
            if (LinkifyCompat.addLinks((Spannable)charSequence, n)) {
                LinkifyCompat.addLinkMovementMethod(textView);
                return true;
            }
            return false;
        }
        if (LinkifyCompat.addLinks((Spannable)(charSequence = SpannableString.valueOf((CharSequence)charSequence)), n)) {
            LinkifyCompat.addLinkMovementMethod(textView);
            textView.setText(charSequence);
            return true;
        }
        return false;
    }

    private static void applyLink(String string2, int n, int n2, Spannable spannable) {
        spannable.setSpan((Object)new URLSpan(string2), n, n2, 33);
    }

    private static String findAddress(String string2) {
        if (Build.VERSION.SDK_INT >= 28) {
            return WebView.findAddress((String)string2);
        }
        return FindAddress.findAddress(string2);
    }

    private static void gatherLinks(ArrayList<LinkSpec> arrayList, Spannable spannable, Pattern object, String[] stringArray, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        object = ((Pattern)object).matcher((CharSequence)spannable);
        while (((Matcher)object).find()) {
            int n = ((Matcher)object).start();
            int n2 = ((Matcher)object).end();
            if (matchFilter != null && !matchFilter.acceptMatch((CharSequence)spannable, n, n2)) continue;
            LinkSpec linkSpec = new LinkSpec();
            linkSpec.url = LinkifyCompat.makeUrl(((Matcher)object).group(0), stringArray, (Matcher)object, transformFilter);
            linkSpec.start = n;
            linkSpec.end = n2;
            arrayList.add(linkSpec);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void gatherMapLinks(ArrayList<LinkSpec> arrayList, Spannable object) {
        object = object.toString();
        int n = 0;
        try {
            CharSequence charSequence;
            while ((charSequence = LinkifyCompat.findAddress((String)object)) != null) {
                String string2;
                int n2 = ((String)object).indexOf((String)charSequence);
                if (n2 < 0) {
                    return;
                }
                LinkSpec linkSpec = new LinkSpec();
                int n3 = n2 + ((String)charSequence).length();
                linkSpec.start = n + n2;
                linkSpec.end = n + n3;
                object = ((String)object).substring(n3);
                n += n3;
                try {
                    string2 = URLEncoder.encode((String)charSequence, "UTF-8");
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append("geo:0,0?q=");
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    continue;
                }
                ((StringBuilder)charSequence).append(string2);
                linkSpec.url = ((StringBuilder)charSequence).toString();
                arrayList.add(linkSpec);
            }
            return;
        }
        catch (UnsupportedOperationException unsupportedOperationException) {
            return;
        }
    }

    private static String makeUrl(String charSequence, String[] stringArray, Matcher object, Linkify.TransformFilter transformFilter) {
        boolean bl;
        String string2 = charSequence;
        if (transformFilter != null) {
            string2 = transformFilter.transformUrl((Matcher)object, (String)charSequence);
        }
        boolean bl2 = false;
        int n = 0;
        while (true) {
            bl = bl2;
            charSequence = string2;
            if (n >= stringArray.length) break;
            if (string2.regionMatches(true, 0, stringArray[n], 0, stringArray[n].length())) {
                bl = bl2 = true;
                charSequence = string2;
                if (string2.regionMatches(false, 0, stringArray[n], 0, stringArray[n].length())) break;
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(stringArray[n]);
                ((StringBuilder)charSequence).append(string2.substring(stringArray[n].length()));
                charSequence = ((StringBuilder)charSequence).toString();
                bl = bl2;
                break;
            }
            ++n;
        }
        object = charSequence;
        if (!bl) {
            object = charSequence;
            if (stringArray.length > 0) {
                object = new StringBuilder();
                ((StringBuilder)object).append(stringArray[0]);
                ((StringBuilder)object).append((String)charSequence);
                object = ((StringBuilder)object).toString();
            }
        }
        return object;
    }

    private static void pruneOverlaps(ArrayList<LinkSpec> arrayList, Spannable spannable) {
        LinkSpec linkSpec;
        int n;
        Object object = (URLSpan[])spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (n = 0; n < ((URLSpan[])object).length; ++n) {
            linkSpec = new LinkSpec();
            linkSpec.frameworkAddedSpan = object[n];
            linkSpec.start = spannable.getSpanStart((Object)object[n]);
            linkSpec.end = spannable.getSpanEnd((Object)object[n]);
            arrayList.add(linkSpec);
        }
        Collections.sort(arrayList, COMPARATOR);
        int n2 = arrayList.size();
        int n3 = 0;
        while (n3 < n2 - 1) {
            linkSpec = arrayList.get(n3);
            object = arrayList.get(n3 + 1);
            n = -1;
            if (linkSpec.start <= object.start && linkSpec.end > object.start) {
                if (object.end <= linkSpec.end) {
                    n = n3 + 1;
                } else if (linkSpec.end - linkSpec.start > object.end - object.start) {
                    n = n3 + 1;
                } else if (linkSpec.end - linkSpec.start < object.end - object.start) {
                    n = n3;
                }
                if (n != -1) {
                    linkSpec = arrayList.get((int)n).frameworkAddedSpan;
                    if (linkSpec != null) {
                        spannable.removeSpan((Object)linkSpec);
                    }
                    arrayList.remove(n);
                    --n2;
                    continue;
                }
            }
            ++n3;
        }
    }

    private static boolean shouldAddLinksFallbackToFramework() {
        boolean bl = Build.VERSION.SDK_INT >= 28;
        return bl;
    }

    private static class LinkSpec {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;

        LinkSpec() {
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface LinkifyMask {
    }
}

