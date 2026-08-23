/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.ini4j.BasicProfile;
import org.ini4j.Config;
import org.ini4j.Configurable;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Persistable;
import org.ini4j.Profile;
import org.ini4j.spi.IniBuilder;
import org.ini4j.spi.IniFormatter;
import org.ini4j.spi.IniHandler;
import org.ini4j.spi.IniParser;

public class Ini
extends BasicProfile
implements Persistable,
Configurable {
    private static final long serialVersionUID = -6029486578113700585L;
    private Config _config = Config.getGlobal();
    private File _file;

    public Ini() {
    }

    public Ini(File file) throws IOException, InvalidFileFormatException {
        this();
        this._file = file;
        this.load();
    }

    public Ini(InputStream inputStream) throws IOException, InvalidFileFormatException {
        this();
        this.load(inputStream);
    }

    public Ini(Reader reader) throws IOException, InvalidFileFormatException {
        this();
        this.load(reader);
    }

    public Ini(URL uRL) throws IOException, InvalidFileFormatException {
        this();
        this.load(uRL);
    }

    @Override
    public Config getConfig() {
        return this._config;
    }

    @Override
    public File getFile() {
        return this._file;
    }

    @Override
    char getPathSeparator() {
        return this.getConfig().getPathSeparator();
    }

    @Override
    boolean isPropertyFirstUpper() {
        return this.getConfig().isPropertyFirstUpper();
    }

    @Override
    boolean isTreeMode() {
        return this.getConfig().isTree();
    }

    @Override
    public void load() throws IOException, InvalidFileFormatException {
        File file = this._file;
        if (file != null) {
            this.load(file);
            return;
        }
        throw new FileNotFoundException();
    }

    @Override
    public void load(File file) throws IOException, InvalidFileFormatException {
        this.load(file.toURI().toURL());
    }

    @Override
    public void load(InputStream inputStream) throws IOException, InvalidFileFormatException {
        this.load(new InputStreamReader(inputStream, this.getConfig().getFileEncoding()));
    }

    @Override
    public void load(Reader reader) throws IOException, InvalidFileFormatException {
        IniParser.newInstance(this.getConfig()).parse(reader, this.newBuilder());
    }

    @Override
    public void load(URL uRL) throws IOException, InvalidFileFormatException {
        IniParser.newInstance(this.getConfig()).parse(uRL, this.newBuilder());
    }

    protected IniHandler newBuilder() {
        return IniBuilder.newInstance(this);
    }

    @Override
    public void setConfig(Config config) {
        this._config = config;
    }

    @Override
    public void setFile(File file) {
        this._file = file;
    }

    @Override
    public void store() throws IOException {
        File file = this._file;
        if (file != null) {
            this.store(file);
            return;
        }
        throw new FileNotFoundException();
    }

    @Override
    public void store(File object) throws IOException {
        object = new FileOutputStream((File)object);
        this.store((OutputStream)object);
        ((OutputStream)object).close();
    }

    @Override
    public void store(OutputStream outputStream) throws IOException {
        this.store(new OutputStreamWriter(outputStream, this.getConfig().getFileEncoding()));
    }

    @Override
    public void store(Writer writer) throws IOException {
        this.store(IniFormatter.newInstance(writer, this.getConfig()));
    }

    @Override
    protected void store(IniHandler iniHandler, Profile.Section section) {
        if (this.getConfig().isEmptySection() || section.size() != 0) {
            super.store(iniHandler, section);
        }
    }

    @Override
    protected void store(IniHandler iniHandler, Profile.Section section, String string2, int n) {
        if (this.getConfig().isMultiOption() || n == section.length(string2) - 1) {
            super.store(iniHandler, section, string2, n);
        }
    }
}

