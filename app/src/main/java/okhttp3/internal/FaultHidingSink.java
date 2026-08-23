/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.io.IOException;
import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

class FaultHidingSink
extends ForwardingSink {
    private boolean hasErrors;

    public FaultHidingSink(Sink sink) {
        super(sink);
    }

    @Override
    public void close() throws IOException {
        if (this.hasErrors) {
            return;
        }
        try {
            super.close();
        }
        catch (IOException iOException) {
            this.hasErrors = true;
            this.onException(iOException);
        }
    }

    @Override
    public void flush() throws IOException {
        if (this.hasErrors) {
            return;
        }
        try {
            super.flush();
        }
        catch (IOException iOException) {
            this.hasErrors = true;
            this.onException(iOException);
        }
    }

    protected void onException(IOException iOException) {
    }

    @Override
    public void write(Buffer buffer, long l) throws IOException {
        if (this.hasErrors) {
            buffer.skip(l);
            return;
        }
        try {
            super.write(buffer, l);
        }
        catch (IOException iOException) {
            this.hasErrors = true;
            this.onException(iOException);
        }
    }
}

