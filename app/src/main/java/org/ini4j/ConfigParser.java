/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile;
import org.ini4j.spi.IniHandler;

public class ConfigParser
implements Serializable {
    private static final long serialVersionUID = 9118857036229164353L;
    private PyIni _ini;

    public ConfigParser() {
        this(Collections.EMPTY_MAP);
    }

    public ConfigParser(Map<String, String> map) {
        this._ini = new PyIni(map);
    }

    private String requireOption(String string2, String string3) throws NoSectionException, NoOptionException {
        if ((string2 = (String)this.requireSection(string2).get(string3)) != null) {
            return string2;
        }
        throw new NoOptionException(string3);
    }

    private Profile.Section requireSection(String string2) throws NoSectionException {
        Profile.Section section = (Profile.Section)this._ini.get(string2);
        if (section != null) {
            return section;
        }
        throw new NoSectionException(string2);
    }

    public void addSection(String string2) throws DuplicateSectionException {
        if (!this._ini.containsKey(string2)) {
            if (!"DEFAULT".equalsIgnoreCase(string2)) {
                this._ini.add(string2);
                return;
            }
            throw new IllegalArgumentException(string2);
        }
        throw new DuplicateSectionException(string2);
    }

    public Map<String, String> defaults() {
        return this._ini.getDefaults();
    }

    public String get(String string2, String string3) throws NoSectionException, NoOptionException, InterpolationException {
        return this.get(string2, string3, false, Collections.EMPTY_MAP);
    }

    public String get(String string2, String string3, boolean bl) throws NoSectionException, NoOptionException, InterpolationException {
        return this.get(string2, string3, bl, Collections.EMPTY_MAP);
    }

    public String get(String string2, String string3, boolean bl, Map<String, String> map) throws NoSectionException, NoOptionException, InterpolationException {
        String string4;
        String string5 = string4 = this.requireOption(string2, string3);
        if (!bl) {
            string5 = string4;
            if (string4 != null) {
                string5 = string4;
                if (string4.indexOf(37) >= 0) {
                    string5 = this._ini.fetch(string2, string3, map);
                }
            }
        }
        return string5;
    }

    public boolean getBoolean(String string2, String string3) throws NoSectionException, NoOptionException, InterpolationException {
        boolean bl;
        if (!("1".equalsIgnoreCase(string2 = this.get(string2, string3)) || "yes".equalsIgnoreCase(string2) || "true".equalsIgnoreCase(string2) || "on".equalsIgnoreCase(string2))) {
            if (!("0".equalsIgnoreCase(string2) || "no".equalsIgnoreCase(string2) || "false".equalsIgnoreCase(string2) || "off".equalsIgnoreCase(string2))) {
                throw new IllegalArgumentException(string2);
            }
            bl = false;
        } else {
            bl = true;
        }
        return bl;
    }

    public double getDouble(String string2, String string3) throws NoSectionException, NoOptionException, InterpolationException {
        return Double.parseDouble(this.get(string2, string3));
    }

    public float getFloat(String string2, String string3) throws NoSectionException, NoOptionException, InterpolationException {
        return Float.parseFloat(this.get(string2, string3));
    }

    protected Ini getIni() {
        return this._ini;
    }

    public int getInt(String string2, String string3) throws NoSectionException, NoOptionException, InterpolationException {
        return Integer.parseInt(this.get(string2, string3));
    }

    public long getLong(String string2, String string3) throws NoSectionException, NoOptionException, InterpolationException {
        return Long.parseLong(this.get(string2, string3));
    }

    public boolean hasOption(String object, String string2) {
        boolean bl = (object = (Profile.Section)this._ini.get(object)) != null && object.containsKey(string2);
        return bl;
    }

    public boolean hasSection(String string2) {
        return this._ini.containsKey(string2);
    }

    public List<Map.Entry<String, String>> items(String string2) throws NoSectionException, InterpolationMissingOptionException {
        return this.items(string2, false, Collections.EMPTY_MAP);
    }

    public List<Map.Entry<String, String>> items(String string2, boolean bl) throws NoSectionException, InterpolationMissingOptionException {
        return this.items(string2, bl, Collections.EMPTY_MAP);
    }

    public List<Map.Entry<String, String>> items(String object, boolean bl, Map<String, String> map) throws NoSectionException, InterpolationMissingOptionException {
        Profile.Section section = this.requireSection((String)object);
        if (bl) {
            object = new HashMap<String, String>(section);
        } else {
            HashMap<Object, String> hashMap = new HashMap<Object, String>();
            Iterator iterator2 = section.keySet().iterator();
            while (true) {
                object = hashMap;
                if (!iterator2.hasNext()) break;
                object = (String)iterator2.next();
                hashMap.put(object, this._ini.fetch(section, (String)object, map));
            }
        }
        return new ArrayList<Map.Entry<String, String>>(object.entrySet());
    }

    public List<String> options(String string2) throws NoSectionException {
        this.requireSection(string2);
        return new ArrayList<String>(((Profile.Section)this._ini.get(string2)).keySet());
    }

    public void read(File file) throws IOException, ParsingException {
        try {
            PyIni pyIni = this._ini;
            FileReader fileReader = new FileReader(file);
            pyIni.load(fileReader);
            return;
        }
        catch (InvalidFileFormatException invalidFileFormatException) {
            throw new ParsingException(invalidFileFormatException);
        }
    }

    public void read(InputStream inputStream) throws IOException, ParsingException {
        try {
            this._ini.load(inputStream);
            return;
        }
        catch (InvalidFileFormatException invalidFileFormatException) {
            throw new ParsingException(invalidFileFormatException);
        }
    }

    public void read(Reader reader) throws IOException, ParsingException {
        try {
            this._ini.load(reader);
            return;
        }
        catch (InvalidFileFormatException invalidFileFormatException) {
            throw new ParsingException(invalidFileFormatException);
        }
    }

    public void read(URL uRL) throws IOException, ParsingException {
        try {
            this._ini.load(uRL);
            return;
        }
        catch (InvalidFileFormatException invalidFileFormatException) {
            throw new ParsingException(invalidFileFormatException);
        }
    }

    public void read(String ... stringArray) throws IOException, ParsingException {
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            this.read(new File(stringArray[i]));
        }
    }

    public boolean removeOption(String object, String string2) throws NoSectionException {
        object = this.requireSection((String)object);
        boolean bl = object.containsKey(string2);
        object.remove(string2);
        return bl;
    }

    public boolean removeSection(String string2) {
        boolean bl = this._ini.containsKey(string2);
        this._ini.remove(string2);
        return bl;
    }

    public List<String> sections() {
        return new ArrayList<String>(this._ini.keySet());
    }

    public void set(String object, String string2, Object object2) throws NoSectionException {
        object = this.requireSection((String)object);
        if (object2 == null) {
            object.remove(string2);
        } else {
            object.put(string2, object2.toString());
        }
    }

    public void write(File file) throws IOException {
        this._ini.store(new FileWriter(file));
    }

    public void write(OutputStream outputStream) throws IOException {
        this._ini.store(outputStream);
    }

    public void write(Writer writer) throws IOException {
        this._ini.store(writer);
    }

    public static class ConfigParserException
    extends Exception {
        private static final long serialVersionUID = -6845546313519392093L;

        public ConfigParserException(String string2) {
            super(string2);
        }
    }

    public static final class DuplicateSectionException
    extends ConfigParserException {
        private static final long serialVersionUID = -5244008445735700699L;

        private DuplicateSectionException(String string2) {
            super(string2);
        }
    }

    public static class InterpolationException
    extends ConfigParserException {
        private static final long serialVersionUID = 8924443303158546939L;

        protected InterpolationException(String string2) {
            super(string2);
        }
    }

    public static final class InterpolationMissingOptionException
    extends InterpolationException {
        private static final long serialVersionUID = 2903136975820447879L;

        private InterpolationMissingOptionException(String string2) {
            super(string2);
        }
    }

    public static final class NoOptionException
    extends ConfigParserException {
        private static final long serialVersionUID = 8460082078809425858L;

        private NoOptionException(String string2) {
            super(string2);
        }
    }

    public static final class NoSectionException
    extends ConfigParserException {
        private static final long serialVersionUID = 8553627727493146118L;

        private NoSectionException(String string2) {
            super(string2);
        }
    }

    public static final class ParsingException
    extends IOException {
        private static final long serialVersionUID = -5395990242007205038L;

        private ParsingException(Throwable throwable) {
            super(throwable.getMessage());
            this.initCause(throwable);
        }
    }

    static class PyIni
    extends Ini {
        protected static final String DEFAULT_SECTION_NAME = "DEFAULT";
        private static final Pattern EXPRESSION = Pattern.compile("(?<!\\\\)\\%\\(([^\\)]+)\\)");
        private static final int G_OPTION = 1;
        private static final char SUBST_CHAR = '%';
        private static final long serialVersionUID = -7152857626328996122L;
        private Profile.Section _defaultSection;
        private final Map<String, String> _defaults;

        public PyIni(Map<String, String> object) {
            this._defaults = object;
            object = this.getConfig().clone();
            ((Config)object).setEscape(false);
            ((Config)object).setMultiOption(false);
            ((Config)object).setMultiSection(false);
            ((Config)object).setLowerCaseOption(true);
            ((Config)object).setLowerCaseSection(true);
            super.setConfig((Config)object);
        }

        @Override
        public Profile.Section add(String object) {
            if (DEFAULT_SECTION_NAME.equalsIgnoreCase((String)object)) {
                if (this._defaultSection == null) {
                    this._defaultSection = this.newSection((String)object);
                }
                object = this._defaultSection;
            } else {
                object = super.add((String)object);
            }
            return object;
        }

        public String fetch(String string2, String string3, Map<String, String> map) throws InterpolationMissingOptionException {
            return this.fetch((Profile.Section)this.get(string2), string3, map);
        }

        protected String fetch(Profile.Section section, String charSequence, Map<String, String> map) throws InterpolationMissingOptionException {
            String string2 = (String)section.get(charSequence);
            charSequence = string2;
            if (string2 != null) {
                charSequence = string2;
                if (string2.indexOf(37) >= 0) {
                    charSequence = new StringBuilder(string2);
                    this.resolve((StringBuilder)charSequence, section, map);
                    charSequence = ((StringBuilder)charSequence).toString();
                }
            }
            return charSequence;
        }

        protected Profile.Section getDefaultSection() {
            return this._defaultSection;
        }

        public Map<String, String> getDefaults() {
            return this._defaults;
        }

        protected void resolve(StringBuilder stringBuilder, Profile.Section section, Map<String, String> map) throws InterpolationMissingOptionException {
            Matcher matcher = EXPRESSION.matcher(stringBuilder);
            while (matcher.find()) {
                String string2;
                String string3 = matcher.group(1);
                String string4 = string2 = (String)section.get(string3);
                if (string2 == null) {
                    string4 = map.get(string3);
                }
                string2 = string4;
                if (string4 == null) {
                    string2 = this._defaults.get(string3);
                }
                string4 = string2;
                if (string2 == null) {
                    Profile.Section section2 = this._defaultSection;
                    string4 = string2;
                    if (section2 != null) {
                        string4 = (String)section2.get(string3);
                    }
                }
                if (string4 != null) {
                    stringBuilder.replace(matcher.start(), matcher.end(), string4);
                    matcher.reset(stringBuilder);
                    continue;
                }
                throw new InterpolationMissingOptionException(string3);
            }
        }

        @Override
        public void setConfig(Config config) {
        }

        @Override
        protected void store(IniHandler iniHandler) {
            iniHandler.startIni();
            Object object = this._defaultSection;
            if (object != null) {
                this.store(iniHandler, (Profile.Section)object);
            }
            object = this.values().iterator();
            while (object.hasNext()) {
                this.store(iniHandler, (Profile.Section)object.next());
            }
            iniHandler.endIni();
        }

        @Override
        protected void store(IniHandler iniHandler, Profile.Section section) {
            iniHandler.startSection(section.getName());
            for (String string2 : section.keySet()) {
                iniHandler.handleOption(string2, (String)section.get(string2));
            }
            iniHandler.endSection();
        }
    }
}

