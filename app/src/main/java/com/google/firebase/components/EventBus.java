/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.EventBus$$ExternalSyntheticLambda0;
import com.google.firebase.components.Preconditions;
import com.google.firebase.events.Event;
import com.google.firebase.events.EventHandler;
import com.google.firebase.events.Publisher;
import com.google.firebase.events.Subscriber;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

class EventBus
implements Subscriber,
Publisher {
    private final Executor defaultExecutor;
    private final Map<Class<?>, ConcurrentHashMap<EventHandler<Object>, Executor>> handlerMap = new HashMap();
    private Queue<Event<?>> pendingEvents = new ArrayDeque();

    EventBus(Executor executor) {
        this.defaultExecutor = executor;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Set<Map.Entry<EventHandler<Object>, Executor>> getHandlers(Event<?> set) {
        synchronized (this) {
            set = this.handlerMap.get(((Event)((Object)set)).getType());
            if (set != null) return set.entrySet();
            return Collections.emptySet();
        }
    }

    static /* synthetic */ void lambda$publish$0(Map.Entry entry, Event event) {
        ((EventHandler)entry.getKey()).handle(event);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void enablePublishingAndFlushPending() {
        Object object = null;
        synchronized (this) {
            Queue<Event<?>> queue = this.pendingEvents;
            if (queue != null) {
                object = queue;
                this.pendingEvents = null;
            }
            // MONITOREXIT @DISABLED, blocks:[0, 2] lbl8 : MonitorExitStatement: MONITOREXIT : this
            if (object == null) return;
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
        object = object.iterator();
        while (object.hasNext()) {
            this.publish((Event)object.next());
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void publish(Event<?> event) {
        Iterator<Map.Entry<EventHandler<Object>, Executor>> iterator2;
        Object object;
        Preconditions.checkNotNull(event);
        synchronized (this) {
            object = this.pendingEvents;
            if (object != null) {
                object.add(event);
                return;
            }
            // MONITOREXIT @DISABLED, blocks:[0, 2] lbl11 : MonitorExitStatement: MONITOREXIT : this
            iterator2 = this.getHandlers(event).iterator();
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
        while (iterator2.hasNext()) {
            object = iterator2.next();
            ((Executor)object.getValue()).execute(new EventBus$$ExternalSyntheticLambda0((Map.Entry)object, event));
        }
    }

    @Override
    public <T> void subscribe(Class<T> clazz, EventHandler<? super T> eventHandler) {
        this.subscribe(clazz, this.defaultExecutor, eventHandler);
    }

    @Override
    public <T> void subscribe(Class<T> clazz, Executor executor, EventHandler<? super T> eventHandler) {
        synchronized (this) {
            Preconditions.checkNotNull(clazz);
            Preconditions.checkNotNull(eventHandler);
            Preconditions.checkNotNull(executor);
            if (!this.handlerMap.containsKey(clazz)) {
                Map<Class<?>, ConcurrentHashMap<EventHandler<Object>, Executor>> map = this.handlerMap;
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                map.put(clazz, concurrentHashMap);
            }
            this.handlerMap.get(clazz).put(eventHandler, executor);
            return;
        }
    }

    @Override
    public <T> void unsubscribe(Class<T> clazz, EventHandler<? super T> eventHandler) {
        synchronized (this) {
            block5: {
                Preconditions.checkNotNull(clazz);
                Preconditions.checkNotNull(eventHandler);
                boolean bl = this.handlerMap.containsKey(clazz);
                if (bl) break block5;
                return;
            }
            ConcurrentHashMap<EventHandler<Object>, Executor> concurrentHashMap = this.handlerMap.get(clazz);
            concurrentHashMap.remove(eventHandler);
            if (concurrentHashMap.isEmpty()) {
                this.handlerMap.remove(clazz);
            }
            return;
        }
    }
}

