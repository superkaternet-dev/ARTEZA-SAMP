/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Color
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.StateSet
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import androidx.core.R;
import androidx.core.content.res.GrowingArrayUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ColorStateListInflaterCompat {
    private ColorStateListInflaterCompat() {
    }

    public static ColorStateList createFromXml(Resources object, XmlPullParser xmlPullParser, Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlPullParser);
        while ((n = xmlPullParser.next()) != 2 && n != 1) {
        }
        if (n == 2) {
            return ColorStateListInflaterCompat.createFromXmlInner(object, xmlPullParser, attributeSet, theme);
        }
        object = new XmlPullParserException("No start tag found");
        throw object;
    }

    public static ColorStateList createFromXmlInner(Resources object, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        String string2 = xmlPullParser.getName();
        if (string2.equals("selector")) {
            return ColorStateListInflaterCompat.inflate((Resources)object, xmlPullParser, attributeSet, theme);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append(xmlPullParser.getPositionDescription());
        ((StringBuilder)object).append(": invalid color state list tag ");
        ((StringBuilder)object).append(string2);
        throw new XmlPullParserException(((StringBuilder)object).toString());
    }

    public static ColorStateList inflate(Resources resources, int n, Resources.Theme theme) {
        try {
            resources = ColorStateListInflaterCompat.createFromXml(resources, (XmlPullParser)resources.getXml(n), theme);
            return resources;
        }
        catch (Exception exception) {
            Log.e((String)"CSLCompat", (String)"Failed to inflate ColorStateList.", (Throwable)exception);
            return null;
        }
    }

    private static ColorStateList inflate(Resources object, XmlPullParser object2, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        int n2;
        int n3 = object2.getDepth() + 1;
        Object object3 = new int[20][];
        int[] nArray = new int[((int[][])object3).length];
        int n4 = 0;
        while ((n2 = object2.next()) != 1 && ((n = object2.getDepth()) >= n3 || n2 != 3)) {
            if (n2 != 2 || n > n3 || !object2.getName().equals("item")) continue;
            Object object4 = ColorStateListInflaterCompat.obtainAttributes(object, theme, attributeSet, R.styleable.ColorStateListItem);
            int n5 = object4.getColor(R.styleable.ColorStateListItem_android_color, -65281);
            float f = 1.0f;
            if (object4.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
                f = object4.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0f);
            } else if (object4.hasValue(R.styleable.ColorStateListItem_alpha)) {
                f = object4.getFloat(R.styleable.ColorStateListItem_alpha, 1.0f);
            }
            object4.recycle();
            n2 = 0;
            int n6 = attributeSet.getAttributeCount();
            int[] nArray2 = new int[n6];
            for (n = 0; n < n6; ++n) {
                int n7 = attributeSet.getAttributeNameResource(n);
                int n8 = n2;
                if (n7 != 16843173) {
                    n8 = n2;
                    if (n7 != 16843551) {
                        n8 = n2;
                        if (n7 != R.attr.alpha) {
                            n8 = attributeSet.getAttributeBooleanValue(n, false) ? n7 : -n7;
                            nArray2[n2] = n8;
                            n8 = n2 + 1;
                        }
                    }
                }
                n2 = n8;
            }
            object4 = StateSet.trimStateSet((int[])nArray2, (int)n2);
            nArray = GrowingArrayUtils.append(nArray, n4, ColorStateListInflaterCompat.modulateColorAlpha(n5, f));
            object3 = (int[][])GrowingArrayUtils.append(object3, n4, object4);
            ++n4;
        }
        object = new int[n4];
        object2 = new int[n4][];
        System.arraycopy(nArray, 0, object, 0, n4);
        System.arraycopy(object3, 0, object2, 0, n4);
        return new ColorStateList((int[][])object2, (int[])object);
    }

    private static int modulateColorAlpha(int n, float f) {
        return 0xFFFFFF & n | Math.round((float)Color.alpha((int)n) * f) << 24;
    }

    private static TypedArray obtainAttributes(Resources resources, Resources.Theme theme, AttributeSet attributeSet, int[] nArray) {
        resources = theme == null ? resources.obtainAttributes(attributeSet, nArray) : theme.obtainStyledAttributes(attributeSet, nArray, 0, 0);
        return resources;
    }
}

