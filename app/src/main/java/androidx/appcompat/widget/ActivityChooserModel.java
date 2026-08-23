/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ResolveInfo
 *  android.database.DataSetObservable
 *  android.os.AsyncTask
 *  android.text.TextUtils
 *  android.util.Log
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParserException
 *  org.xmlpull.v1.XmlSerializer
 */
package androidx.appcompat.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

class ActivityChooserModel
extends DataSetObservable {
    static final String ATTRIBUTE_ACTIVITY = "activity";
    static final String ATTRIBUTE_TIME = "time";
    static final String ATTRIBUTE_WEIGHT = "weight";
    static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities;
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    boolean mCanReadHistoricalData = true;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords;
    private boolean mHistoricalRecordsChanged = true;
    final String mHistoryFileName;
    private int mHistoryMaxSize = 50;
    private final Object mInstanceLock = new Object();
    private Intent mIntent;
    private boolean mReadShareHistoryCalled = false;
    private boolean mReloadActivities = false;

    static {
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }

    private ActivityChooserModel(Context object, String string2) {
        this.mActivities = new ArrayList<ActivityResolveInfo>();
        this.mHistoricalRecords = new ArrayList<HistoricalRecord>();
        this.mActivitySorter = new DefaultSorter();
        this.mContext = object.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)string2) && !string2.endsWith(HISTORY_FILE_EXTENSION)) {
            object = new StringBuilder();
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(HISTORY_FILE_EXTENSION);
            this.mHistoryFileName = ((StringBuilder)object).toString();
        } else {
            this.mHistoryFileName = string2;
        }
    }

    private boolean addHistoricalRecord(HistoricalRecord historicalRecord) {
        boolean bl = this.mHistoricalRecords.add(historicalRecord);
        if (bl) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return bl;
    }

    private void ensureConsistentState() {
        boolean bl = this.loadActivitiesIfNeeded();
        boolean bl2 = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (bl | bl2) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ActivityChooserModel get(Context context, String string2) {
        Object object = sRegistryLock;
        synchronized (object) {
            ActivityChooserModel activityChooserModel;
            Map<String, ActivityChooserModel> map = sDataModelRegistry;
            ActivityChooserModel activityChooserModel2 = activityChooserModel = map.get(string2);
            if (activityChooserModel == null) {
                activityChooserModel2 = new ActivityChooserModel(context, string2);
                map.put(string2, activityChooserModel2);
            }
            return activityChooserModel2;
        }
    }

    private boolean loadActivitiesIfNeeded() {
        if (this.mReloadActivities && this.mIntent != null) {
            this.mReloadActivities = false;
            this.mActivities.clear();
            List list = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                ResolveInfo resolveInfo = (ResolveInfo)list.get(i);
                this.mActivities.add(new ActivityResolveInfo(resolveInfo));
            }
            return true;
        }
        return false;
    }

    private void persistHistoricalDataIfNeeded() {
        if (this.mReadShareHistoryCalled) {
            if (!this.mHistoricalRecordsChanged) {
                return;
            }
            this.mHistoricalRecordsChanged = false;
            if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
                new PersistHistoryAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[]{new ArrayList<HistoricalRecord>(this.mHistoricalRecords), this.mHistoryFileName});
            }
            return;
        }
        throw new IllegalStateException("No preceding call to #readHistoricalData");
    }

    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n <= 0) {
            return;
        }
        this.mHistoricalRecordsChanged = true;
        for (int i = 0; i < n; ++i) {
            HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0);
        }
    }

    private boolean readHistoricalDataIfNeeded() {
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void readHistoricalDataImpl() {
        Throwable throwable222222;
        Object object;
        int n;
        Object object2;
        FileInputStream fileInputStream;
        block20: {
            fileInputStream = this.mContext.openFileInput(this.mHistoryFileName);
            object2 = Xml.newPullParser();
            object2.setInput((InputStream)fileInputStream, "UTF-8");
            n = 0;
            while (n != 1 && n != 2) {
                n = object2.next();
            }
            if (!TAG_HISTORICAL_RECORDS.equals(object2.getName())) {
                object2 = new XmlPullParserException("Share records file does not start with historical-records tag.");
                throw object2;
            }
            break block20;
            catch (FileNotFoundException fileNotFoundException) {
                return;
            }
        }
        Object object3 = this.mHistoricalRecords;
        object3.clear();
        while (true) {
            if ((n = object2.next()) == 1) {
                if (fileInputStream == null) return;
                fileInputStream.close();
                return;
            }
            if (n == 3 || n == 4) continue;
            if (!TAG_HISTORICAL_RECORD.equals(object2.getName())) {
                object2 = new XmlPullParserException("Share records file not well-formed.");
                throw object2;
            }
            String string2 = object2.getAttributeValue(null, ATTRIBUTE_ACTIVITY);
            long l = Long.parseLong(object2.getAttributeValue(null, ATTRIBUTE_TIME));
            float f = Float.parseFloat(object2.getAttributeValue(null, ATTRIBUTE_WEIGHT));
            object = new HistoricalRecord(string2, l, f);
            object3.add(object);
        }
        {
            catch (Throwable throwable222222) {
            }
            catch (IOException iOException) {
                object = LOG_TAG;
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("Error reading historical recrod file: ");
                ((StringBuilder)object3).append(this.mHistoryFileName);
                Log.e((String)object, (String)((StringBuilder)object3).toString(), (Throwable)iOException);
                if (fileInputStream == null) return;
                try {
                    fileInputStream.close();
                    return;
                }
                catch (IOException iOException2) {
                    return;
                }
                catch (XmlPullParserException xmlPullParserException) {}
                {
                    object = LOG_TAG;
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append("Error reading historical recrod file: ");
                    ((StringBuilder)object3).append(this.mHistoryFileName);
                    Log.e((String)object, (String)((StringBuilder)object3).toString(), (Throwable)xmlPullParserException);
                    if (fileInputStream == null) return;
                }
                fileInputStream.close();
                return;
            }
        }
        if (fileInputStream == null) throw throwable222222;
        try {
            fileInputStream.close();
            throw throwable222222;
        }
        catch (IOException iOException) {
            // empty catch block
        }
        throw throwable222222;
    }

    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Intent chooseActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            Object object2;
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            ComponentName componentName = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            activityResolveInfo = new Intent(this.mIntent);
            activityResolveInfo.setComponent(componentName);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, (Intent)(object2 = new Intent((Intent)activityResolveInfo)))) {
                return null;
            }
            object2 = new HistoricalRecord(componentName, System.currentTimeMillis(), 1.0f);
            this.addHistoricalRecord((HistoricalRecord)object2);
            return activityResolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ResolveInfo getActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mActivities.get((int)n).resolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getActivityCount() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getActivityIndex(ResolveInfo resolveInfo) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            List<ActivityResolveInfo> list = this.mActivities;
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                if (list.get((int)i).resolveInfo != resolveInfo) continue;
                return i;
            }
            return -1;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ResolveInfo getDefaultActivity() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            if (this.mActivities.isEmpty()) return null;
            return this.mActivities.get((int)0).resolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getHistoryMaxSize() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            return this.mHistoryMaxSize;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getHistorySize() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Intent getIntent() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            return this.mIntent;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setActivitySorter(ActivitySorter activitySorter) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mActivitySorter == activitySorter) {
                return;
            }
            this.mActivitySorter = activitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setDefaultActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            Object object2 = this.mActivities.get(n);
            ActivityResolveInfo activityResolveInfo = this.mActivities.get(0);
            float f = activityResolveInfo != null ? activityResolveInfo.weight - ((ActivityResolveInfo)object2).weight + 5.0f : 1.0f;
            activityResolveInfo = new ComponentName(((ActivityResolveInfo)object2).resolveInfo.activityInfo.packageName, ((ActivityResolveInfo)object2).resolveInfo.activityInfo.name);
            object2 = new HistoricalRecord((ComponentName)activityResolveInfo, System.currentTimeMillis(), f);
            this.addHistoricalRecord((HistoricalRecord)object2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setHistoryMaxSize(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mHistoryMaxSize == n) {
                return;
            }
            this.mHistoryMaxSize = n;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setIntent(Intent intent) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mIntent == intent) {
                return;
            }
            this.mIntent = intent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setOnChooseActivityListener(OnChooseActivityListener onChooseActivityListener) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.mActivityChoserModelPolicy = onChooseActivityListener;
            return;
        }
    }

    public static interface ActivityChooserModelClient {
        public void setActivityChooserModel(ActivityChooserModel var1);
    }

    public static final class ActivityResolveInfo
    implements Comparable<ActivityResolveInfo> {
        public final ResolveInfo resolveInfo;
        public float weight;

        public ActivityResolveInfo(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }

        @Override
        public int compareTo(ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (ActivityResolveInfo)object;
            return Float.floatToIntBits(this.weight) == Float.floatToIntBits(((ActivityResolveInfo)object).weight);
        }

        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append("resolveInfo:");
            stringBuilder.append(this.resolveInfo.toString());
            stringBuilder.append("; weight:");
            stringBuilder.append(new BigDecimal(this.weight));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static interface ActivitySorter {
        public void sort(Intent var1, List<ActivityResolveInfo> var2, List<HistoricalRecord> var3);
    }

    private static final class DefaultSorter
    implements ActivitySorter {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();

        DefaultSorter() {
        }

        @Override
        public void sort(Intent object, List<ActivityResolveInfo> list, List<HistoricalRecord> list2) {
            ActivityResolveInfo activityResolveInfo;
            int n;
            object = this.mPackageNameToActivityMap;
            object.clear();
            int n2 = list.size();
            for (n = 0; n < n2; ++n) {
                activityResolveInfo = list.get(n);
                activityResolveInfo.weight = 0.0f;
                object.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
            }
            n = list2.size();
            float f = 1.0f;
            --n;
            while (n >= 0) {
                HistoricalRecord historicalRecord = list2.get(n);
                activityResolveInfo = (ActivityResolveInfo)object.get(historicalRecord.activity);
                float f2 = f;
                if (activityResolveInfo != null) {
                    activityResolveInfo.weight += historicalRecord.weight * f;
                    f2 = f * 0.95f;
                }
                --n;
                f = f2;
            }
            Collections.sort(list);
        }
    }

    public static final class HistoricalRecord {
        public final ComponentName activity;
        public final long time;
        public final float weight;

        public HistoricalRecord(ComponentName componentName, long l, float f) {
            this.activity = componentName;
            this.time = l;
            this.weight = f;
        }

        public HistoricalRecord(String string2, long l, float f) {
            this(ComponentName.unflattenFromString((String)string2), l, f);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (HistoricalRecord)object;
            ComponentName componentName = this.activity;
            if (componentName == null ? ((HistoricalRecord)object).activity != null : !componentName.equals((Object)((HistoricalRecord)object).activity)) {
                return false;
            }
            if (this.time != ((HistoricalRecord)object).time) {
                return false;
            }
            return Float.floatToIntBits(this.weight) == Float.floatToIntBits(((HistoricalRecord)object).weight);
        }

        public int hashCode() {
            ComponentName componentName = this.activity;
            int n = componentName == null ? 0 : componentName.hashCode();
            long l = this.time;
            return ((1 * 31 + n) * 31 + (int)(l ^ l >>> 32)) * 31 + Float.floatToIntBits(this.weight);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append("; activity:");
            stringBuilder.append(this.activity);
            stringBuilder.append("; time:");
            stringBuilder.append(this.time);
            stringBuilder.append("; weight:");
            stringBuilder.append(new BigDecimal(this.weight));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static interface OnChooseActivityListener {
        public boolean onChooseActivity(ActivityChooserModel var1, Intent var2);
    }

    private final class PersistHistoryAsyncTask
    extends AsyncTask<Object, Void, Void> {
        final ActivityChooserModel this$0;

        PersistHistoryAsyncTask(ActivityChooserModel activityChooserModel) {
            this.this$0 = activityChooserModel;
        }

        /*
         * Loose catch block
         * WARNING - void declaration
         */
        public Void doInBackground(Object ... object) {
            void var1_11;
            FileOutputStream fileOutputStream;
            block34: {
                block35: {
                    Object object2;
                    Object object3;
                    block33: {
                        block32: {
                            block31: {
                                object3 = (List)object[0];
                                object = (String)object[1];
                                fileOutputStream = this.this$0.mContext.openFileOutput((String)object, 0);
                                XmlSerializer xmlSerializer = Xml.newSerializer();
                                object2 = object3;
                                object2 = object3;
                                object2 = object3;
                                object2 = object3;
                                xmlSerializer.setOutput((OutputStream)fileOutputStream, null);
                                object2 = object3;
                                object2 = object3;
                                object2 = object3;
                                object2 = object3;
                                xmlSerializer.startDocument("UTF-8", Boolean.valueOf(true));
                                object2 = object3;
                                object2 = object3;
                                object2 = object3;
                                object2 = object3;
                                xmlSerializer.startTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORDS);
                                object2 = object3;
                                object2 = object3;
                                object2 = object3;
                                object2 = object3;
                                int n = object3.size();
                                object = object3;
                                for (int i = 0; i < n; ++i) {
                                    object2 = object;
                                    object2 = object;
                                    object2 = object;
                                    object2 = object;
                                    object3 = (HistoricalRecord)object.remove(0);
                                    object2 = object;
                                    object2 = object;
                                    object2 = object;
                                    object2 = object;
                                    xmlSerializer.startTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORD);
                                    object2 = object;
                                    object2 = object;
                                    object2 = object;
                                    object2 = object;
                                    xmlSerializer.attribute(null, ActivityChooserModel.ATTRIBUTE_ACTIVITY, ((HistoricalRecord)object3).activity.flattenToString());
                                    xmlSerializer.attribute(null, ActivityChooserModel.ATTRIBUTE_TIME, String.valueOf(((HistoricalRecord)object3).time));
                                    xmlSerializer.attribute(null, ActivityChooserModel.ATTRIBUTE_WEIGHT, String.valueOf(((HistoricalRecord)object3).weight));
                                    xmlSerializer.endTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORD);
                                    continue;
                                }
                                xmlSerializer.endTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORDS);
                                xmlSerializer.endDocument();
                                this.this$0.mCanReadHistoricalData = true;
                                if (fileOutputStream == null) break block35;
                                try {
                                    fileOutputStream.close();
                                }
                                catch (IOException iOException) {}
                                catch (IOException iOException) {
                                    break block31;
                                }
                                catch (IllegalStateException illegalStateException) {
                                    break block32;
                                }
                                catch (IllegalArgumentException illegalArgumentException) {
                                    break block33;
                                }
                                catch (Throwable throwable) {
                                    break block34;
                                }
                                catch (IOException iOException) {
                                    // empty catch block
                                }
                            }
                            object3 = LOG_TAG;
                            object2 = new StringBuilder();
                            ((StringBuilder)object2).append("Error writing historical record file: ");
                            ((StringBuilder)object2).append(this.this$0.mHistoryFileName);
                            Log.e((String)object3, (String)((StringBuilder)object2).toString(), (Throwable)object);
                            this.this$0.mCanReadHistoricalData = true;
                            if (fileOutputStream == null) break block35;
                            fileOutputStream.close();
                            break block35;
                            catch (IllegalStateException illegalStateException) {
                                // empty catch block
                            }
                        }
                        object3 = LOG_TAG;
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Error writing historical record file: ");
                        ((StringBuilder)object2).append(this.this$0.mHistoryFileName);
                        Log.e((String)object3, (String)((StringBuilder)object2).toString(), (Throwable)object);
                        this.this$0.mCanReadHistoricalData = true;
                        if (fileOutputStream == null) break block35;
                        fileOutputStream.close();
                        break block35;
                        catch (IllegalArgumentException illegalArgumentException) {
                            // empty catch block
                        }
                    }
                    object2 = LOG_TAG;
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append("Error writing historical record file: ");
                    ((StringBuilder)object3).append(this.this$0.mHistoryFileName);
                    Log.e((String)object2, (String)((StringBuilder)object3).toString(), (Throwable)object);
                    this.this$0.mCanReadHistoricalData = true;
                    if (fileOutputStream == null) break block35;
                    fileOutputStream.close();
                }
                return null;
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            this.this$0.mCanReadHistoricalData = true;
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
            throw var1_11;
            catch (FileNotFoundException fileNotFoundException) {
                String string2 = LOG_TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error writing historical record file: ");
                stringBuilder.append((String)object);
                Log.e((String)string2, (String)stringBuilder.toString(), (Throwable)fileNotFoundException);
                return null;
            }
        }
    }
}

