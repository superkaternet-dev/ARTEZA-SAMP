/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.util.ArrayList;
import java.util.regex.Pattern;
import org.ini4j.BasicOptionMap;
import org.ini4j.BasicProfile;
import org.ini4j.Profile;

class BasicProfileSection
extends BasicOptionMap
implements Profile.Section {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final char REGEXP_ESCAPE_CHAR = '\\';
    private static final long serialVersionUID = 985800697957194374L;
    private final Pattern _childPattern;
    private final String _name;
    private final BasicProfile _profile;

    protected BasicProfileSection(BasicProfile basicProfile, String string2) {
        this._profile = basicProfile;
        this._name = string2;
        this._childPattern = this.newChildPattern(string2);
    }

    private String childName(String string2) {
        StringBuilder stringBuilder = new StringBuilder(this._name);
        stringBuilder.append(this._profile.getPathSeparator());
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    private Pattern newChildPattern(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('^');
        stringBuilder.append(Pattern.quote(string2));
        stringBuilder.append('\\');
        stringBuilder.append(this._profile.getPathSeparator());
        stringBuilder.append("[^");
        stringBuilder.append('\\');
        stringBuilder.append(this._profile.getPathSeparator());
        stringBuilder.append("]+$");
        return Pattern.compile(stringBuilder.toString());
    }

    @Override
    public Profile.Section addChild(String string2) {
        string2 = this.childName(string2);
        return this._profile.add(string2);
    }

    @Override
    public String[] childrenNames() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : this._profile.keySet()) {
            if (!this._childPattern.matcher(string2).matches()) continue;
            arrayList.add(string2.substring(this._name.length() + 1));
        }
        return arrayList.toArray(EMPTY_STRING_ARRAY);
    }

    @Override
    public Profile.Section getChild(String string2) {
        return (Profile.Section)this._profile.get(this.childName(string2));
    }

    @Override
    public String getName() {
        return this._name;
    }

    @Override
    public Profile.Section getParent() {
        Object object = null;
        int n = this._name.lastIndexOf(this._profile.getPathSeparator());
        if (n >= 0) {
            object = this._name.substring(0, n);
            object = (Profile.Section)this._profile.get(object);
        }
        return object;
    }

    @Override
    public String getSimpleName() {
        int n = this._name.lastIndexOf(this._profile.getPathSeparator());
        String string2 = this._name;
        if (n >= 0) {
            string2 = string2.substring(n + 1);
        }
        return string2;
    }

    @Override
    boolean isPropertyFirstUpper() {
        return this._profile.isPropertyFirstUpper();
    }

    @Override
    public Profile.Section lookup(String ... stringArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : stringArray) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(this._profile.getPathSeparator());
            }
            stringBuilder.append(string2);
        }
        return (Profile.Section)this._profile.get(this.childName(stringBuilder.toString()));
    }

    @Override
    public void removeChild(String string2) {
        string2 = this.childName(string2);
        this._profile.remove(string2);
    }

    @Override
    void resolve(StringBuilder stringBuilder) {
        this._profile.resolve(stringBuilder, this);
    }
}

