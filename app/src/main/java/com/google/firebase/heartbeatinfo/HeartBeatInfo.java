/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.heartbeatinfo;

public interface HeartBeatInfo {
    public HeartBeat getHeartBeatCode(String var1);

    public static final class HeartBeat
    extends Enum<HeartBeat> {
        private static final HeartBeat[] $VALUES;
        public static final /* enum */ HeartBeat COMBINED;
        public static final /* enum */ HeartBeat GLOBAL;
        public static final /* enum */ HeartBeat NONE;
        public static final /* enum */ HeartBeat SDK;
        private final int code;

        static {
            HeartBeat heartBeat;
            HeartBeat heartBeat2;
            HeartBeat heartBeat3;
            HeartBeat heartBeat4;
            NONE = heartBeat4 = new HeartBeat(0);
            SDK = heartBeat3 = new HeartBeat(1);
            GLOBAL = heartBeat2 = new HeartBeat(2);
            COMBINED = heartBeat = new HeartBeat(3);
            $VALUES = new HeartBeat[]{heartBeat4, heartBeat3, heartBeat2, heartBeat};
        }

        private HeartBeat(int n2) {
            this.code = n2;
        }

        public static HeartBeat valueOf(String string2) {
            return Enum.valueOf(HeartBeat.class, string2);
        }

        public static HeartBeat[] values() {
            return (HeartBeat[])$VALUES.clone();
        }

        public int getCode() {
            return this.code;
        }
    }
}

