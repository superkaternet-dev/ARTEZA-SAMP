/*
 * Decompiled with CFR 0.152.
 */
package androidx.collection;

public final class CircularArray<E> {
    private int mCapacityBitmask;
    private E[] mElements;
    private int mHead;
    private int mTail;

    public CircularArray() {
        this(8);
    }

    public CircularArray(int n) {
        if (n >= 1) {
            if (n <= 0x40000000) {
                if (Integer.bitCount(n) != 1) {
                    n = Integer.highestOneBit(n - 1) << 1;
                }
                this.mCapacityBitmask = n - 1;
                this.mElements = new Object[n];
                return;
            }
            throw new IllegalArgumentException("capacity must be <= 2^30");
        }
        throw new IllegalArgumentException("capacity must be >= 1");
    }

    private void doubleCapacity() {
        E[] EArray = this.mElements;
        int n = EArray.length;
        int n2 = this.mHead;
        int n3 = n - n2;
        int n4 = n << 1;
        if (n4 >= 0) {
            Object[] objectArray = new Object[n4];
            System.arraycopy(EArray, n2, objectArray, 0, n3);
            System.arraycopy(this.mElements, 0, objectArray, n3, this.mHead);
            this.mElements = objectArray;
            this.mHead = 0;
            this.mTail = n;
            this.mCapacityBitmask = n4 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }

    public void addFirst(E e) {
        int n;
        this.mHead = n = this.mHead - 1 & this.mCapacityBitmask;
        this.mElements[n] = e;
        if (n == this.mTail) {
            this.doubleCapacity();
        }
    }

    public void addLast(E e) {
        E[] EArray = this.mElements;
        int n = this.mTail;
        EArray[n] = e;
        this.mTail = n = this.mCapacityBitmask & n + 1;
        if (n == this.mHead) {
            this.doubleCapacity();
        }
    }

    public void clear() {
        this.removeFromStart(this.size());
    }

    public E get(int n) {
        if (n >= 0 && n < this.size()) {
            return this.mElements[this.mHead + n & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getFirst() {
        int n = this.mHead;
        if (n != this.mTail) {
            return this.mElements[n];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getLast() {
        int n = this.mHead;
        int n2 = this.mTail;
        if (n != n2) {
            return this.mElements[n2 - 1 & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean isEmpty() {
        boolean bl = this.mHead == this.mTail;
        return bl;
    }

    public E popFirst() {
        int n = this.mHead;
        if (n != this.mTail) {
            E[] EArray = this.mElements;
            E e = EArray[n];
            EArray[n] = null;
            this.mHead = n + 1 & this.mCapacityBitmask;
            return e;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E popLast() {
        int n = this.mHead;
        int n2 = this.mTail;
        if (n != n2) {
            n2 = this.mCapacityBitmask & n2 - 1;
            E[] EArray = this.mElements;
            E e = EArray[n2];
            EArray[n2] = null;
            this.mTail = n2;
            return e;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromEnd(int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.size()) {
            int n2;
            int n3 = 0;
            int n4 = this.mTail;
            if (n < n4) {
                n3 = n4 - n;
            }
            for (n4 = n3; n4 < (n2 = this.mTail); ++n4) {
                this.mElements[n4] = null;
            }
            n3 = n2 - n3;
            this.mTail = n2 - n3;
            if ((n -= n3) > 0) {
                this.mTail = n3 = this.mElements.length;
                n3 -= n;
                for (n = n3; n < this.mTail; ++n) {
                    this.mElements[n] = null;
                }
                this.mTail = n3;
            }
            return;
        }
        ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = new ArrayIndexOutOfBoundsException();
        throw arrayIndexOutOfBoundsException;
    }

    public void removeFromStart(int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.size()) {
            int n2 = this.mElements.length;
            int n3 = this.mHead;
            int n4 = n2;
            if (n < n2 - n3) {
                n4 = n3 + n;
            }
            for (n2 = this.mHead; n2 < n4; ++n2) {
                this.mElements[n2] = null;
            }
            n2 = this.mHead;
            n3 = n4 - n2;
            n4 = n - n3;
            this.mHead = n2 + n3 & this.mCapacityBitmask;
            if (n4 > 0) {
                for (n = 0; n < n4; ++n) {
                    this.mElements[n] = null;
                }
                this.mHead = n4;
            }
            return;
        }
        ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = new ArrayIndexOutOfBoundsException();
        throw arrayIndexOutOfBoundsException;
    }

    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}

