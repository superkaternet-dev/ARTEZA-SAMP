/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.framed;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import okhttp3.internal.framed.Header;
import okhttp3.internal.framed.Spdy3;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSource;
import okio.InflaterSource;
import okio.Okio;

class NameValueBlockReader {
    private int compressedLimit;
    private final InflaterSource inflaterSource;
    private final BufferedSource source;

    public NameValueBlockReader(BufferedSource source) {
        source = new InflaterSource(new ForwardingSource(this, source){
            final NameValueBlockReader this$0;
            {
                this.this$0 = nameValueBlockReader;
                super(source);
            }

            @Override
            public long read(Buffer object, long l) throws IOException {
                if (this.this$0.compressedLimit == 0) {
                    return -1L;
                }
                if ((l = super.read((Buffer)object, Math.min(l, (long)this.this$0.compressedLimit))) == -1L) {
                    return -1L;
                }
                object = this.this$0;
                NameValueBlockReader.access$002((NameValueBlockReader)object, (int)((long)((NameValueBlockReader)object).compressedLimit - l));
                return l;
            }
        }, new Inflater(this){
            final NameValueBlockReader this$0;
            {
                this.this$0 = nameValueBlockReader;
            }

            @Override
            public int inflate(byte[] byArray, int n, int n2) throws DataFormatException {
                int n3;
                int n4 = n3 = super.inflate(byArray, n, n2);
                if (n3 == 0) {
                    n4 = n3;
                    if (this.needsDictionary()) {
                        this.setDictionary(Spdy3.DICTIONARY);
                        n4 = super.inflate(byArray, n, n2);
                    }
                }
                return n4;
            }
        });
        this.inflaterSource = source;
        this.source = Okio.buffer(source);
    }

    static /* synthetic */ int access$002(NameValueBlockReader nameValueBlockReader, int n) {
        nameValueBlockReader.compressedLimit = n;
        return n;
    }

    private void doneReading() throws IOException {
        if (this.compressedLimit > 0) {
            this.inflaterSource.refill();
            if (this.compressedLimit != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("compressedLimit > 0: ");
                stringBuilder.append(this.compressedLimit);
                throw new IOException(stringBuilder.toString());
            }
        }
    }

    private ByteString readByteString() throws IOException {
        int n = this.source.readInt();
        return this.source.readByteString(n);
    }

    public void close() throws IOException {
        this.source.close();
    }

    public List<Header> readNameValueBlock(int n) throws IOException {
        this.compressedLimit += n;
        int n2 = this.source.readInt();
        if (n2 >= 0) {
            if (n2 <= 1024) {
                ArrayList<Header> arrayList = new ArrayList<Header>(n2);
                for (n = 0; n < n2; ++n) {
                    ByteString byteString = this.readByteString().toAsciiLowercase();
                    ByteString byteString2 = this.readByteString();
                    if (byteString.size() != 0) {
                        arrayList.add(new Header(byteString, byteString2));
                        continue;
                    }
                    throw new IOException("name.size == 0");
                }
                this.doneReading();
                return arrayList;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("numberOfPairs > 1024: ");
            stringBuilder.append(n2);
            throw new IOException(stringBuilder.toString());
        }
        Serializable serializable = new StringBuilder();
        serializable.append("numberOfPairs < 0: ");
        serializable.append(n2);
        serializable = new IOException(serializable.toString());
        throw serializable;
    }
}

