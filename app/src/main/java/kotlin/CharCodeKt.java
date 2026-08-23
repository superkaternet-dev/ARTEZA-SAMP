/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.UShort;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u000e\n\u0000\n\u0002\u0010\b\n\u0002\u0010\f\n\u0002\b\u0006\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0000\u001a\u00020\u0001H\u0087\b\"\u001f\u0010\u0000\u001a\u00020\u0001*\u00020\u00028\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\b"}, d2={"code", "", "", "getCode$annotations", "(C)V", "getCode", "(C)I", "Char", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class CharCodeKt {
    private static final char Char(int n) {
        if (n >= CharCodeKt.getCode('\u0000') && n <= CharCodeKt.getCode('\uffff')) {
            return (char)(0xFFFF & UShort.constructor-impl((short)n));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid Char code: ");
        stringBuilder.append(n);
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    private static final int getCode(char c) {
        return c;
    }

    public static /* synthetic */ void getCode$annotations(char c) {
    }
}

