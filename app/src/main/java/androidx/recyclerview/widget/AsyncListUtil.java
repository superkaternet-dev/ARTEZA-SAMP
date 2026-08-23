/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.util.SparseBooleanArray
 *  android.util.SparseIntArray
 */
package androidx.recyclerview.widget;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import androidx.recyclerview.widget.MessageThreadUtil;
import androidx.recyclerview.widget.ThreadUtil;
import androidx.recyclerview.widget.TileList;

public class AsyncListUtil<T> {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncListUtil";
    boolean mAllowScrollHints;
    private final ThreadUtil.BackgroundCallback<T> mBackgroundCallback;
    final ThreadUtil.BackgroundCallback<T> mBackgroundProxy;
    final DataCallback<T> mDataCallback;
    int mDisplayedGeneration = 0;
    int mItemCount = 0;
    private final ThreadUtil.MainThreadCallback<T> mMainThreadCallback;
    final ThreadUtil.MainThreadCallback<T> mMainThreadProxy;
    final SparseIntArray mMissingPositions;
    final int[] mPrevRange;
    int mRequestedGeneration = 0;
    private int mScrollHint = 0;
    final Class<T> mTClass;
    final TileList<T> mTileList;
    final int mTileSize;
    final int[] mTmpRange = new int[2];
    final int[] mTmpRangeExtended;
    final ViewCallback mViewCallback;

    public AsyncListUtil(Class<T> object, int n, DataCallback<T> dataCallback, ViewCallback viewCallback) {
        ThreadUtil.BackgroundCallback backgroundCallback;
        ThreadUtil.MainThreadCallback mainThreadCallback;
        this.mPrevRange = new int[2];
        this.mTmpRangeExtended = new int[2];
        this.mMissingPositions = new SparseIntArray();
        this.mMainThreadCallback = mainThreadCallback = new ThreadUtil.MainThreadCallback<T>(this){
            final AsyncListUtil this$0;
            {
                this.this$0 = asyncListUtil;
            }

            private boolean isRequestedGeneration(int n) {
                boolean bl = n == this.this$0.mRequestedGeneration;
                return bl;
            }

            private void recycleAllTiles() {
                for (int i = 0; i < this.this$0.mTileList.size(); ++i) {
                    this.this$0.mBackgroundProxy.recycleTile(this.this$0.mTileList.getAtIndex(i));
                }
                this.this$0.mTileList.clear();
            }

            @Override
            public void addTile(int n, TileList.Tile<T> tile) {
                if (!this.isRequestedGeneration(n)) {
                    this.this$0.mBackgroundProxy.recycleTile(tile);
                    return;
                }
                TileList.Tile tile2 = this.this$0.mTileList.addOrReplace(tile);
                if (tile2 != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("duplicate tile @");
                    stringBuilder.append(tile2.mStartPosition);
                    Log.e((String)AsyncListUtil.TAG, (String)stringBuilder.toString());
                    this.this$0.mBackgroundProxy.recycleTile(tile2);
                }
                int n2 = tile.mStartPosition;
                int n3 = tile.mItemCount;
                n = 0;
                while (n < this.this$0.mMissingPositions.size()) {
                    int n4 = this.this$0.mMissingPositions.keyAt(n);
                    if (tile.mStartPosition <= n4 && n4 < n2 + n3) {
                        this.this$0.mMissingPositions.removeAt(n);
                        this.this$0.mViewCallback.onItemLoaded(n4);
                        continue;
                    }
                    ++n;
                }
            }

            @Override
            public void removeTile(int n, int n2) {
                if (!this.isRequestedGeneration(n)) {
                    return;
                }
                TileList.Tile tile = this.this$0.mTileList.removeAtPos(n2);
                if (tile == null) {
                    tile = new StringBuilder();
                    ((StringBuilder)((Object)tile)).append("tile not found @");
                    ((StringBuilder)((Object)tile)).append(n2);
                    Log.e((String)AsyncListUtil.TAG, (String)((StringBuilder)((Object)tile)).toString());
                    return;
                }
                this.this$0.mBackgroundProxy.recycleTile(tile);
            }

            @Override
            public void updateItemCount(int n, int n2) {
                if (!this.isRequestedGeneration(n)) {
                    return;
                }
                this.this$0.mItemCount = n2;
                this.this$0.mViewCallback.onDataRefresh();
                AsyncListUtil asyncListUtil = this.this$0;
                asyncListUtil.mDisplayedGeneration = asyncListUtil.mRequestedGeneration;
                this.recycleAllTiles();
                this.this$0.mAllowScrollHints = false;
                this.this$0.updateRange();
            }
        };
        this.mBackgroundCallback = backgroundCallback = new ThreadUtil.BackgroundCallback<T>(this){
            private int mFirstRequiredTileStart;
            private int mGeneration;
            private int mItemCount;
            private int mLastRequiredTileStart;
            final SparseBooleanArray mLoadedTiles;
            private TileList.Tile<T> mRecycledRoot;
            final AsyncListUtil this$0;
            {
                this.this$0 = asyncListUtil;
                this.mLoadedTiles = new SparseBooleanArray();
            }

            private TileList.Tile<T> acquireTile() {
                TileList.Tile tile = this.mRecycledRoot;
                if (tile != null) {
                    TileList.Tile tile2 = this.mRecycledRoot;
                    this.mRecycledRoot = tile.mNext;
                    return tile2;
                }
                return new TileList.Tile(this.this$0.mTClass, this.this$0.mTileSize);
            }

            private void addTile(TileList.Tile<T> tile) {
                this.mLoadedTiles.put(tile.mStartPosition, true);
                this.this$0.mMainThreadProxy.addTile(this.mGeneration, tile);
            }

            private void flushTileCache(int n) {
                int n2 = this.this$0.mDataCallback.getMaxCachedTiles();
                while (this.mLoadedTiles.size() >= n2) {
                    int n3 = this.mLoadedTiles.keyAt(0);
                    SparseBooleanArray sparseBooleanArray = this.mLoadedTiles;
                    int n4 = sparseBooleanArray.keyAt(sparseBooleanArray.size() - 1);
                    int n5 = this.mFirstRequiredTileStart - n3;
                    int n6 = n4 - this.mLastRequiredTileStart;
                    if (n5 > 0 && (n5 >= n6 || n == 2)) {
                        this.removeTile(n3);
                        continue;
                    }
                    if (n6 > 0 && (n5 < n6 || n == 1)) {
                        this.removeTile(n4);
                        continue;
                    }
                    return;
                }
            }

            private int getTileStart(int n) {
                return n - n % this.this$0.mTileSize;
            }

            private boolean isTileLoaded(int n) {
                return this.mLoadedTiles.get(n);
            }

            private void log(String string2, Object ... objectArray) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[BKGR] ");
                stringBuilder.append(String.format(string2, objectArray));
                Log.d((String)AsyncListUtil.TAG, (String)stringBuilder.toString());
            }

            private void removeTile(int n) {
                this.mLoadedTiles.delete(n);
                this.this$0.mMainThreadProxy.removeTile(this.mGeneration, n);
            }

            private void requestTiles(int n, int n2, int n3, boolean bl) {
                for (int i = n; i <= n2; i += this.this$0.mTileSize) {
                    int n4 = bl ? n2 + n - i : i;
                    this.this$0.mBackgroundProxy.loadTile(n4, n3);
                }
            }

            @Override
            public void loadTile(int n, int n2) {
                if (this.isTileLoaded(n)) {
                    return;
                }
                TileList.Tile tile = this.acquireTile();
                tile.mStartPosition = n;
                tile.mItemCount = Math.min(this.this$0.mTileSize, this.mItemCount - tile.mStartPosition);
                this.this$0.mDataCallback.fillData(tile.mItems, tile.mStartPosition, tile.mItemCount);
                this.flushTileCache(n2);
                this.addTile(tile);
            }

            @Override
            public void recycleTile(TileList.Tile<T> tile) {
                this.this$0.mDataCallback.recycleData(tile.mItems, tile.mItemCount);
                tile.mNext = this.mRecycledRoot;
                this.mRecycledRoot = tile;
            }

            @Override
            public void refresh(int n) {
                this.mGeneration = n;
                this.mLoadedTiles.clear();
                this.mItemCount = this.this$0.mDataCallback.refreshData();
                this.this$0.mMainThreadProxy.updateItemCount(this.mGeneration, this.mItemCount);
            }

            @Override
            public void updateRange(int n, int n2, int n3, int n4, int n5) {
                if (n > n2) {
                    return;
                }
                n = this.getTileStart(n);
                n2 = this.getTileStart(n2);
                this.mFirstRequiredTileStart = this.getTileStart(n3);
                this.mLastRequiredTileStart = n3 = this.getTileStart(n4);
                if (n5 == 1) {
                    this.requestTiles(this.mFirstRequiredTileStart, n2, n5, true);
                    this.requestTiles(this.this$0.mTileSize + n2, this.mLastRequiredTileStart, n5, false);
                } else {
                    this.requestTiles(n, n3, n5, false);
                    this.requestTiles(this.mFirstRequiredTileStart, n - this.this$0.mTileSize, n5, true);
                }
            }
        };
        this.mTClass = object;
        this.mTileSize = n;
        this.mDataCallback = dataCallback;
        this.mViewCallback = viewCallback;
        this.mTileList = new TileList(n);
        object = new MessageThreadUtil();
        this.mMainThreadProxy = object.getMainThreadProxy(mainThreadCallback);
        this.mBackgroundProxy = object.getBackgroundProxy(backgroundCallback);
        this.refresh();
    }

    private boolean isRefreshPending() {
        boolean bl = this.mRequestedGeneration != this.mDisplayedGeneration;
        return bl;
    }

    public T getItem(int n) {
        if (n >= 0 && n < this.mItemCount) {
            T t = this.mTileList.getItemAt(n);
            if (t == null && !this.isRefreshPending()) {
                this.mMissingPositions.put(n, 0);
            }
            return t;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n);
        stringBuilder.append(" is not within 0 and ");
        stringBuilder.append(this.mItemCount);
        throw new IndexOutOfBoundsException(stringBuilder.toString());
    }

    public int getItemCount() {
        return this.mItemCount;
    }

    void log(String string2, Object ... objectArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[MAIN] ");
        stringBuilder.append(String.format(string2, objectArray));
        Log.d((String)TAG, (String)stringBuilder.toString());
    }

    public void onRangeChanged() {
        if (this.isRefreshPending()) {
            return;
        }
        this.updateRange();
        this.mAllowScrollHints = true;
    }

    public void refresh() {
        int n;
        this.mMissingPositions.clear();
        ThreadUtil.BackgroundCallback<T> backgroundCallback = this.mBackgroundProxy;
        this.mRequestedGeneration = n = this.mRequestedGeneration + 1;
        backgroundCallback.refresh(n);
    }

    void updateRange() {
        this.mViewCallback.getItemRangeInto(this.mTmpRange);
        Object object = this.mTmpRange;
        if (object[0] <= object[1] && object[0] >= 0) {
            int[] nArray;
            int n;
            if (object[1] >= this.mItemCount) {
                return;
            }
            if (!this.mAllowScrollHints) {
                this.mScrollHint = 0;
            } else {
                n = object[0];
                nArray = this.mPrevRange;
                if (n <= nArray[1] && nArray[0] <= object[1]) {
                    if (object[0] < nArray[0]) {
                        this.mScrollHint = 1;
                    } else if (object[0] > nArray[0]) {
                        this.mScrollHint = 2;
                    }
                } else {
                    this.mScrollHint = 0;
                }
            }
            nArray = this.mPrevRange;
            nArray[0] = object[0];
            nArray[1] = object[1];
            this.mViewCallback.extendRangeInto((int[])object, this.mTmpRangeExtended, this.mScrollHint);
            object = this.mTmpRangeExtended;
            object[0] = Math.min(this.mTmpRange[0], Math.max(object[0], 0));
            object = this.mTmpRangeExtended;
            object[1] = Math.max(this.mTmpRange[1], Math.min(object[1], this.mItemCount - 1));
            object = this.mBackgroundProxy;
            nArray = this.mTmpRange;
            n = nArray[0];
            int n2 = nArray[1];
            nArray = this.mTmpRangeExtended;
            object.updateRange(n, n2, nArray[0], nArray[1], this.mScrollHint);
            return;
        }
    }

    public static abstract class DataCallback<T> {
        public abstract void fillData(T[] var1, int var2, int var3);

        public int getMaxCachedTiles() {
            return 10;
        }

        public void recycleData(T[] TArray, int n) {
        }

        public abstract int refreshData();
    }

    public static abstract class ViewCallback {
        public static final int HINT_SCROLL_ASC = 2;
        public static final int HINT_SCROLL_DESC = 1;
        public static final int HINT_SCROLL_NONE = 0;

        public void extendRangeInto(int[] nArray, int[] nArray2, int n) {
            int n2 = nArray[1] - nArray[0] + 1;
            int n3 = n2 / 2;
            int n4 = nArray[0];
            int n5 = n == 1 ? n2 : n3;
            nArray2[0] = n4 - n5;
            n5 = nArray[1];
            if (n != 2) {
                n2 = n3;
            }
            nArray2[1] = n5 + n2;
        }

        public abstract void getItemRangeInto(int[] var1);

        public abstract void onDataRefresh();

        public abstract void onItemLoaded(int var1);
    }
}

