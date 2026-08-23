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
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.ini4j.BasicRegistry;
import org.ini4j.Config;
import org.ini4j.Configurable;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Persistable;
import org.ini4j.Registry;
import org.ini4j.spi.IniFormatter;
import org.ini4j.spi.IniHandler;
import org.ini4j.spi.IniParser;
import org.ini4j.spi.RegBuilder;

public class Reg
extends BasicRegistry
implements Registry,
Persistable,
Configurable {
    private static final char CR = '\r';
    protected static final String DEFAULT_SUFFIX = ".reg";
    private static final char LF = '\n';
    private static final String PROP_OS_NAME = "os.name";
    private static final int STDERR_BUFF_SIZE = 8192;
    protected static final String TMP_PREFIX = "reg-";
    private static final boolean WINDOWS = Config.getSystemProperty("os.name", "Unknown").startsWith("Windows");
    private static final long serialVersionUID = -1485602876922985912L;
    private Config _config;
    private File _file;

    public Reg() {
        Object object = Config.getGlobal().clone();
        ((Config)object).setEscape(false);
        ((Config)object).setGlobalSection(false);
        ((Config)object).setEmptyOption(true);
        ((Config)object).setMultiOption(true);
        ((Config)object).setStrictOperator(true);
        ((Config)object).setEmptySection(true);
        ((Config)object).setPathSeparator('\\');
        ((Config)object).setFileEncoding(FILE_ENCODING);
        ((Config)object).setLineSeparator("\r\n");
        this._config = object;
    }

    public Reg(File file) throws IOException, InvalidFileFormatException {
        this();
        this._file = file;
        this.load();
    }

    public Reg(InputStream inputStream) throws IOException, InvalidFileFormatException {
        this();
        this.load(inputStream);
    }

    public Reg(Reader reader) throws IOException, InvalidFileFormatException {
        this();
        this.load(reader);
    }

    public Reg(String string2) throws IOException {
        this();
        this.read(string2);
    }

    public Reg(URL uRL) throws IOException, InvalidFileFormatException {
        this();
        this.load(uRL);
    }

    private File createTempFile() throws IOException {
        File file = File.createTempFile(TMP_PREFIX, DEFAULT_SUFFIX);
        file.deleteOnExit();
        return file;
    }

    public static boolean isWindows() {
        return WINDOWS;
    }

    private void regExport(String string2, File file) throws IOException {
        this.requireWindows();
        this.exec(new String[]{"cmd", "/c", "reg", "export", string2, file.getAbsolutePath()});
    }

    private void regImport(File file) throws IOException {
        this.requireWindows();
        this.exec(new String[]{"cmd", "/c", "reg", "import", file.getAbsolutePath()});
    }

    private void requireWindows() {
        if (WINDOWS) {
            return;
        }
        throw new UnsupportedOperationException("Unsupported operating system or runtime environment");
    }

    void exec(String[] object) throws IOException {
        object = Runtime.getRuntime().exec((String[])object);
        try {
            if (object.waitFor() == 0) {
                return;
            }
            Object object2 = new InputStreamReader(object.getErrorStream());
            object = new char[8192];
            int n = ((Reader)object2).read((char[])object);
            ((Reader)object2).close();
            object2 = new String((char[])object, 0, n);
            IOException iOException = new IOException(((String)object2).trim());
            throw iOException;
        }
        catch (InterruptedException interruptedException) {
            throw (IOException)new InterruptedIOException().initCause(interruptedException);
        }
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
    public void load(Reader object) throws IOException, InvalidFileFormatException {
        int n = 2;
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = ((Reader)object).read();
        while (n2 != -1) {
            int n3;
            if (n2 == 10) {
                n3 = n2 = n - 1;
                if (n2 == 0) {
                    break;
                }
            } else {
                n3 = n;
                if (n2 != 13) {
                    n3 = n;
                    if (n != 1) {
                        stringBuilder.append((char)n2);
                        n3 = n;
                    }
                }
            }
            n2 = ((Reader)object).read();
            n = n3;
        }
        if (stringBuilder.length() != 0) {
            if (stringBuilder.toString().equals(this.getVersion())) {
                IniParser.newInstance(this.getConfig()).parse((Reader)object, this.newBuilder());
                return;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unsupported version: ");
            ((StringBuilder)object).append(stringBuilder.toString());
            throw new InvalidFileFormatException(((StringBuilder)object).toString());
        }
        object = new InvalidFileFormatException("Missing version header");
        throw object;
    }

    @Override
    public void load(URL uRL) throws IOException, InvalidFileFormatException {
        this.load(new InputStreamReader(uRL.openStream(), this.getConfig().getFileEncoding()));
    }

    protected IniHandler newBuilder() {
        return RegBuilder.newInstance(this);
    }

    public void read(String string2) throws IOException {
        File file = this.createTempFile();
        try {
            this.regExport(string2, file);
            this.load(file);
            return;
        }
        finally {
            file.delete();
        }
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
        writer.write(this.getVersion());
        writer.write(this.getConfig().getLineSeparator());
        writer.write(this.getConfig().getLineSeparator());
        this.store(IniFormatter.newInstance(writer, this.getConfig()));
    }

    public void write() throws IOException {
        File file = this.createTempFile();
        try {
            this.store(file);
            this.regImport(file);
            return;
        }
        finally {
            file.delete();
        }
    }
}

