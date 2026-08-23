/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto.PBKDF2;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import net.lingala.zip4j.crypto.PBKDF2.PRF;

public class MacBasedPRF
implements PRF {
    protected int hLen;
    protected Mac mac;
    protected String macAlgorithm;

    public MacBasedPRF(String object) {
        this.macAlgorithm = object;
        try {
            this.mac = object = Mac.getInstance((String)object);
            this.hLen = ((Mac)object).getMacLength();
            return;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }

    public MacBasedPRF(String object, String string2) {
        this.macAlgorithm = object;
        try {
            this.mac = object = Mac.getInstance((String)object, string2);
            this.hLen = ((Mac)object).getMacLength();
            return;
        }
        catch (NoSuchProviderException noSuchProviderException) {
            throw new RuntimeException(noSuchProviderException);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }

    public byte[] doFinal() {
        return this.mac.doFinal();
    }

    @Override
    public byte[] doFinal(byte[] byArray) {
        return this.mac.doFinal(byArray);
    }

    @Override
    public int getHLen() {
        return this.hLen;
    }

    @Override
    public void init(byte[] byArray) {
        try {
            Mac mac = this.mac;
            SecretKeySpec secretKeySpec = new SecretKeySpec(byArray, this.macAlgorithm);
            mac.init(secretKeySpec);
            return;
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new RuntimeException(invalidKeyException);
        }
    }

    public void update(byte[] byArray) {
        try {
            this.mac.update(byArray);
            return;
        }
        catch (IllegalStateException illegalStateException) {
            throw new RuntimeException(illegalStateException);
        }
    }

    public void update(byte[] byArray, int n, int n2) {
        try {
            this.mac.update(byArray, n, n2);
            return;
        }
        catch (IllegalStateException illegalStateException) {
            throw new RuntimeException(illegalStateException);
        }
    }
}

