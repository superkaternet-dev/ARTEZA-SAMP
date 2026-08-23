/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 */
package androidx.recyclerview.widget;

import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class AsyncListDiffer<T> {
    private static final Executor sMainThreadExecutor = new MainThreadExecutor();
    final AsyncDifferConfig<T> mConfig;
    private List<T> mList;
    final Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    private List<T> mReadOnlyList = Collections.emptyList();
    private final ListUpdateCallback mUpdateCallback;

    public AsyncListDiffer(ListUpdateCallback listUpdateCallback, AsyncDifferConfig<T> asyncDifferConfig) {
        this.mUpdateCallback = listUpdateCallback;
        this.mConfig = asyncDifferConfig;
        this.mMainThreadExecutor = asyncDifferConfig.getMainThreadExecutor() != null ? asyncDifferConfig.getMainThreadExecutor() : sMainThreadExecutor;
    }

    public AsyncListDiffer(RecyclerView.Adapter adapter, DiffUtil.ItemCallback<T> itemCallback) {
        this(new AdapterListUpdateCallback(adapter), new AsyncDifferConfig.Builder<T>(itemCallback).build());
    }

    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }

    void latchList(List<T> list, DiffUtil.DiffResult diffResult) {
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList(list);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
    }

    public void submitList(List<T> list) {
        int n;
        this.mMaxScheduledGeneration = n = this.mMaxScheduledGeneration + 1;
        List<T> list2 = this.mList;
        if (list == list2) {
            return;
        }
        if (list == null) {
            n = list2.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, n);
            return;
        }
        if (list2 == null) {
            this.mList = list;
            this.mReadOnlyList = Collections.unmodifiableList(list);
            this.mUpdateCallback.onInserted(0, list.size());
            return;
        }
        list2 = this.mList;
        this.mConfig.getBackgroundThreadExecutor().execute(new Runnable(this, list2, list, n){
            final AsyncListDiffer this$0;
            final List val$newList;
            final List val$oldList;
            final int val$runGeneration;
            {
                this.this$0 = asyncListDiffer;
                this.val$oldList = list;
                this.val$newList = list2;
                this.val$runGeneration = n;
            }

            @Override
            public void run() {
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback(this){
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public boolean areContentsTheSame(int n, int n2) {
                        Object e = this.this$1.val$oldList.get(n);
                        Object e2 = this.this$1.val$newList.get(n2);
                        if (e != null && e2 != null) {
                            return this.this$1.this$0.mConfig.getDiffCallback().areContentsTheSame(e, e2);
                        }
                        if (e == null && e2 == null) {
                            return true;
                        }
                        throw new AssertionError();
                    }

                    @Override
                    public boolean areItemsTheSame(int n, int n2) {
                        Object e = this.this$1.val$oldList.get(n);
                        Object e2 = this.this$1.val$newList.get(n2);
                        if (e != null && e2 != null) {
                            return this.this$1.this$0.mConfig.getDiffCallback().areItemsTheSame(e, e2);
                        }
                        boolean bl = e == null && e2 == null;
                        return bl;
                    }

                    @Override
                    public Object getChangePayload(int n, int n2) {
                        Object e = this.this$1.val$oldList.get(n);
                        Object e2 = this.this$1.val$newList.get(n2);
                        if (e != null && e2 != null) {
                            return this.this$1.this$0.mConfig.getDiffCallback().getChangePayload(e, e2);
                        }
                        throw new AssertionError();
                    }

                    @Override
                    public int getNewListSize() {
                        return this.this$1.val$newList.size();
                    }

                    @Override
                    public int getOldListSize() {
                        return this.this$1.val$oldList.size();
                    }
                });
                this.this$0.mMainThreadExecutor.execute(new Runnable(this, diffResult){
                    final 1 this$1;
                    final DiffUtil.DiffResult val$result;
                    {
                        this.this$1 = var1_1;
                        this.val$result = diffResult;
                    }

                    @Override
                    public void run() {
                        if (this.this$1.this$0.mMaxScheduledGeneration == this.this$1.val$runGeneration) {
                            this.this$1.this$0.latchList(this.this$1.val$newList, this.val$result);
                        }
                    }
                });
            }
        });
    }

    private static class MainThreadExecutor
    implements Executor {
        final Handler mHandler = new Handler(Looper.getMainLooper());

        MainThreadExecutor() {
        }

        @Override
        public void execute(Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }
}

