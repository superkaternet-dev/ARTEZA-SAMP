/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.app.Application$ActivityLifecycleCallbacks
 *  android.content.res.Configuration
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Looper
 *  android.util.Log
 */
package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

final class ActivityRecreator {
    private static final String LOG_TAG = "ActivityRecreator";
    protected static final Class<?> activityThreadClass;
    private static final Handler mainHandler;
    protected static final Field mainThreadField;
    protected static final Method performStopActivity2ParamsMethod;
    protected static final Method performStopActivity3ParamsMethod;
    protected static final Method requestRelaunchActivityMethod;
    protected static final Field tokenField;

    static {
        mainHandler = new Handler(Looper.getMainLooper());
        Class<?> clazz = ActivityRecreator.getActivityThreadClass();
        activityThreadClass = clazz;
        mainThreadField = ActivityRecreator.getMainThreadField();
        tokenField = ActivityRecreator.getTokenField();
        performStopActivity3ParamsMethod = ActivityRecreator.getPerformStopActivity3Params(clazz);
        performStopActivity2ParamsMethod = ActivityRecreator.getPerformStopActivity2Params(clazz);
        requestRelaunchActivityMethod = ActivityRecreator.getRequestRelaunchActivityMethod(clazz);
    }

    private ActivityRecreator() {
    }

    private static Class<?> getActivityThreadClass() {
        try {
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            return clazz;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Field getMainThreadField() {
        try {
            Field field = Activity.class.getDeclaredField("mMainThread");
            field.setAccessible(true);
            return field;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Method getPerformStopActivity2Params(Class<?> genericDeclaration) {
        if (genericDeclaration == null) {
            return null;
        }
        try {
            genericDeclaration = ((Class)genericDeclaration).getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE);
            ((Method)genericDeclaration).setAccessible(true);
            return genericDeclaration;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Method getPerformStopActivity3Params(Class<?> genericDeclaration) {
        if (genericDeclaration == null) {
            return null;
        }
        try {
            genericDeclaration = ((Class)genericDeclaration).getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE, String.class);
            ((Method)genericDeclaration).setAccessible(true);
            return genericDeclaration;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Method getRequestRelaunchActivityMethod(Class<?> genericDeclaration) {
        if (ActivityRecreator.needsRelaunchCall() && genericDeclaration != null) {
            try {
                genericDeclaration = ((Class)genericDeclaration).getDeclaredMethod("requestRelaunchActivity", IBinder.class, List.class, List.class, Integer.TYPE, Boolean.TYPE, Configuration.class, Configuration.class, Boolean.TYPE, Boolean.TYPE);
                ((Method)genericDeclaration).setAccessible(true);
                return genericDeclaration;
            }
            catch (Throwable throwable) {
                return null;
            }
        }
        return null;
    }

    private static Field getTokenField() {
        try {
            Field field = Activity.class.getDeclaredField("mToken");
            field.setAccessible(true);
            return field;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static boolean needsRelaunchCall() {
        boolean bl = Build.VERSION.SDK_INT == 26 || Build.VERSION.SDK_INT == 27;
        return bl;
    }

    protected static boolean queueOnStopIfNecessary(Object object, Activity object2) {
        Object object3;
        block3: {
            try {
                object3 = tokenField.get(object2);
                if (object3 == object) break block3;
                return false;
            }
            catch (Throwable throwable) {
                Log.e((String)LOG_TAG, (String)"Exception while fetching field values", (Throwable)throwable);
                return false;
            }
        }
        object2 = mainThreadField.get(object2);
        object = mainHandler;
        Runnable runnable = new Runnable(object2, object3){
            final Object val$activityThread;
            final Object val$token;
            {
                this.val$activityThread = object;
                this.val$token = object2;
            }

            @Override
            public void run() {
                block5: {
                    try {
                        if (performStopActivity3ParamsMethod != null) {
                            performStopActivity3ParamsMethod.invoke(this.val$activityThread, this.val$token, false, "AppCompat recreation");
                        } else {
                            performStopActivity2ParamsMethod.invoke(this.val$activityThread, this.val$token, false);
                        }
                    }
                    catch (Throwable throwable) {
                        Log.e((String)ActivityRecreator.LOG_TAG, (String)"Exception while invoking performStopActivity", (Throwable)throwable);
                    }
                    catch (RuntimeException runtimeException) {
                        if (runtimeException.getClass() != RuntimeException.class || runtimeException.getMessage() == null || !runtimeException.getMessage().startsWith("Unable to stop")) break block5;
                        throw runtimeException;
                    }
                }
            }
        };
        object.postAtFrontOfQueue(runnable);
        return true;
    }

    /*
     * Loose catch block
     */
    static boolean recreate(Activity object) {
        Runnable runnable;
        Object object2;
        LifecycleCheckCallbacks lifecycleCheckCallbacks;
        Application application;
        block14: {
            Object object3;
            Object object4;
            block13: {
                block12: {
                    if (Build.VERSION.SDK_INT >= 28) {
                        object.recreate();
                        return true;
                    }
                    if (ActivityRecreator.needsRelaunchCall() && requestRelaunchActivityMethod == null) {
                        return false;
                    }
                    if (performStopActivity2ParamsMethod == null && performStopActivity3ParamsMethod == null) {
                        return false;
                    }
                    object4 = tokenField.get(object);
                    if (object4 != null) break block12;
                    return false;
                    {
                        catch (Throwable throwable) {
                            return false;
                        }
                    }
                }
                object3 = mainThreadField.get(object);
                if (object3 != null) break block13;
                return false;
            }
            application = object.getApplication();
            lifecycleCheckCallbacks = new LifecycleCheckCallbacks((Activity)object);
            application.registerActivityLifecycleCallbacks((Application.ActivityLifecycleCallbacks)lifecycleCheckCallbacks);
            object2 = mainHandler;
            runnable = new Runnable(lifecycleCheckCallbacks, object4){
                final LifecycleCheckCallbacks val$callbacks;
                final Object val$token;
                {
                    this.val$callbacks = lifecycleCheckCallbacks;
                    this.val$token = object;
                }

                @Override
                public void run() {
                    this.val$callbacks.currentlyRecreatingToken = this.val$token;
                }
            };
            object2.post(runnable);
            if (ActivityRecreator.needsRelaunchCall()) {
                requestRelaunchActivityMethod.invoke(object3, object4, null, null, 0, false, null, null, false, false);
                break block14;
            }
            object.recreate();
        }
        object = new Runnable(application, lifecycleCheckCallbacks){
            final Application val$application;
            final LifecycleCheckCallbacks val$callbacks;
            {
                this.val$application = application;
                this.val$callbacks = lifecycleCheckCallbacks;
            }

            @Override
            public void run() {
                this.val$application.unregisterActivityLifecycleCallbacks((Application.ActivityLifecycleCallbacks)this.val$callbacks);
            }
        };
        object2.post((Runnable)object);
        return true;
        catch (Throwable throwable) {
            runnable = mainHandler;
            object2 = new /* invalid duplicate definition of identical inner class */;
            runnable.post((Runnable)object2);
            throw throwable;
        }
    }

    private static final class LifecycleCheckCallbacks
    implements Application.ActivityLifecycleCallbacks {
        Object currentlyRecreatingToken;
        private Activity mActivity;
        private boolean mDestroyed = false;
        private boolean mStarted = false;
        private boolean mStopQueued = false;

        LifecycleCheckCallbacks(Activity activity) {
            this.mActivity = activity;
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
            if (this.mActivity == activity) {
                this.mActivity = null;
                this.mDestroyed = true;
            }
        }

        public void onActivityPaused(Activity activity) {
            if (this.mDestroyed && !this.mStopQueued && !this.mStarted && ActivityRecreator.queueOnStopIfNecessary(this.currentlyRecreatingToken, activity)) {
                this.mStopQueued = true;
                this.currentlyRecreatingToken = null;
            }
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
            if (this.mActivity == activity) {
                this.mStarted = true;
            }
        }

        public void onActivityStopped(Activity activity) {
        }
    }
}

