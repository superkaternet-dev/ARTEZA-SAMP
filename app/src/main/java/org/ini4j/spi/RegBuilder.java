/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import org.ini4j.Config;
import org.ini4j.Profile;
import org.ini4j.Reg;
import org.ini4j.Registry;
import org.ini4j.spi.AbstractProfileBuilder;
import org.ini4j.spi.RegEscapeTool;
import org.ini4j.spi.ServiceFinder;

public class RegBuilder
extends AbstractProfileBuilder {
    private Reg _reg;

    private static RegBuilder newInstance() {
        return ServiceFinder.findService(RegBuilder.class);
    }

    public static RegBuilder newInstance(Reg reg) {
        RegBuilder regBuilder = RegBuilder.newInstance();
        regBuilder.setReg(reg);
        return regBuilder;
    }

    @Override
    Config getConfig() {
        return this._reg.getConfig();
    }

    @Override
    Profile getProfile() {
        return this._reg;
    }

    @Override
    public void handleOption(String string2, String stringArray) {
        if (string2.charAt(0) == '\"') {
            string2 = RegEscapeTool.getInstance().unquote(string2);
        }
        if ((stringArray = RegEscapeTool.getInstance().decode((String)stringArray)).getType() != Registry.Type.REG_SZ) {
            ((Registry.Key)this.getCurrentSection()).putType(string2, stringArray.getType());
        }
        stringArray = stringArray.getValues();
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            super.handleOption(string2, stringArray[i]);
        }
    }

    public void setReg(Reg reg) {
        this._reg = reg;
    }
}

