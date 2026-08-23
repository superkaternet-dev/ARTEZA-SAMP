/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto;

import java.util.Arrays;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.util.Raw;

public class AESDecrypter
implements IDecrypter {
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private final int PASSWORD_VERIFIER_LENGTH;
    private int SALT_LENGTH;
    private AESEngine aesEngine;
    private byte[] aesKey;
    private byte[] counterBlock;
    private byte[] derivedPasswordVerifier;
    private byte[] iv;
    private LocalFileHeader localFileHeader;
    private int loopCount = 0;
    private MacBasedPRF mac;
    private byte[] macKey;
    private int nonce = 1;
    private byte[] storedMac;

    public AESDecrypter(LocalFileHeader localFileHeader, byte[] byArray, byte[] byArray2) throws ZipException {
        this.PASSWORD_VERIFIER_LENGTH = 2;
        if (localFileHeader != null) {
            this.localFileHeader = localFileHeader;
            this.storedMac = null;
            this.iv = new byte[16];
            this.counterBlock = new byte[16];
            this.init(byArray, byArray2);
            return;
        }
        throw new ZipException("one of the input parameters is null in AESDecryptor Constructor");
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

    private void init(byte[] object, byte[] byArray) throws ZipException {
        Object object2 = this.localFileHeader;
        if (object2 != null) {
            if ((object2 = ((LocalFileHeader)object2).getAesExtraDataRecord()) != null) {
                switch (((AESExtraDataRecord)object2).getAesStrength()) {
                    default: {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("invalid aes key strength for file: ");
                        ((StringBuilder)object).append(this.localFileHeader.getFileName());
                        throw new ZipException(((StringBuilder)object).toString());
                    }
                    case 3: {
                        this.KEY_LENGTH = 32;
                        this.MAC_LENGTH = 32;
                        this.SALT_LENGTH = 16;
                        break;
                    }
                    case 2: {
                        this.KEY_LENGTH = 24;
                        this.MAC_LENGTH = 24;
                        this.SALT_LENGTH = 12;
                        break;
                    }
                    case 1: {
                        this.KEY_LENGTH = 16;
                        this.MAC_LENGTH = 16;
                        this.SALT_LENGTH = 8;
                    }
                }
                if (this.localFileHeader.getPassword() != null && this.localFileHeader.getPassword().length > 0) {
                    int n;
                    int n2;
                    int n3;
                    if ((object = (Object)this.deriveKey((byte[])object, this.localFileHeader.getPassword())) != null && (n3 = ((byte[])object).length) == (n2 = this.KEY_LENGTH) + (n = this.MAC_LENGTH) + 2) {
                        object2 = new byte[n2];
                        this.aesKey = (byte[])object2;
                        this.macKey = new byte[n];
                        this.derivedPasswordVerifier = new byte[2];
                        System.arraycopy(object, 0, object2, 0, n2);
                        System.arraycopy(object, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
                        System.arraycopy(object, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
                        object = this.derivedPasswordVerifier;
                        if (object != null) {
                            if (Arrays.equals(byArray, (byte[])object)) {
                                this.aesEngine = new AESEngine(this.aesKey);
                                this.mac = object = new MacBasedPRF("HmacSHA1");
                                ((MacBasedPRF)object).init(this.macKey);
                                return;
                            }
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Wrong Password for file: ");
                            ((StringBuilder)object).append(this.localFileHeader.getFileName());
                            throw new ZipException(((StringBuilder)object).toString(), 5);
                        }
                        throw new ZipException("invalid derived password verifier for AES");
                    }
                    throw new ZipException("invalid derived key");
                }
                throw new ZipException("empty or null password provided for AES Decryptor");
            }
            throw new ZipException("invalid aes extra data record - in init method of AESDecryptor");
        }
        throw new ZipException("invalid file header in init method of AESDecryptor");
    }

    @Override
    public int decryptData(byte[] byArray) throws ZipException {
        return this.decryptData(byArray, 0, byArray.length);
    }

    @Override
    public int decryptData(byte[] object, int n, int n2) throws ZipException {
        if (this.aesEngine != null) {
            for (int i = n; i < n + n2; i += 16) {
                int n3 = i + 16 <= n + n2 ? 16 : n + n2 - i;
                this.loopCount = n3;
                this.mac.update((byte[])object, i, n3);
                Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
                this.aesEngine.processBlock(this.iv, this.counterBlock);
                n3 = 0;
                while (true) {
                    if (n3 >= this.loopCount) break;
                    object[i + n3] = (byte)(object[i + n3] ^ this.counterBlock[n3]);
                    ++n3;
                    continue;
                    break;
                }
                try {
                    ++this.nonce;
                    continue;
                }
                catch (Exception exception) {
                    throw new ZipException(exception);
                }
                catch (ZipException zipException) {
                    throw zipException;
                }
            }
            return n2;
        }
        object = new ZipException("AES not initialized properly");
        throw object;
    }

    public byte[] getCalculatedAuthenticationBytes() {
        return this.mac.doFinal();
    }

    public int getPasswordVerifierLength() {
        return 2;
    }

    public int getSaltLength() {
        return this.SALT_LENGTH;
    }

    public byte[] getStoredMac() {
        return this.storedMac;
    }

    public void setStoredMac(byte[] byArray) {
        this.storedMac = byArray;
    }
}

