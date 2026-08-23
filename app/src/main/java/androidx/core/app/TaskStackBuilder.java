/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.Log
 */
package androidx.core.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Iterator;

public final class TaskStackBuilder
implements Iterable<Intent> {
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents = new ArrayList();
    private final Context mSourceContext;

    private TaskStackBuilder(Context context) {
        this.mSourceContext = context;
    }

    public static TaskStackBuilder create(Context context) {
        return new TaskStackBuilder(context);
    }

    @Deprecated
    public static TaskStackBuilder from(Context context) {
        return TaskStackBuilder.create(context);
    }

    public TaskStackBuilder addNextIntent(Intent intent) {
        this.mIntents.add(intent);
        return this;
    }

    public TaskStackBuilder addNextIntentWithParentStack(Intent intent) {
        ComponentName componentName;
        ComponentName componentName2 = componentName = intent.getComponent();
        if (componentName == null) {
            componentName2 = intent.resolveActivity(this.mSourceContext.getPackageManager());
        }
        if (componentName2 != null) {
            this.addParentStack(componentName2);
        }
        this.addNextIntent(intent);
        return this;
    }

    public TaskStackBuilder addParentStack(Activity activity) {
        Intent intent = null;
        if (activity instanceof SupportParentable) {
            intent = ((SupportParentable)activity).getSupportParentActivityIntent();
        }
        Intent intent2 = intent;
        if (intent == null) {
            intent2 = NavUtils.getParentActivityIntent(activity);
        }
        if (intent2 != null) {
            intent = intent2.getComponent();
            activity = intent;
            if (intent == null) {
                activity = intent2.resolveActivity(this.mSourceContext.getPackageManager());
            }
            this.addParentStack((ComponentName)activity);
            this.addNextIntent(intent2);
        }
        return this;
    }

    public TaskStackBuilder addParentStack(ComponentName componentName) {
        int n = this.mIntents.size();
        try {
            componentName = NavUtils.getParentActivityIntent(this.mSourceContext, componentName);
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.e((String)TAG, (String)"Bad ComponentName while traversing activity parent metadata");
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(nameNotFoundException);
            throw illegalArgumentException;
        }
        while (componentName != null) {
            this.mIntents.add(n, (Intent)componentName);
            componentName = NavUtils.getParentActivityIntent(this.mSourceContext, componentName.getComponent());
        }
        return this;
    }

    public TaskStackBuilder addParentStack(Class<?> clazz) {
        return this.addParentStack(new ComponentName(this.mSourceContext, clazz));
    }

    public Intent editIntentAt(int n) {
        return this.mIntents.get(n);
    }

    @Deprecated
    public Intent getIntent(int n) {
        return this.editIntentAt(n);
    }

    public int getIntentCount() {
        return this.mIntents.size();
    }

    public Intent[] getIntents() {
        Intent[] intentArray = new Intent[this.mIntents.size()];
        if (intentArray.length == 0) {
            return intentArray;
        }
        intentArray[0] = new Intent(this.mIntents.get(0)).addFlags(0x1000C000);
        for (int i = 1; i < intentArray.length; ++i) {
            intentArray[i] = new Intent(this.mIntents.get(i));
        }
        return intentArray;
    }

    public PendingIntent getPendingIntent(int n, int n2) {
        return this.getPendingIntent(n, n2, null);
    }

    public PendingIntent getPendingIntent(int n, int n2, Bundle bundle) {
        if (!this.mIntents.isEmpty()) {
            Intent[] intentArray = this.mIntents;
            intentArray = intentArray.toArray(new Intent[intentArray.size()]);
            intentArray[0] = new Intent(intentArray[0]).addFlags(0x1000C000);
            if (Build.VERSION.SDK_INT >= 16) {
                return PendingIntent.getActivities((Context)this.mSourceContext, (int)n, (Intent[])intentArray, (int)n2, (Bundle)bundle);
            }
            return PendingIntent.getActivities((Context)this.mSourceContext, (int)n, (Intent[])intentArray, (int)n2);
        }
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
    }

    @Override
    @Deprecated
    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }

    public void startActivities() {
        this.startActivities(null);
    }

    public void startActivities(Bundle bundle) {
        if (!this.mIntents.isEmpty()) {
            Intent[] intentArray = this.mIntents;
            intentArray = intentArray.toArray(new Intent[intentArray.size()]);
            intentArray[0] = new Intent(intentArray[0]).addFlags(0x1000C000);
            if (!ContextCompat.startActivities(this.mSourceContext, intentArray, bundle)) {
                bundle = new Intent(intentArray[intentArray.length - 1]);
                bundle.addFlags(0x10000000);
                this.mSourceContext.startActivity((Intent)bundle);
            }
            return;
        }
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
    }

    public static interface SupportParentable {
        public Intent getSupportParentActivityIntent();
    }
}

