/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.okdownload.core.breakpoint;

import android.util.SparseArray;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.IdentifiedTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.breakpoint.KeyToIdMap;
import com.liulishuo.okdownload.core.cause.EndCause;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BreakpointStoreOnCache
implements DownloadStore {
    public static final int FIRST_ID = 1;
    private final KeyToIdMap keyToIdMap;
    private final HashMap<String, String> responseFilenameMap;
    private final List<Integer> sortedOccupiedIds;
    private final SparseArray<BreakpointInfo> storedInfos;
    private final SparseArray<IdentifiedTask> unStoredTasks;

    public BreakpointStoreOnCache() {
        this((SparseArray<BreakpointInfo>)new SparseArray(), new HashMap<String, String>());
    }

    public BreakpointStoreOnCache(SparseArray<BreakpointInfo> sparseArray, HashMap<String, String> hashMap) {
        this.unStoredTasks = new SparseArray();
        this.storedInfos = sparseArray;
        this.responseFilenameMap = hashMap;
        this.keyToIdMap = new KeyToIdMap();
        int n = sparseArray.size();
        this.sortedOccupiedIds = new ArrayList<Integer>(n);
        for (int i = 0; i < n; ++i) {
            this.sortedOccupiedIds.add(((BreakpointInfo)sparseArray.valueAt((int)i)).id);
        }
        Collections.sort(this.sortedOccupiedIds);
    }

    BreakpointStoreOnCache(SparseArray<BreakpointInfo> sparseArray, HashMap<String, String> hashMap, SparseArray<IdentifiedTask> sparseArray2, List<Integer> list, KeyToIdMap keyToIdMap) {
        this.unStoredTasks = sparseArray2;
        this.storedInfos = sparseArray;
        this.responseFilenameMap = hashMap;
        this.sortedOccupiedIds = list;
        this.keyToIdMap = keyToIdMap;
    }

    /*
     * Unable to fully structure code
     */
    int allocateId() {
        synchronized (this) {
            block12: {
                var6_1 = 0;
                var5_2 = 0;
                var4_3 = 0;
                var3_4 = 0;
                while (true) {
                    block11: {
                        var1_5 = var6_1;
                        var2_6 = var5_2;
                        if (var3_4 >= this.sortedOccupiedIds.size()) break;
                        var7_7 = this.sortedOccupiedIds.get(var3_4);
                        if (var7_7 != null) break block11;
                        var2_6 = var3_4;
                        var1_5 = var4_3 + 1;
                        break;
                    }
                    var1_5 = var7_7.intValue();
                    if (var4_3 == 0) {
                        if (var1_5 != 1) {
                            var1_5 = 1;
                            var2_6 = 0;
                            break;
                        }
                    } else if (var1_5 != var4_3 + 1) {
                        var1_5 = var4_3 + 1;
                        var2_6 = var3_4;
                        break;
                    }
                    var4_3 = var1_5;
                    ++var3_4;
                    continue;
                    break;
                }
                var3_4 = var1_5;
                var4_3 = var2_6;
                if (var1_5 != 0) ** GOTO lbl48
                try {
                    if (!this.sortedOccupiedIds.isEmpty()) break block12;
                    var3_4 = 1;
                    var4_3 = var2_6;
                    ** GOTO lbl48
                }
                catch (Throwable var7_8) {}
                {
                    throw var7_8;
                }
            }
            var7_7 = this.sortedOccupiedIds;
            var3_4 = (Integer)var7_7.get(var7_7.size() - 1) + 1;
            var4_3 = this.sortedOccupiedIds.size();
lbl48:
            // 3 sources

            this.sortedOccupiedIds.add(var4_3, var3_4);
            return var3_4;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public BreakpointInfo createAndInsert(DownloadTask object) {
        int n = ((DownloadTask)object).getId();
        object = new BreakpointInfo(n, ((DownloadTask)object).getUrl(), ((DownloadTask)object).getParentFile(), ((DownloadTask)object).getFilename());
        synchronized (this) {
            this.storedInfos.put(n, object);
            this.unStoredTasks.remove(n);
            return object;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public BreakpointInfo findAnotherInfoFromCompare(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
        SparseArray sparseArray;
        synchronized (this) {
            sparseArray = this.storedInfos.clone();
        }
        int n = sparseArray.size();
        int n2 = 0;
        while (n2 < n) {
            void var2_2;
            BreakpointInfo breakpointInfo2 = (BreakpointInfo)sparseArray.valueAt(n2);
            if (breakpointInfo2 != var2_2 && breakpointInfo2.isSameFrom(downloadTask)) {
                return breakpointInfo2;
            }
            ++n2;
        }
        return null;
    }

    @Override
    public int findOrCreateId(DownloadTask downloadTask) {
        synchronized (this) {
            int n;
            Object object;
            block13: {
                object = this.keyToIdMap.get(downloadTask);
                if (object == null) break block13;
                int n2 = (Integer)object;
                return n2;
            }
            int n3 = this.storedInfos.size();
            for (n = 0; n < n3; ++n) {
                object = (BreakpointInfo)this.storedInfos.valueAt(n);
                if (object == null) continue;
                if (!((BreakpointInfo)object).isSameFrom(downloadTask)) continue;
                n = ((BreakpointInfo)object).id;
                return n;
            }
            n3 = this.unStoredTasks.size();
            for (n = 0; n < n3; ++n) {
                object = (IdentifiedTask)this.unStoredTasks.valueAt(n);
                if (object == null) continue;
                if (!((IdentifiedTask)object).compareIgnoreId(downloadTask)) continue;
                n = ((IdentifiedTask)object).getId();
                return n;
            }
            try {
                n = this.allocateId();
                this.unStoredTasks.put(n, (Object)downloadTask.mock(n));
                this.keyToIdMap.add(downloadTask, n);
                return n;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    @Override
    public BreakpointInfo get(int n) {
        return (BreakpointInfo)this.storedInfos.get(n);
    }

    @Override
    public BreakpointInfo getAfterCompleted(int n) {
        return null;
    }

    @Override
    public String getResponseFilename(String string2) {
        return this.responseFilenameMap.get(string2);
    }

    @Override
    public boolean isOnlyMemoryCache() {
        return true;
    }

    @Override
    public void onSyncToFilesystemSuccess(BreakpointInfo breakpointInfo, int n, long l) throws IOException {
        BreakpointInfo breakpointInfo2 = (BreakpointInfo)this.storedInfos.get(breakpointInfo.id);
        if (breakpointInfo == breakpointInfo2) {
            breakpointInfo2.getBlock(n).increaseCurrentOffset(l);
            return;
        }
        throw new IOException("Info not on store!");
    }

    @Override
    public void onTaskEnd(int n, EndCause endCause, Exception exception) {
        if (endCause == EndCause.COMPLETED) {
            this.remove(n);
        }
    }

    @Override
    public void onTaskStart(int n) {
    }

    @Override
    public void remove(int n) {
        synchronized (this) {
            this.storedInfos.remove(n);
            if (this.unStoredTasks.get(n) == null) {
                this.sortedOccupiedIds.remove((Object)n);
            }
            this.keyToIdMap.remove(n);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean update(BreakpointInfo breakpointInfo) {
        Object object = breakpointInfo.getFilename();
        if (breakpointInfo.isTaskOnlyProvidedParentPath() && object != null) {
            this.responseFilenameMap.put(breakpointInfo.getUrl(), (String)object);
        }
        if ((object = (BreakpointInfo)this.storedInfos.get(breakpointInfo.id)) == null) {
            return false;
        }
        if (object == breakpointInfo) {
            return true;
        }
        synchronized (this) {
            this.storedInfos.put(breakpointInfo.id, (Object)breakpointInfo.copy());
            return true;
        }
    }
}

