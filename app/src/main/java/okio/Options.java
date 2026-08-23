/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.AbstractList;
import java.util.RandomAccess;
import okio.ByteString;

public final class Options
extends AbstractList<ByteString>
implements RandomAccess {
    final ByteString[] byteStrings;

    private Options(ByteString[] byteStringArray) {
        this.byteStrings = byteStringArray;
    }

    public static Options of(ByteString ... byteStringArray) {
        return new Options((ByteString[])byteStringArray.clone());
    }

    @Override
    public ByteString get(int n) {
        return this.byteStrings[n];
    }

    @Override
    public int size() {
        return this.byteStrings.length;
    }
}

