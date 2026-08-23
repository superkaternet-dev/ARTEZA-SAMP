/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto;

import java.util.Random;
import net.lingala.zip4j.crypto.IEncrypter;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.Raw;

public class AESEncrpyter
implements IEncrypter {
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private final int PASSWORD_VERIFIER_LENGTH;
    private int SALT_LENGTH;
    private AESEngine aesEngine;
    private byte[] aesKey;
    private byte[] counterBlock;
    private byte[] derivedPasswordVerifier;
    private boolean finished;
    private byte[] iv;
    private int keyStrength;
    private int loopCount = 0;
    private MacBasedPRF mac;
    private byte[] macKey;
    private int nonce = 1;
    private char[] password;
    private byte[] saltBytes;

    public AESEncrpyter(char[] cArray, int n) throws ZipException {
        this.PASSWORD_VERIFIER_LENGTH = 2;
        if (cArray != null && cArray.length != 0) {
            if (n != 1 && n != 3) {
                throw new ZipException("Invalid key strength in AES encrypter constructor");
            }
            this.password = cArray;
            this.keyStrength = n;
            this.finished = false;
            this.counterBlock = new byte[16];
            this.iv = new byte[16];
            this.init();
            return;
        }
        throw new ZipException("input password is empty or null in AES encrypter constructor");
    }

    private byte[] deriveKey(byte[] object, char[] cArray) throws ZipException {
        try {
            PBKDF2Parameters pBKDF2Parameters = new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", (byte[])object, 1000);
            object = new PBKDF2Engine;
            object(pBKDF2Parameters);
            object = object.deriveKey(cArray, this.KEY_LENGTH + this.MAC_LENGTH + 2);
            return object;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

    private static byte[] generateSalt(int n) throws ZipException {
        if (n != 8 && n != 16) {
            throw new ZipException("invalid salt size, cannot generate salt");
        }
        int n2 = 0;
        if (n == 8) {
            n2 = 2;
        }
        if (n == 16) {
            n2 = 4;
        }
        byte[] byArray = new byte[n];
        for (n = 0; n < n2; ++n) {
            int n3 = new Random().nextInt();
            byArray[n * 4 + 0] = (byte)(n3 >> 24);
            byArray[n * 4 + 1] = (byte)(n3 >> 16);
            byArray[n * 4 + 2] = (byte)(n3 >> 8);
            byArray[n * 4 + 3] = (byte)n3;
        }
        return byArray;
    }

    private void init() throws ZipException {
        int n;
        int n2;
        int n3;
        switch (this.keyStrength) {
            default: {
                throw new ZipException("invalid aes key strength, cannot determine key sizes");
            }
            case 3: {
                this.KEY_LENGTH = 32;
                this.MAC_LENGTH = 32;
                this.SALT_LENGTH = 16;
                break;
            }
            case 1: {
                this.KEY_LENGTH = 16;
                this.MAC_LENGTH = 16;
                this.SALT_LENGTH = 8;
            }
        }
        Object object = AESEncrpyter.generateSalt(this.SALT_LENGTH);
        this.saltBytes = object;
        object = this.deriveKey((byte[])object, this.password);
        if (object != null && (n3 = ((byte[])object).length) == (n2 = this.KEY_LENGTH) + (n = this.MAC_LENGTH) + 2) {
            byte[] byArray = new byte[n2];
            this.aesKey = byArray;
            this.macKey = new byte[n];
            this.derivedPasswordVerifier = new byte[2];
            System.arraycopy(object, 0, byArray, 0, n2);
            System.arraycopy(object, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
            System.arraycopy(object, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
            this.aesEngine = new AESEngine(this.aesKey);
            object = new MacBasedPRF("HmacSHA1");
            this.mac = object;
            ((MacBasedPRF)object).init(this.macKey);
            return;
        }
        throw new ZipException("invalid key generated, cannot decrypt file");
    }

    @Override
    public int encryptData(byte[] byArray) throws ZipException {
        if (byArray != null) {
            return this.encryptData(byArray, 0, byArray.length);
        }
        throw new ZipException("input bytes are null, cannot perform AES encrpytion");
    }

    @Override
    public int encryptData(byte[] object, int n, int n2) throws ZipException {
        if (!this.finished) {
            if (n2 % 16 != 0) {
                this.finished = true;
            }
            for (int i = n; i < n + n2; i += 16) {
                int n3;
                int n4 = i + 16 <= n + n2 ? 16 : n + n2 - i;
                this.loopCount = n4;
                Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
                this.aesEngine.processBlock(this.iv, this.counterBlock);
                for (n4 = 0; n4 < (n3 = this.loopCount); ++n4) {
                    object[i + n4] = (byte)(object[i + n4] ^ this.counterBlock[n4]);
                }
                this.mac.update((byte[])object, i, n3);
                ++this.nonce;
            }
            return n2;
        }
        object = new ZipException("AES Encrypter is in finished state (A non 16 byte block has already been passed to encrypter)");
        throw object;
    }

    public byte[] getDerivedPasswordVerifier() {
        return this.derivedPasswordVerifier;
    }

    public byte[] getFinalMac() {
        byte[] byArray = this.mac.doFinal();
        byte[] byArray2 = new byte[10];
        System.arraycopy(byArray, 0, byArray2, 0, 10);
        return byArray2;
    }

    public int getPasswordVeriifierLength() {
        return 2;
    }

    public byte[] getSaltBytes() {
        return this.saltBytes;
    }

    public int getSaltLength() {
        return this.SALT_LENGTH;
    }

    public void setDerivedPasswordVerifier(byte[] byArray) {
        this.derivedPasswordVerifier = byArray;
    }

    public void setSaltBytes(byte[] byArray) {
        this.saltBytes = byArray;
    }
}

