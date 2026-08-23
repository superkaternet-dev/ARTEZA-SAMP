/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.model;

import com.liulishuo.filedownloader.BaseDownloadTask;

public class FileDownloadStatus {
    public static final byte INVALID_STATUS = 0;
    public static final byte blockComplete = 4;
    public static final byte completed = -3;
    public static final byte connected = 2;
    public static final byte error = -1;
    public static final byte paused = -2;
    public static final byte pending = 1;
    public static final byte progress = 3;
    public static final byte retry = 5;
    public static final byte started = 6;
    public static final byte toFileDownloadService = 11;
    public static final byte toLaunchPool = 10;
    public static final byte warn = -4;

    public static boolean isIng(int n) {
        boolean bl = n > 0;
        return bl;
    }

    public static boolean isKeepAhead(int n, int n2) {
        if (n != 3 && n != 5 && n == n2) {
            return false;
        }
        if (FileDownloadStatus.isOver(n)) {
            return false;
        }
        if (n >= 1 && n <= 6 && n2 >= 10 && n2 <= 11) {
            return false;
        }
        switch (n) {
            default: {
                return true;
            }
            case 6: {
                switch (n2) {
                    default: {
                        return true;
                    }
                    case 0: 
                    case 1: 
                }
                return false;
            }
            case 5: {
                switch (n2) {
                    default: {
                        return true;
                    }
                    case 1: 
                    case 6: 
                }
                return false;
            }
            case 3: {
                switch (n2) {
                    default: {
                        return true;
                    }
                    case 0: 
                    case 1: 
                    case 2: 
                    case 6: 
                }
                return false;
            }
            case 2: {
                switch (n2) {
                    default: {
                        return true;
                    }
                    case 0: 
                    case 1: 
                    case 6: 
                }
                return false;
            }
            case 1: 
        }
        switch (n2) {
            default: {
                return true;
            }
            case 0: 
        }
        return false;
    }

    public static boolean isKeepFlow(int n, int n2) {
        if (n != 3 && n != 5 && n == n2) {
            return false;
        }
        if (FileDownloadStatus.isOver(n)) {
            return false;
        }
        if (n2 == -2) {
            return true;
        }
        if (n2 == -1) {
            return true;
        }
        switch (n) {
            default: {
                return false;
            }
            case 11: {
                switch (n2) {
                    default: {
                        return false;
                    }
                    case -4: 
                    case -3: 
                    case 1: 
                }
                return true;
            }
            case 10: {
                switch (n2) {
                    default: {
                        return false;
                    }
                    case 11: 
                }
                return true;
            }
            case 5: 
            case 6: {
                switch (n2) {
                    default: {
                        return false;
                    }
                    case 2: 
                    case 5: 
                }
                return true;
            }
            case 2: 
            case 3: {
                switch (n2) {
                    default: {
                        return false;
                    }
                    case -3: 
                    case 3: 
                    case 5: 
                }
                return true;
            }
            case 1: {
                switch (n2) {
                    default: {
                        return false;
                    }
                    case 6: 
                }
                return true;
            }
            case 0: 
        }
        switch (n2) {
            default: {
                return false;
            }
            case 10: 
        }
        return true;
    }

    public static boolean isMoreLikelyCompleted(BaseDownloadTask baseDownloadTask) {
        boolean bl = baseDownloadTask.getStatus() == 0 || baseDownloadTask.getStatus() == 3;
        return bl;
    }

    public static boolean isOver(int n) {
        boolean bl = n < 0;
        return bl;
    }
}

