/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.ChildEventRegistration;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.ValueEventRegistration;
import com.google.firebase.database.core.ZombieEventManager;
import com.google.firebase.database.core.utilities.PushIdGenerator;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.utilities.Validation;
import com.google.firebase.database.core.view.QueryParams;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.snapshot.BooleanNode;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.DoubleNode;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.KeyIndex;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.PathIndex;
import com.google.firebase.database.snapshot.PriorityIndex;
import com.google.firebase.database.snapshot.PriorityUtilities;
import com.google.firebase.database.snapshot.StringNode;
import com.google.firebase.database.snapshot.ValueIndex;

public class Query {
    private final boolean orderByCalled;
    protected final QueryParams params;
    protected final Path path;
    protected final Repo repo;

    Query(Repo repo, Path path) {
        this.repo = repo;
        this.path = path;
        this.params = QueryParams.DEFAULT_PARAMS;
        this.orderByCalled = false;
    }

    Query(Repo repo, Path path, QueryParams queryParams, boolean bl) throws DatabaseException {
        this.repo = repo;
        this.path = path;
        this.params = queryParams;
        this.orderByCalled = bl;
        Utilities.hardAssert(queryParams.isValid(), "Validation of queries failed.");
    }

    private void addEventRegistration(EventRegistration eventRegistration) {
        ZombieEventManager.getInstance().recordEventRegistration(eventRegistration);
        this.repo.scheduleNow(new Runnable(this, eventRegistration){
            final Query this$0;
            final EventRegistration val$listener;
            {
                this.this$0 = query;
                this.val$listener = eventRegistration;
            }

            @Override
            public void run() {
                this.this$0.repo.addEventCallback(this.val$listener);
            }
        });
    }

    private Query endAt(Node object, String object2) {
        Validation.validateNullableKey((String)object2);
        if (!object.isLeafNode() && !object.isEmpty()) {
            throw new IllegalArgumentException("Can only use simple values for endAt()");
        }
        object2 = object2 != null ? ChildKey.fromString((String)object2) : null;
        if (!this.params.hasEnd()) {
            object = this.params.endAt((Node)object, (ChildKey)object2);
            this.validateLimit((QueryParams)object);
            this.validateQueryEndpoints((QueryParams)object);
            Utilities.hardAssert(((QueryParams)object).isValid());
            return new Query(this.repo, this.path, (QueryParams)object, this.orderByCalled);
        }
        throw new IllegalArgumentException("Can't call endAt() or equalTo() multiple times");
    }

    private Query endBefore(Node node, String string2) {
        return this.endAt(node, PushIdGenerator.predecessor(string2));
    }

    private void removeEventRegistration(EventRegistration eventRegistration) {
        ZombieEventManager.getInstance().zombifyForRemove(eventRegistration);
        this.repo.scheduleNow(new Runnable(this, eventRegistration){
            final Query this$0;
            final EventRegistration val$registration;
            {
                this.this$0 = query;
                this.val$registration = eventRegistration;
            }

            @Override
            public void run() {
                this.this$0.repo.removeEventCallback(this.val$registration);
            }
        });
    }

    private Query startAfter(Node node, String string2) {
        return this.startAt(node, PushIdGenerator.successor(string2));
    }

    private Query startAt(Node object, String string2) {
        Validation.validateNullableKey(string2);
        if (!object.isLeafNode() && !object.isEmpty()) {
            throw new IllegalArgumentException("Can only use simple values for startAt() and startAfter()");
        }
        if (!this.params.hasStart()) {
            ChildKey childKey = null;
            if (string2 != null) {
                childKey = string2.equals("[MIN_NAME]") ? ChildKey.getMinName() : (string2.equals("[MAX_KEY]") ? ChildKey.getMaxName() : ChildKey.fromString(string2));
            }
            object = this.params.startAt((Node)object, childKey);
            this.validateLimit((QueryParams)object);
            this.validateQueryEndpoints((QueryParams)object);
            Utilities.hardAssert(((QueryParams)object).isValid());
            return new Query(this.repo, this.path, (QueryParams)object, this.orderByCalled);
        }
        throw new IllegalArgumentException("Can't call startAt(), startAfte(), or equalTo() multiple times");
    }

    private void validateEqualToCall() {
        if (!this.params.hasStart()) {
            if (!this.params.hasEnd()) {
                return;
            }
            throw new IllegalArgumentException("Cannot combine equalTo() with endAt() or endBefore()");
        }
        throw new IllegalArgumentException("Cannot combine equalTo() with startAt() or startAfter()");
    }

    private void validateLimit(QueryParams queryParams) {
        if (queryParams.hasStart() && queryParams.hasEnd() && queryParams.hasLimit() && !queryParams.hasAnchoredLimit()) {
            throw new IllegalArgumentException("Can't combine startAt(), startAfter(), endAt(), endBefore(), and limit(). Use limitToFirst() or limitToLast() instead");
        }
    }

    private void validateNoOrderByCall() {
        if (!this.orderByCalled) {
            return;
        }
        throw new IllegalArgumentException("You can't combine multiple orderBy calls!");
    }

    private void validateQueryEndpoints(QueryParams queryParams) {
        if (queryParams.getIndex().equals(KeyIndex.getInstance())) {
            Node node;
            if (queryParams.hasStart()) {
                node = queryParams.getIndexStartValue();
                if (!Objects.equal(queryParams.getIndexStartName(), ChildKey.getMinName()) || !(node instanceof StringNode)) {
                    throw new IllegalArgumentException("You must use startAt(String value), startAfter(String value), endAt(String value), endBefore(String value) or equalTo(String value) in combination with orderByKey(). Other type of values or using the version with 2 parameters is not supported");
                }
            }
            if (queryParams.hasEnd()) {
                node = queryParams.getIndexEndValue();
                if (!queryParams.getIndexEndName().equals(ChildKey.getMaxName()) || !(node instanceof StringNode)) {
                    throw new IllegalArgumentException("You must use startAt(String value), startAfter(String value), endAt(String value), endBefore(String value) or equalTo(String value) in combination with orderByKey(). Other type of values or using the version with 2 parameters is not supported");
                }
            }
        } else if (queryParams.getIndex().equals(PriorityIndex.getInstance()) && (queryParams.hasStart() && !PriorityUtilities.isValidPriority(queryParams.getIndexStartValue()) || queryParams.hasEnd() && !PriorityUtilities.isValidPriority(queryParams.getIndexEndValue()))) {
            throw new IllegalArgumentException("When using orderByPriority(), values provided to startAt(), startAfter(), endAt(), endBefore(), or equalTo() must be valid priorities.");
        }
    }

    public ChildEventListener addChildEventListener(ChildEventListener childEventListener) {
        this.addEventRegistration(new ChildEventRegistration(this.repo, childEventListener, this.getSpec()));
        return childEventListener;
    }

    public void addListenerForSingleValueEvent(ValueEventListener valueEventListener) {
        this.addEventRegistration(new ValueEventRegistration(this.repo, new ValueEventListener(this, valueEventListener){
            final Query this$0;
            final ValueEventListener val$listener;
            {
                this.this$0 = query;
                this.val$listener = valueEventListener;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                this.val$listener.onCancelled(databaseError);
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                this.this$0.removeEventListener(this);
                this.val$listener.onDataChange(dataSnapshot);
            }
        }, this.getSpec()));
    }

    public ValueEventListener addValueEventListener(ValueEventListener valueEventListener) {
        this.addEventRegistration(new ValueEventRegistration(this.repo, valueEventListener, this.getSpec()));
        return valueEventListener;
    }

    public Query endAt(double d) {
        return this.endAt(d, null);
    }

    public Query endAt(double d, String string2) {
        return this.endAt(new DoubleNode(d, PriorityUtilities.NullPriority()), string2);
    }

    public Query endAt(String string2) {
        return this.endAt(string2, null);
    }

    public Query endAt(String object, String string2) {
        object = object != null ? new StringNode((String)object, PriorityUtilities.NullPriority()) : EmptyNode.Empty();
        return this.endAt((Node)object, string2);
    }

    public Query endAt(boolean bl) {
        return this.endAt(bl, null);
    }

    public Query endAt(boolean bl, String string2) {
        return this.endAt(new BooleanNode(bl, PriorityUtilities.NullPriority()), string2);
    }

    public Query endBefore(double d) {
        return this.endAt(d, ChildKey.getMinName().asString());
    }

    public Query endBefore(double d, String string2) {
        return this.endBefore(new DoubleNode(d, PriorityUtilities.NullPriority()), string2);
    }

    public Query endBefore(String string2) {
        if (string2 != null && this.params.getIndex().equals(KeyIndex.getInstance())) {
            return this.endAt(PushIdGenerator.predecessor(string2));
        }
        return this.endAt(string2, ChildKey.getMinName().asString());
    }

    public Query endBefore(String object, String string2) {
        String string3 = object;
        if (object != null) {
            string3 = object;
            if (this.params.getIndex().equals(KeyIndex.getInstance())) {
                string3 = PushIdGenerator.predecessor((String)object);
            }
        }
        object = string3 != null ? new StringNode(string3, PriorityUtilities.NullPriority()) : EmptyNode.Empty();
        return this.endBefore((Node)object, string2);
    }

    public Query endBefore(boolean bl) {
        return this.endAt(bl, ChildKey.getMinName().asString());
    }

    public Query endBefore(boolean bl, String string2) {
        return this.endBefore(new BooleanNode(bl, PriorityUtilities.NullPriority()), string2);
    }

    public Query equalTo(double d) {
        this.validateEqualToCall();
        return this.startAt(d).endAt(d);
    }

    public Query equalTo(double d, String string2) {
        this.validateEqualToCall();
        return this.startAt(d, string2).endAt(d, string2);
    }

    public Query equalTo(String string2) {
        this.validateEqualToCall();
        return this.startAt(string2).endAt(string2);
    }

    public Query equalTo(String string2, String string3) {
        this.validateEqualToCall();
        return this.startAt(string2, string3).endAt(string2, string3);
    }

    public Query equalTo(boolean bl) {
        this.validateEqualToCall();
        return this.startAt(bl).endAt(bl);
    }

    public Query equalTo(boolean bl, String string2) {
        this.validateEqualToCall();
        return this.startAt(bl, string2).endAt(bl, string2);
    }

    public Task<DataSnapshot> get() {
        return this.repo.getValue(this);
    }

    public Path getPath() {
        return this.path;
    }

    public DatabaseReference getRef() {
        return new DatabaseReference(this.repo, this.getPath());
    }

    public Repo getRepo() {
        return this.repo;
    }

    public QuerySpec getSpec() {
        return new QuerySpec(this.path, this.params);
    }

    public void keepSynced(boolean bl) {
        if (!this.path.isEmpty() && this.path.getFront().equals(ChildKey.getInfoKey())) {
            throw new DatabaseException("Can't call keepSynced() on .info paths.");
        }
        this.repo.scheduleNow(new Runnable(this, bl){
            final Query this$0;
            final boolean val$keepSynced;
            {
                this.this$0 = query;
                this.val$keepSynced = bl;
            }

            @Override
            public void run() {
                this.this$0.repo.keepSynced(this.this$0.getSpec(), this.val$keepSynced);
            }
        });
    }

    public Query limitToFirst(int n) {
        if (n > 0) {
            if (!this.params.hasLimit()) {
                return new Query(this.repo, this.path, this.params.limitToFirst(n), this.orderByCalled);
            }
            throw new IllegalArgumentException("Can't call limitToLast on query with previously set limit!");
        }
        throw new IllegalArgumentException("Limit must be a positive integer!");
    }

    public Query limitToLast(int n) {
        if (n > 0) {
            if (!this.params.hasLimit()) {
                return new Query(this.repo, this.path, this.params.limitToLast(n), this.orderByCalled);
            }
            throw new IllegalArgumentException("Can't call limitToLast on query with previously set limit!");
        }
        throw new IllegalArgumentException("Limit must be a positive integer!");
    }

    public Query orderByChild(String object) {
        if (object != null) {
            if (!((String)object).equals("$key") && !((String)object).equals(".key")) {
                if (!((String)object).equals("$priority") && !((String)object).equals(".priority")) {
                    if (!((String)object).equals("$value") && !((String)object).equals(".value")) {
                        Validation.validatePathString((String)object);
                        this.validateNoOrderByCall();
                        object = new Path((String)object);
                        if (((Path)object).size() != 0) {
                            object = new PathIndex((Path)object);
                            return new Query(this.repo, this.path, this.params.orderBy((Index)object), true);
                        }
                        throw new IllegalArgumentException("Can't use empty path, use orderByValue() instead!");
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Can't use '");
                    stringBuilder.append((String)object);
                    stringBuilder.append("' as path, please use orderByValue() instead!");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Can't use '");
                stringBuilder.append((String)object);
                stringBuilder.append("' as path, please use orderByPriority() instead!");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't use '");
            stringBuilder.append((String)object);
            stringBuilder.append("' as path, please use orderByKey() instead!");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new NullPointerException("Key can't be null");
    }

    public Query orderByKey() {
        this.validateNoOrderByCall();
        QueryParams queryParams = this.params.orderBy(KeyIndex.getInstance());
        this.validateQueryEndpoints(queryParams);
        return new Query(this.repo, this.path, queryParams, true);
    }

    public Query orderByPriority() {
        this.validateNoOrderByCall();
        QueryParams queryParams = this.params.orderBy(PriorityIndex.getInstance());
        this.validateQueryEndpoints(queryParams);
        return new Query(this.repo, this.path, queryParams, true);
    }

    public Query orderByValue() {
        this.validateNoOrderByCall();
        return new Query(this.repo, this.path, this.params.orderBy(ValueIndex.getInstance()), true);
    }

    public void removeEventListener(ChildEventListener childEventListener) {
        if (childEventListener != null) {
            this.removeEventRegistration(new ChildEventRegistration(this.repo, childEventListener, this.getSpec()));
            return;
        }
        throw new NullPointerException("listener must not be null");
    }

    public void removeEventListener(ValueEventListener valueEventListener) {
        if (valueEventListener != null) {
            this.removeEventRegistration(new ValueEventRegistration(this.repo, valueEventListener, this.getSpec()));
            return;
        }
        throw new NullPointerException("listener must not be null");
    }

    public Query startAfter(double d) {
        return this.startAt(d, ChildKey.getMaxName().asString());
    }

    public Query startAfter(double d, String string2) {
        return this.startAfter(new DoubleNode(d, PriorityUtilities.NullPriority()), string2);
    }

    public Query startAfter(String string2) {
        if (string2 != null && this.params.getIndex().equals(KeyIndex.getInstance())) {
            return this.startAt(PushIdGenerator.successor(string2));
        }
        return this.startAt(string2, ChildKey.getMaxName().asString());
    }

    public Query startAfter(String object, String string2) {
        String string3 = object;
        if (object != null) {
            string3 = object;
            if (this.params.getIndex().equals(KeyIndex.getInstance())) {
                string3 = PushIdGenerator.successor((String)object);
            }
        }
        object = string3 != null ? new StringNode(string3, PriorityUtilities.NullPriority()) : EmptyNode.Empty();
        return this.startAfter((Node)object, string2);
    }

    public Query startAfter(boolean bl) {
        return this.startAt(bl, ChildKey.getMaxName().asString());
    }

    public Query startAfter(boolean bl, String string2) {
        return this.startAfter(new BooleanNode(bl, PriorityUtilities.NullPriority()), string2);
    }

    public Query startAt(double d) {
        return this.startAt(d, null);
    }

    public Query startAt(double d, String string2) {
        return this.startAt(new DoubleNode(d, PriorityUtilities.NullPriority()), string2);
    }

    public Query startAt(String string2) {
        return this.startAt(string2, null);
    }

    public Query startAt(String object, String string2) {
        object = object != null ? new StringNode((String)object, PriorityUtilities.NullPriority()) : EmptyNode.Empty();
        return this.startAt((Node)object, string2);
    }

    public Query startAt(boolean bl) {
        return this.startAt(bl, null);
    }

    public Query startAt(boolean bl, String string2) {
        return this.startAt(new BooleanNode(bl, PriorityUtilities.NullPriority()), string2);
    }
}

