/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accessibilityservice.AccessibilityServiceInfo
 *  android.content.pm.PackageManager
 *  android.os.Build$VERSION
 */
package androidx.core.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public final class AccessibilityServiceInfoCompat {
    public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
    public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
    public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
    public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
    public static final int FEEDBACK_ALL_MASK = -1;
    public static final int FEEDBACK_BRAILLE = 32;
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
    public static final int FLAG_REPORT_VIEW_IDS = 16;
    public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
    public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
    public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;

    private AccessibilityServiceInfoCompat() {
    }

    public static String capabilityToString(int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 8: {
                return "CAPABILITY_CAN_FILTER_KEY_EVENTS";
            }
            case 4: {
                return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
            }
            case 2: {
                return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
            }
            case 1: 
        }
        return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
    }

    public static String feedbackTypeToString(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        block7: while (n > 0) {
            int n2 = 1 << Integer.numberOfTrailingZeros(n);
            n &= ~n2;
            if (stringBuilder.length() > 1) {
                stringBuilder.append(", ");
            }
            switch (n2) {
                default: {
                    continue block7;
                }
                case 16: {
                    stringBuilder.append("FEEDBACK_GENERIC");
                    continue block7;
                }
                case 8: {
                    stringBuilder.append("FEEDBACK_VISUAL");
                    continue block7;
                }
                case 4: {
                    stringBuilder.append("FEEDBACK_AUDIBLE");
                    continue block7;
                }
                case 2: {
                    stringBuilder.append("FEEDBACK_HAPTIC");
                    continue block7;
                }
                case 1: 
            }
            stringBuilder.append("FEEDBACK_SPOKEN");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static String flagToString(int n) {
        switch (n) {
            default: {
                return null;
            }
            case 32: {
                return "FLAG_REQUEST_FILTER_KEY_EVENTS";
            }
            case 16: {
                return "FLAG_REPORT_VIEW_IDS";
            }
            case 8: {
                return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
            }
            case 4: {
                return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
            }
            case 2: {
                return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
            }
            case 1: 
        }
        return "DEFAULT";
    }

    public static int getCapabilities(AccessibilityServiceInfo accessibilityServiceInfo) {
        if (Build.VERSION.SDK_INT >= 18) {
            return accessibilityServiceInfo.getCapabilities();
        }
        if (accessibilityServiceInfo.getCanRetrieveWindowContent()) {
            return 1;
        }
        return 0;
    }

    public static String loadDescription(AccessibilityServiceInfo accessibilityServiceInfo, PackageManager packageManager) {
        if (Build.VERSION.SDK_INT >= 16) {
            return accessibilityServiceInfo.loadDescription(packageManager);
        }
        return accessibilityServiceInfo.getDescription();
    }
}

