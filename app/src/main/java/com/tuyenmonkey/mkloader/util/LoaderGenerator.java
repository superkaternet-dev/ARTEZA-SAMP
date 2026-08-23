/*
 * Decompiled with CFR 0.152.
 */
package com.tuyenmonkey.mkloader.util;

import com.tuyenmonkey.mkloader.exception.InvalidNumberOfPulseException;
import com.tuyenmonkey.mkloader.type.ClassicSpinner;
import com.tuyenmonkey.mkloader.type.FishSpinner;
import com.tuyenmonkey.mkloader.type.LineSpinner;
import com.tuyenmonkey.mkloader.type.LoaderView;
import com.tuyenmonkey.mkloader.type.PhoneWave;
import com.tuyenmonkey.mkloader.type.Pulse;
import com.tuyenmonkey.mkloader.type.Radar;
import com.tuyenmonkey.mkloader.type.Sharingan;
import com.tuyenmonkey.mkloader.type.TwinFishesSpinner;
import com.tuyenmonkey.mkloader.type.Whirlpool;
import com.tuyenmonkey.mkloader.type.Worm;

public class LoaderGenerator {
    public static LoaderView generateLoaderView(int n) {
        switch (n) {
            default: {
                return new ClassicSpinner();
            }
            case 11: {
                return new Sharingan();
            }
            case 10: {
                return new PhoneWave();
            }
            case 9: {
                return new Whirlpool();
            }
            case 8: {
                return new Worm();
            }
            case 7: {
                return new TwinFishesSpinner();
            }
            case 3: {
                try {
                    Pulse pulse = new Pulse(3);
                    return pulse;
                }
                catch (InvalidNumberOfPulseException invalidNumberOfPulseException) {
                    invalidNumberOfPulseException.printStackTrace();
                }
            }
            case 4: {
                try {
                    Pulse pulse = new Pulse(4);
                    return pulse;
                }
                catch (InvalidNumberOfPulseException invalidNumberOfPulseException) {
                    invalidNumberOfPulseException.printStackTrace();
                }
            }
            case 5: {
                try {
                    Pulse pulse = new Pulse(5);
                    return pulse;
                }
                catch (InvalidNumberOfPulseException invalidNumberOfPulseException) {
                    invalidNumberOfPulseException.printStackTrace();
                }
            }
            case 6: {
                return new Radar();
            }
            case 2: {
                return new LineSpinner();
            }
            case 1: {
                return new FishSpinner();
            }
            case 0: 
        }
        return new ClassicSpinner();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static LoaderView generateLoaderView(String object) {
        int n;
        block34: {
            switch (((String)object).hashCode()) {
                case 975043943: {
                    if (!((String)object).equals("PhoneWave")) break;
                    n = 10;
                    break block34;
                }
                case 958968807: {
                    if (!((String)object).equals("FivePulse")) break;
                    n = 5;
                    break block34;
                }
                case 905524411: {
                    if (!((String)object).equals("ThreePulse")) break;
                    n = 3;
                    break block34;
                }
                case 776041799: {
                    if (!((String)object).equals("ClassicSpinner")) break;
                    n = 0;
                    break block34;
                }
                case 426399209: {
                    if (!((String)object).equals("Sharingan")) break;
                    n = 11;
                    break block34;
                }
                case 299449070: {
                    if (!((String)object).equals("Whirlpool")) break;
                    n = 9;
                    break block34;
                }
                case 78717670: {
                    if (!((String)object).equals("Radar")) break;
                    n = 6;
                    break block34;
                }
                case 50472805: {
                    if (!((String)object).equals("LineSpinner")) break;
                    n = 2;
                    break block34;
                }
                case 2702131: {
                    if (!((String)object).equals("Worm")) break;
                    n = 8;
                    break block34;
                }
                case -805352437: {
                    if (!((String)object).equals("TwinFishesSpinner")) break;
                    n = 7;
                    break block34;
                }
                case -1566594943: {
                    if (!((String)object).equals("FishSpinner")) break;
                    n = 1;
                    break block34;
                }
                case -1984395789: {
                    if (!((String)object).equals("FourPulse")) break;
                    n = 4;
                    break block34;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                return new ClassicSpinner();
            }
            case 11: {
                return new Sharingan();
            }
            case 10: {
                return new PhoneWave();
            }
            case 9: {
                return new Whirlpool();
            }
            case 8: {
                return new Worm();
            }
            case 7: {
                return new TwinFishesSpinner();
            }
            case 3: {
                try {
                    return new Pulse(3);
                }
                catch (InvalidNumberOfPulseException invalidNumberOfPulseException) {
                    invalidNumberOfPulseException.printStackTrace();
                }
            }
            case 4: {
                try {
                    return new Pulse(4);
                }
                catch (InvalidNumberOfPulseException invalidNumberOfPulseException) {
                    invalidNumberOfPulseException.printStackTrace();
                }
            }
            case 5: {
                try {
                    return new Pulse(5);
                }
                catch (InvalidNumberOfPulseException invalidNumberOfPulseException) {
                    invalidNumberOfPulseException.printStackTrace();
                }
            }
            case 6: {
                return new Radar();
            }
            case 2: {
                return new LineSpinner();
            }
            case 1: {
                return new FishSpinner();
            }
            case 0: 
        }
        return new ClassicSpinner();
    }
}

