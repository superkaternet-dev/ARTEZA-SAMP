/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile;

public class IniPreferences
extends AbstractPreferences {
    private static final String[] EMPTY = new String[0];
    private final Ini _ini;

    public IniPreferences(InputStream inputStream) throws IOException, InvalidFileFormatException {
        super(null, "");
        this._ini = new Ini(inputStream);
    }

    public IniPreferences(Reader reader) throws IOException, InvalidFileFormatException {
        super(null, "");
        this._ini = new Ini(reader);
    }

    public IniPreferences(URL uRL) throws IOException, InvalidFileFormatException {
        super(null, "");
        this._ini = new Ini(uRL);
    }

    public IniPreferences(Ini ini) {
        super(null, "");
        this._ini = ini;
    }

    @Override
    protected SectionPreferences childSpi(String string2) {
        Profile.Section section = (Profile.Section)this._ini.get(string2);
        boolean bl = section == null;
        if (bl) {
            section = this._ini.add(string2);
        }
        return new SectionPreferences(this, this, section, bl);
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : this._ini.keySet()) {
            if (string2.indexOf(this._ini.getPathSeparator()) >= 0) continue;
            arrayList.add(string2);
        }
        return arrayList.toArray(EMPTY);
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
    }

    protected Ini getIni() {
        return this._ini;
    }

    @Override
    protected String getSpi(String string2) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        return EMPTY;
    }

    @Override
    protected void putSpi(String string2, String string3) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeSpi(String string2) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
    }

    protected class SectionPreferences
    extends AbstractPreferences {
        private final Profile.Section _section;
        final IniPreferences this$0;

        SectionPreferences(IniPreferences iniPreferences, AbstractPreferences abstractPreferences, Profile.Section section, boolean bl) {
            this.this$0 = iniPreferences;
            super(abstractPreferences, section.getSimpleName());
            this._section = section;
            this.newNode = bl;
        }

        @Override
        protected SectionPreferences childSpi(String string2) throws UnsupportedOperationException {
            Profile.Section section = this._section.getChild(string2);
            boolean bl = section == null;
            if (bl) {
                section = this._section.addChild(string2);
            }
            return new SectionPreferences(this.this$0, this, section, bl);
        }

        @Override
        protected String[] childrenNamesSpi() throws BackingStoreException {
            return this._section.childrenNames();
        }

        @Override
        public void flush() throws BackingStoreException {
            this.parent().flush();
        }

        @Override
        protected void flushSpi() throws BackingStoreException {
        }

        @Override
        protected String getSpi(String string2) {
            return this._section.fetch(string2);
        }

        @Override
        protected String[] keysSpi() throws BackingStoreException {
            return this._section.keySet().toArray(EMPTY);
        }

        @Override
        protected void putSpi(String string2, String string3) {
            this._section.put(string2, string3);
        }

        @Override
        protected void removeNodeSpi() throws BackingStoreException {
            this.this$0._ini.remove(this._section);
        }

        @Override
        protected void removeSpi(String string2) {
            this._section.remove(string2);
        }

        @Override
        public void sync() throws BackingStoreException {
            this.parent().sync();
        }

        @Override
        protected void syncSpi() throws BackingStoreException {
        }
    }
}

