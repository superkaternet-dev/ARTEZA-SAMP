/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Size
 *  android.util.SizeF
 *  android.util.SparseArray
 */
package androidx.lifecycle;

import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import androidx.lifecycle.MutableLiveData;
import androidx.savedstate.SavedStateRegistry;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SavedStateHandle {
    private static final Class[] ACCEPTABLE_CLASSES;
    private static final String KEYS = "keys";
    private static final String VALUES = "values";
    private final Map<String, SavingStateLiveData<?>> mLiveDatas;
    final Map<String, Object> mRegular;
    private final SavedStateRegistry.SavedStateProvider mSavedStateProvider;
    final Map<String, SavedStateRegistry.SavedStateProvider> mSavedStateProviders = new HashMap<String, SavedStateRegistry.SavedStateProvider>();

    static {
        Class<Boolean> clazz = Boolean.TYPE;
        Class<Double> clazz2 = Double.TYPE;
        Class<Integer> clazz3 = Integer.TYPE;
        Class<Long> clazz4 = Long.TYPE;
        Class<Byte> clazz5 = Byte.TYPE;
        Class<Character> clazz6 = Character.TYPE;
        Class<Float> clazz7 = Float.TYPE;
        Class<Short> clazz8 = Short.TYPE;
        Class clazz9 = Build.VERSION.SDK_INT >= 21 ? Size.class : Integer.TYPE;
        Class clazz10 = Build.VERSION.SDK_INT >= 21 ? SizeF.class : Integer.TYPE;
        ACCEPTABLE_CLASSES = new Class[]{clazz, boolean[].class, clazz2, double[].class, clazz3, int[].class, clazz4, long[].class, String.class, String[].class, Binder.class, Bundle.class, clazz5, byte[].class, clazz6, char[].class, CharSequence.class, CharSequence[].class, ArrayList.class, clazz7, float[].class, Parcelable.class, Parcelable[].class, Serializable.class, clazz8, short[].class, SparseArray.class, clazz9, clazz10};
    }

    public SavedStateHandle() {
        this.mLiveDatas = new HashMap();
        this.mSavedStateProvider = new SavedStateRegistry.SavedStateProvider(this){
            final SavedStateHandle this$0;
            {
                this.this$0 = savedStateHandle;
            }

            @Override
            public Bundle saveState() {
                Object object;
                for (Map.Entry bundle2 : new HashMap<String, SavedStateRegistry.SavedStateProvider>(this.this$0.mSavedStateProviders).entrySet()) {
                    object = ((SavedStateRegistry.SavedStateProvider)bundle2.getValue()).saveState();
                    this.this$0.set((String)bundle2.getKey(), object);
                }
                Set<String> set = this.this$0.mRegular.keySet();
                ArrayList arrayList = new ArrayList(set.size());
                object = new ArrayList(arrayList.size());
                for (String string2 : set) {
                    arrayList.add(string2);
                    ((ArrayList)object).add(this.this$0.mRegular.get(string2));
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(SavedStateHandle.KEYS, arrayList);
                bundle.putParcelableArrayList(SavedStateHandle.VALUES, (ArrayList)object);
                return bundle;
            }
        };
        this.mRegular = new HashMap<String, Object>();
    }

    public SavedStateHandle(Map<String, Object> map) {
        this.mLiveDatas = new HashMap();
        this.mSavedStateProvider = new /* invalid duplicate definition of identical inner class */;
        this.mRegular = new HashMap<String, Object>(map);
    }

    static SavedStateHandle createHandle(Bundle object, Bundle object2) {
        if (object == null && object2 == null) {
            return new SavedStateHandle();
        }
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (object2 != null) {
            for (String string2 : object2.keySet()) {
                hashMap.put(string2, object2.get(string2));
            }
        }
        if (object == null) {
            return new SavedStateHandle(hashMap);
        }
        object2 = object.getParcelableArrayList(KEYS);
        object = object.getParcelableArrayList(VALUES);
        if (object2 != null && object != null && ((ArrayList)object2).size() == ((ArrayList)object).size()) {
            for (int i = 0; i < ((ArrayList)object2).size(); ++i) {
                hashMap.put((String)((ArrayList)object2).get(i), ((ArrayList)object).get(i));
            }
            return new SavedStateHandle(hashMap);
        }
        object = new IllegalStateException("Invalid bundle passed as restored state");
        throw object;
    }

    private <T> MutableLiveData<T> getLiveDataInternal(String string2, boolean bl, T object) {
        MutableLiveData mutableLiveData = this.mLiveDatas.get(string2);
        if (mutableLiveData != null) {
            return mutableLiveData;
        }
        object = this.mRegular.containsKey(string2) ? new SavingStateLiveData<Object>(this, string2, this.mRegular.get(string2)) : (bl ? new SavingStateLiveData<T>(this, string2, object) : new SavingStateLiveData(this, string2));
        this.mLiveDatas.put(string2, (SavingStateLiveData<?>)object);
        return object;
    }

    private static void validateValue(Object object) {
        if (object == null) {
            return;
        }
        Object object2 = ACCEPTABLE_CLASSES;
        int n = ((Class[])object2).length;
        for (int i = 0; i < n; ++i) {
            if (!object2[i].isInstance(object)) continue;
            return;
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("Can't put value with type ");
        ((StringBuilder)object2).append(object.getClass());
        ((StringBuilder)object2).append(" into saved state");
        object = new IllegalArgumentException(((StringBuilder)object2).toString());
        throw object;
    }

    public void clearSavedStateProvider(String string2) {
        this.mSavedStateProviders.remove(string2);
    }

    public boolean contains(String string2) {
        return this.mRegular.containsKey(string2);
    }

    public <T> T get(String string2) {
        return (T)this.mRegular.get(string2);
    }

    public <T> MutableLiveData<T> getLiveData(String string2) {
        return this.getLiveDataInternal(string2, false, null);
    }

    public <T> MutableLiveData<T> getLiveData(String string2, T t) {
        return this.getLiveDataInternal(string2, true, t);
    }

    public Set<String> keys() {
        HashSet<String> hashSet = new HashSet<String>(this.mRegular.keySet());
        hashSet.addAll(this.mSavedStateProviders.keySet());
        hashSet.addAll(this.mLiveDatas.keySet());
        return hashSet;
    }

    public <T> T remove(String object) {
        Object object2 = this.mRegular.remove(object);
        if ((object = this.mLiveDatas.remove(object)) != null) {
            ((SavingStateLiveData)object).detach();
        }
        return (T)object2;
    }

    SavedStateRegistry.SavedStateProvider savedStateProvider() {
        return this.mSavedStateProvider;
    }

    public <T> void set(String string2, T t) {
        SavedStateHandle.validateValue(t);
        MutableLiveData mutableLiveData = this.mLiveDatas.get(string2);
        if (mutableLiveData != null) {
            mutableLiveData.setValue(t);
        } else {
            this.mRegular.put(string2, t);
        }
    }

    public void setSavedStateProvider(String string2, SavedStateRegistry.SavedStateProvider savedStateProvider) {
        this.mSavedStateProviders.put(string2, savedStateProvider);
    }

    static class SavingStateLiveData<T>
    extends MutableLiveData<T> {
        private SavedStateHandle mHandle;
        private String mKey;

        SavingStateLiveData(SavedStateHandle savedStateHandle, String string2) {
            this.mKey = string2;
            this.mHandle = savedStateHandle;
        }

        SavingStateLiveData(SavedStateHandle savedStateHandle, String string2, T t) {
            super(t);
            this.mKey = string2;
            this.mHandle = savedStateHandle;
        }

        void detach() {
            this.mHandle = null;
        }

        @Override
        public void setValue(T t) {
            SavedStateHandle savedStateHandle = this.mHandle;
            if (savedStateHandle != null) {
                savedStateHandle.mRegular.put(this.mKey, t);
            }
            super.setValue(t);
        }
    }
}

