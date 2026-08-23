/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Build$VERSION
 */
package com.google.firebase.heartbeatinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.google.firebase.heartbeatinfo.HeartBeatResult;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class HeartBeatInfoStorage {
    private static final String GLOBAL = "fire-global";
    private static final String HEARTBEAT_PREFERENCES_NAME = "FirebaseHeartBeat";
    private static final int HEART_BEAT_COUNT_LIMIT = 30;
    private static final String HEART_BEAT_COUNT_TAG = "fire-count";
    private static final String LAST_STORED_DATE = "last-used-date";
    private static final String PREFERENCES_NAME = "FirebaseAppHeartBeat";
    private static HeartBeatInfoStorage instance = null;
    private final SharedPreferences firebaseSharedPreferences;

    public HeartBeatInfoStorage(Context context, String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HEARTBEAT_PREFERENCES_NAME);
        stringBuilder.append(string2);
        this.firebaseSharedPreferences = context.getSharedPreferences(stringBuilder.toString(), 0);
    }

    HeartBeatInfoStorage(SharedPreferences sharedPreferences) {
        this.firebaseSharedPreferences = sharedPreferences;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void cleanUpStoredHeartBeats() {
        synchronized (this) {
            long l = this.firebaseSharedPreferences.getLong(HEART_BEAT_COUNT_TAG, 0L);
            Object object = null;
            Object object2 = "";
            SharedPreferences sharedPreferences = this.firebaseSharedPreferences.getAll().entrySet().iterator();
            while (true) {
                Object object3;
                Object object4;
                block9: {
                    if (!sharedPreferences.hasNext()) {
                        sharedPreferences = this.firebaseSharedPreferences;
                        object4 = new HashSet();
                        object3 = new HashSet(sharedPreferences.getStringSet((String)object2, object4));
                        object3.remove(object);
                        this.firebaseSharedPreferences.edit().putStringSet((String)object2, object3).putLong(HEART_BEAT_COUNT_TAG, l - 1L).commit();
                        return;
                    }
                    Map.Entry entry = sharedPreferences.next();
                    object3 = object;
                    object4 = object2;
                    if (!(entry.getValue() instanceof Set)) break block9;
                    Iterator iterator2 = ((Set)entry.getValue()).iterator();
                    while (true) {
                        block11: {
                            block10: {
                                object3 = object;
                                object4 = object2;
                                if (!iterator2.hasNext()) break;
                                object3 = (String)iterator2.next();
                                if (object == null) break block10;
                                object4 = object;
                                if (((String)object).compareTo((String)object3) <= 0) break block11;
                            }
                            object4 = object3;
                            object2 = (String)entry.getKey();
                        }
                        object = object4;
                    }
                }
                object = object3;
                object2 = object4;
            }
        }
    }

    private String getFormattedDate(long l) {
        synchronized (this) {
            block4: {
                if (Build.VERSION.SDK_INT < 26) break block4;
                Object object = new Date(l);
                object = ((Date)object).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
                return object;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            Object object = new Date(l);
            object = simpleDateFormat.format((Date)object);
            return object;
        }
    }

    private String getStoredUserAgentString(String string2) {
        synchronized (this) {
            try {
                for (Map.Entry entry : this.firebaseSharedPreferences.getAll().entrySet()) {
                    if (!(entry.getValue() instanceof Set)) continue;
                    Iterator iterator2 = ((Set)entry.getValue()).iterator();
                    while (iterator2.hasNext()) {
                        if (!string2.equals((String)iterator2.next())) continue;
                        string2 = (String)entry.getKey();
                        return string2;
                    }
                }
            }
            catch (Throwable throwable) {}
            {
                throw throwable;
            }
            return null;
        }
    }

    private void removeStoredDate(String string2) {
        synchronized (this) {
            String string3;
            block6: {
                string3 = this.getStoredUserAgentString(string2);
                if (string3 != null) break block6;
                return;
            }
            SharedPreferences sharedPreferences = this.firebaseSharedPreferences;
            HashSet hashSet = new HashSet();
            HashSet hashSet2 = new HashSet(sharedPreferences.getStringSet(string3, hashSet));
            hashSet2.remove(string2);
            if (hashSet2.isEmpty()) {
                this.firebaseSharedPreferences.edit().remove(string3).commit();
            } else {
                this.firebaseSharedPreferences.edit().putStringSet(string3, hashSet2).commit();
            }
            return;
        }
    }

    void deleteAllHeartBeats() {
        synchronized (this) {
            try {
                SharedPreferences.Editor editor = this.firebaseSharedPreferences.edit();
                for (Map.Entry entry : this.firebaseSharedPreferences.getAll().entrySet()) {
                    if (!(entry.getValue() instanceof Set)) continue;
                    editor.remove((String)entry.getKey());
                }
                editor.remove(HEART_BEAT_COUNT_TAG);
                editor.commit();
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    List<HeartBeatResult> getAllHeartBeats() {
        synchronized (this) {
            try {
                ArrayList<HeartBeatResult> arrayList = new ArrayList<HeartBeatResult>();
                for (Map.Entry entry : this.firebaseSharedPreferences.getAll().entrySet()) {
                    if (!(entry.getValue() instanceof Set)) continue;
                    String string2 = (String)entry.getKey();
                    ArrayList<String> arrayList2 = new ArrayList<String>((Set)entry.getValue());
                    arrayList.add(HeartBeatResult.create(string2, arrayList2));
                }
                this.updateGlobalHeartBeat(System.currentTimeMillis());
                return arrayList;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    int getHeartBeatCount() {
        return (int)this.firebaseSharedPreferences.getLong(HEART_BEAT_COUNT_TAG, 0L);
    }

    long getLastGlobalHeartBeat() {
        synchronized (this) {
            long l = this.firebaseSharedPreferences.getLong(GLOBAL, -1L);
            return l;
        }
    }

    boolean isSameDateUtc(long l, long l2) {
        synchronized (this) {
            boolean bl = this.getFormattedDate(l).equals(this.getFormattedDate(l2));
            return bl;
        }
    }

    void postHeartBeatCleanUp() {
        synchronized (this) {
            String string2 = this.getFormattedDate(System.currentTimeMillis());
            this.firebaseSharedPreferences.edit().putString(LAST_STORED_DATE, string2).commit();
            this.removeStoredDate(string2);
            return;
        }
    }

    boolean shouldSendGlobalHeartBeat(long l) {
        synchronized (this) {
            boolean bl = this.shouldSendSdkHeartBeat(GLOBAL, l);
            return bl;
        }
    }

    boolean shouldSendSdkHeartBeat(String string2, long l) {
        synchronized (this) {
            if (this.firebaseSharedPreferences.contains(string2)) {
                if (!this.isSameDateUtc(this.firebaseSharedPreferences.getLong(string2, -1L), l)) {
                    this.firebaseSharedPreferences.edit().putLong(string2, l).commit();
                    return true;
                }
                return false;
            }
            this.firebaseSharedPreferences.edit().putLong(string2, l).commit();
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void storeHeartBeat(long l, String string2) {
        synchronized (this) {
            long l2;
            String string3 = this.getFormattedDate(l);
            boolean bl = this.firebaseSharedPreferences.getString(LAST_STORED_DATE, "").equals(string3);
            if (bl) {
                return;
            }
            l = l2 = this.firebaseSharedPreferences.getLong(HEART_BEAT_COUNT_TAG, 0L);
            if (l2 + 1L == 30L) {
                this.cleanUpStoredHeartBeats();
                l = this.firebaseSharedPreferences.getLong(HEART_BEAT_COUNT_TAG, 0L);
            }
            SharedPreferences sharedPreferences = this.firebaseSharedPreferences;
            HashSet hashSet = new HashSet();
            HashSet<String> hashSet2 = new HashSet<String>(sharedPreferences.getStringSet(string2, hashSet));
            hashSet2.add(string3);
            this.firebaseSharedPreferences.edit().putStringSet(string2, hashSet2).putLong(HEART_BEAT_COUNT_TAG, l + 1L).putString(LAST_STORED_DATE, string3).commit();
            return;
        }
    }

    void updateGlobalHeartBeat(long l) {
        synchronized (this) {
            this.firebaseSharedPreferences.edit().putLong(GLOBAL, l).commit();
            return;
        }
    }
}

