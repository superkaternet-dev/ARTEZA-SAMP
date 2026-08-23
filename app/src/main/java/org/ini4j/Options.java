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
import org.ini4j.BasicOptionMap;
import org.ini4j.Config;
import org.ini4j.Configurable;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Persistable;
import org.ini4j.spi.OptionsBuilder;
import org.ini4j.spi.OptionsFormatter;
import org.ini4j.spi.OptionsHandler;
import org.ini4j.spi.OptionsParser;

public class Options
extends BasicOptionMap
implements Persistable,
Configurable {
    private static final long serialVersionUID = -1119753444859181822L;
    private String _comment;
    private Config _config;
    private File _file;

    public Options() {
        Object object;
        this._config = object = Config.getGlobal().clone();
        ((Config)object).setEmptyOption(true);
    }

    public Options(File file) throws IOException, InvalidFileFormatException {
        this();
        this._file = file;
        this.load();
    }

    public Options(InputStream inputStream) throws IOException, InvalidFileFormatException {
        this();
        this.load(inputStream);
    }

    public Options(Reader reader) throws IOException, InvalidFileFormatException {
        this();
        this.load(reader);
    }

    public Options(URL uRL) throws IOException, InvalidFileFormatException {
        this();
        this.load(uRL);
    }

    private void storeComment(OptionsHandler optionsHandler, String string2) {
        optionsHandler.handleComment(string2);
    }

    public String getComment() {
        return this._comment;
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
    boolean isPropertyFirstUpper() {
        return this.getConfig().isPropertyFirstUpper();
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
        OptionsParser.newInstance(this.getConfig()).parse(reader, this.newBuilder());
    }

    @Override
    public void load(URL uRL) throws IOException, InvalidFileFormatException {
        OptionsParser.newInstance(this.getConfig()).parse(uRL, this.newBuilder());
    }

    protected OptionsHandler newBuilder() {
        return OptionsBuilder.newInstance(this);
    }

    public void setComment(String string2) {
        this._comment = string2;
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
        this.store(OptionsFormatter.newInstance(writer, this.getConfig()));
    }

    protected void store(OptionsHandler optionsHandler) throws IOException {
        optionsHandler.startOptions();
        this.storeComment(optionsHandler, this._comment);
        for (String string2 : this.keySet()) {
            this.storeComment(optionsHandler, this.getComment(string2));
            int n = this.getConfig().isMultiOption() ? this.length(string2) : 1;
            for (int i = 0; i < n; ++i) {
                optionsHandler.handleOption(string2, (String)this.get((Object)string2, i));
            }
        }
        optionsHandler.endOptions();
    }
}

