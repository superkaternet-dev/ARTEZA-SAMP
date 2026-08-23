/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 */
package com.nvidia.devtech;

import android.content.Context;
import android.content.res.AssetManager;
import com.nvidia.devtech.NvAPKFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

public class NvAPKFileHelper {
    private static NvAPKFileHelper instance = new NvAPKFileHelper();
    private static final boolean logAssetFiles = false;
    private int READ_MODE_ONLY = 0x10000000;
    int apkCount = 0;
    String[] apkFiles;
    private Context context = null;
    boolean hasAPKFiles = false;
    int myApkCount = 0;

    private int findInAPKFiles(String string2) {
        String[] stringArray;
        if (this.myApkCount == 0) {
            return -1;
        }
        CharSequence charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append(string2);
        ((StringBuilder)charSequence).append(".mp3");
        charSequence = ((StringBuilder)charSequence).toString();
        for (int i = 0; i < (stringArray = this.apkFiles).length; ++i) {
            if (string2.compareToIgnoreCase(stringArray[i]) != 0 && ((String)charSequence).compareToIgnoreCase(this.apkFiles[i]) != 0) {
                continue;
            }
            string2.compareTo(this.apkFiles[i]);
            return i;
        }
        return -1;
    }

    public static NvAPKFileHelper getInstance() {
        return instance;
    }

    public void AddAssetFile(String string2) {
        String[] stringArray = this.apkFiles;
        int n = this.myApkCount;
        this.myApkCount = n + 1;
        stringArray[n] = string2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void GetAssetList() {
        try {
            Object object = new InputStreamReader(this.context.getAssets().open("assetfile.txt"));
            BufferedReader bufferedReader = new BufferedReader((Reader)object);
            int n = Integer.parseInt(bufferedReader.readLine());
            this.myApkCount = 0;
            if (n <= 0) return;
            this.apkFiles = new String[n];
            while (true) {
                if ((object = bufferedReader.readLine()) == null) {
                    return;
                }
                String[] stringArray = this.apkFiles;
                n = this.myApkCount;
                this.myApkCount = n + 1;
                stringArray[n] = object;
            }
        }
        catch (Exception exception) {
            AssetManager assetManager = this.context.getAssets();
            this.getDirectoryListing(assetManager, "", 0);
            this.getDirectoryListing(assetManager, "", this.apkCount);
        }
    }

    public void closeFileAndroid(NvAPKFile nvAPKFile) {
        try {
            nvAPKFile.is.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        nvAPKFile.data = new byte[0];
        nvAPKFile.is = null;
    }

    /*
     * WARNING - combined exceptions agressively - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getDirectoryListing(AssetManager object, String string2, int n) {
        try {
            String[] stringArray;
            if (this.apkFiles == null && n > 0) {
                this.apkFiles = new String[n];
            }
            if ((stringArray = object.list(string2)).length == 0) {
                if (n > 0) {
                    this.AddAssetFile(string2);
                } else {
                    ++this.apkCount;
                }
            }
            for (int i = 0; i < stringArray.length; ++i) {
                CharSequence charSequence;
                int n2 = stringArray[i].indexOf(46);
                if (n2 == -1) {
                    if (string2.length() > 0) {
                        charSequence = new StringBuilder();
                        ((StringBuilder)charSequence).append(string2);
                        ((StringBuilder)charSequence).append("/");
                        ((StringBuilder)charSequence).append(stringArray[i]);
                        charSequence = ((StringBuilder)charSequence).toString();
                    } else {
                        charSequence = stringArray[i];
                    }
                    this.getDirectoryListing((AssetManager)object, (String)charSequence, n);
                    continue;
                }
                if (n > 0) {
                    if (string2.length() > 0) {
                        charSequence = new StringBuilder();
                        ((StringBuilder)charSequence).append(string2);
                        ((StringBuilder)charSequence).append("/");
                        ((StringBuilder)charSequence).append(stringArray[i]);
                        charSequence = ((StringBuilder)charSequence).toString();
                    } else {
                        charSequence = stringArray[i];
                    }
                    this.AddAssetFile((String)charSequence);
                    continue;
                }
                ++this.apkCount;
            }
            return 0;
        }
        catch (Exception exception) {
            object = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ERROR: getDirectoryListing ");
            stringBuilder.append(exception.getMessage());
            ((PrintStream)object).println(stringBuilder.toString());
        }
        return 0;
    }

    public NvAPKFile openFileAndroid(String object) {
        int n;
        if (!this.hasAPKFiles) {
            this.apkCount = 0;
            this.apkFiles = null;
            this.GetAssetList();
            this.hasAPKFiles = true;
        }
        if ((n = this.findInAPKFiles((String)object)) == -1) {
            return null;
        }
        object = new NvAPKFile();
        ((NvAPKFile)object).is = null;
        ((NvAPKFile)object).length = 0;
        ((NvAPKFile)object).position = 0;
        ((NvAPKFile)object).bufferSize = 0;
        try {
            ((NvAPKFile)object).is = this.context.getAssets().open(this.apkFiles[n]);
            ((NvAPKFile)object).length = ((NvAPKFile)object).is.available();
            ((NvAPKFile)object).is.mark(this.READ_MODE_ONLY);
            ((NvAPKFile)object).bufferSize = 1024;
            ((NvAPKFile)object).data = new byte[((NvAPKFile)object).bufferSize];
            return object;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public void readFileAndroid(NvAPKFile nvAPKFile, int n) {
        if (n > nvAPKFile.bufferSize) {
            nvAPKFile.data = new byte[n];
            nvAPKFile.bufferSize = n;
        }
        try {
            nvAPKFile.is.read(nvAPKFile.data, 0, n);
            nvAPKFile.position += n;
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    /*
     * Loose catch block
     */
    public long seekFileAndroid(NvAPKFile nvAPKFile, int n) {
        long l = 0L;
        long l2 = 0L;
        long l3 = l;
        nvAPKFile.is.reset();
        for (int i = 128; n > 0 && i > 0; --i) {
            try {
                l2 = l3 = nvAPKFile.is.skip(n);
            }
            catch (IOException iOException) {
                l3 = l;
                iOException.printStackTrace();
            }
            l += l2;
            n = (int)((long)n - l2);
            continue;
            {
                catch (IOException iOException) {
                    l = l3;
                    break;
                }
            }
        }
        nvAPKFile.position = (int)l;
        return l;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

