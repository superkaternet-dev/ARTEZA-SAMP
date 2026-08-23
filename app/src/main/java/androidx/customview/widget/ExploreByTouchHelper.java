/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityManager
 *  android.view.accessibility.AccessibilityRecord
 */
package androidx.customview.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityRecord;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import androidx.customview.widget.FocusStrategy;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper
extends AccessibilityDelegateCompat {
    private static final String DEFAULT_CLASS_NAME = "android.view.View";
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat> NODE_ADAPTER = new FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat>(){

        @Override
        public void obtainBounds(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, Rect rect) {
            accessibilityNodeInfoCompat.getBoundsInParent(rect);
        }
    };
    private static final FocusStrategy.CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> SPARSE_VALUES_ADAPTER = new FocusStrategy.CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat>(){

        @Override
        public AccessibilityNodeInfoCompat get(SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat, int n) {
            return sparseArrayCompat.valueAt(n);
        }

        @Override
        public int size(SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat) {
            return sparseArrayCompat.size();
        }
    };
    int mAccessibilityFocusedVirtualViewId;
    private final View mHost;
    private int mHoveredVirtualViewId;
    int mKeyboardFocusedVirtualViewId;
    private final AccessibilityManager mManager;
    private MyNodeProvider mNodeProvider;
    private final int[] mTempGlobalRect;
    private final Rect mTempParentRect;
    private final Rect mTempScreenRect = new Rect();
    private final Rect mTempVisibleRect;

    public ExploreByTouchHelper(View view) {
        this.mTempParentRect = new Rect();
        this.mTempVisibleRect = new Rect();
        this.mTempGlobalRect = new int[2];
        this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mHoveredVirtualViewId = Integer.MIN_VALUE;
        if (view != null) {
            this.mHost = view;
            this.mManager = (AccessibilityManager)view.getContext().getSystemService("accessibility");
            view.setFocusable(true);
            if (ViewCompat.getImportantForAccessibility(view) == 0) {
                ViewCompat.setImportantForAccessibility(view, 1);
            }
            return;
        }
        throw new IllegalArgumentException("View may not be null");
    }

    private boolean clearAccessibilityFocus(int n) {
        if (this.mAccessibilityFocusedVirtualViewId == n) {
            this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
            this.mHost.invalidate();
            this.sendEventForVirtualView(n, 65536);
            return true;
        }
        return false;
    }

    private boolean clickKeyboardFocusedVirtualView() {
        int n = this.mKeyboardFocusedVirtualViewId;
        boolean bl = n != Integer.MIN_VALUE && this.onPerformActionForVirtualView(n, 16, null);
        return bl;
    }

    private AccessibilityEvent createEvent(int n, int n2) {
        switch (n) {
            default: {
                return this.createEventForChild(n, n2);
            }
            case -1: 
        }
        return this.createEventForHost(n2);
    }

    private AccessibilityEvent createEventForChild(int n, int n2) {
        AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain((int)n2);
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = this.obtainAccessibilityNodeInfo(n);
        accessibilityEvent.getText().add(accessibilityNodeInfoCompat.getText());
        accessibilityEvent.setContentDescription(accessibilityNodeInfoCompat.getContentDescription());
        accessibilityEvent.setScrollable(accessibilityNodeInfoCompat.isScrollable());
        accessibilityEvent.setPassword(accessibilityNodeInfoCompat.isPassword());
        accessibilityEvent.setEnabled(accessibilityNodeInfoCompat.isEnabled());
        accessibilityEvent.setChecked(accessibilityNodeInfoCompat.isChecked());
        this.onPopulateEventForVirtualView(n, accessibilityEvent);
        if (accessibilityEvent.getText().isEmpty() && accessibilityEvent.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        }
        accessibilityEvent.setClassName(accessibilityNodeInfoCompat.getClassName());
        AccessibilityRecordCompat.setSource((AccessibilityRecord)accessibilityEvent, this.mHost, n);
        accessibilityEvent.setPackageName((CharSequence)this.mHost.getContext().getPackageName());
        return accessibilityEvent;
    }

    private AccessibilityEvent createEventForHost(int n) {
        AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain((int)n);
        this.mHost.onInitializeAccessibilityEvent(accessibilityEvent);
        return accessibilityEvent;
    }

    private AccessibilityNodeInfoCompat createNodeForChild(int n) {
        Object object = AccessibilityNodeInfoCompat.obtain();
        ((AccessibilityNodeInfoCompat)object).setEnabled(true);
        ((AccessibilityNodeInfoCompat)object).setFocusable(true);
        ((AccessibilityNodeInfoCompat)object).setClassName(DEFAULT_CLASS_NAME);
        Object object2 = INVALID_PARENT_BOUNDS;
        ((AccessibilityNodeInfoCompat)object).setBoundsInParent((Rect)object2);
        ((AccessibilityNodeInfoCompat)object).setBoundsInScreen((Rect)object2);
        ((AccessibilityNodeInfoCompat)object).setParent(this.mHost);
        this.onPopulateNodeForVirtualView(n, (AccessibilityNodeInfoCompat)object);
        if (((AccessibilityNodeInfoCompat)object).getText() == null && ((AccessibilityNodeInfoCompat)object).getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        }
        ((AccessibilityNodeInfoCompat)object).getBoundsInParent(this.mTempParentRect);
        if (!this.mTempParentRect.equals(object2)) {
            int n2 = ((AccessibilityNodeInfoCompat)object).getActions();
            if ((n2 & 0x40) == 0) {
                if ((n2 & 0x80) == 0) {
                    ((AccessibilityNodeInfoCompat)object).setPackageName(this.mHost.getContext().getPackageName());
                    ((AccessibilityNodeInfoCompat)object).setSource(this.mHost, n);
                    if (this.mAccessibilityFocusedVirtualViewId == n) {
                        ((AccessibilityNodeInfoCompat)object).setAccessibilityFocused(true);
                        ((AccessibilityNodeInfoCompat)object).addAction(128);
                    } else {
                        ((AccessibilityNodeInfoCompat)object).setAccessibilityFocused(false);
                        ((AccessibilityNodeInfoCompat)object).addAction(64);
                    }
                    boolean bl = this.mKeyboardFocusedVirtualViewId == n;
                    if (bl) {
                        ((AccessibilityNodeInfoCompat)object).addAction(2);
                    } else if (((AccessibilityNodeInfoCompat)object).isFocusable()) {
                        ((AccessibilityNodeInfoCompat)object).addAction(1);
                    }
                    ((AccessibilityNodeInfoCompat)object).setFocused(bl);
                    this.mHost.getLocationOnScreen(this.mTempGlobalRect);
                    ((AccessibilityNodeInfoCompat)object).getBoundsInScreen(this.mTempScreenRect);
                    if (this.mTempScreenRect.equals(object2)) {
                        ((AccessibilityNodeInfoCompat)object).getBoundsInParent(this.mTempScreenRect);
                        if (((AccessibilityNodeInfoCompat)object).mParentVirtualDescendantId != -1) {
                            object2 = AccessibilityNodeInfoCompat.obtain();
                            n = ((AccessibilityNodeInfoCompat)object).mParentVirtualDescendantId;
                            while (n != -1) {
                                ((AccessibilityNodeInfoCompat)object2).setParent(this.mHost, -1);
                                ((AccessibilityNodeInfoCompat)object2).setBoundsInParent(INVALID_PARENT_BOUNDS);
                                this.onPopulateNodeForVirtualView(n, (AccessibilityNodeInfoCompat)object2);
                                ((AccessibilityNodeInfoCompat)object2).getBoundsInParent(this.mTempParentRect);
                                this.mTempScreenRect.offset(this.mTempParentRect.left, this.mTempParentRect.top);
                                n = ((AccessibilityNodeInfoCompat)object2).mParentVirtualDescendantId;
                            }
                            ((AccessibilityNodeInfoCompat)object2).recycle();
                        }
                        this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                    }
                    if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect)) {
                        this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                        if (this.mTempScreenRect.intersect(this.mTempVisibleRect)) {
                            ((AccessibilityNodeInfoCompat)object).setBoundsInScreen(this.mTempScreenRect);
                            if (this.isVisibleToUser(this.mTempScreenRect)) {
                                ((AccessibilityNodeInfoCompat)object).setVisibleToUser(true);
                            }
                        }
                    }
                    return object;
                }
                throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            }
            throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        }
        object = new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
        throw object;
    }

    private AccessibilityNodeInfoCompat createNodeForHost() {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(this.mHost);
        ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, accessibilityNodeInfoCompat);
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        this.getVisibleVirtualViews(arrayList);
        if (accessibilityNodeInfoCompat.getChildCount() > 0 && arrayList.size() > 0) {
            throw new RuntimeException("Views cannot have both real and virtual children");
        }
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            accessibilityNodeInfoCompat.addChild(this.mHost, arrayList.get(i));
        }
        return accessibilityNodeInfoCompat;
    }

    private SparseArrayCompat<AccessibilityNodeInfoCompat> getAllNodes() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        this.getVisibleVirtualViews(arrayList);
        SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat = new SparseArrayCompat<AccessibilityNodeInfoCompat>();
        for (int i = 0; i < arrayList.size(); ++i) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = this.createNodeForChild((Integer)arrayList.get(i));
            sparseArrayCompat.put((Integer)arrayList.get(i), accessibilityNodeInfoCompat);
        }
        return sparseArrayCompat;
    }

    private void getBoundsInParent(int n, Rect rect) {
        this.obtainAccessibilityNodeInfo(n).getBoundsInParent(rect);
    }

    private static Rect guessPreviouslyFocusedRect(View view, int n, Rect rect) {
        int n2 = view.getWidth();
        int n3 = view.getHeight();
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 130: {
                rect.set(0, -1, n2, -1);
                break;
            }
            case 66: {
                rect.set(-1, 0, -1, n3);
                break;
            }
            case 33: {
                rect.set(0, n3, n2, n3);
                break;
            }
            case 17: {
                rect.set(n2, 0, n2, n3);
            }
        }
        return rect;
    }

    private boolean isVisibleToUser(Rect rect) {
        boolean bl = false;
        if (rect != null && !rect.isEmpty()) {
            if (this.mHost.getWindowVisibility() != 0) {
                return false;
            }
            rect = this.mHost.getParent();
            while (rect instanceof View) {
                if (!((rect = (View)rect).getAlpha() <= 0.0f) && rect.getVisibility() == 0) {
                    rect = rect.getParent();
                    continue;
                }
                return false;
            }
            if (rect != null) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    private static int keyToDirection(int n) {
        switch (n) {
            default: {
                return 130;
            }
            case 22: {
                return 66;
            }
            case 21: {
                return 17;
            }
            case 19: 
        }
        return 33;
    }

    private boolean moveFocus(int n, Rect object) {
        SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat = this.getAllNodes();
        int n2 = this.mKeyboardFocusedVirtualViewId;
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = n2 == Integer.MIN_VALUE ? null : sparseArrayCompat.get(n2);
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 17: 
            case 33: 
            case 66: 
            case 130: {
                Rect rect = new Rect();
                n2 = this.mKeyboardFocusedVirtualViewId;
                if (n2 != Integer.MIN_VALUE) {
                    this.getBoundsInParent(n2, rect);
                } else if (object != null) {
                    rect.set(object);
                } else {
                    ExploreByTouchHelper.guessPreviouslyFocusedRect(this.mHost, n, rect);
                }
                object = FocusStrategy.findNextFocusInAbsoluteDirection(sparseArrayCompat, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, accessibilityNodeInfoCompat, rect, n);
                break;
            }
            case 1: 
            case 2: {
                boolean bl = ViewCompat.getLayoutDirection(this.mHost) == 1;
                object = FocusStrategy.findNextFocusInRelativeDirection(sparseArrayCompat, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, accessibilityNodeInfoCompat, n, bl, false);
            }
        }
        n = object == null ? Integer.MIN_VALUE : sparseArrayCompat.keyAt(sparseArrayCompat.indexOfValue((AccessibilityNodeInfoCompat)object));
        return this.requestKeyboardFocusForVirtualView(n);
    }

    private boolean performActionForChild(int n, int n2, Bundle bundle) {
        switch (n2) {
            default: {
                return this.onPerformActionForVirtualView(n, n2, bundle);
            }
            case 128: {
                return this.clearAccessibilityFocus(n);
            }
            case 64: {
                return this.requestAccessibilityFocus(n);
            }
            case 2: {
                return this.clearKeyboardFocusForVirtualView(n);
            }
            case 1: 
        }
        return this.requestKeyboardFocusForVirtualView(n);
    }

    private boolean performActionForHost(int n, Bundle bundle) {
        return ViewCompat.performAccessibilityAction(this.mHost, n, bundle);
    }

    private boolean requestAccessibilityFocus(int n) {
        if (this.mManager.isEnabled() && this.mManager.isTouchExplorationEnabled()) {
            int n2 = this.mAccessibilityFocusedVirtualViewId;
            if (n2 != n) {
                if (n2 != Integer.MIN_VALUE) {
                    this.clearAccessibilityFocus(n2);
                }
                this.mAccessibilityFocusedVirtualViewId = n;
                this.mHost.invalidate();
                this.sendEventForVirtualView(n, 32768);
                return true;
            }
            return false;
        }
        return false;
    }

    private void updateHoveredVirtualView(int n) {
        if (this.mHoveredVirtualViewId == n) {
            return;
        }
        int n2 = this.mHoveredVirtualViewId;
        this.mHoveredVirtualViewId = n;
        this.sendEventForVirtualView(n, 128);
        this.sendEventForVirtualView(n2, 256);
    }

    public final boolean clearKeyboardFocusForVirtualView(int n) {
        if (this.mKeyboardFocusedVirtualViewId != n) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        this.onVirtualViewKeyboardFocusChanged(n, false);
        this.sendEventForVirtualView(n, 8);
        return true;
    }

    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        boolean bl = this.mManager.isEnabled();
        boolean bl2 = false;
        if (bl && this.mManager.isTouchExplorationEnabled()) {
            switch (motionEvent.getAction()) {
                default: {
                    return false;
                }
                case 10: {
                    if (this.mHoveredVirtualViewId != Integer.MIN_VALUE) {
                        this.updateHoveredVirtualView(Integer.MIN_VALUE);
                        return true;
                    }
                    return false;
                }
                case 7: 
                case 9: 
            }
            int n = this.getVirtualViewAt(motionEvent.getX(), motionEvent.getY());
            this.updateHoveredVirtualView(n);
            if (n != Integer.MIN_VALUE) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = bl;
        if (keyEvent.getAction() != 1) {
            int n = keyEvent.getKeyCode();
            block0 : switch (n) {
                default: {
                    bl3 = bl;
                    break;
                }
                case 61: {
                    if (keyEvent.hasNoModifiers()) {
                        bl3 = this.moveFocus(2, null);
                        break;
                    }
                    bl3 = bl;
                    if (!keyEvent.hasModifiers(1)) break;
                    bl3 = this.moveFocus(1, null);
                    break;
                }
                case 23: 
                case 66: {
                    bl3 = bl;
                    if (!keyEvent.hasNoModifiers()) break;
                    bl3 = bl;
                    if (keyEvent.getRepeatCount() != 0) break;
                    this.clickKeyboardFocusedVirtualView();
                    bl3 = true;
                    break;
                }
                case 19: 
                case 20: 
                case 21: 
                case 22: {
                    bl3 = bl;
                    if (!keyEvent.hasNoModifiers()) break;
                    int n2 = ExploreByTouchHelper.keyToDirection(n);
                    int n3 = keyEvent.getRepeatCount();
                    n = 0;
                    while (true) {
                        bl3 = bl2;
                        if (n >= n3 + 1) break block0;
                        bl3 = bl2;
                        if (!this.moveFocus(n2, null)) break block0;
                        bl2 = true;
                        ++n;
                    }
                }
            }
        }
        return bl3;
    }

    public final int getAccessibilityFocusedVirtualViewId() {
        return this.mAccessibilityFocusedVirtualViewId;
    }

    @Override
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        if (this.mNodeProvider == null) {
            this.mNodeProvider = new MyNodeProvider(this);
        }
        return this.mNodeProvider;
    }

    @Deprecated
    public int getFocusedVirtualView() {
        return this.getAccessibilityFocusedVirtualViewId();
    }

    public final int getKeyboardFocusedVirtualViewId() {
        return this.mKeyboardFocusedVirtualViewId;
    }

    protected abstract int getVirtualViewAt(float var1, float var2);

    protected abstract void getVisibleVirtualViews(List<Integer> var1);

    public final void invalidateRoot() {
        this.invalidateVirtualView(-1, 1);
    }

    public final void invalidateVirtualView(int n) {
        this.invalidateVirtualView(n, 0);
    }

    public final void invalidateVirtualView(int n, int n2) {
        ViewParent viewParent;
        if (n != Integer.MIN_VALUE && this.mManager.isEnabled() && (viewParent = this.mHost.getParent()) != null) {
            AccessibilityEvent accessibilityEvent = this.createEvent(n, 2048);
            AccessibilityEventCompat.setContentChangeTypes(accessibilityEvent, n2);
            viewParent.requestSendAccessibilityEvent(this.mHost, accessibilityEvent);
        }
    }

    AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int n) {
        if (n == -1) {
            return this.createNodeForHost();
        }
        return this.createNodeForChild(n);
    }

    public final void onFocusChanged(boolean bl, int n, Rect rect) {
        int n2 = this.mKeyboardFocusedVirtualViewId;
        if (n2 != Integer.MIN_VALUE) {
            this.clearKeyboardFocusForVirtualView(n2);
        }
        if (bl) {
            this.moveFocus(n, rect);
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(view, accessibilityEvent);
        this.onPopulateEventForHost(accessibilityEvent);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        this.onPopulateNodeForHost(accessibilityNodeInfoCompat);
    }

    protected abstract boolean onPerformActionForVirtualView(int var1, int var2, Bundle var3);

    protected void onPopulateEventForHost(AccessibilityEvent accessibilityEvent) {
    }

    protected void onPopulateEventForVirtualView(int n, AccessibilityEvent accessibilityEvent) {
    }

    protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
    }

    protected abstract void onPopulateNodeForVirtualView(int var1, AccessibilityNodeInfoCompat var2);

    protected void onVirtualViewKeyboardFocusChanged(int n, boolean bl) {
    }

    boolean performAction(int n, int n2, Bundle bundle) {
        switch (n) {
            default: {
                return this.performActionForChild(n, n2, bundle);
            }
            case -1: 
        }
        return this.performActionForHost(n2, bundle);
    }

    public final boolean requestKeyboardFocusForVirtualView(int n) {
        if (!this.mHost.isFocused() && !this.mHost.requestFocus()) {
            return false;
        }
        int n2 = this.mKeyboardFocusedVirtualViewId;
        if (n2 == n) {
            return false;
        }
        if (n2 != Integer.MIN_VALUE) {
            this.clearKeyboardFocusForVirtualView(n2);
        }
        if (n == Integer.MIN_VALUE) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = n;
        this.onVirtualViewKeyboardFocusChanged(n, true);
        this.sendEventForVirtualView(n, 8);
        return true;
    }

    public final boolean sendEventForVirtualView(int n, int n2) {
        if (n != Integer.MIN_VALUE && this.mManager.isEnabled()) {
            ViewParent viewParent = this.mHost.getParent();
            if (viewParent == null) {
                return false;
            }
            AccessibilityEvent accessibilityEvent = this.createEvent(n, n2);
            return viewParent.requestSendAccessibilityEvent(this.mHost, accessibilityEvent);
        }
        return false;
    }

    private class MyNodeProvider
    extends AccessibilityNodeProviderCompat {
        final ExploreByTouchHelper this$0;

        MyNodeProvider(ExploreByTouchHelper exploreByTouchHelper) {
            this.this$0 = exploreByTouchHelper;
        }

        @Override
        public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int n) {
            return AccessibilityNodeInfoCompat.obtain(this.this$0.obtainAccessibilityNodeInfo(n));
        }

        @Override
        public AccessibilityNodeInfoCompat findFocus(int n) {
            if ((n = n == 2 ? this.this$0.mAccessibilityFocusedVirtualViewId : this.this$0.mKeyboardFocusedVirtualViewId) == Integer.MIN_VALUE) {
                return null;
            }
            return this.createAccessibilityNodeInfo(n);
        }

        @Override
        public boolean performAction(int n, int n2, Bundle bundle) {
            return this.this$0.performAction(n, n2, bundle);
        }
    }
}

