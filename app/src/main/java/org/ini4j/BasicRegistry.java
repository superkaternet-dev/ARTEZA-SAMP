/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import org.ini4j.BasicProfile;
import org.ini4j.BasicRegistryKey;
import org.ini4j.Profile;
import org.ini4j.Registry;
import org.ini4j.spi.IniHandler;
import org.ini4j.spi.RegEscapeTool;
import org.ini4j.spi.TypeValuesPair;

public class BasicRegistry
extends BasicProfile
implements Registry {
    private static final long serialVersionUID = -6432826330714504802L;
    private String _version = "Windows Registry Editor Version 5.00";

    @Override
    public Registry.Key add(String string2) {
        return (Registry.Key)super.add(string2);
    }

    @Override
    public Registry.Key get(Object object) {
        return (Registry.Key)super.get(object);
    }

    @Override
    public Registry.Key get(Object object, int n) {
        return (Registry.Key)super.get(object, n);
    }

    @Override
    public String getVersion() {
        return this._version;
    }

    @Override
    Registry.Key newSection(String string2) {
        return new BasicRegistryKey(this, string2);
    }

    @Override
    public Registry.Key put(String string2, Profile.Section section) {
        return (Registry.Key)super.put(string2, section);
    }

    @Override
    public Registry.Key put(String string2, Profile.Section section, int n) {
        return (Registry.Key)super.put(string2, section, n);
    }

    @Override
    public Registry.Key remove(Object object) {
        return (Registry.Key)super.remove(object);
    }

    @Override
    public Registry.Key remove(Object object, int n) {
        return (Registry.Key)super.remove(object, n);
    }

    @Override
    public Registry.Key remove(Profile.Section section) {
        return (Registry.Key)super.remove(section);
    }

    @Override
    public void setVersion(String string2) {
        this._version = string2;
    }

    @Override
    void store(IniHandler iniHandler, Profile.Section section, String string2) {
        this.store(iniHandler, section.getComment(string2));
        Registry.Type type = ((Registry.Key)section).getType(string2, Registry.Type.REG_SZ);
        String string3 = string2.equals("@") ? string2 : RegEscapeTool.getInstance().quote(string2);
        String[] stringArray = new String[section.length(string2)];
        for (int i = 0; i < stringArray.length; ++i) {
            stringArray[i] = (String)section.get((Object)string2, i);
        }
        iniHandler.handleOption(string3, RegEscapeTool.getInstance().encode(new TypeValuesPair(type, stringArray)));
    }
}

