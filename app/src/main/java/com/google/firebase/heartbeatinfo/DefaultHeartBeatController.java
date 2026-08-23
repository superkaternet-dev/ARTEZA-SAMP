/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.firebase.heartbeatinfo;

import android.content.Context;
import androidx.core.os.UserManagerCompat;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.Dependency;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda0;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda1;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda2;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda3;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatController$$ExternalSyntheticLambda4;
import com.google.firebase.heartbeatinfo.HeartBeatConsumer;
import com.google.firebase.heartbeatinfo.HeartBeatController;
import com.google.firebase.heartbeatinfo.HeartBeatInfo;
import com.google.firebase.heartbeatinfo.HeartBeatInfoStorage;
import com.google.firebase.inject.Provider;
import com.google.firebase.platforminfo.UserAgentPublisher;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class DefaultHeartBeatController
implements HeartBeatController,
HeartBeatInfo {
    private static final ThreadFactory THREAD_FACTORY = DefaultHeartBeatController$$ExternalSyntheticLambda4.INSTANCE;
    private final Context applicationContext;
    private final Executor backgroundExecutor;
    private final Set<HeartBeatConsumer> consumers;
    private final Provider<HeartBeatInfoStorage> storageProvider;
    private final Provider<UserAgentPublisher> userAgentProvider;

    private DefaultHeartBeatController(Context context, String string2, Set<HeartBeatConsumer> set, Provider<UserAgentPublisher> provider) {
        this(new DefaultHeartBeatController$$ExternalSyntheticLambda1(context, string2), set, new ThreadPoolExecutor(0, 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), THREAD_FACTORY), provider, context);
    }

    DefaultHeartBeatController(Provider<HeartBeatInfoStorage> provider, Set<HeartBeatConsumer> set, Executor executor, Provider<UserAgentPublisher> provider2, Context context) {
        this.storageProvider = provider;
        this.consumers = set;
        this.backgroundExecutor = executor;
        this.userAgentProvider = provider2;
        this.applicationContext = context;
    }

    public static Component<DefaultHeartBeatController> component() {
        return Component.builder(DefaultHeartBeatController.class, HeartBeatController.class, HeartBeatInfo.class).add(Dependency.required(Context.class)).add(Dependency.required(FirebaseApp.class)).add(Dependency.setOf(HeartBeatConsumer.class)).add(Dependency.requiredProvider(UserAgentPublisher.class)).factory(DefaultHeartBeatController$$ExternalSyntheticLambda0.INSTANCE).build();
    }

    static /* synthetic */ DefaultHeartBeatController lambda$component$4(ComponentContainer componentContainer) {
        return new DefaultHeartBeatController(componentContainer.get(Context.class), componentContainer.get(FirebaseApp.class).getPersistenceKey(), componentContainer.setOf(HeartBeatConsumer.class), componentContainer.getProvider(UserAgentPublisher.class));
    }

    static /* synthetic */ HeartBeatInfoStorage lambda$new$3(Context context, String string2) {
        return new HeartBeatInfoStorage(context, string2);
    }

    static /* synthetic */ Thread lambda$static$0(Runnable runnable) {
        return new Thread(runnable, "heartbeat-information-executor");
    }

    @Override
    public HeartBeatInfo.HeartBeat getHeartBeatCode(String object) {
        synchronized (this) {
            block4: {
                long l = System.currentTimeMillis();
                object = this.storageProvider.get();
                if (!((HeartBeatInfoStorage)object).shouldSendGlobalHeartBeat(l)) break block4;
                ((HeartBeatInfoStorage)object).postHeartBeatCleanUp();
                object = HeartBeatInfo.HeartBeat.GLOBAL;
                return object;
            }
            object = HeartBeatInfo.HeartBeat.NONE;
            return object;
        }
    }

    @Override
    public Task<String> getHeartBeatsHeader() {
        if (UserManagerCompat.isUserUnlocked(this.applicationContext) ^ true) {
            return Tasks.forResult("");
        }
        return Tasks.call(this.backgroundExecutor, new DefaultHeartBeatController$$ExternalSyntheticLambda2(this));
    }

    /*
     * Exception decompiling
     */
    public /* synthetic */ String lambda$getHeartBeatsHeader$2$com-google-firebase-heartbeatinfo-DefaultHeartBeatController() throws Exception {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 11[TRYBLOCK] [11 : 241->246)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1050)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public /* synthetic */ Void lambda$registerHeartBeat$1$com-google-firebase-heartbeatinfo-DefaultHeartBeatController() throws Exception {
        synchronized (this) {
            this.storageProvider.get().storeHeartBeat(System.currentTimeMillis(), this.userAgentProvider.get().getUserAgent());
            return null;
        }
    }

    public Task<Void> registerHeartBeat() {
        if (this.consumers.size() <= 0) {
            return Tasks.forResult(null);
        }
        if (UserManagerCompat.isUserUnlocked(this.applicationContext) ^ true) {
            return Tasks.forResult(null);
        }
        return Tasks.call(this.backgroundExecutor, new DefaultHeartBeatController$$ExternalSyntheticLambda3(this));
    }
}

