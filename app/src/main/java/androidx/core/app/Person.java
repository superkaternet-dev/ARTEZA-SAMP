/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Person
 *  android.app.Person$Builder
 *  android.graphics.drawable.Icon
 *  android.os.Bundle
 *  android.os.PersistableBundle
 */
package androidx.core.app;

import android.app.Person;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.core.graphics.drawable.IconCompat;

public class Person {
    private static final String ICON_KEY = "icon";
    private static final String IS_BOT_KEY = "isBot";
    private static final String IS_IMPORTANT_KEY = "isImportant";
    private static final String KEY_KEY = "key";
    private static final String NAME_KEY = "name";
    private static final String URI_KEY = "uri";
    IconCompat mIcon;
    boolean mIsBot;
    boolean mIsImportant;
    String mKey;
    CharSequence mName;
    String mUri;

    Person(Builder builder) {
        this.mName = builder.mName;
        this.mIcon = builder.mIcon;
        this.mUri = builder.mUri;
        this.mKey = builder.mKey;
        this.mIsBot = builder.mIsBot;
        this.mIsImportant = builder.mIsImportant;
    }

    public static Person fromAndroidPerson(android.app.Person person) {
        Builder builder = new Builder().setName(person.getName());
        IconCompat iconCompat = person.getIcon() != null ? IconCompat.createFromIcon(person.getIcon()) : null;
        return builder.setIcon(iconCompat).setUri(person.getUri()).setKey(person.getKey()).setBot(person.isBot()).setImportant(person.isImportant()).build();
    }

    public static Person fromBundle(Bundle bundle) {
        Object object = bundle.getBundle(ICON_KEY);
        Builder builder = new Builder().setName(bundle.getCharSequence(NAME_KEY));
        object = object != null ? IconCompat.createFromBundle(object) : null;
        return builder.setIcon((IconCompat)object).setUri(bundle.getString(URI_KEY)).setKey(bundle.getString(KEY_KEY)).setBot(bundle.getBoolean(IS_BOT_KEY)).setImportant(bundle.getBoolean(IS_IMPORTANT_KEY)).build();
    }

    public static Person fromPersistableBundle(PersistableBundle persistableBundle) {
        return new Builder().setName(persistableBundle.getString(NAME_KEY)).setUri(persistableBundle.getString(URI_KEY)).setKey(persistableBundle.getString(KEY_KEY)).setBot(persistableBundle.getBoolean(IS_BOT_KEY)).setImportant(persistableBundle.getBoolean(IS_IMPORTANT_KEY)).build();
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public String getKey() {
        return this.mKey;
    }

    public CharSequence getName() {
        return this.mName;
    }

    public String getUri() {
        return this.mUri;
    }

    public boolean isBot() {
        return this.mIsBot;
    }

    public boolean isImportant() {
        return this.mIsImportant;
    }

    public android.app.Person toAndroidPerson() {
        Person.Builder builder = new Person.Builder().setName(this.getName());
        Icon icon = this.getIcon() != null ? this.getIcon().toIcon() : null;
        return builder.setIcon(icon).setUri(this.getUri()).setKey(this.getKey()).setBot(this.isBot()).setImportant(this.isImportant()).build();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(NAME_KEY, this.mName);
        IconCompat iconCompat = this.mIcon;
        iconCompat = iconCompat != null ? iconCompat.toBundle() : null;
        bundle.putBundle(ICON_KEY, (Bundle)iconCompat);
        bundle.putString(URI_KEY, this.mUri);
        bundle.putString(KEY_KEY, this.mKey);
        bundle.putBoolean(IS_BOT_KEY, this.mIsBot);
        bundle.putBoolean(IS_IMPORTANT_KEY, this.mIsImportant);
        return bundle;
    }

    public PersistableBundle toPersistableBundle() {
        PersistableBundle persistableBundle = new PersistableBundle();
        CharSequence charSequence = this.mName;
        charSequence = charSequence != null ? charSequence.toString() : null;
        persistableBundle.putString(NAME_KEY, (String)charSequence);
        persistableBundle.putString(URI_KEY, this.mUri);
        persistableBundle.putString(KEY_KEY, this.mKey);
        persistableBundle.putBoolean(IS_BOT_KEY, this.mIsBot);
        persistableBundle.putBoolean(IS_IMPORTANT_KEY, this.mIsImportant);
        return persistableBundle;
    }

    public static class Builder {
        IconCompat mIcon;
        boolean mIsBot;
        boolean mIsImportant;
        String mKey;
        CharSequence mName;
        String mUri;

        public Builder() {
        }

        Builder(Person person) {
            this.mName = person.mName;
            this.mIcon = person.mIcon;
            this.mUri = person.mUri;
            this.mKey = person.mKey;
            this.mIsBot = person.mIsBot;
            this.mIsImportant = person.mIsImportant;
        }

        public Person build() {
            return new Person(this);
        }

        public Builder setBot(boolean bl) {
            this.mIsBot = bl;
            return this;
        }

        public Builder setIcon(IconCompat iconCompat) {
            this.mIcon = iconCompat;
            return this;
        }

        public Builder setImportant(boolean bl) {
            this.mIsImportant = bl;
            return this;
        }

        public Builder setKey(String string2) {
            this.mKey = string2;
            return this;
        }

        public Builder setName(CharSequence charSequence) {
            this.mName = charSequence;
            return this;
        }

        public Builder setUri(String string2) {
            this.mUri = string2;
            return this;
        }
    }
}

