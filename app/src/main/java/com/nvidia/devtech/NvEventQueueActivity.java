/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AlertDialog$Builder
 *  android.app.Dialog
 *  android.content.ClipboardManager
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.content.res.Configuration
 *  android.graphics.BitmapFactory
 *  android.hardware.Sensor
 *  android.hardware.SensorEvent
 *  android.hardware.SensorEventListener
 *  android.hardware.SensorManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.Display
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.SurfaceHolder
 *  android.view.SurfaceHolder$Callback
 *  android.view.SurfaceView
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnSystemUiVisibilityChangeListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewParent
 *  android.view.WindowManager
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.EditText
 *  android.widget.FrameLayout
 *  android.widget.Switch
 *  android.widget.TextView
 *  javax.microedition.khronos.egl.EGL10
 *  javax.microedition.khronos.egl.EGLConfig
 *  javax.microedition.khronos.egl.EGLContext
 *  javax.microedition.khronos.egl.EGLDisplay
 *  javax.microedition.khronos.egl.EGLSurface
 *  javax.microedition.khronos.opengles.GL11
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.nvidia.devtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.blackrussia.game.core.DialogClientSettings;
import com.blackrussia.game.gui.BrNotification;
import com.blackrussia.game.gui.ChooseServer;
import com.blackrussia.game.gui.HudManager;
import com.blackrussia.game.gui.Menu;
import com.blackrussia.game.gui.Speedometer;
import com.blackrussia.game.gui.dialogs.BrDialogWindow;
import com.nvidia.devtech.HeightProvider;
import com.nvidia.devtech.InputManager;
import com.nvidia.devtech.NvAPKFileHelper;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda0;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda1;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda10;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda11;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda12;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda13;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda14;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda15;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda16;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda17;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda18;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda19;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda2;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda20;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda21;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda22;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda23;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda24;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda3;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda4;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda5;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda6;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda7;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda8;
import com.nvidia.devtech.NvEventQueueActivity$$ExternalSyntheticLambda9;
import com.nvidia.devtech.NvUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL11;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public abstract class NvEventQueueActivity
extends AppCompatActivity
implements SensorEventListener,
InputManager.InputListener,
View.OnTouchListener,
HeightProvider.HeightListener {
    private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private static final int EGL_OPENGL_ES3_BIT = 64;
    private static final int EGL_RENDERABLE_TYPE = 12352;
    private static NvEventQueueActivity instance = null;
    private boolean GameIsFocused = false;
    private boolean HasGLExtensions = false;
    protected boolean ResumeEventDone = false;
    private int SwapBufferSkip = 0;
    protected int alphaSize = 0;
    protected int blueSize = 5;
    protected SurfaceHolder cachedSurfaceHolder = null;
    protected int[] configAttrs = null;
    protected int[] contextAttrs = null;
    protected int depthSize = 16;
    protected Display display = null;
    EGL10 egl = null;
    protected EGLConfig eglConfig = null;
    protected EGLContext eglContext = null;
    protected EGLDisplay eglDisplay = null;
    protected EGLSurface eglSurface = null;
    private int fixedHeight = 0;
    private int fixedWidth = 0;
    GL11 gl = null;
    private String glExtensions = null;
    private String glRenderer = null;
    private String glVendor = null;
    private String glVersion = null;
    protected int greenSize = 6;
    protected Handler handler = null;
    FrameLayout mAndroidUI = null;
    private BrDialogWindow mBrDialogWindow = null;
    private BrNotification mBrNotification = null;
    private ChooseServer mChooseServer = null;
    protected ClipboardManager mClipboardManager = null;
    private DialogClientSettings mDialogClientSettings = null;
    private HeightProvider mHeightProvider = null;
    private HudManager mHudManager = null;
    private InputManager mInputManager = null;
    private Menu mMenu = null;
    private FrameLayout mRootFrame = null;
    protected int mSensorDelay = 1;
    protected SensorManager mSensorManager = null;
    private Speedometer mSpeedometer = null;
    private SurfaceView mSurfaceView = null;
    private int mUseFullscreen = 0;
    protected boolean paused = false;
    private boolean ranInit = false;
    protected int redSize = 5;
    protected int stencilSize = 0;
    protected boolean supportPauseResume = true;
    private int surfaceHeight = 0;
    private int surfaceWidth = 0;
    private boolean viewIsActive = false;
    protected boolean wantsAccelerometer = false;
    protected boolean wantsMultitouch = false;

    static /* synthetic */ DialogClientSettings access$002(NvEventQueueActivity nvEventQueueActivity, DialogClientSettings dialogClientSettings) {
        nvEventQueueActivity.mDialogClientSettings = dialogClientSettings;
        return dialogClientSettings;
    }

    static /* synthetic */ boolean access$302(NvEventQueueActivity nvEventQueueActivity, boolean bl) {
        nvEventQueueActivity.ranInit = bl;
        return bl;
    }

    static /* synthetic */ int access$402(NvEventQueueActivity nvEventQueueActivity, int n) {
        nvEventQueueActivity.surfaceWidth = n;
        return n;
    }

    static /* synthetic */ int access$502(NvEventQueueActivity nvEventQueueActivity, int n) {
        nvEventQueueActivity.surfaceHeight = n;
        return n;
    }

    static /* synthetic */ boolean access$602(NvEventQueueActivity nvEventQueueActivity, boolean bl) {
        nvEventQueueActivity.viewIsActive = bl;
        return bl;
    }

    public static int dpToPx(float f, Context context) {
        return (int)TypedValue.applyDimension((int)1, (float)f, (DisplayMetrics)context.getResources().getDisplayMetrics());
    }

    public static void fixEditTextForAndroid10Xiaomi(EditText editText) {
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi") && Build.VERSION.SDK_INT == 29) {
            editText.setCursorVisible(false);
        }
    }

    public static NvEventQueueActivity getInstance() {
        return instance;
    }

    static /* synthetic */ void lambda$RadarBR$14() {
        NvEventQueueActivity.setNativeHudElementPosition(6, 5, 5);
    }

    static /* synthetic */ void lambda$showClient$17(Dialog dialog) {
        dialog.dismiss();
    }

    private native void onInputEnd(byte[] var1);

    private native void onNativeHeightChanged(int var1, int var2);

    private void processCutout() {
        if (Build.VERSION.SDK_INT >= 28 && this.mUseFullscreen == 1) {
            this.getWindow().getAttributes().layoutInDisplayCutoutMode = 1;
        }
    }

    public static native void setNativeHudElementPosition(int var0, int var1, int var2);

    public static native void setNativeHudElementScale(int var0, int var1, int var2);

    public void DoResumeEvent() {
        new Thread(new Runnable(this){
            final NvEventQueueActivity this$0;
            {
                this.this$0 = nvEventQueueActivity;
            }

            @Override
            public void run() {
                while (this.this$0.cachedSurfaceHolder == null) {
                    this.this$0.mSleep(1000L);
                }
                System.out.println("Call from DoResumeEvent");
                this.this$0.resumeEvent();
                this.this$0.ResumeEventDone = true;
            }
        }).start();
    }

    public void GetGLExtensions() {
        Object object;
        if (!this.HasGLExtensions && (object = this.gl) != null && this.cachedSurfaceHolder != null) {
            this.glVendor = object.glGetString(7936);
            this.glExtensions = this.gl.glGetString(7939);
            this.glRenderer = this.gl.glGetString(7937);
            this.glVersion = this.gl.glGetString(7938);
            object = System.out;
            Appendable appendable = new StringBuilder();
            ((StringBuilder)appendable).append("Vendor: ");
            ((StringBuilder)appendable).append(this.glVendor);
            ((PrintStream)object).println(((StringBuilder)appendable).toString());
            object = System.out;
            appendable = new StringBuilder();
            ((StringBuilder)appendable).append("Extensions ");
            ((StringBuilder)appendable).append(this.glExtensions);
            ((PrintStream)object).println(((StringBuilder)appendable).toString());
            appendable = System.out;
            object = new StringBuilder();
            ((StringBuilder)object).append("Renderer: ");
            ((StringBuilder)object).append(this.glRenderer);
            ((PrintStream)appendable).println(((StringBuilder)object).toString());
            appendable = System.out;
            object = new StringBuilder();
            ((StringBuilder)object).append("GIVersion: ");
            ((StringBuilder)object).append(this.glVersion);
            ((PrintStream)appendable).println(((StringBuilder)object).toString());
            if (this.glVendor != null) {
                this.HasGLExtensions = true;
            }
        }
    }

    public SurfaceView GetSurfaceView() {
        return this.mSurfaceView;
    }

    public boolean InitEGLAndGLES2(int n) {
        System.out.println("lnitEGLAndGLES2");
        if (this.cachedSurfaceHolder == null) {
            System.out.println("InitEGLAndGLES2 failed, cachedSurfaceHoIder is null");
            return false;
        }
        boolean bl = true;
        if (this.eglContext == null) {
            bl = this.initEGL();
        }
        if (bl) {
            System.out.println("Should we create a surface?");
            if (!this.viewIsActive) {
                System.out.println("Yes! Calling create surface");
                this.createEGLSurface(this.cachedSurfaceHolder);
                System.out.println("Done creating surface");
            }
            this.viewIsActive = true;
            this.SwapBufferSkip = 1;
            return true;
        }
        System.out.println("initEGlAndGLES2 failed, core EGL init failure");
        return false;
    }

    @Override
    public void OnInputEnd(String object) {
        Object var2_3 = null;
        try {
            object = ((String)object).getBytes("windows-1251");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            object = var2_3;
        }
        this.onInputEnd((byte[])object);
    }

    public void RadarBR() {
        this.runOnUiThread(NvEventQueueActivity$$ExternalSyntheticLambda16.INSTANCE);
    }

    public native boolean accelerometerEvent(float var1, float var2, float var3);

    public void callLauncherActivity() {
        this.runOnUiThread(new Runnable(this){
            final NvEventQueueActivity this$0;
            {
                this.this$0 = nvEventQueueActivity;
            }

            @Override
            public void run() {
                Intent intent = this.this$0.getPackageManager().getLaunchIntentForPackage("com.blackrussia.launcher");
                intent.putExtra("minimize", true);
                if (this.this$0.ResumeEventDone) {
                    this.this$0.pauseEvent();
                }
                System.out.println("Calling launcher activity");
                this.this$0.startActivity(intent);
                System.out.println("Called launcher activity");
            }
        });
    }

    public native void changeConnection(boolean var1);

    public native void cleanup();

    protected void cleanupEGL() {
        System.out.println("cleanupEGL");
        this.destroyEGLSurface();
        EGLDisplay eGLDisplay = this.eglDisplay;
        if (eGLDisplay != null) {
            this.egl.eglMakeCurrent(eGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        }
        if ((eGLDisplay = this.eglContext) != null) {
            this.egl.eglDestroyContext(this.eglDisplay, (EGLContext)eGLDisplay);
        }
        if ((eGLDisplay = this.eglDisplay) != null) {
            this.egl.eglTerminate(eGLDisplay);
        }
        this.eglDisplay = null;
        this.eglContext = null;
        this.eglSurface = null;
        this.ranInit = false;
        this.eglConfig = null;
        this.cachedSurfaceHolder = null;
        this.surfaceWidth = 0;
        this.surfaceHeight = 0;
    }

    protected boolean createEGLSurface(SurfaceHolder object) {
        this.eglSurface = this.egl.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, object, null);
        PrintStream printStream = System.out;
        object = new StringBuilder();
        object.append("eglSurface: ");
        object.append(this.eglSurface);
        object.append(", err: ");
        object.append(this.egl.eglGetError());
        printStream.println(object.toString());
        object = new int[1];
        this.egl.eglQuerySurface(this.eglDisplay, this.eglSurface, 12375, (int[])object);
        this.surfaceWidth = (int)object[0];
        this.egl.eglQuerySurface(this.eglDisplay, this.eglSurface, 12374, (int[])object);
        this.surfaceHeight = (int)object[0];
        System.out.println("checking glVendor == null?");
        if (this.glVendor == null) {
            System.out.println("Making current and back");
            this.makeCurrent();
            this.unMakeCurrent();
        }
        System.out.println("Done create EGL surface");
        return true;
    }

    public native boolean customMultiTouchEvent(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

    protected void destroyEGLSurface() {
        System.out.println("*** destroyEGLSurface");
        EGLDisplay eGLDisplay = this.eglDisplay;
        if (eGLDisplay != null && this.eglSurface != null) {
            this.egl.eglMakeCurrent(eGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        }
        if ((eGLDisplay = this.eglSurface) != null) {
            this.egl.eglDestroySurface(this.eglDisplay, (EGLSurface)eGLDisplay);
        }
        this.eglSurface = null;
    }

    public byte[] getClipboardText() {
        String string2 = " ";
        Object object = string2;
        if (this.mClipboardManager.getPrimaryClip() != null) {
            Object object2 = this.mClipboardManager.getPrimaryClip().getItemAt(0);
            object = string2;
            if (object2 != null) {
                object2 = object2.getText();
                object = string2;
                if (object2 != null) {
                    object = object2.toString();
                }
            }
        }
        string2 = null;
        try {
            object = ((String)object).getBytes("windows-1251");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            object = string2;
        }
        return object;
    }

    public String getHudElementColor(int n) {
        byte[] byArray = this.getNativeHudElementColor(n);
        String string2 = null;
        try {
            String string3;
            string2 = string3 = new String(byArray, "windows-1251");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            // empty catch block
        }
        return string2;
    }

    public native int getLastServer();

    public native boolean getNativeCutoutSettings();

    public native boolean getNativeDialog();

    public native boolean getNativeFpsCounterSettings();

    public native boolean getNativeHpArmourText();

    public native boolean getNativeHud();

    public native byte[] getNativeHudElementColor(int var1);

    public native int[] getNativeHudElementPosition(int var1);

    public native int[] getNativeHudElementScale(int var1);

    public native boolean getNativeKeyboardSettings();

    public native boolean getNativeOutfitGunsSettings();

    public native boolean getNativePcMoney();

    public native boolean getNativeRadarrect();

    public native boolean getNativeSkyBox();

    public native int[] getNativeWidgetPositionAndScale(int var1);

    public int getOrientation() {
        return this.display.getOrientation();
    }

    public boolean getSupportPauseResume() {
        return this.supportPauseResume;
    }

    public int getSurfaceHeight() {
        return this.surfaceHeight;
    }

    public int getSurfaceWidth() {
        return this.surfaceWidth;
    }

    public FrameLayout getmRootFrame() {
        return this.mRootFrame;
    }

    public void hideGps() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda17(this));
    }

    public void hideHud() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda18(this));
    }

    public void hideInputLayout() {
        this.runOnUiThread(new Runnable(this){
            final NvEventQueueActivity this$0;
            {
                this.this$0 = nvEventQueueActivity;
            }

            @Override
            public void run() {
                this.this$0.mInputManager.HideInputLayout();
            }
        });
    }

    public void hideSpeed() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda19(this));
    }

    public void hideSystemUI() {
        this.getWindow().getDecorView().setSystemUiVisibility(7942);
    }

    public void hideZona() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda20(this));
    }

    public void hideradar() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda21(this));
    }

    public void hidex2() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda22(this));
    }

    public native void imeClosed();

    public native boolean init(boolean var1);

    protected boolean initEGL() {
        int n;
        if (this.configAttrs == null) {
            this.configAttrs = new int[]{12344};
        }
        Object object = this.configAttrs;
        this.configAttrs = new int[((int[])object).length + 3 - 1];
        for (n = 0; n < ((int[])object).length - 1; ++n) {
            this.configAttrs[n] = object[n];
        }
        Object object2 = this.configAttrs;
        int n2 = n + 1;
        object2[n] = 12352;
        n = n2 + 1;
        object2[n2] = 4;
        object2[n] = 12344;
        this.contextAttrs = new int[]{12440, 2, 12344};
        if (object2 == null) {
            this.configAttrs = new int[]{12344};
        }
        int[] nArray = this.configAttrs;
        this.configAttrs = new int[nArray.length + 13 - 1];
        for (n = 0; n < nArray.length - 1; ++n) {
            this.configAttrs[n] = nArray[n];
        }
        object2 = this.configAttrs;
        n2 = n + 1;
        object2[n] = 12324;
        Object object3 = n2 + 1;
        object2[n2] = this.redSize;
        n = object3 + 1;
        object2[object3] = 12323;
        n2 = n + 1;
        object2[n] = this.greenSize;
        n = n2 + 1;
        object2[n2] = 12322;
        n2 = n + 1;
        object2[n] = this.blueSize;
        n = n2 + 1;
        object2[n2] = 12321;
        n2 = n + 1;
        object2[n] = this.alphaSize;
        n = n2 + 1;
        object2[n2] = 12326;
        n2 = n + 1;
        object2[n] = this.stencilSize;
        n = n2 + 1;
        object2[n2] = 12325;
        n2 = n + 1;
        object2[n] = this.depthSize;
        object2[n2] = 12344;
        object2 = (EGL10)EGLContext.getEGL();
        this.egl = (EGL10)object2;
        object2.eglGetError();
        this.eglDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        Object object4 = System.out;
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("eglDisplay: ");
        ((StringBuilder)object2).append(this.eglDisplay);
        ((StringBuilder)object2).append(", errr: ");
        ((StringBuilder)object2).append(this.egl.eglGetError());
        ((PrintStream)object4).println(((StringBuilder)object2).toString());
        object2 = new int[2];
        boolean bl = this.egl.eglInitialize(this.eglDisplay, (int[])object2);
        Appendable appendable = System.out;
        object4 = new StringBuilder();
        ((StringBuilder)object4).append("EGLInitialize returned: ");
        ((StringBuilder)object4).append(bl);
        ((PrintStream)appendable).println(((StringBuilder)object4).toString());
        if (!bl) {
            return false;
        }
        n2 = this.egl.eglGetError();
        if (n2 != 12288) {
            return false;
        }
        object4 = System.out;
        appendable = new StringBuilder();
        ((StringBuilder)appendable).append("eglInitialize err: ");
        ((StringBuilder)appendable).append(n2);
        ((PrintStream)object4).println(((StringBuilder)appendable).toString());
        appendable = new EGLConfig[20];
        object4 = new int[1];
        this.egl.eglChooseConfig(this.eglDisplay, this.configAttrs, (EGLConfig[])appendable, ((Appendable)appendable).length, (int[])object4);
        Object object5 = System.out;
        Object object6 = new StringBuilder();
        ((StringBuilder)object6).append("eglChooseConfig err: ");
        ((StringBuilder)object6).append(this.egl.eglGetError());
        ((PrintStream)object5).println(((StringBuilder)object6).toString());
        object3 = 0x1000000;
        object6 = new int[1];
        for (n = 0; n < object4[0]; ++n) {
            Object object7;
            Object object8;
            Object object9;
            block9: {
                object9 = 1;
                for (object8 = 0; object8 < ((int[])object).length - 1 >> 1; ++object8) {
                    this.egl.eglGetConfigAttrib(this.eglDisplay, (EGLConfig)appendable[n], this.configAttrs[object8 * 2], (int[])object6);
                    object7 = object6[0];
                    object5 = this.configAttrs;
                    if ((object7 & object5[object8 * 2 + 1]) == object5[object8 * 2 + 1]) continue;
                    object8 = 0;
                    break block9;
                }
                object8 = object9;
            }
            if (object8 == 0) continue;
            this.egl.eglGetConfigAttrib(this.eglDisplay, (EGLConfig)appendable[n], 12324, (int[])object6);
            object9 = object6[0];
            this.egl.eglGetConfigAttrib(this.eglDisplay, (EGLConfig)appendable[n], 12323, (int[])object6);
            object7 = object6[0];
            this.egl.eglGetConfigAttrib(this.eglDisplay, (EGLConfig)appendable[n], 12322, (int[])object6);
            Object object10 = object6[0];
            this.egl.eglGetConfigAttrib(this.eglDisplay, (EGLConfig)appendable[n], 12321, (int[])object6);
            Object object11 = object6[0];
            this.egl.eglGetConfigAttrib(this.eglDisplay, (EGLConfig)appendable[n], 12325, (int[])object6);
            Object object12 = object6[0];
            this.egl.eglGetConfigAttrib(this.eglDisplay, (EGLConfig)appendable[n], 12326, (int[])object6);
            object8 = object6[0];
            object5 = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(">>> EGL Config [");
            stringBuilder.append(n);
            stringBuilder.append("] R");
            stringBuilder.append((int)object9);
            stringBuilder.append("G");
            stringBuilder.append((int)object7);
            stringBuilder.append("B");
            stringBuilder.append((int)object10);
            stringBuilder.append("A");
            stringBuilder.append((int)object11);
            stringBuilder.append(" D");
            stringBuilder.append((int)object12);
            stringBuilder.append("S");
            stringBuilder.append((int)object8);
            ((PrintStream)object5).println(stringBuilder.toString());
            object7 = (Math.abs(object9 - this.redSize) + Math.abs((int)(object7 - this.greenSize)) + Math.abs((int)(object10 - this.blueSize)) + Math.abs((int)(object11 - this.alphaSize)) << 16) + (Math.abs((int)(object12 - this.depthSize)) << 8) + Math.abs(object8 - this.stencilSize);
            if (object7 >= object3) continue;
            System.out.println("--------------------------");
            object5 = System.out;
            stringBuilder = new StringBuilder();
            stringBuilder.append("New config chosen: ");
            stringBuilder.append(n);
            ((PrintStream)object5).println(stringBuilder.toString());
            object3 = object9;
            for (object8 = 0; object8 < ((Object)(object5 = (Object)this.configAttrs)).length - 1 >> 1; ++object8) {
                this.egl.eglGetConfigAttrib(this.eglDisplay, (EGLConfig)appendable[n], (int)object5[object8 * 2], (int[])object6);
                if (object6[0] < this.configAttrs[object8 * 2 + 1]) continue;
                object5 = System.out;
                stringBuilder = new StringBuilder();
                stringBuilder.append("setting ");
                stringBuilder.append((int)object8);
                stringBuilder.append(", matches: ");
                stringBuilder.append((int)object6[0]);
                ((PrintStream)object5).println(stringBuilder.toString());
            }
            this.eglConfig = appendable[n];
            object3 = object7;
        }
        this.eglContext = this.egl.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, this.contextAttrs);
        object2 = System.out;
        object = new StringBuilder();
        ((StringBuilder)object).append("eglCreateContext: ");
        ((StringBuilder)object).append(this.egl.eglGetError());
        ((PrintStream)object2).println(((StringBuilder)object).toString());
        this.gl = (GL11)this.eglContext.getGL();
        return true;
    }

    public native void initSAMP();

    public native boolean keyEvent(int var1, int var2, int var3, int var4, KeyEvent var5);

    public /* synthetic */ void lambda$hideGps$5$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.HideGps();
    }

    public /* synthetic */ void lambda$hideHud$3$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.HideHud();
    }

    public /* synthetic */ void lambda$hideSpeed$13$com-nvidia-devtech-NvEventQueueActivity() {
        this.mSpeedometer.HideSpeed();
    }

    public /* synthetic */ void lambda$hideZona$7$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.HideZona();
    }

    public /* synthetic */ void lambda$hideradar$22$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.HideRadar();
    }

    public /* synthetic */ void lambda$hidex2$9$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.HideX2();
    }

    public /* synthetic */ void lambda$localShowNotification$15$com-nvidia-devtech-NvEventQueueActivity(JSONObject jSONObject) {
        BrNotification.newInstance().show(jSONObject);
    }

    public /* synthetic */ void lambda$setPauseState$10$com-nvidia-devtech-NvEventQueueActivity(boolean bl) {
        FrameLayout frameLayout = this.mAndroidUI;
        int n = bl ? 8 : 0;
        frameLayout.setVisibility(n);
    }

    public /* synthetic */ void lambda$showClient$18$com-nvidia-devtech-NvEventQueueActivity(Dialog dialog, View view) {
        this.handler.postDelayed((Runnable)new NvEventQueueActivity$$ExternalSyntheticLambda11(dialog), 200L);
    }

    public /* synthetic */ void lambda$showClient$19$com-nvidia-devtech-NvEventQueueActivity() {
        Dialog dialog = new Dialog((Context)this);
        dialog.setContentView(2131558476);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(2131230957);
        dialog.getWindow().setLayout(-1, -2);
        ((TextView)dialog.findViewById(2131362223)).setText((CharSequence)"\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438 \u0433\u0440\u0430\u0444\u0438\u043a\u0438");
        ((Switch)dialog.findViewById(2131362431)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final NvEventQueueActivity this$0;
            {
                this.this$0 = nvEventQueueActivity;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.setNativeSkyBox(bl);
            }
        });
        ((Switch)dialog.findViewById(2131362432)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final NvEventQueueActivity this$0;
            {
                this.this$0 = nvEventQueueActivity;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.setNativeDialog(bl);
            }
        });
        ((TextView)dialog.findViewById(2131361940)).setOnClickListener((View.OnClickListener)new NvEventQueueActivity$$ExternalSyntheticLambda0(this, dialog));
        dialog.show();
    }

    public /* synthetic */ void lambda$showClient$20$com-nvidia-devtech-NvEventQueueActivity() {
        this.handler.postDelayed((Runnable)new NvEventQueueActivity$$ExternalSyntheticLambda23(this), 200L);
    }

    public /* synthetic */ void lambda$showDialog$0$com-nvidia-devtech-NvEventQueueActivity(int n, int n2, String string2, String string3, String string4, String string5) {
        this.mBrDialogWindow.show(n, n2, string2, string3, string4, string5);
    }

    public /* synthetic */ void lambda$showGps$4$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.ShowGps();
    }

    public /* synthetic */ void lambda$showHud$2$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.ShowHud();
    }

    public /* synthetic */ void lambda$showMenu$16$com-nvidia-devtech-NvEventQueueActivity() {
        this.mMenu.ShowMenu();
    }

    public /* synthetic */ void lambda$showSpeed$12$com-nvidia-devtech-NvEventQueueActivity() {
        this.mSpeedometer.ShowSpeed();
    }

    public /* synthetic */ void lambda$showSplash$24$com-nvidia-devtech-NvEventQueueActivity() {
        this.mChooseServer.Show();
    }

    public /* synthetic */ void lambda$showZona$6$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.ShowZona();
    }

    public /* synthetic */ void lambda$showradar$21$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.ShowRadar();
    }

    public /* synthetic */ void lambda$showx2$8$com-nvidia-devtech-NvEventQueueActivity() {
        this.mHudManager.ShowX2();
    }

    public /* synthetic */ void lambda$updateHudInfo$1$com-nvidia-devtech-NvEventQueueActivity(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.mHudManager.UpdateHudInfo(n, n2, n3, n4, n5, n6, n7, n8);
    }

    public /* synthetic */ void lambda$updateSpeedInfo$11$com-nvidia-devtech-NvEventQueueActivity(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.mSpeedometer.UpdateSpeedInfo(n, n2, n3, n4, n5, n6, n7, n8);
    }

    public /* synthetic */ void lambda$updateSplash$23$com-nvidia-devtech-NvEventQueueActivity(int n) {
        this.mChooseServer.Update(n);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public RawData loadFile(String object) {
        Object object2;
        RawData rawData;
        block14: {
            Throwable throwable2;
            Object object3;
            block13: {
                InputStream inputStream = null;
                Object var5_7 = null;
                InputStream inputStream2 = null;
                rawData = new RawData(this);
                object3 = inputStream2;
                object2 = inputStream;
                try {
                    try {
                        object3 = inputStream2;
                        object2 = inputStream;
                        object3 = inputStream2;
                        object2 = inputStream;
                        StringBuilder stringBuilder = new StringBuilder();
                        object3 = inputStream2;
                        object2 = inputStream;
                        stringBuilder.append("/data/");
                        object3 = inputStream2;
                        object2 = inputStream;
                        stringBuilder.append((String)object);
                        object3 = inputStream2;
                        object2 = inputStream;
                        FileInputStream fileInputStream = new FileInputStream(stringBuilder.toString());
                        object = fileInputStream;
                    }
                    catch (Exception exception) {
                        object3 = inputStream2;
                        object2 = inputStream;
                        try {
                            object = this.getAssets().open((String)object);
                        }
                        catch (Exception exception2) {
                            object = var5_7;
                        }
                    }
                }
                catch (Throwable throwable2) {
                    break block13;
                }
                catch (IOException iOException) {
                    break block14;
                }
                object3 = object;
                object2 = object;
                int n = ((InputStream)object).available();
                object3 = object;
                object2 = object;
                rawData.length = n;
                object3 = object;
                object2 = object;
                rawData.data = new byte[n];
                object3 = object;
                object2 = object;
                ((InputStream)object).read(rawData.data);
                if (object == null) return rawData;
                try {
                    ((InputStream)object).close();
                    return rawData;
                }
                catch (Exception exception) {
                    return rawData;
                }
            }
            if (object3 == null) throw throwable2;
            try {
                ((InputStream)object3).close();
                throw throwable2;
            }
            catch (Exception exception) {
                // empty catch block
            }
            throw throwable2;
        }
        if (object2 == null) return rawData;
        {
            ((InputStream)object2).close();
        }
        return rawData;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public RawTexture loadTexture(String object) {
        int n5;
        int n4;
        int n;
        int n3;
        int n2;
        FileInputStream fileInputStream;
        RawTexture rawTexture = new RawTexture(this);
        Object[] objectArray = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/data/");
            stringBuilder.append((String)object);
            fileInputStream = new FileInputStream(stringBuilder.toString());
            object = fileInputStream;
        }
        catch (Exception exception) {
            try {
                object = this.getAssets().open((String)object);
            }
            catch (Exception exception2) {
                object = objectArray;
            }
        }
        try {
            fileInputStream = BitmapFactory.decodeStream((InputStream)object);
            rawTexture.width = fileInputStream.getWidth();
            rawTexture.height = fileInputStream.getHeight();
            object = new int[fileInputStream.getWidth() * fileInputStream.getHeight()];
            fileInputStream.getPixels((int[])object, 0, fileInputStream.getWidth(), 0, 0, fileInputStream.getWidth(), fileInputStream.getHeight());
            objectArray = new int[fileInputStream.getWidth()];
            n2 = fileInputStream.getWidth();
            n3 = fileInputStream.getHeight();
            for (n = 0; n < n3 >> 1; ++n) {
                System.arraycopy(object, n * n2, objectArray, 0, n2);
                System.arraycopy(object, (n3 - 1 - n) * n2, object, n * n2, n2);
                System.arraycopy(objectArray, 0, object, (n3 - 1 - n) * n2, n2);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return rawTexture;
        }
        {
            rawTexture.length = ((Object)object).length * 4;
            rawTexture.data = new byte[rawTexture.length];
            n4 = 0;
            n5 = 0;
        }
        for (n = 0; n < n3; ++n) {
            for (int n6 = 0; n6 < n2; ++n6, ++n4) {
                int n7;
                Object object2 = object[n4];
                {
                    objectArray = rawTexture.data;
                    n7 = n5 + 1;
                }
                objectArray[n5] = (byte)(object2 >> 16 & 0xFF);
                {
                    objectArray = rawTexture.data;
                    n5 = n7 + 1;
                }
                objectArray[n7] = (byte)(object2 >> 8 & 0xFF);
                {
                    objectArray = rawTexture.data;
                    n7 = n5 + 1;
                }
                objectArray[n5] = (byte)(object2 >> 0 & 0xFF);
                {
                    objectArray = rawTexture.data;
                    n5 = n7 + 1;
                }
                objectArray[n7] = (byte)(object2 >> 24 & 0xFF);
            }
        }
        return rawTexture;
    }

    public void localShowNotification(JSONObject jSONObject) {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda14(this, jSONObject));
    }

    public native void lowMemoryEvent();

    public void mSleep(long l) {
        try {
            Thread.sleep(l);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    public boolean makeCurrent() {
        EGLDisplay eGLDisplay;
        Object object = this.eglContext;
        if (object == null) {
            System.out.println("eglContext is NULL");
            return false;
        }
        Object object2 = this.eglSurface;
        if (object2 == null) {
            System.out.println("eglSurface is NULL");
            return false;
        }
        if (!this.egl.eglMakeCurrent(this.eglDisplay, (EGLSurface)object2, (EGLSurface)object2, (EGLContext)object) && !(object2 = this.egl).eglMakeCurrent(eGLDisplay = this.eglDisplay, (EGLSurface)(object = this.eglSurface), (EGLSurface)object, this.eglContext)) {
            object2 = System.out;
            object = new StringBuilder();
            ((StringBuilder)object).append("eglMakeCurrent err: ");
            ((StringBuilder)object).append(this.egl.eglGetError());
            ((PrintStream)object2).println(((StringBuilder)object).toString());
            return false;
        }
        this.GetGLExtensions();
        return true;
    }

    public native boolean multiTouchEvent(int var1, int var2, int var3, int var4, int var5, int var6, MotionEvent var7);

    public native void notifyChange(String var1, int var2);

    public native void nvAcquireTimeExtension();

    public native long nvGetSystemTime();

    public void onAccuracyChanged(Sensor sensor, int n) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.onEventBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onCreate(Bundle bundle) {
        System.out.println("**** onCreate");
        super.onCreate(bundle);
        instance = this;
        if (this.supportPauseResume) {
            System.out.println("Calling init(false)");
            this.init(false);
            System.out.println("Calling initSAMP");
            this.initSAMP();
            System.out.println("Called");
        }
        this.handler = new Handler();
        if (this.wantsAccelerometer && this.mSensorManager == null) {
            this.mSensorManager = (SensorManager)this.getSystemService("sensor");
        }
        this.mClipboardManager = (ClipboardManager)this.getSystemService("clipboard");
        NvUtil.getInstance().setActivity(this);
        NvAPKFileHelper.getInstance().setContext((Context)this);
        this.display = ((WindowManager)this.getSystemService("window")).getDefaultDisplay();
        this.getWindow().addFlags(1024);
        this.setRequestedOrientation(6);
        int n = Build.VERSION.SDK_INT;
        this.systemInit();
        this.hideSystemUI();
        this.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(this){
            final NvEventQueueActivity this$0;
            {
                this.this$0 = nvEventQueueActivity;
            }

            public void onSystemUiVisibilityChange(int n) {
                if ((n & 4) == 0) {
                    this.this$0.hideSystemUI();
                }
            }
        });
        this.processCutout();
    }

    @Override
    public void onDestroy() {
        System.out.println("**** onDestroy");
        if (this.supportPauseResume) {
            this.quitAndWait();
            this.finish();
        }
        super.onDestroy();
        this.systemCleanup();
    }

    public native void onEventBackPressed();

    @Override
    public void onHeightChanged(int n, int n2) {
        InputManager inputManager = this.mInputManager;
        if (inputManager != null) {
            inputManager.onHeightChanged(n2);
        }
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        boolean bl = false;
        if (n == 4) {
            this.onEventBackPressed();
        }
        if (n != 24 && n != 25) {
            if (n != 89 && n != 85 && n != 90) {
                boolean bl2 = bl;
                if (n != 82) {
                    bl2 = bl;
                    if (n != 4) {
                        bl2 = super.onKeyDown(n, keyEvent);
                    }
                }
                bl = bl2;
                if (!bl2) {
                    bl = this.keyEvent(keyEvent.getAction(), n, keyEvent.getUnicodeChar(), keyEvent.getMetaState(), keyEvent);
                }
                return bl;
            }
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (n == 115 && Build.VERSION.SDK_INT >= 11) {
            int n2 = keyEvent.isCapsLockOn() ? 3 : 4;
            this.keyEvent(n2, 115, 0, 0, keyEvent);
        }
        if (n != 89 && n != 85 && n != 90) {
            boolean bl = super.onKeyUp(n, keyEvent);
            if (bl) {
                return bl;
            }
            return this.keyEvent(keyEvent.getAction(), n, keyEvent.getUnicodeChar(), keyEvent.getMetaState(), keyEvent);
        }
        return false;
    }

    @Override
    protected void onPause() {
        System.out.println("**** onPause");
        super.onPause();
        this.paused = true;
        if (this.ResumeEventDone) {
            System.out.println("java is invoking pauseEvent(), this will block until\nthe client calls NVEventPauseProcessed");
            this.pauseEvent();
            System.out.println("pauseEvent() returned");
        }
    }

    protected void onRestart() {
        System.out.println("**** onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        System.out.println("**** onResume");
        super.onResume();
        Object object = this.mSensorManager;
        if (object != null) {
            object.registerListener((SensorEventListener)this, object.getDefaultSensor(1), this.mSensorDelay);
        }
        this.paused = false;
        object = this.mHeightProvider;
        if (object != null) {
            object.init((View)this.mRootFrame);
        }
        if (this.viewIsActive && this.ResumeEventDone) {
            this.resumeEvent();
            object = this.cachedSurfaceHolder;
            if (object != null) {
                object.setKeepScreenOn(true);
            }
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 1) {
            float f = 0.0f;
            float f2 = 0.0f;
            switch (this.display.getRotation()) {
                default: {
                    break;
                }
                case 3: {
                    f = -sensorEvent.values[1];
                    f2 = sensorEvent.values[0];
                    break;
                }
                case 2: {
                    f = sensorEvent.values[0];
                    f2 = sensorEvent.values[1];
                    break;
                }
                case 1: {
                    f = sensorEvent.values[1];
                    f2 = sensorEvent.values[0];
                    break;
                }
                case 0: {
                    f = -sensorEvent.values[0];
                    f2 = sensorEvent.values[1];
                }
            }
            this.accelerometerEvent(f, f2, sensorEvent.values[2]);
        }
    }

    public native void onSettingsWindowDefaults(int var1);

    public native void onSettingsWindowSave();

    @Override
    protected void onStop() {
        System.out.println("**** onStop");
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager != null) {
            sensorManager.unregisterListener((SensorEventListener)this);
        }
        super.onStop();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == this.mRootFrame) {
            if (this.wantsMultitouch) {
                int n;
                int n2 = motionEvent.getPointerCount();
                int n3 = 0;
                int n4 = 0;
                int n5 = 0;
                int n6 = 0;
                int n7 = 0;
                int n8 = 0;
                for (n = 0; n < n2; ++n) {
                    int n9;
                    int n10;
                    int n11;
                    int n12;
                    int n13 = motionEvent.getPointerId(n);
                    if (n13 == 0) {
                        n12 = (int)motionEvent.getX(n);
                        n11 = (int)motionEvent.getY(n);
                        n10 = n5;
                        n9 = n6;
                    } else if (n13 == 1) {
                        n10 = (int)motionEvent.getX(n);
                        n9 = (int)motionEvent.getY(n);
                        n12 = n3;
                        n11 = n4;
                    } else {
                        n12 = n3;
                        n11 = n4;
                        n10 = n5;
                        n9 = n6;
                        if (n13 == 2) {
                            n7 = (int)motionEvent.getX(n);
                            n8 = (int)motionEvent.getY(n);
                            n9 = n6;
                            n10 = n5;
                            n11 = n4;
                            n12 = n3;
                        }
                    }
                    n3 = n12;
                    n4 = n11;
                    n5 = n10;
                    n6 = n9;
                }
                n = motionEvent.getPointerId(motionEvent.getActionIndex());
                this.customMultiTouchEvent(motionEvent.getActionMasked(), n, n3, n4, n5, n6, n7, n8);
            } else {
                this.touchEvent(motionEvent.getAction(), (int)motionEvent.getX(), (int)motionEvent.getY(), motionEvent);
            }
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public native void onWeaponChanged();

    public void onWindowFocusChanged(boolean bl) {
        Object object = this.mDialogClientSettings;
        if (object != null && ((DialogFragment)object).getDialog() != null && this.mDialogClientSettings.getDialog().isShowing()) {
            this.hideSystemUI();
            super.onWindowFocusChanged(bl);
            return;
        }
        if (this.ResumeEventDone && this.viewIsActive && !this.paused) {
            boolean bl2 = this.GameIsFocused;
            if (bl2 && !bl) {
                object = this.mInputManager;
                if (object != null) {
                    if (!((InputManager)object).IsShowing()) {
                        this.pauseEvent();
                    }
                } else {
                    this.pauseEvent();
                }
            } else if (!bl2 && bl) {
                this.resumeEvent();
            }
            this.GameIsFocused = bl;
        }
        super.onWindowFocusChanged(bl);
        if (bl) {
            this.hideSystemUI();
        }
    }

    public native void pauseEvent();

    public native void postCleanup();

    public native boolean processTouchpadAsPointer(ViewParent var1, boolean var2);

    public native void quitAndWait();

    public native void resumeEvent();

    public native void sendCommand(byte[] var1);

    public native void sendDialogResponse(int var1, int var2, int var3, byte[] var4);

    public native void sendRPC(int var1, byte[] var2, int var3);

    public void setFixedSize(int n, int n2) {
        this.fixedWidth = n;
        this.fixedHeight = n2;
    }

    public native void setNativeCutoutSettings(boolean var1);

    public native void setNativeDialog(boolean var1);

    public native void setNativeFpsCounterSettings(boolean var1);

    public native void setNativeHpArmourText(boolean var1);

    public native void setNativeHud(boolean var1);

    public native void setNativeHudElementColor(int var1, int var2, int var3, int var4, int var5);

    public native void setNativeKeyboardSettings(boolean var1);

    public native void setNativeOutfitGunsSettings(boolean var1);

    public native void setNativePcMoney(boolean var1);

    public native void setNativeRadarrect(boolean var1);

    public native void setNativeSkyBox(boolean var1);

    public native void setNativeWidgetPositionAndScale(int var1, int var2, int var3, int var4);

    public void setPauseState(boolean bl) {
        if (this.mAndroidUI == null) {
            this.mAndroidUI = (FrameLayout)this.findViewById(2131362499);
        }
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda15(this, bl));
    }

    public void setUseFullscreen(int n) {
        this.mUseFullscreen = n;
    }

    public native void setWindowSize(int var1, int var2);

    public void showClient() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda24(this));
    }

    public void showClientSettings() {
        this.runOnUiThread(new Runnable(this){
            final NvEventQueueActivity this$0;
            {
                this.this$0 = nvEventQueueActivity;
            }

            @Override
            public void run() {
                if (this.this$0.mDialogClientSettings != null) {
                    NvEventQueueActivity.access$002(this.this$0, null);
                }
                NvEventQueueActivity.access$002(this.this$0, new DialogClientSettings());
                this.this$0.mDialogClientSettings.show(this.this$0.getSupportFragmentManager(), "test");
            }
        });
    }

    public void showDialog(int n, int n2, String string2, String string3, String string4, String string5) {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda13(this, n, n2, string2, string3, string4, string5));
    }

    public void showGps() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda1(this));
    }

    public void showHud() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda2(this));
    }

    public void showInputLayout() {
        this.runOnUiThread(new Runnable(this){
            final NvEventQueueActivity this$0;
            {
                this.this$0 = nvEventQueueActivity;
            }

            @Override
            public void run() {
                this.this$0.mInputManager.ShowInputLayout();
            }
        });
    }

    public void showMenu() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda3(this));
    }

    public void showNotification(int n, String string2, int n2, String string3, String string4) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("t", n);
            jSONObject.put("d", n2);
            jSONObject.put("k", (Object)string4);
            jSONObject.put("a", (Object)string3);
            jSONObject.put("i", (Object)string2);
        }
        catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }
        this.localShowNotification(jSONObject);
    }

    public void showSpeed() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda4(this));
    }

    public void showSplash() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda5(this));
    }

    public native void showTab();

    public void showZona() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda6(this));
    }

    public void showradar() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda7(this));
    }

    public void showx2() {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda8(this));
    }

    public boolean swapBuffers() {
        int n = this.SwapBufferSkip;
        if (n > 0) {
            this.SwapBufferSkip = n - 1;
            System.out.println("swapBuffer wait");
            return true;
        }
        Object object = this.eglSurface;
        if (object == null) {
            System.out.println("eglSurface is NULL");
            return false;
        }
        if (!this.egl.eglSwapBuffers(this.eglDisplay, (EGLSurface)object)) {
            object = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("eglSwapBufferrr: ");
            stringBuilder.append(this.egl.eglGetError());
            ((PrintStream)object).println(stringBuilder.toString());
            return false;
        }
        return true;
    }

    protected void systemCleanup() {
        if (this.ranInit) {
            this.cleanup();
        }
        this.cleanupEGL();
    }

    protected boolean systemInit() {
        SurfaceView surfaceView;
        System.out.println("ln systemInit");
        this.setContentView(2131558487);
        this.mSurfaceView = surfaceView = (SurfaceView)this.findViewById(2131362217);
        this.mRootFrame = (FrameLayout)this.findViewById(2131362213);
        this.mAndroidUI = (FrameLayout)this.findViewById(2131362499);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(2);
        surfaceHolder.setKeepScreenOn(true);
        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(true);
        this.mRootFrame.setOnTouchListener((View.OnTouchListener)this);
        this.mInputManager = new InputManager(this);
        this.mHeightProvider = new HeightProvider(this).init((View)this.mRootFrame).setHeightListener(this);
        this.mBrDialogWindow = new BrDialogWindow(this);
        this.mHudManager = new HudManager(this);
        this.mSpeedometer = new Speedometer(this);
        this.mMenu = new Menu(this);
        this.mChooseServer = new ChooseServer(this);
        this.DoResumeEvent();
        surfaceHolder.addCallback(new SurfaceHolder.Callback(this, this){
            final NvEventQueueActivity this$0;
            final NvEventQueueActivity val$act;
            {
                this.this$0 = nvEventQueueActivity;
                this.val$act = nvEventQueueActivity2;
            }

            public void surfaceChanged(SurfaceHolder object, int n, int n2, int n3) {
                object = System.out;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Surface changed: ");
                stringBuilder.append(n2);
                stringBuilder.append(", ");
                stringBuilder.append(n3);
                ((PrintStream)object).println(stringBuilder.toString());
                NvEventQueueActivity.access$402(this.this$0, n2);
                NvEventQueueActivity.access$502(this.this$0, n3);
                object = this.this$0;
                ((NvEventQueueActivity)object).setWindowSize(((NvEventQueueActivity)object).surfaceWidth, this.this$0.surfaceHeight);
            }

            public void surfaceCreated(SurfaceHolder object) {
                System.out.println("systemInit.surfaceCreated");
                boolean bl = this.this$0.cachedSurfaceHolder == null;
                this.this$0.cachedSurfaceHolder = object;
                if (this.this$0.fixedWidth != 0 && this.this$0.fixedHeight != 0) {
                    System.out.println("Setting fixed window size");
                    object.setFixedSize(this.this$0.fixedWidth, this.this$0.fixedHeight);
                }
                NvEventQueueActivity.access$302(this.this$0, true);
                if (!this.this$0.supportPauseResume && !this.this$0.init(true)) {
                    this.this$0.handler.post(new Runnable(this){
                        final 4 this$1;
                        {
                            this.this$1 = var1_1;
                        }

                        @Override
                        public void run() {
                            new AlertDialog.Builder((Context)this.this$1.val$act).setMessage((CharSequence)"Application initialization failed. The application will exit.").setPositiveButton((CharSequence)"Ok", new DialogInterface.OnClickListener(this){
                                final 1 this$2;
                                {
                                    this.this$2 = var1_1;
                                }

                                public void onClick(DialogInterface dialogInterface, int n) {
                                    this.this$2.this$1.this$0.finish();
                                }
                            }).setCancelable(false).show();
                        }
                    });
                }
                if (!bl && this.this$0.ResumeEventDone) {
                    System.out.println("entering resumeEvent");
                    this.this$0.resumeEvent();
                    System.out.println("returned from resumeEvent");
                }
                object = this.this$0;
                ((NvEventQueueActivity)object).setWindowSize(((NvEventQueueActivity)object).surfaceWidth, this.this$0.surfaceHeight);
            }

            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                System.out.println("systemInit.surfaceDestroyed");
                NvEventQueueActivity.access$602(this.this$0, false);
                this.this$0.pauseEvent();
                this.this$0.destroyEGLSurface();
            }
        });
        return true;
    }

    public native void togglePlayer(int var1);

    public native boolean touchEvent(int var1, int var2, int var3, MotionEvent var4);

    public boolean unMakeCurrent() {
        if (!this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("egl(Un)MakeCurrent err: ");
            stringBuilder.append(this.egl.eglGetError());
            printStream.println(stringBuilder.toString());
            return false;
        }
        return true;
    }

    public void updateHudInfo(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda10(this, n, n2, n3, n4, n5, n6, n7, n8));
    }

    public void updateSpeedInfo(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda12(this, n, n2, n3, n4, n5, n6, n7, n8));
    }

    public void updateSplash(int n) {
        this.runOnUiThread(new NvEventQueueActivity$$ExternalSyntheticLambda9(this, n));
    }

    public class RawData {
        public byte[] data;
        public int length;
        final NvEventQueueActivity this$0;

        public RawData(NvEventQueueActivity nvEventQueueActivity) {
            this.this$0 = nvEventQueueActivity;
        }
    }

    public class RawTexture
    extends RawData {
        public int height;
        final NvEventQueueActivity this$0;
        public int width;

        public RawTexture(NvEventQueueActivity nvEventQueueActivity) {
            this.this$0 = nvEventQueueActivity;
            super(nvEventQueueActivity);
        }
    }
}

