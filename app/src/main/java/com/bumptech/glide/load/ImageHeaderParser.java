/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ImageHeaderParser {
    public static final int UNKNOWN_ORIENTATION = -1;

    public int getOrientation(InputStream var1, ArrayPool var2) throws IOException;

    public int getOrientation(ByteBuffer var1, ArrayPool var2) throws IOException;

    public ImageType getType(InputStream var1) throws IOException;

    public ImageType getType(ByteBuffer var1) throws IOException;

    public static final class ImageType
    extends Enum<ImageType> {
        private static final ImageType[] $VALUES;
        public static final /* enum */ ImageType ANIMATED_WEBP;
        public static final /* enum */ ImageType AVIF;
        public static final /* enum */ ImageType GIF;
        public static final /* enum */ ImageType JPEG;
        public static final /* enum */ ImageType PNG;
        public static final /* enum */ ImageType PNG_A;
        public static final /* enum */ ImageType RAW;
        public static final /* enum */ ImageType UNKNOWN;
        public static final /* enum */ ImageType WEBP;
        public static final /* enum */ ImageType WEBP_A;
        private final boolean hasAlpha;

        static {
            ImageType imageType;
            ImageType imageType2;
            ImageType imageType3;
            ImageType imageType4;
            ImageType imageType5;
            ImageType imageType6;
            ImageType imageType7;
            ImageType imageType8;
            ImageType imageType9;
            ImageType imageType10;
            GIF = imageType10 = new ImageType(true);
            JPEG = imageType9 = new ImageType(false);
            RAW = imageType8 = new ImageType(false);
            PNG_A = imageType7 = new ImageType(true);
            PNG = imageType6 = new ImageType(false);
            WEBP_A = imageType5 = new ImageType(true);
            WEBP = imageType4 = new ImageType(false);
            ANIMATED_WEBP = imageType3 = new ImageType(true);
            AVIF = imageType2 = new ImageType(true);
            UNKNOWN = imageType = new ImageType(false);
            $VALUES = new ImageType[]{imageType10, imageType9, imageType8, imageType7, imageType6, imageType5, imageType4, imageType3, imageType2, imageType};
        }

        private ImageType(boolean bl) {
            this.hasAlpha = bl;
        }

        public static ImageType valueOf(String string2) {
            return Enum.valueOf(ImageType.class, string2);
        }

        public static ImageType[] values() {
            return (ImageType[])$VALUES.clone();
        }

        public boolean hasAlpha() {
            return this.hasAlpha;
        }

        public boolean isWebp() {
            switch (1.$SwitchMap$com$bumptech$glide$load$ImageHeaderParser$ImageType[this.ordinal()]) {
                default: {
                    return false;
                }
                case 1: 
                case 2: 
                case 3: 
            }
            return true;
        }
    }
}

