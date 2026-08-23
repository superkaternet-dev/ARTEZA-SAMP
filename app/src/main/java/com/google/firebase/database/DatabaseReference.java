/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.OnDisconnect;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.DatabaseConfig;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.RepoManager;
import com.google.firebase.database.core.ValidationPath;
import com.google.firebase.database.core.utilities.Pair;
import com.google.firebase.database.core.utilities.ParsedUrl;
import com.google.firebase.database.core.utilities.PushIdGenerator;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.utilities.Validation;
import com.google.firebase.database.core.utilities.encoding.CustomClassMapper;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class DatabaseReference
extends Query {
    private static DatabaseConfig defaultConfig;

    DatabaseReference(Repo repo, Path path) {
        super(repo, path);
    }

    private DatabaseReference(ParsedUrl parsedUrl, DatabaseConfig databaseConfig) {
        this(RepoManager.getRepo(databaseConfig, parsedUrl.repoInfo), parsedUrl.path);
    }

    DatabaseReference(String string2, DatabaseConfig databaseConfig) {
        this(Utilities.parseUrl(string2), databaseConfig);
    }

    private static DatabaseConfig getDefaultConfig() {
        synchronized (DatabaseReference.class) {
            DatabaseConfig databaseConfig;
            if (defaultConfig == null) {
                defaultConfig = databaseConfig = new DatabaseConfig();
            }
            databaseConfig = defaultConfig;
            return databaseConfig;
        }
    }

    public static void goOffline() {
        DatabaseReference.goOffline(DatabaseReference.getDefaultConfig());
    }

    static void goOffline(DatabaseConfig databaseConfig) {
        RepoManager.interrupt(databaseConfig);
    }

    public static void goOnline() {
        DatabaseReference.goOnline(DatabaseReference.getDefaultConfig());
    }

    static void goOnline(DatabaseConfig databaseConfig) {
        RepoManager.resume(databaseConfig);
    }

    private Task<Void> setPriorityInternal(Node node, CompletionListener object) {
        Validation.validateWritablePath(this.getPath());
        object = Utilities.wrapOnComplete((CompletionListener)object);
        this.repo.scheduleNow(new Runnable(this, node, (Pair)object){
            final DatabaseReference this$0;
            final Node val$priority;
            final Pair val$wrapped;
            {
                this.this$0 = databaseReference;
                this.val$priority = node;
                this.val$wrapped = pair;
            }

            @Override
            public void run() {
                this.this$0.repo.setValue(this.this$0.getPath().child(ChildKey.getPriorityKey()), this.val$priority, (CompletionListener)this.val$wrapped.getSecond());
            }
        });
        return (Task)((Pair)object).getFirst();
    }

    private Task<Void> setValueInternal(Object object, Node object2, CompletionListener completionListener) {
        Validation.validateWritablePath(this.getPath());
        ValidationPath.validateWithObject(this.getPath(), object);
        object = CustomClassMapper.convertToPlainJavaTypes(object);
        Validation.validateWritableObject(object);
        object = NodeUtilities.NodeFromJSON(object, (Node)object2);
        object2 = Utilities.wrapOnComplete(completionListener);
        this.repo.scheduleNow(new Runnable(this, (Node)object, (Pair)object2){
            final DatabaseReference this$0;
            final Node val$node;
            final Pair val$wrapped;
            {
                this.this$0 = databaseReference;
                this.val$node = node;
                this.val$wrapped = pair;
            }

            @Override
            public void run() {
                this.this$0.repo.setValue(this.this$0.getPath(), this.val$node, (CompletionListener)this.val$wrapped.getSecond());
            }
        });
        return (Task)((Pair)object2).getFirst();
    }

    private Task<Void> updateChildrenInternal(Map<String, Object> object, CompletionListener object2) {
        if (object != null) {
            Map<String, Object> map = CustomClassMapper.convertToPlainJavaTypes(object);
            object = CompoundWrite.fromPathMerge(Validation.parseAndValidateUpdate(this.getPath(), map));
            object2 = Utilities.wrapOnComplete((CompletionListener)object2);
            this.repo.scheduleNow(new Runnable(this, (CompoundWrite)object, (Pair)object2, map){
                final DatabaseReference this$0;
                final Map val$bouncedUpdate;
                final CompoundWrite val$merge;
                final Pair val$wrapped;
                {
                    this.this$0 = databaseReference;
                    this.val$merge = compoundWrite;
                    this.val$wrapped = pair;
                    this.val$bouncedUpdate = map;
                }

                @Override
                public void run() {
                    this.this$0.repo.updateChildren(this.this$0.getPath(), this.val$merge, (CompletionListener)this.val$wrapped.getSecond(), this.val$bouncedUpdate);
                }
            });
            return (Task)((Pair)object2).getFirst();
        }
        throw new NullPointerException("Can't pass null for argument 'update' in updateChildren()");
    }

    public DatabaseReference child(String object) {
        if (object != null) {
            if (this.getPath().isEmpty()) {
                Validation.validateRootPathString((String)object);
            } else {
                Validation.validatePathString((String)object);
            }
            object = this.getPath().child(new Path((String)object));
            return new DatabaseReference(this.repo, (Path)object);
        }
        throw new NullPointerException("Can't pass null for argument 'pathString' in child()");
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof DatabaseReference && this.toString().equals(object.toString());
        return bl;
    }

    public FirebaseDatabase getDatabase() {
        return this.repo.getDatabase();
    }

    public String getKey() {
        if (this.getPath().isEmpty()) {
            return null;
        }
        return this.getPath().getBack().asString();
    }

    public DatabaseReference getParent() {
        Path path = this.getPath().getParent();
        if (path != null) {
            return new DatabaseReference(this.repo, path);
        }
        return null;
    }

    public DatabaseReference getRoot() {
        return new DatabaseReference(this.repo, new Path(""));
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public OnDisconnect onDisconnect() {
        Validation.validateWritablePath(this.getPath());
        return new OnDisconnect(this.repo, this.getPath());
    }

    public DatabaseReference push() {
        ChildKey childKey = ChildKey.fromString(PushIdGenerator.generatePushChildName(this.repo.getServerTime()));
        return new DatabaseReference(this.repo, this.getPath().child(childKey));
    }

    public Task<Void> removeValue() {
        return this.setValue(null);
    }

    public void removeValue(CompletionListener completionListener) {
        this.setValue(null, completionListener);
    }

    public void runTransaction(Transaction.Handler handler) {
        this.runTransaction(handler, true);
    }

    public void runTransaction(Transaction.Handler handler, boolean bl) {
        if (handler != null) {
            Validation.validateWritablePath(this.getPath());
            this.repo.scheduleNow(new Runnable(this, handler, bl){
                final DatabaseReference this$0;
                final boolean val$fireLocalEvents;
                final Transaction.Handler val$handler;
                {
                    this.this$0 = databaseReference;
                    this.val$handler = handler;
                    this.val$fireLocalEvents = bl;
                }

                @Override
                public void run() {
                    this.this$0.repo.startTransaction(this.this$0.getPath(), this.val$handler, this.val$fireLocalEvents);
                }
            });
            return;
        }
        throw new NullPointerException("Can't pass null for argument 'handler' in runTransaction()");
    }

    void setHijackHash(boolean bl) {
        this.repo.scheduleNow(new Runnable(this, bl){
            final DatabaseReference this$0;
            final boolean val$hijackHash;
            {
                this.this$0 = databaseReference;
                this.val$hijackHash = bl;
            }

            @Override
            public void run() {
                this.this$0.repo.setHijackHash(this.val$hijackHash);
            }
        });
    }

    public Task<Void> setPriority(Object object) {
        return this.setPriorityInternal(PriorityUtilities.parsePriority(this.path, object), null);
    }

    public void setPriority(Object object, CompletionListener completionListener) {
        this.setPriorityInternal(PriorityUtilities.parsePriority(this.path, object), completionListener);
    }

    public Task<Void> setValue(Object object) {
        return this.setValueInternal(object, PriorityUtilities.parsePriority(this.path, null), null);
    }

    public Task<Void> setValue(Object object, Object object2) {
        return this.setValueInternal(object, PriorityUtilities.parsePriority(this.path, object2), null);
    }

    public void setValue(Object object, CompletionListener completionListener) {
        this.setValueInternal(object, PriorityUtilities.parsePriority(this.path, null), completionListener);
    }

    public void setValue(Object object, Object object2, CompletionListener completionListener) {
        this.setValueInternal(object, PriorityUtilities.parsePriority(this.path, object2), completionListener);
    }

    public String toString() {
        Object object = this.getParent();
        if (object == null) {
            return this.repo.toString();
        }
        try {
            CharSequence charSequence = new StringBuilder();
            charSequence.append(((DatabaseReference)object).toString());
            charSequence.append("/");
            charSequence.append(URLEncoder.encode(this.getKey(), "UTF-8").replace("+", "%20"));
            charSequence = charSequence.toString();
            return charSequence;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Failed to URLEncode key: ");
            ((StringBuilder)object).append(this.getKey());
            throw new DatabaseException(((StringBuilder)object).toString(), unsupportedEncodingException);
        }
    }

    public Task<Void> updateChildren(Map<String, Object> map) {
        return this.updateChildrenInternal(map, null);
    }

    public void updateChildren(Map<String, Object> map, CompletionListener completionListener) {
        this.updateChildrenInternal(map, completionListener);
    }

    public static interface CompletionListener {
        public void onComplete(DatabaseError var1, DatabaseReference var2);
    }
}

