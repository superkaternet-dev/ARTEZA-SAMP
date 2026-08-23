/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.tubesock;

import com.google.firebase.database.tubesock.WebSocketMessage;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;

class MessageBuilderFactory {
    MessageBuilderFactory() {
    }

    static Builder builder(byte by) {
        if (by == 2) {
            return new BinaryBuilder();
        }
        return new TextBuilder();
    }

    static class BinaryBuilder
    implements Builder {
        private int pendingByteCount = 0;
        private List<byte[]> pendingBytes = new ArrayList<byte[]>();

        BinaryBuilder() {
        }

        @Override
        public boolean appendBytes(byte[] byArray) {
            this.pendingBytes.add(byArray);
            this.pendingByteCount += byArray.length;
            return true;
        }

        @Override
        public WebSocketMessage toMessage() {
            byte[] byArray = new byte[this.pendingByteCount];
            int n = 0;
            for (int i = 0; i < this.pendingBytes.size(); ++i) {
                byte[] byArray2 = this.pendingBytes.get(i);
                System.arraycopy(byArray2, 0, byArray, n, byArray2.length);
                n += byArray2.length;
            }
            return new WebSocketMessage(byArray);
        }
    }

    static interface Builder {
        public boolean appendBytes(byte[] var1);

        public WebSocketMessage toMessage();
    }

    static class TextBuilder
    implements Builder {
        private static ThreadLocal<CharsetDecoder> localDecoder = new ThreadLocal<CharsetDecoder>(){

            @Override
            protected CharsetDecoder initialValue() {
                CharsetDecoder charsetDecoder = Charset.forName("UTF8").newDecoder();
                charsetDecoder.onMalformedInput(CodingErrorAction.REPORT);
                charsetDecoder.onUnmappableCharacter(CodingErrorAction.REPORT);
                return charsetDecoder;
            }
        };
        private static ThreadLocal<CharsetEncoder> localEncoder = new ThreadLocal<CharsetEncoder>(){

            @Override
            protected CharsetEncoder initialValue() {
                CharsetEncoder charsetEncoder = Charset.forName("UTF8").newEncoder();
                charsetEncoder.onMalformedInput(CodingErrorAction.REPORT);
                charsetEncoder.onUnmappableCharacter(CodingErrorAction.REPORT);
                return charsetEncoder;
            }
        };
        private StringBuilder builder = new StringBuilder();
        private ByteBuffer carryOver;

        TextBuilder() {
        }

        private String decodeString(byte[] object) {
            try {
                object = ByteBuffer.wrap(object);
                object = localDecoder.get().decode((ByteBuffer)object).toString();
                return object;
            }
            catch (CharacterCodingException characterCodingException) {
                return null;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private String decodeStringStreaming(byte[] object) {
            try {
                ByteBuffer byteBuffer = this.getBuffer((byte[])object);
                int n = (int)((float)byteBuffer.remaining() * localDecoder.get().averageCharsPerByte());
                object = CharBuffer.allocate(n);
                while (true) {
                    Object object2;
                    CoderResult coderResult;
                    if ((coderResult = localDecoder.get().decode(byteBuffer, (CharBuffer)object, false)).isError()) {
                        return null;
                    }
                    if (coderResult.isUnderflow()) {
                        if (byteBuffer.remaining() > 0) {
                            this.carryOver = byteBuffer;
                        }
                        object2 = CharBuffer.wrap((CharSequence)object);
                        localEncoder.get().encode((CharBuffer)object2);
                        ((CharBuffer)object).flip();
                        return ((CharBuffer)object).toString();
                    }
                    int n2 = n;
                    object2 = object;
                    if (coderResult.isOverflow()) {
                        n2 = n * 2 + 1;
                        object2 = CharBuffer.allocate(n2);
                        ((CharBuffer)object).flip();
                        ((CharBuffer)object2).put((CharBuffer)object);
                    }
                    n = n2;
                    object = object2;
                }
            }
            catch (CharacterCodingException characterCodingException) {
                return null;
            }
        }

        private ByteBuffer getBuffer(byte[] byArray) {
            ByteBuffer byteBuffer = this.carryOver;
            if (byteBuffer != null) {
                byteBuffer = ByteBuffer.allocate(byArray.length + byteBuffer.remaining());
                byteBuffer.put(this.carryOver);
                this.carryOver = null;
                byteBuffer.put(byArray);
                byteBuffer.flip();
                return byteBuffer;
            }
            return ByteBuffer.wrap(byArray);
        }

        @Override
        public boolean appendBytes(byte[] object) {
            if ((object = (Object)this.decodeString((byte[])object)) != null) {
                this.builder.append((String)object);
                return true;
            }
            return false;
        }

        @Override
        public WebSocketMessage toMessage() {
            if (this.carryOver != null) {
                return null;
            }
            return new WebSocketMessage(this.builder.toString());
        }
    }
}

