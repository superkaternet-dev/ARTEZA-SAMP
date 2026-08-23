/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2={"Lkotlin/io/OnErrorAction;", "", "(Ljava/lang/String;I)V", "SKIP", "TERMINATE", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class OnErrorAction
extends Enum<OnErrorAction> {
    private static final OnErrorAction[] $VALUES;
    public static final /* enum */ OnErrorAction SKIP;
    public static final /* enum */ OnErrorAction TERMINATE;

    static {
        OnErrorAction onErrorAction;
        OnErrorAction onErrorAction2;
        SKIP = onErrorAction2 = new OnErrorAction();
        TERMINATE = onErrorAction = new OnErrorAction();
        $VALUES = new OnErrorAction[]{onErrorAction2, onErrorAction};
    }

    public static OnErrorAction valueOf(String string2) {
        return Enum.valueOf(OnErrorAction.class, string2);
    }

    public static OnErrorAction[] values() {
        return (OnErrorAction[])$VALUES.clone();
    }
}

