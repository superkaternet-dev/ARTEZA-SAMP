/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.ini4j.spi.BeanTool;

public abstract class AbstractBeanInvocationHandler
implements InvocationHandler {
    private static final String ADD_PREFIX = "add";
    private static final String HAS_PREFIX = "has";
    private static final String PROPERTY_CHANGE_LISTENER = "PropertyChangeListener";
    private static final String READ_BOOLEAN_PREFIX = "is";
    private static final String READ_PREFIX = "get";
    private static final String REMOVE_PREFIX = "remove";
    private static final String VETOABLE_CHANGE_LISTENER = "VetoableChangeListener";
    private static final String WRITE_PREFIX = "set";
    private PropertyChangeSupport _pcSupport;
    private Object _proxy;
    private VetoableChangeSupport _vcSupport;

    private void updateProxy(Object object) {
        synchronized (this) {
            if (this._proxy == null) {
                this._proxy = object;
            }
            return;
        }
    }

    protected void addPropertyChangeListener(String string2, PropertyChangeListener propertyChangeListener) {
        synchronized (this) {
            if (this._pcSupport == null) {
                PropertyChangeSupport propertyChangeSupport;
                this._pcSupport = propertyChangeSupport = new PropertyChangeSupport(this._proxy);
            }
            this._pcSupport.addPropertyChangeListener(string2, propertyChangeListener);
            return;
        }
    }

    protected void addVetoableChangeListener(String string2, VetoableChangeListener vetoableChangeListener) {
        synchronized (this) {
            if (this._vcSupport == null) {
                VetoableChangeSupport vetoableChangeSupport;
                this._vcSupport = vetoableChangeSupport = new VetoableChangeSupport(this._proxy);
            }
            this._vcSupport.addVetoableChangeListener(string2, vetoableChangeListener);
            return;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void firePropertyChange(String string2, Object object, Object object2) {
        synchronized (this) {
            PropertyChangeSupport propertyChangeSupport = this._pcSupport;
            if (propertyChangeSupport != null) {
                void var3_3;
                void var2_2;
                propertyChangeSupport.firePropertyChange(string2, var2_2, var3_3);
            }
            return;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void fireVetoableChange(String string2, Object object, Object object2) throws PropertyVetoException {
        synchronized (this) {
            VetoableChangeSupport vetoableChangeSupport = this._vcSupport;
            if (vetoableChangeSupport != null) {
                void var3_3;
                void var2_2;
                vetoableChangeSupport.fireVetoableChange(string2, var2_2, var3_3);
            }
            return;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected Object getProperty(String var1_1, Class<?> var2_5) {
        synchronized (this) {
            block12: {
                try {
                    try {
                        var4_6 = this.getPropertySpi((String)var1_1, var2_5);
                        if (var4_6 == null) {
                        }
                        ** GOTO lbl13
                    }
                    catch (Exception var1_4) {
                        // empty catch block
                        return this.zero(var2_5);
                    }
                    try {
                        return this.zero(var2_5);
lbl13:
                        // 1 sources

                        if (var2_5.isArray() && var4_6 instanceof String[] && !var2_5.equals(String[].class)) {
                            var4_6 = var4_6;
                            var1_1 = Array.newInstance(var2_5.getComponentType(), var4_6.length);
                            var3_7 = 0;
                            while (var3_7 < var4_6.length) {
                                Array.set(var1_1, var3_7, this.parse(var4_6[var3_7], var2_5.getComponentType()));
                                ++var3_7;
                            }
                            return var1_1;
                        }
                        var1_1 = var4_6;
                    }
                    catch (Exception var1_2) {
                        return this.zero(var2_5);
                    }
                }
                catch (Throwable var1_3) {
                    break block12;
                }
                {
                    if (var4_6 instanceof String == false) return var1_1;
                    var1_1 = var4_6;
                    if (var2_5.equals(String.class) != false) return var1_1;
                    return this.parse((String)var4_6, var2_5);
                }
            }
            throw var1_3;
        }
    }

    protected abstract Object getPropertySpi(String var1, Class<?> var2);

    protected Object getProxy() {
        synchronized (this) {
            Object object = this._proxy;
            return object;
        }
    }

    protected boolean hasProperty(String string2) {
        synchronized (this) {
            boolean bl;
            try {
                bl = this.hasPropertySpi(string2);
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            catch (Exception exception) {
                bl = false;
            }
            return bl;
        }
    }

    protected abstract boolean hasPropertySpi(String var1);

    @Override
    public Object invoke(Object object, Method method, Object[] objectArray) throws PropertyVetoException {
        String string2 = null;
        Prefix prefix = Prefix.parse(method.getName());
        Object object2 = string2;
        if (prefix != null) {
            object2 = prefix.getTail(method.getName());
            this.updateProxy(object);
            switch (1.$SwitchMap$org$ini4j$spi$AbstractBeanInvocationHandler$Prefix[prefix.ordinal()]) {
                default: {
                    object2 = string2;
                    break;
                }
                case 8: {
                    this.removeVetoableChangeListener((String)objectArray[0], (VetoableChangeListener)objectArray[1]);
                    object2 = string2;
                    break;
                }
                case 7: {
                    this.removePropertyChangeListener((String)objectArray[0], (PropertyChangeListener)objectArray[1]);
                    object2 = string2;
                    break;
                }
                case 6: {
                    this.addVetoableChangeListener((String)objectArray[0], (VetoableChangeListener)objectArray[1]);
                    object2 = string2;
                    break;
                }
                case 5: {
                    this.addPropertyChangeListener((String)objectArray[0], (PropertyChangeListener)objectArray[1]);
                    object2 = string2;
                    break;
                }
                case 4: {
                    object2 = this.hasProperty(prefix.getTail(method.getName()));
                    break;
                }
                case 3: {
                    this.setProperty((String)object2, objectArray[0], method.getParameterTypes()[0]);
                    object2 = string2;
                    break;
                }
                case 2: {
                    object2 = this.getProperty(prefix.getTail(method.getName()), method.getReturnType());
                    break;
                }
                case 1: {
                    object2 = this.getProperty(prefix.getTail(method.getName()), method.getReturnType());
                }
            }
        }
        return object2;
    }

    protected Object parse(String string2, Class<?> clazz) throws IllegalArgumentException {
        return BeanTool.getInstance().parse(string2, clazz);
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void removePropertyChangeListener(String string2, PropertyChangeListener propertyChangeListener) {
        synchronized (this) {
            PropertyChangeSupport propertyChangeSupport = this._pcSupport;
            if (propertyChangeSupport != null) {
                void var2_2;
                propertyChangeSupport.removePropertyChangeListener(string2, (PropertyChangeListener)var2_2);
            }
            return;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void removeVetoableChangeListener(String string2, VetoableChangeListener vetoableChangeListener) {
        synchronized (this) {
            VetoableChangeSupport vetoableChangeSupport = this._vcSupport;
            if (vetoableChangeSupport != null) {
                void var2_2;
                vetoableChangeSupport.removeVetoableChangeListener(string2, (VetoableChangeListener)var2_2);
            }
            return;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void setProperty(String string2, Object object, Class<?> clazz) throws PropertyVetoException {
        synchronized (this) {
            void var3_3;
            void var2_2;
            Object object2 = this._pcSupport;
            boolean bl = true;
            boolean bl2 = object2 != null && ((PropertyChangeSupport)object2).hasListeners(string2);
            object2 = this._vcSupport;
            if (object2 == null || !((VetoableChangeSupport)object2).hasListeners(string2)) {
                bl = false;
            }
            Object object3 = null;
            object2 = var2_2 != null && var3_3.equals(String.class) && !(var2_2 instanceof String) ? var2_2.toString() : var2_2;
            if (bl2 || bl) {
                object3 = this.getProperty(string2, (Class<?>)var3_3);
            }
            if (bl) {
                this.fireVetoableChange(string2, object3, var2_2);
            }
            this.setPropertySpi(string2, object2, (Class<?>)var3_3);
            if (bl2) {
                this.firePropertyChange(string2, object3, var2_2);
            }
            return;
        }
    }

    protected abstract void setPropertySpi(String var1, Object var2, Class<?> var3);

    protected Object zero(Class<?> clazz) {
        return BeanTool.getInstance().zero(clazz);
    }

    private static final class Prefix
    extends Enum<Prefix> {
        private static final Prefix[] $VALUES;
        public static final /* enum */ Prefix ADD_CHANGE;
        public static final /* enum */ Prefix ADD_VETO;
        public static final /* enum */ Prefix HAS;
        public static final /* enum */ Prefix READ;
        public static final /* enum */ Prefix READ_BOOLEAN;
        public static final /* enum */ Prefix REMOVE_CHANGE;
        public static final /* enum */ Prefix REMOVE_VETO;
        public static final /* enum */ Prefix WRITE;
        private int _len;
        private String _value;

        static {
            Prefix prefix;
            Prefix prefix2;
            Prefix prefix3;
            Prefix prefix4;
            Prefix prefix5;
            Prefix prefix6;
            Prefix prefix7;
            Prefix prefix8;
            READ = prefix8 = new Prefix(AbstractBeanInvocationHandler.READ_PREFIX);
            READ_BOOLEAN = prefix7 = new Prefix(AbstractBeanInvocationHandler.READ_BOOLEAN_PREFIX);
            WRITE = prefix6 = new Prefix(AbstractBeanInvocationHandler.WRITE_PREFIX);
            ADD_CHANGE = prefix5 = new Prefix("addPropertyChangeListener");
            ADD_VETO = prefix4 = new Prefix("addVetoableChangeListener");
            REMOVE_CHANGE = prefix3 = new Prefix("removePropertyChangeListener");
            REMOVE_VETO = prefix2 = new Prefix("removeVetoableChangeListener");
            HAS = prefix = new Prefix(AbstractBeanInvocationHandler.HAS_PREFIX);
            $VALUES = new Prefix[]{prefix8, prefix7, prefix6, prefix5, prefix4, prefix3, prefix2, prefix};
        }

        private Prefix(String string3) {
            this._value = string3;
            this._len = string3.length();
        }

        public static Prefix parse(String string2) {
            Prefix prefix;
            Prefix prefix2 = null;
            Prefix[] prefixArray = Prefix.values();
            int n = prefixArray.length;
            int n2 = 0;
            while (true) {
                prefix = prefix2;
                if (n2 >= n || string2.startsWith((prefix = prefixArray[n2]).getValue())) break;
                ++n2;
            }
            return prefix;
        }

        public static Prefix valueOf(String string2) {
            return Enum.valueOf(Prefix.class, string2);
        }

        public static Prefix[] values() {
            return (Prefix[])$VALUES.clone();
        }

        public String getTail(String string2) {
            return Introspector.decapitalize(string2.substring(this._len));
        }

        public String getValue() {
            return this._value;
        }
    }
}

