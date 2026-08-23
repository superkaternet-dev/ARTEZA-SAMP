/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.TimeZone;
import org.ini4j.spi.AbstractBeanInvocationHandler;
import org.ini4j.spi.BeanAccess;
import org.ini4j.spi.ServiceFinder;

public class BeanTool {
    private static final BeanTool INSTANCE = ServiceFinder.findService(BeanTool.class);
    private static final String PARSE_METHOD = "valueOf";

    public static final BeanTool getInstance() {
        return INSTANCE;
    }

    private PropertyDescriptor[] getPropertyDescriptors(Class propertyDescriptorArray) {
        try {
            propertyDescriptorArray = Introspector.getBeanInfo(propertyDescriptorArray).getPropertyDescriptors();
            return propertyDescriptorArray;
        }
        catch (IntrospectionException introspectionException) {
            throw new IllegalArgumentException(introspectionException);
        }
    }

    private Object parsePrimitiveValue(String string2, Class clazz) throws IllegalArgumentException {
        Comparable<Boolean> comparable = null;
        try {
            if (clazz == Boolean.TYPE) {
                comparable = Boolean.valueOf(string2);
            } else if (clazz == Byte.TYPE) {
                comparable = Byte.valueOf(string2);
            } else if (clazz == Character.TYPE) {
                comparable = new Character(string2.charAt(0));
            } else if (clazz == Double.TYPE) {
                comparable = Double.valueOf(string2);
            } else if (clazz == Float.TYPE) {
                comparable = Float.valueOf(string2);
            } else if (clazz == Integer.TYPE) {
                comparable = Integer.valueOf(string2);
            } else if (clazz == Long.TYPE) {
                comparable = Long.valueOf(string2);
            } else if (clazz == Short.TYPE) {
                comparable = Short.valueOf(string2);
            }
            return comparable;
        }
        catch (Exception exception) {
            throw (IllegalArgumentException)new IllegalArgumentException().initCause(exception);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void inject(Object object, BeanAccess object2) {
        PropertyDescriptor propertyDescriptor;
        PropertyDescriptor[] propertyDescriptorArray = this.getPropertyDescriptors(object.getClass());
        int n = propertyDescriptorArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                return;
            }
            propertyDescriptor = propertyDescriptorArray[n2];
            Method method = propertyDescriptor.getWriteMethod();
            String string2 = propertyDescriptor.getName();
            if (method != null && object2.propLength(string2) != 0) {
                Object object3;
                if (propertyDescriptor.getPropertyType().isArray()) {
                    object3 = Array.newInstance(propertyDescriptor.getPropertyType().getComponentType(), object2.propLength(string2));
                    for (int i = 0; i < object2.propLength(string2); ++i) {
                        Array.set(object3, i, this.parse(object2.propGet(string2, i), propertyDescriptor.getPropertyType().getComponentType()));
                    }
                } else {
                    object3 = this.parse(object2.propGet(string2), propertyDescriptor.getPropertyType());
                }
                method.invoke(object, object3);
            }
            ++n2;
            continue;
            break;
        }
        catch (Exception exception) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Failed to set property: ");
            ((StringBuilder)object2).append(propertyDescriptor.getDisplayName());
            throw (IllegalArgumentException)new IllegalArgumentException(((StringBuilder)object2).toString()).initCause(exception);
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void inject(BeanAccess object, Object object2) {
        PropertyDescriptor[] propertyDescriptorArray = this.getPropertyDescriptors(object2.getClass());
        int n = propertyDescriptorArray.length;
        int n2 = 0;
        while (true) {
            block9: {
                Object object3;
                if (n2 >= n) {
                    return;
                }
                PropertyDescriptor propertyDescriptor = propertyDescriptorArray[n2];
                try {
                    Object object4 = propertyDescriptor.getReadMethod();
                    if (object4 == null || "class".equals(propertyDescriptor.getName()) || (object3 = ((Method)object4).invoke(object2, (Object[])null)) == null) break block9;
                    if (propertyDescriptor.getPropertyType().isArray()) {
                        for (int i = 0; i < Array.getLength(object3); ++i) {
                            Object object5;
                            object4 = object5 = Array.get(object3, i);
                            if (object5 != null) {
                                object4 = object5;
                                if (!object5.getClass().equals(String.class)) {
                                    object4 = object5.toString();
                                }
                            }
                            object.propAdd(propertyDescriptor.getName(), (String)object4);
                        }
                        break block9;
                    }
                }
                catch (Exception exception) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Failed to set property: ");
                    ((StringBuilder)object).append(propertyDescriptor.getDisplayName());
                    throw new IllegalArgumentException(((StringBuilder)object).toString(), exception);
                }
                {
                    object.propSet(propertyDescriptor.getName(), object3.toString());
                }
            }
            ++n2;
        }
    }

    public <T> T parse(String object, Class<T> clazz) throws IllegalArgumentException {
        if (clazz != null) {
            if (object == null) {
                object = this.zero(clazz);
            } else if (clazz.isPrimitive()) {
                object = this.parsePrimitiveValue((String)object, clazz);
            } else if (clazz != String.class) {
                object = clazz == Character.class ? new Character(((String)object).charAt(0)) : this.parseSpecialValue((String)object, clazz);
            }
            return (T)object;
        }
        throw new IllegalArgumentException("null argument");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected Object parseSpecialValue(String object, Class clazz) throws IllegalArgumentException {
        if (clazz == File.class) {
            return new File((String)object);
        }
        if (clazz == URL.class) {
            return new URL((String)object);
        }
        if (clazz == URI.class) {
            return new URI((String)object);
        }
        if (clazz == Class.class) {
            return Class.forName((String)object);
        }
        if (clazz != TimeZone.class) return clazz.getMethod(PARSE_METHOD, String.class).invoke(null, object);
        try {
            return TimeZone.getTimeZone(object);
        }
        catch (Exception exception) {
            throw (IllegalArgumentException)new IllegalArgumentException().initCause(exception);
        }
    }

    public <T> T proxy(Class<T> clazz, BeanAccess object) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        object = new BeanInvocationHandler((BeanAccess)object);
        return clazz.cast(Proxy.newProxyInstance(classLoader, new Class[]{clazz}, (InvocationHandler)object));
    }

    public <T> T zero(Class<T> clazz) {
        Boolean bl;
        Comparable<Boolean> comparable = bl = null;
        if (clazz.isPrimitive()) {
            if (clazz == Boolean.TYPE) {
                comparable = Boolean.FALSE;
            } else if (clazz == Byte.TYPE) {
                comparable = (byte)0;
            } else if (clazz == Character.TYPE) {
                comparable = new Character('\u0000');
            } else if (clazz == Double.TYPE) {
                comparable = new Double(0.0);
            } else if (clazz == Float.TYPE) {
                comparable = new Float(0.0f);
            } else if (clazz == Integer.TYPE) {
                comparable = 0;
            } else if (clazz == Long.TYPE) {
                comparable = 0L;
            } else {
                comparable = bl;
                if (clazz == Short.TYPE) {
                    comparable = (short)0;
                }
            }
        }
        return (T)comparable;
    }

    static class BeanInvocationHandler
    extends AbstractBeanInvocationHandler {
        private final BeanAccess _backend;

        BeanInvocationHandler(BeanAccess beanAccess) {
            this._backend = beanAccess;
        }

        @Override
        protected Object getPropertySpi(String string2, Class<?> stringArray) {
            Object var4_3 = null;
            if (stringArray.isArray()) {
                int n = this._backend.propLength(string2);
                stringArray = var4_3;
                if (n != 0) {
                    stringArray = new String[n];
                    for (n = 0; n < stringArray.length; ++n) {
                        stringArray[n] = this._backend.propGet(string2, n);
                    }
                }
            } else {
                stringArray = this._backend.propGet(string2);
            }
            return stringArray;
        }

        @Override
        protected boolean hasPropertySpi(String string2) {
            boolean bl = this._backend.propLength(string2) != 0;
            return bl;
        }

        @Override
        protected void setPropertySpi(String string2, Object object, Class<?> clazz) {
            if (clazz.isArray()) {
                this._backend.propDel(string2);
                for (int i = 0; i < Array.getLength(object); ++i) {
                    this._backend.propAdd(string2, Array.get(object, i).toString());
                }
            } else {
                this._backend.propSet(string2, object.toString());
            }
        }
    }
}

