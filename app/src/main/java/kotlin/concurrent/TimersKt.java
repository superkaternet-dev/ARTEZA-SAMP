/*
 * Decompiled with CFR 0.152.
 */
package kotlin.concurrent;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

@Metadata(bv={1, 0, 3}, d1={"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001aM\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001aO\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a\u001a\u0010\u0010\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001aM\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001aO\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a'\u0010\u0011\u001a\u00020\f2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a3\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a3\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u0016"}, d2={"fixedRateTimer", "Ljava/util/Timer;", "name", "", "daemon", "", "startAt", "Ljava/util/Date;", "period", "", "action", "Lkotlin/Function1;", "Ljava/util/TimerTask;", "", "Lkotlin/ExtensionFunctionType;", "initialDelay", "timer", "timerTask", "schedule", "time", "delay", "scheduleAtFixedRate", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class TimersKt {
    private static final Timer fixedRateTimer(String object, boolean bl, long l, long l2, Function1<? super TimerTask, Unit> function1) {
        object = TimersKt.timer((String)object, bl);
        ((Timer)object).scheduleAtFixedRate(new TimerTask(function1){
            final Function1 $action;
            {
                this.$action = function1;
            }

            public void run() {
                this.$action.invoke(this);
            }
        }, l, l2);
        return object;
    }

    private static final Timer fixedRateTimer(String object, boolean bl, Date date, long l, Function1<? super TimerTask, Unit> function1) {
        object = TimersKt.timer((String)object, bl);
        ((Timer)object).scheduleAtFixedRate(new /* invalid duplicate definition of identical inner class */, date, l);
        return object;
    }

    static /* synthetic */ Timer fixedRateTimer$default(String object, boolean bl, long l, long l2, Function1 function1, int n, Object object2) {
        if ((n & 1) != 0) {
            object = null;
        }
        if ((n & 2) != 0) {
            bl = false;
        }
        if ((n & 4) != 0) {
            l = 0L;
        }
        object = TimersKt.timer((String)object, bl);
        ((Timer)object).scheduleAtFixedRate(new /* invalid duplicate definition of identical inner class */, l, l2);
        return object;
    }

    static /* synthetic */ Timer fixedRateTimer$default(String object, boolean bl, Date date, long l, Function1 function1, int n, Object object2) {
        if ((n & 1) != 0) {
            object = null;
        }
        if ((n & 2) != 0) {
            bl = false;
        }
        object = TimersKt.timer((String)object, bl);
        ((Timer)object).scheduleAtFixedRate(new /* invalid duplicate definition of identical inner class */, date, l);
        return object;
    }

    private static final TimerTask schedule(Timer timer, long l, long l2, Function1<? super TimerTask, Unit> object) {
        object = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object, l, l2);
        return object;
    }

    private static final TimerTask schedule(Timer timer, long l, Function1<? super TimerTask, Unit> object) {
        object = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object, l);
        return object;
    }

    private static final TimerTask schedule(Timer timer, Date date, long l, Function1<? super TimerTask, Unit> object) {
        object = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object, date, l);
        return object;
    }

    private static final TimerTask schedule(Timer timer, Date date, Function1<? super TimerTask, Unit> object) {
        object = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object, date);
        return object;
    }

    private static final TimerTask scheduleAtFixedRate(Timer timer, long l, long l2, Function1<? super TimerTask, Unit> object) {
        object = new /* invalid duplicate definition of identical inner class */;
        timer.scheduleAtFixedRate((TimerTask)object, l, l2);
        return object;
    }

    private static final TimerTask scheduleAtFixedRate(Timer timer, Date date, long l, Function1<? super TimerTask, Unit> object) {
        object = new /* invalid duplicate definition of identical inner class */;
        timer.scheduleAtFixedRate((TimerTask)object, date, l);
        return object;
    }

    public static final Timer timer(String string2, boolean bl) {
        Timer timer = string2 == null ? new Timer(bl) : new Timer(string2, bl);
        return timer;
    }

    private static final Timer timer(String object, boolean bl, long l, long l2, Function1<? super TimerTask, Unit> function1) {
        object = TimersKt.timer((String)object, bl);
        ((Timer)object).schedule(new /* invalid duplicate definition of identical inner class */, l, l2);
        return object;
    }

    private static final Timer timer(String object, boolean bl, Date date, long l, Function1<? super TimerTask, Unit> function1) {
        object = TimersKt.timer((String)object, bl);
        ((Timer)object).schedule(new /* invalid duplicate definition of identical inner class */, date, l);
        return object;
    }

    static /* synthetic */ Timer timer$default(String object, boolean bl, long l, long l2, Function1 function1, int n, Object object2) {
        if ((n & 1) != 0) {
            object = null;
        }
        if ((n & 2) != 0) {
            bl = false;
        }
        if ((n & 4) != 0) {
            l = 0L;
        }
        object = TimersKt.timer((String)object, bl);
        ((Timer)object).schedule(new /* invalid duplicate definition of identical inner class */, l, l2);
        return object;
    }

    static /* synthetic */ Timer timer$default(String object, boolean bl, Date date, long l, Function1 function1, int n, Object object2) {
        if ((n & 1) != 0) {
            object = null;
        }
        if ((n & 2) != 0) {
            bl = false;
        }
        object = TimersKt.timer((String)object, bl);
        ((Timer)object).schedule(new /* invalid duplicate definition of identical inner class */, date, l);
        return object;
    }

    private static final TimerTask timerTask(Function1<? super TimerTask, Unit> function1) {
        return new /* invalid duplicate definition of identical inner class */;
    }
}

