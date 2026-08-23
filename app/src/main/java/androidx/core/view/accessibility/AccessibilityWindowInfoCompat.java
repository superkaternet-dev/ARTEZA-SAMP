/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Build$VERSION
 *  android.view.accessibility.AccessibilityWindowInfo
 */
package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.os.Build;
import android.view.accessibility.AccessibilityWindowInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

public class AccessibilityWindowInfoCompat {
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private Object mInfo;

    private AccessibilityWindowInfoCompat(Object object) {
        this.mInfo = object;
    }

    public static AccessibilityWindowInfoCompat obtain() {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance(AccessibilityWindowInfo.obtain());
        }
        return null;
    }

    public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat accessibilityWindowInfoCompat) {
        int n = Build.VERSION.SDK_INT;
        Object var2_2 = null;
        if (n >= 21) {
            accessibilityWindowInfoCompat = accessibilityWindowInfoCompat == null ? var2_2 : AccessibilityWindowInfoCompat.wrapNonNullInstance(AccessibilityWindowInfo.obtain((AccessibilityWindowInfo)((AccessibilityWindowInfo)accessibilityWindowInfoCompat.mInfo)));
            return accessibilityWindowInfoCompat;
        }
        return null;
    }

    private static String typeToString(int n) {
        switch (n) {
            default: {
                return "<UNKNOWN>";
            }
            case 4: {
                return "TYPE_ACCESSIBILITY_OVERLAY";
            }
            case 3: {
                return "TYPE_SYSTEM";
            }
            case 2: {
                return "TYPE_INPUT_METHOD";
            }
            case 1: 
        }
        return "TYPE_APPLICATION";
    }

    static AccessibilityWindowInfoCompat wrapNonNullInstance(Object object) {
        if (object != null) {
            return new AccessibilityWindowInfoCompat(object);
        }
        return null;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof AccessibilityWindowInfoCompat)) {
            return false;
        }
        object = (AccessibilityWindowInfoCompat)object;
        Object object2 = this.mInfo;
        return !(object2 == null ? ((AccessibilityWindowInfoCompat)object).mInfo != null : !object2.equals(((AccessibilityWindowInfoCompat)object).mInfo));
    }

    public AccessibilityNodeInfoCompat getAnchor() {
        if (Build.VERSION.SDK_INT >= 24) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getAnchor());
        }
        return null;
    }

    public void getBoundsInScreen(Rect rect) {
        if (Build.VERSION.SDK_INT >= 21) {
            ((AccessibilityWindowInfo)this.mInfo).getBoundsInScreen(rect);
        }
    }

    public AccessibilityWindowInfoCompat getChild(int n) {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getChild(n));
        }
        return null;
    }

    public int getChildCount() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getChildCount();
        }
        return 0;
    }

    public int getId() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getId();
        }
        return -1;
    }

    public int getLayer() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getLayer();
        }
        return -1;
    }

    public AccessibilityWindowInfoCompat getParent() {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getParent());
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getRoot() {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getRoot());
        }
        return null;
    }

    public CharSequence getTitle() {
        if (Build.VERSION.SDK_INT >= 24) {
            return ((AccessibilityWindowInfo)this.mInfo).getTitle();
        }
        return null;
    }

    public int getType() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getType();
        }
        return -1;
    }

    public int hashCode() {
        Object object = this.mInfo;
        int n = object == null ? 0 : object.hashCode();
        return n;
    }

    public boolean isAccessibilityFocused() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).isAccessibilityFocused();
        }
        return true;
    }

    public boolean isActive() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).isActive();
        }
        return true;
    }

    public boolean isFocused() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).isFocused();
        }
        return true;
    }

    public void recycle() {
        if (Build.VERSION.SDK_INT >= 21) {
            ((AccessibilityWindowInfo)this.mInfo).recycle();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Object object = new Rect();
        this.getBoundsInScreen((Rect)object);
        stringBuilder.append("AccessibilityWindowInfo[");
        stringBuilder.append("id=");
        stringBuilder.append(this.getId());
        stringBuilder.append(", type=");
        stringBuilder.append(AccessibilityWindowInfoCompat.typeToString(this.getType()));
        stringBuilder.append(", layer=");
        stringBuilder.append(this.getLayer());
        stringBuilder.append(", bounds=");
        stringBuilder.append(object);
        stringBuilder.append(", focused=");
        stringBuilder.append(this.isFocused());
        stringBuilder.append(", active=");
        stringBuilder.append(this.isActive());
        stringBuilder.append(", hasParent=");
        object = this.getParent();
        boolean bl = true;
        boolean bl2 = object != null;
        stringBuilder.append(bl2);
        stringBuilder.append(", hasChildren=");
        bl2 = this.getChildCount() > 0 ? bl : false;
        stringBuilder.append(bl2);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

