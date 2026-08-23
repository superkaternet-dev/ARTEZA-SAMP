/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.widget.ListView
 */
package androidx.core.widget;

import android.os.Build;
import android.view.View;
import android.widget.ListView;

public final class ListViewCompat {
    private ListViewCompat() {
    }

    public static boolean canScrollList(ListView listView, int n) {
        int n2;
        boolean bl;
        block6: {
            block8: {
                block7: {
                    if (Build.VERSION.SDK_INT >= 19) {
                        return listView.canScrollList(n);
                    }
                    int n3 = listView.getChildCount();
                    bl = false;
                    boolean bl2 = false;
                    if (n3 == 0) {
                        return false;
                    }
                    n2 = listView.getFirstVisiblePosition();
                    if (n <= 0) break block6;
                    n = listView.getChildAt(n3 - 1).getBottom();
                    if (n2 + n3 < listView.getCount()) break block7;
                    bl = bl2;
                    if (n <= listView.getHeight() - listView.getListPaddingBottom()) break block8;
                }
                bl = true;
            }
            return bl;
        }
        n = listView.getChildAt(0).getTop();
        if (n2 > 0 || n < listView.getListPaddingTop()) {
            bl = true;
        }
        return bl;
    }

    public static void scrollListBy(ListView listView, int n) {
        if (Build.VERSION.SDK_INT >= 19) {
            listView.scrollListBy(n);
        } else {
            int n2 = listView.getFirstVisiblePosition();
            if (n2 == -1) {
                return;
            }
            View view = listView.getChildAt(0);
            if (view == null) {
                return;
            }
            listView.setSelectionFromTop(n2, view.getTop() - n);
        }
    }
}

