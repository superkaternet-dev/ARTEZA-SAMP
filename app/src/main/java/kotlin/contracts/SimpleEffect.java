/*
 * Decompiled with CFR 0.152.
 */
package kotlin.contracts;

import kotlin.Metadata;
import kotlin.contracts.ConditionalEffect;
import kotlin.contracts.Effect;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bg\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7\u0004\u00a8\u0006\u0006"}, d2={"Lkotlin/contracts/SimpleEffect;", "Lkotlin/contracts/Effect;", "implies", "Lkotlin/contracts/ConditionalEffect;", "booleanExpression", "", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public interface SimpleEffect
extends Effect {
    public ConditionalEffect implies(boolean var1);
}

