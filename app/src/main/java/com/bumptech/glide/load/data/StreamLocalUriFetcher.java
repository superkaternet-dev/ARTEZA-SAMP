/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.UriMatcher
 *  android.net.Uri
 *  android.provider.ContactsContract$Contacts
 */
package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.ContactsContract;
import com.bumptech.glide.load.data.LocalUriFetcher;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StreamLocalUriFetcher
extends LocalUriFetcher<InputStream> {
    private static final int ID_CONTACTS_CONTACT = 3;
    private static final int ID_CONTACTS_LOOKUP = 1;
    private static final int ID_CONTACTS_PHOTO = 4;
    private static final int ID_CONTACTS_THUMBNAIL = 2;
    private static final int ID_LOOKUP_BY_PHONE = 5;
    private static final UriMatcher URI_MATCHER;

    static {
        UriMatcher uriMatcher;
        URI_MATCHER = uriMatcher = new UriMatcher(-1);
        uriMatcher.addURI("com.android.contacts", "contacts/lookup/*/#", 1);
        uriMatcher.addURI("com.android.contacts", "contacts/lookup/*", 1);
        uriMatcher.addURI("com.android.contacts", "contacts/#/photo", 2);
        uriMatcher.addURI("com.android.contacts", "contacts/#", 3);
        uriMatcher.addURI("com.android.contacts", "contacts/#/display_photo", 4);
        uriMatcher.addURI("com.android.contacts", "phone_lookup/*", 5);
    }

    public StreamLocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        super(contentResolver, uri);
    }

    private InputStream loadResourceFromUri(Uri uri, ContentResolver contentResolver) throws FileNotFoundException {
        switch (URI_MATCHER.match(uri)) {
            default: {
                return contentResolver.openInputStream(uri);
            }
            case 3: {
                return this.openContactPhotoInputStream(contentResolver, uri);
            }
            case 1: 
            case 5: 
        }
        uri = ContactsContract.Contacts.lookupContact((ContentResolver)contentResolver, (Uri)uri);
        if (uri != null) {
            return this.openContactPhotoInputStream(contentResolver, uri);
        }
        throw new FileNotFoundException("Contact cannot be found");
    }

    private InputStream openContactPhotoInputStream(ContentResolver contentResolver, Uri uri) {
        return ContactsContract.Contacts.openContactPhotoInputStream((ContentResolver)contentResolver, (Uri)uri, (boolean)true);
    }

    @Override
    protected void close(InputStream inputStream) throws IOException {
        inputStream.close();
    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override
    protected InputStream loadResource(Uri uri, ContentResolver object) throws FileNotFoundException {
        if ((object = this.loadResourceFromUri(uri, (ContentResolver)object)) != null) {
            return object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("InputStream is null for ");
        ((StringBuilder)object).append(uri);
        throw new FileNotFoundException(((StringBuilder)object).toString());
    }
}

