/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.content.res.XmlResourceParser
 *  android.util.AttributeSet
 *  android.util.Xml
 *  android.view.InflateException
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.preference;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class PreferenceInflater {
    private static final HashMap<String, Constructor> CONSTRUCTOR_MAP;
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE;
    private static final String EXTRA_TAG_NAME = "extra";
    private static final String INTENT_TAG_NAME = "intent";
    private final Object[] mConstructorArgs = new Object[2];
    private final Context mContext;
    private String[] mDefaultPackages;
    private PreferenceManager mPreferenceManager;

    static {
        CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class};
        CONSTRUCTOR_MAP = new HashMap();
    }

    public PreferenceInflater(Context context, PreferenceManager preferenceManager) {
        this.mContext = context;
        this.init(preferenceManager);
    }

    /*
     * Unable to fully structure code
     */
    private Preference createItem(String var1_1, String[] var2_3, AttributeSet var3_5) throws ClassNotFoundException, InflateException {
        var7_6 = PreferenceInflater.CONSTRUCTOR_MAP.get(var1_1);
        var6_8 = var7_6;
        if (var7_6 != null) ** GOTO lbl48
        var9_9 = this.mContext.getClassLoader();
        var8_10 = null;
        if (var2_3 == null) ** GOTO lbl43
        if (((Object[])var2_3).length == 0) ** GOTO lbl43
        var7_6 = null;
        var5_11 = ((Object)var2_3).length;
        var4_12 = 0;
        while (true) {
            var6_8 = var8_10;
            if (var4_12 >= var5_11) break;
            var6_8 = var2_3[var4_12];
            try {
                var10_13 = new StringBuilder();
                var10_13.append((String)var6_8);
                var10_13.append(var1_1);
                var6_8 = Class.forName(var10_13.toString(), false, var9_9);
            }
            catch (ClassNotFoundException var7_7) {
                ++var4_12;
                continue;
            }
            break;
        }
        var2_3 = var6_8;
        if (var6_8 != null) ** GOTO lbl44
        if (var7_6 != null) ** GOTO lbl42
        var2_3 = new StringBuilder();
        var2_3.append(var3_5.getPositionDescription());
        var2_3.append(": Error inflating class ");
        var2_3.append(var1_1);
        var6_8 = new InflateException(var2_3.toString());
        throw var6_8;
lbl42:
        // 1 sources

        throw var7_6;
lbl43:
        // 2 sources

        var2_3 = Class.forName(var1_1, false, var9_9);
lbl44:
        // 2 sources

        var6_8 = var2_3.getConstructor(PreferenceInflater.CONSTRUCTOR_SIGNATURE);
        var6_8.setAccessible(true);
        PreferenceInflater.CONSTRUCTOR_MAP.put(var1_1, (Constructor)var6_8);
lbl48:
        // 2 sources

        var2_3 = this.mConstructorArgs;
        var2_3[1] = var3_5;
        try {
            var2_3 = (Preference)var6_8.newInstance((Object[])var2_3);
            return var2_3;
        }
        catch (Exception var2_4) {
            var6_8 = new StringBuilder();
            var6_8.append(var3_5.getPositionDescription());
            var6_8.append(": Error inflating class ");
            var6_8.append(var1_1);
            var1_1 = new InflateException(var6_8.toString());
            var1_1.initCause((Throwable)var2_4);
            throw var1_1;
        }
        catch (ClassNotFoundException var1_2) {
            throw var1_2;
        }
    }

    private Preference createItemFromTag(String object, AttributeSet attributeSet) {
        block6: {
            block5: {
                if (-1 != ((String)object).indexOf(46)) break block5;
                Preference preference = this.onCreateItem((String)object, attributeSet);
                object = preference;
                break block6;
            }
            try {
                Preference preference = this.createItem((String)object, null, attributeSet);
                object = preference;
            }
            catch (Exception exception) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(attributeSet.getPositionDescription());
                stringBuilder.append(": Error inflating class ");
                stringBuilder.append((String)object);
                object = new InflateException(stringBuilder.toString());
                object.initCause((Throwable)exception);
                throw object;
            }
            catch (ClassNotFoundException classNotFoundException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(attributeSet.getPositionDescription());
                stringBuilder.append(": Error inflating class (not found)");
                stringBuilder.append((String)object);
                object = new InflateException(stringBuilder.toString());
                object.initCause((Throwable)classNotFoundException);
                throw object;
            }
            catch (InflateException inflateException) {
                throw inflateException;
            }
        }
        return object;
    }

    private void init(PreferenceManager object) {
        this.mPreferenceManager = object;
        object = new StringBuilder();
        ((StringBuilder)object).append(Preference.class.getPackage().getName());
        ((StringBuilder)object).append(".");
        String string2 = ((StringBuilder)object).toString();
        object = new StringBuilder();
        ((StringBuilder)object).append(SwitchPreference.class.getPackage().getName());
        ((StringBuilder)object).append(".");
        this.setDefaultPackages(new String[]{string2, ((StringBuilder)object).toString()});
    }

    private PreferenceGroup onMergeRoots(PreferenceGroup preferenceGroup, PreferenceGroup preferenceGroup2) {
        if (preferenceGroup == null) {
            preferenceGroup2.onAttachedToHierarchy(this.mPreferenceManager);
            return preferenceGroup2;
        }
        return preferenceGroup;
    }

    private void rInflate(XmlPullParser object, Preference object2, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        int n;
        int n2 = object.getDepth();
        while (((n = object.next()) != 3 || object.getDepth() > n2) && n != 1) {
            if (n != 2) continue;
            Object object3 = object.getName();
            if (INTENT_TAG_NAME.equals(object3)) {
                try {
                    object3 = Intent.parseIntent((Resources)this.getContext().getResources(), (XmlPullParser)object, (AttributeSet)attributeSet);
                    object2.setIntent((Intent)object3);
                    continue;
                }
                catch (IOException iOException) {
                    object = new XmlPullParserException("Error parsing preference");
                    object.initCause((Throwable)iOException);
                    throw object;
                }
            }
            if (EXTRA_TAG_NAME.equals(object3)) {
                this.getContext().getResources().parseBundleExtra(EXTRA_TAG_NAME, attributeSet, object2.getExtras());
                try {
                    PreferenceInflater.skipCurrentTag(object);
                    continue;
                }
                catch (IOException iOException) {
                    object2 = new XmlPullParserException("Error parsing preference");
                    object2.initCause(iOException);
                    throw object2;
                }
            }
            object3 = this.createItemFromTag((String)object3, attributeSet);
            ((PreferenceGroup)object2).addItemFromInflater((Preference)object3);
            this.rInflate((XmlPullParser)object, (Preference)object3, attributeSet);
        }
    }

    private static void skipCurrentTag(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int n;
        int n2 = xmlPullParser.getDepth();
        while ((n = xmlPullParser.next()) != 1 && (n != 3 || xmlPullParser.getDepth() > n2)) {
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public String[] getDefaultPackages() {
        return this.mDefaultPackages;
    }

    public Preference inflate(int n, PreferenceGroup preference) {
        XmlResourceParser xmlResourceParser = this.getContext().getResources().getXml(n);
        try {
            preference = this.inflate((XmlPullParser)xmlResourceParser, (PreferenceGroup)preference);
            return preference;
        }
        finally {
            xmlResourceParser.close();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Preference inflate(XmlPullParser xmlPullParser, PreferenceGroup object) {
        Object[] objectArray = this.mConstructorArgs;
        synchronized (objectArray) {
            Object object2 = Xml.asAttributeSet((XmlPullParser)xmlPullParser);
            this.mConstructorArgs[0] = this.mContext;
            try {
                int n;
                while ((n = xmlPullParser.next()) != 2 && n != 1) {
                }
                if (n == 2) {
                    object = this.onMergeRoots((PreferenceGroup)object, (PreferenceGroup)this.createItemFromTag(xmlPullParser.getName(), (AttributeSet)object2));
                    this.rInflate(xmlPullParser, (Preference)object, (AttributeSet)object2);
                    return object;
                }
                object2 = new StringBuilder();
                ((StringBuilder)object2).append(xmlPullParser.getPositionDescription());
                ((StringBuilder)object2).append(": No start tag found!");
                object = new InflateException(((StringBuilder)object2).toString());
                throw object;
            }
            catch (IOException iOException) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append(xmlPullParser.getPositionDescription());
                ((StringBuilder)object2).append(": ");
                ((StringBuilder)object2).append(iOException.getMessage());
                InflateException inflateException = new InflateException(((StringBuilder)object2).toString());
                inflateException.initCause((Throwable)iOException);
                throw inflateException;
            }
            catch (XmlPullParserException xmlPullParserException) {
                object = new InflateException(xmlPullParserException.getMessage());
                object.initCause(xmlPullParserException);
                throw object;
            }
            catch (InflateException inflateException) {
                throw inflateException;
            }
        }
    }

    protected Preference onCreateItem(String string2, AttributeSet attributeSet) throws ClassNotFoundException {
        return this.createItem(string2, this.mDefaultPackages, attributeSet);
    }

    public void setDefaultPackages(String[] stringArray) {
        this.mDefaultPackages = stringArray;
    }
}

