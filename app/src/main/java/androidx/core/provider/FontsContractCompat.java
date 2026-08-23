/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentUris
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ProviderInfo
 *  android.content.pm.Signature
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 *  android.os.Handler
 *  android.provider.BaseColumns
 */
package androidx.core.provider;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.provider.FontRequest;
import androidx.core.provider.SelfDestructiveThread;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FontsContractCompat {
    private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
    public static final String PARCEL_FONT_RESULTS = "font_results";
    static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final SelfDestructiveThread sBackgroundThread;
    private static final Comparator<byte[]> sByteArrayComparator;
    static final Object sLock;
    static final SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>> sPendingReplies;
    static final LruCache<String, Typeface> sTypefaceCache;

    static {
        sTypefaceCache = new LruCache(16);
        sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
        sLock = new Object();
        sPendingReplies = new SimpleArrayMap();
        sByteArrayComparator = new Comparator<byte[]>(){

            @Override
            public int compare(byte[] byArray, byte[] byArray2) {
                if (byArray.length != byArray2.length) {
                    return byArray.length - byArray2.length;
                }
                for (int i = 0; i < byArray.length; ++i) {
                    if (byArray[i] == byArray2[i]) continue;
                    return byArray[i] - byArray2[i];
                }
                return 0;
            }
        };
    }

    private FontsContractCompat() {
    }

    public static Typeface buildTypeface(Context context, CancellationSignal cancellationSignal, FontInfo[] fontInfoArray) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, fontInfoArray, 0);
    }

    private static List<byte[]> convertToByteArrayList(Signature[] signatureArray) {
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        for (int i = 0; i < signatureArray.length; ++i) {
            arrayList.add(signatureArray[i].toByteArray());
        }
        return arrayList;
    }

    private static boolean equalsByteArrayList(List<byte[]> list, List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (Arrays.equals(list.get(i), list2.get(i))) continue;
            return false;
        }
        return true;
    }

    public static FontFamilyResult fetchFonts(Context context, CancellationSignal cancellationSignal, FontRequest fontRequest) throws PackageManager.NameNotFoundException {
        ProviderInfo providerInfo = FontsContractCompat.getProvider(context.getPackageManager(), fontRequest, context.getResources());
        if (providerInfo == null) {
            return new FontFamilyResult(1, null);
        }
        return new FontFamilyResult(0, FontsContractCompat.getFontFromProvider(context, fontRequest, providerInfo.authority, cancellationSignal));
    }

    private static List<List<byte[]>> getCertificates(FontRequest fontRequest, Resources resources) {
        if (fontRequest.getCertificates() != null) {
            return fontRequest.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    static FontInfo[] getFontFromProvider(Context var0, FontRequest var1_1, String var2_7, CancellationSignal var3_8) {
        block37: {
            block27: {
                block29: {
                    block28: {
                        block26: {
                            block25: {
                                var14_9 = new ArrayList();
                                var16_10 = new Uri.Builder().scheme("content").authority((String)var2_7).build();
                                var17_11 = new Uri.Builder().scheme("content").authority((String)var2_7).appendPath("file").build();
                                var2_7 = null;
                                var15_12 = null;
                                var4_13 = Build.VERSION.SDK_INT;
                                if (var4_13 <= 16) break block25;
                                try {
                                    var0 /* !! */  = var0 /* !! */ .getContentResolver();
                                    var1_1 = var1_1.getQuery();
                                    var0 /* !! */  = var0 /* !! */ .query(var16_10, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var1_1}, null, (CancellationSignal)var3_8);
                                    break block26;
                                }
                                catch (Throwable var1_2) {
                                    var0 /* !! */  = var2_7;
                                    break block27;
                                }
                            }
                            var0 /* !! */  = var0 /* !! */ .getContentResolver();
                            var1_1 = var1_1.getQuery();
                            var2_7 = var15_12;
                            var0 /* !! */  = var0 /* !! */ .query(var16_10, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var1_1}, null);
                        }
                        if (var0 /* !! */  == null) break block28;
                        var2_7 = var0 /* !! */ ;
                        if (var0 /* !! */ .getCount() <= 0) break block28;
                        var2_7 = var0 /* !! */ ;
                        var10_14 = var0 /* !! */ .getColumnIndex("result_code");
                        var2_7 = var0 /* !! */ ;
                        var2_7 = var0 /* !! */ ;
                        var3_8 = new ArrayList();
                        try {
                            var12_15 = var0 /* !! */ .getColumnIndex("_id");
                            var11_16 = var0 /* !! */ .getColumnIndex("file_id");
                            var9_17 = var0 /* !! */ .getColumnIndex("font_ttc_index");
                            var8_18 = var0 /* !! */ .getColumnIndex("font_weight");
                            var7_19 = var0 /* !! */ .getColumnIndex("font_italic");
                        }
                        catch (Throwable var1_3) {}
                        while (true) {
                            var1_1 = var3_8;
                            if (!var0 /* !! */ .moveToNext()) break block29;
                            if (var10_14 == -1) break block30;
                            break;
                        }
                        {
                            block35: {
                                block34: {
                                    block33: {
                                        block32: {
                                            block36: {
                                                block31: {
                                                    block30: {
                                                        var4_13 = var0 /* !! */ .getInt(var10_14);
                                                        break block31;
                                                    }
                                                    var4_13 = 0;
                                                }
                                                if (var9_17 != -1) {
                                                    var5_20 = var0 /* !! */ .getInt(var9_17);
                                                } else {
                                                    var5_20 = 0;
                                                }
                                                if (var11_16 != -1) ** GOTO lbl63
                                                var1_1 = ContentUris.withAppendedId((Uri)var16_10, (long)var0 /* !! */ .getLong(var12_15));
                                                break block36;
lbl63:
                                                // 1 sources

                                                var1_1 = ContentUris.withAppendedId((Uri)var17_11, (long)var0 /* !! */ .getLong(var11_16));
                                            }
                                            if (var8_18 == -1) break block32;
                                            var6_21 = var0 /* !! */ .getInt(var8_18);
                                            break block33;
                                        }
                                        var6_21 = 400;
                                    }
                                    if (var7_19 != -1) {
                                        if (var0 /* !! */ .getInt(var7_19) != 1) break block34;
                                        var13_22 = true;
                                        break block35;
                                    }
                                }
                                var13_22 = false;
                            }
                            var2_7 = new FontInfo((Uri)var1_1, var5_20, var6_21, var13_22, var4_13);
                            var3_8.add(var2_7);
                            continue;
                        }
                        break block27;
                    }
                    var1_1 = var14_9;
                }
                if (var0 /* !! */  != null) {
                    var0 /* !! */ .close();
                }
                return var1_1.toArray(new FontInfo[0]);
                catch (Throwable var1_4) {
                    var0 /* !! */  = var2_7;
                }
                break block27;
                catch (Throwable var1_5) {
                    var0 /* !! */  = var2_7;
                }
            }
            if (var0 /* !! */  == null) break block37;
            var0 /* !! */ .close();
        }
        throw var1_6;
    }

    static TypefaceResult getFontInternal(Context context, FontRequest object, int n) {
        int n2;
        block4: {
            try {
                object = FontsContractCompat.fetchFonts(context, null, (FontRequest)object);
                int n3 = ((FontFamilyResult)object).getStatusCode();
                n2 = -3;
                if (n3 != 0) break block4;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                return new TypefaceResult(null, -1);
            }
            if ((context = TypefaceCompat.createFromFontInfo(context, null, ((FontFamilyResult)object).getFonts(), n)) != null) {
                n2 = 0;
            }
            return new TypefaceResult((Typeface)context, n2);
        }
        if (((FontFamilyResult)object).getStatusCode() == 1) {
            n2 = -2;
        }
        return new TypefaceResult(null, n2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Typeface getFontSync(Context object, FontRequest object2, ResourcesCompat.FontCallback object3, Handler object4, boolean bl, int n, int n2) {
        CharSequence charSequence = new StringBuilder();
        charSequence.append(((FontRequest)object2).getIdentifier());
        charSequence.append("-");
        charSequence.append(n2);
        charSequence = charSequence.toString();
        Object object5 = sTypefaceCache.get((String)charSequence);
        if (object5 != null) {
            if (object3 == null) return object5;
            ((ResourcesCompat.FontCallback)object3).onFontRetrieved((Typeface)object5);
            return object5;
        }
        if (bl && n == -1) {
            object = FontsContractCompat.getFontInternal(object, (FontRequest)object2, n2);
            if (object3 == null) return object.mTypeface;
            if (object.mResult == 0) {
                ((ResourcesCompat.FontCallback)object3).callbackSuccessAsync(object.mTypeface, (Handler)object4);
                return object.mTypeface;
            }
            ((ResourcesCompat.FontCallback)object3).callbackFailAsync(object.mResult, (Handler)object4);
            return object.mTypeface;
        }
        object2 = new Callable<TypefaceResult>(object, (FontRequest)object2, n2, (String)charSequence){
            final Context val$context;
            final String val$id;
            final FontRequest val$request;
            final int val$style;
            {
                this.val$context = context;
                this.val$request = fontRequest;
                this.val$style = n;
                this.val$id = string2;
            }

            @Override
            public TypefaceResult call() throws Exception {
                TypefaceResult typefaceResult = FontsContractCompat.getFontInternal(this.val$context, this.val$request, this.val$style);
                if (typefaceResult.mTypeface != null) {
                    sTypefaceCache.put(this.val$id, typefaceResult.mTypeface);
                }
                return typefaceResult;
            }
        };
        if (bl) {
            try {
                return ((TypefaceResult)FontsContractCompat.sBackgroundThread.postAndWait(object2, (int)n)).mTypeface;
            }
            catch (InterruptedException interruptedException) {
                return null;
            }
        }
        object = object3 == null ? null : new SelfDestructiveThread.ReplyCallback<TypefaceResult>((ResourcesCompat.FontCallback)object3, (Handler)object4){
            final ResourcesCompat.FontCallback val$fontCallback;
            final Handler val$handler;
            {
                this.val$fontCallback = fontCallback;
                this.val$handler = handler;
            }

            @Override
            public void onReply(TypefaceResult typefaceResult) {
                if (typefaceResult == null) {
                    this.val$fontCallback.callbackFailAsync(1, this.val$handler);
                } else if (typefaceResult.mResult == 0) {
                    this.val$fontCallback.callbackSuccessAsync(typefaceResult.mTypeface, this.val$handler);
                } else {
                    this.val$fontCallback.callbackFailAsync(typefaceResult.mResult, this.val$handler);
                }
            }
        };
        object3 = sLock;
        synchronized (object3) {
            object4 = sPendingReplies;
            object5 = (ArrayList)((SimpleArrayMap)object4).get(charSequence);
            if (object5 != null) {
                if (object == null) return null;
                ((ArrayList)object5).add(object);
                return null;
            }
            if (object != null) {
                object5 = new ArrayList();
                ((ArrayList)object5).add(object);
                ((SimpleArrayMap)object4).put(charSequence, object5);
            }
        }
        sBackgroundThread.postAndReply(object2, new SelfDestructiveThread.ReplyCallback<TypefaceResult>((String)charSequence){
            final String val$id;
            {
                this.val$id = string2;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onReply(TypefaceResult typefaceResult) {
                int n;
                ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>> arrayList;
                Object object = sLock;
                synchronized (object) {
                    arrayList = sPendingReplies.get(this.val$id);
                    if (arrayList == null) {
                        return;
                    }
                    sPendingReplies.remove(this.val$id);
                    // MONITOREXIT @DISABLED, blocks:[0, 2] lbl10 : MonitorExitStatement: MONITOREXIT : var3_3
                    n = 0;
                    {
                        catch (Throwable throwable) {}
                        {
                            throw throwable;
                        }
                    }
                }
                while (n < arrayList.size()) {
                    arrayList.get(n).onReply(typefaceResult);
                    ++n;
                }
            }
        });
        return null;
    }

    public static ProviderInfo getProvider(PackageManager object, FontRequest object2, Resources object3) throws PackageManager.NameNotFoundException {
        String string2 = ((FontRequest)object2).getProviderAuthority();
        ProviderInfo providerInfo = object.resolveContentProvider(string2, 0);
        if (providerInfo != null) {
            if (providerInfo.packageName.equals(((FontRequest)object2).getProviderPackage())) {
                object = FontsContractCompat.convertToByteArrayList(object.getPackageInfo((String)providerInfo.packageName, (int)64).signatures);
                Collections.sort(object, sByteArrayComparator);
                object2 = FontsContractCompat.getCertificates((FontRequest)object2, object3);
                for (int i = 0; i < object2.size(); ++i) {
                    object3 = new ArrayList((Collection)object2.get(i));
                    Collections.sort(object3, sByteArrayComparator);
                    if (!FontsContractCompat.equalsByteArrayList((List<byte[]>)object, (List<byte[]>)object3)) continue;
                    return providerInfo;
                }
                return null;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Found content provider ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(", but package was not ");
            ((StringBuilder)object).append(((FontRequest)object2).getProviderPackage());
            throw new PackageManager.NameNotFoundException(((StringBuilder)object).toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("No package found for authority: ");
        ((StringBuilder)object).append(string2);
        object = new PackageManager.NameNotFoundException(((StringBuilder)object).toString());
        throw object;
    }

    public static Map<Uri, ByteBuffer> prepareFontData(Context context, FontInfo[] fontInfoArray, CancellationSignal cancellationSignal) {
        HashMap<FontInfo, ByteBuffer> hashMap = new HashMap<FontInfo, ByteBuffer>();
        for (FontInfo fontInfo : fontInfoArray) {
            if (fontInfo.getResultCode() != 0 || hashMap.containsKey(fontInfo = fontInfo.getUri())) continue;
            hashMap.put(fontInfo, TypefaceCompatUtil.mmap(context, cancellationSignal, (Uri)fontInfo));
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public static void requestFont(Context context, FontRequest fontRequest, FontRequestCallback fontRequestCallback, Handler handler) {
        FontsContractCompat.requestFontInternal(context.getApplicationContext(), fontRequest, fontRequestCallback, handler);
    }

    private static void requestFontInternal(Context context, FontRequest fontRequest, FontRequestCallback fontRequestCallback, Handler handler) {
        handler.post(new Runnable(context, fontRequest, new Handler(), fontRequestCallback){
            final Context val$appContext;
            final FontRequestCallback val$callback;
            final Handler val$callerThreadHandler;
            final FontRequest val$request;
            {
                this.val$appContext = context;
                this.val$request = fontRequest;
                this.val$callerThreadHandler = handler;
                this.val$callback = fontRequestCallback;
            }

            @Override
            public void run() {
                Typeface typeface;
                block11: {
                    try {
                        typeface = FontsContractCompat.fetchFonts(this.val$appContext, null, this.val$request);
                        if (typeface.getStatusCode() == 0) break block11;
                    }
                    catch (PackageManager.NameNotFoundException nameNotFoundException) {
                        this.val$callerThreadHandler.post(new Runnable(this){
                            final 4 this$0;
                            {
                                this.this$0 = var1_1;
                            }

                            @Override
                            public void run() {
                                this.this$0.val$callback.onTypefaceRequestFailed(-1);
                            }
                        });
                        return;
                    }
                    switch (typeface.getStatusCode()) {
                        default: {
                            this.val$callerThreadHandler.post(new Runnable(this){
                                final 4 this$0;
                                {
                                    this.this$0 = var1_1;
                                }

                                @Override
                                public void run() {
                                    this.this$0.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        case 2: {
                            this.val$callerThreadHandler.post(new Runnable(this){
                                final 4 this$0;
                                {
                                    this.this$0 = var1_1;
                                }

                                @Override
                                public void run() {
                                    this.this$0.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        case 1: 
                    }
                    this.val$callerThreadHandler.post(new Runnable(this){
                        final 4 this$0;
                        {
                            this.this$0 = var1_1;
                        }

                        @Override
                        public void run() {
                            this.this$0.val$callback.onTypefaceRequestFailed(-2);
                        }
                    });
                    return;
                }
                if ((typeface = typeface.getFonts()) != null && ((FontInfo[])typeface).length != 0) {
                    int n = ((FontInfo[])typeface).length;
                    for (int i = 0; i < n; ++i) {
                        FontInfo fontInfo = typeface[i];
                        if (fontInfo.getResultCode() == 0) continue;
                        i = fontInfo.getResultCode();
                        if (i < 0) {
                            this.val$callerThreadHandler.post(new Runnable(this){
                                final 4 this$0;
                                {
                                    this.this$0 = var1_1;
                                }

                                @Override
                                public void run() {
                                    this.this$0.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                        } else {
                            this.val$callerThreadHandler.post(new Runnable(this, i){
                                final 4 this$0;
                                final int val$resultCode;
                                {
                                    this.this$0 = var1_1;
                                    this.val$resultCode = n;
                                }

                                @Override
                                public void run() {
                                    this.this$0.val$callback.onTypefaceRequestFailed(this.val$resultCode);
                                }
                            });
                        }
                        return;
                    }
                    if ((typeface = FontsContractCompat.buildTypeface(this.val$appContext, null, (FontInfo[])typeface)) == null) {
                        this.val$callerThreadHandler.post(new Runnable(this){
                            final 4 this$0;
                            {
                                this.this$0 = var1_1;
                            }

                            @Override
                            public void run() {
                                this.this$0.val$callback.onTypefaceRequestFailed(-3);
                            }
                        });
                        return;
                    }
                    this.val$callerThreadHandler.post(new Runnable(this, typeface){
                        final 4 this$0;
                        final Typeface val$typeface;
                        {
                            this.this$0 = var1_1;
                            this.val$typeface = typeface;
                        }

                        @Override
                        public void run() {
                            this.this$0.val$callback.onTypefaceRetrieved(this.val$typeface);
                        }
                    });
                    return;
                }
                this.val$callerThreadHandler.post(new Runnable(this){
                    final 4 this$0;
                    {
                        this.this$0 = var1_1;
                    }

                    @Override
                    public void run() {
                        this.this$0.val$callback.onTypefaceRequestFailed(1);
                    }
                });
                return;
            }
        });
    }

    public static void resetCache() {
        sTypefaceCache.evictAll();
    }

    public static final class Columns
    implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }

    public static class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        public FontFamilyResult(int n, FontInfo[] fontInfoArray) {
            this.mStatusCode = n;
            this.mFonts = fontInfoArray;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }
    }

    public static class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        public FontInfo(Uri uri, int n, int n2, boolean bl, int n3) {
            this.mUri = Preconditions.checkNotNull(uri);
            this.mTtcIndex = n;
            this.mWeight = n2;
            this.mItalic = bl;
            this.mResultCode = n3;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }
    }

    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        public static final int RESULT_OK = 0;

        public void onTypefaceRequestFailed(int n) {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface FontRequestFailReason {
        }
    }

    private static final class TypefaceResult {
        final int mResult;
        final Typeface mTypeface;

        TypefaceResult(Typeface typeface, int n) {
            this.mTypeface = typeface;
            this.mResult = n;
        }
    }
}

