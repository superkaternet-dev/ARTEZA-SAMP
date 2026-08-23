/*
 * Decompiled with CFR 0.152.
 */
package com.wardrumstudios.utils;

import com.wardrumstudios.utils.WarBase;
import java.io.PrintStream;

public class WarBilling
extends WarBase {
    public void AddSKU(String string2) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**** AddSKU: ");
        stringBuilder.append(string2);
        printStream.println(stringBuilder.toString());
    }

    public boolean InitBilling() {
        System.out.println("**** InitBilling()");
        return true;
    }

    public String LocalizedPrice(String string2) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**** LocalizedPrice: ");
        stringBuilder.append(string2);
        printStream.println(stringBuilder.toString());
        return "";
    }

    public boolean RequestPurchase(String string2) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**** RequestPurchase: ");
        stringBuilder.append(string2);
        printStream.println(stringBuilder.toString());
        return true;
    }

    public void SetBillingKey(String string2) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**** SetBillingKey: ");
        stringBuilder.append(string2);
        printStream.println(stringBuilder.toString());
    }

    @Override
    public native void changeConnection(boolean var1);

    @Override
    public native void notifyChange(String var1, int var2);
}

