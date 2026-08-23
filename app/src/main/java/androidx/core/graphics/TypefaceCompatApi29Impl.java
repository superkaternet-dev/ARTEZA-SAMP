/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.graphics.Typeface$CustomFallbackBuilder
 *  android.graphics.fonts.Font
 *  android.graphics.fonts.Font$Builder
 *  android.graphics.fonts.FontFamily
 *  android.graphics.fonts.FontFamily$Builder
 *  android.graphics.fonts.FontStyle
 *  android.os.CancellationSignal
 *  android.os.ParcelFileDescriptor
 */
package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompatBaseImpl;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.io.InputStream;

public class TypefaceCompatApi29Impl
extends TypefaceCompatBaseImpl {
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(Context object, FontResourcesParserCompat.FontFamilyFilesResourceEntry object2, Resources resources, int n) {
        object = null;
        FontResourcesParserCompat.FontFileResourceEntry[] fontFileResourceEntryArray = ((FontResourcesParserCompat.FontFamilyFilesResourceEntry)object2).getEntries();
        int n2 = fontFileResourceEntryArray.length;
        int n3 = 0;
        int n4 = 0;
        while (true) {
            Font.Builder builder;
            block8: {
                int n5;
                block7: {
                    n5 = 1;
                    if (n4 >= n2) break;
                    object2 = fontFileResourceEntryArray[n4];
                    builder = new Font.Builder(resources, ((FontResourcesParserCompat.FontFileResourceEntry)object2).getResourceId());
                    builder = builder.setWeight(((FontResourcesParserCompat.FontFileResourceEntry)object2).getWeight());
                    if (((FontResourcesParserCompat.FontFileResourceEntry)object2).isItalic()) break block7;
                    n5 = 0;
                }
                builder = builder.setSlant(n5).setTtcIndex(((FontResourcesParserCompat.FontFileResourceEntry)object2).getTtcIndex()).setFontVariationSettings(((FontResourcesParserCompat.FontFileResourceEntry)object2).getVariationSettings()).build();
                if (object != null) break block8;
                object2 = new FontFamily.Builder((Font)builder);
                object = object2;
            }
            try {
                object.addFont((Font)builder);
            }
            catch (IOException iOException) {
                // empty catch block
            }
            ++n4;
        }
        if (object == null) {
            return null;
        }
        n4 = (n & 1) != 0 ? 700 : 400;
        n = (n & 2) != 0 ? 1 : n3;
        object2 = new FontStyle(n4, n);
        return new Typeface.CustomFallbackBuilder(object.build()).setStyle((FontStyle)object2).build();
    }

    /*
     * Loose catch block
     */
    @Override
    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontsContractCompat.FontInfo[] fontInfoArray, int n) {
        Context context2 = null;
        ContentResolver contentResolver = context.getContentResolver();
        int n2 = fontInfoArray.length;
        int n3 = 0;
        int n4 = 0;
        context = context2;
        while (true) {
            block16: {
                ParcelFileDescriptor parcelFileDescriptor;
                block19: {
                    FontsContractCompat.FontInfo fontInfo;
                    block18: {
                        int n5;
                        block17: {
                            block15: {
                                n5 = 1;
                                if (n4 >= n2) break;
                                fontInfo = fontInfoArray[n4];
                                context2 = context;
                                parcelFileDescriptor = contentResolver.openFileDescriptor(fontInfo.getUri(), "r", cancellationSignal);
                                if (parcelFileDescriptor != null) break block15;
                                if (parcelFileDescriptor == null) break block16;
                                context2 = context;
                                parcelFileDescriptor.close();
                            }
                            context2 = new Font.Builder(parcelFileDescriptor);
                            context2 = context2.setWeight(fontInfo.getWeight());
                            if (fontInfo.isItalic()) break block17;
                            n5 = 0;
                        }
                        fontInfo = context2.setSlant(n5).setTtcIndex(fontInfo.getTtcIndex()).build();
                        if (context != null) break block18;
                        context = context2 = new FontFamily.Builder((Font)fontInfo);
                        break block19;
                    }
                    context.addFont((Font)fontInfo);
                }
                if (parcelFileDescriptor == null) break block16;
                context2 = context;
                parcelFileDescriptor.close();
                catch (Throwable throwable) {
                    if (parcelFileDescriptor != null) {
                        try {
                            parcelFileDescriptor.close();
                        }
                        catch (Throwable throwable2) {
                            // empty catch block
                        }
                    }
                    context2 = context;
                    try {
                        throw throwable;
                    }
                    catch (IOException iOException) {
                        context = context2;
                    }
                }
            }
            ++n4;
        }
        if (context == null) {
            return null;
        }
        n4 = (n & 1) != 0 ? 700 : 400;
        n = (n & 2) != 0 ? 1 : n3;
        cancellationSignal = new FontStyle(n4, n);
        return new Typeface.CustomFallbackBuilder(context.build()).setStyle((FontStyle)cancellationSignal).build();
    }

    @Override
    protected Typeface createFromInputStream(Context context, InputStream inputStream) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    @Override
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int n, String string2, int n2) {
        try {
            context = new Font.Builder(resources, n);
            context = context.build();
            resources = new FontFamily.Builder((Font)context);
            resources = resources.build();
        }
        catch (IOException iOException) {
            return null;
        }
        return new Typeface.CustomFallbackBuilder((FontFamily)resources).setStyle(context.getStyle()).build();
    }

    @Override
    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fontInfoArray, int n) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }
}

