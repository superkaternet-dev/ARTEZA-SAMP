/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto.PBKDF2;

import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.PBKDF2.PRF;
import net.lingala.zip4j.util.Raw;

public class PBKDF2Engine {
    protected PBKDF2Parameters parameters;
    protected PRF prf;

    public PBKDF2Engine() {
        this.parameters = null;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters pBKDF2Parameters) {
        this.parameters = pBKDF2Parameters;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters pBKDF2Parameters, PRF pRF) {
        this.parameters = pBKDF2Parameters;
        this.prf = pRF;
    }

    protected void INT(byte[] byArray, int n, int n2) {
        byArray[n + 0] = (byte)(n2 / 0x1000000);
        byArray[n + 1] = (byte)(n2 / 65536);
        byArray[n + 2] = (byte)(n2 / 256);
        byArray[n + 3] = (byte)n2;
    }

    protected byte[] PBKDF2(PRF object, byte[] byArray, int n, int n2) {
        if (byArray == null) {
            byArray = new byte[]{};
        }
        int n3 = object.getHLen();
        int n4 = this.ceil(n2, n3);
        byte[] byArray2 = new byte[n4 * n3];
        int n5 = 0;
        for (int i = 1; i <= n4; ++i) {
            this._F(byArray2, n5, (PRF)object, byArray, n, i);
            n5 += n3;
        }
        if (n2 - (n4 - 1) * n3 < n3) {
            object = new byte[n2];
            System.arraycopy(byArray2, 0, object, 0, n2);
            return object;
        }
        return byArray2;
    }

    protected void _F(byte[] byArray, int n, PRF pRF, byte[] byArray2, int n2, int n3) {
        int n4 = pRF.getHLen();
        byte[] byArray3 = new byte[n4];
        byte[] byArray4 = new byte[byArray2.length + 4];
        System.arraycopy(byArray2, 0, byArray4, 0, byArray2.length);
        this.INT(byArray4, byArray2.length, n3);
        byArray2 = byArray4;
        for (n3 = 0; n3 < n2; ++n3) {
            byArray2 = pRF.doFinal(byArray2);
            this.xor(byArray3, byArray2);
        }
        System.arraycopy(byArray3, 0, byArray, n, n4);
    }

    protected void assertPRF(byte[] byArray) {
        if (this.prf == null) {
            this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm());
        }
        this.prf.init(byArray);
    }

    protected int ceil(int n, int n2) {
        int n3 = 0;
        if (n % n2 > 0) {
            n3 = 1;
        }
        return n / n2 + n3;
    }

    public byte[] deriveKey(char[] cArray) {
        return this.deriveKey(cArray, 0);
    }

    public byte[] deriveKey(char[] cArray, int n) {
        if (cArray != null) {
            this.assertPRF(Raw.convertCharArrayToByteArray(cArray));
            int n2 = n;
            if (n == 0) {
                n2 = this.prf.getHLen();
            }
            return this.PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), n2);
        }
        throw new NullPointerException();
    }

    public PBKDF2Parameters getParameters() {
        return this.parameters;
    }

    public PRF getPseudoRandomFunction() {
        return this.prf;
    }

    public void setParameters(PBKDF2Parameters pBKDF2Parameters) {
        this.parameters = pBKDF2Parameters;
    }

    public void setPseudoRandomFunction(PRF pRF) {
        this.prf = pRF;
    }

    public boolean verifyKey(char[] objectArray) {
        byte[] byArray = this.getParameters().getDerivedKey();
        if (byArray != null && byArray.length != 0) {
            if ((objectArray = (Object[])this.deriveKey((char[])objectArray, byArray.length)) != null && objectArray.length == byArray.length) {
                for (int i = 0; i < objectArray.length; ++i) {
                    if (objectArray[i] == byArray[i]) continue;
                    return false;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    protected void xor(byte[] byArray, byte[] byArray2) {
        for (int i = 0; i < byArray.length; ++i) {
            byArray[i] = (byte)(byArray[i] ^ byArray2[i]);
        }
    }
}

