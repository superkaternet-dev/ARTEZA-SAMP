/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import org.ini4j.spi.HandlerBase;

public interface OptionsHandler
extends HandlerBase {
    public void endOptions();

    @Override
    public void handleComment(String var1);

    @Override
    public void handleOption(String var1, String var2);

    public void startOptions();
}

