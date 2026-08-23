/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ini4j.CommonMultiMap;
import org.ini4j.Config;
import org.ini4j.OptionMap;
import org.ini4j.spi.BeanAccess;
import org.ini4j.spi.BeanTool;

public class BasicOptionMap
extends CommonMultiMap<String, String>
implements OptionMap {
    private static final String ENVIRONMENT_PREFIX = "@env/";
    private static final int ENVIRONMENT_PREFIX_LEN;
    private static final Pattern EXPRESSION;
    private static final int G_INDEX = 4;
    private static final int G_OPTION = 2;
    private static final char SUBST_CHAR = '$';
    private static final String SYSTEM_PROPERTY_PREFIX = "@prop/";
    private static final int SYSTEM_PROPERTY_PREFIX_LEN;
    private static final long serialVersionUID = 325469712293707584L;
    private BeanAccess _defaultBeanAccess;
    private final boolean _propertyFirstUpper;

    static {
        SYSTEM_PROPERTY_PREFIX_LEN = SYSTEM_PROPERTY_PREFIX.length();
        ENVIRONMENT_PREFIX_LEN = ENVIRONMENT_PREFIX.length();
        EXPRESSION = Pattern.compile("(?<!\\\\)\\$\\{(([^\\[\\}]+)(\\[([0-9]+)\\])?)\\}");
    }

    public BasicOptionMap() {
        this(false);
    }

    public BasicOptionMap(boolean bl) {
        this._propertyFirstUpper = bl;
    }

    private void requireArray(Class clazz) {
        if (clazz.isArray()) {
            return;
        }
        throw new IllegalArgumentException("Array required");
    }

    @Override
    public void add(String string2, Object object) {
        object = object != null && !(object instanceof String) ? String.valueOf(object) : (String)object;
        super.add(string2, object);
    }

    @Override
    public void add(String string2, Object object, int n) {
        object = object != null && !(object instanceof String) ? String.valueOf(object) : (String)object;
        super.add(string2, object, n);
    }

    @Override
    public <T> T as(Class<T> clazz) {
        return BeanTool.getInstance().proxy(clazz, this.getDefaultBeanAccess());
    }

    @Override
    public <T> T as(Class<T> clazz, String string2) {
        return BeanTool.getInstance().proxy(clazz, this.newBeanAccess(string2));
    }

    @Override
    public <T> T fetch(Object object, int n, Class<T> clazz) {
        return BeanTool.getInstance().parse(this.fetch(object, n), clazz);
    }

    @Override
    public <T> T fetch(Object object, Class<T> clazz) {
        return BeanTool.getInstance().parse(this.fetch(object), clazz);
    }

    @Override
    public <T> T fetch(Object object, Class<T> clazz, T t) {
        object = (object = this.fetch(object)) == null ? t : BeanTool.getInstance().parse((String)object, clazz);
        return (T)object;
    }

    @Override
    public String fetch(Object object) {
        int n = this.length(object);
        object = n == 0 ? null : this.fetch(object, n - 1);
        return object;
    }

    @Override
    public String fetch(Object object, int n) {
        String string2 = (String)this.get(object, n);
        object = string2;
        if (string2 != null) {
            object = string2;
            if (string2.indexOf(36) >= 0) {
                object = new StringBuilder(string2);
                this.resolve((StringBuilder)object);
                object = ((StringBuilder)object).toString();
            }
        }
        return object;
    }

    @Override
    public String fetch(Object object, String object2) {
        if ((object = (String)this.get(object)) != null) {
            object2 = object;
        }
        return object2;
    }

    @Override
    public <T> T fetchAll(Object object, Class<T> clazz) {
        this.requireArray(clazz);
        Object object2 = Array.newInstance(clazz.getComponentType(), this.length(object));
        for (int i = 0; i < this.length(object); ++i) {
            Array.set(object2, i, BeanTool.getInstance().parse(this.fetch(object, i), clazz.getComponentType()));
        }
        return (T)object2;
    }

    @Override
    public void from(Object object) {
        BeanTool.getInstance().inject(this.getDefaultBeanAccess(), object);
    }

    @Override
    public void from(Object object, String string2) {
        BeanTool.getInstance().inject(this.newBeanAccess(string2), object);
    }

    @Override
    public <T> T get(Object object, int n, Class<T> clazz) {
        return BeanTool.getInstance().parse((String)this.get(object, n), clazz);
    }

    @Override
    public <T> T get(Object object, Class<T> clazz) {
        return BeanTool.getInstance().parse((String)this.get(object), clazz);
    }

    @Override
    public <T> T get(Object object, Class<T> clazz, T t) {
        object = (object = (String)this.get(object)) == null ? t : BeanTool.getInstance().parse((String)object, clazz);
        return (T)object;
    }

    @Override
    public String get(Object object, String string2) {
        block0: {
            if ((object = (String)this.get(object)) != null) break block0;
            object = string2;
        }
        return object;
    }

    @Override
    public <T> T getAll(Object object, Class<T> clazz) {
        this.requireArray(clazz);
        Object object2 = Array.newInstance(clazz.getComponentType(), this.length(object));
        for (int i = 0; i < this.length(object); ++i) {
            Array.set(object2, i, BeanTool.getInstance().parse((String)this.get(object, i), clazz.getComponentType()));
        }
        return (T)object2;
    }

    BeanAccess getDefaultBeanAccess() {
        synchronized (this) {
            if (this._defaultBeanAccess == null) {
                this._defaultBeanAccess = this.newBeanAccess();
            }
            BeanAccess beanAccess = this._defaultBeanAccess;
            return beanAccess;
        }
    }

    boolean isPropertyFirstUpper() {
        return this._propertyFirstUpper;
    }

    BeanAccess newBeanAccess() {
        return new Access(this);
    }

    BeanAccess newBeanAccess(String string2) {
        return new Access(this, string2);
    }

    @Override
    public String put(String string2, Object object) {
        object = object != null && !(object instanceof String) ? String.valueOf(object) : (String)object;
        return (String)super.put(string2, object);
    }

    @Override
    public String put(String string2, Object object, int n) {
        object = object != null && !(object instanceof String) ? String.valueOf(object) : (String)object;
        return (String)super.put(string2, object, n);
    }

    @Override
    public void putAll(String string2, Object object) {
        if (object != null) {
            this.requireArray(object.getClass());
        }
        this.remove(string2);
        if (object != null) {
            int n = Array.getLength(object);
            for (int i = 0; i < n; ++i) {
                this.add(string2, Array.get(object, i));
            }
        }
    }

    void resolve(StringBuilder stringBuilder) {
        Matcher matcher = EXPRESSION.matcher(stringBuilder);
        while (matcher.find()) {
            String string2 = matcher.group(2);
            int n = matcher.group(4) == null ? -1 : Integer.parseInt(matcher.group(4));
            if ((string2 = string2.startsWith(ENVIRONMENT_PREFIX) ? Config.getEnvironment(string2.substring(ENVIRONMENT_PREFIX_LEN)) : (string2.startsWith(SYSTEM_PROPERTY_PREFIX) ? Config.getSystemProperty(string2.substring(SYSTEM_PROPERTY_PREFIX_LEN)) : (n == -1 ? this.fetch(string2) : this.fetch((Object)string2, n)))) == null) continue;
            stringBuilder.replace(matcher.start(), matcher.end(), string2);
            matcher.reset(stringBuilder);
        }
    }

    @Override
    public void to(Object object) {
        BeanTool.getInstance().inject(object, this.getDefaultBeanAccess());
    }

    @Override
    public void to(Object object, String string2) {
        BeanTool.getInstance().inject(object, this.newBeanAccess(string2));
    }

    class Access
    implements BeanAccess {
        private final String _prefix;
        final BasicOptionMap this$0;

        Access(BasicOptionMap basicOptionMap) {
            this(basicOptionMap, null);
        }

        Access(BasicOptionMap basicOptionMap, String string2) {
            this.this$0 = basicOptionMap;
            this._prefix = string2;
        }

        private String transform(String string2) {
            String string3;
            block8: {
                CharSequence charSequence;
                block7: {
                    charSequence = string2;
                    if (this._prefix != null) break block7;
                    string3 = charSequence;
                    if (!this.this$0.isPropertyFirstUpper()) break block8;
                }
                string3 = charSequence;
                if (string2 != null) {
                    charSequence = new StringBuilder();
                    string3 = this._prefix;
                    if (string3 != null) {
                        ((StringBuilder)charSequence).append(string3);
                    }
                    if (this.this$0.isPropertyFirstUpper()) {
                        ((StringBuilder)charSequence).append(Character.toUpperCase(string2.charAt(0)));
                        ((StringBuilder)charSequence).append(string2.substring(1));
                    } else {
                        ((StringBuilder)charSequence).append(string2);
                    }
                    string3 = ((StringBuilder)charSequence).toString();
                }
            }
            return string3;
        }

        @Override
        public void propAdd(String string2, String string3) {
            this.this$0.add(this.transform(string2), string3);
        }

        @Override
        public String propDel(String string2) {
            return (String)this.this$0.remove(this.transform(string2));
        }

        @Override
        public String propGet(String string2) {
            return this.this$0.fetch(this.transform(string2));
        }

        @Override
        public String propGet(String string2, int n) {
            return this.this$0.fetch((Object)this.transform(string2), n);
        }

        @Override
        public int propLength(String string2) {
            return this.this$0.length(this.transform(string2));
        }

        @Override
        public String propSet(String string2, String string3) {
            return this.this$0.put(this.transform(string2), string3);
        }

        @Override
        public String propSet(String string2, String string3, int n) {
            return this.this$0.put(this.transform(string2), string3, n);
        }
    }
}

