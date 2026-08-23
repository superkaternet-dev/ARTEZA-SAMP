/*
 * Decompiled with CFR 0.152.
 */
package kotlin.random;

import kotlin.Metadata;
import kotlin.random.Random;
import kotlin.random.RandomKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0010\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005B7\b\u0000\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0016J\b\u0010\u000f\u001a\u00020\u0003H\u0016R\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lkotlin/random/XorWowRandom;", "Lkotlin/random/Random;", "seed1", "", "seed2", "(II)V", "x", "y", "z", "w", "v", "addend", "(IIIIII)V", "nextBits", "bitCount", "nextInt", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class XorWowRandom
extends Random {
    private int addend;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z;

    public XorWowRandom(int n, int n2) {
        this(n, n2, 0, 0, ~n, n << 10 ^ n2 >>> 4);
    }

    public XorWowRandom(int n, int n2, int n3, int n4, int n5, int n6) {
        this.x = n;
        this.y = n2;
        this.z = n3;
        this.w = n4;
        this.v = n5;
        this.addend = n6;
        n6 = 0;
        n = (n | n2 | n3 | n4 | n5) != 0 ? 1 : 0;
        if (n != 0) {
            for (n = n6; n < 64; ++n) {
                this.nextInt();
            }
            return;
        }
        Throwable throwable = new IllegalArgumentException("Initial state must have at least one non-zero element.".toString());
        throw throwable;
    }

    @Override
    public int nextBits(int n) {
        return RandomKt.takeUpperBits(this.nextInt(), n);
    }

    @Override
    public int nextInt() {
        int n;
        int n2 = this.x;
        n2 ^= n2 >>> 2;
        this.x = this.y;
        this.y = this.z;
        this.z = this.w;
        this.w = n = this.v;
        this.v = n = n2 << 1 ^ n2 ^ n ^ n << 4;
        this.addend = n2 = this.addend + 362437;
        return n2 + n;
    }
}

