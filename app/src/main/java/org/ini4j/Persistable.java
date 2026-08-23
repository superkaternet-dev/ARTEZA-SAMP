/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.ini4j.InvalidFileFormatException;

public interface Persistable {
    public File getFile();

    public void load() throws IOException, InvalidFileFormatException;

    public void load(File var1) throws IOException, InvalidFileFormatException;

    public void load(InputStream var1) throws IOException, InvalidFileFormatException;

    public void load(Reader var1) throws IOException, InvalidFileFormatException;

    public void load(URL var1) throws IOException, InvalidFileFormatException;

    public void setFile(File var1);

    public void store() throws IOException;

    public void store(File var1) throws IOException;

    public void store(OutputStream var1) throws IOException;

    public void store(Writer var1) throws IOException;
}

