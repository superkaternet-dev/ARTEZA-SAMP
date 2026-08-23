/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.download;

import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.liulishuo.filedownloader.services.ForegroundServiceConfig;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.io.IOException;

public class CustomComponentHolder {
    private FileDownloadHelper.ConnectionCountAdapter connectionCountAdapter;
    private FileDownloadHelper.ConnectionCreator connectionCreator;
    private FileDownloadDatabase database;
    private ForegroundServiceConfig foregroundServiceConfig;
    private FileDownloadHelper.IdGenerator idGenerator;
    private DownloadMgrInitialParams initialParams;
    private FileDownloadHelper.OutputStreamCreator outputStreamCreator;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private FileDownloadHelper.ConnectionCountAdapter getConnectionCountAdapter() {
        FileDownloadHelper.ConnectionCountAdapter connectionCountAdapter = this.connectionCountAdapter;
        if (connectionCountAdapter != null) {
            return connectionCountAdapter;
        }
        synchronized (this) {
            if (this.connectionCountAdapter == null) {
                this.connectionCountAdapter = this.getDownloadMgrInitialParams().createConnectionCountAdapter();
            }
            return this.connectionCountAdapter;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private FileDownloadHelper.ConnectionCreator getConnectionCreator() {
        FileDownloadHelper.ConnectionCreator connectionCreator = this.connectionCreator;
        if (connectionCreator != null) {
            return connectionCreator;
        }
        synchronized (this) {
            if (this.connectionCreator == null) {
                this.connectionCreator = this.getDownloadMgrInitialParams().createConnectionCreator();
            }
            return this.connectionCreator;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private DownloadMgrInitialParams getDownloadMgrInitialParams() {
        DownloadMgrInitialParams downloadMgrInitialParams = this.initialParams;
        if (downloadMgrInitialParams != null) {
            return downloadMgrInitialParams;
        }
        synchronized (this) {
            if (this.initialParams == null) {
                this.initialParams = downloadMgrInitialParams = new DownloadMgrInitialParams();
            }
            return this.initialParams;
        }
    }

    public static CustomComponentHolder getImpl() {
        return LazyLoader.INSTANCE;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private FileDownloadHelper.OutputStreamCreator getOutputStreamCreator() {
        FileDownloadHelper.OutputStreamCreator outputStreamCreator = this.outputStreamCreator;
        if (outputStreamCreator != null) {
            return outputStreamCreator;
        }
        synchronized (this) {
            if (this.outputStreamCreator == null) {
                this.outputStreamCreator = this.getDownloadMgrInitialParams().createOutputStreamCreator();
            }
            return this.outputStreamCreator;
        }
    }

    /*
     * Unable to fully structure code
     */
    private static void maintainDatabase(FileDownloadDatabase.Maintainer var0) {
        block37: {
            block30: {
                block28: {
                    var16_1 = "refreshed data count: %d , delete data count: %d, reset id count: %d. consume %d";
                    var17_2 = var0.iterator();
                    var7_8 = 0L;
                    var13_9 = 0L;
                    var3_10 = 0L;
                    var18_11 = CustomComponentHolder.getImpl().getIdGeneratorInstance();
                    var11_12 = System.currentTimeMillis();
                    while (true) {
                        block31: {
                            block29: {
                                if (!var17_2.hasNext()) break block28;
                                var20_19 = (FileDownloadModel)var17_2.next();
                                var1_13 = var20_19.getStatus();
                                if (var1_13 == 3) break block29;
                                try {
                                    if (var20_19.getStatus() == 2 || var20_19.getStatus() == -1 || var20_19.getStatus() == 1 && (var5_15 = var20_19.getSoFar()) > 0L) {
                                    }
                                    ** GOTO lbl24
                                }
                                catch (Throwable var17_3) {
                                    var18_11 = var16_1;
                                    break block30;
                                }
                            }
                            var20_19.setStatus((byte)-2);
lbl24:
                            // 2 sources

                            var21_20 = var20_19.getTargetFilePath();
                            if (var21_20 != null) break block31;
                            var1_13 = 1;
                            break block32;
                        }
                        var19_18 = new File((String)var21_20);
                        if (var20_19.getStatus() != -2) break block33;
                        var1_13 = var20_19.getId();
                        var21_20 = var20_19.getPath();
                        if (!FileDownloadUtils.isBreakpointAvailable(var1_13, var20_19, (String)var21_20, null) || (var21_20 = new File(var20_19.getTempFilePath())).exists() || !var19_18.exists()) break block33;
                        var15_17 = var19_18.renameTo((File)var21_20);
                        if (!FileDownloadLog.NEED_LOG) break block33;
                        var9_16 = var3_10;
                        FileDownloadLog.d(FileDownloadDatabase.class, "resume from the old no-temp-file architecture [%B], [%s]->[%s]", new Object[]{var15_17, var19_18.getPath(), var21_20.getPath()});
                        break block33;
                        break;
                    }
                    catch (Throwable var17_5) {
                        var18_11 = var16_1;
                        var3_10 = var9_16;
                        break block30;
                    }
                    {
                        block35: {
                            block36: {
                                block32: {
                                    block34: {
                                        block33: {
                                            catch (Throwable var17_4) {
                                                var18_11 = var16_1;
                                                break block30;
                                            }
                                        }
                                        var9_16 = var3_10;
                                        if (var20_19.getStatus() != 1) break block34;
                                        var9_16 = var3_10;
                                        if (var20_19.getSoFar() > 0L) break block34;
                                        var1_13 = 1;
                                        break block32;
                                    }
                                    var9_16 = var3_10;
                                    if (!FileDownloadUtils.isBreakpointAvailable(var20_19.getId(), var20_19)) {
                                        var1_13 = 1;
                                        break block32;
                                    }
                                    var9_16 = var3_10;
                                    var1_13 = var19_18.exists() != false ? 1 : 0;
                                }
                                if (var1_13 != 0) {
                                    var9_16 = var3_10;
                                    var17_2.remove();
                                    var9_16 = var3_10;
                                    var0.onRemovedInvalidData(var20_19);
                                    ++var13_9;
                                    continue;
                                }
                                var9_16 = var3_10;
                                var1_13 = var20_19.getId();
                                var9_16 = var3_10;
                                var2_14 = var18_11.transOldId(var1_13, var20_19.getUrl(), var20_19.getPath(), var20_19.isPathAsDirectory());
                                var5_15 = var3_10;
                                if (var2_14 == var1_13) break block35;
                                var9_16 = var3_10;
                                if (!FileDownloadLog.NEED_LOG) break block36;
                                var9_16 = var3_10;
                                FileDownloadLog.d(FileDownloadDatabase.class, "the id is changed on restoring from db: old[%d] -> new[%d]", new Object[]{var1_13, var2_14});
                            }
                            var9_16 = var3_10;
                            var20_19.setId(var2_14);
                            var9_16 = var3_10;
                            var0.changeFileDownloadModelId(var1_13, var20_19);
                            var5_15 = var3_10 + 1L;
                        }
                        var9_16 = var5_15;
                        var0.onRefreshedValidData(var20_19);
                        ++var7_8;
                        var3_10 = var5_15;
                        continue;
                    }
                }
                FileDownloadUtils.markConverted(FileDownloadHelper.getAppContext());
                var0.onFinishMaintain();
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(FileDownloadDatabase.class, var16_1, new Object[]{var7_8, var13_9, var3_10, System.currentTimeMillis() - var11_12});
                }
                return;
                catch (Throwable var17_6) {
                    var18_11 = var16_1;
                }
            }
            FileDownloadUtils.markConverted(FileDownloadHelper.getAppContext());
            var0.onFinishMaintain();
            if (!FileDownloadLog.NEED_LOG) break block37;
            FileDownloadLog.d(FileDownloadDatabase.class, var16_1, new Object[]{var7_8, var13_9, var3_10, System.currentTimeMillis() - var11_12});
        }
        throw var17_7;
    }

    public FileDownloadConnection createConnection(String string2) throws IOException {
        return this.getConnectionCreator().create(string2);
    }

    public FileDownloadOutputStream createOutputStream(File file) throws IOException {
        return this.getOutputStreamCreator().create(file);
    }

    public int determineConnectionCount(int n, String string2, String string3, long l) {
        return this.getConnectionCountAdapter().determineConnectionCount(n, string2, string3, l);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public FileDownloadDatabase getDatabaseInstance() {
        FileDownloadDatabase fileDownloadDatabase = this.database;
        if (fileDownloadDatabase != null) {
            return fileDownloadDatabase;
        }
        synchronized (this) {
            if (this.database == null) {
                this.database = fileDownloadDatabase = this.getDownloadMgrInitialParams().createDatabase();
                CustomComponentHolder.maintainDatabase(fileDownloadDatabase.maintainer());
            }
            return this.database;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ForegroundServiceConfig getForegroundConfigInstance() {
        ForegroundServiceConfig foregroundServiceConfig = this.foregroundServiceConfig;
        if (foregroundServiceConfig != null) {
            return foregroundServiceConfig;
        }
        synchronized (this) {
            if (this.foregroundServiceConfig == null) {
                this.foregroundServiceConfig = this.getDownloadMgrInitialParams().createForegroundServiceConfig();
            }
            return this.foregroundServiceConfig;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public FileDownloadHelper.IdGenerator getIdGeneratorInstance() {
        FileDownloadHelper.IdGenerator idGenerator = this.idGenerator;
        if (idGenerator != null) {
            return idGenerator;
        }
        synchronized (this) {
            if (this.idGenerator == null) {
                this.idGenerator = this.getDownloadMgrInitialParams().createIdGenerator();
            }
            return this.idGenerator;
        }
    }

    public int getMaxNetworkThreadCount() {
        return this.getDownloadMgrInitialParams().getMaxNetworkThreadCount();
    }

    public boolean isSupportSeek() {
        return this.getOutputStreamCreator().supportSeek();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setInitCustomMaker(DownloadMgrInitialParams.InitCustomMaker initCustomMaker) {
        synchronized (this) {
            DownloadMgrInitialParams downloadMgrInitialParams;
            this.initialParams = downloadMgrInitialParams = new DownloadMgrInitialParams(initCustomMaker);
            this.connectionCreator = null;
            this.outputStreamCreator = null;
            this.database = null;
            this.idGenerator = null;
            return;
        }
    }

    private static final class LazyLoader {
        private static final CustomComponentHolder INSTANCE = new CustomComponentHolder();

        private LazyLoader() {
        }
    }
}

