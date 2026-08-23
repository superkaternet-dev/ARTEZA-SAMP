/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 */
package androidx.customview.widget;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {
    private FocusStrategy() {
    }

    private static boolean beamBeats(int n, Rect rect, Rect rect2, Rect rect3) {
        boolean bl = FocusStrategy.beamsOverlap(n, rect, rect2);
        boolean bl2 = FocusStrategy.beamsOverlap(n, rect, rect3);
        boolean bl3 = false;
        if (!bl2 && bl) {
            if (!FocusStrategy.isToDirectionOf(n, rect, rect3)) {
                return true;
            }
            if (n != 17 && n != 66) {
                if (FocusStrategy.majorAxisDistance(n, rect, rect2) < FocusStrategy.majorAxisDistanceToFarEdge(n, rect, rect3)) {
                    bl3 = true;
                }
                return bl3;
            }
            return true;
        }
        return false;
    }

    private static boolean beamsOverlap(int n, Rect rect, Rect rect2) {
        boolean bl = true;
        boolean bl2 = true;
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 33: 
            case 130: {
                bl = rect2.right >= rect.left && rect2.left <= rect.right ? bl2 : false;
                return bl;
            }
            case 17: 
            case 66: 
        }
        if (rect2.bottom < rect.top || rect2.top > rect.bottom) {
            bl = false;
        }
        return bl;
    }

    public static <L, T> T findNextFocusInAbsoluteDirection(L l, CollectionAdapter<L, T> collectionAdapter, BoundsAdapter<T> boundsAdapter, T t, Rect rect, int n) {
        Rect rect2 = new Rect(rect);
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 130: {
                rect2.offset(0, -(rect.height() + 1));
                break;
            }
            case 66: {
                rect2.offset(-(rect.width() + 1), 0);
                break;
            }
            case 33: {
                rect2.offset(0, rect.height() + 1);
                break;
            }
            case 17: {
                rect2.offset(rect.width() + 1, 0);
            }
        }
        T t2 = null;
        int n2 = collectionAdapter.size(l);
        Rect rect3 = new Rect();
        for (int i = 0; i < n2; ++i) {
            T t3 = collectionAdapter.get(l, i);
            if (t3 == t) continue;
            boundsAdapter.obtainBounds(t3, rect3);
            if (!FocusStrategy.isBetterCandidate(n, rect, rect3, rect2)) continue;
            rect2.set(rect3);
            t2 = t3;
        }
        return t2;
    }

    public static <L, T> T findNextFocusInRelativeDirection(L l, CollectionAdapter<L, T> collectionAdapter, BoundsAdapter<T> boundsAdapter, T t, int n, boolean bl, boolean bl2) {
        int n2 = collectionAdapter.size(l);
        ArrayList<T> arrayList = new ArrayList<T>(n2);
        for (int i = 0; i < n2; ++i) {
            arrayList.add(collectionAdapter.get(l, i));
        }
        Collections.sort(arrayList, new SequentialComparator<T>(bl, boundsAdapter));
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
            }
            case 2: {
                return FocusStrategy.getNextFocusable(t, arrayList, bl2);
            }
            case 1: 
        }
        return FocusStrategy.getPreviousFocusable(t, arrayList, bl2);
    }

    private static <T> T getNextFocusable(T t, ArrayList<T> arrayList, boolean bl) {
        int n = arrayList.size();
        int n2 = t == null ? -1 : arrayList.lastIndexOf(t);
        if (++n2 < n) {
            return arrayList.get(n2);
        }
        if (bl && n > 0) {
            return arrayList.get(0);
        }
        return null;
    }

    private static <T> T getPreviousFocusable(T t, ArrayList<T> arrayList, boolean bl) {
        int n = arrayList.size();
        int n2 = t == null ? n : arrayList.indexOf(t);
        if (--n2 >= 0) {
            return arrayList.get(n2);
        }
        if (bl && n > 0) {
            return arrayList.get(n - 1);
        }
        return null;
    }

    private static int getWeightedDistanceFor(int n, int n2) {
        return n * 13 * n + n2 * n2;
    }

    private static boolean isBetterCandidate(int n, Rect rect, Rect rect2, Rect rect3) {
        boolean bl = FocusStrategy.isCandidate(rect, rect2, n);
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        if (!FocusStrategy.isCandidate(rect, rect3, n)) {
            return true;
        }
        if (FocusStrategy.beamBeats(n, rect, rect2, rect3)) {
            return true;
        }
        if (FocusStrategy.beamBeats(n, rect, rect3, rect2)) {
            return false;
        }
        int n2 = FocusStrategy.getWeightedDistanceFor(FocusStrategy.majorAxisDistance(n, rect, rect2), FocusStrategy.minorAxisDistance(n, rect, rect2));
        if (n2 < FocusStrategy.getWeightedDistanceFor(FocusStrategy.majorAxisDistance(n, rect, rect3), FocusStrategy.minorAxisDistance(n, rect, rect3))) {
            bl2 = true;
        }
        return bl2;
    }

    private static boolean isCandidate(Rect rect, Rect rect2, int n) {
        boolean bl = true;
        boolean bl2 = true;
        boolean bl3 = true;
        boolean bl4 = true;
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 130: {
                bl2 = (rect.top < rect2.top || rect.bottom <= rect2.top) && rect.bottom < rect2.bottom ? bl4 : false;
                return bl2;
            }
            case 66: {
                bl2 = (rect.left < rect2.left || rect.right <= rect2.left) && rect.right < rect2.right ? bl : false;
                return bl2;
            }
            case 33: {
                if (rect.bottom <= rect2.bottom && rect.top < rect2.bottom || rect.top <= rect2.top) {
                    bl2 = false;
                }
                return bl2;
            }
            case 17: 
        }
        bl2 = (rect.right > rect2.right || rect.left >= rect2.right) && rect.left > rect2.left ? bl3 : false;
        return bl2;
    }

    private static boolean isToDirectionOf(int n, Rect rect, Rect rect2) {
        boolean bl = true;
        boolean bl2 = true;
        boolean bl3 = true;
        boolean bl4 = true;
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 130: {
                bl = rect.bottom <= rect2.top ? bl4 : false;
                return bl;
            }
            case 66: {
                if (rect.right > rect2.left) {
                    bl = false;
                }
                return bl;
            }
            case 33: {
                bl = rect.top >= rect2.bottom ? bl2 : false;
                return bl;
            }
            case 17: 
        }
        bl = rect.left >= rect2.right ? bl3 : false;
        return bl;
    }

    private static int majorAxisDistance(int n, Rect rect, Rect rect2) {
        return Math.max(0, FocusStrategy.majorAxisDistanceRaw(n, rect, rect2));
    }

    private static int majorAxisDistanceRaw(int n, Rect rect, Rect rect2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 130: {
                return rect2.top - rect.bottom;
            }
            case 66: {
                return rect2.left - rect.right;
            }
            case 33: {
                return rect.top - rect2.bottom;
            }
            case 17: 
        }
        return rect.left - rect2.right;
    }

    private static int majorAxisDistanceToFarEdge(int n, Rect rect, Rect rect2) {
        return Math.max(1, FocusStrategy.majorAxisDistanceToFarEdgeRaw(n, rect, rect2));
    }

    private static int majorAxisDistanceToFarEdgeRaw(int n, Rect rect, Rect rect2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 130: {
                return rect2.bottom - rect.bottom;
            }
            case 66: {
                return rect2.right - rect.right;
            }
            case 33: {
                return rect.top - rect2.top;
            }
            case 17: 
        }
        return rect.left - rect2.left;
    }

    private static int minorAxisDistance(int n, Rect rect, Rect rect2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 33: 
            case 130: {
                return Math.abs(rect.left + rect.width() / 2 - (rect2.left + rect2.width() / 2));
            }
            case 17: 
            case 66: 
        }
        return Math.abs(rect.top + rect.height() / 2 - (rect2.top + rect2.height() / 2));
    }

    public static interface BoundsAdapter<T> {
        public void obtainBounds(T var1, Rect var2);
    }

    public static interface CollectionAdapter<T, V> {
        public V get(T var1, int var2);

        public int size(T var1);
    }

    private static class SequentialComparator<T>
    implements Comparator<T> {
        private final BoundsAdapter<T> mAdapter;
        private final boolean mIsLayoutRtl;
        private final Rect mTemp1 = new Rect();
        private final Rect mTemp2 = new Rect();

        SequentialComparator(boolean bl, BoundsAdapter<T> boundsAdapter) {
            this.mIsLayoutRtl = bl;
            this.mAdapter = boundsAdapter;
        }

        @Override
        public int compare(T t, T t2) {
            Rect rect = this.mTemp1;
            Rect rect2 = this.mTemp2;
            this.mAdapter.obtainBounds(t, rect);
            this.mAdapter.obtainBounds(t2, rect2);
            int n = rect.top;
            int n2 = rect2.top;
            int n3 = -1;
            if (n < n2) {
                return -1;
            }
            if (rect.top > rect2.top) {
                return 1;
            }
            if (rect.left < rect2.left) {
                if (this.mIsLayoutRtl) {
                    n3 = 1;
                }
                return n3;
            }
            if (rect.left > rect2.left) {
                if (!this.mIsLayoutRtl) {
                    n3 = 1;
                }
                return n3;
            }
            if (rect.bottom < rect2.bottom) {
                return -1;
            }
            if (rect.bottom > rect2.bottom) {
                return 1;
            }
            if (rect.right < rect2.right) {
                if (this.mIsLayoutRtl) {
                    n3 = 1;
                }
                return n3;
            }
            if (rect.right > rect2.right) {
                if (!this.mIsLayoutRtl) {
                    n3 = 1;
                }
                return n3;
            }
            return 0;
        }
    }
}

