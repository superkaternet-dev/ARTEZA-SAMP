/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ini4j.BasicProfileSection;
import org.ini4j.CommonMultiMap;
import org.ini4j.Config;
import org.ini4j.Profile;
import org.ini4j.spi.AbstractBeanInvocationHandler;
import org.ini4j.spi.BeanTool;
import org.ini4j.spi.IniHandler;

public class BasicProfile
extends CommonMultiMap<String, Profile.Section>
implements Profile {
    private static final Pattern EXPRESSION = Pattern.compile("(?<!\\\\)\\$\\{(([^\\[\\}]+)(\\[([0-9]+)\\])?/)?([^\\[^/\\}]+)(\\[(([0-9]+))\\])?\\}");
    private static final int G_OPTION = 5;
    private static final int G_OPTION_IDX = 7;
    private static final int G_SECTION = 2;
    private static final int G_SECTION_IDX = 4;
    private static final String SECTION_ENVIRONMENT = "@env";
    private static final String SECTION_SYSTEM_PROPERTIES = "@prop";
    private static final long serialVersionUID = -1817521505004015256L;
    private String _comment;
    private final boolean _propertyFirstUpper;
    private final boolean _treeMode;

    public BasicProfile() {
        this(false, false);
    }

    public BasicProfile(boolean bl, boolean bl2) {
        this._treeMode = bl;
        this._propertyFirstUpper = bl2;
    }

    private Profile.Section getOrAdd(String object) {
        Profile.Section section = (Profile.Section)this.get(object);
        object = section == null ? this.add((String)object) : section;
        return object;
    }

    private int parseOptionIndex(Matcher matcher) {
        int n = matcher.group(7) == null ? -1 : Integer.parseInt(matcher.group(7));
        return n;
    }

    private Profile.Section parseSection(Matcher matcher, Profile.Section section) {
        String string2 = matcher.group(2);
        int n = this.parseSectionIndex(matcher);
        if (string2 != null) {
            matcher = n == -1 ? this.get(string2) : this.get((Object)string2, n);
            section = (Profile.Section)((Object)matcher);
        }
        return section;
    }

    private int parseSectionIndex(Matcher matcher) {
        int n = matcher.group(4) == null ? -1 : Integer.parseInt(matcher.group(4));
        return n;
    }

    @Override
    public Profile.Section add(String string2) {
        Object object;
        int n;
        if (this.isTreeMode() && (n = string2.lastIndexOf(this.getPathSeparator())) > 0 && !this.containsKey(object = string2.substring(0, n))) {
            this.add((String)object);
        }
        object = this.newSection(string2);
        this.add(string2, object);
        return object;
    }

    @Override
    public void add(String string2, String string3, Object object) {
        this.getOrAdd(string2).add(string3, object);
    }

    @Override
    public <T> T as(Class<T> clazz) {
        return this.as(clazz, null);
    }

    @Override
    public <T> T as(Class<T> clazz, String object) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        object = new BeanInvocationHandler(this, (String)object);
        return clazz.cast(Proxy.newProxyInstance(classLoader, new Class[]{clazz}, (InvocationHandler)object));
    }

    @Override
    public <T> T fetch(Object object, Object object2, Class<T> clazz) {
        object = (object = (Profile.Section)this.get(object)) == null ? BeanTool.getInstance().zero(clazz) : object.fetch(object2, clazz);
        return (T)object;
    }

    @Override
    public String fetch(Object object, Object object2) {
        object = (object = (Profile.Section)this.get(object)) == null ? null : object.fetch(object2);
        return object;
    }

    @Override
    public <T> T get(Object object, Object object2, Class<T> clazz) {
        object = (object = (Profile.Section)this.get(object)) == null ? BeanTool.getInstance().zero(clazz) : object.get(object2, clazz);
        return (T)object;
    }

    @Override
    public String get(Object object, Object object2) {
        object = (object = (Profile.Section)this.get(object)) == null ? null : (String)object.get(object2);
        return object;
    }

    @Override
    public String getComment() {
        return this._comment;
    }

    char getPathSeparator() {
        return '/';
    }

    boolean isPropertyFirstUpper() {
        return this._propertyFirstUpper;
    }

    boolean isTreeMode() {
        return this._treeMode;
    }

    Profile.Section newSection(String string2) {
        return new BasicProfileSection(this, string2);
    }

    @Override
    public String put(String string2, String string3, Object object) {
        return this.getOrAdd(string2).put(string3, object);
    }

    @Override
    public String remove(Object object, Object object2) {
        object = (object = (Profile.Section)this.get(object)) == null ? null : (String)object.remove(object2);
        return object;
    }

    @Override
    public Profile.Section remove(Profile.Section section) {
        return (Profile.Section)this.remove(section.getName());
    }

    void resolve(StringBuilder stringBuilder, Profile.Section section) {
        Matcher matcher = EXPRESSION.matcher(stringBuilder);
        while (matcher.find()) {
            String string2 = matcher.group(2);
            String string3 = matcher.group(5);
            int n = this.parseOptionIndex(matcher);
            Profile.Section section2 = this.parseSection(matcher, section);
            String string4 = null;
            if (SECTION_ENVIRONMENT.equals(string2)) {
                string4 = Config.getEnvironment(string3);
            } else if (SECTION_SYSTEM_PROPERTIES.equals(string2)) {
                string4 = Config.getSystemProperty(string3);
            } else if (section2 != null) {
                string4 = n == -1 ? section2.fetch(string3) : section2.fetch((Object)string3, n);
            }
            if (string4 == null) continue;
            stringBuilder.replace(matcher.start(), matcher.end(), string4);
            matcher.reset(stringBuilder);
        }
    }

    @Override
    public void setComment(String string2) {
        this._comment = string2;
    }

    void store(IniHandler iniHandler) {
        iniHandler.startIni();
        this.store(iniHandler, this.getComment());
        Iterator iterator2 = this.values().iterator();
        while (iterator2.hasNext()) {
            this.store(iniHandler, (Profile.Section)iterator2.next());
        }
        iniHandler.endIni();
    }

    void store(IniHandler iniHandler, String string2) {
        iniHandler.handleComment(string2);
    }

    void store(IniHandler iniHandler, Profile.Section section) {
        this.store(iniHandler, this.getComment(section.getName()));
        iniHandler.startSection(section.getName());
        Iterator iterator2 = section.keySet().iterator();
        while (iterator2.hasNext()) {
            this.store(iniHandler, section, (String)iterator2.next());
        }
        iniHandler.endSection();
    }

    void store(IniHandler iniHandler, Profile.Section section, String string2) {
        this.store(iniHandler, section.getComment(string2));
        int n = section.length(string2);
        for (int i = 0; i < n; ++i) {
            this.store(iniHandler, section, string2, i);
        }
    }

    void store(IniHandler iniHandler, Profile.Section section, String string2, int n) {
        iniHandler.handleOption(string2, (String)section.get((Object)string2, n));
    }

    private final class BeanInvocationHandler
    extends AbstractBeanInvocationHandler {
        private final String _prefix;
        final BasicProfile this$0;

        private BeanInvocationHandler(BasicProfile basicProfile, String string2) {
            this.this$0 = basicProfile;
            this._prefix = string2;
        }

        @Override
        protected Object getPropertySpi(String object, Class<?> clazz) {
            String string2 = this.transform((String)object);
            object = null;
            if (this.this$0.containsKey(string2)) {
                if (clazz.isArray()) {
                    object = Array.newInstance(clazz.getComponentType(), this.this$0.length(string2));
                    for (int i = 0; i < this.this$0.length(string2); ++i) {
                        Array.set(object, i, ((Profile.Section)this.this$0.get((Object)string2, i)).as(clazz.getComponentType()));
                    }
                } else {
                    object = ((Profile.Section)this.this$0.get(string2)).as(clazz);
                }
            }
            return object;
        }

        @Override
        protected boolean hasPropertySpi(String string2) {
            return this.this$0.containsKey(this.transform(string2));
        }

        @Override
        protected void setPropertySpi(String string2, Object object, Class<?> clazz) {
            string2 = this.transform(string2);
            this.this$0.remove(string2);
            if (object != null) {
                if (clazz.isArray()) {
                    for (int i = 0; i < Array.getLength(object); ++i) {
                        this.this$0.add(string2).from(Array.get(object, i));
                    }
                } else {
                    this.this$0.add(string2).from(object);
                }
            }
        }

        String transform(String string2) {
            CharSequence charSequence;
            if (this._prefix == null) {
                charSequence = string2;
            } else {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(this._prefix);
                ((StringBuilder)charSequence).append(string2);
                charSequence = ((StringBuilder)charSequence).toString();
            }
            if (this.this$0.isPropertyFirstUpper()) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(Character.toUpperCase(string2.charAt(0)));
                ((StringBuilder)charSequence).append(string2.substring(1));
                charSequence = ((StringBuilder)charSequence).toString();
            }
            return charSequence;
        }
    }
}

