/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.UiModeManager
 *  android.content.Context
 *  android.graphics.Point
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 *  android.view.Display
 *  android.view.Display$Mode
 */
package androidx.core.view;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import androidx.core.util.Preconditions;
import java.util.ArrayList;

public final class DisplayCompat {
    private static final int DISPLAY_SIZE_4K_HEIGHT = 2160;
    private static final int DISPLAY_SIZE_4K_WIDTH = 3840;

    private DisplayCompat() {
    }

    private static Point getPhysicalDisplaySize(Context context, Display display) {
        Point point = Build.VERSION.SDK_INT < 28 ? DisplayCompat.parsePhysicalDisplaySizeFromSystemProperties("sys.display-size", display) : DisplayCompat.parsePhysicalDisplaySizeFromSystemProperties("vendor.display-size", display);
        if (point != null) {
            return point;
        }
        if (DisplayCompat.isSonyBravia4kTv(context)) {
            return new Point(3840, 2160);
        }
        context = new Point();
        if (Build.VERSION.SDK_INT >= 23) {
            display = display.getMode();
            context.x = display.getPhysicalWidth();
            context.y = display.getPhysicalHeight();
        } else if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize((Point)context);
        } else {
            display.getSize((Point)context);
        }
        return context;
    }

    public static ModeCompat[] getSupportedModes(Context context, Display modeArray) {
        context = DisplayCompat.getPhysicalDisplaySize(context, (Display)modeArray);
        if (Build.VERSION.SDK_INT >= 23) {
            modeArray = modeArray.getSupportedModes();
            ArrayList<ModeCompat> arrayList = new ArrayList<ModeCompat>(modeArray.length);
            boolean bl = false;
            for (int i = 0; i < modeArray.length; ++i) {
                if (DisplayCompat.physicalSizeEquals(modeArray[i], (Point)context)) {
                    arrayList.add(i, new ModeCompat(modeArray[i], true));
                    bl = true;
                    continue;
                }
                arrayList.add(i, new ModeCompat(modeArray[i], false));
            }
            if (!bl) {
                arrayList.add(new ModeCompat((Point)context));
            }
            return arrayList.toArray(new ModeCompat[0]);
        }
        return new ModeCompat[]{new ModeCompat((Point)context)};
    }

    private static String getSystemProperty(String string2) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            string2 = (String)clazz.getMethod("get", String.class).invoke(clazz, string2);
            return string2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static boolean isSonyBravia4kTv(Context context) {
        boolean bl = DisplayCompat.isTv(context) && "Sony".equals(Build.MANUFACTURER) && Build.MODEL.startsWith("BRAVIA") && context.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd");
        return bl;
    }

    private static boolean isTv(Context context) {
        boolean bl = (context = (UiModeManager)context.getSystemService("uimode")) != null && context.getCurrentModeType() == 4;
        return bl;
    }

    private static Point parseDisplaySize(String stringArray) throws NumberFormatException {
        if ((stringArray = stringArray.trim().split("x", -1)).length == 2) {
            int n = Integer.parseInt(stringArray[0]);
            int n2 = Integer.parseInt(stringArray[1]);
            if (n > 0 && n2 > 0) {
                return new Point(n, n2);
            }
        }
        throw new NumberFormatException();
    }

    private static Point parsePhysicalDisplaySizeFromSystemProperties(String string2, Display display) {
        if (display.getDisplayId() == 0 && !TextUtils.isEmpty((CharSequence)(string2 = DisplayCompat.getSystemProperty(string2)))) {
            try {
                string2 = DisplayCompat.parseDisplaySize(string2);
                return string2;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return null;
    }

    private static boolean physicalSizeEquals(Display.Mode mode, Point point) {
        boolean bl = mode.getPhysicalWidth() == point.x && mode.getPhysicalHeight() == point.y || mode.getPhysicalWidth() == point.y && mode.getPhysicalHeight() == point.x;
        return bl;
    }

    public static final class ModeCompat {
        private final boolean mIsNative;
        private final Display.Mode mMode;
        private final Point mPhysicalDisplaySize;

        ModeCompat(Point point) {
            Preconditions.checkNotNull(point, "physicalDisplaySize == null");
            this.mIsNative = true;
            this.mPhysicalDisplaySize = point;
            this.mMode = null;
        }

        ModeCompat(Display.Mode mode, boolean bl) {
            Preconditions.checkNotNull(mode, "Display.Mode == null, can't wrap a null reference");
            this.mIsNative = bl;
            this.mPhysicalDisplaySize = new Point(mode.getPhysicalWidth(), mode.getPhysicalHeight());
            this.mMode = mode;
        }

        public int getPhysicalHeight() {
            return this.mPhysicalDisplaySize.y;
        }

        public int getPhysicalWidth() {
            return this.mPhysicalDisplaySize.x;
        }

        public boolean isNative() {
            return this.mIsNative;
        }

        public Display.Mode toMode() {
            return this.mMode;
        }
    }
}

