/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ClipDescription
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.os.ResultReceiver
 *  android.text.TextUtils
 *  android.view.inputmethod.EditorInfo
 *  android.view.inputmethod.InputConnection
 *  android.view.inputmethod.InputConnectionWrapper
 *  android.view.inputmethod.InputContentInfo
 */
package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_INTEROP_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    @Deprecated
    public InputConnectionCompat() {
    }

    public static boolean commitContent(InputConnection inputConnection, EditorInfo object, InputContentInfoCompat inputContentInfoCompat, int n, Bundle bundle) {
        boolean bl;
        ClipDescription clipDescription = inputContentInfoCompat.getDescription();
        boolean bl2 = false;
        Bundle bundle2 = EditorInfoCompat.getContentMimeTypes(object);
        int n2 = ((String[])bundle2).length;
        int n3 = 0;
        while (true) {
            bl = bl2;
            if (n3 >= n2) break;
            if (clipDescription.hasMimeType(bundle2[n3])) {
                bl = true;
                break;
            }
            ++n3;
        }
        if (!bl) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo)inputContentInfoCompat.unwrap(), n, bundle);
        }
        switch (EditorInfoCompat.getProtocol(object)) {
            default: {
                return false;
            }
            case 3: 
            case 4: {
                bl = false;
                break;
            }
            case 2: {
                bl = true;
            }
        }
        bundle2 = new Bundle();
        object = bl ? COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY : COMMIT_CONTENT_CONTENT_URI_KEY;
        bundle2.putParcelable((String)object, (Parcelable)inputContentInfoCompat.getContentUri());
        object = bl ? COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY : COMMIT_CONTENT_DESCRIPTION_KEY;
        bundle2.putParcelable((String)object, (Parcelable)inputContentInfoCompat.getDescription());
        object = bl ? COMMIT_CONTENT_LINK_URI_INTEROP_KEY : COMMIT_CONTENT_LINK_URI_KEY;
        bundle2.putParcelable((String)object, (Parcelable)inputContentInfoCompat.getLinkUri());
        object = bl ? COMMIT_CONTENT_FLAGS_INTEROP_KEY : COMMIT_CONTENT_FLAGS_KEY;
        bundle2.putInt((String)object, n);
        object = bl ? COMMIT_CONTENT_OPTS_INTEROP_KEY : COMMIT_CONTENT_OPTS_KEY;
        bundle2.putParcelable((String)object, (Parcelable)bundle);
        object = bl ? COMMIT_CONTENT_INTEROP_ACTION : COMMIT_CONTENT_ACTION;
        return inputConnection.performPrivateCommand((String)object, bundle2);
    }

    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, OnCommitContentListener onCommitContentListener) {
        if (inputConnection != null) {
            if (editorInfo != null) {
                if (onCommitContentListener != null) {
                    if (Build.VERSION.SDK_INT >= 25) {
                        return new InputConnectionWrapper(inputConnection, false, onCommitContentListener){
                            final OnCommitContentListener val$listener;
                            {
                                this.val$listener = onCommitContentListener;
                                super(inputConnection, bl);
                            }

                            public boolean commitContent(InputContentInfo inputContentInfo, int n, Bundle bundle) {
                                if (this.val$listener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), n, bundle)) {
                                    return true;
                                }
                                return super.commitContent(inputContentInfo, n, bundle);
                            }
                        };
                    }
                    if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
                        return inputConnection;
                    }
                    return new InputConnectionWrapper(inputConnection, false, onCommitContentListener){
                        final OnCommitContentListener val$listener;
                        {
                            this.val$listener = onCommitContentListener;
                            super(inputConnection, bl);
                        }

                        public boolean performPrivateCommand(String string2, Bundle bundle) {
                            if (InputConnectionCompat.handlePerformPrivateCommand(string2, bundle, this.val$listener)) {
                                return true;
                            }
                            return super.performPrivateCommand(string2, bundle);
                        }
                    };
                }
                throw new IllegalArgumentException("onCommitContentListener must be non-null");
            }
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        throw new IllegalArgumentException("inputConnection must be non-null");
    }

    static boolean handlePerformPrivateCommand(String string2, Bundle bundle, OnCommitContentListener onCommitContentListener) {
        block18: {
            boolean bl;
            ResultReceiver resultReceiver;
            int n;
            int n2;
            block15: {
                block17: {
                    block16: {
                        n2 = 0;
                        if (bundle == null) {
                            return false;
                        }
                        if (!TextUtils.equals((CharSequence)COMMIT_CONTENT_ACTION, (CharSequence)string2)) break block16;
                        n = 0;
                        break block17;
                    }
                    if (!TextUtils.equals((CharSequence)COMMIT_CONTENT_INTEROP_ACTION, (CharSequence)string2)) break block18;
                    n = 1;
                }
                string2 = null;
                boolean bl2 = false;
                Object object = n != 0 ? COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY : COMMIT_CONTENT_RESULT_RECEIVER_KEY;
                try {
                    resultReceiver = (ResultReceiver)bundle.getParcelable((String)object);
                    object = n != 0 ? COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY : COMMIT_CONTENT_CONTENT_URI_KEY;
                    string2 = resultReceiver;
                }
                catch (Throwable throwable) {
                    if (string2 != null) {
                        string2.send(0, null);
                    }
                    throw throwable;
                }
                Uri uri = (Uri)bundle.getParcelable((String)object);
                object = n != 0 ? COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY : COMMIT_CONTENT_DESCRIPTION_KEY;
                string2 = resultReceiver;
                ClipDescription clipDescription = (ClipDescription)bundle.getParcelable((String)object);
                object = n != 0 ? COMMIT_CONTENT_LINK_URI_INTEROP_KEY : COMMIT_CONTENT_LINK_URI_KEY;
                string2 = resultReceiver;
                Uri uri2 = (Uri)bundle.getParcelable((String)object);
                object = n != 0 ? COMMIT_CONTENT_FLAGS_INTEROP_KEY : COMMIT_CONTENT_FLAGS_KEY;
                string2 = resultReceiver;
                int n3 = bundle.getInt((String)object);
                object = n != 0 ? COMMIT_CONTENT_OPTS_INTEROP_KEY : COMMIT_CONTENT_OPTS_KEY;
                string2 = resultReceiver;
                bundle = (Bundle)bundle.getParcelable((String)object);
                bl = bl2;
                if (uri == null) break block15;
                bl = bl2;
                if (clipDescription == null) break block15;
                string2 = resultReceiver;
                string2 = resultReceiver;
                object = new InputContentInfoCompat(uri, clipDescription, uri2);
                string2 = resultReceiver;
                bl = onCommitContentListener.onCommitContent((InputContentInfoCompat)object, n3, bundle);
            }
            if (resultReceiver != null) {
                n = n2;
                if (bl) {
                    n = 1;
                }
                resultReceiver.send(n, null);
            }
            return bl;
        }
        return false;
    }

    public static interface OnCommitContentListener {
        public boolean onCommitContent(InputContentInfoCompat var1, int var2, Bundle var3);
    }
}

