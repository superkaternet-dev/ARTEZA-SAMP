/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Handler
 *  android.os.Looper
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.google.firebase;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.core.os.UserManagerCompat;
import com.google.android.gms.common.api.internal.BackgroundDetector;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.firebase.FirebaseApp$$ExternalSyntheticLambda0;
import com.google.firebase.FirebaseApp$$ExternalSyntheticLambda1;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.FirebaseCommonRegistrar;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentDiscovery;
import com.google.firebase.components.ComponentDiscoveryService;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.ComponentRuntime;
import com.google.firebase.components.Lazy;
import com.google.firebase.events.Publisher;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatController;
import com.google.firebase.inject.Provider;
import com.google.firebase.internal.DataCollectionConfigStorage;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class FirebaseApp {
    public static final String DEFAULT_APP_NAME = "[DEFAULT]";
    private static final String FIREBASE_ANDROID = "fire-android";
    private static final String FIREBASE_COMMON = "fire-core";
    static final Map<String, FirebaseApp> INSTANCES;
    private static final String KOTLIN = "kotlin";
    private static final Object LOCK;
    private static final String LOG_TAG = "FirebaseApp";
    private static final Executor UI_EXECUTOR;
    private final Context applicationContext;
    private final AtomicBoolean automaticResourceManagementEnabled = new AtomicBoolean(false);
    private final List<BackgroundStateChangeListener> backgroundStateChangeListeners;
    private final ComponentRuntime componentRuntime;
    private final Lazy<DataCollectionConfigStorage> dataCollectionConfigStorage;
    private final Provider<DefaultHeartBeatController> defaultHeartBeatController;
    private final AtomicBoolean deleted = new AtomicBoolean();
    private final List<FirebaseAppLifecycleListener> lifecycleListeners;
    private final String name;
    private final FirebaseOptions options;

    static {
        LOCK = new Object();
        UI_EXECUTOR = new UiExecutor();
        INSTANCES = new ArrayMap<String, FirebaseApp>();
    }

    protected FirebaseApp(Context context, String object, FirebaseOptions firebaseOptions) {
        this.backgroundStateChangeListeners = new CopyOnWriteArrayList<BackgroundStateChangeListener>();
        this.lifecycleListeners = new CopyOnWriteArrayList<FirebaseAppLifecycleListener>();
        this.applicationContext = Preconditions.checkNotNull(context);
        this.name = Preconditions.checkNotEmpty((String)object);
        this.options = Preconditions.checkNotNull(firebaseOptions);
        object = ComponentDiscovery.forContext(context, ComponentDiscoveryService.class).discoverLazy();
        this.componentRuntime = object = ComponentRuntime.builder(UI_EXECUTOR).addLazyComponentRegistrars((Collection<Provider<ComponentRegistrar>>)object).addComponentRegistrar(new FirebaseCommonRegistrar()).addComponent(Component.of(context, Context.class, new Class[0])).addComponent(Component.of(this, FirebaseApp.class, new Class[0])).addComponent(Component.of(firebaseOptions, FirebaseOptions.class, new Class[0])).build();
        this.dataCollectionConfigStorage = new Lazy<FirebaseApp$$ExternalSyntheticLambda1>(new FirebaseApp$$ExternalSyntheticLambda1(this, context));
        this.defaultHeartBeatController = ((ComponentRuntime)object).getProvider(DefaultHeartBeatController.class);
        this.addBackgroundStateChangeListener(new FirebaseApp$$ExternalSyntheticLambda0(this));
    }

    private void checkNotDeleted() {
        Preconditions.checkState(this.deleted.get() ^ true, "FirebaseApp was deleted");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void clearInstancesForTest() {
        Object object = LOCK;
        synchronized (object) {
            INSTANCES.clear();
            return;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<String> getAllAppNames() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Object object = LOCK;
        synchronized (object) {
            Iterator<FirebaseApp> iterator2 = INSTANCES.values().iterator();
            while (iterator2.hasNext()) {
                arrayList.add(iterator2.next().getName());
            }
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<FirebaseApp> getApps(Context object) {
        object = LOCK;
        synchronized (object) {
            return new ArrayList<FirebaseApp>(INSTANCES.values());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static FirebaseApp getInstance() {
        Object object = LOCK;
        synchronized (object) {
            Object object2 = INSTANCES.get(DEFAULT_APP_NAME);
            if (object2 != null) {
                return object2;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Default FirebaseApp is not initialized in this process ");
            stringBuilder.append(ProcessUtils.getMyProcessName());
            stringBuilder.append(". Make sure to call FirebaseApp.initializeApp(Context) first.");
            object2 = new IllegalStateException(stringBuilder.toString());
            throw object2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static FirebaseApp getInstance(String string2) {
        Object object = LOCK;
        synchronized (object) {
            List<String> list = INSTANCES.get(FirebaseApp.normalize(string2));
            if (list != null) {
                ((FirebaseApp)((Object)list)).defaultHeartBeatController.get().registerHeartBeat();
                return list;
            }
            list = FirebaseApp.getAllAppNames();
            if (list.isEmpty()) {
                list = "";
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Available app names: ");
                stringBuilder.append(TextUtils.join((CharSequence)", ", (Iterable)list));
                list = stringBuilder.toString();
            }
            string2 = String.format("FirebaseApp with name %s doesn't exist. %s", string2, list);
            list = new IllegalStateException(string2);
            throw list;
        }
    }

    public static String getPersistenceKey(String string2, FirebaseOptions firebaseOptions) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Base64Utils.encodeUrlSafeNoPadding(string2.getBytes(Charset.defaultCharset())));
        stringBuilder.append("+");
        stringBuilder.append(Base64Utils.encodeUrlSafeNoPadding(firebaseOptions.getApplicationId().getBytes(Charset.defaultCharset())));
        return stringBuilder.toString();
    }

    private void initializeAllApis() {
        if (UserManagerCompat.isUserUnlocked(this.applicationContext) ^ true) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Device in Direct Boot Mode: postponing initialization of Firebase APIs for app ");
            stringBuilder.append(this.getName());
            Log.i((String)LOG_TAG, (String)stringBuilder.toString());
            UserUnlockReceiver.ensureReceiverRegistered(this.applicationContext);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Device unlocked: initializing all Firebase APIs for app ");
            stringBuilder.append(this.getName());
            Log.i((String)LOG_TAG, (String)stringBuilder.toString());
            this.componentRuntime.initializeEagerComponents(this.isDefaultApp());
            this.defaultHeartBeatController.get().registerHeartBeat();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static FirebaseApp initializeApp(Context object) {
        Object object2 = LOCK;
        synchronized (object2) {
            if (INSTANCES.containsKey(DEFAULT_APP_NAME)) {
                return FirebaseApp.getInstance();
            }
            FirebaseOptions firebaseOptions = FirebaseOptions.fromResource(object);
            if (firebaseOptions != null) return FirebaseApp.initializeApp(object, firebaseOptions);
            Log.w((String)LOG_TAG, (String)"Default FirebaseApp failed to initialize because no default options were found. This usually means that com.google.gms:google-services was not applied to your gradle project.");
            return null;
        }
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions) {
        return FirebaseApp.initializeApp(context, firebaseOptions, DEFAULT_APP_NAME);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions, String string2) {
        Object object;
        GlobalBackgroundStateListener.ensureBackgroundStateListenerRegistered(context);
        string2 = FirebaseApp.normalize(string2);
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        Object object2 = LOCK;
        synchronized (object2) {
            Map<String, FirebaseApp> map = INSTANCES;
            boolean bl = !map.containsKey(string2);
            object = new StringBuilder();
            ((StringBuilder)object).append("FirebaseApp name ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(" already exists!");
            Preconditions.checkState(bl, ((StringBuilder)object).toString());
            Preconditions.checkNotNull(context, "Application context cannot be null.");
            object = new FirebaseApp(context, string2, firebaseOptions);
            map.put(string2, (FirebaseApp)object);
        }
        super.initializeAllApis();
        return object;
    }

    private static String normalize(String string2) {
        return string2.trim();
    }

    private void notifyBackgroundStateChangeListeners(boolean bl) {
        Log.d((String)LOG_TAG, (String)"Notifying background state change listeners.");
        Iterator<BackgroundStateChangeListener> iterator2 = this.backgroundStateChangeListeners.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onBackgroundStateChanged(bl);
        }
    }

    private void notifyOnAppDeleted() {
        Iterator<FirebaseAppLifecycleListener> iterator2 = this.lifecycleListeners.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDeleted(this.name, this.options);
        }
    }

    public void addBackgroundStateChangeListener(BackgroundStateChangeListener backgroundStateChangeListener) {
        this.checkNotDeleted();
        if (this.automaticResourceManagementEnabled.get() && BackgroundDetector.getInstance().isInBackground()) {
            backgroundStateChangeListener.onBackgroundStateChanged(true);
        }
        this.backgroundStateChangeListeners.add(backgroundStateChangeListener);
    }

    public void addLifecycleEventListener(FirebaseAppLifecycleListener firebaseAppLifecycleListener) {
        this.checkNotDeleted();
        Preconditions.checkNotNull(firebaseAppLifecycleListener);
        this.lifecycleListeners.add(firebaseAppLifecycleListener);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void delete() {
        if (!this.deleted.compareAndSet(false, true)) {
            return;
        }
        Object object = LOCK;
        synchronized (object) {
            INSTANCES.remove(this.name);
        }
        this.notifyOnAppDeleted();
    }

    public boolean equals(Object object) {
        if (!(object instanceof FirebaseApp)) {
            return false;
        }
        return this.name.equals(((FirebaseApp)object).getName());
    }

    public <T> T get(Class<T> clazz) {
        this.checkNotDeleted();
        return (T)this.componentRuntime.get((Class)clazz);
    }

    public Context getApplicationContext() {
        this.checkNotDeleted();
        return this.applicationContext;
    }

    public String getName() {
        this.checkNotDeleted();
        return this.name;
    }

    public FirebaseOptions getOptions() {
        this.checkNotDeleted();
        return this.options;
    }

    public String getPersistenceKey() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Base64Utils.encodeUrlSafeNoPadding(this.getName().getBytes(Charset.defaultCharset())));
        stringBuilder.append("+");
        stringBuilder.append(Base64Utils.encodeUrlSafeNoPadding(this.getOptions().getApplicationId().getBytes(Charset.defaultCharset())));
        return stringBuilder.toString();
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    void initializeAllComponents() {
        this.componentRuntime.initializeAllComponentsForTests();
    }

    public boolean isDataCollectionDefaultEnabled() {
        this.checkNotDeleted();
        return this.dataCollectionConfigStorage.get().isEnabled();
    }

    public boolean isDefaultApp() {
        return DEFAULT_APP_NAME.equals(this.getName());
    }

    public /* synthetic */ DataCollectionConfigStorage lambda$new$0$com-google-firebase-FirebaseApp(Context context) {
        return new DataCollectionConfigStorage(context, this.getPersistenceKey(), (Publisher)this.componentRuntime.get(Publisher.class));
    }

    public /* synthetic */ void lambda$new$1$com-google-firebase-FirebaseApp(boolean bl) {
        if (!bl) {
            this.defaultHeartBeatController.get().registerHeartBeat();
        }
    }

    public void removeBackgroundStateChangeListener(BackgroundStateChangeListener backgroundStateChangeListener) {
        this.checkNotDeleted();
        this.backgroundStateChangeListeners.remove(backgroundStateChangeListener);
    }

    public void removeLifecycleEventListener(FirebaseAppLifecycleListener firebaseAppLifecycleListener) {
        this.checkNotDeleted();
        Preconditions.checkNotNull(firebaseAppLifecycleListener);
        this.lifecycleListeners.remove(firebaseAppLifecycleListener);
    }

    public void setAutomaticResourceManagementEnabled(boolean bl) {
        this.checkNotDeleted();
        if (this.automaticResourceManagementEnabled.compareAndSet(bl ^ true, bl)) {
            boolean bl2 = BackgroundDetector.getInstance().isInBackground();
            if (bl && bl2) {
                this.notifyBackgroundStateChangeListeners(true);
            } else if (!bl && bl2) {
                this.notifyBackgroundStateChangeListeners(false);
            }
        }
    }

    public void setDataCollectionDefaultEnabled(Boolean bl) {
        this.checkNotDeleted();
        this.dataCollectionConfigStorage.get().setEnabled(bl);
    }

    @Deprecated
    public void setDataCollectionDefaultEnabled(boolean bl) {
        this.setDataCollectionDefaultEnabled((Boolean)bl);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("name", this.name).add("options", this.options).toString();
    }

    public static interface BackgroundStateChangeListener {
        public void onBackgroundStateChanged(boolean var1);
    }

    private static class GlobalBackgroundStateListener
    implements BackgroundDetector.BackgroundStateChangeListener {
        private static AtomicReference<GlobalBackgroundStateListener> INSTANCE = new AtomicReference();

        private GlobalBackgroundStateListener() {
        }

        private static void ensureBackgroundStateListenerRegistered(Context object) {
            if (PlatformVersion.isAtLeastIceCreamSandwich() && object.getApplicationContext() instanceof Application) {
                Application application = (Application)object.getApplicationContext();
                if (INSTANCE.get() == null && INSTANCE.compareAndSet(null, (GlobalBackgroundStateListener)(object = new GlobalBackgroundStateListener()))) {
                    BackgroundDetector.initialize(application);
                    BackgroundDetector.getInstance().addListener((BackgroundDetector.BackgroundStateChangeListener)object);
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void onBackgroundStateChanged(boolean bl) {
            Object object = LOCK;
            synchronized (object) {
                Object object2 = new ArrayList(INSTANCES.values());
                Iterator<FirebaseApp> iterator2 = ((ArrayList)object2).iterator();
                while (iterator2.hasNext()) {
                    object2 = iterator2.next();
                    if (!((FirebaseApp)object2).automaticResourceManagementEnabled.get()) continue;
                    ((FirebaseApp)object2).notifyBackgroundStateChangeListeners(bl);
                }
                return;
            }
        }
    }

    private static class UiExecutor
    implements Executor {
        private static final Handler HANDLER = new Handler(Looper.getMainLooper());

        private UiExecutor() {
        }

        @Override
        public void execute(Runnable runnable) {
            HANDLER.post(runnable);
        }
    }

    private static class UserUnlockReceiver
    extends BroadcastReceiver {
        private static AtomicReference<UserUnlockReceiver> INSTANCE = new AtomicReference();
        private final Context applicationContext;

        public UserUnlockReceiver(Context context) {
            this.applicationContext = context;
        }

        private static void ensureReceiverRegistered(Context context) {
            UserUnlockReceiver userUnlockReceiver;
            if (INSTANCE.get() == null && INSTANCE.compareAndSet(null, userUnlockReceiver = new UserUnlockReceiver(context))) {
                context.registerReceiver((BroadcastReceiver)userUnlockReceiver, new IntentFilter("android.intent.action.USER_UNLOCKED"));
            }
        }

        /*
         * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void onReceive(Context object, Intent object2) {
            object = LOCK;
            synchronized (object) {
                object2 = INSTANCES.values().iterator();
                while (true) {
                    if (!object2.hasNext()) {
                        // MONITOREXIT @DISABLED, blocks:[0, 2, 4, 5] lbl7 : MonitorExitStatement: MONITOREXIT : var1_1 /* !! */ 
                        this.unregister();
                        return;
                    }
                    ((FirebaseApp)object2.next()).initializeAllApis();
                }
                {
                    catch (Throwable throwable) {}
                    {
                        throw throwable;
                    }
                }
            }
        }

        public void unregister() {
            this.applicationContext.unregisterReceiver((BroadcastReceiver)this);
        }
    }
}

