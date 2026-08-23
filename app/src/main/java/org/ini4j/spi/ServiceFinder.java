/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

final class ServiceFinder {
    private static final String SERVICES_PATH = "META-INF/services/";

    private ServiceFinder() {
    }

    static <T> T findService(Class<T> clazz) {
        T t;
        try {
            t = clazz.cast(ServiceFinder.findServiceClass(clazz).newInstance());
        }
        catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Provider ");
            stringBuilder.append(clazz.getName());
            stringBuilder.append(" could not be instantiated: ");
            stringBuilder.append(exception);
            throw (IllegalArgumentException)new IllegalArgumentException(stringBuilder.toString()).initCause(exception);
        }
        return t;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static <T> Class<? extends T> findServiceClass(Class<T> serializable) throws IllegalArgumentException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String string2 = ServiceFinder.findServiceClassName(((Class)serializable).getName());
        if (string2 == null) return serializable;
        if (classLoader != null) return classLoader.loadClass(string2);
        try {
            return Class.forName(string2);
        }
        catch (ClassNotFoundException classNotFoundException) {
            serializable = new StringBuilder();
            ((StringBuilder)serializable).append("Provider ");
            ((StringBuilder)serializable).append(string2);
            ((StringBuilder)serializable).append(" not found");
            throw (IllegalArgumentException)new IllegalArgumentException(((StringBuilder)serializable).toString()).initCause(classNotFoundException);
        }
    }

    static String findServiceClassName(String string2) throws IllegalArgumentException {
        String string3;
        CharSequence charSequence = null;
        String string4 = null;
        try {
            string3 = System.getProperty(string2);
            charSequence = string4;
            if (string3 != null) {
                charSequence = string3;
            }
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        string3 = charSequence;
        if (charSequence == null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(SERVICES_PATH);
            ((StringBuilder)charSequence).append(string2);
            string3 = ServiceFinder.loadLine(((StringBuilder)charSequence).toString());
        }
        return string3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static String loadLine(String object) {
        Object var2_2 = null;
        Object var3_3 = null;
        try {
            Object object2 = Thread.currentThread().getContextClassLoader();
            object2 = object2 == null ? ClassLoader.getSystemResourceAsStream((String)object) : ((ClassLoader)object2).getResourceAsStream((String)object);
            object = var3_3;
            if (object2 == null) return object;
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream)object2, "UTF-8");
            object = new BufferedReader(inputStreamReader);
            object2 = ((BufferedReader)object).readLine();
            ((BufferedReader)object).close();
            object = var3_3;
            if (object2 == null) return object;
            object2 = ((String)object2).trim();
            object = var3_3;
            if (((String)object2).length() == 0) return object;
            return ((String)object2).split("\\s|#")[0];
        }
        catch (Exception exception) {
            return var2_2;
        }
    }
}

