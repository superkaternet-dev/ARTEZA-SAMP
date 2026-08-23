/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Fragment
 *  android.content.ComponentCallbacks
 *  android.content.ComponentCallbacks2
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.res.AssetFileDescriptor
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 *  android.util.Log
 *  android.view.View
 */
package com.bumptech.glide;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.GeneratedAppGlideModule;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.GlideExperiments;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Registry;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.load.model.AssetUriLoader;
import com.bumptech.glide.load.model.ByteArrayLoader;
import com.bumptech.glide.load.model.ByteBufferEncoder;
import com.bumptech.glide.load.model.ByteBufferFileLoader;
import com.bumptech.glide.load.model.DataUrlLoader;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.MediaStoreFileLoader;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.load.model.UnitModelLoader;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.UrlUriLoader;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.bumptech.glide.load.model.stream.QMediaStoreUriLoader;
import com.bumptech.glide.load.model.stream.UrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapImageDecoderResourceDecoder;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.ExifInterfaceImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import com.bumptech.glide.load.resource.bitmap.InputStreamBitmapImageDecoderResourceDecoder;
import com.bumptech.glide.load.resource.bitmap.ParcelFileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.ResourceBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.UnitBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.load.resource.bytes.ByteBufferRewinder;
import com.bumptech.glide.load.resource.drawable.AnimatedWebpDecoder;
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder;
import com.bumptech.glide.load.resource.drawable.UnitDrawableDecoder;
import com.bumptech.glide.load.resource.file.FileDecoder;
import com.bumptech.glide.load.resource.gif.ByteBufferGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableEncoder;
import com.bumptech.glide.load.resource.gif.GifFrameResourceDecoder;
import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.DrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Glide
implements ComponentCallbacks2 {
    private static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
    private static final String TAG = "Glide";
    private static volatile Glide glide;
    private static volatile boolean isInitializing;
    private final ArrayPool arrayPool;
    private final BitmapPool bitmapPool;
    private BitmapPreFiller bitmapPreFiller;
    private final ConnectivityMonitorFactory connectivityMonitorFactory;
    private final RequestOptionsFactory defaultRequestOptionsFactory;
    private final Engine engine;
    private final GlideContext glideContext;
    private final List<RequestManager> managers = new ArrayList<RequestManager>();
    private final MemoryCache memoryCache;
    private MemoryCategory memoryCategory = MemoryCategory.NORMAL;
    private final Registry registry;
    private final RequestManagerRetriever requestManagerRetriever;

    Glide(Context context, Engine engine, MemoryCache resourceDecoder, BitmapPool bitmapPool, ArrayPool arrayPool, RequestManagerRetriever object, ConnectivityMonitorFactory connectivityMonitorFactory, int n, RequestOptionsFactory requestOptionsFactory, Map<Class<?>, TransitionOptions<?, ?>> map, List<RequestListener<Object>> list, GlideExperiments glideExperiments) {
        Registry registry;
        this.engine = engine;
        this.bitmapPool = bitmapPool;
        this.arrayPool = arrayPool;
        this.memoryCache = resourceDecoder;
        this.requestManagerRetriever = object;
        this.connectivityMonitorFactory = connectivityMonitorFactory;
        this.defaultRequestOptionsFactory = requestOptionsFactory;
        connectivityMonitorFactory = context.getResources();
        this.registry = registry = new Registry();
        registry.register(new DefaultImageHeaderParser());
        if (Build.VERSION.SDK_INT >= 27) {
            registry.register(new ExifInterfaceImageHeaderParser());
        }
        List<ImageHeaderParser> list2 = registry.getImageHeaderParsers();
        ByteBufferGifDecoder byteBufferGifDecoder = new ByteBufferGifDecoder(context, list2, bitmapPool, arrayPool);
        ResourceDecoder<ParcelFileDescriptor, Bitmap> resourceDecoder2 = VideoDecoder.parcel(bitmapPool);
        Downsampler downsampler = new Downsampler(registry.getImageHeaderParsers(), connectivityMonitorFactory.getDisplayMetrics(), bitmapPool, arrayPool);
        if (Build.VERSION.SDK_INT >= 28 && glideExperiments.isEnabled(GlideBuilder.EnableImageDecoderForBitmaps.class)) {
            resourceDecoder = new InputStreamBitmapImageDecoderResourceDecoder();
            object = new ByteBufferBitmapImageDecoderResourceDecoder();
        } else {
            object = new ByteBufferBitmapDecoder(downsampler);
            resourceDecoder = new StreamBitmapDecoder(downsampler, arrayPool);
        }
        if (Build.VERSION.SDK_INT >= 28 && glideExperiments.isEnabled(GlideBuilder.EnableImageDecoderForAnimatedWebp.class)) {
            registry.append("Animation", InputStream.class, Drawable.class, AnimatedWebpDecoder.streamDecoder(list2, arrayPool));
            registry.append("Animation", ByteBuffer.class, Drawable.class, AnimatedWebpDecoder.byteBufferDecoder(list2, arrayPool));
        }
        ResourceDrawableDecoder resourceDrawableDecoder = new ResourceDrawableDecoder(context);
        ResourceLoader.StreamFactory streamFactory = new ResourceLoader.StreamFactory((Resources)connectivityMonitorFactory);
        ResourceLoader.UriFactory uriFactory = new ResourceLoader.UriFactory((Resources)connectivityMonitorFactory);
        ResourceLoader.FileDescriptorFactory fileDescriptorFactory = new ResourceLoader.FileDescriptorFactory((Resources)connectivityMonitorFactory);
        ResourceLoader.AssetFileDescriptorFactory assetFileDescriptorFactory = new ResourceLoader.AssetFileDescriptorFactory((Resources)connectivityMonitorFactory);
        BitmapEncoder bitmapEncoder = new BitmapEncoder(arrayPool);
        BitmapBytesTranscoder bitmapBytesTranscoder = new BitmapBytesTranscoder();
        GifDrawableBytesTranscoder gifDrawableBytesTranscoder = new GifDrawableBytesTranscoder();
        ContentResolver contentResolver = context.getContentResolver();
        registry.append(ByteBuffer.class, new ByteBufferEncoder()).append(InputStream.class, new StreamEncoder(arrayPool)).append("Bitmap", ByteBuffer.class, Bitmap.class, object).append("Bitmap", InputStream.class, Bitmap.class, resourceDecoder);
        if (ParcelFileDescriptorRewinder.isSupported()) {
            registry.append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, new ParcelFileDescriptorBitmapDecoder(downsampler));
        }
        registry.append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, resourceDecoder2).append("Bitmap", AssetFileDescriptor.class, Bitmap.class, VideoDecoder.asset(bitmapPool)).append(Bitmap.class, Bitmap.class, UnitModelLoader.Factory.getInstance()).append("Bitmap", Bitmap.class, Bitmap.class, new UnitBitmapDecoder()).append(Bitmap.class, bitmapEncoder).append("BitmapDrawable", ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder((Resources)connectivityMonitorFactory, object)).append("BitmapDrawable", InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder((Resources)connectivityMonitorFactory, resourceDecoder)).append("BitmapDrawable", ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder<ParcelFileDescriptor>((Resources)connectivityMonitorFactory, resourceDecoder2)).append(BitmapDrawable.class, new BitmapDrawableEncoder(bitmapPool, bitmapEncoder)).append("Animation", InputStream.class, GifDrawable.class, new StreamGifDecoder(list2, byteBufferGifDecoder, arrayPool)).append("Animation", ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder).append(GifDrawable.class, new GifDrawableEncoder()).append(GifDecoder.class, GifDecoder.class, UnitModelLoader.Factory.getInstance()).append("Bitmap", GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(bitmapPool)).append(Uri.class, Drawable.class, resourceDrawableDecoder).append(Uri.class, Bitmap.class, new ResourceBitmapDecoder(resourceDrawableDecoder, bitmapPool)).register(new ByteBufferRewinder.Factory()).append(File.class, ByteBuffer.class, new ByteBufferFileLoader.Factory()).append(File.class, InputStream.class, new FileLoader.StreamFactory()).append(File.class, File.class, new FileDecoder()).append(File.class, ParcelFileDescriptor.class, new FileLoader.FileDescriptorFactory()).append(File.class, File.class, UnitModelLoader.Factory.getInstance()).register(new InputStreamRewinder.Factory(arrayPool));
        if (ParcelFileDescriptorRewinder.isSupported()) {
            registry.register(new ParcelFileDescriptorRewinder.Factory());
        }
        registry.append(Integer.TYPE, InputStream.class, streamFactory).append(Integer.TYPE, ParcelFileDescriptor.class, fileDescriptorFactory).append(Integer.class, InputStream.class, streamFactory).append(Integer.class, ParcelFileDescriptor.class, fileDescriptorFactory).append(Integer.class, Uri.class, uriFactory).append(Integer.TYPE, AssetFileDescriptor.class, assetFileDescriptorFactory).append(Integer.class, AssetFileDescriptor.class, assetFileDescriptorFactory).append(Integer.TYPE, Uri.class, uriFactory).append(String.class, InputStream.class, new DataUrlLoader.StreamFactory()).append(Uri.class, InputStream.class, new DataUrlLoader.StreamFactory()).append(String.class, InputStream.class, new StringLoader.StreamFactory()).append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory()).append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory()).append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets())).append(Uri.class, AssetFileDescriptor.class, new AssetUriLoader.FileDescriptorFactory(context.getAssets())).append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context)).append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context));
        if (Build.VERSION.SDK_INT >= 29) {
            registry.append(Uri.class, InputStream.class, new QMediaStoreUriLoader.InputStreamFactory(context));
            registry.append(Uri.class, ParcelFileDescriptor.class, new QMediaStoreUriLoader.FileDescriptorFactory(context));
        }
        registry.append(Uri.class, InputStream.class, new UriLoader.StreamFactory(contentResolver)).append(Uri.class, ParcelFileDescriptor.class, new UriLoader.FileDescriptorFactory(contentResolver)).append(Uri.class, AssetFileDescriptor.class, new UriLoader.AssetFileDescriptorFactory(contentResolver)).append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory()).append(URL.class, InputStream.class, new UrlLoader.StreamFactory()).append(Uri.class, File.class, new MediaStoreFileLoader.Factory(context)).append(GlideUrl.class, InputStream.class, new HttpGlideUrlLoader.Factory()).append(byte[].class, ByteBuffer.class, new ByteArrayLoader.ByteBufferFactory()).append(byte[].class, InputStream.class, new ByteArrayLoader.StreamFactory()).append(Uri.class, Uri.class, UnitModelLoader.Factory.getInstance()).append(Drawable.class, Drawable.class, UnitModelLoader.Factory.getInstance()).append(Drawable.class, Drawable.class, new UnitDrawableDecoder()).register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder((Resources)connectivityMonitorFactory)).register(Bitmap.class, byte[].class, bitmapBytesTranscoder).register(Drawable.class, byte[].class, new DrawableBytesTranscoder(bitmapPool, bitmapBytesTranscoder, gifDrawableBytesTranscoder)).register(GifDrawable.class, byte[].class, gifDrawableBytesTranscoder);
        if (Build.VERSION.SDK_INT >= 23) {
            resourceDecoder = VideoDecoder.byteBuffer(bitmapPool);
            registry.append(ByteBuffer.class, Bitmap.class, resourceDecoder);
            registry.append(ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder<ByteBuffer>((Resources)connectivityMonitorFactory, resourceDecoder));
        }
        this.glideContext = new GlideContext(context, arrayPool, registry, new ImageViewTargetFactory(), requestOptionsFactory, map, list, engine, glideExperiments, n);
    }

    private static void checkAndInitializeGlide(Context context, GeneratedAppGlideModule generatedAppGlideModule) {
        if (!isInitializing) {
            isInitializing = true;
            Glide.initializeGlide(context, generatedAppGlideModule);
            isInitializing = false;
            return;
        }
        throw new IllegalStateException("You cannot call Glide.get() in registerComponents(), use the provided Glide instance instead");
    }

    public static void enableHardwareBitmaps() {
        HardwareConfigState.getInstance().unblockHardwareBitmaps();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Glide get(Context context) {
        if (glide != null) return glide;
        GeneratedAppGlideModule generatedAppGlideModule = Glide.getAnnotationGeneratedGlideModules(context.getApplicationContext());
        synchronized (Glide.class) {
            if (glide != null) return glide;
            Glide.checkAndInitializeGlide(context, generatedAppGlideModule);
            return glide;
        }
    }

    private static GeneratedAppGlideModule getAnnotationGeneratedGlideModules(Context object) {
        block6: {
            Object var2_6 = null;
            Object var1_7 = null;
            try {
                object = (GeneratedAppGlideModule)Class.forName("com.bumptech.glide.GeneratedAppGlideModuleImpl").getDeclaredConstructor(Context.class).newInstance(object.getApplicationContext());
            }
            catch (InvocationTargetException invocationTargetException) {
                Glide.throwIncorrectGlideModule(invocationTargetException);
                object = var2_6;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Glide.throwIncorrectGlideModule(noSuchMethodException);
                object = var1_7;
            }
            catch (IllegalAccessException illegalAccessException) {
                Glide.throwIncorrectGlideModule(illegalAccessException);
                object = var1_7;
            }
            catch (InstantiationException instantiationException) {
                Glide.throwIncorrectGlideModule(instantiationException);
                object = var1_7;
            }
            catch (ClassNotFoundException classNotFoundException) {
                object = var1_7;
                if (!Log.isLoggable((String)TAG, (int)5)) break block6;
                Log.w((String)TAG, (String)"Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored");
                object = var1_7;
            }
        }
        return object;
    }

    public static File getPhotoCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    public static File getPhotoCacheDir(Context object, String string2) {
        if ((object = object.getCacheDir()) != null) {
            if (!((File)(object = new File((File)object, string2))).isDirectory() && !((File)object).mkdirs()) {
                return null;
            }
            return object;
        }
        if (Log.isLoggable((String)TAG, (int)6)) {
            Log.e((String)TAG, (String)"default disk cache dir is null");
        }
        return null;
    }

    private static RequestManagerRetriever getRetriever(Context context) {
        Preconditions.checkNotNull(context, "You cannot start a load on a not yet attached View or a Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
        return Glide.get(context).getRequestManagerRetriever();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void init(Context context, GlideBuilder glideBuilder) {
        GeneratedAppGlideModule generatedAppGlideModule = Glide.getAnnotationGeneratedGlideModules(context);
        synchronized (Glide.class) {
            if (glide != null) {
                Glide.tearDown();
            }
            Glide.initializeGlide(context, glideBuilder, generatedAppGlideModule);
            return;
        }
    }

    @Deprecated
    public static void init(Glide glide) {
        synchronized (Glide.class) {
            if (Glide.glide != null) {
                Glide.tearDown();
            }
            Glide.glide = glide;
            return;
        }
    }

    private static void initializeGlide(Context context, GeneratedAppGlideModule generatedAppGlideModule) {
        Glide.initializeGlide(context, new GlideBuilder(), generatedAppGlideModule);
    }

    private static void initializeGlide(Context object, GlideBuilder object2, GeneratedAppGlideModule generatedAppGlideModule) {
        StringBuilder stringBuilder;
        Object object3;
        Object object4;
        Context context = object.getApplicationContext();
        object = Collections.emptyList();
        if (generatedAppGlideModule == null || generatedAppGlideModule.isManifestParsingEnabled()) {
            object = new ManifestParser(context).parse();
        }
        if (generatedAppGlideModule != null && !generatedAppGlideModule.getExcludedModuleClasses().isEmpty()) {
            object4 = generatedAppGlideModule.getExcludedModuleClasses();
            object3 = object.iterator();
            while (object3.hasNext()) {
                GlideModule glideModule = (GlideModule)object3.next();
                if (!object4.contains(glideModule.getClass())) continue;
                if (Log.isLoggable((String)TAG, (int)3)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("AppGlideModule excludes manifest GlideModule: ");
                    stringBuilder.append(glideModule);
                    Log.d((String)TAG, (String)stringBuilder.toString());
                }
                object3.remove();
            }
        }
        if (Log.isLoggable((String)TAG, (int)3)) {
            object3 = object.iterator();
            while (object3.hasNext()) {
                object4 = (GlideModule)object3.next();
                stringBuilder = new StringBuilder();
                stringBuilder.append("Discovered GlideModule from manifest: ");
                stringBuilder.append(object4.getClass());
                Log.d((String)TAG, (String)stringBuilder.toString());
            }
        }
        object3 = generatedAppGlideModule != null ? generatedAppGlideModule.getRequestManagerFactory() : null;
        ((GlideBuilder)object2).setRequestManagerFactory((RequestManagerRetriever.RequestManagerFactory)object3);
        object3 = object.iterator();
        while (object3.hasNext()) {
            ((GlideModule)object3.next()).applyOptions(context, (GlideBuilder)object2);
        }
        if (generatedAppGlideModule != null) {
            generatedAppGlideModule.applyOptions(context, (GlideBuilder)object2);
        }
        object2 = ((GlideBuilder)object2).build(context);
        object3 = object.iterator();
        while (object3.hasNext()) {
            object = (GlideModule)object3.next();
            try {
                object.registerComponents(context, (Glide)object2, ((Glide)object2).registry);
            }
            catch (AbstractMethodError abstractMethodError) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Attempting to register a Glide v3 module. If you see this, you or one of your dependencies may be including Glide v3 even though you're using Glide v4. You'll need to find and remove (or update) the offending dependency. The v3 module name is: ");
                ((StringBuilder)object2).append(object.getClass().getName());
                throw new IllegalStateException(((StringBuilder)object2).toString(), abstractMethodError);
            }
        }
        if (generatedAppGlideModule != null) {
            generatedAppGlideModule.registerComponents(context, (Glide)object2, ((Glide)object2).registry);
        }
        context.registerComponentCallbacks((ComponentCallbacks)object2);
        glide = object2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void tearDown() {
        synchronized (Glide.class) {
            if (glide != null) {
                glide.getContext().getApplicationContext().unregisterComponentCallbacks((ComponentCallbacks)glide);
                Glide.glide.engine.shutdown();
            }
            glide = null;
            return;
        }
    }

    private static void throwIncorrectGlideModule(Exception exception) {
        throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", exception);
    }

    public static RequestManager with(Activity activity) {
        return Glide.getRetriever((Context)activity).get(activity);
    }

    @Deprecated
    public static RequestManager with(Fragment fragment) {
        return Glide.getRetriever((Context)fragment.getActivity()).get(fragment);
    }

    public static RequestManager with(Context context) {
        return Glide.getRetriever(context).get(context);
    }

    public static RequestManager with(View view) {
        return Glide.getRetriever(view.getContext()).get(view);
    }

    public static RequestManager with(androidx.fragment.app.Fragment fragment) {
        return Glide.getRetriever(fragment.getContext()).get(fragment);
    }

    public static RequestManager with(FragmentActivity fragmentActivity) {
        return Glide.getRetriever((Context)fragmentActivity).get(fragmentActivity);
    }

    public void clearDiskCache() {
        Util.assertBackgroundThread();
        this.engine.clearDiskCache();
    }

    public void clearMemory() {
        Util.assertMainThread();
        this.memoryCache.clearMemory();
        this.bitmapPool.clearMemory();
        this.arrayPool.clearMemory();
    }

    public ArrayPool getArrayPool() {
        return this.arrayPool;
    }

    public BitmapPool getBitmapPool() {
        return this.bitmapPool;
    }

    ConnectivityMonitorFactory getConnectivityMonitorFactory() {
        return this.connectivityMonitorFactory;
    }

    public Context getContext() {
        return this.glideContext.getBaseContext();
    }

    GlideContext getGlideContext() {
        return this.glideContext;
    }

    public Registry getRegistry() {
        return this.registry;
    }

    public RequestManagerRetriever getRequestManagerRetriever() {
        return this.requestManagerRetriever;
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public void onLowMemory() {
        this.clearMemory();
    }

    public void onTrimMemory(int n) {
        this.trimMemory(n);
    }

    public void preFillBitmapPool(PreFillType.Builder ... builderArray) {
        synchronized (this) {
            if (this.bitmapPreFiller == null) {
                BitmapPreFiller bitmapPreFiller;
                DecodeFormat decodeFormat = this.defaultRequestOptionsFactory.build().getOptions().get(Downsampler.DECODE_FORMAT);
                this.bitmapPreFiller = bitmapPreFiller = new BitmapPreFiller(this.memoryCache, this.bitmapPool, decodeFormat);
            }
            this.bitmapPreFiller.preFill(builderArray);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void registerRequestManager(RequestManager object) {
        List<RequestManager> list = this.managers;
        synchronized (list) {
            if (!this.managers.contains(object)) {
                this.managers.add((RequestManager)object);
                return;
            }
            object = new IllegalStateException("Cannot register already registered manager");
            throw object;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    boolean removeFromManagers(Target<?> target) {
        List<RequestManager> list = this.managers;
        synchronized (list) {
            Iterator<RequestManager> iterator2 = this.managers.iterator();
            do {
                if (!iterator2.hasNext()) return false;
            } while (!iterator2.next().untrack(target));
            return true;
        }
    }

    public MemoryCategory setMemoryCategory(MemoryCategory memoryCategory) {
        Util.assertMainThread();
        this.memoryCache.setSizeMultiplier(memoryCategory.getMultiplier());
        this.bitmapPool.setSizeMultiplier(memoryCategory.getMultiplier());
        MemoryCategory memoryCategory2 = this.memoryCategory;
        this.memoryCategory = memoryCategory;
        return memoryCategory2;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void trimMemory(int n) {
        Util.assertMainThread();
        List<RequestManager> list = this.managers;
        synchronized (list) {
            Iterator<RequestManager> iterator2 = this.managers.iterator();
            while (true) {
                if (!iterator2.hasNext()) {
                    // MONITOREXIT @DISABLED, blocks:[0, 2, 4, 5] lbl8 : MonitorExitStatement: MONITOREXIT : var2_2
                    this.memoryCache.trimMemory(n);
                    this.bitmapPool.trimMemory(n);
                    this.arrayPool.trimMemory(n);
                    return;
                }
                iterator2.next().onTrimMemory(n);
            }
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void unregisterRequestManager(RequestManager object) {
        List<RequestManager> list = this.managers;
        synchronized (list) {
            if (this.managers.contains(object)) {
                this.managers.remove(object);
                return;
            }
            object = new IllegalStateException("Cannot unregister not yet registered manager");
            throw object;
        }
    }

    public static interface RequestOptionsFactory {
        public RequestOptions build();
    }
}

