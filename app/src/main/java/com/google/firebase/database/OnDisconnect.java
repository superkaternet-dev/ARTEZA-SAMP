/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.ValidationPath;
import com.google.firebase.database.core.utilities.Pair;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.utilities.Validation;
import com.google.firebase.database.core.utilities.encoding.CustomClassMapper;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.util.Map;

public class OnDisconnect {
    private Path path;
    private Repo repo;

    OnDisconnect(Repo repo, Path path) {
        this.repo = repo;
        this.path = path;
    }

    private Task<Void> cancelInternal(DatabaseReference.CompletionListener object) {
        object = Utilities.wrapOnComplete((DatabaseReference.CompletionListener)object);
        this.repo.scheduleNow(new Runnable(this, (Pair)object){
            final OnDisconnect this$0;
            final Pair val$wrapped;
            {
                this.this$0 = onDisconnect;
                this.val$wrapped = pair;
            }

            @Override
            public void run() {
                this.this$0.repo.onDisconnectCancel(this.this$0.path, (DatabaseReference.CompletionListener)this.val$wrapped.getSecond());
            }
        });
        return (Task)((Pair)object).getFirst();
    }

    private Task<Void> onDisconnectSetInternal(Object object, Node object2, DatabaseReference.CompletionListener completionListener) {
        Validation.validateWritablePath(this.path);
        ValidationPath.validateWithObject(this.path, object);
        object = CustomClassMapper.convertToPlainJavaTypes(object);
        Validation.validateWritableObject(object);
        object = NodeUtilities.NodeFromJSON(object, (Node)object2);
        object2 = Utilities.wrapOnComplete(completionListener);
        this.repo.scheduleNow(new Runnable(this, (Node)object, (Pair)object2){
            final OnDisconnect this$0;
            final Node val$node;
            final Pair val$wrapped;
            {
                this.this$0 = onDisconnect;
                this.val$node = node;
                this.val$wrapped = pair;
            }

            @Override
            public void run() {
                this.this$0.repo.onDisconnectSetValue(this.this$0.path, this.val$node, (DatabaseReference.CompletionListener)this.val$wrapped.getSecond());
            }
        });
        return (Task)((Pair)object2).getFirst();
    }

    private Task<Void> updateChildrenInternal(Map<String, Object> map, DatabaseReference.CompletionListener object) {
        Map<Path, Node> map2 = Validation.parseAndValidateUpdate(this.path, map);
        object = Utilities.wrapOnComplete((DatabaseReference.CompletionListener)object);
        this.repo.scheduleNow(new Runnable(this, map2, (Pair)object, map){
            final OnDisconnect this$0;
            final Map val$parsedUpdate;
            final Map val$update;
            final Pair val$wrapped;
            {
                this.this$0 = onDisconnect;
                this.val$parsedUpdate = map;
                this.val$wrapped = pair;
                this.val$update = map2;
            }

            @Override
            public void run() {
                this.this$0.repo.onDisconnectUpdate(this.this$0.path, this.val$parsedUpdate, (DatabaseReference.CompletionListener)this.val$wrapped.getSecond(), this.val$update);
            }
        });
        return (Task)((Pair)object).getFirst();
    }

    public Task<Void> cancel() {
        return this.cancelInternal(null);
    }

    public void cancel(DatabaseReference.CompletionListener completionListener) {
        this.cancelInternal(completionListener);
    }

    public Task<Void> removeValue() {
        return this.setValue(null);
    }

    public void removeValue(DatabaseReference.CompletionListener completionListener) {
        this.setValue(null, completionListener);
    }

    public Task<Void> setValue(Object object) {
        return this.onDisconnectSetInternal(object, PriorityUtilities.NullPriority(), null);
    }

    public Task<Void> setValue(Object object, double d) {
        return this.onDisconnectSetInternal(object, PriorityUtilities.parsePriority(this.path, d), null);
    }

    public Task<Void> setValue(Object object, String string2) {
        return this.onDisconnectSetInternal(object, PriorityUtilities.parsePriority(this.path, string2), null);
    }

    public void setValue(Object object, double d, DatabaseReference.CompletionListener completionListener) {
        this.onDisconnectSetInternal(object, PriorityUtilities.parsePriority(this.path, d), completionListener);
    }

    public void setValue(Object object, DatabaseReference.CompletionListener completionListener) {
        this.onDisconnectSetInternal(object, PriorityUtilities.NullPriority(), completionListener);
    }

    public void setValue(Object object, String string2, DatabaseReference.CompletionListener completionListener) {
        this.onDisconnectSetInternal(object, PriorityUtilities.parsePriority(this.path, string2), completionListener);
    }

    public void setValue(Object object, Map map, DatabaseReference.CompletionListener completionListener) {
        this.onDisconnectSetInternal(object, PriorityUtilities.parsePriority(this.path, map), completionListener);
    }

    public Task<Void> updateChildren(Map<String, Object> map) {
        return this.updateChildrenInternal(map, null);
    }

    public void updateChildren(Map<String, Object> map, DatabaseReference.CompletionListener completionListener) {
        this.updateChildrenInternal(map, completionListener);
    }
}

