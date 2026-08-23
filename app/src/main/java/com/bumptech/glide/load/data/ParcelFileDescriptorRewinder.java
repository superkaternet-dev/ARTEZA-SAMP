/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 *  android.system.ErrnoException
 *  android.system.Os
 *  android.system.OsConstants
 */
package com.bumptech.glide.load.data;

import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import com.bumptech.glide.load.data.DataRewinder;
import java.io.FileDescriptor;
import java.io.IOException;

public final class ParcelFileDescriptorRewinder
implements DataRewinder<ParcelFileDescriptor> {
    private final InternalRewinder rewinder;

    public ParcelFileDescriptorRewinder(ParcelFileDescriptor parcelFileDescriptor) {
        this.rewinder = new InternalRewinder(parcelFileDescriptor);
    }

    public static boolean isSupported() {
        boolean bl = Build.VERSION.SDK_INT >= 21;
        return bl;
    }

    @Override
    public void cleanup() {
    }

    @Override
    public ParcelFileDescriptor rewindAndGet() throws IOException {
        return this.rewinder.rewind();
    }

    public static final class Factory
    implements DataRewinder.Factory<ParcelFileDescriptor> {
        @Override
        public DataRewinder<ParcelFileDescriptor> build(ParcelFileDescriptor parcelFileDescriptor) {
            return new ParcelFileDescriptorRewinder(parcelFileDescriptor);
        }

        @Override
        public Class<ParcelFileDescriptor> getDataClass() {
            return ParcelFileDescriptor.class;
        }
    }

    private static final class InternalRewinder {
        private final ParcelFileDescriptor parcelFileDescriptor;

        InternalRewinder(ParcelFileDescriptor parcelFileDescriptor) {
            this.parcelFileDescriptor = parcelFileDescriptor;
        }

        ParcelFileDescriptor rewind() throws IOException {
            try {
                Os.lseek((FileDescriptor)this.parcelFileDescriptor.getFileDescriptor(), (long)0L, (int)OsConstants.SEEK_SET);
                return this.parcelFileDescriptor;
            }
            catch (ErrnoException errnoException) {
                throw new IOException(errnoException);
            }
        }
    }
}

