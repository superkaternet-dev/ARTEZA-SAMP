/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto.PBKDF2;

interface PRF {
    public byte[] doFinal(byte[] var1);

    public int getHLen();

    public void init(byte[] var1);
}

