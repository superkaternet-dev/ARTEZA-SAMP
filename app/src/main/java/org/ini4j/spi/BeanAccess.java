/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

public interface BeanAccess {
    public void propAdd(String var1, String var2);

    public String propDel(String var1);

    public String propGet(String var1);

    public String propGet(String var1, int var2);

    public int propLength(String var1);

    public String propSet(String var1, String var2);

    public String propSet(String var1, String var2, int var3);
}

