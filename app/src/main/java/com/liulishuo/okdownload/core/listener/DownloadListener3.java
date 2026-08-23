/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.listener;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener1;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;

public abstract class DownloadListener3
extends DownloadListener1 {
    protected abstract void canceled(DownloadTask var1);

    protected abstract void completed(DownloadTask var1);

    protected abstract void error(DownloadTask var1, Exception var2);

    protected abstract void started(DownloadTask var1);

    @Override
    public void taskEnd(DownloadTask comparable, EndCause endCause, Exception exception, Listener1Assist.Listener1Model listener1Model) {
        switch (1.$SwitchMap$com$liulishuo$okdownload$core$cause$EndCause[endCause.ordinal()]) {
            default: {
                comparable = new StringBuilder();
                ((StringBuilder)comparable).append("Don't support ");
                ((StringBuilder)comparable).append((Object)endCause);
                Util.w("DownloadListener3", ((StringBuilder)comparable).toString());
                break;
            }
            case 5: 
            case 6: {
                this.warn((DownloadTask)comparable);
                break;
            }
            case 3: 
            case 4: {
                this.error((DownloadTask)comparable, exception);
                break;
            }
            case 2: {
                this.canceled((DownloadTask)comparable);
                break;
            }
            case 1: {
                this.completed((DownloadTask)comparable);
            }
        }
    }

    @Override
    public final void taskStart(DownloadTask downloadTask, Listener1Assist.Listener1Model listener1Model) {
        this.started(downloadTask);
    }

    protected abstract void warn(DownloadTask var1);
}

