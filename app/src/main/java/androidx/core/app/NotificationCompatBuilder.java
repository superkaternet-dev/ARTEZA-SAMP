/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Action$Builder
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.app.RemoteInput
 *  android.graphics.drawable.Icon
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.text.TextUtils
 *  android.util.SparseArray
 *  android.widget.RemoteViews
 */
package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompatJellybean;
import androidx.core.app.RemoteInput;
import androidx.core.graphics.drawable.IconCompat;
import java.util.ArrayList;
import java.util.List;

class NotificationCompatBuilder
implements NotificationBuilderWithBuilderAccessor {
    private final List<Bundle> mActionExtrasList = new ArrayList<Bundle>();
    private RemoteViews mBigContentView;
    private final Notification.Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private final Bundle mExtras = new Bundle();
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;

    NotificationCompatBuilder(NotificationCompat.Builder builder) {
        this.mBuilderCompat = builder;
        this.mBuilder = Build.VERSION.SDK_INT >= 26 ? new Notification.Builder(builder.mContext, builder.mChannelId) : new Notification.Builder(builder.mContext);
        Notification notification = builder.mNotification;
        Object object = this.mBuilder.setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, builder.mTickerView).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
        boolean bl = (notification.flags & 2) != 0;
        object = object.setOngoing(bl);
        bl = (notification.flags & 8) != 0;
        object = object.setOnlyAlertOnce(bl);
        bl = (notification.flags & 0x10) != 0;
        object = object.setAutoCancel(bl).setDefaults(notification.defaults).setContentTitle(builder.mContentTitle).setContentText(builder.mContentText).setContentInfo(builder.mContentInfo).setContentIntent(builder.mContentIntent).setDeleteIntent(notification.deleteIntent);
        PendingIntent object22 = builder.mFullScreenIntent;
        bl = (notification.flags & 0x80) != 0;
        object.setFullScreenIntent(object22, bl).setLargeIcon(builder.mLargeIcon).setNumber(builder.mNumber).setProgress(builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate);
        if (Build.VERSION.SDK_INT < 21) {
            this.mBuilder.setSound(notification.sound, notification.audioStreamType);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            this.mBuilder.setSubText(builder.mSubText).setUsesChronometer(builder.mUseChronometer).setPriority(builder.mPriority);
            object = builder.mActions.iterator();
            while (object.hasNext()) {
                this.addAction((NotificationCompat.Action)object.next());
            }
            if (builder.mExtras != null) {
                this.mExtras.putAll(builder.mExtras);
            }
            if (Build.VERSION.SDK_INT < 20) {
                if (builder.mLocalOnly) {
                    this.mExtras.putBoolean("android.support.localOnly", true);
                }
                if (builder.mGroupKey != null) {
                    this.mExtras.putString("android.support.groupKey", builder.mGroupKey);
                    if (builder.mGroupSummary) {
                        this.mExtras.putBoolean("android.support.isGroupSummary", true);
                    } else {
                        this.mExtras.putBoolean("android.support.useSideChannel", true);
                    }
                }
                if (builder.mSortKey != null) {
                    this.mExtras.putString("android.support.sortKey", builder.mSortKey);
                }
            }
            this.mContentView = builder.mContentView;
            this.mBigContentView = builder.mBigContentView;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            this.mBuilder.setShowWhen(builder.mShowWhen);
            if (Build.VERSION.SDK_INT < 21 && builder.mPeople != null && !builder.mPeople.isEmpty()) {
                this.mExtras.putStringArray("android.people", builder.mPeople.toArray(new String[builder.mPeople.size()]));
            }
        }
        if (Build.VERSION.SDK_INT >= 20) {
            this.mBuilder.setLocalOnly(builder.mLocalOnly).setGroup(builder.mGroupKey).setGroupSummary(builder.mGroupSummary).setSortKey(builder.mSortKey);
            this.mGroupAlertBehavior = builder.mGroupAlertBehavior;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            this.mBuilder.setCategory(builder.mCategory).setColor(builder.mColor).setVisibility(builder.mVisibility).setPublicVersion(builder.mPublicVersion).setSound(notification.sound, notification.audioAttributes);
            for (String string2 : builder.mPeople) {
                this.mBuilder.addPerson(string2);
            }
            this.mHeadsUpContentView = builder.mHeadsUpContentView;
            if (builder.mInvisibleActions.size() > 0) {
                Bundle bundle = builder.getExtras().getBundle("android.car.EXTENSIONS");
                object = bundle;
                if (bundle == null) {
                    object = new Bundle();
                }
                Bundle bundle2 = new Bundle();
                for (int i = 0; i < builder.mInvisibleActions.size(); ++i) {
                    bundle2.putBundle(Integer.toString(i), NotificationCompatJellybean.getBundleForAction(builder.mInvisibleActions.get(i)));
                }
                object.putBundle("invisible_actions", bundle2);
                builder.getExtras().putBundle("android.car.EXTENSIONS", (Bundle)object);
                this.mExtras.putBundle("android.car.EXTENSIONS", (Bundle)object);
            }
        }
        if (Build.VERSION.SDK_INT >= 24) {
            this.mBuilder.setExtras(builder.mExtras).setRemoteInputHistory(builder.mRemoteInputHistory);
            if (builder.mContentView != null) {
                this.mBuilder.setCustomContentView(builder.mContentView);
            }
            if (builder.mBigContentView != null) {
                this.mBuilder.setCustomBigContentView(builder.mBigContentView);
            }
            if (builder.mHeadsUpContentView != null) {
                this.mBuilder.setCustomHeadsUpContentView(builder.mHeadsUpContentView);
            }
        }
        if (Build.VERSION.SDK_INT >= 26) {
            this.mBuilder.setBadgeIconType(builder.mBadgeIcon).setShortcutId(builder.mShortcutId).setTimeoutAfter(builder.mTimeout).setGroupAlertBehavior(builder.mGroupAlertBehavior);
            if (builder.mColorizedSet) {
                this.mBuilder.setColorized(builder.mColorized);
            }
            if (!TextUtils.isEmpty((CharSequence)builder.mChannelId)) {
                this.mBuilder.setSound(null).setDefaults(0).setLights(0, 0, 0).setVibrate(null);
            }
        }
        if (Build.VERSION.SDK_INT >= 29) {
            this.mBuilder.setAllowSystemGeneratedContextualActions(builder.mAllowSystemGeneratedContextualActions);
            this.mBuilder.setBubbleMetadata(NotificationCompat.BubbleMetadata.toPlatform(builder.mBubbleMetadata));
        }
        if (builder.mSilent) {
            this.mGroupAlertBehavior = this.mBuilderCompat.mGroupSummary ? 2 : 1;
            this.mBuilder.setVibrate(null);
            this.mBuilder.setSound(null);
            notification.defaults &= 0xFFFFFFFE;
            notification.defaults &= 0xFFFFFFFD;
            this.mBuilder.setDefaults(notification.defaults);
            if (Build.VERSION.SDK_INT >= 26) {
                if (TextUtils.isEmpty((CharSequence)this.mBuilderCompat.mGroupKey)) {
                    this.mBuilder.setGroup("silent");
                }
                this.mBuilder.setGroupAlertBehavior(this.mGroupAlertBehavior);
            }
        }
    }

    private void addAction(NotificationCompat.Action action) {
        block8: {
            block7: {
                Bundle bundle;
                if (Build.VERSION.SDK_INT < 20) break block7;
                IconCompat iconCompat = action.getIconCompat();
                int n = Build.VERSION.SDK_INT;
                int n2 = 0;
                if (n >= 23) {
                    iconCompat = iconCompat != null ? iconCompat.toIcon() : null;
                    iconCompat = new Notification.Action.Builder((Icon)iconCompat, action.getTitle(), action.getActionIntent());
                } else {
                    n = iconCompat != null ? iconCompat.getResId() : 0;
                    iconCompat = new Notification.Action.Builder(n, action.getTitle(), action.getActionIntent());
                }
                if (action.getRemoteInputs() != null) {
                    bundle = RemoteInput.fromCompat(action.getRemoteInputs());
                    int n3 = ((android.app.RemoteInput[])bundle).length;
                    for (n = n2; n < n3; ++n) {
                        iconCompat.addRemoteInput((android.app.RemoteInput)bundle[n]);
                    }
                }
                bundle = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
                bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
                if (Build.VERSION.SDK_INT >= 24) {
                    iconCompat.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
                }
                bundle.putInt("android.support.action.semanticAction", action.getSemanticAction());
                if (Build.VERSION.SDK_INT >= 28) {
                    iconCompat.setSemanticAction(action.getSemanticAction());
                }
                if (Build.VERSION.SDK_INT >= 29) {
                    iconCompat.setContextual(action.isContextual());
                }
                bundle.putBoolean("android.support.action.showsUserInterface", action.getShowsUserInterface());
                iconCompat.addExtras(bundle);
                this.mBuilder.addAction(iconCompat.build());
                break block8;
            }
            if (Build.VERSION.SDK_INT < 16) break block8;
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, action));
        }
    }

    private void removeSoundAndVibration(Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults &= 0xFFFFFFFE;
        notification.defaults &= 0xFFFFFFFD;
    }

    public Notification build() {
        NotificationCompat.Style style2 = this.mBuilderCompat.mStyle;
        if (style2 != null) {
            style2.apply(this);
        }
        RemoteViews remoteViews = style2 != null ? style2.makeContentView(this) : null;
        Notification notification = this.buildInternal();
        if (remoteViews != null) {
            notification.contentView = remoteViews;
        } else if (this.mBuilderCompat.mContentView != null) {
            notification.contentView = this.mBuilderCompat.mContentView;
        }
        if (Build.VERSION.SDK_INT >= 16 && style2 != null && (remoteViews = style2.makeBigContentView(this)) != null) {
            notification.bigContentView = remoteViews;
        }
        if (Build.VERSION.SDK_INT >= 21 && style2 != null && (remoteViews = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this)) != null) {
            notification.headsUpContentView = remoteViews;
        }
        if (Build.VERSION.SDK_INT >= 16 && style2 != null && (remoteViews = NotificationCompat.getExtras(notification)) != null) {
            style2.addCompatExtras((Bundle)remoteViews);
        }
        return notification;
    }

    protected Notification buildInternal() {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.mBuilder.build();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            Notification notification = this.mBuilder.build();
            if (this.mGroupAlertBehavior != 0) {
                if (notification.getGroup() != null && (notification.flags & 0x200) != 0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(notification);
                }
                if (notification.getGroup() != null && (notification.flags & 0x200) == 0 && this.mGroupAlertBehavior == 1) {
                    this.removeSoundAndVibration(notification);
                }
            }
            return notification;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            this.mBuilder.setExtras(this.mExtras);
            Notification notification = this.mBuilder.build();
            RemoteViews remoteViews = this.mContentView;
            if (remoteViews != null) {
                notification.contentView = remoteViews;
            }
            if ((remoteViews = this.mBigContentView) != null) {
                notification.bigContentView = remoteViews;
            }
            if ((remoteViews = this.mHeadsUpContentView) != null) {
                notification.headsUpContentView = remoteViews;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (notification.getGroup() != null && (notification.flags & 0x200) != 0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(notification);
                }
                if (notification.getGroup() != null && (notification.flags & 0x200) == 0 && this.mGroupAlertBehavior == 1) {
                    this.removeSoundAndVibration(notification);
                }
            }
            return notification;
        }
        if (Build.VERSION.SDK_INT >= 20) {
            this.mBuilder.setExtras(this.mExtras);
            Notification notification = this.mBuilder.build();
            RemoteViews remoteViews = this.mContentView;
            if (remoteViews != null) {
                notification.contentView = remoteViews;
            }
            if ((remoteViews = this.mBigContentView) != null) {
                notification.bigContentView = remoteViews;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (notification.getGroup() != null && (notification.flags & 0x200) != 0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(notification);
                }
                if (notification.getGroup() != null && (notification.flags & 0x200) == 0 && this.mGroupAlertBehavior == 1) {
                    this.removeSoundAndVibration(notification);
                }
            }
            return notification;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            Notification notification = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (notification != null) {
                this.mExtras.putSparseParcelableArray("android.support.actionExtras", notification);
            }
            this.mBuilder.setExtras(this.mExtras);
            notification = this.mBuilder.build();
            RemoteViews remoteViews = this.mContentView;
            if (remoteViews != null) {
                notification.contentView = remoteViews;
            }
            if ((remoteViews = this.mBigContentView) != null) {
                notification.bigContentView = remoteViews;
            }
            return notification;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            Notification notification = this.mBuilder.build();
            RemoteViews remoteViews = NotificationCompat.getExtras(notification);
            Bundle bundle = new Bundle(this.mExtras);
            for (String string2 : this.mExtras.keySet()) {
                if (!remoteViews.containsKey(string2)) continue;
                bundle.remove(string2);
            }
            remoteViews.putAll(bundle);
            remoteViews = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (remoteViews != null) {
                NotificationCompat.getExtras(notification).putSparseParcelableArray("android.support.actionExtras", (SparseArray)remoteViews);
            }
            if ((remoteViews = this.mContentView) != null) {
                notification.contentView = remoteViews;
            }
            if ((remoteViews = this.mBigContentView) != null) {
                notification.bigContentView = remoteViews;
            }
            return notification;
        }
        return this.mBuilder.getNotification();
    }

    @Override
    public Notification.Builder getBuilder() {
        return this.mBuilder;
    }
}

