/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.io.Serializable;
import java.nio.charset.Charset;

public class Config
implements Cloneable,
Serializable {
    public static final boolean DEFAULT_COMMENT = true;
    public static final boolean DEFAULT_EMPTY_OPTION = false;
    public static final boolean DEFAULT_EMPTY_SECTION = false;
    public static final boolean DEFAULT_ESCAPE = true;
    public static final boolean DEFAULT_ESCAPE_KEY_ONLY = false;
    public static final boolean DEFAULT_ESCAPE_NEWLINE = true;
    public static final Charset DEFAULT_FILE_ENCODING;
    public static final boolean DEFAULT_GLOBAL_SECTION = false;
    public static final String DEFAULT_GLOBAL_SECTION_NAME = "?";
    public static final boolean DEFAULT_HEADER_COMMENT = true;
    public static final boolean DEFAULT_INCLUDE = false;
    public static final String DEFAULT_LINE_SEPARATOR;
    public static final boolean DEFAULT_LOWER_CASE_OPTION = false;
    public static final boolean DEFAULT_LOWER_CASE_SECTION = false;
    public static final boolean DEFAULT_MULTI_OPTION = true;
    public static final boolean DEFAULT_MULTI_SECTION = false;
    public static final char DEFAULT_PATH_SEPARATOR = '/';
    public static final boolean DEFAULT_PROPERTY_FIRST_UPPER = false;
    public static final boolean DEFAULT_STRICT_OPERATOR = false;
    public static final boolean DEFAULT_TREE = true;
    public static final boolean DEFAULT_UNNAMED_SECTION = false;
    private static final Config GLOBAL;
    public static final String KEY_PREFIX = "org.ini4j.config.";
    public static final String PROP_COMMENT = "comment";
    public static final String PROP_EMPTY_OPTION = "emptyOption";
    public static final String PROP_EMPTY_SECTION = "emptySection";
    public static final String PROP_ESCAPE = "escape";
    public static final String PROP_ESCAPE_KEY_ONLY = "escapeKey";
    public static final String PROP_ESCAPE_NEWLINE = "escapeNewline";
    public static final String PROP_FILE_ENCODING = "fileEncoding";
    public static final String PROP_GLOBAL_SECTION = "globalSection";
    public static final String PROP_GLOBAL_SECTION_NAME = "globalSectionName";
    public static final String PROP_HEADER_COMMENT = "headerComment";
    public static final String PROP_INCLUDE = "include";
    public static final String PROP_LINE_SEPARATOR = "lineSeparator";
    public static final String PROP_LOWER_CASE_OPTION = "lowerCaseOption";
    public static final String PROP_LOWER_CASE_SECTION = "lowerCaseSection";
    public static final String PROP_MULTI_OPTION = "multiOption";
    public static final String PROP_MULTI_SECTION = "multiSection";
    public static final String PROP_PATH_SEPARATOR = "pathSeparator";
    public static final String PROP_PROPERTY_FIRST_UPPER = "propertyFirstUpper";
    public static final String PROP_STRICT_OPERATOR = "strictOperator";
    public static final String PROP_TREE = "tree";
    public static final String PROP_UNNAMED_SECTION = "unnamedSection";
    private static final long serialVersionUID = 2865793267410367814L;
    private boolean _comment;
    private boolean _emptyOption;
    private boolean _emptySection;
    private boolean _escape;
    private boolean _escapeKeyOnly;
    private boolean _escapeNewline;
    private Charset _fileEncoding;
    private boolean _globalSection;
    private String _globalSectionName;
    private boolean _headerComment;
    private boolean _include;
    private String _lineSeparator;
    private boolean _lowerCaseOption;
    private boolean _lowerCaseSection;
    private boolean _multiOption;
    private boolean _multiSection;
    private char _pathSeparator;
    private boolean _propertyFirstUpper;
    private boolean _strictOperator;
    private boolean _tree;
    private boolean _unnamedSection;

    static {
        DEFAULT_LINE_SEPARATOR = Config.getSystemProperty("line.separator", "\n");
        DEFAULT_FILE_ENCODING = Charset.forName("UTF-8");
        GLOBAL = new Config();
    }

    public Config() {
        this.reset();
    }

    private boolean getBoolean(String string2, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(KEY_PREFIX);
        stringBuilder.append(string2);
        string2 = Config.getSystemProperty(stringBuilder.toString());
        if (string2 != null) {
            bl = Boolean.parseBoolean(string2);
        }
        return bl;
    }

    private char getChar(String string2, char c) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(KEY_PREFIX);
        stringBuilder.append(string2);
        string2 = Config.getSystemProperty(stringBuilder.toString());
        if (string2 != null) {
            c = string2.charAt(0);
        }
        return c;
    }

    private Charset getCharset(String string2, Charset charset) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(KEY_PREFIX);
        stringBuilder.append(string2);
        string2 = Config.getSystemProperty(stringBuilder.toString());
        if (string2 != null) {
            charset = Charset.forName(string2);
        }
        return charset;
    }

    public static String getEnvironment(String string2) {
        return Config.getEnvironment(string2, null);
    }

    public static String getEnvironment(String string2, String string3) {
        block2: {
            try {
                string2 = System.getenv(string2);
            }
            catch (SecurityException securityException) {
                string2 = null;
            }
            if (string2 != null) break block2;
            string2 = string3;
        }
        return string2;
    }

    public static Config getGlobal() {
        return GLOBAL;
    }

    private String getString(String string2, String string3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(KEY_PREFIX);
        stringBuilder.append(string2);
        return Config.getSystemProperty(stringBuilder.toString(), string3);
    }

    public static String getSystemProperty(String string2) {
        return Config.getSystemProperty(string2, null);
    }

    public static String getSystemProperty(String string2, String string3) {
        block2: {
            try {
                string2 = System.getProperty(string2);
            }
            catch (SecurityException securityException) {
                string2 = null;
            }
            if (string2 != null) break block2;
            string2 = string3;
        }
        return string2;
    }

    public Config clone() {
        try {
            Config config = (Config)super.clone();
            return config;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError((Object)cloneNotSupportedException);
        }
    }

    public Charset getFileEncoding() {
        return this._fileEncoding;
    }

    public String getGlobalSectionName() {
        return this._globalSectionName;
    }

    public String getLineSeparator() {
        return this._lineSeparator;
    }

    public char getPathSeparator() {
        return this._pathSeparator;
    }

    public boolean isComment() {
        return this._comment;
    }

    public boolean isEmptyOption() {
        return this._emptyOption;
    }

    public boolean isEmptySection() {
        return this._emptySection;
    }

    public boolean isEscape() {
        return this._escape;
    }

    public boolean isEscapeKeyOnly() {
        return this._escapeKeyOnly;
    }

    public boolean isEscapeNewline() {
        return this._escapeNewline;
    }

    public boolean isGlobalSection() {
        return this._globalSection;
    }

    public boolean isHeaderComment() {
        return this._headerComment;
    }

    public boolean isInclude() {
        return this._include;
    }

    public boolean isLowerCaseOption() {
        return this._lowerCaseOption;
    }

    public boolean isLowerCaseSection() {
        return this._lowerCaseSection;
    }

    public boolean isMultiOption() {
        return this._multiOption;
    }

    public boolean isMultiSection() {
        return this._multiSection;
    }

    public boolean isPropertyFirstUpper() {
        return this._propertyFirstUpper;
    }

    public boolean isStrictOperator() {
        return this._strictOperator;
    }

    public boolean isTree() {
        return this._tree;
    }

    public boolean isUnnamedSection() {
        return this._unnamedSection;
    }

    public final void reset() {
        this._emptyOption = this.getBoolean(PROP_EMPTY_OPTION, false);
        this._emptySection = this.getBoolean(PROP_EMPTY_SECTION, false);
        this._globalSection = this.getBoolean(PROP_GLOBAL_SECTION, false);
        this._globalSectionName = this.getString(PROP_GLOBAL_SECTION_NAME, DEFAULT_GLOBAL_SECTION_NAME);
        this._include = this.getBoolean(PROP_INCLUDE, false);
        this._lowerCaseOption = this.getBoolean(PROP_LOWER_CASE_OPTION, false);
        this._lowerCaseSection = this.getBoolean(PROP_LOWER_CASE_SECTION, false);
        this._multiOption = this.getBoolean(PROP_MULTI_OPTION, true);
        this._multiSection = this.getBoolean(PROP_MULTI_SECTION, false);
        this._strictOperator = this.getBoolean(PROP_STRICT_OPERATOR, false);
        this._unnamedSection = this.getBoolean(PROP_UNNAMED_SECTION, false);
        this._escape = this.getBoolean(PROP_ESCAPE, true);
        this._escapeKeyOnly = this.getBoolean(PROP_ESCAPE_KEY_ONLY, false);
        this._escapeNewline = this.getBoolean(PROP_ESCAPE_NEWLINE, true);
        this._pathSeparator = this.getChar(PROP_PATH_SEPARATOR, '/');
        this._tree = this.getBoolean(PROP_TREE, true);
        this._propertyFirstUpper = this.getBoolean(PROP_PROPERTY_FIRST_UPPER, false);
        this._lineSeparator = this.getString(PROP_LINE_SEPARATOR, DEFAULT_LINE_SEPARATOR);
        this._fileEncoding = this.getCharset(PROP_FILE_ENCODING, DEFAULT_FILE_ENCODING);
        this._comment = this.getBoolean(PROP_COMMENT, true);
        this._headerComment = this.getBoolean(PROP_HEADER_COMMENT, true);
    }

    public void setComment(boolean bl) {
        this._comment = bl;
    }

    public void setEmptyOption(boolean bl) {
        this._emptyOption = bl;
    }

    public void setEmptySection(boolean bl) {
        this._emptySection = bl;
    }

    public void setEscape(boolean bl) {
        this._escape = bl;
    }

    public void setEscapeKeyOnly(boolean bl) {
        this._escapeKeyOnly = bl;
    }

    public void setEscapeNewline(boolean bl) {
        this._escapeNewline = bl;
    }

    public void setFileEncoding(Charset charset) {
        this._fileEncoding = charset;
    }

    public void setGlobalSection(boolean bl) {
        this._globalSection = bl;
    }

    public void setGlobalSectionName(String string2) {
        this._globalSectionName = string2;
    }

    public void setHeaderComment(boolean bl) {
        this._headerComment = bl;
    }

    public void setInclude(boolean bl) {
        this._include = bl;
    }

    public void setLineSeparator(String string2) {
        this._lineSeparator = string2;
    }

    public void setLowerCaseOption(boolean bl) {
        this._lowerCaseOption = bl;
    }

    public void setLowerCaseSection(boolean bl) {
        this._lowerCaseSection = bl;
    }

    public void setMultiOption(boolean bl) {
        this._multiOption = bl;
    }

    public void setMultiSection(boolean bl) {
        this._multiSection = bl;
    }

    public void setPathSeparator(char c) {
        this._pathSeparator = c;
    }

    public void setPropertyFirstUpper(boolean bl) {
        this._propertyFirstUpper = bl;
    }

    public void setStrictOperator(boolean bl) {
        this._strictOperator = bl;
    }

    public void setTree(boolean bl) {
        this._tree = bl;
    }

    public void setUnnamedSection(boolean bl) {
        this._unnamedSection = bl;
    }
}

