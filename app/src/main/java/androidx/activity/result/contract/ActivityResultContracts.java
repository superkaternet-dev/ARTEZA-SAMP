/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 */
package androidx.activity.result.contract;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public final class ActivityResultContracts {
    private ActivityResultContracts() {
    }

    public static class CreateDocument
    extends ActivityResultContract<String, Uri> {
        @Override
        public Intent createIntent(Context context, String string2) {
            return new Intent("android.intent.action.CREATE_DOCUMENT").setType("*/*").putExtra("android.intent.extra.TITLE", string2);
        }

        @Override
        public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context context, String string2) {
            return null;
        }

        @Override
        public final Uri parseResult(int n, Intent intent) {
            if (intent != null && n == -1) {
                return intent.getData();
            }
            return null;
        }
    }

    public static class GetContent
    extends ActivityResultContract<String, Uri> {
        @Override
        public Intent createIntent(Context context, String string2) {
            return new Intent("android.intent.action.GET_CONTENT").addCategory("android.intent.category.OPENABLE").setType(string2);
        }

        @Override
        public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context context, String string2) {
            return null;
        }

        @Override
        public final Uri parseResult(int n, Intent intent) {
            if (intent != null && n == -1) {
                return intent.getData();
            }
            return null;
        }
    }

    public static class GetMultipleContents
    extends ActivityResultContract<String, List<Uri>> {
        static List<Uri> getClipDataUris(Intent intent) {
            LinkedHashSet<Uri> linkedHashSet = new LinkedHashSet<Uri>();
            if (intent.getData() != null) {
                linkedHashSet.add(intent.getData());
            }
            if ((intent = intent.getClipData()) == null && linkedHashSet.isEmpty()) {
                return Collections.emptyList();
            }
            if (intent != null) {
                for (int i = 0; i < intent.getItemCount(); ++i) {
                    Uri uri = intent.getItemAt(i).getUri();
                    if (uri == null) continue;
                    linkedHashSet.add(uri);
                }
            }
            return new ArrayList<Uri>(linkedHashSet);
        }

        @Override
        public Intent createIntent(Context context, String string2) {
            return new Intent("android.intent.action.GET_CONTENT").addCategory("android.intent.category.OPENABLE").setType(string2).putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        }

        @Override
        public final ActivityResultContract.SynchronousResult<List<Uri>> getSynchronousResult(Context context, String string2) {
            return null;
        }

        @Override
        public final List<Uri> parseResult(int n, Intent intent) {
            if (intent != null && n == -1) {
                return GetMultipleContents.getClipDataUris(intent);
            }
            return Collections.emptyList();
        }
    }

    public static class OpenDocument
    extends ActivityResultContract<String[], Uri> {
        @Override
        public Intent createIntent(Context context, String[] stringArray) {
            return new Intent("android.intent.action.OPEN_DOCUMENT").putExtra("android.intent.extra.MIME_TYPES", stringArray).setType("*/*");
        }

        @Override
        public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context context, String[] stringArray) {
            return null;
        }

        @Override
        public final Uri parseResult(int n, Intent intent) {
            if (intent != null && n == -1) {
                return intent.getData();
            }
            return null;
        }
    }

    public static class OpenDocumentTree
    extends ActivityResultContract<Uri, Uri> {
        @Override
        public Intent createIntent(Context context, Uri uri) {
            context = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
            if (Build.VERSION.SDK_INT >= 26 && uri != null) {
                context.putExtra("android.provider.extra.INITIAL_URI", (Parcelable)uri);
            }
            return context;
        }

        @Override
        public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context context, Uri uri) {
            return null;
        }

        @Override
        public final Uri parseResult(int n, Intent intent) {
            if (intent != null && n == -1) {
                return intent.getData();
            }
            return null;
        }
    }

    public static class OpenMultipleDocuments
    extends ActivityResultContract<String[], List<Uri>> {
        @Override
        public Intent createIntent(Context context, String[] stringArray) {
            return new Intent("android.intent.action.OPEN_DOCUMENT").putExtra("android.intent.extra.MIME_TYPES", stringArray).putExtra("android.intent.extra.ALLOW_MULTIPLE", true).setType("*/*");
        }

        @Override
        public final ActivityResultContract.SynchronousResult<List<Uri>> getSynchronousResult(Context context, String[] stringArray) {
            return null;
        }

        @Override
        public final List<Uri> parseResult(int n, Intent intent) {
            if (n == -1 && intent != null) {
                return GetMultipleContents.getClipDataUris(intent);
            }
            return Collections.emptyList();
        }
    }

    public static final class PickContact
    extends ActivityResultContract<Void, Uri> {
        @Override
        public Intent createIntent(Context context, Void void_) {
            return new Intent("android.intent.action.PICK").setType("vnd.android.cursor.dir/contact");
        }

        @Override
        public Uri parseResult(int n, Intent intent) {
            if (intent != null && n == -1) {
                return intent.getData();
            }
            return null;
        }
    }

    public static final class RequestMultiplePermissions
    extends ActivityResultContract<String[], Map<String, Boolean>> {
        public static final String ACTION_REQUEST_PERMISSIONS = "androidx.activity.result.contract.action.REQUEST_PERMISSIONS";
        public static final String EXTRA_PERMISSIONS = "androidx.activity.result.contract.extra.PERMISSIONS";
        public static final String EXTRA_PERMISSION_GRANT_RESULTS = "androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS";

        static Intent createIntent(String[] stringArray) {
            return new Intent(ACTION_REQUEST_PERMISSIONS).putExtra(EXTRA_PERMISSIONS, stringArray);
        }

        @Override
        public Intent createIntent(Context context, String[] stringArray) {
            return RequestMultiplePermissions.createIntent(stringArray);
        }

        @Override
        public ActivityResultContract.SynchronousResult<Map<String, Boolean>> getSynchronousResult(Context context, String[] stringArray) {
            if (stringArray != null && stringArray.length != 0) {
                ArrayMap<String, Boolean> arrayMap = new ArrayMap<String, Boolean>();
                boolean bl = true;
                for (String string2 : stringArray) {
                    boolean bl2 = ContextCompat.checkSelfPermission(context, string2) == 0;
                    arrayMap.put(string2, bl2);
                    if (bl2) continue;
                    bl = false;
                }
                if (bl) {
                    return new ActivityResultContract.SynchronousResult<Map<String, Boolean>>(arrayMap);
                }
                return null;
            }
            return new ActivityResultContract.SynchronousResult<Map<String, Boolean>>(Collections.emptyMap());
        }

        @Override
        public Map<String, Boolean> parseResult(int n, Intent object) {
            if (n != -1) {
                return Collections.emptyMap();
            }
            if (object == null) {
                return Collections.emptyMap();
            }
            String[] stringArray = object.getStringArrayExtra(EXTRA_PERMISSIONS);
            int[] nArray = object.getIntArrayExtra(EXTRA_PERMISSION_GRANT_RESULTS);
            if (nArray != null && stringArray != null) {
                HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
                int n2 = stringArray.length;
                for (n = 0; n < n2; ++n) {
                    object = stringArray[n];
                    boolean bl = nArray[n] == 0;
                    hashMap.put((String)object, bl);
                }
                return hashMap;
            }
            return Collections.emptyMap();
        }
    }

    public static final class RequestPermission
    extends ActivityResultContract<String, Boolean> {
        @Override
        public Intent createIntent(Context context, String string2) {
            return RequestMultiplePermissions.createIntent(new String[]{string2});
        }

        @Override
        public ActivityResultContract.SynchronousResult<Boolean> getSynchronousResult(Context context, String string2) {
            if (string2 == null) {
                return new ActivityResultContract.SynchronousResult<Boolean>(false);
            }
            if (ContextCompat.checkSelfPermission(context, string2) == 0) {
                return new ActivityResultContract.SynchronousResult<Boolean>(true);
            }
            return null;
        }

        @Override
        public Boolean parseResult(int n, Intent object) {
            boolean bl = false;
            Boolean bl2 = false;
            if (object != null && n == -1) {
                if ((object = (Object)object.getIntArrayExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS")) != null && ((Intent)object).length != 0) {
                    if (object[0] == false) {
                        bl = true;
                    }
                    return bl;
                }
                return bl2;
            }
            return bl2;
        }
    }

    public static final class StartActivityForResult
    extends ActivityResultContract<Intent, ActivityResult> {
        public static final String EXTRA_ACTIVITY_OPTIONS_BUNDLE = "androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE";

        @Override
        public Intent createIntent(Context context, Intent intent) {
            return intent;
        }

        @Override
        public ActivityResult parseResult(int n, Intent intent) {
            return new ActivityResult(n, intent);
        }
    }

    public static final class StartIntentSenderForResult
    extends ActivityResultContract<IntentSenderRequest, ActivityResult> {
        public static final String ACTION_INTENT_SENDER_REQUEST = "androidx.activity.result.contract.action.INTENT_SENDER_REQUEST";
        public static final String EXTRA_INTENT_SENDER_REQUEST = "androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST";
        public static final String EXTRA_SEND_INTENT_EXCEPTION = "androidx.activity.result.contract.extra.SEND_INTENT_EXCEPTION";

        @Override
        public Intent createIntent(Context context, IntentSenderRequest intentSenderRequest) {
            return new Intent(ACTION_INTENT_SENDER_REQUEST).putExtra(EXTRA_INTENT_SENDER_REQUEST, (Parcelable)intentSenderRequest);
        }

        @Override
        public ActivityResult parseResult(int n, Intent intent) {
            return new ActivityResult(n, intent);
        }
    }

    public static class TakePicture
    extends ActivityResultContract<Uri, Boolean> {
        @Override
        public Intent createIntent(Context context, Uri uri) {
            return new Intent("android.media.action.IMAGE_CAPTURE").putExtra("output", (Parcelable)uri);
        }

        @Override
        public final ActivityResultContract.SynchronousResult<Boolean> getSynchronousResult(Context context, Uri uri) {
            return null;
        }

        @Override
        public final Boolean parseResult(int n, Intent intent) {
            boolean bl = n == -1;
            return bl;
        }
    }

    public static class TakePicturePreview
    extends ActivityResultContract<Void, Bitmap> {
        @Override
        public Intent createIntent(Context context, Void void_) {
            return new Intent("android.media.action.IMAGE_CAPTURE");
        }

        @Override
        public final ActivityResultContract.SynchronousResult<Bitmap> getSynchronousResult(Context context, Void void_) {
            return null;
        }

        @Override
        public final Bitmap parseResult(int n, Intent intent) {
            if (intent != null && n == -1) {
                return (Bitmap)intent.getParcelableExtra("data");
            }
            return null;
        }
    }

    public static class TakeVideo
    extends ActivityResultContract<Uri, Bitmap> {
        @Override
        public Intent createIntent(Context context, Uri uri) {
            return new Intent("android.media.action.VIDEO_CAPTURE").putExtra("output", (Parcelable)uri);
        }

        @Override
        public final ActivityResultContract.SynchronousResult<Bitmap> getSynchronousResult(Context context, Uri uri) {
            return null;
        }

        @Override
        public final Bitmap parseResult(int n, Intent intent) {
            if (intent != null && n == -1) {
                return (Bitmap)intent.getParcelableExtra("data");
            }
            return null;
        }
    }
}

