/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.os.Build$VERSION
 *  android.util.Log
 *  dalvik.system.DexFile
 */
package androidx.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import androidx.multidex.MultiDexExtractor;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

public final class MultiDex {
    private static final String CODE_CACHE_NAME = "code_cache";
    private static final String CODE_CACHE_SECONDARY_FOLDER_NAME = "secondary-dexes";
    private static final boolean IS_VM_MULTIDEX_CAPABLE;
    private static final int MAX_SUPPORTED_SDK_VERSION = 20;
    private static final int MIN_SDK_VERSION = 4;
    private static final String NO_KEY_PREFIX = "";
    private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
    static final String TAG = "MultiDex";
    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
    private static final Set<File> installedApk;

    static {
        installedApk = new HashSet<File>();
        IS_VM_MULTIDEX_CAPABLE = MultiDex.isVMMultidexCapable(System.getProperty("java.vm.version"));
    }

    private MultiDex() {
    }

    private static void clearOldDexDir(Context object) throws Exception {
        if (((File)(object = new File(object.getFilesDir(), "secondary-dexes"))).isDirectory()) {
            StringBuilder comparable2 = new StringBuilder();
            comparable2.append("Clearing old secondary dex dir (");
            comparable2.append(((File)object).getPath());
            comparable2.append(").");
            Log.i((String)TAG, (String)comparable2.toString());
            File[] fileArray = ((File)object).listFiles();
            if (fileArray == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to list secondary dex dir content (");
                stringBuilder.append(((File)object).getPath());
                stringBuilder.append(").");
                Log.w((String)TAG, (String)stringBuilder.toString());
                return;
            }
            for (File file : fileArray) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Trying to delete old file ");
                stringBuilder.append(file.getPath());
                stringBuilder.append(" of size ");
                stringBuilder.append(file.length());
                Log.i((String)TAG, (String)stringBuilder.toString());
                if (!file.delete()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to delete old file ");
                    stringBuilder.append(file.getPath());
                    Log.w((String)TAG, (String)stringBuilder.toString());
                    continue;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Deleted old file ");
                stringBuilder.append(file.getPath());
                Log.i((String)TAG, (String)stringBuilder.toString());
            }
            if (!((File)object).delete()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete secondary dex dir ");
                stringBuilder.append(((File)object).getPath());
                Log.w((String)TAG, (String)stringBuilder.toString());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Deleted old secondary dex dir ");
                stringBuilder.append(((File)object).getPath());
                Log.i((String)TAG, (String)stringBuilder.toString());
            }
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void doInstallation(Context object, File file, File object2, String object3, String string2, boolean bl) throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException {
        Set<File> set = installedApk;
        synchronized (set) {
            IOException iOException222222;
            block21: {
                Object object4;
                block20: {
                    if (set.contains(file)) {
                        return;
                    }
                    set.add(file);
                    if (Build.VERSION.SDK_INT > 20) {
                        object4 = new StringBuilder();
                        ((StringBuilder)object4).append("MultiDex is not guaranteed to work in SDK version ");
                        ((StringBuilder)object4).append(Build.VERSION.SDK_INT);
                        ((StringBuilder)object4).append(": SDK version higher than ");
                        ((StringBuilder)object4).append(20);
                        ((StringBuilder)object4).append(" should be backed by ");
                        ((StringBuilder)object4).append("runtime with built-in multidex capabilty but it's not the ");
                        ((StringBuilder)object4).append("case here: java.vm.version=\"");
                        ((StringBuilder)object4).append(System.getProperty("java.vm.version"));
                        ((StringBuilder)object4).append("\"");
                        Log.w((String)TAG, (String)((StringBuilder)object4).toString());
                    }
                    object4 = object.getClassLoader();
                    if (object4 != null) break block20;
                    Log.e((String)TAG, (String)"Context class loader is null. Must be running in test mode. Skip patching.");
                    return;
                }
                try {
                    MultiDex.clearOldDexDir(object);
                }
                catch (Throwable throwable) {
                    Log.w((String)TAG, (String)"Something went wrong when trying to clear old MultiDex extraction, continuing without cleaning.", (Throwable)throwable);
                }
                object3 = MultiDex.getDexDir(object, (File)object2, (String)object3);
                object2 = new MultiDexExtractor(file, (File)object3);
                file = null;
                try {
                    List<? extends File> list = ((MultiDexExtractor)object2).load((Context)object, string2, false);
                    try {
                        MultiDex.installSecondaryDexes((ClassLoader)object4, (File)object3, list);
                    }
                    catch (IOException iOException222222) {
                        if (!bl) break block21;
                        Log.w((String)TAG, (String)"Failed to install extracted secondary dex files, retrying with forced extraction", (Throwable)iOException222222);
                        MultiDex.installSecondaryDexes((ClassLoader)object4, (File)object3, ((MultiDexExtractor)object2).load((Context)object, string2, true));
                    }
                }
                catch (Throwable throwable) {
                    try {
                        ((MultiDexExtractor)object2).close();
                        throw throwable;
                    }
                    catch (IOException iOException3) {
                        // empty catch block
                    }
                    throw throwable;
                }
                try {
                    ((MultiDexExtractor)object2).close();
                    object = file;
                }
                catch (IOException iOException4) {
                    // empty catch block
                }
                if (object != null) throw object;
                return;
            }
            throw iOException222222;
            catch (RuntimeException runtimeException) {
                Log.w((String)TAG, (String)"Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", (Throwable)runtimeException);
                return;
            }
        }
    }

    private static void expandFieldArray(Object object, String objectArray, Object[] objectArray2) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = MultiDex.findField(object, (String)objectArray);
        Object[] objectArray3 = (Object[])field.get(object);
        objectArray = (Object[])Array.newInstance(objectArray3.getClass().getComponentType(), objectArray3.length + objectArray2.length);
        System.arraycopy(objectArray3, 0, objectArray, 0, objectArray3.length);
        System.arraycopy(objectArray2, 0, objectArray, objectArray3.length, objectArray2.length);
        field.set(object, objectArray);
    }

    private static Field findField(Object object, String string2) throws NoSuchFieldException {
        Serializable serializable;
        for (serializable = object.getClass(); serializable != null; serializable = ((Class)serializable).getSuperclass()) {
            try {
                Field field = ((Class)serializable).getDeclaredField(string2);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                continue;
            }
        }
        serializable = new StringBuilder();
        ((StringBuilder)serializable).append("Field ");
        ((StringBuilder)serializable).append(string2);
        ((StringBuilder)serializable).append(" not found in ");
        ((StringBuilder)serializable).append(object.getClass());
        object = new NoSuchFieldException(((StringBuilder)serializable).toString());
        throw object;
    }

    private static Method findMethod(Object object, String string2, Class<?> ... classArray) throws NoSuchMethodException {
        Serializable serializable;
        for (serializable = object.getClass(); serializable != null; serializable = ((Class)serializable).getSuperclass()) {
            try {
                Method method = ((Class)serializable).getDeclaredMethod(string2, classArray);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                continue;
            }
        }
        serializable = new StringBuilder();
        ((StringBuilder)serializable).append("Method ");
        ((StringBuilder)serializable).append(string2);
        ((StringBuilder)serializable).append(" with parameters ");
        ((StringBuilder)serializable).append(Arrays.asList(classArray));
        ((StringBuilder)serializable).append(" not found in ");
        ((StringBuilder)serializable).append(object.getClass());
        object = new NoSuchMethodException(((StringBuilder)serializable).toString());
        throw object;
    }

    private static ApplicationInfo getApplicationInfo(Context context) {
        try {
            context = context.getApplicationInfo();
            return context;
        }
        catch (RuntimeException runtimeException) {
            Log.w((String)TAG, (String)"Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", (Throwable)runtimeException);
            return null;
        }
    }

    private static File getDexDir(Context object, File file, String string2) throws IOException {
        file = new File(file, CODE_CACHE_NAME);
        try {
            MultiDex.mkdirChecked(file);
            object = file;
        }
        catch (IOException iOException) {
            object = new File(object.getFilesDir(), CODE_CACHE_NAME);
            MultiDex.mkdirChecked((File)object);
        }
        object = new File((File)object, string2);
        MultiDex.mkdirChecked((File)object);
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void install(Context object) {
        Log.i((String)TAG, (String)"Installing application");
        if (IS_VM_MULTIDEX_CAPABLE) {
            Log.i((String)TAG, (String)"VM has multidex support, MultiDex support library is disabled.");
            return;
        }
        if (Build.VERSION.SDK_INT < 4) {
            object = new StringBuilder();
            ((StringBuilder)object).append("MultiDex installation failed. SDK ");
            ((StringBuilder)object).append(Build.VERSION.SDK_INT);
            ((StringBuilder)object).append(" is unsupported. Min SDK version is ");
            ((StringBuilder)object).append(4);
            ((StringBuilder)object).append(".");
            throw new RuntimeException(((StringBuilder)object).toString());
        }
        try {
            ApplicationInfo applicationInfo = MultiDex.getApplicationInfo((Context)object);
            if (applicationInfo == null) {
                Log.i((String)TAG, (String)"No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                return;
            }
            File file = new File(applicationInfo.sourceDir);
            File file2 = new File(applicationInfo.dataDir);
            MultiDex.doInstallation((Context)object, file, file2, "secondary-dexes", NO_KEY_PREFIX, true);
        }
        catch (Exception exception) {
            Log.e((String)TAG, (String)"MultiDex installation failure", (Throwable)exception);
            object = new StringBuilder();
            ((StringBuilder)object).append("MultiDex installation failed (");
            ((StringBuilder)object).append(exception.getMessage());
            ((StringBuilder)object).append(").");
            throw new RuntimeException(((StringBuilder)object).toString());
        }
        Log.i((String)TAG, (String)"install done");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void installInstrumentation(Context object, Context object2) {
        Log.i((String)TAG, (String)"Installing instrumentation");
        if (IS_VM_MULTIDEX_CAPABLE) {
            Log.i((String)TAG, (String)"VM has multidex support, MultiDex support library is disabled.");
            return;
        }
        if (Build.VERSION.SDK_INT < 4) {
            object = new StringBuilder();
            ((StringBuilder)object).append("MultiDex installation failed. SDK ");
            ((StringBuilder)object).append(Build.VERSION.SDK_INT);
            ((StringBuilder)object).append(" is unsupported. Min SDK version is ");
            ((StringBuilder)object).append(4);
            ((StringBuilder)object).append(".");
            throw new RuntimeException(((StringBuilder)object).toString());
        }
        try {
            Object object3 = MultiDex.getApplicationInfo((Context)object);
            if (object3 == null) {
                Log.i((String)TAG, (String)"No ApplicationInfo available for instrumentation, i.e. running on a test Context: MultiDex support library is disabled.");
                return;
            }
            ApplicationInfo applicationInfo = MultiDex.getApplicationInfo((Context)object2);
            if (applicationInfo == null) {
                Log.i((String)TAG, (String)"No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(object.getPackageName());
            stringBuilder.append(".");
            String string2 = stringBuilder.toString();
            object = new File(applicationInfo.dataDir);
            File file = new File(((ApplicationInfo)object3).sourceDir);
            object3 = new StringBuilder();
            ((StringBuilder)object3).append(string2);
            ((StringBuilder)object3).append("secondary-dexes");
            MultiDex.doInstallation((Context)object2, file, (File)object, ((StringBuilder)object3).toString(), string2, false);
            object3 = new File(applicationInfo.sourceDir);
            MultiDex.doInstallation((Context)object2, (File)object3, (File)object, "secondary-dexes", NO_KEY_PREFIX, false);
        }
        catch (Exception exception) {
            Log.e((String)TAG, (String)"MultiDex installation failure", (Throwable)exception);
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("MultiDex installation failed (");
            ((StringBuilder)object2).append(exception.getMessage());
            ((StringBuilder)object2).append(").");
            throw new RuntimeException(((StringBuilder)object2).toString());
        }
        Log.i((String)TAG, (String)"Installation done");
    }

    private static void installSecondaryDexes(ClassLoader classLoader, File file, List<? extends File> list) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, SecurityException, ClassNotFoundException, InstantiationException {
        if (!list.isEmpty()) {
            if (Build.VERSION.SDK_INT >= 19) {
                V19.install(classLoader, list, file);
            } else if (Build.VERSION.SDK_INT >= 14) {
                V14.install(classLoader, list);
            } else {
                V4.install(classLoader, list);
            }
        }
    }

    static boolean isVMMultidexCapable(String string2) {
        Object object;
        boolean bl;
        boolean bl2 = bl = false;
        if (string2 != null) {
            object = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(string2);
            bl2 = bl;
            if (((Matcher)object).matches()) {
                boolean bl3 = true;
                try {
                    int n = Integer.parseInt(((Matcher)object).group(1));
                    int n2 = Integer.parseInt(((Matcher)object).group(2));
                    bl2 = bl3;
                    if (n <= 2) {
                        bl2 = n == 2 && n2 >= 1 ? bl3 : false;
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    bl2 = bl;
                }
            }
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("VM with version ");
        ((StringBuilder)object).append(string2);
        string2 = bl2 ? " has multidex support" : " does not have multidex support";
        ((StringBuilder)object).append(string2);
        Log.i((String)TAG, (String)((StringBuilder)object).toString());
        return bl2;
    }

    private static void mkdirChecked(File file) throws IOException {
        file.mkdir();
        if (!file.isDirectory()) {
            Comparable<File> comparable = file.getParentFile();
            if (comparable == null) {
                comparable = new StringBuilder();
                ((StringBuilder)comparable).append("Failed to create dir ");
                ((StringBuilder)comparable).append(file.getPath());
                ((StringBuilder)comparable).append(". Parent file is null.");
                Log.e((String)TAG, (String)((StringBuilder)comparable).toString());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to create dir ");
                stringBuilder.append(file.getPath());
                stringBuilder.append(". parent file is a dir ");
                stringBuilder.append(((File)comparable).isDirectory());
                stringBuilder.append(", a file ");
                stringBuilder.append(((File)comparable).isFile());
                stringBuilder.append(", exists ");
                stringBuilder.append(((File)comparable).exists());
                stringBuilder.append(", readable ");
                stringBuilder.append(((File)comparable).canRead());
                stringBuilder.append(", writable ");
                stringBuilder.append(((File)comparable).canWrite());
                Log.e((String)TAG, (String)stringBuilder.toString());
            }
            comparable = new StringBuilder();
            ((StringBuilder)comparable).append("Failed to create directory ");
            ((StringBuilder)comparable).append(file.getPath());
            throw new IOException(((StringBuilder)comparable).toString());
        }
    }

    private static final class V14 {
        private static final int EXTRACTED_SUFFIX_LENGTH = ".zip".length();
        private final ElementConstructor elementConstructor;

        private V14() throws ClassNotFoundException, SecurityException, NoSuchMethodException {
            ElementConstructor elementConstructor;
            Class<?> clazz = Class.forName("dalvik.system.DexPathList$Element");
            try {
                elementConstructor = new ICSElementConstructor(clazz);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                try {
                    super(clazz);
                }
                catch (NoSuchMethodException noSuchMethodException2) {
                    elementConstructor = new JBMR2ElementConstructor(clazz);
                }
            }
            this.elementConstructor = elementConstructor;
        }

        static void install(ClassLoader object, List<? extends File> objectArray) throws IOException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            object = MultiDex.findField(object, "pathList").get(object);
            objectArray = new V14().makeDexElements((List<? extends File>)objectArray);
            try {
                MultiDex.expandFieldArray(object, "dexElements", objectArray);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.w((String)MultiDex.TAG, (String)"Failed find field 'dexElements' attempting 'pathElements'", (Throwable)noSuchFieldException);
                MultiDex.expandFieldArray(object, "pathElements", objectArray);
            }
        }

        private Object[] makeDexElements(List<? extends File> list) throws IOException, SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
            Object[] objectArray = new Object[list.size()];
            for (int i = 0; i < objectArray.length; ++i) {
                File file = list.get(i);
                objectArray[i] = this.elementConstructor.newInstance(file, DexFile.loadDex((String)file.getPath(), (String)V14.optimizedPathFor(file), (int)0));
            }
            return objectArray;
        }

        private static String optimizedPathFor(File comparable) {
            File file = ((File)comparable).getParentFile();
            String string2 = ((File)comparable).getName();
            comparable = new StringBuilder();
            ((StringBuilder)comparable).append(string2.substring(0, string2.length() - EXTRACTED_SUFFIX_LENGTH));
            ((StringBuilder)comparable).append(".dex");
            return new File(file, ((StringBuilder)comparable).toString()).getPath();
        }

        private static interface ElementConstructor {
            public Object newInstance(File var1, DexFile var2) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException;
        }

        private static class ICSElementConstructor
        implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            ICSElementConstructor(Class<?> genericDeclaration) throws SecurityException, NoSuchMethodException {
                genericDeclaration = ((Class)genericDeclaration).getConstructor(File.class, ZipFile.class, DexFile.class);
                this.elementConstructor = genericDeclaration;
                ((Constructor)genericDeclaration).setAccessible(true);
            }

            @Override
            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
                return this.elementConstructor.newInstance(file, new ZipFile(file), dexFile);
            }
        }

        private static class JBMR11ElementConstructor
        implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            JBMR11ElementConstructor(Class<?> genericDeclaration) throws SecurityException, NoSuchMethodException {
                genericDeclaration = ((Class)genericDeclaration).getConstructor(File.class, File.class, DexFile.class);
                this.elementConstructor = genericDeclaration;
                ((Constructor)genericDeclaration).setAccessible(true);
            }

            @Override
            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return this.elementConstructor.newInstance(file, file, dexFile);
            }
        }

        private static class JBMR2ElementConstructor
        implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            JBMR2ElementConstructor(Class<?> genericDeclaration) throws SecurityException, NoSuchMethodException {
                genericDeclaration = ((Class)genericDeclaration).getConstructor(File.class, Boolean.TYPE, File.class, DexFile.class);
                this.elementConstructor = genericDeclaration;
                ((Constructor)genericDeclaration).setAccessible(true);
            }

            @Override
            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return this.elementConstructor.newInstance(file, Boolean.FALSE, file, dexFile);
            }
        }
    }

    private static final class V19 {
        private V19() {
        }

        static void install(ClassLoader object, List<? extends File> object2, File iOExceptionArray) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException {
            Object object3 = MultiDex.findField(object, "pathList").get(object);
            ArrayList<IOException> arrayList = new ArrayList<IOException>();
            MultiDex.expandFieldArray(object3, "dexElements", V19.makeDexElements(object3, new ArrayList<File>((Collection<File>)object2), (File)iOExceptionArray, arrayList));
            if (arrayList.size() > 0) {
                object = arrayList.iterator();
                while (object.hasNext()) {
                    Log.w((String)MultiDex.TAG, (String)"Exception in makeDexElement", (Throwable)((IOException)object.next()));
                }
                object2 = MultiDex.findField(object3, "dexElementsSuppressedExceptions");
                iOExceptionArray = (IOException[])((Field)object2).get(object3);
                if (iOExceptionArray == null) {
                    object = arrayList.toArray(new IOException[arrayList.size()]);
                } else {
                    object = new IOException[arrayList.size() + iOExceptionArray.length];
                    arrayList.toArray((T[])object);
                    System.arraycopy(iOExceptionArray, 0, object, arrayList.size(), iOExceptionArray.length);
                }
                ((Field)object2).set(object3, object);
                object = new IOException("I/O exception during makeDexElement");
                ((Throwable)object).initCause(arrayList.get(0));
                throw object;
            }
        }

        private static Object[] makeDexElements(Object object, ArrayList<File> arrayList, File file, ArrayList<IOException> arrayList2) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            return (Object[])MultiDex.findMethod(object, "makeDexElements", new Class[]{ArrayList.class, File.class, ArrayList.class}).invoke(object, arrayList, file, arrayList2);
        }
    }

    private static final class V4 {
        private V4() {
        }

        static void install(ClassLoader classLoader, List<? extends File> object) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException {
            int n = object.size();
            Field field = MultiDex.findField(classLoader, "path");
            StringBuilder stringBuilder = new StringBuilder((String)field.get(classLoader));
            Object[] objectArray = new String[n];
            Object[] objectArray2 = new File[n];
            Object[] objectArray3 = new ZipFile[n];
            Object[] objectArray4 = new DexFile[n];
            ListIterator<? extends File> listIterator = object.listIterator();
            while (listIterator.hasNext()) {
                Comparable<File> comparable = listIterator.next();
                object = ((File)comparable).getAbsolutePath();
                stringBuilder.append(':');
                stringBuilder.append((String)object);
                n = listIterator.previousIndex();
                objectArray[n] = object;
                objectArray2[n] = comparable;
                objectArray3[n] = new ZipFile((File)comparable);
                comparable = new StringBuilder();
                ((StringBuilder)comparable).append((String)object);
                ((StringBuilder)comparable).append(".dex");
                objectArray4[n] = DexFile.loadDex((String)object, (String)((StringBuilder)comparable).toString(), (int)0);
            }
            field.set(classLoader, stringBuilder.toString());
            MultiDex.expandFieldArray(classLoader, "mPaths", objectArray);
            MultiDex.expandFieldArray(classLoader, "mFiles", objectArray2);
            MultiDex.expandFieldArray(classLoader, "mZips", objectArray3);
            MultiDex.expandFieldArray(classLoader, "mDexs", objectArray4);
        }
    }
}

