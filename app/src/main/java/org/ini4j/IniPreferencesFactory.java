/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

public class IniPreferencesFactory
implements PreferencesFactory {
    public static final String KEY_SYSTEM = "org.ini4j.prefs.system";
    public static final String KEY_USER = "org.ini4j.prefs.user";
    public static final String PROPERTIES = "ini4j.properties";
    private Preferences _system;
    private Preferences _user;

    String getIniLocation(String string2) {
        String string3 = Config.getSystemProperty(string2);
        Object object = string3;
        if (string3 == null) {
            try {
                object = new Properties();
                ((Properties)object).load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES));
                object = ((Properties)object).getProperty(string2);
            }
            catch (Exception exception) {
                object = string3;
            }
        }
        return object;
    }

    URL getResource(String object) throws IllegalArgumentException {
        try {
            URI uRI = new URI((String)object);
            object = uRI.getScheme() == null ? Thread.currentThread().getContextClassLoader().getResource((String)object) : uRI.toURL();
            return object;
        }
        catch (Exception exception) {
            throw (IllegalArgumentException)new IllegalArgumentException().initCause(exception);
        }
    }

    InputStream getResourceAsStream(String object) throws IllegalArgumentException {
        try {
            object = this.getResource((String)object).openStream();
            return object;
        }
        catch (Exception exception) {
            throw (IllegalArgumentException)new IllegalArgumentException().initCause(exception);
        }
    }

    Preferences newIniPreferences(String string2) {
        Ini ini = new Ini();
        if ((string2 = this.getIniLocation(string2)) != null) {
            try {
                ini.load(this.getResourceAsStream(string2));
            }
            catch (Exception exception) {
                throw (IllegalArgumentException)new IllegalArgumentException().initCause(exception);
            }
        }
        return new IniPreferences(ini);
    }

    @Override
    public Preferences systemRoot() {
        synchronized (this) {
            if (this._system == null) {
                this._system = this.newIniPreferences(KEY_SYSTEM);
            }
            Preferences preferences = this._system;
            return preferences;
        }
    }

    @Override
    public Preferences userRoot() {
        synchronized (this) {
            if (this._user == null) {
                this._user = this.newIniPreferences(KEY_USER);
            }
            Preferences preferences = this._user;
            return preferences;
        }
    }
}

