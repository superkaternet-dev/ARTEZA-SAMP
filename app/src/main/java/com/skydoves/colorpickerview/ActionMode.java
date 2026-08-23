/*
 * Decompiled with CFR 0.152.
 */
package com.skydoves.colorpickerview;

public final class ActionMode
extends Enum<ActionMode> {
    private static final ActionMode[] $VALUES;
    public static final /* enum */ ActionMode ALWAYS;
    public static final /* enum */ ActionMode LAST;

    static {
        ActionMode actionMode;
        ActionMode actionMode2;
        ALWAYS = actionMode2 = new ActionMode();
        LAST = actionMode = new ActionMode();
        $VALUES = new ActionMode[]{actionMode2, actionMode};
    }

    public static ActionMode valueOf(String string2) {
        return Enum.valueOf(ActionMode.class, string2);
    }

    public static ActionMode[] values() {
        return (ActionMode[])$VALUES.clone();
    }
}

