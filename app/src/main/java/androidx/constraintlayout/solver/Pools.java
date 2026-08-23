/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver;

final class Pools {
    private static final boolean DEBUG = false;

    private Pools() {
    }

    static interface Pool<T> {
        public T acquire();

        public boolean release(T var1);

        public void releaseAll(T[] var1, int var2);
    }

    static class SimplePool<T>
    implements Pool<T> {
        private final Object[] mPool;
        private int mPoolSize;

        SimplePool(int n) {
            if (n > 0) {
                this.mPool = new Object[n];
                return;
            }
            throw new IllegalArgumentException("The max pool size must be > 0");
        }

        private boolean isInPool(T t) {
            for (int i = 0; i < this.mPoolSize; ++i) {
                if (this.mPool[i] != t) continue;
                return true;
            }
            return false;
        }

        @Override
        public T acquire() {
            int n = this.mPoolSize;
            if (n > 0) {
                int n2 = n - 1;
                Object[] objectArray = this.mPool;
                Object object = objectArray[n2];
                objectArray[n2] = null;
                this.mPoolSize = n - 1;
                return (T)object;
            }
            return null;
        }

        @Override
        public boolean release(T t) {
            int n = this.mPoolSize;
            Object[] objectArray = this.mPool;
            if (n < objectArray.length) {
                objectArray[n] = t;
                this.mPoolSize = n + 1;
                return true;
            }
            return false;
        }

        @Override
        public void releaseAll(T[] TArray, int n) {
            int n2 = n;
            if (n > TArray.length) {
                n2 = TArray.length;
            }
            for (n = 0; n < n2; ++n) {
                T t = TArray[n];
                int n3 = this.mPoolSize;
                Object[] objectArray = this.mPool;
                if (n3 >= objectArray.length) continue;
                objectArray[n3] = t;
                this.mPoolSize = n3 + 1;
            }
        }
    }
}

