/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetFileDescriptor
 *  android.media.AudioManager
 *  android.media.MediaPlayer
 *  android.media.SoundPool
 *  android.util.Log
 */
package com.nvidia.devtech;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import java.io.IOException;

public class AudioHelper {
    private static final int MAX_SOUND_STREAMS = 10;
    private static String ResourceLocation = "com.nvidia.devtech.audio:raw/";
    private static final String TAG = "AudioHelper";
    private static AudioHelper instance = null;
    private MediaPlayer MusicPlayer = null;
    private SoundPool Sounds = null;
    private Context context = null;

    private AudioHelper() {
    }

    public static AudioHelper getInstance() {
        if (instance == null) {
            AudioHelper audioHelper;
            instance = audioHelper = new AudioHelper();
            audioHelper.Initialise();
        }
        return instance;
    }

    void Initialise() {
        this.Sounds = new SoundPool(10, 3, 0);
        Log.i((String)TAG, (String)"created sound pool");
    }

    public int LoadSound(String string2, int n) {
        CharSequence charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("Load sound ");
        ((StringBuilder)charSequence).append(string2);
        Log.i((String)TAG, (String)((StringBuilder)charSequence).toString());
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append(ResourceLocation);
        ((StringBuilder)charSequence).append(string2);
        charSequence = ((StringBuilder)charSequence).toString();
        int n2 = this.context.getResources().getIdentifier((String)charSequence, null, null);
        if (n2 == 0) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("unidentified resource id for ");
            ((StringBuilder)charSequence).append(string2);
            Log.i((String)TAG, (String)((StringBuilder)charSequence).toString());
            return 0;
        }
        return this.Sounds.load(this.context, n2, n);
    }

    public int LoadSoundAsset(String string2, int n) {
        Object var3_4 = null;
        try {
            string2 = this.context.getAssets().openFd(string2);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            string2 = var3_4;
        }
        return this.Sounds.load((AssetFileDescriptor)string2, n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void MusicSetDataSource(String string2) {
        try {
            CharSequence charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(ResourceLocation);
            ((StringBuilder)charSequence).append(string2);
            charSequence = ((StringBuilder)charSequence).toString();
            int n = this.context.getResources().getIdentifier((String)charSequence, null, null);
            if (n == 0) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("unidentified resource id for ");
                ((StringBuilder)charSequence).append(string2);
                Log.i((String)TAG, (String)((StringBuilder)charSequence).toString());
                return;
            }
            charSequence = MediaPlayer.create((Context)this.context, (int)n);
            this.MusicPlayer = charSequence;
            if (charSequence == null) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("failed to create music player");
                ((StringBuilder)charSequence).append(string2);
                Log.i((String)TAG, (String)((StringBuilder)charSequence).toString());
                return;
            }
            charSequence.start();
            return;
        }
        catch (IllegalStateException illegalStateException) {
            illegalStateException.printStackTrace();
            return;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
        }
    }

    public void MusicStart() {
        this.MusicPlayer.start();
    }

    public void MusicStop() {
        MediaPlayer mediaPlayer = this.MusicPlayer;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            this.MusicPlayer.stop();
            this.MusicPlayer.reset();
        }
    }

    public void MusicVolume(float f, float f2) {
        this.MusicPlayer.setVolume(f, f2);
    }

    public void PauseSound(int n) {
        this.Sounds.pause(n);
    }

    public int PlaySound(int n, float f, float f2, int n2, int n3, float f3) {
        return this.Sounds.play(n, f, f2, n2, n3, f3);
    }

    public void ResumeSound(int n) {
        this.Sounds.resume(n);
    }

    void SetMaxVolume() {
        AudioManager audioManager = (AudioManager)this.context.getSystemService("audio");
        audioManager.setStreamVolume(3, audioManager.getStreamMaxVolume(3), 0);
    }

    public void SetResouceLocation(String string2) {
        ResourceLocation = string2;
    }

    public void SetVolume(int n, float f, float f2) {
        this.Sounds.setVolume(n, f, f2);
    }

    public void StopSound(int n) {
        this.Sounds.stop(n);
    }

    public boolean UnloadSample(int n) {
        return this.Sounds.unload(n);
    }

    public void finalize() {
        SoundPool soundPool = this.Sounds;
        if (soundPool != null) {
            soundPool.release();
            this.Sounds = null;
        }
        if ((soundPool = this.MusicPlayer) != null) {
            soundPool.release();
            this.MusicPlayer = null;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

