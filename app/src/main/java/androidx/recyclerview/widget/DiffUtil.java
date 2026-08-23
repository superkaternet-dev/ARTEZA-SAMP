/*
 * Decompiled with CFR 0.152.
 */
package androidx.recyclerview.widget;

import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.BatchingListUpdateCallback;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DiffUtil {
    private static final Comparator<Snake> SNAKE_COMPARATOR = new Comparator<Snake>(){

        @Override
        public int compare(Snake snake, Snake snake2) {
            int n;
            block0: {
                n = snake.x - snake2.x;
                if (n != 0) break block0;
                n = snake.y - snake2.y;
            }
            return n;
        }
    };

    private DiffUtil() {
    }

    public static DiffResult calculateDiff(Callback callback) {
        return DiffUtil.calculateDiff(callback, true);
    }

    public static DiffResult calculateDiff(Callback callback, boolean bl) {
        int n = callback.getOldListSize();
        int n2 = callback.getNewListSize();
        ArrayList<Snake> arrayList = new ArrayList<Snake>();
        ArrayList<Range> arrayList2 = new ArrayList<Range>();
        arrayList2.add(new Range(0, n, 0, n2));
        n = n + n2 + Math.abs(n - n2);
        int[] nArray = new int[n * 2];
        int[] nArray2 = new int[n * 2];
        ArrayList<Range> arrayList3 = new ArrayList<Range>();
        while (!arrayList2.isEmpty()) {
            Range range = (Range)arrayList2.remove(arrayList2.size() - 1);
            Snake snake = DiffUtil.diffPartial(callback, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, nArray, nArray2, n);
            if (snake != null) {
                if (snake.size > 0) {
                    arrayList.add(snake);
                }
                snake.x += range.oldListStart;
                snake.y += range.newListStart;
                Range range2 = arrayList3.isEmpty() ? new Range() : (Range)arrayList3.remove(arrayList3.size() - 1);
                range2.oldListStart = range.oldListStart;
                range2.newListStart = range.newListStart;
                if (snake.reverse) {
                    range2.oldListEnd = snake.x;
                    range2.newListEnd = snake.y;
                } else if (snake.removal) {
                    range2.oldListEnd = snake.x - 1;
                    range2.newListEnd = snake.y;
                } else {
                    range2.oldListEnd = snake.x;
                    range2.newListEnd = snake.y - 1;
                }
                arrayList2.add(range2);
                if (snake.reverse) {
                    if (snake.removal) {
                        range.oldListStart = snake.x + snake.size + 1;
                        range.newListStart = snake.y + snake.size;
                    } else {
                        range.oldListStart = snake.x + snake.size;
                        range.newListStart = snake.y + snake.size + 1;
                    }
                } else {
                    range.oldListStart = snake.x + snake.size;
                    range.newListStart = snake.y + snake.size;
                }
                arrayList2.add(range);
                continue;
            }
            arrayList3.add(range);
        }
        Collections.sort(arrayList, SNAKE_COMPARATOR);
        return new DiffResult(callback, arrayList, nArray, nArray2, bl);
    }

    private static Snake diffPartial(Callback object, int n, int n2, int n3, int n4, int[] nArray, int[] nArray2, int n5) {
        int n6 = n2 - n;
        int n7 = n4 - n3;
        if (n2 - n >= 1 && n4 - n3 >= 1) {
            int n8 = n6 - n7;
            int n9 = (n6 + n7 + 1) / 2;
            Arrays.fill(nArray, n5 - n9 - 1, n5 + n9 + 1, 0);
            Arrays.fill(nArray2, n5 - n9 - 1 + n8, n5 + n9 + 1 + n8, n6);
            boolean bl = n8 % 2 != 0;
            n4 = n6;
            for (int i = 0; i <= n9; ++i) {
                int n10;
                boolean bl2;
                for (n6 = -i; n6 <= i; n6 += 2) {
                    if (n6 != -i && (n6 == i || nArray[n5 + n6 - 1] >= nArray[n5 + n6 + 1])) {
                        n2 = nArray[n5 + n6 - 1] + 1;
                        bl2 = true;
                    } else {
                        n2 = nArray[n5 + n6 + 1];
                        bl2 = false;
                    }
                    for (n10 = n2 - n6; n2 < n4 && n10 < n7 && ((Callback)object).areItemsTheSame(n + n2, n3 + n10); ++n2, ++n10) {
                    }
                    nArray[n5 + n6] = n2;
                    if (!bl || n6 < n8 - i + 1 || n6 > n8 + i - 1 || nArray[n5 + n6] < nArray2[n5 + n6]) continue;
                    object = new Snake();
                    ((Snake)object).x = nArray2[n5 + n6];
                    ((Snake)object).y = ((Snake)object).x - n6;
                    ((Snake)object).size = nArray[n5 + n6] - nArray2[n5 + n6];
                    ((Snake)object).removal = bl2;
                    ((Snake)object).reverse = false;
                    return object;
                }
                n2 = n4;
                for (n6 = -i; n6 <= i; n6 += 2) {
                    int n11 = n6 + n8;
                    if (n11 != i + n8 && (n11 == -i + n8 || nArray2[n5 + n11 - 1] >= nArray2[n5 + n11 + 1])) {
                        n4 = nArray2[n5 + n11 + 1] - 1;
                        bl2 = true;
                    } else {
                        n4 = nArray2[n5 + n11 - 1];
                        bl2 = false;
                    }
                    for (n10 = n4 - n11; n4 > 0 && n10 > 0 && ((Callback)object).areItemsTheSame(n + n4 - 1, n3 + n10 - 1); --n4, --n10) {
                    }
                    nArray2[n5 + n11] = n4;
                    if (bl || n6 + n8 < -i || n6 + n8 > i || nArray[n5 + n11] < nArray2[n5 + n11]) continue;
                    object = new Snake();
                    ((Snake)object).x = nArray2[n5 + n11];
                    ((Snake)object).y = ((Snake)object).x - n11;
                    ((Snake)object).size = nArray[n5 + n11] - nArray2[n5 + n11];
                    ((Snake)object).removal = bl2;
                    ((Snake)object).reverse = true;
                    return object;
                }
                n4 = n2;
            }
            throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
        }
        return null;
    }

    public static abstract class Callback {
        public abstract boolean areContentsTheSame(int var1, int var2);

        public abstract boolean areItemsTheSame(int var1, int var2);

        public Object getChangePayload(int n, int n2) {
            return null;
        }

        public abstract int getNewListSize();

        public abstract int getOldListSize();
    }

    public static class DiffResult {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_IGNORE = 16;
        private static final int FLAG_MASK = 31;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 5;
        public static final int NO_POSITION = -1;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;
        private final List<Snake> mSnakes;

        DiffResult(Callback callback, List<Snake> list, int[] nArray, int[] nArray2, boolean bl) {
            this.mSnakes = list;
            this.mOldItemStatuses = nArray;
            this.mNewItemStatuses = nArray2;
            Arrays.fill(nArray, 0);
            Arrays.fill(nArray2, 0);
            this.mCallback = callback;
            this.mOldListSize = callback.getOldListSize();
            this.mNewListSize = callback.getNewListSize();
            this.mDetectMoves = bl;
            this.addRootSnake();
            this.findMatchingItems();
        }

        private void addRootSnake() {
            Snake snake = this.mSnakes.isEmpty() ? null : this.mSnakes.get(0);
            if (snake == null || snake.x != 0 || snake.y != 0) {
                snake = new Snake();
                snake.x = 0;
                snake.y = 0;
                snake.removal = false;
                snake.size = 0;
                snake.reverse = false;
                this.mSnakes.add(0, snake);
            }
        }

        private void dispatchAdditions(List<PostponedUpdate> object, ListUpdateCallback listUpdateCallback, int n, int n2, int n3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onInserted(n, n2);
                return;
            }
            --n2;
            while (n2 >= 0) {
                Object object2 = this.mNewItemStatuses;
                int n4 = object2[n3 + n2] & 0x1F;
                switch (n4) {
                    default: {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("unknown flag for pos ");
                        ((StringBuilder)object).append(n3 + n2);
                        ((StringBuilder)object).append(" ");
                        ((StringBuilder)object).append(Long.toBinaryString(n4));
                        throw new IllegalStateException(((StringBuilder)object).toString());
                    }
                    case 16: {
                        object.add((PostponedUpdate)new PostponedUpdate(n3 + n2, n, false));
                        break;
                    }
                    case 4: 
                    case 8: {
                        int n5 = object2[n3 + n2] >> 5;
                        listUpdateCallback.onMoved(DiffResult.removePostponedUpdate((List<PostponedUpdate>)object, (int)n5, (boolean)true).currentPos, n);
                        if (n4 != 4) break;
                        listUpdateCallback.onChanged(n, 1, this.mCallback.getChangePayload(n5, n3 + n2));
                        break;
                    }
                    case 0: {
                        listUpdateCallback.onInserted(n, 1);
                        Iterator iterator2 = object.iterator();
                        while (iterator2.hasNext()) {
                            object2 = (PostponedUpdate)iterator2.next();
                            ++object2.currentPos;
                        }
                        break block0;
                    }
                }
                --n2;
            }
        }

        private void dispatchRemovals(List<PostponedUpdate> object, ListUpdateCallback listUpdateCallback, int n, int n2, int n3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onRemoved(n, n2);
                return;
            }
            --n2;
            while (n2 >= 0) {
                Object object2 = this.mOldItemStatuses;
                int n4 = object2[n3 + n2] & 0x1F;
                switch (n4) {
                    default: {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("unknown flag for pos ");
                        ((StringBuilder)object).append(n3 + n2);
                        ((StringBuilder)object).append(" ");
                        ((StringBuilder)object).append(Long.toBinaryString(n4));
                        throw new IllegalStateException(((StringBuilder)object).toString());
                    }
                    case 16: {
                        object.add((PostponedUpdate)new PostponedUpdate(n3 + n2, n + n2, true));
                        break;
                    }
                    case 4: 
                    case 8: {
                        int n5 = object2[n3 + n2] >> 5;
                        object2 = DiffResult.removePostponedUpdate((List<PostponedUpdate>)object, n5, false);
                        listUpdateCallback.onMoved(n + n2, object2.currentPos - 1);
                        if (n4 != 4) break;
                        listUpdateCallback.onChanged(object2.currentPos - 1, 1, this.mCallback.getChangePayload(n3 + n2, n5));
                        break;
                    }
                    case 0: {
                        listUpdateCallback.onRemoved(n + n2, 1);
                        Iterator iterator2 = object.iterator();
                        while (iterator2.hasNext()) {
                            object2 = (PostponedUpdate)iterator2.next();
                            --object2.currentPos;
                        }
                        break block0;
                    }
                }
                --n2;
            }
        }

        private void findAddition(int n, int n2, int n3) {
            if (this.mOldItemStatuses[n - 1] != 0) {
                return;
            }
            this.findMatchingItem(n, n2, n3, false);
        }

        private boolean findMatchingItem(int n, int n2, int n3, boolean bl) {
            int n4;
            int n5;
            int n6;
            if (bl) {
                n6 = n2 - 1;
                n5 = n;
                n4 = n2 - 1;
                n2 = n5;
                n5 = n4;
            } else {
                n6 = n - 1;
                n4 = n - 1;
                n5 = n2;
                n2 = n4;
            }
            n4 = n2;
            while (n3 >= 0) {
                Snake snake = this.mSnakes.get(n3);
                int n7 = snake.x;
                int n8 = snake.size;
                int n9 = snake.y;
                int n10 = snake.size;
                n2 = 8;
                if (bl) {
                    for (n5 = n4 - 1; n5 >= n7 + n8; --n5) {
                        if (!this.mCallback.areItemsTheSame(n5, n6)) continue;
                        if (!this.mCallback.areContentsTheSame(n5, n6)) {
                            n2 = 4;
                        }
                        this.mNewItemStatuses[n6] = n5 << 5 | 0x10;
                        this.mOldItemStatuses[n5] = n6 << 5 | n2;
                        return true;
                    }
                } else {
                    --n5;
                    while (n5 >= n9 + n10) {
                        if (this.mCallback.areItemsTheSame(n6, n5)) {
                            if (!this.mCallback.areContentsTheSame(n6, n5)) {
                                n2 = 4;
                            }
                            this.mOldItemStatuses[n - 1] = n5 << 5 | 0x10;
                            this.mNewItemStatuses[n5] = n - 1 << 5 | n2;
                            return true;
                        }
                        --n5;
                    }
                }
                n4 = snake.x;
                n5 = snake.y;
                --n3;
            }
            return false;
        }

        private void findMatchingItems() {
            int n = this.mOldListSize;
            int n2 = this.mNewListSize;
            for (int i = this.mSnakes.size() - 1; i >= 0; --i) {
                int n3;
                Snake snake = this.mSnakes.get(i);
                int n4 = snake.x;
                int n5 = snake.size;
                int n6 = snake.y;
                int n7 = snake.size;
                if (this.mDetectMoves) {
                    while (true) {
                        if (n <= n4 + n5) break;
                        this.findAddition(n, n2, i);
                        --n;
                    }
                    for (n3 = n2; n3 > n6 + n7; --n3) {
                        this.findRemoval(n, n3, i);
                    }
                }
                for (n2 = 0; n2 < snake.size; ++n2) {
                    n3 = snake.x + n2;
                    n6 = snake.y + n2;
                    n = this.mCallback.areContentsTheSame(n3, n6) ? 1 : 2;
                    this.mOldItemStatuses[n3] = n6 << 5 | n;
                    this.mNewItemStatuses[n6] = n3 << 5 | n;
                }
                n = snake.x;
                n2 = snake.y;
            }
        }

        private void findRemoval(int n, int n2, int n3) {
            if (this.mNewItemStatuses[n2 - 1] != 0) {
                return;
            }
            this.findMatchingItem(n, n2, n3, true);
        }

        private static PostponedUpdate removePostponedUpdate(List<PostponedUpdate> list, int n, boolean bl) {
            for (int i = list.size() - 1; i >= 0; --i) {
                PostponedUpdate postponedUpdate = list.get(i);
                if (postponedUpdate.posInOwnerList != n || postponedUpdate.removal != bl) continue;
                list.remove(i);
                for (n = i; n < list.size(); ++n) {
                    PostponedUpdate postponedUpdate2 = list.get(n);
                    int n2 = postponedUpdate2.currentPos;
                    i = bl ? 1 : -1;
                    postponedUpdate2.currentPos = n2 + i;
                }
                return postponedUpdate;
            }
            return null;
        }

        public int convertNewPositionToOld(int object) {
            Object object2;
            if (object >= 0 && object < ((int[])(object2 = this.mNewItemStatuses)).length) {
                if (((object = (Object)object2[object]) & 0x1F) == 0) {
                    return -1;
                }
                return object >> 5;
            }
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Index out of bounds - passed position = ");
            ((StringBuilder)object2).append((int)object);
            ((StringBuilder)object2).append(", new list size = ");
            ((StringBuilder)object2).append(this.mNewItemStatuses.length);
            throw new IndexOutOfBoundsException(((StringBuilder)object2).toString());
        }

        public int convertOldPositionToNew(int object) {
            Object object2;
            if (object >= 0 && object < ((int[])(object2 = this.mOldItemStatuses)).length) {
                if (((object = (Object)object2[object]) & 0x1F) == 0) {
                    return -1;
                }
                return object >> 5;
            }
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Index out of bounds - passed position = ");
            ((StringBuilder)object2).append((int)object);
            ((StringBuilder)object2).append(", old list size = ");
            ((StringBuilder)object2).append(this.mOldItemStatuses.length);
            throw new IndexOutOfBoundsException(((StringBuilder)object2).toString());
        }

        public void dispatchUpdatesTo(ListUpdateCallback listUpdateCallback) {
            listUpdateCallback = listUpdateCallback instanceof BatchingListUpdateCallback ? (BatchingListUpdateCallback)listUpdateCallback : new BatchingListUpdateCallback(listUpdateCallback);
            ArrayList<PostponedUpdate> arrayList = new ArrayList<PostponedUpdate>();
            int n = this.mOldListSize;
            int n2 = this.mNewListSize;
            int n3 = this.mSnakes.size();
            --n3;
            while (n3 >= 0) {
                Snake snake = this.mSnakes.get(n3);
                int n4 = snake.size;
                int n5 = snake.x + n4;
                int n6 = snake.y + n4;
                if (n5 < n) {
                    this.dispatchRemovals(arrayList, listUpdateCallback, n5, n - n5, n5);
                }
                if (n6 < n2) {
                    this.dispatchAdditions(arrayList, listUpdateCallback, n5, n2 - n6, n6);
                }
                for (n2 = n4 - 1; n2 >= 0; --n2) {
                    if ((this.mOldItemStatuses[snake.x + n2] & 0x1F) != 2) continue;
                    ((BatchingListUpdateCallback)listUpdateCallback).onChanged(snake.x + n2, 1, this.mCallback.getChangePayload(snake.x + n2, snake.y + n2));
                }
                n = snake.x;
                n2 = snake.y;
                --n3;
            }
            ((BatchingListUpdateCallback)listUpdateCallback).dispatchLastEvent();
        }

        public void dispatchUpdatesTo(RecyclerView.Adapter adapter) {
            this.dispatchUpdatesTo(new AdapterListUpdateCallback(adapter));
        }

        List<Snake> getSnakes() {
            return this.mSnakes;
        }
    }

    public static abstract class ItemCallback<T> {
        public abstract boolean areContentsTheSame(T var1, T var2);

        public abstract boolean areItemsTheSame(T var1, T var2);

        public Object getChangePayload(T t, T t2) {
            return null;
        }
    }

    private static class PostponedUpdate {
        int currentPos;
        int posInOwnerList;
        boolean removal;

        public PostponedUpdate(int n, int n2, boolean bl) {
            this.posInOwnerList = n;
            this.currentPos = n2;
            this.removal = bl;
        }
    }

    static class Range {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;

        public Range() {
        }

        public Range(int n, int n2, int n3, int n4) {
            this.oldListStart = n;
            this.oldListEnd = n2;
            this.newListStart = n3;
            this.newListEnd = n4;
        }
    }

    static class Snake {
        boolean removal;
        boolean reverse;
        int size;
        int x;
        int y;

        Snake() {
        }
    }
}

