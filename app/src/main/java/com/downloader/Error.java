/*
 * Decompiled with CFR 0.152.
 */
package com.downloader;

import java.util.List;
import java.util.Map;

public class Error {
    private Throwable connectionException;
    private Map<String, List<String>> headerFields;
    private boolean isConnectionError;
    private boolean isServerError;
    private int responseCode;
    private String serverErrorMessage;

    public Throwable getConnectionException() {
        return this.connectionException;
    }

    public Map<String, List<String>> getHeaderFields() {
        return this.headerFields;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getServerErrorMessage() {
        return this.serverErrorMessage;
    }

    public boolean isConnectionError() {
        return this.isConnectionError;
    }

    public boolean isServerError() {
        return this.isServerError;
    }

    public void setConnectionError(boolean bl) {
        this.isConnectionError = bl;
    }

    public void setConnectionException(Throwable throwable) {
        this.connectionException = throwable;
    }

    public void setHeaderFields(Map<String, List<String>> map) {
        this.headerFields = map;
    }

    public void setResponseCode(int n) {
        this.responseCode = n;
    }

    public void setServerError(boolean bl) {
        this.isServerError = bl;
    }

    public void setServerErrorMessage(String string2) {
        this.serverErrorMessage = string2;
    }
}

