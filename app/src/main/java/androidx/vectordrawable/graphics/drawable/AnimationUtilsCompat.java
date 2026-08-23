/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Xml
 *  android.view.animation.AccelerateDecelerateInterpolator
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.AnimationUtils
 *  android.view.animation.AnticipateInterpolator
 *  android.view.animation.AnticipateOvershootInterpolator
 *  android.view.animation.BounceInterpolator
 *  android.view.animation.CycleInterpolator
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.LinearInterpolator
 *  android.view.animation.OvershootInterpolator
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationUtilsCompat {
    private AnimationUtilsCompat() {
    }

    private static Interpolator createInterpolatorFromXml(Context object, Resources object2, Resources.Theme theme, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int n;
        object2 = null;
        int n2 = xmlPullParser.getDepth();
        while (((n = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n2) && n != 1) {
            if (n != 2) continue;
            theme = Xml.asAttributeSet((XmlPullParser)xmlPullParser);
            object2 = xmlPullParser.getName();
            if (((String)object2).equals("linearInterpolator")) {
                object2 = new LinearInterpolator();
                continue;
            }
            if (((String)object2).equals("accelerateInterpolator")) {
                object2 = new AccelerateInterpolator((Context)object, (AttributeSet)theme);
                continue;
            }
            if (((String)object2).equals("decelerateInterpolator")) {
                object2 = new DecelerateInterpolator((Context)object, (AttributeSet)theme);
                continue;
            }
            if (((String)object2).equals("accelerateDecelerateInterpolator")) {
                object2 = new AccelerateDecelerateInterpolator();
                continue;
            }
            if (((String)object2).equals("cycleInterpolator")) {
                object2 = new CycleInterpolator((Context)object, (AttributeSet)theme);
                continue;
            }
            if (((String)object2).equals("anticipateInterpolator")) {
                object2 = new AnticipateInterpolator((Context)object, (AttributeSet)theme);
                continue;
            }
            if (((String)object2).equals("overshootInterpolator")) {
                object2 = new OvershootInterpolator((Context)object, (AttributeSet)theme);
                continue;
            }
            if (((String)object2).equals("anticipateOvershootInterpolator")) {
                object2 = new AnticipateOvershootInterpolator((Context)object, (AttributeSet)theme);
                continue;
            }
            if (((String)object2).equals("bounceInterpolator")) {
                object2 = new BounceInterpolator();
                continue;
            }
            if (((String)object2).equals("pathInterpolator")) {
                object2 = new PathInterpolatorCompat((Context)object, (AttributeSet)theme, xmlPullParser);
                continue;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unknown interpolator name: ");
            ((StringBuilder)object).append(xmlPullParser.getName());
            throw new RuntimeException(((StringBuilder)object).toString());
        }
        return object2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Interpolator loadInterpolator(Context object, int n) throws Resources.NotFoundException {
        Throwable throwable2;
        Object object2;
        block14: {
            XmlResourceParser xmlResourceParser;
            if (Build.VERSION.SDK_INT >= 21) {
                return AnimationUtils.loadInterpolator((Context)object, (int)n);
            }
            Resources.NotFoundException notFoundException = null;
            Object object3 = null;
            object2 = null;
            if (n == 17563663) {
                object = new FastOutLinearInInterpolator();
                return object;
            }
            if (n == 17563661) {
                return new FastOutSlowInInterpolator();
            }
            if (n == 17563662) {
                return new LinearOutSlowInInterpolator();
            }
            try {
                xmlResourceParser = object.getResources().getAnimation(n);
                object2 = xmlResourceParser;
                notFoundException = xmlResourceParser;
                object3 = xmlResourceParser;
                object = AnimationUtilsCompat.createInterpolatorFromXml((Context)object, object.getResources(), object.getTheme(), (XmlPullParser)xmlResourceParser);
                if (xmlResourceParser == null) return object;
            }
            catch (Throwable throwable2) {}
            catch (IOException iOException) {}
            catch (XmlPullParserException xmlPullParserException) {}
            finally {
                break block14;
            }
            xmlResourceParser.close();
            return object;
        }
        if (object2 == null) throw throwable2;
        object2.close();
        throw throwable2;
    }
}

