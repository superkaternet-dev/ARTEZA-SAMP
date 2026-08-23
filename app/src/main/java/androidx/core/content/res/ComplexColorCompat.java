/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.graphics.Shader
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import androidx.core.content.res.ColorStateListInflaterCompat;
import androidx.core.content.res.GradientColorInflaterCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ComplexColorCompat {
    private static final String LOG_TAG = "ComplexColorCompat";
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;

    private ComplexColorCompat(Shader shader, ColorStateList colorStateList, int n) {
        this.mShader = shader;
        this.mColorStateList = colorStateList;
        this.mColor = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static ComplexColorCompat createFromXml(Resources object, int n, Resources.Theme theme) throws IOException, XmlPullParserException {
        String string2;
        AttributeSet attributeSet;
        XmlResourceParser xmlResourceParser;
        block10: {
            int n2;
            xmlResourceParser = object.getXml(n);
            attributeSet = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
            do {
                n2 = xmlResourceParser.next();
                n = 1;
            } while (n2 != 2 && n2 != 1);
            if (n2 != 2) {
                object = new XmlPullParserException("No start tag found");
                throw object;
            }
            string2 = xmlResourceParser.getName();
            switch (string2.hashCode()) {
                case 1191572447: {
                    if (!string2.equals("selector")) break;
                    n = 0;
                    break block10;
                }
                case 89650992: {
                    if (string2.equals("gradient")) break block10;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                object = new StringBuilder();
                ((StringBuilder)object).append(xmlResourceParser.getPositionDescription());
                ((StringBuilder)object).append(": unsupported complex color tag ");
                ((StringBuilder)object).append(string2);
                throw new XmlPullParserException(((StringBuilder)object).toString());
            }
            case 1: {
                return ComplexColorCompat.from(GradientColorInflaterCompat.createFromXmlInner((Resources)object, (XmlPullParser)xmlResourceParser, attributeSet, theme));
            }
            case 0: 
        }
        return ComplexColorCompat.from(ColorStateListInflaterCompat.createFromXmlInner((Resources)object, (XmlPullParser)xmlResourceParser, attributeSet, theme));
    }

    static ComplexColorCompat from(int n) {
        return new ComplexColorCompat(null, null, n);
    }

    static ComplexColorCompat from(ColorStateList colorStateList) {
        return new ComplexColorCompat(null, colorStateList, colorStateList.getDefaultColor());
    }

    static ComplexColorCompat from(Shader shader) {
        return new ComplexColorCompat(shader, null, 0);
    }

    public static ComplexColorCompat inflate(Resources object, int n, Resources.Theme theme) {
        try {
            object = ComplexColorCompat.createFromXml(object, n, theme);
            return object;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"Failed to inflate ComplexColor.", (Throwable)exception);
            return null;
        }
    }

    public int getColor() {
        return this.mColor;
    }

    public Shader getShader() {
        return this.mShader;
    }

    public boolean isGradient() {
        boolean bl = this.mShader != null;
        return bl;
    }

    public boolean isStateful() {
        ColorStateList colorStateList;
        boolean bl = this.mShader == null && (colorStateList = this.mColorStateList) != null && colorStateList.isStateful();
        return bl;
    }

    public boolean onStateChanged(int[] nArray) {
        boolean bl;
        boolean bl2 = bl = false;
        if (this.isStateful()) {
            ColorStateList colorStateList = this.mColorStateList;
            int n = colorStateList.getColorForState(nArray, colorStateList.getDefaultColor());
            bl2 = bl;
            if (n != this.mColor) {
                bl2 = true;
                this.mColor = n;
            }
        }
        return bl2;
    }

    public void setColor(int n) {
        this.mColor = n;
    }

    public boolean willDraw() {
        boolean bl = this.isGradient() || this.mColor != 0;
        return bl;
    }
}

