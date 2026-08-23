/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.InternalHelpers;
import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.DatabaseConfig;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.RepoInfo;
import com.google.firebase.database.core.RunLoop;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RepoManager {
    private static final RepoManager instance = new RepoManager();
    private final Map<Context, Map<String, Repo>> repos = new HashMap<Context, Map<String, Repo>>();

    public static void clear() {
        instance.clearRepos();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void clearRepos() {
        Map<Context, Map<String, Repo>> map = this.repos;
        synchronized (map) {
            this.repos.clear();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Repo createLocalRepo(Context object, RepoInfo repoInfo, FirebaseDatabase firebaseDatabase) throws DatabaseException {
        ((Context)object).freeze();
        Object object2 = new StringBuilder();
        ((StringBuilder)object2).append("https://");
        ((StringBuilder)object2).append(repoInfo.host);
        ((StringBuilder)object2).append("/");
        ((StringBuilder)object2).append(repoInfo.namespace);
        String string2 = ((StringBuilder)object2).toString();
        object2 = this.repos;
        synchronized (object2) {
            Map<Object, Object> map;
            if (!this.repos.containsKey(object)) {
                map = new Map<Object, Object>();
                this.repos.put((Context)object, map);
            }
            if (!(map = this.repos.get(object)).containsKey(string2)) {
                Repo repo = new Repo(repoInfo, (Context)object, firebaseDatabase);
                map.put(string2, repo);
                return repo;
            }
            object = new IllegalStateException("createLocalRepo() called for existing repo.");
            throw object;
        }
    }

    public static Repo createRepo(Context context, RepoInfo repoInfo, FirebaseDatabase firebaseDatabase) throws DatabaseException {
        return instance.createLocalRepo(context, repoInfo, firebaseDatabase);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Repo getLocalRepo(Context object, RepoInfo repoInfo) throws DatabaseException {
        ((Context)object).freeze();
        Object object2 = new StringBuilder();
        ((StringBuilder)object2).append("https://");
        ((StringBuilder)object2).append(repoInfo.host);
        ((StringBuilder)object2).append("/");
        ((StringBuilder)object2).append(repoInfo.namespace);
        String string2 = ((StringBuilder)object2).toString();
        object2 = this.repos;
        synchronized (object2) {
            if (this.repos.containsKey(object)) {
                if (this.repos.get(object).containsKey(string2)) return this.repos.get(object).get(string2);
            }
            InternalHelpers.createDatabaseForTests(FirebaseApp.getInstance(), repoInfo, (DatabaseConfig)object);
            return this.repos.get(object).get(string2);
        }
    }

    public static Repo getRepo(Context context, RepoInfo repoInfo) throws DatabaseException {
        return instance.getLocalRepo(context, repoInfo);
    }

    public static void interrupt(Context context) {
        instance.interruptInternal(context);
    }

    public static void interrupt(Repo repo) {
        repo.scheduleNow(new Runnable(repo){
            final Repo val$repo;
            {
                this.val$repo = repo;
            }

            @Override
            public void run() {
                this.val$repo.interrupt();
            }
        });
    }

    private void interruptInternal(Context context) {
        RunLoop runLoop = context.getRunLoop();
        if (runLoop != null) {
            runLoop.scheduleNow(new Runnable(this, context){
                final RepoManager this$0;
                final Context val$ctx;
                {
                    this.this$0 = repoManager;
                    this.val$ctx = context;
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public void run() {
                    Map map = this.this$0.repos;
                    synchronized (map) {
                        boolean bl = true;
                        if (!this.this$0.repos.containsKey(this.val$ctx)) return;
                        Iterator iterator2 = ((Map)this.this$0.repos.get(this.val$ctx)).values().iterator();
                        while (true) {
                            if (!iterator2.hasNext()) {
                                if (!bl) return;
                                this.val$ctx.stop();
                                return;
                            }
                            Repo repo = (Repo)iterator2.next();
                            repo.interrupt();
                            if (bl && !repo.hasListeners()) {
                                bl = true;
                                continue;
                            }
                            bl = false;
                        }
                    }
                }
            });
        }
    }

    public static void resume(Context context) {
        instance.resumeInternal(context);
    }

    public static void resume(Repo repo) {
        repo.scheduleNow(new Runnable(repo){
            final Repo val$repo;
            {
                this.val$repo = repo;
            }

            @Override
            public void run() {
                this.val$repo.resume();
            }
        });
    }

    private void resumeInternal(Context context) {
        RunLoop runLoop = context.getRunLoop();
        if (runLoop != null) {
            runLoop.scheduleNow(new Runnable(this, context){
                final RepoManager this$0;
                final Context val$ctx;
                {
                    this.this$0 = repoManager;
                    this.val$ctx = context;
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public void run() {
                    Map map = this.this$0.repos;
                    synchronized (map) {
                        if (!this.this$0.repos.containsKey(this.val$ctx)) return;
                        Iterator iterator2 = ((Map)this.this$0.repos.get(this.val$ctx)).values().iterator();
                        while (iterator2.hasNext()) {
                            ((Repo)iterator2.next()).resume();
                        }
                        return;
                    }
                }
            });
        }
    }
}

