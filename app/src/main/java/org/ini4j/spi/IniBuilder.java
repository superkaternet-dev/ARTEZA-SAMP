/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.ini4j.spi.AbstractProfileBuilder;
import org.ini4j.spi.IniHandler;
import org.ini4j.spi.ServiceFinder;

public class IniBuilder
extends AbstractProfileBuilder
implements IniHandler {
    private Ini _ini;

    private static IniBuilder newInstance() {
        return ServiceFinder.findService(IniBuilder.class);
    }

    public static IniBuilder newInstance(Ini ini) {
        IniBuilder iniBuilder = IniBuilder.newInstance();
        iniBuilder.setIni(ini);
        return iniBuilder;
    }

    @Override
    Config getConfig() {
        return this._ini.getConfig();
    }

    @Override
    Profile getProfile() {
        return this._ini;
    }

    public void setIni(Ini ini) {
        this._ini = ini;
    }
}

