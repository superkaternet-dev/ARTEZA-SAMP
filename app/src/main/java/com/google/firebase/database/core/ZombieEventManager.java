/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.EventRegistrationZombieListener;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.QuerySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ZombieEventManager
implements EventRegistrationZombieListener {
    private static ZombieEventManager defaultInstance = new ZombieEventManager();
    final HashMap<EventRegistration, List<EventRegistration>> globalEventRegistrations = new HashMap();

    private ZombieEventManager() {
    }

    public static ZombieEventManager getInstance() {
        return defaultInstance;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void unRecordEventRegistration(EventRegistration eventRegistration) {
        HashMap<EventRegistration, List<EventRegistration>> hashMap = this.globalEventRegistrations;
        synchronized (hashMap) {
            int n;
            int n2 = 0;
            int n3 = 0;
            Object object = this.globalEventRegistrations.get(eventRegistration);
            if (object != null) {
                n2 = 0;
                while (true) {
                    n = n3;
                    if (n2 >= object.size()) break;
                    if (object.get(n2) == eventRegistration) {
                        n = 1;
                        object.remove(n2);
                        break;
                    }
                    ++n2;
                }
                n2 = n;
                if (object.isEmpty()) {
                    this.globalEventRegistrations.remove(eventRegistration);
                    n2 = n;
                }
            }
            boolean bl = n2 != 0 || !eventRegistration.isUserInitiated();
            Utilities.hardAssert(bl);
            if (eventRegistration.getQuerySpec().isDefault()) return;
            object = eventRegistration.clone(QuerySpec.defaultQueryAtPath(eventRegistration.getQuerySpec().getPath()));
            List<EventRegistration> list = this.globalEventRegistrations.get(object);
            if (list == null) return;
            for (n = 0; n < list.size(); ++n) {
                if (list.get(n) != eventRegistration) continue;
                list.remove(n);
                break;
            }
            if (!list.isEmpty()) return;
            this.globalEventRegistrations.remove(object);
            return;
        }
    }

    @Override
    public void onZombied(EventRegistration eventRegistration) {
        this.unRecordEventRegistration(eventRegistration);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void recordEventRegistration(EventRegistration eventRegistration) {
        HashMap<EventRegistration, List<EventRegistration>> hashMap = this.globalEventRegistrations;
        synchronized (hashMap) {
            List<EventRegistration> list;
            ArrayList<EventRegistration> arrayList = list = this.globalEventRegistrations.get(eventRegistration);
            if (list == null) {
                arrayList = new ArrayList<EventRegistration>();
                this.globalEventRegistrations.put(eventRegistration, arrayList);
            }
            arrayList.add(eventRegistration);
            if (!eventRegistration.getQuerySpec().isDefault()) {
                EventRegistration eventRegistration2 = eventRegistration.clone(QuerySpec.defaultQueryAtPath(eventRegistration.getQuerySpec().getPath()));
                arrayList = list = this.globalEventRegistrations.get(eventRegistration2);
                if (list == null) {
                    arrayList = new ArrayList<EventRegistration>();
                    this.globalEventRegistrations.put(eventRegistration2, arrayList);
                }
                arrayList.add(eventRegistration);
            }
            eventRegistration.setIsUserInitiated(true);
            eventRegistration.setOnZombied(this);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void zombifyForRemove(EventRegistration hashSet) {
        HashMap<EventRegistration, List<EventRegistration>> hashMap = this.globalEventRegistrations;
        synchronized (hashMap) {
            List<EventRegistration> list = this.globalEventRegistrations.get(hashSet);
            if (list == null) return;
            if (list.isEmpty()) return;
            if (((EventRegistration)((Object)hashSet)).getQuerySpec().isDefault()) {
                hashSet = new HashSet<QuerySpec>();
                int n = list.size() - 1;
                while (n >= 0) {
                    EventRegistration eventRegistration = list.get(n);
                    if (!hashSet.contains(eventRegistration.getQuerySpec())) {
                        hashSet.add(eventRegistration.getQuerySpec());
                        eventRegistration.zombify();
                    }
                    --n;
                }
                return;
            }
            list.get(0).zombify();
            return;
        }
    }
}

