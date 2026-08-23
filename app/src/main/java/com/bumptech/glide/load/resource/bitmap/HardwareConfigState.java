/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory$Options
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public final class HardwareConfigState {
    public static final boolean BLOCK_HARDWARE_BITMAPS_WHEN_GL_CONTEXT_MIGHT_NOT_BE_INITIALIZED;
    private static final File FD_SIZE_LIST;
    public static final boolean HARDWARE_BITMAPS_SUPPORTED;
    private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_O = 700;
    private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_P = 20000;
    private static final int MINIMUM_DECODES_BETWEEN_FD_CHECKS = 50;
    static final int MIN_HARDWARE_DIMENSION_O = 128;
    private static final int MIN_HARDWARE_DIMENSION_P = 0;
    public static final int NO_MAX_FD_COUNT = -1;
    private static final String TAG = "HardwareConfig";
    private static volatile HardwareConfigState instance;
    private static volatile int manualOverrideMaxFdCount;
    private int decodesSinceLastFdCheck;
    private boolean isFdSizeBelowHardwareLimit = true;
    private final AtomicBoolean isHardwareConfigAllowedByAppState = new AtomicBoolean(false);
    private final boolean isHardwareConfigAllowedByDeviceModel = HardwareConfigState.isHardwareConfigAllowedByDeviceModel();
    private final int minHardwareDimension;
    private final int sdkBasedMaxFdCount;

    static {
        int n = Build.VERSION.SDK_INT;
        boolean bl = true;
        boolean bl2 = n < 29;
        BLOCK_HARDWARE_BITMAPS_WHEN_GL_CONTEXT_MIGHT_NOT_BE_INITIALIZED = bl2;
        bl2 = Build.VERSION.SDK_INT >= 26 ? bl : false;
        HARDWARE_BITMAPS_SUPPORTED = bl2;
        FD_SIZE_LIST = new File("/proc/self/fd");
        manualOverrideMaxFdCount = -1;
    }

    HardwareConfigState() {
        if (Build.VERSION.SDK_INT >= 28) {
            this.sdkBasedMaxFdCount = 20000;
            this.minHardwareDimension = 0;
        } else {
            this.sdkBasedMaxFdCount = 700;
            this.minHardwareDimension = 128;
        }
    }

    private boolean areHardwareBitmapsBlockedByAppState() {
        boolean bl = BLOCK_HARDWARE_BITMAPS_WHEN_GL_CONTEXT_MIGHT_NOT_BE_INITIALIZED && !this.isHardwareConfigAllowedByAppState.get();
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static HardwareConfigState getInstance() {
        if (instance != null) return instance;
        synchronized (HardwareConfigState.class) {
            HardwareConfigState hardwareConfigState;
            if (instance != null) return instance;
            instance = hardwareConfigState = new HardwareConfigState();
            return instance;
        }
    }

    private int getMaxFdCount() {
        int n = manualOverrideMaxFdCount != -1 ? manualOverrideMaxFdCount : this.sdkBasedMaxFdCount;
        return n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isFdSizeBelowHardwareLimit() {
        synchronized (this) {
            int n = this.decodesSinceLastFdCheck;
            boolean bl = true;
            ++n;
            this.decodesSinceLastFdCheck = n;
            if (n < 50) return this.isFdSizeBelowHardwareLimit;
            this.decodesSinceLastFdCheck = 0;
            n = FD_SIZE_LIST.list().length;
            long l = this.getMaxFdCount();
            if ((long)n >= l) {
                bl = false;
            }
            this.isFdSizeBelowHardwareLimit = bl;
            if (bl) return this.isFdSizeBelowHardwareLimit;
            if (!Log.isLoggable((String)"Downsampler", (int)5)) return this.isFdSizeBelowHardwareLimit;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors ");
            stringBuilder.append(n);
            stringBuilder.append(", limit ");
            stringBuilder.append(l);
            Log.w((String)"Downsampler", (String)stringBuilder.toString());
            return this.isFdSizeBelowHardwareLimit;
        }
    }

    private static boolean isHardwareConfigAllowedByDeviceModel() {
        boolean bl = !HardwareConfigState.isHardwareConfigDisallowedByB112551574() && !HardwareConfigState.isHardwareConfigDisallowedByB147430447();
        return bl;
    }

    private static boolean isHardwareConfigDisallowedByB112551574() {
        if (Build.VERSION.SDK_INT != 26) {
            return false;
        }
        for (String string2 : Arrays.asList("SC-04J", "SM-N935", "SM-J720", "SM-G570F", "SM-G570M", "SM-G960", "SM-G965", "SM-G935", "SM-G930", "SM-A520", "SM-A720F", "moto e5", "moto e5 play", "moto e5 plus", "moto e5 cruise", "moto g(6) forge", "moto g(6) play")) {
            if (!Build.MODEL.startsWith(string2)) continue;
            return true;
        }
        return false;
    }

    private static boolean isHardwareConfigDisallowedByB147430447() {
        if (Build.VERSION.SDK_INT != 27) {
            return false;
        }
        return Arrays.asList("LG-M250", "LG-M320", "LG-Q710AL", "LG-Q710PL", "LGM-K121K", "LGM-K121L", "LGM-K121S", "LGM-X320K", "LGM-X320L", "LGM-X320S", "LGM-X401L", "LGM-X401S", "LM-Q610.FG", "LM-Q610.FGN", "LM-Q617.FG", "LM-Q617.FGN", "LM-Q710.FG", "LM-Q710.FGN", "LM-X220PM", "LM-X220QMA", "LM-X410PM").contains(Build.MODEL);
    }

    public boolean areHardwareBitmapsBlocked() {
        Util.assertMainThread();
        return this.isHardwareConfigAllowedByAppState.get() ^ true;
    }

    public void blockHardwareBitmaps() {
        Util.assertMainThread();
        this.isHardwareConfigAllowedByAppState.set(false);
    }

    public boolean isHardwareConfigAllowed(int n, int n2, boolean bl, boolean bl2) {
        if (!bl) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"Hardware config disallowed by caller");
            }
            return false;
        }
        if (!this.isHardwareConfigAllowedByDeviceModel) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"Hardware config disallowed by device model");
            }
            return false;
        }
        if (!HARDWARE_BITMAPS_SUPPORTED) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"Hardware config disallowed by sdk");
            }
            return false;
        }
        if (this.areHardwareBitmapsBlockedByAppState()) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"Hardware config disallowed by app state");
            }
            return false;
        }
        if (bl2) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"Hardware config disallowed because exif orientation is required");
            }
            return false;
        }
        int n3 = this.minHardwareDimension;
        if (n < n3) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"Hardware config disallowed because width is too small");
            }
            return false;
        }
        if (n2 < n3) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"Hardware config disallowed because height is too small");
            }
            return false;
        }
        if (!this.isFdSizeBelowHardwareLimit()) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"Hardware config disallowed because there are insufficient FDs");
            }
            return false;
        }
        return true;
    }

    boolean setHardwareConfigIfAllowed(int n, int n2, BitmapFactory.Options options, boolean bl, boolean bl2) {
        if (bl = this.isHardwareConfigAllowed(n, n2, bl, bl2)) {
            options.inPreferredConfig = Bitmap.Config.HARDWARE;
            options.inMutable = false;
        }
        return bl;
    }

    public void unblockHardwareBitmaps() {
        Util.assertMainThread();
        this.isHardwareConfigAllowedByAppState.set(true);
    }
}

