/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class Transformations {
    private Transformations() {
    }

    public static <X, Y> LiveData<Y> map(LiveData<X> liveData, Function<X, Y> function) {
        MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<X>(mediatorLiveData, function){
            final Function val$mapFunction;
            final MediatorLiveData val$result;
            {
                this.val$result = mediatorLiveData;
                this.val$mapFunction = function;
            }

            @Override
            public void onChanged(X x) {
                this.val$result.setValue(this.val$mapFunction.apply(x));
            }
        });
        return mediatorLiveData;
    }

    public static <X, Y> LiveData<Y> switchMap(LiveData<X> liveData, Function<X, LiveData<Y>> function) {
        MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<X>(function, mediatorLiveData){
            LiveData<Y> mSource;
            final MediatorLiveData val$result;
            final Function val$switchMapFunction;
            {
                this.val$switchMapFunction = function;
                this.val$result = mediatorLiveData;
            }

            @Override
            public void onChanged(X object) {
                LiveData liveData = this.mSource;
                if (liveData == (object = (LiveData)this.val$switchMapFunction.apply(object))) {
                    return;
                }
                if (liveData != null) {
                    this.val$result.removeSource(liveData);
                }
                this.mSource = object;
                if (object != null) {
                    this.val$result.addSource(object, new Observer<Y>(this){
                        final 2 this$0;
                        {
                            this.this$0 = var1_1;
                        }

                        @Override
                        public void onChanged(Y y) {
                            this.this$0.val$result.setValue(y);
                        }
                    });
                }
            }
        });
        return mediatorLiveData;
    }
}

