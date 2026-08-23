/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import org.ini4j.BasicProfileSection;
import org.ini4j.BasicRegistry;
import org.ini4j.Registry;

class BasicRegistryKey
extends BasicProfileSection
implements Registry.Key {
    private static final String META_TYPE = "type";
    private static final long serialVersionUID = -1390060044244350928L;

    public BasicRegistryKey(BasicRegistry basicRegistry, String string2) {
        super(basicRegistry, string2);
    }

    @Override
    public Registry.Key addChild(String string2) {
        return (Registry.Key)super.addChild(string2);
    }

    @Override
    public Registry.Key getChild(String string2) {
        return (Registry.Key)super.getChild(string2);
    }

    @Override
    public Registry.Key getParent() {
        return (Registry.Key)super.getParent();
    }

    @Override
    public Registry.Type getType(Object object) {
        return (Registry.Type)((Object)this.getMeta(META_TYPE, object));
    }

    @Override
    public Registry.Type getType(Object object, Registry.Type object2) {
        if ((object = this.getType(object)) != null) {
            object2 = object;
        }
        return object2;
    }

    @Override
    public Registry.Key lookup(String ... stringArray) {
        return (Registry.Key)super.lookup(stringArray);
    }

    @Override
    public Registry.Type putType(String string2, Registry.Type type) {
        return (Registry.Type)((Object)this.putMeta(META_TYPE, string2, (Object)type));
    }

    @Override
    public Registry.Type removeType(Object object) {
        return (Registry.Type)((Object)this.removeMeta(META_TYPE, object));
    }
}

