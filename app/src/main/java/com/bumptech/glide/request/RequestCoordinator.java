/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.request;

import com.bumptech.glide.request.Request;

public interface RequestCoordinator {
    public boolean canNotifyCleared(Request var1);

    public boolean canNotifyStatusChanged(Request var1);

    public boolean canSetImage(Request var1);

    public RequestCoordinator getRoot();

    public boolean isAnyResourceSet();

    public void onRequestFailed(Request var1);

    public void onRequestSuccess(Request var1);

    public static final class RequestState
    extends Enum<RequestState> {
        private static final RequestState[] $VALUES;
        public static final /* enum */ RequestState CLEARED;
        public static final /* enum */ RequestState FAILED;
        public static final /* enum */ RequestState PAUSED;
        public static final /* enum */ RequestState RUNNING;
        public static final /* enum */ RequestState SUCCESS;
        private final boolean isComplete;

        static {
            RequestState requestState;
            RequestState requestState2;
            RequestState requestState3;
            RequestState requestState4;
            RequestState requestState5;
            RUNNING = requestState5 = new RequestState(false);
            PAUSED = requestState4 = new RequestState(false);
            CLEARED = requestState3 = new RequestState(false);
            SUCCESS = requestState2 = new RequestState(true);
            FAILED = requestState = new RequestState(true);
            $VALUES = new RequestState[]{requestState5, requestState4, requestState3, requestState2, requestState};
        }

        private RequestState(boolean bl) {
            this.isComplete = bl;
        }

        public static RequestState valueOf(String string2) {
            return Enum.valueOf(RequestState.class, string2);
        }

        public static RequestState[] values() {
            return (RequestState[])$VALUES.clone();
        }

        boolean isComplete() {
            return this.isComplete;
        }
    }
}

