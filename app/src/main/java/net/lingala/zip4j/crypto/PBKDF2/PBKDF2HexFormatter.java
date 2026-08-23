/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.crypto.PBKDF2;

import net.lingala.zip4j.crypto.PBKDF2.BinTools;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;

class PBKDF2HexFormatter {
    PBKDF2HexFormatter() {
    }

    public boolean fromString(PBKDF2Parameters pBKDF2Parameters, String object) {
        if (pBKDF2Parameters != null && object != null) {
            Object[] objectArray = ((String)object).split(":");
            if (objectArray != null && objectArray.length == 3) {
                object = BinTools.hex2bin(objectArray[0]);
                int n = Integer.parseInt(objectArray[1]);
                objectArray = BinTools.hex2bin(objectArray[2]);
                pBKDF2Parameters.setSalt((byte[])object);
                pBKDF2Parameters.setIterationCount(n);
                pBKDF2Parameters.setDerivedKey((byte[])objectArray);
                return false;
            }
            return true;
        }
        return true;
    }

    public String toString(PBKDF2Parameters pBKDF2Parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BinTools.bin2hex(pBKDF2Parameters.getSalt()));
        stringBuilder.append(":");
        stringBuilder.append(String.valueOf(pBKDF2Parameters.getIterationCount()));
        stringBuilder.append(":");
        stringBuilder.append(BinTools.bin2hex(pBKDF2Parameters.getDerivedKey()));
        return stringBuilder.toString();
    }
}

