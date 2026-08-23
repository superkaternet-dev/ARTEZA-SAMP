/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.graphics.Region
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.text.SpannableString
 *  android.text.Spanned
 *  android.text.TextUtils
 *  android.text.style.ClickableSpan
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionInfo
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo
 *  android.view.accessibility.AccessibilityNodeInfo$RangeInfo
 *  android.view.accessibility.AccessibilityNodeInfo$TouchDelegateInfo
 */
package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.R;
import androidx.core.view.accessibility.AccessibilityClickableSpanCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.core.view.accessibility.AccessibilityWindowInfoCompat;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AccessibilityNodeInfoCompat {
    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";
    public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_X = "ACTION_ARGUMENT_MOVE_WINDOW_X";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_Y = "ACTION_ARGUMENT_MOVE_WINDOW_Y";
    public static final String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";
    public static final String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";
    public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
    public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
    public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_COLLAPSE = 524288;
    public static final int ACTION_COPY = 16384;
    public static final int ACTION_CUT = 65536;
    public static final int ACTION_DISMISS = 0x100000;
    public static final int ACTION_EXPAND = 262144;
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PASTE = 32768;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_SET_SELECTION = 131072;
    public static final int ACTION_SET_TEXT = 0x200000;
    private static final int BOOLEAN_PROPERTY_IS_HEADING = 2;
    private static final int BOOLEAN_PROPERTY_IS_SHOWING_HINT = 4;
    private static final int BOOLEAN_PROPERTY_IS_TEXT_ENTRY_KEY = 8;
    private static final String BOOLEAN_PROPERTY_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY";
    private static final int BOOLEAN_PROPERTY_SCREEN_READER_FOCUSABLE = 1;
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int FOCUS_INPUT = 1;
    private static final String HINT_TEXT_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY";
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    private static final String PANE_TITLE_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY";
    private static final String ROLE_DESCRIPTION_KEY = "AccessibilityNodeInfo.roleDescription";
    private static final String SPANS_ACTION_ID_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY";
    private static final String SPANS_END_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY";
    private static final String SPANS_FLAGS_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY";
    private static final String SPANS_ID_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY";
    private static final String SPANS_START_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY";
    private static final String TOOLTIP_TEXT_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.TOOLTIP_TEXT_KEY";
    private static int sClickableSpanId = 0;
    private final AccessibilityNodeInfo mInfo;
    public int mParentVirtualDescendantId = -1;
    private int mVirtualDescendantId = -1;

    private AccessibilityNodeInfoCompat(AccessibilityNodeInfo accessibilityNodeInfo) {
        this.mInfo = accessibilityNodeInfo;
    }

    @Deprecated
    public AccessibilityNodeInfoCompat(Object object) {
        this.mInfo = (AccessibilityNodeInfo)object;
    }

    private void addSpanLocationToExtras(ClickableSpan clickableSpan, Spanned spanned, int n) {
        this.extrasIntList(SPANS_START_KEY).add(spanned.getSpanStart((Object)clickableSpan));
        this.extrasIntList(SPANS_END_KEY).add(spanned.getSpanEnd((Object)clickableSpan));
        this.extrasIntList(SPANS_FLAGS_KEY).add(spanned.getSpanFlags((Object)clickableSpan));
        this.extrasIntList(SPANS_ID_KEY).add(n);
    }

    private void clearExtrasSpans() {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().remove(SPANS_START_KEY);
            this.mInfo.getExtras().remove(SPANS_END_KEY);
            this.mInfo.getExtras().remove(SPANS_FLAGS_KEY);
            this.mInfo.getExtras().remove(SPANS_ID_KEY);
        }
    }

    private List<Integer> extrasIntList(String string2) {
        ArrayList arrayList;
        if (Build.VERSION.SDK_INT < 19) {
            return new ArrayList<Integer>();
        }
        ArrayList arrayList2 = arrayList = this.mInfo.getExtras().getIntegerArrayList(string2);
        if (arrayList == null) {
            arrayList2 = new ArrayList();
            this.mInfo.getExtras().putIntegerArrayList(string2, arrayList2);
        }
        return arrayList2;
    }

    private static String getActionSymbolicName(int n) {
        switch (n) {
            default: {
                return "ACTION_UNKNOWN";
            }
            case 16908361: {
                return "ACTION_PAGE_RIGHT";
            }
            case 16908360: {
                return "ACTION_PAGE_LEFT";
            }
            case 16908359: {
                return "ACTION_PAGE_DOWN";
            }
            case 16908358: {
                return "ACTION_PAGE_UP";
            }
            case 16908357: {
                return "ACTION_HIDE_TOOLTIP";
            }
            case 16908356: {
                return "ACTION_SHOW_TOOLTIP";
            }
            case 16908354: {
                return "ACTION_MOVE_WINDOW";
            }
            case 16908349: {
                return "ACTION_SET_PROGRESS";
            }
            case 16908348: {
                return "ACTION_CONTEXT_CLICK";
            }
            case 16908347: {
                return "ACTION_SCROLL_RIGHT";
            }
            case 16908346: {
                return "ACTION_SCROLL_DOWN";
            }
            case 16908345: {
                return "ACTION_SCROLL_LEFT";
            }
            case 16908344: {
                return "ACTION_SCROLL_UP";
            }
            case 16908343: {
                return "ACTION_SCROLL_TO_POSITION";
            }
            case 16908342: {
                return "ACTION_SHOW_ON_SCREEN";
            }
            case 0x200000: {
                return "ACTION_SET_TEXT";
            }
            case 524288: {
                return "ACTION_COLLAPSE";
            }
            case 262144: {
                return "ACTION_EXPAND";
            }
            case 131072: {
                return "ACTION_SET_SELECTION";
            }
            case 65536: {
                return "ACTION_CUT";
            }
            case 32768: {
                return "ACTION_PASTE";
            }
            case 16384: {
                return "ACTION_COPY";
            }
            case 8192: {
                return "ACTION_SCROLL_BACKWARD";
            }
            case 4096: {
                return "ACTION_SCROLL_FORWARD";
            }
            case 2048: {
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            }
            case 1024: {
                return "ACTION_NEXT_HTML_ELEMENT";
            }
            case 512: {
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            }
            case 256: {
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            }
            case 128: {
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            }
            case 64: {
                return "ACTION_ACCESSIBILITY_FOCUS";
            }
            case 32: {
                return "ACTION_LONG_CLICK";
            }
            case 16: {
                return "ACTION_CLICK";
            }
            case 8: {
                return "ACTION_CLEAR_SELECTION";
            }
            case 4: {
                return "ACTION_SELECT";
            }
            case 2: {
                return "ACTION_CLEAR_FOCUS";
            }
            case 1: 
        }
        return "ACTION_FOCUS";
    }

    private boolean getBooleanProperty(int n) {
        Bundle bundle = this.getExtras();
        boolean bl = false;
        if (bundle == null) {
            return false;
        }
        if ((bundle.getInt(BOOLEAN_PROPERTY_KEY, 0) & n) == n) {
            bl = true;
        }
        return bl;
    }

    public static ClickableSpan[] getClickableSpans(CharSequence charSequence) {
        if (charSequence instanceof Spanned) {
            return (ClickableSpan[])((Spanned)charSequence).getSpans(0, charSequence.length(), ClickableSpan.class);
        }
        return null;
    }

    private SparseArray<WeakReference<ClickableSpan>> getOrCreateSpansFromViewTags(View view) {
        SparseArray sparseArray;
        SparseArray sparseArray2 = sparseArray = this.getSpansFromViewTags(view);
        if (sparseArray == null) {
            sparseArray2 = new SparseArray();
            view.setTag(R.id.tag_accessibility_clickable_spans, (Object)sparseArray2);
        }
        return sparseArray2;
    }

    private SparseArray<WeakReference<ClickableSpan>> getSpansFromViewTags(View view) {
        return (SparseArray)view.getTag(R.id.tag_accessibility_clickable_spans);
    }

    private boolean hasSpans() {
        return this.extrasIntList(SPANS_START_KEY).isEmpty() ^ true;
    }

    private int idForClickableSpan(ClickableSpan clickableSpan, SparseArray<WeakReference<ClickableSpan>> sparseArray) {
        int n;
        if (sparseArray != null) {
            for (n = 0; n < sparseArray.size(); ++n) {
                if (!clickableSpan.equals((ClickableSpan)((WeakReference)sparseArray.valueAt(n)).get())) continue;
                return sparseArray.keyAt(n);
            }
        }
        n = sClickableSpanId;
        sClickableSpanId = n + 1;
        return n;
    }

    public static AccessibilityNodeInfoCompat obtain() {
        return AccessibilityNodeInfoCompat.wrap(AccessibilityNodeInfo.obtain());
    }

    public static AccessibilityNodeInfoCompat obtain(View view) {
        return AccessibilityNodeInfoCompat.wrap(AccessibilityNodeInfo.obtain((View)view));
    }

    public static AccessibilityNodeInfoCompat obtain(View view, int n) {
        if (Build.VERSION.SDK_INT >= 16) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(AccessibilityNodeInfo.obtain((View)view, (int)n));
        }
        return null;
    }

    public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return AccessibilityNodeInfoCompat.wrap(AccessibilityNodeInfo.obtain((AccessibilityNodeInfo)accessibilityNodeInfoCompat.mInfo));
    }

    private void removeCollectedSpans(View object) {
        SparseArray<WeakReference<ClickableSpan>> sparseArray = this.getSpansFromViewTags((View)object);
        if (sparseArray != null) {
            int n;
            object = new ArrayList();
            for (n = 0; n < sparseArray.size(); ++n) {
                if (((WeakReference)sparseArray.valueAt(n)).get() != null) continue;
                object.add(n);
            }
            for (n = 0; n < object.size(); ++n) {
                sparseArray.remove(((Integer)object.get(n)).intValue());
            }
        }
    }

    private void setBooleanProperty(int n, boolean bl) {
        Bundle bundle = this.getExtras();
        if (bundle != null) {
            int n2 = 0;
            int n3 = bundle.getInt(BOOLEAN_PROPERTY_KEY, 0);
            if (bl) {
                n2 = n;
            }
            bundle.putInt(BOOLEAN_PROPERTY_KEY, n2 | n3 & ~n);
        }
    }

    public static AccessibilityNodeInfoCompat wrap(AccessibilityNodeInfo accessibilityNodeInfo) {
        return new AccessibilityNodeInfoCompat(accessibilityNodeInfo);
    }

    static AccessibilityNodeInfoCompat wrapNonNullInstance(Object object) {
        if (object != null) {
            return new AccessibilityNodeInfoCompat(object);
        }
        return null;
    }

    public void addAction(int n) {
        this.mInfo.addAction(n);
    }

    public void addAction(AccessibilityActionCompat accessibilityActionCompat) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mInfo.addAction((AccessibilityNodeInfo.AccessibilityAction)accessibilityActionCompat.mAction);
        }
    }

    public void addChild(View view) {
        this.mInfo.addChild(view);
    }

    public void addChild(View view, int n) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.mInfo.addChild(view, n);
        }
    }

    public void addSpansToExtras(CharSequence charSequence, View sparseArray) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 26) {
            this.clearExtrasSpans();
            this.removeCollectedSpans((View)sparseArray);
            ClickableSpan[] clickableSpanArray = AccessibilityNodeInfoCompat.getClickableSpans(charSequence);
            if (clickableSpanArray != null && clickableSpanArray.length > 0) {
                this.getExtras().putInt(SPANS_ACTION_ID_KEY, R.id.accessibility_action_clickable_span);
                sparseArray = this.getOrCreateSpansFromViewTags((View)sparseArray);
                for (int i = 0; clickableSpanArray != null && i < clickableSpanArray.length; ++i) {
                    int n = this.idForClickableSpan(clickableSpanArray[i], sparseArray);
                    sparseArray.put(n, new WeakReference<ClickableSpan>(clickableSpanArray[i]));
                    this.addSpanLocationToExtras(clickableSpanArray[i], (Spanned)charSequence, n);
                }
            }
        }
    }

    public boolean canOpenPopup() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.canOpenPopup();
        }
        return false;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof AccessibilityNodeInfoCompat)) {
            return false;
        }
        object = (AccessibilityNodeInfoCompat)object;
        AccessibilityNodeInfo accessibilityNodeInfo = this.mInfo;
        if (accessibilityNodeInfo == null ? ((AccessibilityNodeInfoCompat)object).mInfo != null : !accessibilityNodeInfo.equals((Object)((AccessibilityNodeInfoCompat)object).mInfo)) {
            return false;
        }
        if (this.mVirtualDescendantId != ((AccessibilityNodeInfoCompat)object).mVirtualDescendantId) {
            return false;
        }
        return this.mParentVirtualDescendantId == ((AccessibilityNodeInfoCompat)object).mParentVirtualDescendantId;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String object) {
        ArrayList<AccessibilityNodeInfoCompat> arrayList = new ArrayList<AccessibilityNodeInfoCompat>();
        object = this.mInfo.findAccessibilityNodeInfosByText((String)object);
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            arrayList.add(AccessibilityNodeInfoCompat.wrap((AccessibilityNodeInfo)object.get(i)));
        }
        return arrayList;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId(String object) {
        if (Build.VERSION.SDK_INT >= 18) {
            Object object2 = this.mInfo.findAccessibilityNodeInfosByViewId((String)object);
            object = new ArrayList();
            object2 = object2.iterator();
            while (object2.hasNext()) {
                object.add(AccessibilityNodeInfoCompat.wrap((AccessibilityNodeInfo)object2.next()));
            }
            return object;
        }
        return Collections.emptyList();
    }

    public AccessibilityNodeInfoCompat findFocus(int n) {
        if (Build.VERSION.SDK_INT >= 16) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mInfo.findFocus(n));
        }
        return null;
    }

    public AccessibilityNodeInfoCompat focusSearch(int n) {
        if (Build.VERSION.SDK_INT >= 16) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mInfo.focusSearch(n));
        }
        return null;
    }

    public List<AccessibilityActionCompat> getActionList() {
        List list = null;
        if (Build.VERSION.SDK_INT >= 21) {
            list = this.mInfo.getActionList();
        }
        if (list != null) {
            ArrayList<AccessibilityActionCompat> arrayList = new ArrayList<AccessibilityActionCompat>();
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                arrayList.add(new AccessibilityActionCompat(list.get(i)));
            }
            return arrayList;
        }
        return Collections.emptyList();
    }

    public int getActions() {
        return this.mInfo.getActions();
    }

    @Deprecated
    public void getBoundsInParent(Rect rect) {
        this.mInfo.getBoundsInParent(rect);
    }

    public void getBoundsInScreen(Rect rect) {
        this.mInfo.getBoundsInScreen(rect);
    }

    public AccessibilityNodeInfoCompat getChild(int n) {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mInfo.getChild(n));
    }

    public int getChildCount() {
        return this.mInfo.getChildCount();
    }

    public CharSequence getClassName() {
        return this.mInfo.getClassName();
    }

    public CollectionInfoCompat getCollectionInfo() {
        AccessibilityNodeInfo.CollectionInfo collectionInfo;
        if (Build.VERSION.SDK_INT >= 19 && (collectionInfo = this.mInfo.getCollectionInfo()) != null) {
            return new CollectionInfoCompat(collectionInfo);
        }
        return null;
    }

    public CollectionItemInfoCompat getCollectionItemInfo() {
        AccessibilityNodeInfo.CollectionItemInfo collectionItemInfo;
        if (Build.VERSION.SDK_INT >= 19 && (collectionItemInfo = this.mInfo.getCollectionItemInfo()) != null) {
            return new CollectionItemInfoCompat(collectionItemInfo);
        }
        return null;
    }

    public CharSequence getContentDescription() {
        return this.mInfo.getContentDescription();
    }

    public int getDrawingOrder() {
        if (Build.VERSION.SDK_INT >= 24) {
            return this.mInfo.getDrawingOrder();
        }
        return 0;
    }

    public CharSequence getError() {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mInfo.getError();
        }
        return null;
    }

    public Bundle getExtras() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras();
        }
        return new Bundle();
    }

    public CharSequence getHintText() {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.mInfo.getHintText();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence(HINT_TEXT_KEY);
        }
        return null;
    }

    @Deprecated
    public Object getInfo() {
        return this.mInfo;
    }

    public int getInputType() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.getInputType();
        }
        return 0;
    }

    public AccessibilityNodeInfoCompat getLabelFor() {
        if (Build.VERSION.SDK_INT >= 17) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mInfo.getLabelFor());
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getLabeledBy() {
        if (Build.VERSION.SDK_INT >= 17) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mInfo.getLabeledBy());
        }
        return null;
    }

    public int getLiveRegion() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.getLiveRegion();
        }
        return 0;
    }

    public int getMaxTextLength() {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mInfo.getMaxTextLength();
        }
        return -1;
    }

    public int getMovementGranularities() {
        if (Build.VERSION.SDK_INT >= 16) {
            return this.mInfo.getMovementGranularities();
        }
        return 0;
    }

    public CharSequence getPackageName() {
        return this.mInfo.getPackageName();
    }

    public CharSequence getPaneTitle() {
        if (Build.VERSION.SDK_INT >= 28) {
            return this.mInfo.getPaneTitle();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence(PANE_TITLE_KEY);
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getParent() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mInfo.getParent());
    }

    public RangeInfoCompat getRangeInfo() {
        AccessibilityNodeInfo.RangeInfo rangeInfo;
        if (Build.VERSION.SDK_INT >= 19 && (rangeInfo = this.mInfo.getRangeInfo()) != null) {
            return new RangeInfoCompat(rangeInfo);
        }
        return null;
    }

    public CharSequence getRoleDescription() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence(ROLE_DESCRIPTION_KEY);
        }
        return null;
    }

    public CharSequence getText() {
        if (this.hasSpans()) {
            List<Integer> list = this.extrasIntList(SPANS_START_KEY);
            List<Integer> list2 = this.extrasIntList(SPANS_END_KEY);
            List<Integer> list3 = this.extrasIntList(SPANS_FLAGS_KEY);
            List<Integer> list4 = this.extrasIntList(SPANS_ID_KEY);
            SpannableString spannableString = new SpannableString((CharSequence)TextUtils.substring((CharSequence)this.mInfo.getText(), (int)0, (int)this.mInfo.getText().length()));
            for (int i = 0; i < list.size(); ++i) {
                spannableString.setSpan((Object)new AccessibilityClickableSpanCompat(list4.get(i), this, this.getExtras().getInt(SPANS_ACTION_ID_KEY)), list.get(i).intValue(), list2.get(i).intValue(), list3.get(i).intValue());
            }
            return spannableString;
        }
        return this.mInfo.getText();
    }

    public int getTextSelectionEnd() {
        if (Build.VERSION.SDK_INT >= 18) {
            return this.mInfo.getTextSelectionEnd();
        }
        return -1;
    }

    public int getTextSelectionStart() {
        if (Build.VERSION.SDK_INT >= 18) {
            return this.mInfo.getTextSelectionStart();
        }
        return -1;
    }

    public CharSequence getTooltipText() {
        if (Build.VERSION.SDK_INT >= 28) {
            return this.mInfo.getTooltipText();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence(TOOLTIP_TEXT_KEY);
        }
        return null;
    }

    public TouchDelegateInfoCompat getTouchDelegateInfo() {
        AccessibilityNodeInfo.TouchDelegateInfo touchDelegateInfo;
        if (Build.VERSION.SDK_INT >= 29 && (touchDelegateInfo = this.mInfo.getTouchDelegateInfo()) != null) {
            return new TouchDelegateInfoCompat(touchDelegateInfo);
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getTraversalAfter() {
        if (Build.VERSION.SDK_INT >= 22) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mInfo.getTraversalAfter());
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getTraversalBefore() {
        if (Build.VERSION.SDK_INT >= 22) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mInfo.getTraversalBefore());
        }
        return null;
    }

    public String getViewIdResourceName() {
        if (Build.VERSION.SDK_INT >= 18) {
            return this.mInfo.getViewIdResourceName();
        }
        return null;
    }

    public AccessibilityWindowInfoCompat getWindow() {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance(this.mInfo.getWindow());
        }
        return null;
    }

    public int getWindowId() {
        return this.mInfo.getWindowId();
    }

    public int hashCode() {
        AccessibilityNodeInfo accessibilityNodeInfo = this.mInfo;
        int n = accessibilityNodeInfo == null ? 0 : accessibilityNodeInfo.hashCode();
        return n;
    }

    public boolean isAccessibilityFocused() {
        if (Build.VERSION.SDK_INT >= 16) {
            return this.mInfo.isAccessibilityFocused();
        }
        return false;
    }

    public boolean isCheckable() {
        return this.mInfo.isCheckable();
    }

    public boolean isChecked() {
        return this.mInfo.isChecked();
    }

    public boolean isClickable() {
        return this.mInfo.isClickable();
    }

    public boolean isContentInvalid() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.isContentInvalid();
        }
        return false;
    }

    public boolean isContextClickable() {
        if (Build.VERSION.SDK_INT >= 23) {
            return this.mInfo.isContextClickable();
        }
        return false;
    }

    public boolean isDismissable() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.isDismissable();
        }
        return false;
    }

    public boolean isEditable() {
        if (Build.VERSION.SDK_INT >= 18) {
            return this.mInfo.isEditable();
        }
        return false;
    }

    public boolean isEnabled() {
        return this.mInfo.isEnabled();
    }

    public boolean isFocusable() {
        return this.mInfo.isFocusable();
    }

    public boolean isFocused() {
        return this.mInfo.isFocused();
    }

    public boolean isHeading() {
        if (Build.VERSION.SDK_INT >= 28) {
            return this.mInfo.isHeading();
        }
        boolean bl = this.getBooleanProperty(2);
        boolean bl2 = true;
        if (bl) {
            return true;
        }
        CollectionItemInfoCompat collectionItemInfoCompat = this.getCollectionItemInfo();
        if (collectionItemInfoCompat == null || !collectionItemInfoCompat.isHeading()) {
            bl2 = false;
        }
        return bl2;
    }

    public boolean isImportantForAccessibility() {
        if (Build.VERSION.SDK_INT >= 24) {
            return this.mInfo.isImportantForAccessibility();
        }
        return true;
    }

    public boolean isLongClickable() {
        return this.mInfo.isLongClickable();
    }

    public boolean isMultiLine() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.mInfo.isMultiLine();
        }
        return false;
    }

    public boolean isPassword() {
        return this.mInfo.isPassword();
    }

    public boolean isScreenReaderFocusable() {
        if (Build.VERSION.SDK_INT >= 28) {
            return this.mInfo.isScreenReaderFocusable();
        }
        return this.getBooleanProperty(1);
    }

    public boolean isScrollable() {
        return this.mInfo.isScrollable();
    }

    public boolean isSelected() {
        return this.mInfo.isSelected();
    }

    public boolean isShowingHintText() {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.mInfo.isShowingHintText();
        }
        return this.getBooleanProperty(4);
    }

    public boolean isTextEntryKey() {
        if (Build.VERSION.SDK_INT >= 29) {
            return this.mInfo.isTextEntryKey();
        }
        return this.getBooleanProperty(8);
    }

    public boolean isVisibleToUser() {
        if (Build.VERSION.SDK_INT >= 16) {
            return this.mInfo.isVisibleToUser();
        }
        return false;
    }

    public boolean performAction(int n) {
        return this.mInfo.performAction(n);
    }

    public boolean performAction(int n, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            return this.mInfo.performAction(n, bundle);
        }
        return false;
    }

    public void recycle() {
        this.mInfo.recycle();
    }

    public boolean refresh() {
        if (Build.VERSION.SDK_INT >= 18) {
            return this.mInfo.refresh();
        }
        return false;
    }

    public boolean removeAction(AccessibilityActionCompat accessibilityActionCompat) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mInfo.removeAction((AccessibilityNodeInfo.AccessibilityAction)accessibilityActionCompat.mAction);
        }
        return false;
    }

    public boolean removeChild(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mInfo.removeChild(view);
        }
        return false;
    }

    public boolean removeChild(View view, int n) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mInfo.removeChild(view, n);
        }
        return false;
    }

    public void setAccessibilityFocused(boolean bl) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.mInfo.setAccessibilityFocused(bl);
        }
    }

    @Deprecated
    public void setBoundsInParent(Rect rect) {
        this.mInfo.setBoundsInParent(rect);
    }

    public void setBoundsInScreen(Rect rect) {
        this.mInfo.setBoundsInScreen(rect);
    }

    public void setCanOpenPopup(boolean bl) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.setCanOpenPopup(bl);
        }
    }

    public void setCheckable(boolean bl) {
        this.mInfo.setCheckable(bl);
    }

    public void setChecked(boolean bl) {
        this.mInfo.setChecked(bl);
    }

    public void setClassName(CharSequence charSequence) {
        this.mInfo.setClassName(charSequence);
    }

    public void setClickable(boolean bl) {
        this.mInfo.setClickable(bl);
    }

    public void setCollectionInfo(Object object) {
        if (Build.VERSION.SDK_INT >= 19) {
            AccessibilityNodeInfo accessibilityNodeInfo = this.mInfo;
            object = object == null ? null : (AccessibilityNodeInfo.CollectionInfo)((CollectionInfoCompat)object).mInfo;
            accessibilityNodeInfo.setCollectionInfo((AccessibilityNodeInfo.CollectionInfo)object);
        }
    }

    public void setCollectionItemInfo(Object object) {
        if (Build.VERSION.SDK_INT >= 19) {
            AccessibilityNodeInfo accessibilityNodeInfo = this.mInfo;
            object = object == null ? null : (AccessibilityNodeInfo.CollectionItemInfo)((CollectionItemInfoCompat)object).mInfo;
            accessibilityNodeInfo.setCollectionItemInfo((AccessibilityNodeInfo.CollectionItemInfo)object);
        }
    }

    public void setContentDescription(CharSequence charSequence) {
        this.mInfo.setContentDescription(charSequence);
    }

    public void setContentInvalid(boolean bl) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.setContentInvalid(bl);
        }
    }

    public void setContextClickable(boolean bl) {
        if (Build.VERSION.SDK_INT >= 23) {
            this.mInfo.setContextClickable(bl);
        }
    }

    public void setDismissable(boolean bl) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.setDismissable(bl);
        }
    }

    public void setDrawingOrder(int n) {
        if (Build.VERSION.SDK_INT >= 24) {
            this.mInfo.setDrawingOrder(n);
        }
    }

    public void setEditable(boolean bl) {
        if (Build.VERSION.SDK_INT >= 18) {
            this.mInfo.setEditable(bl);
        }
    }

    public void setEnabled(boolean bl) {
        this.mInfo.setEnabled(bl);
    }

    public void setError(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mInfo.setError(charSequence);
        }
    }

    public void setFocusable(boolean bl) {
        this.mInfo.setFocusable(bl);
    }

    public void setFocused(boolean bl) {
        this.mInfo.setFocused(bl);
    }

    public void setHeading(boolean bl) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.mInfo.setHeading(bl);
        } else {
            this.setBooleanProperty(2, bl);
        }
    }

    public void setHintText(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 26) {
            this.mInfo.setHintText(charSequence);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence(HINT_TEXT_KEY, charSequence);
        }
    }

    public void setImportantForAccessibility(boolean bl) {
        if (Build.VERSION.SDK_INT >= 24) {
            this.mInfo.setImportantForAccessibility(bl);
        }
    }

    public void setInputType(int n) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.setInputType(n);
        }
    }

    public void setLabelFor(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            this.mInfo.setLabelFor(view);
        }
    }

    public void setLabelFor(View view, int n) {
        if (Build.VERSION.SDK_INT >= 17) {
            this.mInfo.setLabelFor(view, n);
        }
    }

    public void setLabeledBy(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            this.mInfo.setLabeledBy(view);
        }
    }

    public void setLabeledBy(View view, int n) {
        if (Build.VERSION.SDK_INT >= 17) {
            this.mInfo.setLabeledBy(view, n);
        }
    }

    public void setLiveRegion(int n) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.setLiveRegion(n);
        }
    }

    public void setLongClickable(boolean bl) {
        this.mInfo.setLongClickable(bl);
    }

    public void setMaxTextLength(int n) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mInfo.setMaxTextLength(n);
        }
    }

    public void setMovementGranularities(int n) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.mInfo.setMovementGranularities(n);
        }
    }

    public void setMultiLine(boolean bl) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.setMultiLine(bl);
        }
    }

    public void setPackageName(CharSequence charSequence) {
        this.mInfo.setPackageName(charSequence);
    }

    public void setPaneTitle(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.mInfo.setPaneTitle(charSequence);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence(PANE_TITLE_KEY, charSequence);
        }
    }

    public void setParent(View view) {
        this.mParentVirtualDescendantId = -1;
        this.mInfo.setParent(view);
    }

    public void setParent(View view, int n) {
        this.mParentVirtualDescendantId = n;
        if (Build.VERSION.SDK_INT >= 16) {
            this.mInfo.setParent(view, n);
        }
    }

    public void setPassword(boolean bl) {
        this.mInfo.setPassword(bl);
    }

    public void setRangeInfo(RangeInfoCompat rangeInfoCompat) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.setRangeInfo((AccessibilityNodeInfo.RangeInfo)rangeInfoCompat.mInfo);
        }
    }

    public void setRoleDescription(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence(ROLE_DESCRIPTION_KEY, charSequence);
        }
    }

    public void setScreenReaderFocusable(boolean bl) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.mInfo.setScreenReaderFocusable(bl);
        } else {
            this.setBooleanProperty(1, bl);
        }
    }

    public void setScrollable(boolean bl) {
        this.mInfo.setScrollable(bl);
    }

    public void setSelected(boolean bl) {
        this.mInfo.setSelected(bl);
    }

    public void setShowingHintText(boolean bl) {
        if (Build.VERSION.SDK_INT >= 26) {
            this.mInfo.setShowingHintText(bl);
        } else {
            this.setBooleanProperty(4, bl);
        }
    }

    public void setSource(View view) {
        this.mVirtualDescendantId = -1;
        this.mInfo.setSource(view);
    }

    public void setSource(View view, int n) {
        this.mVirtualDescendantId = n;
        if (Build.VERSION.SDK_INT >= 16) {
            this.mInfo.setSource(view, n);
        }
    }

    public void setText(CharSequence charSequence) {
        this.mInfo.setText(charSequence);
    }

    public void setTextEntryKey(boolean bl) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.mInfo.setTextEntryKey(bl);
        } else {
            this.setBooleanProperty(8, bl);
        }
    }

    public void setTextSelection(int n, int n2) {
        if (Build.VERSION.SDK_INT >= 18) {
            this.mInfo.setTextSelection(n, n2);
        }
    }

    public void setTooltipText(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.mInfo.setTooltipText(charSequence);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence(TOOLTIP_TEXT_KEY, charSequence);
        }
    }

    public void setTouchDelegateInfo(TouchDelegateInfoCompat touchDelegateInfoCompat) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.mInfo.setTouchDelegateInfo(touchDelegateInfoCompat.mInfo);
        }
    }

    public void setTraversalAfter(View view) {
        if (Build.VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalAfter(view);
        }
    }

    public void setTraversalAfter(View view, int n) {
        if (Build.VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalAfter(view, n);
        }
    }

    public void setTraversalBefore(View view) {
        if (Build.VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalBefore(view);
        }
    }

    public void setTraversalBefore(View view, int n) {
        if (Build.VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalBefore(view, n);
        }
    }

    public void setViewIdResourceName(String string2) {
        if (Build.VERSION.SDK_INT >= 18) {
            this.mInfo.setViewIdResourceName(string2);
        }
    }

    public void setVisibleToUser(boolean bl) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.mInfo.setVisibleToUser(bl);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        Object object = new Rect();
        this.getBoundsInParent((Rect)object);
        CharSequence charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("; boundsInParent: ");
        ((StringBuilder)charSequence).append(object);
        stringBuilder.append(((StringBuilder)charSequence).toString());
        this.getBoundsInScreen((Rect)object);
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("; boundsInScreen: ");
        ((StringBuilder)charSequence).append(object);
        stringBuilder.append(((StringBuilder)charSequence).toString());
        stringBuilder.append("; packageName: ");
        stringBuilder.append(this.getPackageName());
        stringBuilder.append("; className: ");
        stringBuilder.append(this.getClassName());
        stringBuilder.append("; text: ");
        stringBuilder.append(this.getText());
        stringBuilder.append("; contentDescription: ");
        stringBuilder.append(this.getContentDescription());
        stringBuilder.append("; viewId: ");
        stringBuilder.append(this.getViewIdResourceName());
        stringBuilder.append("; checkable: ");
        stringBuilder.append(this.isCheckable());
        stringBuilder.append("; checked: ");
        stringBuilder.append(this.isChecked());
        stringBuilder.append("; focusable: ");
        stringBuilder.append(this.isFocusable());
        stringBuilder.append("; focused: ");
        stringBuilder.append(this.isFocused());
        stringBuilder.append("; selected: ");
        stringBuilder.append(this.isSelected());
        stringBuilder.append("; clickable: ");
        stringBuilder.append(this.isClickable());
        stringBuilder.append("; longClickable: ");
        stringBuilder.append(this.isLongClickable());
        stringBuilder.append("; enabled: ");
        stringBuilder.append(this.isEnabled());
        stringBuilder.append("; password: ");
        stringBuilder.append(this.isPassword());
        object = new StringBuilder();
        ((StringBuilder)object).append("; scrollable: ");
        ((StringBuilder)object).append(this.isScrollable());
        stringBuilder.append(((StringBuilder)object).toString());
        stringBuilder.append("; [");
        if (Build.VERSION.SDK_INT >= 21) {
            List<AccessibilityActionCompat> list = this.getActionList();
            for (int i = 0; i < list.size(); ++i) {
                AccessibilityActionCompat accessibilityActionCompat = list.get(i);
                charSequence = AccessibilityNodeInfoCompat.getActionSymbolicName(accessibilityActionCompat.getId());
                object = charSequence;
                if (((String)charSequence).equals("ACTION_UNKNOWN")) {
                    object = charSequence;
                    if (accessibilityActionCompat.getLabel() != null) {
                        object = accessibilityActionCompat.getLabel().toString();
                    }
                }
                stringBuilder.append((String)object);
                if (i == list.size() - 1) continue;
                stringBuilder.append(", ");
            }
        } else {
            int n = this.getActions();
            while (n != 0) {
                int n2 = 1 << Integer.numberOfTrailingZeros(n);
                stringBuilder.append(AccessibilityNodeInfoCompat.getActionSymbolicName(n2));
                if ((n &= ~n2) == 0) continue;
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public AccessibilityNodeInfo unwrap() {
        return this.mInfo;
    }

    public static class AccessibilityActionCompat {
        public static final AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_SELECTION;
        public static final AccessibilityActionCompat ACTION_CLICK;
        public static final AccessibilityActionCompat ACTION_COLLAPSE;
        public static final AccessibilityActionCompat ACTION_CONTEXT_CLICK;
        public static final AccessibilityActionCompat ACTION_COPY;
        public static final AccessibilityActionCompat ACTION_CUT;
        public static final AccessibilityActionCompat ACTION_DISMISS;
        public static final AccessibilityActionCompat ACTION_EXPAND;
        public static final AccessibilityActionCompat ACTION_FOCUS;
        public static final AccessibilityActionCompat ACTION_HIDE_TOOLTIP;
        public static final AccessibilityActionCompat ACTION_LONG_CLICK;
        public static final AccessibilityActionCompat ACTION_MOVE_WINDOW;
        public static final AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_PAGE_DOWN;
        public static final AccessibilityActionCompat ACTION_PAGE_LEFT;
        public static final AccessibilityActionCompat ACTION_PAGE_RIGHT;
        public static final AccessibilityActionCompat ACTION_PAGE_UP;
        public static final AccessibilityActionCompat ACTION_PASTE;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_SCROLL_BACKWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_DOWN;
        public static final AccessibilityActionCompat ACTION_SCROLL_FORWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_LEFT;
        public static final AccessibilityActionCompat ACTION_SCROLL_RIGHT;
        public static final AccessibilityActionCompat ACTION_SCROLL_TO_POSITION;
        public static final AccessibilityActionCompat ACTION_SCROLL_UP;
        public static final AccessibilityActionCompat ACTION_SELECT;
        public static final AccessibilityActionCompat ACTION_SET_PROGRESS;
        public static final AccessibilityActionCompat ACTION_SET_SELECTION;
        public static final AccessibilityActionCompat ACTION_SET_TEXT;
        public static final AccessibilityActionCompat ACTION_SHOW_ON_SCREEN;
        public static final AccessibilityActionCompat ACTION_SHOW_TOOLTIP;
        private static final String TAG = "A11yActionCompat";
        final Object mAction;
        protected final AccessibilityViewCommand mCommand;
        private final int mId;
        private final Class<? extends AccessibilityViewCommand.CommandArguments> mViewCommandArgumentClass;

        static {
            Object var1 = null;
            ACTION_FOCUS = new AccessibilityActionCompat(1, null);
            ACTION_CLEAR_FOCUS = new AccessibilityActionCompat(2, null);
            ACTION_SELECT = new AccessibilityActionCompat(4, null);
            ACTION_CLEAR_SELECTION = new AccessibilityActionCompat(8, null);
            ACTION_CLICK = new AccessibilityActionCompat(16, null);
            ACTION_LONG_CLICK = new AccessibilityActionCompat(32, null);
            ACTION_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(64, null);
            ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(128, null);
            ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(256, null, AccessibilityViewCommand.MoveAtGranularityArguments.class);
            ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(512, null, AccessibilityViewCommand.MoveAtGranularityArguments.class);
            ACTION_NEXT_HTML_ELEMENT = new AccessibilityActionCompat(1024, null, AccessibilityViewCommand.MoveHtmlArguments.class);
            ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityActionCompat(2048, null, AccessibilityViewCommand.MoveHtmlArguments.class);
            ACTION_SCROLL_FORWARD = new AccessibilityActionCompat(4096, null);
            ACTION_SCROLL_BACKWARD = new AccessibilityActionCompat(8192, null);
            ACTION_COPY = new AccessibilityActionCompat(16384, null);
            ACTION_PASTE = new AccessibilityActionCompat(32768, null);
            ACTION_CUT = new AccessibilityActionCompat(65536, null);
            ACTION_SET_SELECTION = new AccessibilityActionCompat(131072, null, AccessibilityViewCommand.SetSelectionArguments.class);
            ACTION_EXPAND = new AccessibilityActionCompat(262144, null);
            ACTION_COLLAPSE = new AccessibilityActionCompat(524288, null);
            ACTION_DISMISS = new AccessibilityActionCompat(0x100000, null);
            ACTION_SET_TEXT = new AccessibilityActionCompat(0x200000, null, AccessibilityViewCommand.SetTextArguments.class);
            Object object = Build.VERSION.SDK_INT >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN : null;
            ACTION_SHOW_ON_SCREEN = new AccessibilityActionCompat(object, 16908342, null, null, null);
            object = Build.VERSION.SDK_INT >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION : null;
            ACTION_SCROLL_TO_POSITION = new AccessibilityActionCompat(object, 16908343, null, null, AccessibilityViewCommand.ScrollToPositionArguments.class);
            object = Build.VERSION.SDK_INT >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP : null;
            ACTION_SCROLL_UP = new AccessibilityActionCompat(object, 16908344, null, null, null);
            object = Build.VERSION.SDK_INT >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT : null;
            ACTION_SCROLL_LEFT = new AccessibilityActionCompat(object, 16908345, null, null, null);
            object = Build.VERSION.SDK_INT >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN : null;
            ACTION_SCROLL_DOWN = new AccessibilityActionCompat(object, 16908346, null, null, null);
            object = Build.VERSION.SDK_INT >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT : null;
            ACTION_SCROLL_RIGHT = new AccessibilityActionCompat(object, 16908347, null, null, null);
            object = Build.VERSION.SDK_INT >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_UP : null;
            ACTION_PAGE_UP = new AccessibilityActionCompat(object, 16908358, null, null, null);
            object = Build.VERSION.SDK_INT >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_DOWN : null;
            ACTION_PAGE_DOWN = new AccessibilityActionCompat(object, 16908359, null, null, null);
            object = Build.VERSION.SDK_INT >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_LEFT : null;
            ACTION_PAGE_LEFT = new AccessibilityActionCompat(object, 16908360, null, null, null);
            object = Build.VERSION.SDK_INT >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_RIGHT : null;
            ACTION_PAGE_RIGHT = new AccessibilityActionCompat(object, 16908361, null, null, null);
            object = Build.VERSION.SDK_INT >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK : null;
            ACTION_CONTEXT_CLICK = new AccessibilityActionCompat(object, 16908348, null, null, null);
            object = Build.VERSION.SDK_INT >= 24 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS : null;
            ACTION_SET_PROGRESS = new AccessibilityActionCompat(object, 16908349, null, null, AccessibilityViewCommand.SetProgressArguments.class);
            object = Build.VERSION.SDK_INT >= 26 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_MOVE_WINDOW : null;
            ACTION_MOVE_WINDOW = new AccessibilityActionCompat(object, 16908354, null, null, AccessibilityViewCommand.MoveWindowArguments.class);
            object = Build.VERSION.SDK_INT >= 28 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TOOLTIP : null;
            ACTION_SHOW_TOOLTIP = new AccessibilityActionCompat(object, 16908356, null, null, null);
            object = Build.VERSION.SDK_INT >= 28 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_HIDE_TOOLTIP : var1;
            ACTION_HIDE_TOOLTIP = new AccessibilityActionCompat(object, 16908357, null, null, null);
        }

        public AccessibilityActionCompat(int n, CharSequence charSequence) {
            this(null, n, charSequence, null, null);
        }

        public AccessibilityActionCompat(int n, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
            this(null, n, charSequence, accessibilityViewCommand, null);
        }

        private AccessibilityActionCompat(int n, CharSequence charSequence, Class<? extends AccessibilityViewCommand.CommandArguments> clazz) {
            this(null, n, charSequence, null, clazz);
        }

        AccessibilityActionCompat(Object object) {
            this(object, 0, null, null, null);
        }

        AccessibilityActionCompat(Object object, int n, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand, Class<? extends AccessibilityViewCommand.CommandArguments> clazz) {
            this.mId = n;
            this.mCommand = accessibilityViewCommand;
            this.mAction = Build.VERSION.SDK_INT >= 21 && object == null ? new AccessibilityNodeInfo.AccessibilityAction(n, charSequence) : object;
            this.mViewCommandArgumentClass = clazz;
        }

        public AccessibilityActionCompat createReplacementAction(CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
            return new AccessibilityActionCompat(null, this.mId, charSequence, accessibilityViewCommand, this.mViewCommandArgumentClass);
        }

        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (!(object instanceof AccessibilityActionCompat)) {
                return false;
            }
            object = (AccessibilityActionCompat)object;
            Object object2 = this.mAction;
            return !(object2 == null ? ((AccessibilityActionCompat)object).mAction != null : !object2.equals(((AccessibilityActionCompat)object).mAction));
        }

        public int getId() {
            if (Build.VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo.AccessibilityAction)this.mAction).getId();
            }
            return 0;
        }

        public CharSequence getLabel() {
            if (Build.VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo.AccessibilityAction)this.mAction).getLabel();
            }
            return null;
        }

        public int hashCode() {
            Object object = this.mAction;
            int n = object != null ? object.hashCode() : 0;
            return n;
        }

        public boolean perform(View view, Bundle object) {
            if (this.mCommand != null) {
                AccessibilityViewCommand.CommandArguments commandArguments = null;
                AccessibilityViewCommand.CommandArguments commandArguments2 = null;
                Serializable serializable = this.mViewCommandArgumentClass;
                if (serializable != null) {
                    commandArguments = commandArguments2;
                    commandArguments = commandArguments2 = ((Class)serializable).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    try {
                        commandArguments2.setBundle((Bundle)object);
                        commandArguments = commandArguments2;
                    }
                    catch (Exception exception) {
                        object = this.mViewCommandArgumentClass;
                        object = object == null ? "null" : object.getName();
                        serializable = new StringBuilder();
                        ((StringBuilder)serializable).append("Failed to execute command with argument class ViewCommandArgument: ");
                        ((StringBuilder)serializable).append((String)object);
                        Log.e((String)TAG, (String)((StringBuilder)serializable).toString(), (Throwable)exception);
                    }
                }
                return this.mCommand.perform(view, commandArguments);
            }
            return false;
        }
    }

    public static class CollectionInfoCompat {
        public static final int SELECTION_MODE_MULTIPLE = 2;
        public static final int SELECTION_MODE_NONE = 0;
        public static final int SELECTION_MODE_SINGLE = 1;
        final Object mInfo;

        CollectionInfoCompat(Object object) {
            this.mInfo = object;
        }

        public static CollectionInfoCompat obtain(int n, int n2, boolean bl) {
            if (Build.VERSION.SDK_INT >= 19) {
                return new CollectionInfoCompat(AccessibilityNodeInfo.CollectionInfo.obtain((int)n, (int)n2, (boolean)bl));
            }
            return new CollectionInfoCompat(null);
        }

        public static CollectionInfoCompat obtain(int n, int n2, boolean bl, int n3) {
            if (Build.VERSION.SDK_INT >= 21) {
                return new CollectionInfoCompat(AccessibilityNodeInfo.CollectionInfo.obtain((int)n, (int)n2, (boolean)bl, (int)n3));
            }
            if (Build.VERSION.SDK_INT >= 19) {
                return new CollectionInfoCompat(AccessibilityNodeInfo.CollectionInfo.obtain((int)n, (int)n2, (boolean)bl));
            }
            return new CollectionInfoCompat(null);
        }

        public int getColumnCount() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.CollectionInfo)this.mInfo).getColumnCount();
            }
            return -1;
        }

        public int getRowCount() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.CollectionInfo)this.mInfo).getRowCount();
            }
            return -1;
        }

        public int getSelectionMode() {
            if (Build.VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo.CollectionInfo)this.mInfo).getSelectionMode();
            }
            return 0;
        }

        public boolean isHierarchical() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.CollectionInfo)this.mInfo).isHierarchical();
            }
            return false;
        }
    }

    public static class CollectionItemInfoCompat {
        final Object mInfo;

        CollectionItemInfoCompat(Object object) {
            this.mInfo = object;
        }

        public static CollectionItemInfoCompat obtain(int n, int n2, int n3, int n4, boolean bl) {
            if (Build.VERSION.SDK_INT >= 19) {
                return new CollectionItemInfoCompat(AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n, (int)n2, (int)n3, (int)n4, (boolean)bl));
            }
            return new CollectionItemInfoCompat(null);
        }

        public static CollectionItemInfoCompat obtain(int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
            if (Build.VERSION.SDK_INT >= 21) {
                return new CollectionItemInfoCompat(AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n, (int)n2, (int)n3, (int)n4, (boolean)bl, (boolean)bl2));
            }
            if (Build.VERSION.SDK_INT >= 19) {
                return new CollectionItemInfoCompat(AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n, (int)n2, (int)n3, (int)n4, (boolean)bl));
            }
            return new CollectionItemInfoCompat(null);
        }

        public int getColumnIndex() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.CollectionItemInfo)this.mInfo).getColumnIndex();
            }
            return 0;
        }

        public int getColumnSpan() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.CollectionItemInfo)this.mInfo).getColumnSpan();
            }
            return 0;
        }

        public int getRowIndex() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.CollectionItemInfo)this.mInfo).getRowIndex();
            }
            return 0;
        }

        public int getRowSpan() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.CollectionItemInfo)this.mInfo).getRowSpan();
            }
            return 0;
        }

        @Deprecated
        public boolean isHeading() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.CollectionItemInfo)this.mInfo).isHeading();
            }
            return false;
        }

        public boolean isSelected() {
            if (Build.VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo.CollectionItemInfo)this.mInfo).isSelected();
            }
            return false;
        }
    }

    public static class RangeInfoCompat {
        public static final int RANGE_TYPE_FLOAT = 1;
        public static final int RANGE_TYPE_INT = 0;
        public static final int RANGE_TYPE_PERCENT = 2;
        final Object mInfo;

        RangeInfoCompat(Object object) {
            this.mInfo = object;
        }

        public static RangeInfoCompat obtain(int n, float f, float f2, float f3) {
            if (Build.VERSION.SDK_INT >= 19) {
                return new RangeInfoCompat(AccessibilityNodeInfo.RangeInfo.obtain((int)n, (float)f, (float)f2, (float)f3));
            }
            return new RangeInfoCompat(null);
        }

        public float getCurrent() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.RangeInfo)this.mInfo).getCurrent();
            }
            return 0.0f;
        }

        public float getMax() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.RangeInfo)this.mInfo).getMax();
            }
            return 0.0f;
        }

        public float getMin() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.RangeInfo)this.mInfo).getMin();
            }
            return 0.0f;
        }

        public int getType() {
            if (Build.VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo.RangeInfo)this.mInfo).getType();
            }
            return 0;
        }
    }

    public static final class TouchDelegateInfoCompat {
        final AccessibilityNodeInfo.TouchDelegateInfo mInfo;

        TouchDelegateInfoCompat(AccessibilityNodeInfo.TouchDelegateInfo touchDelegateInfo) {
            this.mInfo = touchDelegateInfo;
        }

        public TouchDelegateInfoCompat(Map<Region, View> map) {
            this.mInfo = Build.VERSION.SDK_INT >= 29 ? new AccessibilityNodeInfo.TouchDelegateInfo(map) : null;
        }

        public Region getRegionAt(int n) {
            if (Build.VERSION.SDK_INT >= 29) {
                return this.mInfo.getRegionAt(n);
            }
            return null;
        }

        public int getRegionCount() {
            if (Build.VERSION.SDK_INT >= 29) {
                return this.mInfo.getRegionCount();
            }
            return 0;
        }

        public AccessibilityNodeInfoCompat getTargetForRegion(Region region) {
            if (Build.VERSION.SDK_INT >= 29 && (region = this.mInfo.getTargetForRegion(region)) != null) {
                return AccessibilityNodeInfoCompat.wrap((AccessibilityNodeInfo)region);
            }
            return null;
        }
    }
}

