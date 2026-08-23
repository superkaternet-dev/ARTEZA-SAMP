/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.tubesock;

import com.google.firebase.database.tubesock.ThreadInitializer;
import com.google.firebase.database.tubesock.WebSocket;
import com.google.firebase.database.tubesock.WebSocketException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class WebSocketWriter {
    private WritableByteChannel channel;
    private boolean closeSent = false;
    private final Thread innerThread;
    private BlockingQueue<ByteBuffer> pendingBuffers;
    private final Random random = new Random();
    private volatile boolean stop = false;
    private WebSocket websocket;

    WebSocketWriter(WebSocket webSocket, String string2, int n) {
        this.innerThread = WebSocket.getThreadFactory().newThread(new Runnable(this){
            final WebSocketWriter this$0;
            {
                this.this$0 = webSocketWriter;
            }

            @Override
            public void run() {
                this.this$0.runWriter();
            }
        });
        ThreadInitializer threadInitializer = WebSocket.getIntializer();
        Thread thread2 = this.getInnerThread();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("Writer-");
        stringBuilder.append(n);
        threadInitializer.setName(thread2, stringBuilder.toString());
        this.websocket = webSocket;
        this.pendingBuffers = new LinkedBlockingQueue<ByteBuffer>();
    }

    private ByteBuffer frameInBuffer(byte by, boolean bl, byte[] byArray) throws IOException {
        int n;
        int n2 = 2;
        if (bl) {
            n2 = 2 + 4;
        }
        if ((n = byArray.length) >= 126) {
            n2 = n <= 65535 ? (n2 += 2) : (n2 += 8);
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(byArray.length + n2);
        byteBuffer.put((byte)(0xFFFFFF80 | by));
        if (n < 126) {
            by = (byte)n;
            if (bl) {
                by = (byte)(n | 0x80);
            }
            byteBuffer.put(by);
        } else if (n <= 65535) {
            by = (byte)126;
            if (bl) {
                by = (byte)(0x7E | 0x80);
            }
            byteBuffer.put(by);
            byteBuffer.putShort((short)n);
        } else {
            by = (byte)127;
            if (bl) {
                by = (byte)(0x7F | 0x80);
            }
            byteBuffer.put(by);
            byteBuffer.putInt(0);
            byteBuffer.putInt(n);
        }
        if (bl) {
            byte[] byArray2 = this.generateMask();
            byteBuffer.put(byArray2);
            for (by = 0; by < byArray.length; by = (byte)(by + 1)) {
                byteBuffer.put((byte)(byArray[by] ^ byArray2[by % 4]));
            }
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    private byte[] generateMask() {
        byte[] byArray = new byte[4];
        this.random.nextBytes(byArray);
        return byArray;
    }

    private void handleError(WebSocketException webSocketException) {
        this.websocket.handleReceiverError(webSocketException);
    }

    private void runWriter() {
        block7: {
            int n;
            try {
                while (!this.stop && !Thread.interrupted()) {
                    this.writeMessage();
                }
                n = 0;
            }
            catch (InterruptedException interruptedException) {
                break block7;
            }
            catch (IOException iOException) {
                this.handleError(new WebSocketException("IO Exception", iOException));
            }
            while (true) {
                if (n < this.pendingBuffers.size()) {
                    this.writeMessage();
                    ++n;
                    continue;
                }
                break;
            }
        }
    }

    private void writeMessage() throws InterruptedException, IOException {
        ByteBuffer byteBuffer = this.pendingBuffers.take();
        this.channel.write(byteBuffer);
    }

    Thread getInnerThread() {
        return this.innerThread;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void send(byte by, boolean bl, byte[] object) throws IOException {
        synchronized (this) {
            ByteBuffer byteBuffer = this.frameInBuffer(by, bl, (byte[])object);
            if (this.stop && (this.closeSent || by != 8)) {
                WebSocketException webSocketException = new WebSocketException("Shouldn't be sending");
                throw webSocketException;
            }
            if (by == 8) {
                this.closeSent = true;
            }
            this.pendingBuffers.add(byteBuffer);
            return;
        }
    }

    void setOutput(OutputStream outputStream) {
        this.channel = Channels.newChannel(outputStream);
    }

    void stopIt() {
        this.stop = true;
    }
}

