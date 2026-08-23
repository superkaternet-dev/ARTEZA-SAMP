/*
 * Decompiled with CFR 0.152.
 */
package androidx.recyclerview.widget;

import androidx.core.os.TraceCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

final class GapWorker
implements Runnable {
    static final ThreadLocal<GapWorker> sGapWorker = new ThreadLocal();
    static Comparator<Task> sTaskComparator = new Comparator<Task>(){

        @Override
        public int compare(Task task, Task task2) {
            int n;
            RecyclerView recyclerView = task.view;
            int n2 = 1;
            int n3 = 1;
            int n4 = recyclerView == null ? 1 : 0;
            if (n4 != (n = task2.view == null)) {
                n4 = task.view == null ? n3 : -1;
                return n4;
            }
            if (task.immediate != task2.immediate) {
                n4 = n2;
                if (task.immediate) {
                    n4 = -1;
                }
                return n4;
            }
            n4 = task2.viewVelocity - task.viewVelocity;
            if (n4 != 0) {
                return n4;
            }
            n4 = task.distanceToItem - task2.distanceToItem;
            if (n4 != 0) {
                return n4;
            }
            return 0;
        }
    };
    long mFrameIntervalNs;
    long mPostTimeNs;
    ArrayList<RecyclerView> mRecyclerViews = new ArrayList();
    private ArrayList<Task> mTasks = new ArrayList();

    GapWorker() {
    }

    private void buildTaskList() {
        int n;
        Object object;
        int n2;
        int n3 = this.mRecyclerViews.size();
        int n4 = 0;
        for (n2 = 0; n2 < n3; ++n2) {
            object = this.mRecyclerViews.get(n2);
            n = n4;
            if (object.getWindowVisibility() == 0) {
                ((RecyclerView)object).mPrefetchRegistry.collectPrefetchPositionsFromView((RecyclerView)object, false);
                n = n4 + ((RecyclerView)object).mPrefetchRegistry.mCount;
            }
            n4 = n;
        }
        this.mTasks.ensureCapacity(n4);
        n2 = 0;
        for (n = 0; n < n3; ++n) {
            int n5;
            RecyclerView recyclerView = this.mRecyclerViews.get(n);
            if (recyclerView.getWindowVisibility() != 0) {
                n5 = n2;
            } else {
                LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = recyclerView.mPrefetchRegistry;
                int n6 = Math.abs(layoutPrefetchRegistryImpl.mPrefetchDx) + Math.abs(layoutPrefetchRegistryImpl.mPrefetchDy);
                n4 = 0;
                while (true) {
                    n5 = n2;
                    if (n4 >= layoutPrefetchRegistryImpl.mCount * 2) break;
                    if (n2 >= this.mTasks.size()) {
                        object = new Task();
                        this.mTasks.add((Task)object);
                    } else {
                        object = this.mTasks.get(n2);
                    }
                    n5 = layoutPrefetchRegistryImpl.mPrefetchArray[n4 + 1];
                    boolean bl = n5 <= n6;
                    ((Task)object).immediate = bl;
                    ((Task)object).viewVelocity = n6;
                    ((Task)object).distanceToItem = n5;
                    ((Task)object).view = recyclerView;
                    ((Task)object).position = layoutPrefetchRegistryImpl.mPrefetchArray[n4];
                    ++n2;
                    n4 += 2;
                }
            }
            n2 = n5;
        }
        Collections.sort(this.mTasks, sTaskComparator);
    }

    private void flushTaskWithDeadline(Task object, long l) {
        long l2 = ((Task)object).immediate ? Long.MAX_VALUE : l;
        object = this.prefetchPositionWithDeadline(((Task)object).view, ((Task)object).position, l2);
        if (object != null && ((RecyclerView.ViewHolder)object).mNestedRecyclerView != null && ((RecyclerView.ViewHolder)object).isBound() && !((RecyclerView.ViewHolder)object).isInvalid()) {
            this.prefetchInnerRecyclerViewWithDeadline((RecyclerView)((RecyclerView.ViewHolder)object).mNestedRecyclerView.get(), l);
        }
    }

    private void flushTasksWithDeadline(long l) {
        for (int i = 0; i < this.mTasks.size(); ++i) {
            Task task = this.mTasks.get(i);
            if (task.view == null) break;
            this.flushTaskWithDeadline(task, l);
            task.clear();
        }
    }

    static boolean isPrefetchPositionAttached(RecyclerView recyclerView, int n) {
        int n2 = recyclerView.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n2; ++i) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(recyclerView.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder.mPosition != n || viewHolder.isInvalid()) continue;
            return true;
        }
        return false;
    }

    private void prefetchInnerRecyclerViewWithDeadline(RecyclerView recyclerView, long l) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.mDataSetHasChangedAfterLayout && recyclerView.mChildHelper.getUnfilteredChildCount() != 0) {
            recyclerView.removeAndRecycleViews();
        }
        LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = recyclerView.mPrefetchRegistry;
        layoutPrefetchRegistryImpl.collectPrefetchPositionsFromView(recyclerView, true);
        if (layoutPrefetchRegistryImpl.mCount != 0) {
            int n;
            try {
                TraceCompat.beginSection("RV Nested Prefetch");
                recyclerView.mState.prepareForNestedPrefetch(recyclerView.mAdapter);
                n = 0;
            }
            catch (Throwable throwable) {
                TraceCompat.endSection();
                throw throwable;
            }
            while (true) {
                if (n >= layoutPrefetchRegistryImpl.mCount * 2) break;
                this.prefetchPositionWithDeadline(recyclerView, layoutPrefetchRegistryImpl.mPrefetchArray[n], l);
                n += 2;
                continue;
                break;
            }
            TraceCompat.endSection();
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private RecyclerView.ViewHolder prefetchPositionWithDeadline(RecyclerView recyclerView, int n, long l) {
        void var5_8;
        block8: {
            RecyclerView.ViewHolder viewHolder;
            block7: {
                if (GapWorker.isPrefetchPositionAttached(recyclerView, n)) {
                    return null;
                }
                RecyclerView.Recycler recycler = recyclerView.mRecycler;
                try {
                    recyclerView.onEnterLayoutOrScroll();
                    viewHolder = recycler.tryGetViewHolderForPositionByDeadline(n, false, l);
                    if (viewHolder == null) break block7;
                }
                catch (Throwable throwable) {
                    // empty catch block
                    break block8;
                }
                try {
                    if (viewHolder.isBound() && !viewHolder.isInvalid()) {
                        recycler.recycleView(viewHolder.itemView);
                    } else {
                        recycler.addViewHolderToRecycledViewPool(viewHolder, false);
                    }
                }
                catch (Throwable throwable) {
                    break block8;
                }
            }
            recyclerView.onExitLayoutOrScroll(false);
            return viewHolder;
        }
        recyclerView.onExitLayoutOrScroll(false);
        throw var5_8;
    }

    public void add(RecyclerView recyclerView) {
        this.mRecyclerViews.add(recyclerView);
    }

    void postFromTraversal(RecyclerView recyclerView, int n, int n2) {
        if (recyclerView.isAttachedToWindow() && this.mPostTimeNs == 0L) {
            this.mPostTimeNs = recyclerView.getNanoTime();
            recyclerView.post(this);
        }
        recyclerView.mPrefetchRegistry.setPrefetchVector(n, n2);
    }

    void prefetch(long l) {
        this.buildTaskList();
        this.flushTasksWithDeadline(l);
    }

    public void remove(RecyclerView recyclerView) {
        this.mRecyclerViews.remove(recyclerView);
    }

    @Override
    public void run() {
        block9: {
            TraceCompat.beginSection("RV Prefetch");
            boolean bl = this.mRecyclerViews.isEmpty();
            if (!bl) break block9;
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
            return;
        }
        int n = this.mRecyclerViews.size();
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            RecyclerView recyclerView = this.mRecyclerViews.get(i);
            long l2 = l;
            if (recyclerView.getWindowVisibility() == 0) {
                l2 = Math.max(recyclerView.getDrawingTime(), l);
            }
            l = l2;
            continue;
        }
        if (l == 0L) {
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
            return;
        }
        try {
            this.prefetch(TimeUnit.MILLISECONDS.toNanos(l) + this.mFrameIntervalNs);
            this.mPostTimeNs = 0L;
        }
        catch (Throwable throwable) {
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
            throw throwable;
        }
        TraceCompat.endSection();
    }

    static class LayoutPrefetchRegistryImpl
    implements RecyclerView.LayoutManager.LayoutPrefetchRegistry {
        int mCount;
        int[] mPrefetchArray;
        int mPrefetchDx;
        int mPrefetchDy;

        LayoutPrefetchRegistryImpl() {
        }

        @Override
        public void addPosition(int n, int n2) {
            if (n >= 0) {
                if (n2 >= 0) {
                    int n3 = this.mCount * 2;
                    int[] nArray = this.mPrefetchArray;
                    if (nArray == null) {
                        this.mPrefetchArray = nArray = new int[4];
                        Arrays.fill(nArray, -1);
                    } else if (n3 >= nArray.length) {
                        nArray = this.mPrefetchArray;
                        int[] nArray2 = new int[n3 * 2];
                        this.mPrefetchArray = nArray2;
                        System.arraycopy(nArray, 0, nArray2, 0, nArray.length);
                    }
                    nArray = this.mPrefetchArray;
                    nArray[n3] = n;
                    nArray[n3 + 1] = n2;
                    ++this.mCount;
                    return;
                }
                throw new IllegalArgumentException("Pixel distance must be non-negative");
            }
            throw new IllegalArgumentException("Layout positions must be non-negative");
        }

        void clearPrefetchPositions() {
            int[] nArray = this.mPrefetchArray;
            if (nArray != null) {
                Arrays.fill(nArray, -1);
            }
            this.mCount = 0;
        }

        void collectPrefetchPositionsFromView(RecyclerView recyclerView, boolean bl) {
            this.mCount = 0;
            Object object = this.mPrefetchArray;
            if (object != null) {
                Arrays.fill(object, -1);
            }
            object = recyclerView.mLayout;
            if (recyclerView.mAdapter != null && object != null && object.isItemPrefetchEnabled()) {
                if (bl) {
                    if (!recyclerView.mAdapterHelper.hasPendingUpdates()) {
                        object.collectInitialPrefetchPositions(recyclerView.mAdapter.getItemCount(), this);
                    }
                } else if (!recyclerView.hasPendingAdapterUpdates()) {
                    object.collectAdjacentPrefetchPositions(this.mPrefetchDx, this.mPrefetchDy, recyclerView.mState, this);
                }
                if (this.mCount > object.mPrefetchMaxCountObserved) {
                    object.mPrefetchMaxCountObserved = this.mCount;
                    object.mPrefetchMaxObservedInInitialPrefetch = bl;
                    recyclerView.mRecycler.updateViewCacheSize();
                }
            }
        }

        boolean lastPrefetchIncludedPosition(int n) {
            if (this.mPrefetchArray != null) {
                int n2 = this.mCount;
                for (int i = 0; i < n2 * 2; i += 2) {
                    if (this.mPrefetchArray[i] != n) continue;
                    return true;
                }
            }
            return false;
        }

        void setPrefetchVector(int n, int n2) {
            this.mPrefetchDx = n;
            this.mPrefetchDy = n2;
        }
    }

    static class Task {
        public int distanceToItem;
        public boolean immediate;
        public int position;
        public RecyclerView view;
        public int viewVelocity;

        Task() {
        }

        public void clear() {
            this.immediate = false;
            this.viewVelocity = 0;
            this.distanceToItem = 0;
            this.view = null;
            this.position = 0;
        }
    }
}

