/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 *  android.util.SparseArray
 */
package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.core.graphics.drawable.IconCompat;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

class NotificationCompatJellybean {
    static final String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";
    static final String EXTRA_DATA_ONLY_REMOTE_INPUTS = "android.support.dataRemoteInputs";
    private static final String KEY_ACTION_INTENT = "actionIntent";
    private static final String KEY_ALLOWED_DATA_TYPES = "allowedDataTypes";
    private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_DATA_ONLY_REMOTE_INPUTS = "dataOnlyRemoteInputs";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_ICON = "icon";
    private static final String KEY_LABEL = "label";
    private static final String KEY_REMOTE_INPUTS = "remoteInputs";
    private static final String KEY_RESULT_KEY = "resultKey";
    private static final String KEY_SEMANTIC_ACTION = "semanticAction";
    private static final String KEY_SHOWS_USER_INTERFACE = "showsUserInterface";
    private static final String KEY_TITLE = "title";
    public static final String TAG = "NotificationCompat";
    private static Field sActionIconField;
    private static Field sActionIntentField;
    private static Field sActionTitleField;
    private static boolean sActionsAccessFailed;
    private static Field sActionsField;
    private static final Object sActionsLock;
    private static Field sExtrasField;
    private static boolean sExtrasFieldAccessFailed;
    private static final Object sExtrasLock;

    static {
        sExtrasLock = new Object();
        sActionsLock = new Object();
    }

    private NotificationCompatJellybean() {
    }

    public static SparseArray<Bundle> buildActionExtrasMap(List<Bundle> list) {
        SparseArray sparseArray = null;
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            Bundle bundle = list.get(i);
            SparseArray sparseArray2 = sparseArray;
            if (bundle != null) {
                sparseArray2 = sparseArray;
                if (sparseArray == null) {
                    sparseArray2 = new SparseArray();
                }
                sparseArray2.put(i, (Object)bundle);
            }
            sparseArray = sparseArray2;
        }
        return sparseArray;
    }

    private static boolean ensureActionReflectionReadyLocked() {
        if (sActionsAccessFailed) {
            return false;
        }
        try {
            if (sActionsField == null) {
                AnnotatedElement annotatedElement = Class.forName("android.app.Notification$Action");
                sActionIconField = ((Class)annotatedElement).getDeclaredField(KEY_ICON);
                sActionTitleField = ((Class)annotatedElement).getDeclaredField(KEY_TITLE);
                sActionIntentField = ((Class)annotatedElement).getDeclaredField(KEY_ACTION_INTENT);
                annotatedElement = Notification.class.getDeclaredField("actions");
                sActionsField = annotatedElement;
                ((Field)annotatedElement).setAccessible(true);
            }
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.e((String)TAG, (String)"Unable to access notification actions", (Throwable)noSuchFieldException);
            sActionsAccessFailed = true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            Log.e((String)TAG, (String)"Unable to access notification actions", (Throwable)classNotFoundException);
            sActionsAccessFailed = true;
        }
        return sActionsAccessFailed ^ true;
    }

    private static RemoteInput fromBundle(Bundle bundle) {
        Object object = bundle.getStringArrayList(KEY_ALLOWED_DATA_TYPES);
        HashSet<String> hashSet = new HashSet<String>();
        if (object != null) {
            object = ((ArrayList)object).iterator();
            while (object.hasNext()) {
                hashSet.add((String)object.next());
            }
        }
        return new RemoteInput(bundle.getString(KEY_RESULT_KEY), bundle.getCharSequence(KEY_LABEL), bundle.getCharSequenceArray(KEY_CHOICES), bundle.getBoolean(KEY_ALLOW_FREE_FORM_INPUT), 0, bundle.getBundle(KEY_EXTRAS), hashSet);
    }

    private static RemoteInput[] fromBundleArray(Bundle[] bundleArray) {
        if (bundleArray == null) {
            return null;
        }
        RemoteInput[] remoteInputArray = new RemoteInput[bundleArray.length];
        for (int i = 0; i < bundleArray.length; ++i) {
            remoteInputArray[i] = NotificationCompatJellybean.fromBundle(bundleArray[i]);
        }
        return remoteInputArray;
    }

    /*
     * WARNING - combined exceptions agressively - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static NotificationCompat.Action getAction(Notification object, int n) {
        Object object2 = sActionsLock;
        synchronized (object2) {
            try {
                try {
                    Object[] bundle = NotificationCompatJellybean.getActionObjectsLocked((Notification)object);
                    if (bundle == null) return null;
                    Object object3 = bundle[n];
                    bundle = null;
                    Bundle bundle2 = NotificationCompatJellybean.getExtras((Notification)object);
                    object = bundle;
                    if (bundle2 == null) return NotificationCompatJellybean.readAction(sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
                    bundle2 = bundle2.getSparseParcelableArray("android.support.actionExtras");
                    object = bundle;
                    if (bundle2 == null) return NotificationCompatJellybean.readAction(sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
                    object = (Bundle)bundle2.get(n);
                    return NotificationCompatJellybean.readAction(sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
                }
                catch (IllegalAccessException illegalAccessException) {
                    Log.e((String)TAG, (String)"Unable to access notification actions", (Throwable)illegalAccessException);
                    sActionsAccessFailed = true;
                }
                return null;
            }
            catch (Throwable throwable22) {}
            throw throwable22;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static int getActionCount(Notification objectArray) {
        Object object = sActionsLock;
        synchronized (object) {
            objectArray = NotificationCompatJellybean.getActionObjectsLocked((Notification)objectArray);
            if (objectArray == null) return 0;
            return objectArray.length;
        }
    }

    static NotificationCompat.Action getActionFromBundle(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle(KEY_EXTRAS);
        boolean bl = false;
        if (bundle2 != null) {
            bl = bundle2.getBoolean(EXTRA_ALLOW_GENERATED_REPLIES, false);
        }
        return new NotificationCompat.Action(bundle.getInt(KEY_ICON), bundle.getCharSequence(KEY_TITLE), (PendingIntent)bundle.getParcelable(KEY_ACTION_INTENT), bundle.getBundle(KEY_EXTRAS), NotificationCompatJellybean.fromBundleArray(NotificationCompatJellybean.getBundleArrayFromBundle(bundle, KEY_REMOTE_INPUTS)), NotificationCompatJellybean.fromBundleArray(NotificationCompatJellybean.getBundleArrayFromBundle(bundle, KEY_DATA_ONLY_REMOTE_INPUTS)), bl, bundle.getInt(KEY_SEMANTIC_ACTION), bundle.getBoolean(KEY_SHOWS_USER_INTERFACE), false);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Object[] getActionObjectsLocked(Notification objectArray) {
        Object object = sActionsLock;
        synchronized (object) {
            if (!NotificationCompatJellybean.ensureActionReflectionReadyLocked()) {
                return null;
            }
            try {
                return (Object[])sActionsField.get(objectArray);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)TAG, (String)"Unable to access notification actions", (Throwable)illegalAccessException);
                sActionsAccessFailed = true;
                return null;
            }
        }
    }

    private static Bundle[] getBundleArrayFromBundle(Bundle bundle, String string2) {
        Parcelable[] parcelableArray = bundle.getParcelableArray(string2);
        if (!(parcelableArray instanceof Bundle[]) && parcelableArray != null) {
            parcelableArray = (Bundle[])Arrays.copyOf(parcelableArray, parcelableArray.length, Bundle[].class);
            bundle.putParcelableArray(string2, parcelableArray);
            return parcelableArray;
        }
        return (Bundle[])parcelableArray;
    }

    static Bundle getBundleForAction(NotificationCompat.Action action) {
        Bundle bundle = new Bundle();
        IconCompat iconCompat = action.getIconCompat();
        int n = iconCompat != null ? iconCompat.getResId() : 0;
        bundle.putInt(KEY_ICON, n);
        bundle.putCharSequence(KEY_TITLE, action.getTitle());
        bundle.putParcelable(KEY_ACTION_INTENT, (Parcelable)action.getActionIntent());
        iconCompat = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
        iconCompat.putBoolean(EXTRA_ALLOW_GENERATED_REPLIES, action.getAllowGeneratedReplies());
        bundle.putBundle(KEY_EXTRAS, (Bundle)iconCompat);
        bundle.putParcelableArray(KEY_REMOTE_INPUTS, (Parcelable[])NotificationCompatJellybean.toBundleArray(action.getRemoteInputs()));
        bundle.putBoolean(KEY_SHOWS_USER_INTERFACE, action.getShowsUserInterface());
        bundle.putInt(KEY_SEMANTIC_ACTION, action.getSemanticAction());
        return bundle;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Bundle getExtras(Notification notification) {
        Object object = sExtrasLock;
        synchronized (object) {
            if (sExtrasFieldAccessFailed) {
                return null;
            }
            try {
                Field field;
                if (sExtrasField == null) {
                    field = Notification.class.getDeclaredField(KEY_EXTRAS);
                    if (!Bundle.class.isAssignableFrom(field.getType())) {
                        Log.e((String)TAG, (String)"Notification.extras field is not of type Bundle");
                        sExtrasFieldAccessFailed = true;
                        return null;
                    }
                    field.setAccessible(true);
                    sExtrasField = field;
                }
                Bundle bundle = (Bundle)sExtrasField.get(notification);
                field = bundle;
                if (bundle == null) {
                    field = new Bundle();
                    sExtrasField.set(notification, field);
                }
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.e((String)TAG, (String)"Unable to access notification extras", (Throwable)noSuchFieldException);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)TAG, (String)"Unable to access notification extras", (Throwable)illegalAccessException);
            }
            sExtrasFieldAccessFailed = true;
            return null;
        }
    }

    public static NotificationCompat.Action readAction(int n, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle) {
        boolean bl;
        RemoteInput[] remoteInputArray;
        RemoteInput[] remoteInputArray2;
        if (bundle != null) {
            remoteInputArray2 = NotificationCompatJellybean.fromBundleArray(NotificationCompatJellybean.getBundleArrayFromBundle(bundle, "android.support.remoteInputs"));
            remoteInputArray = NotificationCompatJellybean.fromBundleArray(NotificationCompatJellybean.getBundleArrayFromBundle(bundle, EXTRA_DATA_ONLY_REMOTE_INPUTS));
            bl = bundle.getBoolean(EXTRA_ALLOW_GENERATED_REPLIES);
        } else {
            remoteInputArray2 = null;
            remoteInputArray = null;
            bl = false;
        }
        return new NotificationCompat.Action(n, charSequence, pendingIntent, bundle, remoteInputArray2, remoteInputArray, bl, 0, true, false);
    }

    private static Bundle toBundle(RemoteInput object) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RESULT_KEY, ((RemoteInput)object).getResultKey());
        bundle.putCharSequence(KEY_LABEL, ((RemoteInput)object).getLabel());
        bundle.putCharSequenceArray(KEY_CHOICES, ((RemoteInput)object).getChoices());
        bundle.putBoolean(KEY_ALLOW_FREE_FORM_INPUT, ((RemoteInput)object).getAllowFreeFormInput());
        bundle.putBundle(KEY_EXTRAS, ((RemoteInput)object).getExtras());
        Object object2 = ((RemoteInput)object).getAllowedDataTypes();
        if (object2 != null && !object2.isEmpty()) {
            object = new ArrayList(object2.size());
            object2 = object2.iterator();
            while (object2.hasNext()) {
                ((ArrayList)object).add((String)object2.next());
            }
            bundle.putStringArrayList(KEY_ALLOWED_DATA_TYPES, (ArrayList)object);
        }
        return bundle;
    }

    private static Bundle[] toBundleArray(RemoteInput[] remoteInputArray) {
        if (remoteInputArray == null) {
            return null;
        }
        Bundle[] bundleArray = new Bundle[remoteInputArray.length];
        for (int i = 0; i < remoteInputArray.length; ++i) {
            bundleArray[i] = NotificationCompatJellybean.toBundle(remoteInputArray[i]);
        }
        return bundleArray;
    }

    public static Bundle writeActionAndGetExtras(Notification.Builder builder, NotificationCompat.Action action) {
        IconCompat iconCompat = action.getIconCompat();
        int n = iconCompat != null ? iconCompat.getResId() : 0;
        builder.addAction(n, action.getTitle(), action.getActionIntent());
        builder = new Bundle(action.getExtras());
        if (action.getRemoteInputs() != null) {
            builder.putParcelableArray("android.support.remoteInputs", (Parcelable[])NotificationCompatJellybean.toBundleArray(action.getRemoteInputs()));
        }
        if (action.getDataOnlyRemoteInputs() != null) {
            builder.putParcelableArray(EXTRA_DATA_ONLY_REMOTE_INPUTS, (Parcelable[])NotificationCompatJellybean.toBundleArray(action.getDataOnlyRemoteInputs()));
        }
        builder.putBoolean(EXTRA_ALLOW_GENERATED_REPLIES, action.getAllowGeneratedReplies());
        return builder;
    }
}

