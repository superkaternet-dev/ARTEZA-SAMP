/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Action
 *  android.app.Notification$Action$Builder
 *  android.app.Notification$BigPictureStyle
 *  android.app.Notification$BigTextStyle
 *  android.app.Notification$BubbleMetadata
 *  android.app.Notification$BubbleMetadata$Builder
 *  android.app.Notification$Builder
 *  android.app.Notification$DecoratedCustomViewStyle
 *  android.app.Notification$InboxStyle
 *  android.app.Notification$MessagingStyle
 *  android.app.Notification$MessagingStyle$Message
 *  android.app.Notification$Style
 *  android.app.PendingIntent
 *  android.app.Person
 *  android.app.RemoteInput
 *  android.app.RemoteInput$Builder
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Icon
 *  android.media.AudioAttributes$Builder
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.os.SystemClock
 *  android.text.SpannableStringBuilder
 *  android.text.TextUtils
 *  android.text.style.TextAppearanceSpan
 *  android.util.SparseArray
 *  android.widget.RemoteViews
 */
package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.SparseArray;
import android.widget.RemoteViews;
import androidx.core.R;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompatBuilder;
import androidx.core.app.NotificationCompatJellybean;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.text.BidiFormatter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NotificationCompat {
    public static final int BADGE_ICON_LARGE = 2;
    public static final int BADGE_ICON_NONE = 0;
    public static final int BADGE_ICON_SMALL = 1;
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_NAVIGATION = "navigation";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_REMINDER = "reminder";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    public static final int COLOR_DEFAULT = 0;
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_LIGHTS = 4;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    public static final String EXTRA_AUDIO_CONTENTS_URI = "android.audioContents";
    public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";
    public static final String EXTRA_BIG_TEXT = "android.bigText";
    public static final String EXTRA_CHRONOMETER_COUNT_DOWN = "android.chronometerCountDown";
    public static final String EXTRA_COMPACT_ACTIONS = "android.compactActions";
    public static final String EXTRA_CONVERSATION_TITLE = "android.conversationTitle";
    public static final String EXTRA_HIDDEN_CONVERSATION_TITLE = "android.hiddenConversationTitle";
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_IS_GROUP_CONVERSATION = "android.isGroupConversation";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
    public static final String EXTRA_MESSAGES = "android.messages";
    public static final String EXTRA_MESSAGING_STYLE_USER = "android.messagingStyleUser";
    public static final String EXTRA_PEOPLE = "android.people";
    public static final String EXTRA_PICTURE = "android.picture";
    public static final String EXTRA_PROGRESS = "android.progress";
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
    public static final String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";
    public static final String EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName";
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
    public static final String EXTRA_SHOW_WHEN = "android.showWhen";
    public static final String EXTRA_SMALL_ICON = "android.icon";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEMPLATE = "android.template";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_BUBBLE = 4096;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    public static final int FLAG_GROUP_SUMMARY = 512;
    @Deprecated
    public static final int FLAG_HIGH_PRIORITY = 128;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_LOCAL_ONLY = 256;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    public static final int FLAG_SHOW_LIGHTS = 1;
    public static final int GROUP_ALERT_ALL = 0;
    public static final int GROUP_ALERT_CHILDREN = 2;
    public static final int GROUP_ALERT_SUMMARY = 1;
    public static final String GROUP_KEY_SILENT = "silent";
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;
    public static final int STREAM_DEFAULT = -1;
    public static final int VISIBILITY_PRIVATE = 0;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_SECRET = -1;

    @Deprecated
    public NotificationCompat() {
    }

    public static Action getAction(Notification notification, int n) {
        if (Build.VERSION.SDK_INT >= 20) {
            return NotificationCompat.getActionCompatFromAction(notification.actions[n]);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            Notification.Action action = notification.actions[n];
            Object var2_3 = null;
            SparseArray sparseArray = notification.extras.getSparseParcelableArray("android.support.actionExtras");
            notification = var2_3;
            if (sparseArray != null) {
                notification = (Bundle)sparseArray.get(n);
            }
            return NotificationCompatJellybean.readAction(action.icon, action.title, action.actionIntent, (Bundle)notification);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getAction(notification, n);
        }
        return null;
    }

    static Action getActionCompatFromAction(Notification.Action action) {
        boolean bl;
        int n;
        Object object;
        android.app.RemoteInput remoteInput;
        android.app.RemoteInput[] remoteInputArray = action.getRemoteInputs();
        if (remoteInputArray == null) {
            remoteInput = null;
        } else {
            object = new RemoteInput[remoteInputArray.length];
            n = 0;
            while (true) {
                remoteInput = object;
                if (n >= remoteInputArray.length) break;
                remoteInput = remoteInputArray[n];
                String string2 = remoteInput.getResultKey();
                CharSequence charSequence = remoteInput.getLabel();
                CharSequence[] charSequenceArray = remoteInput.getChoices();
                bl = remoteInput.getAllowFreeFormInput();
                int n2 = Build.VERSION.SDK_INT >= 29 ? remoteInput.getEditChoicesBeforeSending() : 0;
                object[n] = new RemoteInput(string2, charSequence, charSequenceArray, bl, n2, remoteInput.getExtras(), null);
                ++n;
            }
        }
        bl = Build.VERSION.SDK_INT >= 24 ? action.getExtras().getBoolean("android.support.allowGeneratedReplies") || action.getAllowGeneratedReplies() : action.getExtras().getBoolean("android.support.allowGeneratedReplies");
        boolean bl2 = action.getExtras().getBoolean("android.support.action.showsUserInterface", true);
        n = Build.VERSION.SDK_INT >= 28 ? action.getSemanticAction() : action.getExtras().getInt("android.support.action.semanticAction", 0);
        boolean bl3 = Build.VERSION.SDK_INT >= 29 ? action.isContextual() : false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (action.getIcon() == null && action.icon != 0) {
                return new Action(action.icon, action.title, action.actionIntent, action.getExtras(), (RemoteInput[])remoteInput, null, bl, n, bl2, bl3);
            }
            object = action.getIcon() == null ? null : IconCompat.createFromIconOrNullIfZeroResId(action.getIcon());
            return new Action((IconCompat)object, action.title, action.actionIntent, action.getExtras(), (RemoteInput[])remoteInput, null, bl, n, bl2, bl3);
        }
        return new Action(action.icon, action.title, action.actionIntent, action.getExtras(), (RemoteInput[])remoteInput, null, bl, n, bl2, bl3);
    }

    public static int getActionCount(Notification notification) {
        int n = Build.VERSION.SDK_INT;
        int n2 = 0;
        if (n >= 19) {
            if (notification.actions != null) {
                n2 = notification.actions.length;
            }
            return n2;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getActionCount(notification);
        }
        return 0;
    }

    public static boolean getAllowSystemGeneratedContextualActions(Notification notification) {
        if (Build.VERSION.SDK_INT >= 29) {
            return notification.getAllowSystemGeneratedContextualActions();
        }
        return false;
    }

    public static int getBadgeIconType(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getBadgeIconType();
        }
        return 0;
    }

    public static BubbleMetadata getBubbleMetadata(Notification notification) {
        if (Build.VERSION.SDK_INT >= 29) {
            return BubbleMetadata.fromPlatform(notification.getBubbleMetadata());
        }
        return null;
    }

    public static String getCategory(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.category;
        }
        return null;
    }

    public static String getChannelId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getChannelId();
        }
        return null;
    }

    public static CharSequence getContentTitle(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_TITLE);
    }

    public static Bundle getExtras(Notification notification) {
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification);
        }
        return null;
    }

    public static String getGroup(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return notification.getGroup();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getString("android.support.groupKey");
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString("android.support.groupKey");
        }
        return null;
    }

    public static int getGroupAlertBehavior(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getGroupAlertBehavior();
        }
        return 0;
    }

    public static List<Action> getInvisibleActions(Notification notification) {
        ArrayList<Action> arrayList = new ArrayList<Action>();
        notification = notification.extras.getBundle("android.car.EXTENSIONS");
        if (notification == null) {
            return arrayList;
        }
        if ((notification = notification.getBundle("invisible_actions")) != null) {
            for (int i = 0; i < notification.size(); ++i) {
                arrayList.add(NotificationCompatJellybean.getActionFromBundle(notification.getBundle(Integer.toString(i))));
            }
        }
        return arrayList;
    }

    public static boolean getLocalOnly(Notification notification) {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        if (n >= 20) {
            if ((notification.flags & 0x100) != 0) {
                bl = true;
            }
            return bl;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean("android.support.localOnly");
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getBoolean("android.support.localOnly");
        }
        return false;
    }

    static Notification[] getNotificationArrayFromBundle(Bundle bundle, String string2) {
        Parcelable[] parcelableArray = bundle.getParcelableArray(string2);
        if (!(parcelableArray instanceof Notification[]) && parcelableArray != null) {
            Notification[] notificationArray = new Notification[parcelableArray.length];
            for (int i = 0; i < parcelableArray.length; ++i) {
                notificationArray[i] = (Notification)parcelableArray[i];
            }
            bundle.putParcelableArray(string2, (Parcelable[])notificationArray);
            return notificationArray;
        }
        return (Notification[])parcelableArray;
    }

    public static String getShortcutId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getShortcutId();
        }
        return null;
    }

    public static String getSortKey(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return notification.getSortKey();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getString("android.support.sortKey");
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString("android.support.sortKey");
        }
        return null;
    }

    public static long getTimeoutAfter(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getTimeoutAfter();
        }
        return 0L;
    }

    public static boolean isGroupSummary(Notification notification) {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        if (n >= 20) {
            if ((notification.flags & 0x200) != 0) {
                bl = true;
            }
            return bl;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean("android.support.isGroupSummary");
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getBoolean("android.support.isGroupSummary");
        }
        return false;
    }

    public static class Action {
        static final String EXTRA_SEMANTIC_ACTION = "android.support.action.semanticAction";
        static final String EXTRA_SHOWS_USER_INTERFACE = "android.support.action.showsUserInterface";
        public static final int SEMANTIC_ACTION_ARCHIVE = 5;
        public static final int SEMANTIC_ACTION_CALL = 10;
        public static final int SEMANTIC_ACTION_DELETE = 4;
        public static final int SEMANTIC_ACTION_MARK_AS_READ = 2;
        public static final int SEMANTIC_ACTION_MARK_AS_UNREAD = 3;
        public static final int SEMANTIC_ACTION_MUTE = 6;
        public static final int SEMANTIC_ACTION_NONE = 0;
        public static final int SEMANTIC_ACTION_REPLY = 1;
        public static final int SEMANTIC_ACTION_THUMBS_DOWN = 9;
        public static final int SEMANTIC_ACTION_THUMBS_UP = 8;
        public static final int SEMANTIC_ACTION_UNMUTE = 7;
        public PendingIntent actionIntent;
        @Deprecated
        public int icon;
        private boolean mAllowGeneratedReplies;
        private final RemoteInput[] mDataOnlyRemoteInputs;
        final Bundle mExtras;
        private IconCompat mIcon;
        private final boolean mIsContextual;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        boolean mShowsUserInterface;
        public CharSequence title;

        public Action(int n, CharSequence charSequence, PendingIntent pendingIntent) {
            IconCompat iconCompat = null;
            if (n != 0) {
                iconCompat = IconCompat.createWithResource(null, "", n);
            }
            this(iconCompat, charSequence, pendingIntent);
        }

        Action(int n, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArray, RemoteInput[] remoteInputArray2, boolean bl, int n2, boolean bl2, boolean bl3) {
            IconCompat iconCompat = null;
            if (n != 0) {
                iconCompat = IconCompat.createWithResource(null, "", n);
            }
            this(iconCompat, charSequence, pendingIntent, bundle, remoteInputArray, remoteInputArray2, bl, n2, bl2, bl3);
        }

        public Action(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent) {
            this(iconCompat, charSequence, pendingIntent, new Bundle(), null, null, true, 0, true, false);
        }

        Action(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArray, RemoteInput[] remoteInputArray2, boolean bl, int n, boolean bl2, boolean bl3) {
            this.mShowsUserInterface = true;
            this.mIcon = iconCompat;
            if (iconCompat != null && iconCompat.getType() == 2) {
                this.icon = iconCompat.getResId();
            }
            this.title = androidx.core.app.NotificationCompat$Builder.limitCharSequenceLength(charSequence);
            this.actionIntent = pendingIntent;
            if (bundle == null) {
                bundle = new Bundle();
            }
            this.mExtras = bundle;
            this.mRemoteInputs = remoteInputArray;
            this.mDataOnlyRemoteInputs = remoteInputArray2;
            this.mAllowGeneratedReplies = bl;
            this.mSemanticAction = n;
            this.mShowsUserInterface = bl2;
            this.mIsContextual = bl3;
        }

        public PendingIntent getActionIntent() {
            return this.actionIntent;
        }

        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }

        public RemoteInput[] getDataOnlyRemoteInputs() {
            return this.mDataOnlyRemoteInputs;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        @Deprecated
        public int getIcon() {
            return this.icon;
        }

        public IconCompat getIconCompat() {
            int n;
            if (this.mIcon == null && (n = this.icon) != 0) {
                this.mIcon = IconCompat.createWithResource(null, "", n);
            }
            return this.mIcon;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }

        public int getSemanticAction() {
            return this.mSemanticAction;
        }

        public boolean getShowsUserInterface() {
            return this.mShowsUserInterface;
        }

        public CharSequence getTitle() {
            return this.title;
        }

        public boolean isContextual() {
            return this.mIsContextual;
        }

        public static final class Builder {
            private boolean mAllowGeneratedReplies;
            private final Bundle mExtras;
            private final IconCompat mIcon;
            private final PendingIntent mIntent;
            private boolean mIsContextual;
            private ArrayList<RemoteInput> mRemoteInputs;
            private int mSemanticAction;
            private boolean mShowsUserInterface;
            private final CharSequence mTitle;

            public Builder(int n, CharSequence charSequence, PendingIntent pendingIntent) {
                IconCompat iconCompat = null;
                if (n != 0) {
                    iconCompat = IconCompat.createWithResource(null, "", n);
                }
                this(iconCompat, charSequence, pendingIntent, new Bundle(), null, true, 0, true, false);
            }

            public Builder(Action action) {
                this(action.getIconCompat(), action.title, action.actionIntent, new Bundle(action.mExtras), action.getRemoteInputs(), action.getAllowGeneratedReplies(), action.getSemanticAction(), action.mShowsUserInterface, action.isContextual());
            }

            public Builder(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent) {
                this(iconCompat, charSequence, pendingIntent, new Bundle(), null, true, 0, true, false);
            }

            private Builder(IconCompat object, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArray, boolean bl, int n, boolean bl2, boolean bl3) {
                this.mAllowGeneratedReplies = true;
                this.mShowsUserInterface = true;
                this.mIcon = object;
                this.mTitle = androidx.core.app.NotificationCompat$Builder.limitCharSequenceLength(charSequence);
                this.mIntent = pendingIntent;
                this.mExtras = bundle;
                object = remoteInputArray == null ? null : new ArrayList<RemoteInput>(Arrays.asList(remoteInputArray));
                this.mRemoteInputs = object;
                this.mAllowGeneratedReplies = bl;
                this.mSemanticAction = n;
                this.mShowsUserInterface = bl2;
                this.mIsContextual = bl3;
            }

            private void checkContextualActionNullFields() {
                if (!this.mIsContextual) {
                    return;
                }
                if (this.mIntent != null) {
                    return;
                }
                throw new NullPointerException("Contextual Actions must contain a valid PendingIntent");
            }

            public Builder addExtras(Bundle bundle) {
                if (bundle != null) {
                    this.mExtras.putAll(bundle);
                }
                return this;
            }

            public Builder addRemoteInput(RemoteInput remoteInput) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList();
                }
                this.mRemoteInputs.add(remoteInput);
                return this;
            }

            /*
             * WARNING - void declaration
             */
            public Action build() {
                void var3_8;
                this.checkContextualActionNullFields();
                Object object = new ArrayList();
                ArrayList<RemoteInput> arrayList = new ArrayList<RemoteInput>();
                ArrayList<RemoteInput> object22 = this.mRemoteInputs;
                if (object22 != null) {
                    for (RemoteInput remoteInput : object22) {
                        if (remoteInput.isDataOnly()) {
                            object.add(remoteInput);
                            continue;
                        }
                        arrayList.add(remoteInput);
                    }
                }
                boolean bl = object.isEmpty();
                Object var3_6 = null;
                object = bl ? null : object.toArray(new RemoteInput[object.size()]);
                if (!arrayList.isEmpty()) {
                    RemoteInput[] remoteInputArray = arrayList.toArray(new RemoteInput[arrayList.size()]);
                }
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, (RemoteInput[])var3_8, (RemoteInput[])object, this.mAllowGeneratedReplies, this.mSemanticAction, this.mShowsUserInterface, this.mIsContextual);
            }

            public Builder extend(Extender extender) {
                extender.extend(this);
                return this;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Builder setAllowGeneratedReplies(boolean bl) {
                this.mAllowGeneratedReplies = bl;
                return this;
            }

            public Builder setContextual(boolean bl) {
                this.mIsContextual = bl;
                return this;
            }

            public Builder setSemanticAction(int n) {
                this.mSemanticAction = n;
                return this;
            }

            public Builder setShowsUserInterface(boolean bl) {
                this.mShowsUserInterface = bl;
                return this;
            }
        }

        public static interface Extender {
            public Builder extend(Builder var1);
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface SemanticAction {
        }

        public static final class WearableExtender
        implements Extender {
            private static final int DEFAULT_FLAGS = 1;
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
            private static final int FLAG_AVAILABLE_OFFLINE = 1;
            private static final int FLAG_HINT_DISPLAY_INLINE = 4;
            private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 2;
            private static final String KEY_CANCEL_LABEL = "cancelLabel";
            private static final String KEY_CONFIRM_LABEL = "confirmLabel";
            private static final String KEY_FLAGS = "flags";
            private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
            private CharSequence mCancelLabel;
            private CharSequence mConfirmLabel;
            private int mFlags = 1;
            private CharSequence mInProgressLabel;

            public WearableExtender() {
            }

            public WearableExtender(Action action) {
                action = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (action != null) {
                    this.mFlags = action.getInt(KEY_FLAGS, 1);
                    this.mInProgressLabel = action.getCharSequence(KEY_IN_PROGRESS_LABEL);
                    this.mConfirmLabel = action.getCharSequence(KEY_CONFIRM_LABEL);
                    this.mCancelLabel = action.getCharSequence(KEY_CANCEL_LABEL);
                }
            }

            private void setFlag(int n, boolean bl) {
                this.mFlags = bl ? (this.mFlags |= n) : (this.mFlags &= ~n);
            }

            public WearableExtender clone() {
                WearableExtender wearableExtender = new WearableExtender();
                wearableExtender.mFlags = this.mFlags;
                wearableExtender.mInProgressLabel = this.mInProgressLabel;
                wearableExtender.mConfirmLabel = this.mConfirmLabel;
                wearableExtender.mCancelLabel = this.mCancelLabel;
                return wearableExtender;
            }

            @Override
            public Builder extend(Builder builder) {
                CharSequence charSequence;
                Bundle bundle = new Bundle();
                int n = this.mFlags;
                if (n != 1) {
                    bundle.putInt(KEY_FLAGS, n);
                }
                if ((charSequence = this.mInProgressLabel) != null) {
                    bundle.putCharSequence(KEY_IN_PROGRESS_LABEL, charSequence);
                }
                if ((charSequence = this.mConfirmLabel) != null) {
                    bundle.putCharSequence(KEY_CONFIRM_LABEL, charSequence);
                }
                if ((charSequence = this.mCancelLabel) != null) {
                    bundle.putCharSequence(KEY_CANCEL_LABEL, charSequence);
                }
                builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, bundle);
                return builder;
            }

            @Deprecated
            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }

            @Deprecated
            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }

            public boolean getHintDisplayActionInline() {
                boolean bl = (this.mFlags & 4) != 0;
                return bl;
            }

            public boolean getHintLaunchesActivity() {
                boolean bl = (this.mFlags & 2) != 0;
                return bl;
            }

            @Deprecated
            public CharSequence getInProgressLabel() {
                return this.mInProgressLabel;
            }

            public boolean isAvailableOffline() {
                int n = this.mFlags;
                boolean bl = true;
                if ((n & 1) == 0) {
                    bl = false;
                }
                return bl;
            }

            public WearableExtender setAvailableOffline(boolean bl) {
                this.setFlag(1, bl);
                return this;
            }

            @Deprecated
            public WearableExtender setCancelLabel(CharSequence charSequence) {
                this.mCancelLabel = charSequence;
                return this;
            }

            @Deprecated
            public WearableExtender setConfirmLabel(CharSequence charSequence) {
                this.mConfirmLabel = charSequence;
                return this;
            }

            public WearableExtender setHintDisplayActionInline(boolean bl) {
                this.setFlag(4, bl);
                return this;
            }

            public WearableExtender setHintLaunchesActivity(boolean bl) {
                this.setFlag(2, bl);
                return this;
            }

            @Deprecated
            public WearableExtender setInProgressLabel(CharSequence charSequence) {
                this.mInProgressLabel = charSequence;
                return this;
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface BadgeIconType {
    }

    public static class BigPictureStyle
    extends Style {
        private Bitmap mBigLargeIcon;
        private boolean mBigLargeIconSet;
        private Bitmap mPicture;

        public BigPictureStyle() {
        }

        public BigPictureStyle(Builder builder) {
            this.setBuilder(builder);
        }

        @Override
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 16) {
                notificationBuilderWithBuilderAccessor = new Notification.BigPictureStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigPicture(this.mPicture);
                if (this.mBigLargeIconSet) {
                    notificationBuilderWithBuilderAccessor.bigLargeIcon(this.mBigLargeIcon);
                }
                if (this.mSummaryTextSet) {
                    notificationBuilderWithBuilderAccessor.setSummaryText(this.mSummaryText);
                }
            }
        }

        public BigPictureStyle bigLargeIcon(Bitmap bitmap) {
            this.mBigLargeIcon = bitmap;
            this.mBigLargeIconSet = true;
            return this;
        }

        public BigPictureStyle bigPicture(Bitmap bitmap) {
            this.mPicture = bitmap;
            return this;
        }

        public BigPictureStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = Builder.limitCharSequenceLength(charSequence);
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static class BigTextStyle
    extends Style {
        private CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(Builder builder) {
            this.setBuilder(builder);
        }

        @Override
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 16) {
                notificationBuilderWithBuilderAccessor = new Notification.BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigText(this.mBigText);
                if (this.mSummaryTextSet) {
                    notificationBuilderWithBuilderAccessor.setSummaryText(this.mSummaryText);
                }
            }
        }

        public BigTextStyle bigText(CharSequence charSequence) {
            this.mBigText = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public BigTextStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = Builder.limitCharSequenceLength(charSequence);
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static final class BubbleMetadata {
        private static final int FLAG_AUTO_EXPAND_BUBBLE = 1;
        private static final int FLAG_SUPPRESS_NOTIFICATION = 2;
        private PendingIntent mDeleteIntent;
        private int mDesiredHeight;
        private int mDesiredHeightResId;
        private int mFlags;
        private IconCompat mIcon;
        private PendingIntent mPendingIntent;

        private BubbleMetadata(PendingIntent pendingIntent, PendingIntent pendingIntent2, IconCompat iconCompat, int n, int n2, int n3) {
            this.mPendingIntent = pendingIntent;
            this.mIcon = iconCompat;
            this.mDesiredHeight = n;
            this.mDesiredHeightResId = n2;
            this.mDeleteIntent = pendingIntent2;
            this.mFlags = n3;
        }

        public static BubbleMetadata fromPlatform(Notification.BubbleMetadata bubbleMetadata) {
            if (bubbleMetadata == null) {
                return null;
            }
            Builder builder = new Builder().setAutoExpandBubble(bubbleMetadata.getAutoExpandBubble()).setDeleteIntent(bubbleMetadata.getDeleteIntent()).setIcon(IconCompat.createFromIcon(bubbleMetadata.getIcon())).setIntent(bubbleMetadata.getIntent()).setSuppressNotification(bubbleMetadata.isNotificationSuppressed());
            if (bubbleMetadata.getDesiredHeight() != 0) {
                builder.setDesiredHeight(bubbleMetadata.getDesiredHeight());
            }
            if (bubbleMetadata.getDesiredHeightResId() != 0) {
                builder.setDesiredHeightResId(bubbleMetadata.getDesiredHeightResId());
            }
            return builder.build();
        }

        public static Notification.BubbleMetadata toPlatform(BubbleMetadata bubbleMetadata) {
            if (bubbleMetadata == null) {
                return null;
            }
            Notification.BubbleMetadata.Builder builder = new Notification.BubbleMetadata.Builder().setAutoExpandBubble(bubbleMetadata.getAutoExpandBubble()).setDeleteIntent(bubbleMetadata.getDeleteIntent()).setIcon(bubbleMetadata.getIcon().toIcon()).setIntent(bubbleMetadata.getIntent()).setSuppressNotification(bubbleMetadata.isNotificationSuppressed());
            if (bubbleMetadata.getDesiredHeight() != 0) {
                builder.setDesiredHeight(bubbleMetadata.getDesiredHeight());
            }
            if (bubbleMetadata.getDesiredHeightResId() != 0) {
                builder.setDesiredHeightResId(bubbleMetadata.getDesiredHeightResId());
            }
            return builder.build();
        }

        public boolean getAutoExpandBubble() {
            int n = this.mFlags;
            boolean bl = true;
            if ((n & 1) == 0) {
                bl = false;
            }
            return bl;
        }

        public PendingIntent getDeleteIntent() {
            return this.mDeleteIntent;
        }

        public int getDesiredHeight() {
            return this.mDesiredHeight;
        }

        public int getDesiredHeightResId() {
            return this.mDesiredHeightResId;
        }

        public IconCompat getIcon() {
            return this.mIcon;
        }

        public PendingIntent getIntent() {
            return this.mPendingIntent;
        }

        public boolean isNotificationSuppressed() {
            boolean bl = (this.mFlags & 2) != 0;
            return bl;
        }

        public static final class Builder {
            private PendingIntent mDeleteIntent;
            private int mDesiredHeight;
            private int mDesiredHeightResId;
            private int mFlags;
            private IconCompat mIcon;
            private PendingIntent mPendingIntent;

            private Builder setFlag(int n, boolean bl) {
                this.mFlags = bl ? (this.mFlags |= n) : (this.mFlags &= ~n);
                return this;
            }

            public BubbleMetadata build() {
                PendingIntent pendingIntent = this.mPendingIntent;
                if (pendingIntent != null) {
                    IconCompat iconCompat = this.mIcon;
                    if (iconCompat != null) {
                        return new BubbleMetadata(pendingIntent, this.mDeleteIntent, iconCompat, this.mDesiredHeight, this.mDesiredHeightResId, this.mFlags);
                    }
                    throw new IllegalStateException("Must supply an icon for the bubble");
                }
                throw new IllegalStateException("Must supply pending intent to bubble");
            }

            public Builder setAutoExpandBubble(boolean bl) {
                this.setFlag(1, bl);
                return this;
            }

            public Builder setDeleteIntent(PendingIntent pendingIntent) {
                this.mDeleteIntent = pendingIntent;
                return this;
            }

            public Builder setDesiredHeight(int n) {
                this.mDesiredHeight = Math.max(n, 0);
                this.mDesiredHeightResId = 0;
                return this;
            }

            public Builder setDesiredHeightResId(int n) {
                this.mDesiredHeightResId = n;
                this.mDesiredHeight = 0;
                return this;
            }

            public Builder setIcon(IconCompat iconCompat) {
                if (iconCompat != null) {
                    if (iconCompat.getType() != 1) {
                        this.mIcon = iconCompat;
                        return this;
                    }
                    throw new IllegalArgumentException("When using bitmap based icons, Bubbles require TYPE_ADAPTIVE_BITMAP, please use IconCompat#createWithAdaptiveBitmap instead");
                }
                throw new IllegalArgumentException("Bubbles require non-null icon");
            }

            public Builder setIntent(PendingIntent pendingIntent) {
                if (pendingIntent != null) {
                    this.mPendingIntent = pendingIntent;
                    return this;
                }
                throw new IllegalArgumentException("Bubble requires non-null pending intent");
            }

            public Builder setSuppressNotification(boolean bl) {
                this.setFlag(2, bl);
                return this;
            }
        }
    }

    public static class Builder {
        private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
        public ArrayList<Action> mActions = new ArrayList();
        boolean mAllowSystemGeneratedContextualActions;
        int mBadgeIcon = 0;
        RemoteViews mBigContentView;
        BubbleMetadata mBubbleMetadata;
        String mCategory;
        String mChannelId;
        boolean mChronometerCountDown;
        int mColor = 0;
        boolean mColorized;
        boolean mColorizedSet;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        RemoteViews mContentView;
        public Context mContext;
        Bundle mExtras;
        PendingIntent mFullScreenIntent;
        int mGroupAlertBehavior = 0;
        String mGroupKey;
        boolean mGroupSummary;
        RemoteViews mHeadsUpContentView;
        ArrayList<Action> mInvisibleActions = new ArrayList();
        Bitmap mLargeIcon;
        boolean mLocalOnly = false;
        Notification mNotification;
        int mNumber;
        @Deprecated
        public ArrayList<String> mPeople;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        CharSequence[] mRemoteInputHistory;
        String mShortcutId;
        boolean mShowWhen = true;
        boolean mSilent;
        String mSortKey;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        long mTimeout;
        boolean mUseChronometer;
        int mVisibility = 0;

        @Deprecated
        public Builder(Context context) {
            this(context, null);
        }

        public Builder(Context context, String string2) {
            Notification notification;
            this.mNotification = notification = new Notification();
            this.mContext = context;
            this.mChannelId = string2;
            notification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
            this.mPeople = new ArrayList();
            this.mAllowSystemGeneratedContextualActions = true;
        }

        protected static CharSequence limitCharSequenceLength(CharSequence charSequence) {
            if (charSequence == null) {
                return charSequence;
            }
            CharSequence charSequence2 = charSequence;
            if (charSequence.length() > 5120) {
                charSequence2 = charSequence.subSequence(0, 5120);
            }
            return charSequence2;
        }

        private Bitmap reduceLargeIconSize(Bitmap bitmap) {
            if (bitmap != null && Build.VERSION.SDK_INT < 27) {
                Resources resources = this.mContext.getResources();
                int n = resources.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_width);
                int n2 = resources.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_height);
                if (bitmap.getWidth() <= n && bitmap.getHeight() <= n2) {
                    return bitmap;
                }
                double d = n;
                double d2 = Math.max(1, bitmap.getWidth());
                Double.isNaN(d);
                Double.isNaN(d2);
                double d3 = d / d2;
                d2 = n2;
                d = Math.max(1, bitmap.getHeight());
                Double.isNaN(d2);
                Double.isNaN(d);
                d2 = Math.min(d3, d2 / d);
                d = bitmap.getWidth();
                Double.isNaN(d);
                n = (int)Math.ceil(d * d2);
                d = bitmap.getHeight();
                Double.isNaN(d);
                return Bitmap.createScaledBitmap((Bitmap)bitmap, (int)n, (int)((int)Math.ceil(d * d2)), (boolean)true);
            }
            return bitmap;
        }

        private void setFlag(int n, boolean bl) {
            if (bl) {
                Notification notification = this.mNotification;
                notification.flags |= n;
            } else {
                Notification notification = this.mNotification;
                notification.flags &= ~n;
            }
        }

        public Builder addAction(int n, CharSequence charSequence, PendingIntent pendingIntent) {
            this.mActions.add(new Action(n, charSequence, pendingIntent));
            return this;
        }

        public Builder addAction(Action action) {
            this.mActions.add(action);
            return this;
        }

        public Builder addExtras(Bundle bundle) {
            if (bundle != null) {
                Bundle bundle2 = this.mExtras;
                if (bundle2 == null) {
                    this.mExtras = new Bundle(bundle);
                } else {
                    bundle2.putAll(bundle);
                }
            }
            return this;
        }

        public Builder addInvisibleAction(int n, CharSequence charSequence, PendingIntent pendingIntent) {
            return this.addInvisibleAction(new Action(n, charSequence, pendingIntent));
        }

        public Builder addInvisibleAction(Action action) {
            this.mInvisibleActions.add(action);
            return this;
        }

        public Builder addPerson(String string2) {
            this.mPeople.add(string2);
            return this;
        }

        public Notification build() {
            return new NotificationCompatBuilder(this).build();
        }

        public Builder extend(Extender extender) {
            extender.extend(this);
            return this;
        }

        public RemoteViews getBigContentView() {
            return this.mBigContentView;
        }

        public BubbleMetadata getBubbleMetadata() {
            return this.mBubbleMetadata;
        }

        public int getColor() {
            return this.mColor;
        }

        public RemoteViews getContentView() {
            return this.mContentView;
        }

        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }

        public RemoteViews getHeadsUpContentView() {
            return this.mHeadsUpContentView;
        }

        @Deprecated
        public Notification getNotification() {
            return this.build();
        }

        public int getPriority() {
            return this.mPriority;
        }

        public long getWhenIfShowing() {
            long l = this.mShowWhen ? this.mNotification.when : 0L;
            return l;
        }

        public Builder setAllowSystemGeneratedContextualActions(boolean bl) {
            this.mAllowSystemGeneratedContextualActions = bl;
            return this;
        }

        public Builder setAutoCancel(boolean bl) {
            this.setFlag(16, bl);
            return this;
        }

        public Builder setBadgeIconType(int n) {
            this.mBadgeIcon = n;
            return this;
        }

        public Builder setBubbleMetadata(BubbleMetadata bubbleMetadata) {
            this.mBubbleMetadata = bubbleMetadata;
            return this;
        }

        public Builder setCategory(String string2) {
            this.mCategory = string2;
            return this;
        }

        public Builder setChannelId(String string2) {
            this.mChannelId = string2;
            return this;
        }

        public Builder setChronometerCountDown(boolean bl) {
            this.mChronometerCountDown = bl;
            this.mExtras.putBoolean(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN, bl);
            return this;
        }

        public Builder setColor(int n) {
            this.mColor = n;
            return this;
        }

        public Builder setColorized(boolean bl) {
            this.mColorized = bl;
            this.mColorizedSet = true;
            return this;
        }

        public Builder setContent(RemoteViews remoteViews) {
            this.mNotification.contentView = remoteViews;
            return this;
        }

        public Builder setContentInfo(CharSequence charSequence) {
            this.mContentInfo = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public Builder setContentIntent(PendingIntent pendingIntent) {
            this.mContentIntent = pendingIntent;
            return this;
        }

        public Builder setContentText(CharSequence charSequence) {
            this.mContentText = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public Builder setContentTitle(CharSequence charSequence) {
            this.mContentTitle = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public Builder setCustomBigContentView(RemoteViews remoteViews) {
            this.mBigContentView = remoteViews;
            return this;
        }

        public Builder setCustomContentView(RemoteViews remoteViews) {
            this.mContentView = remoteViews;
            return this;
        }

        public Builder setCustomHeadsUpContentView(RemoteViews remoteViews) {
            this.mHeadsUpContentView = remoteViews;
            return this;
        }

        public Builder setDefaults(int n) {
            this.mNotification.defaults = n;
            if ((n & 4) != 0) {
                Notification notification = this.mNotification;
                notification.flags |= 1;
            }
            return this;
        }

        public Builder setDeleteIntent(PendingIntent pendingIntent) {
            this.mNotification.deleteIntent = pendingIntent;
            return this;
        }

        public Builder setExtras(Bundle bundle) {
            this.mExtras = bundle;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent pendingIntent, boolean bl) {
            this.mFullScreenIntent = pendingIntent;
            this.setFlag(128, bl);
            return this;
        }

        public Builder setGroup(String string2) {
            this.mGroupKey = string2;
            return this;
        }

        public Builder setGroupAlertBehavior(int n) {
            this.mGroupAlertBehavior = n;
            return this;
        }

        public Builder setGroupSummary(boolean bl) {
            this.mGroupSummary = bl;
            return this;
        }

        public Builder setLargeIcon(Bitmap bitmap) {
            this.mLargeIcon = this.reduceLargeIconSize(bitmap);
            return this;
        }

        public Builder setLights(int n, int n2, int n3) {
            this.mNotification.ledARGB = n;
            this.mNotification.ledOnMS = n2;
            this.mNotification.ledOffMS = n3;
            n = this.mNotification.ledOnMS;
            n2 = 1;
            n = n != 0 && this.mNotification.ledOffMS != 0 ? 1 : 0;
            Notification notification = this.mNotification;
            n3 = notification.flags;
            n = n != 0 ? n2 : 0;
            notification.flags = n | n3 & 0xFFFFFFFE;
            return this;
        }

        public Builder setLocalOnly(boolean bl) {
            this.mLocalOnly = bl;
            return this;
        }

        public Builder setNotificationSilent() {
            this.mSilent = true;
            return this;
        }

        public Builder setNumber(int n) {
            this.mNumber = n;
            return this;
        }

        public Builder setOngoing(boolean bl) {
            this.setFlag(2, bl);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean bl) {
            this.setFlag(8, bl);
            return this;
        }

        public Builder setPriority(int n) {
            this.mPriority = n;
            return this;
        }

        public Builder setProgress(int n, int n2, boolean bl) {
            this.mProgressMax = n;
            this.mProgress = n2;
            this.mProgressIndeterminate = bl;
            return this;
        }

        public Builder setPublicVersion(Notification notification) {
            this.mPublicVersion = notification;
            return this;
        }

        public Builder setRemoteInputHistory(CharSequence[] charSequenceArray) {
            this.mRemoteInputHistory = charSequenceArray;
            return this;
        }

        public Builder setShortcutId(String string2) {
            this.mShortcutId = string2;
            return this;
        }

        public Builder setShowWhen(boolean bl) {
            this.mShowWhen = bl;
            return this;
        }

        public Builder setSmallIcon(int n) {
            this.mNotification.icon = n;
            return this;
        }

        public Builder setSmallIcon(int n, int n2) {
            this.mNotification.icon = n;
            this.mNotification.iconLevel = n2;
            return this;
        }

        public Builder setSortKey(String string2) {
            this.mSortKey = string2;
            return this;
        }

        public Builder setSound(Uri uri) {
            this.mNotification.sound = uri;
            this.mNotification.audioStreamType = -1;
            if (Build.VERSION.SDK_INT >= 21) {
                this.mNotification.audioAttributes = new AudioAttributes.Builder().setContentType(4).setUsage(5).build();
            }
            return this;
        }

        public Builder setSound(Uri uri, int n) {
            this.mNotification.sound = uri;
            this.mNotification.audioStreamType = n;
            if (Build.VERSION.SDK_INT >= 21) {
                this.mNotification.audioAttributes = new AudioAttributes.Builder().setContentType(4).setLegacyStreamType(n).build();
            }
            return this;
        }

        public Builder setStyle(Style style2) {
            if (this.mStyle != style2) {
                this.mStyle = style2;
                if (style2 != null) {
                    style2.setBuilder(this);
                }
            }
            return this;
        }

        public Builder setSubText(CharSequence charSequence) {
            this.mSubText = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public Builder setTicker(CharSequence charSequence) {
            this.mNotification.tickerText = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public Builder setTicker(CharSequence charSequence, RemoteViews remoteViews) {
            this.mNotification.tickerText = Builder.limitCharSequenceLength(charSequence);
            this.mTickerView = remoteViews;
            return this;
        }

        public Builder setTimeoutAfter(long l) {
            this.mTimeout = l;
            return this;
        }

        public Builder setUsesChronometer(boolean bl) {
            this.mUseChronometer = bl;
            return this;
        }

        public Builder setVibrate(long[] lArray) {
            this.mNotification.vibrate = lArray;
            return this;
        }

        public Builder setVisibility(int n) {
            this.mVisibility = n;
            return this;
        }

        public Builder setWhen(long l) {
            this.mNotification.when = l;
            return this;
        }
    }

    public static final class CarExtender
    implements Extender {
        static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_COLOR = "app_color";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        static final String EXTRA_INVISIBLE_ACTIONS = "invisible_actions";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String KEY_AUTHOR = "author";
        private static final String KEY_MESSAGES = "messages";
        private static final String KEY_ON_READ = "on_read";
        private static final String KEY_ON_REPLY = "on_reply";
        private static final String KEY_PARTICIPANTS = "participants";
        private static final String KEY_REMOTE_INPUT = "remote_input";
        private static final String KEY_TEXT = "text";
        private static final String KEY_TIMESTAMP = "timestamp";
        private int mColor = 0;
        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;

        public CarExtender() {
        }

        public CarExtender(Notification object) {
            if (Build.VERSION.SDK_INT < 21) {
                return;
            }
            if ((object = NotificationCompat.getExtras(object) == null ? null : NotificationCompat.getExtras(object).getBundle(EXTRA_CAR_EXTENDER)) != null) {
                this.mLargeIcon = (Bitmap)object.getParcelable(EXTRA_LARGE_ICON);
                this.mColor = object.getInt(EXTRA_COLOR, 0);
                this.mUnreadConversation = CarExtender.getUnreadConversationFromBundle(object.getBundle(EXTRA_CONVERSATION));
            }
        }

        private static Bundle getBundleForUnreadConversation(UnreadConversation unreadConversation) {
            Bundle bundle = new Bundle();
            String string2 = null;
            Object object = string2;
            if (unreadConversation.getParticipants() != null) {
                object = string2;
                if (unreadConversation.getParticipants().length > 1) {
                    object = unreadConversation.getParticipants()[0];
                }
            }
            Parcelable[] parcelableArray = new Parcelable[unreadConversation.getMessages().length];
            for (int i = 0; i < parcelableArray.length; ++i) {
                string2 = new Bundle();
                string2.putString(KEY_TEXT, unreadConversation.getMessages()[i]);
                string2.putString(KEY_AUTHOR, (String)object);
                parcelableArray[i] = string2;
            }
            bundle.putParcelableArray(KEY_MESSAGES, parcelableArray);
            object = unreadConversation.getRemoteInput();
            if (object != null) {
                bundle.putParcelable(KEY_REMOTE_INPUT, (Parcelable)new RemoteInput.Builder(((RemoteInput)object).getResultKey()).setLabel(((RemoteInput)object).getLabel()).setChoices(((RemoteInput)object).getChoices()).setAllowFreeFormInput(((RemoteInput)object).getAllowFreeFormInput()).addExtras(((RemoteInput)object).getExtras()).build());
            }
            bundle.putParcelable(KEY_ON_REPLY, (Parcelable)unreadConversation.getReplyPendingIntent());
            bundle.putParcelable(KEY_ON_READ, (Parcelable)unreadConversation.getReadPendingIntent());
            bundle.putStringArray(KEY_PARTICIPANTS, unreadConversation.getParticipants());
            bundle.putLong(KEY_TIMESTAMP, unreadConversation.getLatestTimestamp());
            return bundle;
        }

        private static UnreadConversation getUnreadConversationFromBundle(Bundle bundle) {
            int n;
            if (bundle == null) {
                return null;
            }
            Object object = bundle.getParcelableArray(KEY_MESSAGES);
            String[] stringArray = null;
            if (object != null) {
                stringArray = new String[((Parcelable[])object).length];
                int n2 = 1;
                int n3 = 0;
                while (true) {
                    n = n2;
                    if (n3 >= stringArray.length) break;
                    if (!(object[n3] instanceof Bundle)) {
                        n = 0;
                        break;
                    }
                    stringArray[n3] = ((Bundle)object[n3]).getString(KEY_TEXT);
                    if (stringArray[n3] == null) {
                        n = 0;
                        break;
                    }
                    ++n3;
                }
                if (n == 0) {
                    return null;
                }
            }
            PendingIntent pendingIntent = (PendingIntent)bundle.getParcelable(KEY_ON_READ);
            PendingIntent pendingIntent2 = (PendingIntent)bundle.getParcelable(KEY_ON_REPLY);
            android.app.RemoteInput remoteInput = (android.app.RemoteInput)bundle.getParcelable(KEY_REMOTE_INPUT);
            String[] stringArray2 = bundle.getStringArray(KEY_PARTICIPANTS);
            if (stringArray2 != null && stringArray2.length == 1) {
                if (remoteInput != null) {
                    object = remoteInput.getResultKey();
                    CharSequence charSequence = remoteInput.getLabel();
                    CharSequence[] charSequenceArray = remoteInput.getChoices();
                    boolean bl = remoteInput.getAllowFreeFormInput();
                    n = Build.VERSION.SDK_INT >= 29 ? remoteInput.getEditChoicesBeforeSending() : 0;
                    object = new RemoteInput((String)object, charSequence, charSequenceArray, bl, n, remoteInput.getExtras(), null);
                } else {
                    object = null;
                }
                return new UnreadConversation(stringArray, (RemoteInput)object, pendingIntent2, pendingIntent, stringArray2, bundle.getLong(KEY_TIMESTAMP));
            }
            return null;
        }

        @Override
        public Builder extend(Builder builder) {
            int n;
            if (Build.VERSION.SDK_INT < 21) {
                return builder;
            }
            Bundle bundle = new Bundle();
            Object object = this.mLargeIcon;
            if (object != null) {
                bundle.putParcelable(EXTRA_LARGE_ICON, (Parcelable)object);
            }
            if ((n = this.mColor) != 0) {
                bundle.putInt(EXTRA_COLOR, n);
            }
            if ((object = this.mUnreadConversation) != null) {
                bundle.putBundle(EXTRA_CONVERSATION, CarExtender.getBundleForUnreadConversation((UnreadConversation)object));
            }
            builder.getExtras().putBundle(EXTRA_CAR_EXTENDER, bundle);
            return builder;
        }

        public int getColor() {
            return this.mColor;
        }

        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }

        @Deprecated
        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }

        public CarExtender setColor(int n) {
            this.mColor = n;
            return this;
        }

        public CarExtender setLargeIcon(Bitmap bitmap) {
            this.mLargeIcon = bitmap;
            return this;
        }

        @Deprecated
        public CarExtender setUnreadConversation(UnreadConversation unreadConversation) {
            this.mUnreadConversation = unreadConversation;
            return this;
        }

        @Deprecated
        public static class UnreadConversation {
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;

            UnreadConversation(String[] stringArray, RemoteInput remoteInput, PendingIntent pendingIntent, PendingIntent pendingIntent2, String[] stringArray2, long l) {
                this.mMessages = stringArray;
                this.mRemoteInput = remoteInput;
                this.mReadPendingIntent = pendingIntent2;
                this.mReplyPendingIntent = pendingIntent;
                this.mParticipants = stringArray2;
                this.mLatestTimestamp = l;
            }

            public long getLatestTimestamp() {
                return this.mLatestTimestamp;
            }

            public String[] getMessages() {
                return this.mMessages;
            }

            public String getParticipant() {
                Object object = this.mParticipants;
                object = ((String[])object).length > 0 ? object[0] : null;
                return object;
            }

            public String[] getParticipants() {
                return this.mParticipants;
            }

            public PendingIntent getReadPendingIntent() {
                return this.mReadPendingIntent;
            }

            public RemoteInput getRemoteInput() {
                return this.mRemoteInput;
            }

            public PendingIntent getReplyPendingIntent() {
                return this.mReplyPendingIntent;
            }

            public static class Builder {
                private long mLatestTimestamp;
                private final List<String> mMessages = new ArrayList<String>();
                private final String mParticipant;
                private PendingIntent mReadPendingIntent;
                private RemoteInput mRemoteInput;
                private PendingIntent mReplyPendingIntent;

                public Builder(String string2) {
                    this.mParticipant = string2;
                }

                public Builder addMessage(String string2) {
                    this.mMessages.add(string2);
                    return this;
                }

                public UnreadConversation build() {
                    Object object = this.mMessages;
                    String[] stringArray = object.toArray(new String[object.size()]);
                    object = this.mParticipant;
                    RemoteInput remoteInput = this.mRemoteInput;
                    PendingIntent pendingIntent = this.mReplyPendingIntent;
                    PendingIntent pendingIntent2 = this.mReadPendingIntent;
                    long l = this.mLatestTimestamp;
                    return new UnreadConversation(stringArray, remoteInput, pendingIntent, pendingIntent2, new String[]{object}, l);
                }

                public Builder setLatestTimestamp(long l) {
                    this.mLatestTimestamp = l;
                    return this;
                }

                public Builder setReadPendingIntent(PendingIntent pendingIntent) {
                    this.mReadPendingIntent = pendingIntent;
                    return this;
                }

                public Builder setReplyAction(PendingIntent pendingIntent, RemoteInput remoteInput) {
                    this.mRemoteInput = remoteInput;
                    this.mReplyPendingIntent = pendingIntent;
                    return this;
                }
            }
        }
    }

    public static class DecoratedCustomViewStyle
    extends Style {
        private static final int MAX_ACTION_BUTTONS = 3;

        private RemoteViews createRemoteViews(RemoteViews remoteViews, boolean bl) {
            int n = R.layout.notification_template_custom_big;
            int n2 = 0;
            RemoteViews remoteViews2 = this.applyStandardTemplate(true, n, false);
            remoteViews2.removeAllViews(R.id.actions);
            int n3 = 0;
            List<Action> list = DecoratedCustomViewStyle.getNonContextualActions(this.mBuilder.mActions);
            n = n3;
            if (bl) {
                n = n3;
                if (list != null) {
                    int n4 = Math.min(list.size(), 3);
                    n = n3;
                    if (n4 > 0) {
                        int n5 = 1;
                        n3 = 0;
                        while (true) {
                            n = n5;
                            if (n3 >= n4) break;
                            RemoteViews remoteViews3 = this.generateActionButton(list.get(n3));
                            remoteViews2.addView(R.id.actions, remoteViews3);
                            ++n3;
                        }
                    }
                }
            }
            n = n != 0 ? n2 : 8;
            remoteViews2.setViewVisibility(R.id.actions, n);
            remoteViews2.setViewVisibility(R.id.action_divider, n);
            this.buildIntoRemoteViews(remoteViews2, remoteViews);
            return remoteViews2;
        }

        private RemoteViews generateActionButton(Action action) {
            boolean bl = action.actionIntent == null;
            String string2 = this.mBuilder.mContext.getPackageName();
            int n = bl ? R.layout.notification_action_tombstone : R.layout.notification_action;
            string2 = new RemoteViews(string2, n);
            string2.setImageViewBitmap(R.id.action_image, this.createColoredBitmap(action.getIconCompat(), this.mBuilder.mContext.getResources().getColor(R.color.notification_action_color_filter)));
            string2.setTextViewText(R.id.action_text, action.title);
            if (!bl) {
                string2.setOnClickPendingIntent(R.id.action_container, action.actionIntent);
            }
            if (Build.VERSION.SDK_INT >= 15) {
                string2.setContentDescription(R.id.action_container, action.title);
            }
            return string2;
        }

        private static List<Action> getNonContextualActions(List<Action> object) {
            if (object == null) {
                return null;
            }
            ArrayList<Action> arrayList = new ArrayList<Action>();
            object = object.iterator();
            while (object.hasNext()) {
                Action action = (Action)object.next();
                if (action.isContextual()) continue;
                arrayList.add(action);
            }
            return arrayList;
        }

        @Override
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 24) {
                notificationBuilderWithBuilderAccessor.getBuilder().setStyle((Notification.Style)new Notification.DecoratedCustomViewStyle());
            }
        }

        @Override
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            notificationBuilderWithBuilderAccessor = this.mBuilder.getBigContentView();
            if (notificationBuilderWithBuilderAccessor == null) {
                notificationBuilderWithBuilderAccessor = this.mBuilder.getContentView();
            }
            if (notificationBuilderWithBuilderAccessor == null) {
                return null;
            }
            return this.createRemoteViews((RemoteViews)notificationBuilderWithBuilderAccessor, true);
        }

        @Override
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            if (this.mBuilder.getContentView() == null) {
                return null;
            }
            return this.createRemoteViews(this.mBuilder.getContentView(), false);
        }

        @Override
        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            RemoteViews remoteViews = this.mBuilder.getHeadsUpContentView();
            notificationBuilderWithBuilderAccessor = remoteViews != null ? remoteViews : this.mBuilder.getContentView();
            if (remoteViews == null) {
                return null;
            }
            return this.createRemoteViews((RemoteViews)notificationBuilderWithBuilderAccessor, true);
        }
    }

    public static interface Extender {
        public Builder extend(Builder var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface GroupAlertBehavior {
    }

    public static class InboxStyle
    extends Style {
        private ArrayList<CharSequence> mTexts = new ArrayList();

        public InboxStyle() {
        }

        public InboxStyle(Builder builder) {
            this.setBuilder(builder);
        }

        public InboxStyle addLine(CharSequence charSequence) {
            this.mTexts.add(Builder.limitCharSequenceLength(charSequence));
            return this;
        }

        @Override
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 16) {
                notificationBuilderWithBuilderAccessor = new Notification.InboxStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(this.mBigContentTitle);
                if (this.mSummaryTextSet) {
                    notificationBuilderWithBuilderAccessor.setSummaryText(this.mSummaryText);
                }
                Iterator<CharSequence> iterator2 = this.mTexts.iterator();
                while (iterator2.hasNext()) {
                    notificationBuilderWithBuilderAccessor.addLine(iterator2.next());
                }
            }
        }

        public InboxStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(charSequence);
            return this;
        }

        public InboxStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = Builder.limitCharSequenceLength(charSequence);
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static class MessagingStyle
    extends Style {
        public static final int MAXIMUM_RETAINED_MESSAGES = 25;
        private CharSequence mConversationTitle;
        private Boolean mIsGroupConversation;
        private final List<Message> mMessages = new ArrayList<Message>();
        private Person mUser;

        private MessagingStyle() {
        }

        public MessagingStyle(Person person) {
            if (!TextUtils.isEmpty((CharSequence)person.getName())) {
                this.mUser = person;
                return;
            }
            throw new IllegalArgumentException("User's name must not be empty.");
        }

        @Deprecated
        public MessagingStyle(CharSequence charSequence) {
            this.mUser = new Person.Builder().setName(charSequence).build();
        }

        public static MessagingStyle extractMessagingStyleFromNotification(Notification notification) {
            if ((notification = NotificationCompat.getExtras(notification)) != null && !notification.containsKey(NotificationCompat.EXTRA_SELF_DISPLAY_NAME) && !notification.containsKey(NotificationCompat.EXTRA_MESSAGING_STYLE_USER)) {
                return null;
            }
            try {
                MessagingStyle messagingStyle = new MessagingStyle();
                messagingStyle.restoreFromCompatExtras((Bundle)notification);
                return messagingStyle;
            }
            catch (ClassCastException classCastException) {
                return null;
            }
        }

        private Message findLatestIncomingMessage() {
            Object object;
            for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                object = this.mMessages.get(i);
                if (((Message)object).getPerson() == null || TextUtils.isEmpty((CharSequence)((Message)object).getPerson().getName())) continue;
                return object;
            }
            if (!this.mMessages.isEmpty()) {
                object = this.mMessages;
                return (Message)object.get(object.size() - 1);
            }
            return null;
        }

        private boolean hasMessagesWithoutSender() {
            for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                Message message = this.mMessages.get(i);
                if (message.getPerson() == null || message.getPerson().getName() != null) continue;
                return true;
            }
            return false;
        }

        private TextAppearanceSpan makeFontColorSpan(int n) {
            return new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf((int)n), null);
        }

        private CharSequence makeMessageLine(Message object) {
            BidiFormatter bidiFormatter = BidiFormatter.getInstance();
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            boolean bl = Build.VERSION.SDK_INT >= 21;
            int n = bl ? -16777216 : -1;
            Object object2 = ((Message)object).getPerson();
            String string2 = "";
            object2 = object2 == null ? "" : ((Message)object).getPerson().getName();
            int n2 = n;
            Object object3 = object2;
            if (TextUtils.isEmpty((CharSequence)object2)) {
                object3 = this.mUser.getName();
                if (bl && this.mBuilder.getColor() != 0) {
                    n = this.mBuilder.getColor();
                }
                n2 = n;
            }
            object2 = bidiFormatter.unicodeWrap((CharSequence)object3);
            spannableStringBuilder.append((CharSequence)object2);
            spannableStringBuilder.setSpan((Object)this.makeFontColorSpan(n2), spannableStringBuilder.length() - object2.length(), spannableStringBuilder.length(), 33);
            object = ((Message)object).getText() == null ? string2 : ((Message)object).getText();
            spannableStringBuilder.append((CharSequence)"  ").append(bidiFormatter.unicodeWrap((CharSequence)object));
            return spannableStringBuilder;
        }

        @Override
        public void addCompatExtras(Bundle bundle) {
            Boolean bl;
            super.addCompatExtras(bundle);
            bundle.putCharSequence(NotificationCompat.EXTRA_SELF_DISPLAY_NAME, this.mUser.getName());
            bundle.putBundle(NotificationCompat.EXTRA_MESSAGING_STYLE_USER, this.mUser.toBundle());
            bundle.putCharSequence(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE, this.mConversationTitle);
            if (this.mConversationTitle != null && this.mIsGroupConversation.booleanValue()) {
                bundle.putCharSequence(NotificationCompat.EXTRA_CONVERSATION_TITLE, this.mConversationTitle);
            }
            if (!this.mMessages.isEmpty()) {
                bundle.putParcelableArray(NotificationCompat.EXTRA_MESSAGES, (Parcelable[])Message.getBundleArrayForMessages(this.mMessages));
            }
            if ((bl = this.mIsGroupConversation) != null) {
                bundle.putBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION, bl.booleanValue());
            }
        }

        public MessagingStyle addMessage(Message message) {
            this.mMessages.add(message);
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }

        public MessagingStyle addMessage(CharSequence charSequence, long l, Person person) {
            this.addMessage(new Message(charSequence, l, person));
            return this;
        }

        @Deprecated
        public MessagingStyle addMessage(CharSequence charSequence, long l, CharSequence charSequence2) {
            this.mMessages.add(new Message(charSequence, l, new Person.Builder().setName(charSequence2).build()));
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }

        @Override
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            this.setGroupConversation(this.isGroupConversation());
            if (Build.VERSION.SDK_INT >= 24) {
                Notification.MessagingStyle messagingStyle = Build.VERSION.SDK_INT >= 28 ? new Notification.MessagingStyle(this.mUser.toAndroidPerson()) : new Notification.MessagingStyle(this.mUser.getName());
                if (this.mIsGroupConversation.booleanValue() || Build.VERSION.SDK_INT >= 28) {
                    messagingStyle.setConversationTitle(this.mConversationTitle);
                }
                if (Build.VERSION.SDK_INT >= 28) {
                    messagingStyle.setGroupConversation(this.mIsGroupConversation.booleanValue());
                }
                for (Message message : this.mMessages) {
                    Object object;
                    if (Build.VERSION.SDK_INT >= 28) {
                        object = message.getPerson();
                        CharSequence charSequence = message.getText();
                        long l = message.getTimestamp();
                        object = object == null ? null : ((Person)object).toAndroidPerson();
                        object = new Notification.MessagingStyle.Message(charSequence, l, (android.app.Person)object);
                    } else {
                        object = null;
                        if (message.getPerson() != null) {
                            object = message.getPerson().getName();
                        }
                        object = new Notification.MessagingStyle.Message(message.getText(), message.getTimestamp(), (CharSequence)object);
                    }
                    if (message.getDataMimeType() != null) {
                        object.setData(message.getDataMimeType(), message.getDataUri());
                    }
                    messagingStyle.addMessage((Notification.MessagingStyle.Message)object);
                }
                messagingStyle.setBuilder(notificationBuilderWithBuilderAccessor.getBuilder());
            } else {
                Notification.Builder builder;
                Object object = this.findLatestIncomingMessage();
                if (this.mConversationTitle != null && this.mIsGroupConversation.booleanValue()) {
                    notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle(this.mConversationTitle);
                } else if (object != null) {
                    notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle((CharSequence)"");
                    if (((Message)object).getPerson() != null) {
                        notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle(((Message)object).getPerson().getName());
                    }
                }
                if (object != null) {
                    builder = notificationBuilderWithBuilderAccessor.getBuilder();
                    object = this.mConversationTitle != null ? this.makeMessageLine((Message)object) : ((Message)object).getText();
                    builder.setContentText((CharSequence)object);
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    builder = new SpannableStringBuilder();
                    boolean bl = this.mConversationTitle != null || this.hasMessagesWithoutSender();
                    for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                        object = this.mMessages.get(i);
                        object = bl ? this.makeMessageLine((Message)object) : ((Message)object).getText();
                        if (i != this.mMessages.size() - 1) {
                            builder.insert(0, (CharSequence)"\n");
                        }
                        builder.insert(0, (CharSequence)object);
                    }
                    new Notification.BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(null).bigText((CharSequence)builder);
                }
            }
        }

        public CharSequence getConversationTitle() {
            return this.mConversationTitle;
        }

        public List<Message> getMessages() {
            return this.mMessages;
        }

        public Person getUser() {
            return this.mUser;
        }

        @Deprecated
        public CharSequence getUserDisplayName() {
            return this.mUser.getName();
        }

        public boolean isGroupConversation() {
            Object object = this.mBuilder;
            boolean bl = false;
            boolean bl2 = false;
            if (object != null && this.mBuilder.mContext.getApplicationInfo().targetSdkVersion < 28 && this.mIsGroupConversation == null) {
                if (this.mConversationTitle != null) {
                    bl2 = true;
                }
                return bl2;
            }
            object = this.mIsGroupConversation;
            bl2 = bl;
            if (object != null) {
                bl2 = (Boolean)object;
            }
            return bl2;
        }

        @Override
        protected void restoreFromCompatExtras(Bundle bundle) {
            this.mMessages.clear();
            this.mUser = bundle.containsKey(NotificationCompat.EXTRA_MESSAGING_STYLE_USER) ? Person.fromBundle(bundle.getBundle(NotificationCompat.EXTRA_MESSAGING_STYLE_USER)) : new Person.Builder().setName(bundle.getString(NotificationCompat.EXTRA_SELF_DISPLAY_NAME)).build();
            Parcelable[] parcelableArray = bundle.getCharSequence(NotificationCompat.EXTRA_CONVERSATION_TITLE);
            this.mConversationTitle = parcelableArray;
            if (parcelableArray == null) {
                this.mConversationTitle = bundle.getCharSequence(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE);
            }
            if ((parcelableArray = bundle.getParcelableArray(NotificationCompat.EXTRA_MESSAGES)) != null) {
                this.mMessages.addAll(Message.getMessagesFromBundleArray(parcelableArray));
            }
            if (bundle.containsKey(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION)) {
                this.mIsGroupConversation = bundle.getBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION);
            }
        }

        public MessagingStyle setConversationTitle(CharSequence charSequence) {
            this.mConversationTitle = charSequence;
            return this;
        }

        public MessagingStyle setGroupConversation(boolean bl) {
            this.mIsGroupConversation = bl;
            return this;
        }

        public static final class Message {
            static final String KEY_DATA_MIME_TYPE = "type";
            static final String KEY_DATA_URI = "uri";
            static final String KEY_EXTRAS_BUNDLE = "extras";
            static final String KEY_NOTIFICATION_PERSON = "sender_person";
            static final String KEY_PERSON = "person";
            static final String KEY_SENDER = "sender";
            static final String KEY_TEXT = "text";
            static final String KEY_TIMESTAMP = "time";
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras = new Bundle();
            private final Person mPerson;
            private final CharSequence mText;
            private final long mTimestamp;

            public Message(CharSequence charSequence, long l, Person person) {
                this.mText = charSequence;
                this.mTimestamp = l;
                this.mPerson = person;
            }

            @Deprecated
            public Message(CharSequence charSequence, long l, CharSequence charSequence2) {
                this(charSequence, l, new Person.Builder().setName(charSequence2).build());
            }

            static Bundle[] getBundleArrayForMessages(List<Message> list) {
                Bundle[] bundleArray = new Bundle[list.size()];
                int n = list.size();
                for (int i = 0; i < n; ++i) {
                    bundleArray[i] = list.get(i).toBundle();
                }
                return bundleArray;
            }

            static Message getMessageFromBundle(Bundle bundle) {
                block10: {
                    Object object;
                    try {
                        if (!bundle.containsKey(KEY_TEXT) || !bundle.containsKey(KEY_TIMESTAMP)) break block10;
                        object = null;
                    }
                    catch (ClassCastException classCastException) {
                        return null;
                    }
                    if (bundle.containsKey(KEY_PERSON)) {
                        object = Person.fromBundle(bundle.getBundle(KEY_PERSON));
                    } else if (bundle.containsKey(KEY_NOTIFICATION_PERSON) && Build.VERSION.SDK_INT >= 28) {
                        object = Person.fromAndroidPerson((android.app.Person)bundle.getParcelable(KEY_NOTIFICATION_PERSON));
                    } else if (bundle.containsKey(KEY_SENDER)) {
                        object = new Person.Builder();
                        object = ((Person.Builder)object).setName(bundle.getCharSequence(KEY_SENDER)).build();
                    }
                    Message message = new Message(bundle.getCharSequence(KEY_TEXT), bundle.getLong(KEY_TIMESTAMP), (Person)object);
                    if (bundle.containsKey(KEY_DATA_MIME_TYPE) && bundle.containsKey(KEY_DATA_URI)) {
                        message.setData(bundle.getString(KEY_DATA_MIME_TYPE), (Uri)bundle.getParcelable(KEY_DATA_URI));
                    }
                    if (bundle.containsKey(KEY_EXTRAS_BUNDLE)) {
                        message.getExtras().putAll(bundle.getBundle(KEY_EXTRAS_BUNDLE));
                    }
                    return message;
                }
                return null;
            }

            static List<Message> getMessagesFromBundleArray(Parcelable[] parcelableArray) {
                ArrayList<Message> arrayList = new ArrayList<Message>(parcelableArray.length);
                for (int i = 0; i < parcelableArray.length; ++i) {
                    Message message;
                    if (!(parcelableArray[i] instanceof Bundle) || (message = Message.getMessageFromBundle((Bundle)parcelableArray[i])) == null) continue;
                    arrayList.add(message);
                }
                return arrayList;
            }

            private Bundle toBundle() {
                Bundle bundle = new Bundle();
                Object object = this.mText;
                if (object != null) {
                    bundle.putCharSequence(KEY_TEXT, (CharSequence)object);
                }
                bundle.putLong(KEY_TIMESTAMP, this.mTimestamp);
                object = this.mPerson;
                if (object != null) {
                    bundle.putCharSequence(KEY_SENDER, ((Person)object).getName());
                    if (Build.VERSION.SDK_INT >= 28) {
                        bundle.putParcelable(KEY_NOTIFICATION_PERSON, (Parcelable)this.mPerson.toAndroidPerson());
                    } else {
                        bundle.putBundle(KEY_PERSON, this.mPerson.toBundle());
                    }
                }
                if ((object = this.mDataMimeType) != null) {
                    bundle.putString(KEY_DATA_MIME_TYPE, (String)object);
                }
                if ((object = this.mDataUri) != null) {
                    bundle.putParcelable(KEY_DATA_URI, (Parcelable)object);
                }
                if ((object = this.mExtras) != null) {
                    bundle.putBundle(KEY_EXTRAS_BUNDLE, (Bundle)object);
                }
                return bundle;
            }

            public String getDataMimeType() {
                return this.mDataMimeType;
            }

            public Uri getDataUri() {
                return this.mDataUri;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Person getPerson() {
                return this.mPerson;
            }

            @Deprecated
            public CharSequence getSender() {
                Object object = this.mPerson;
                object = object == null ? null : ((Person)object).getName();
                return object;
            }

            public CharSequence getText() {
                return this.mText;
            }

            public long getTimestamp() {
                return this.mTimestamp;
            }

            public Message setData(String string2, Uri uri) {
                this.mDataMimeType = string2;
                this.mDataUri = uri;
                return this;
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface NotificationVisibility {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface StreamType {
    }

    public static abstract class Style {
        CharSequence mBigContentTitle;
        protected Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet = false;

        private int calculateTopPadding() {
            Resources resources = this.mBuilder.mContext.getResources();
            int n = resources.getDimensionPixelSize(R.dimen.notification_top_pad);
            int n2 = resources.getDimensionPixelSize(R.dimen.notification_top_pad_large_text);
            float f = (Style.constrain(resources.getConfiguration().fontScale, 1.0f, 1.3f) - 1.0f) / 0.29999995f;
            return Math.round((1.0f - f) * (float)n + (float)n2 * f);
        }

        private static float constrain(float f, float f2, float f3) {
            block1: {
                block0: {
                    if (!(f < f2)) break block0;
                    f = f2;
                    break block1;
                }
                if (!(f > f3)) break block1;
                f = f3;
            }
            return f;
        }

        private Bitmap createColoredBitmap(int n, int n2, int n3) {
            return this.createColoredBitmap(IconCompat.createWithResource(this.mBuilder.mContext, n), n2, n3);
        }

        private Bitmap createColoredBitmap(IconCompat iconCompat, int n, int n2) {
            iconCompat = iconCompat.loadDrawable(this.mBuilder.mContext);
            int n3 = n2 == 0 ? iconCompat.getIntrinsicWidth() : n2;
            if (n2 == 0) {
                n2 = iconCompat.getIntrinsicHeight();
            }
            Bitmap bitmap = Bitmap.createBitmap((int)n3, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
            iconCompat.setBounds(0, 0, n3, n2);
            if (n != 0) {
                iconCompat.mutate().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff.Mode.SRC_IN));
            }
            iconCompat.draw(new Canvas(bitmap));
            return bitmap;
        }

        private Bitmap createIconWithBackground(int n, int n2, int n3, int n4) {
            int n5 = R.drawable.notification_icon_background;
            if (n4 == 0) {
                n4 = 0;
            }
            Bitmap bitmap = this.createColoredBitmap(n5, n4, n2);
            Canvas canvas = new Canvas(bitmap);
            Drawable drawable2 = this.mBuilder.mContext.getResources().getDrawable(n).mutate();
            drawable2.setFilterBitmap(true);
            n = (n2 - n3) / 2;
            drawable2.setBounds(n, n, n3 + n, n3 + n);
            drawable2.setColorFilter((ColorFilter)new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
            drawable2.draw(canvas);
            return bitmap;
        }

        private void hideNormalContent(RemoteViews remoteViews) {
            remoteViews.setViewVisibility(R.id.title, 8);
            remoteViews.setViewVisibility(R.id.text2, 8);
            remoteViews.setViewVisibility(R.id.text, 8);
        }

        public void addCompatExtras(Bundle bundle) {
        }

        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
        }

        /*
         * Unable to fully structure code
         */
        public RemoteViews applyStandardTemplate(boolean var1_1, int var2_2, boolean var3_3) {
            var9_4 = this.mBuilder.mContext.getResources();
            var10_5 = new RemoteViews(this.mBuilder.mContext.getPackageName(), var2_2);
            var5_6 = false;
            var2_2 = this.mBuilder.getPriority();
            var7_7 = 0;
            var2_2 = var2_2 < -1 ? 1 : 0;
            if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21) {
                if (var2_2 != 0) {
                    var10_5.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg_low);
                    var10_5.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_low_bg);
                } else {
                    var10_5.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg);
                    var10_5.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_bg);
                }
            }
            if (this.mBuilder.mLargeIcon != null) {
                if (Build.VERSION.SDK_INT >= 16) {
                    var10_5.setViewVisibility(R.id.icon, 0);
                    var10_5.setImageViewBitmap(R.id.icon, this.mBuilder.mLargeIcon);
                } else {
                    var10_5.setViewVisibility(R.id.icon, 8);
                }
                if (var1_1 && this.mBuilder.mNotification.icon != 0) {
                    var2_2 = var9_4.getDimensionPixelSize(R.dimen.notification_right_icon_size);
                    var6_8 = var9_4.getDimensionPixelSize(R.dimen.notification_small_icon_background_padding);
                    if (Build.VERSION.SDK_INT >= 21) {
                        var11_9 = this.createIconWithBackground(this.mBuilder.mNotification.icon, var2_2, var2_2 - var6_8 * 2, this.mBuilder.getColor());
                        var10_5.setImageViewBitmap(R.id.right_icon, (Bitmap)var11_9);
                    } else {
                        var10_5.setImageViewBitmap(R.id.right_icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
                    }
                    var10_5.setViewVisibility(R.id.right_icon, 0);
                }
            } else if (var1_1 && this.mBuilder.mNotification.icon != 0) {
                var10_5.setViewVisibility(R.id.icon, 0);
                if (Build.VERSION.SDK_INT >= 21) {
                    var2_2 = var9_4.getDimensionPixelSize(R.dimen.notification_large_icon_width);
                    var8_10 = var9_4.getDimensionPixelSize(R.dimen.notification_big_circle_margin);
                    var6_8 = var9_4.getDimensionPixelSize(R.dimen.notification_small_icon_size_as_large);
                    var11_9 = this.createIconWithBackground(this.mBuilder.mNotification.icon, var2_2 - var8_10, var6_8, this.mBuilder.getColor());
                    var10_5.setImageViewBitmap(R.id.icon, (Bitmap)var11_9);
                } else {
                    var10_5.setImageViewBitmap(R.id.icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
                }
            }
            if (this.mBuilder.mContentTitle != null) {
                var10_5.setTextViewText(R.id.title, this.mBuilder.mContentTitle);
            }
            if (this.mBuilder.mContentText != null) {
                var10_5.setTextViewText(R.id.text, this.mBuilder.mContentText);
                var5_6 = true;
            }
            var2_2 = Build.VERSION.SDK_INT < 21 && this.mBuilder.mLargeIcon != null ? 1 : 0;
            if (this.mBuilder.mContentInfo != null) {
                var10_5.setTextViewText(R.id.info, this.mBuilder.mContentInfo);
                var10_5.setViewVisibility(R.id.info, 0);
                var5_6 = true;
                var2_2 = 1;
            } else if (this.mBuilder.mNumber > 0) {
                var2_2 = var9_4.getInteger(R.integer.status_bar_notification_info_maxnum);
                if (this.mBuilder.mNumber > var2_2) {
                    var10_5.setTextViewText(R.id.info, (CharSequence)var9_4.getString(R.string.status_bar_notification_info_overflow));
                } else {
                    var11_9 = NumberFormat.getIntegerInstance();
                    var10_5.setTextViewText(R.id.info, (CharSequence)var11_9.format(this.mBuilder.mNumber));
                }
                var10_5.setViewVisibility(R.id.info, 0);
                var5_6 = true;
                var2_2 = 1;
            } else {
                var10_5.setViewVisibility(R.id.info, 8);
            }
            if (this.mBuilder.mSubText == null || Build.VERSION.SDK_INT < 16) ** GOTO lbl72
            var10_5.setTextViewText(R.id.text, this.mBuilder.mSubText);
            if (this.mBuilder.mContentText != null) {
                var10_5.setTextViewText(R.id.text2, this.mBuilder.mContentText);
                var10_5.setViewVisibility(R.id.text2, 0);
                var6_8 = 1;
            } else {
                var10_5.setViewVisibility(R.id.text2, 8);
lbl72:
                // 2 sources

                var6_8 = 0;
            }
            if (var6_8 != 0 && Build.VERSION.SDK_INT >= 16) {
                if (var3_3) {
                    var4_11 = var9_4.getDimensionPixelSize(R.dimen.notification_subtext_size);
                    var10_5.setTextViewTextSize(R.id.text, 0, var4_11);
                }
                var10_5.setViewPadding(R.id.line1, 0, 0, 0, 0);
            }
            if (this.mBuilder.getWhenIfShowing() != 0L) {
                if (this.mBuilder.mUseChronometer && Build.VERSION.SDK_INT >= 16) {
                    var10_5.setViewVisibility(R.id.chronometer, 0);
                    var10_5.setLong(R.id.chronometer, "setBase", this.mBuilder.getWhenIfShowing() + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
                    var10_5.setBoolean(R.id.chronometer, "setStarted", true);
                    if (this.mBuilder.mChronometerCountDown && Build.VERSION.SDK_INT >= 24) {
                        var10_5.setChronometerCountDown(R.id.chronometer, this.mBuilder.mChronometerCountDown);
                    }
                } else {
                    var10_5.setViewVisibility(R.id.time, 0);
                    var10_5.setLong(R.id.time, "setTime", this.mBuilder.getWhenIfShowing());
                }
                var2_2 = 1;
            }
            var6_8 = R.id.right_side;
            var2_2 = var2_2 != 0 ? 0 : 8;
            var10_5.setViewVisibility(var6_8, var2_2);
            var6_8 = R.id.line3;
            var2_2 = var5_6 != false ? var7_7 : 8;
            var10_5.setViewVisibility(var6_8, var2_2);
            return var10_5;
        }

        public Notification build() {
            Notification notification = null;
            Builder builder = this.mBuilder;
            if (builder != null) {
                notification = builder.build();
            }
            return notification;
        }

        public void buildIntoRemoteViews(RemoteViews remoteViews, RemoteViews remoteViews2) {
            this.hideNormalContent(remoteViews);
            remoteViews.removeAllViews(R.id.notification_main_column);
            remoteViews.addView(R.id.notification_main_column, remoteViews2.clone());
            remoteViews.setViewVisibility(R.id.notification_main_column, 0);
            if (Build.VERSION.SDK_INT >= 21) {
                remoteViews.setViewPadding(R.id.notification_main_column_container, 0, this.calculateTopPadding(), 0, 0);
            }
        }

        public Bitmap createColoredBitmap(int n, int n2) {
            return this.createColoredBitmap(n, n2, 0);
        }

        Bitmap createColoredBitmap(IconCompat iconCompat, int n) {
            return this.createColoredBitmap(iconCompat, n, 0);
        }

        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }

        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }

        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }

        protected void restoreFromCompatExtras(Bundle bundle) {
        }

        public void setBuilder(Builder builder) {
            if (this.mBuilder != builder) {
                this.mBuilder = builder;
                if (builder != null) {
                    builder.setStyle(this);
                }
            }
        }
    }

    public static final class WearableExtender
    implements Extender {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY = 0x800005;
        private static final int DEFAULT_FLAGS = 1;
        private static final int DEFAULT_GRAVITY = 80;
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
        private static final int FLAG_BIG_PICTURE_AMBIENT = 32;
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 16;
        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 64;
        private static final int FLAG_HINT_HIDE_ICON = 2;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
        private static final int FLAG_START_SCROLL_BOTTOM = 8;
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_BRIDGE_TAG = "bridgeTag";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_DISMISSAL_ID = "dismissalId";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
        private static final String KEY_PAGES = "pages";
        @Deprecated
        public static final int SCREEN_TIMEOUT_LONG = -1;
        @Deprecated
        public static final int SCREEN_TIMEOUT_SHORT = 0;
        @Deprecated
        public static final int SIZE_DEFAULT = 0;
        @Deprecated
        public static final int SIZE_FULL_SCREEN = 5;
        @Deprecated
        public static final int SIZE_LARGE = 4;
        @Deprecated
        public static final int SIZE_MEDIUM = 3;
        @Deprecated
        public static final int SIZE_SMALL = 2;
        @Deprecated
        public static final int SIZE_XSMALL = 1;
        public static final int UNSET_ACTION_INDEX = -1;
        private ArrayList<Action> mActions = new ArrayList();
        private Bitmap mBackground;
        private String mBridgeTag;
        private int mContentActionIndex = -1;
        private int mContentIcon;
        private int mContentIconGravity = 0x800005;
        private int mCustomContentHeight;
        private int mCustomSizePreset = 0;
        private String mDismissalId;
        private PendingIntent mDisplayIntent;
        private int mFlags = 1;
        private int mGravity = 80;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages = new ArrayList();

        public WearableExtender() {
        }

        public WearableExtender(Notification object) {
            object = NotificationCompat.getExtras(object);
            object = object != null ? object.getBundle(EXTRA_WEARABLE_EXTENSIONS) : null;
            if (object != null) {
                Notification[] notificationArray = object.getParcelableArrayList(KEY_ACTIONS);
                if (Build.VERSION.SDK_INT >= 16 && notificationArray != null) {
                    Action[] actionArray = new Action[notificationArray.size()];
                    for (int i = 0; i < actionArray.length; ++i) {
                        if (Build.VERSION.SDK_INT >= 20) {
                            actionArray[i] = NotificationCompat.getActionCompatFromAction((Notification.Action)notificationArray.get(i));
                            continue;
                        }
                        if (Build.VERSION.SDK_INT < 16) continue;
                        actionArray[i] = NotificationCompatJellybean.getActionFromBundle((Bundle)notificationArray.get(i));
                    }
                    Collections.addAll(this.mActions, actionArray);
                }
                this.mFlags = object.getInt(KEY_FLAGS, 1);
                this.mDisplayIntent = (PendingIntent)object.getParcelable(KEY_DISPLAY_INTENT);
                notificationArray = NotificationCompat.getNotificationArrayFromBundle((Bundle)object, KEY_PAGES);
                if (notificationArray != null) {
                    Collections.addAll(this.mPages, notificationArray);
                }
                this.mBackground = (Bitmap)object.getParcelable(KEY_BACKGROUND);
                this.mContentIcon = object.getInt(KEY_CONTENT_ICON);
                this.mContentIconGravity = object.getInt(KEY_CONTENT_ICON_GRAVITY, 0x800005);
                this.mContentActionIndex = object.getInt(KEY_CONTENT_ACTION_INDEX, -1);
                this.mCustomSizePreset = object.getInt(KEY_CUSTOM_SIZE_PRESET, 0);
                this.mCustomContentHeight = object.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
                this.mGravity = object.getInt(KEY_GRAVITY, 80);
                this.mHintScreenTimeout = object.getInt(KEY_HINT_SCREEN_TIMEOUT);
                this.mDismissalId = object.getString(KEY_DISMISSAL_ID);
                this.mBridgeTag = object.getString(KEY_BRIDGE_TAG);
            }
        }

        private static Notification.Action getActionFromActionCompat(Action remoteInputArray) {
            int n;
            int n2;
            IconCompat iconCompat;
            if (Build.VERSION.SDK_INT >= 23) {
                iconCompat = remoteInputArray.getIconCompat();
                iconCompat = iconCompat == null ? null : iconCompat.toIcon();
                iconCompat = new Notification.Action.Builder((Icon)iconCompat, remoteInputArray.getTitle(), remoteInputArray.getActionIntent());
            } else {
                iconCompat = remoteInputArray.getIconCompat();
                n = n2 = 0;
                if (iconCompat != null) {
                    n = n2;
                    if (iconCompat.getType() == 2) {
                        n = iconCompat.getResId();
                    }
                }
                iconCompat = new Notification.Action.Builder(n, remoteInputArray.getTitle(), remoteInputArray.getActionIntent());
            }
            Bundle bundle = remoteInputArray.getExtras() != null ? new Bundle(remoteInputArray.getExtras()) : new Bundle();
            bundle.putBoolean("android.support.allowGeneratedReplies", remoteInputArray.getAllowGeneratedReplies());
            if (Build.VERSION.SDK_INT >= 24) {
                iconCompat.setAllowGeneratedReplies(remoteInputArray.getAllowGeneratedReplies());
            }
            iconCompat.addExtras(bundle);
            remoteInputArray = remoteInputArray.getRemoteInputs();
            if (remoteInputArray != null) {
                remoteInputArray = RemoteInput.fromCompat(remoteInputArray);
                n2 = remoteInputArray.length;
                for (n = 0; n < n2; ++n) {
                    iconCompat.addRemoteInput((android.app.RemoteInput)remoteInputArray[n]);
                }
            }
            return iconCompat.build();
        }

        private void setFlag(int n, boolean bl) {
            this.mFlags = bl ? (this.mFlags |= n) : (this.mFlags &= ~n);
        }

        public WearableExtender addAction(Action action) {
            this.mActions.add(action);
            return this;
        }

        public WearableExtender addActions(List<Action> list) {
            this.mActions.addAll(list);
            return this;
        }

        @Deprecated
        public WearableExtender addPage(Notification notification) {
            this.mPages.add(notification);
            return this;
        }

        @Deprecated
        public WearableExtender addPages(List<Notification> list) {
            this.mPages.addAll(list);
            return this;
        }

        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }

        @Deprecated
        public WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }

        public WearableExtender clone() {
            WearableExtender wearableExtender = new WearableExtender();
            wearableExtender.mActions = new ArrayList<Action>(this.mActions);
            wearableExtender.mFlags = this.mFlags;
            wearableExtender.mDisplayIntent = this.mDisplayIntent;
            wearableExtender.mPages = new ArrayList<Notification>(this.mPages);
            wearableExtender.mBackground = this.mBackground;
            wearableExtender.mContentIcon = this.mContentIcon;
            wearableExtender.mContentIconGravity = this.mContentIconGravity;
            wearableExtender.mContentActionIndex = this.mContentActionIndex;
            wearableExtender.mCustomSizePreset = this.mCustomSizePreset;
            wearableExtender.mCustomContentHeight = this.mCustomContentHeight;
            wearableExtender.mGravity = this.mGravity;
            wearableExtender.mHintScreenTimeout = this.mHintScreenTimeout;
            wearableExtender.mDismissalId = this.mDismissalId;
            wearableExtender.mBridgeTag = this.mBridgeTag;
            return wearableExtender;
        }

        @Override
        public Builder extend(Builder builder) {
            String string2;
            String string3;
            Bitmap bitmap;
            PendingIntent pendingIntent;
            int n;
            Bundle bundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 16) {
                    ArrayList<Object> arrayList = new ArrayList<Object>(this.mActions.size());
                    for (Action object2 : this.mActions) {
                        if (Build.VERSION.SDK_INT >= 20) {
                            arrayList.add(WearableExtender.getActionFromActionCompat(object2));
                            continue;
                        }
                        if (Build.VERSION.SDK_INT < 16) continue;
                        arrayList.add(NotificationCompatJellybean.getBundleForAction(object2));
                    }
                    bundle.putParcelableArrayList(KEY_ACTIONS, arrayList);
                } else {
                    bundle.putParcelableArrayList(KEY_ACTIONS, null);
                }
            }
            if ((n = this.mFlags) != 1) {
                bundle.putInt(KEY_FLAGS, n);
            }
            if ((pendingIntent = this.mDisplayIntent) != null) {
                bundle.putParcelable(KEY_DISPLAY_INTENT, (Parcelable)pendingIntent);
            }
            if (!this.mPages.isEmpty()) {
                ArrayList<Notification> arrayList = this.mPages;
                bundle.putParcelableArray(KEY_PAGES, (Parcelable[])arrayList.toArray(new Notification[arrayList.size()]));
            }
            if ((bitmap = this.mBackground) != null) {
                bundle.putParcelable(KEY_BACKGROUND, (Parcelable)bitmap);
            }
            if ((n = this.mContentIcon) != 0) {
                bundle.putInt(KEY_CONTENT_ICON, n);
            }
            if ((n = this.mContentIconGravity) != 0x800005) {
                bundle.putInt(KEY_CONTENT_ICON_GRAVITY, n);
            }
            if ((n = this.mContentActionIndex) != -1) {
                bundle.putInt(KEY_CONTENT_ACTION_INDEX, n);
            }
            if ((n = this.mCustomSizePreset) != 0) {
                bundle.putInt(KEY_CUSTOM_SIZE_PRESET, n);
            }
            if ((n = this.mCustomContentHeight) != 0) {
                bundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, n);
            }
            if ((n = this.mGravity) != 80) {
                bundle.putInt(KEY_GRAVITY, n);
            }
            if ((n = this.mHintScreenTimeout) != 0) {
                bundle.putInt(KEY_HINT_SCREEN_TIMEOUT, n);
            }
            if ((string3 = this.mDismissalId) != null) {
                bundle.putString(KEY_DISMISSAL_ID, string3);
            }
            if ((string2 = this.mBridgeTag) != null) {
                bundle.putString(KEY_BRIDGE_TAG, string2);
            }
            builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, bundle);
            return builder;
        }

        public List<Action> getActions() {
            return this.mActions;
        }

        @Deprecated
        public Bitmap getBackground() {
            return this.mBackground;
        }

        public String getBridgeTag() {
            return this.mBridgeTag;
        }

        public int getContentAction() {
            return this.mContentActionIndex;
        }

        @Deprecated
        public int getContentIcon() {
            return this.mContentIcon;
        }

        @Deprecated
        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }

        public boolean getContentIntentAvailableOffline() {
            int n = this.mFlags;
            boolean bl = true;
            if ((n & 1) == 0) {
                bl = false;
            }
            return bl;
        }

        @Deprecated
        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }

        @Deprecated
        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }

        public String getDismissalId() {
            return this.mDismissalId;
        }

        @Deprecated
        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }

        @Deprecated
        public int getGravity() {
            return this.mGravity;
        }

        @Deprecated
        public boolean getHintAmbientBigPicture() {
            boolean bl = (this.mFlags & 0x20) != 0;
            return bl;
        }

        @Deprecated
        public boolean getHintAvoidBackgroundClipping() {
            boolean bl = (this.mFlags & 0x10) != 0;
            return bl;
        }

        public boolean getHintContentIntentLaunchesActivity() {
            boolean bl = (this.mFlags & 0x40) != 0;
            return bl;
        }

        @Deprecated
        public boolean getHintHideIcon() {
            boolean bl = (this.mFlags & 2) != 0;
            return bl;
        }

        @Deprecated
        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }

        @Deprecated
        public boolean getHintShowBackgroundOnly() {
            boolean bl = (this.mFlags & 4) != 0;
            return bl;
        }

        @Deprecated
        public List<Notification> getPages() {
            return this.mPages;
        }

        public boolean getStartScrollBottom() {
            boolean bl = (this.mFlags & 8) != 0;
            return bl;
        }

        @Deprecated
        public WearableExtender setBackground(Bitmap bitmap) {
            this.mBackground = bitmap;
            return this;
        }

        public WearableExtender setBridgeTag(String string2) {
            this.mBridgeTag = string2;
            return this;
        }

        public WearableExtender setContentAction(int n) {
            this.mContentActionIndex = n;
            return this;
        }

        @Deprecated
        public WearableExtender setContentIcon(int n) {
            this.mContentIcon = n;
            return this;
        }

        @Deprecated
        public WearableExtender setContentIconGravity(int n) {
            this.mContentIconGravity = n;
            return this;
        }

        public WearableExtender setContentIntentAvailableOffline(boolean bl) {
            this.setFlag(1, bl);
            return this;
        }

        @Deprecated
        public WearableExtender setCustomContentHeight(int n) {
            this.mCustomContentHeight = n;
            return this;
        }

        @Deprecated
        public WearableExtender setCustomSizePreset(int n) {
            this.mCustomSizePreset = n;
            return this;
        }

        public WearableExtender setDismissalId(String string2) {
            this.mDismissalId = string2;
            return this;
        }

        @Deprecated
        public WearableExtender setDisplayIntent(PendingIntent pendingIntent) {
            this.mDisplayIntent = pendingIntent;
            return this;
        }

        @Deprecated
        public WearableExtender setGravity(int n) {
            this.mGravity = n;
            return this;
        }

        @Deprecated
        public WearableExtender setHintAmbientBigPicture(boolean bl) {
            this.setFlag(32, bl);
            return this;
        }

        @Deprecated
        public WearableExtender setHintAvoidBackgroundClipping(boolean bl) {
            this.setFlag(16, bl);
            return this;
        }

        public WearableExtender setHintContentIntentLaunchesActivity(boolean bl) {
            this.setFlag(64, bl);
            return this;
        }

        @Deprecated
        public WearableExtender setHintHideIcon(boolean bl) {
            this.setFlag(2, bl);
            return this;
        }

        @Deprecated
        public WearableExtender setHintScreenTimeout(int n) {
            this.mHintScreenTimeout = n;
            return this;
        }

        @Deprecated
        public WearableExtender setHintShowBackgroundOnly(boolean bl) {
            this.setFlag(4, bl);
            return this;
        }

        public WearableExtender setStartScrollBottom(boolean bl) {
            this.setFlag(8, bl);
            return this;
        }
    }
}

