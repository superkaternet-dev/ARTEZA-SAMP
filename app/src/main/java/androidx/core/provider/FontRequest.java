/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 */
package androidx.core.provider;

import android.util.Base64;
import androidx.core.util.Preconditions;
import java.util.List;

public final class FontRequest {
    private final List<List<byte[]>> mCertificates;
    private final int mCertificatesArray;
    private final String mIdentifier;
    private final String mProviderAuthority;
    private final String mProviderPackage;
    private final String mQuery;

    public FontRequest(String string2, String string3, String charSequence, int n) {
        String string4;
        this.mProviderAuthority = string4 = Preconditions.checkNotNull(string2);
        this.mProviderPackage = string2 = Preconditions.checkNotNull(string3);
        this.mQuery = string3 = Preconditions.checkNotNull(charSequence);
        this.mCertificates = null;
        boolean bl = n != 0;
        Preconditions.checkArgument(bl);
        this.mCertificatesArray = n;
        charSequence = new StringBuilder(string4);
        ((StringBuilder)charSequence).append("-");
        ((StringBuilder)charSequence).append(string2);
        ((StringBuilder)charSequence).append("-");
        ((StringBuilder)charSequence).append(string3);
        this.mIdentifier = ((StringBuilder)charSequence).toString();
    }

    public FontRequest(String charSequence, String string2, String string3, List<List<byte[]>> list) {
        charSequence = Preconditions.checkNotNull(charSequence);
        this.mProviderAuthority = charSequence;
        this.mProviderPackage = string2 = Preconditions.checkNotNull(string2);
        this.mQuery = string3 = Preconditions.checkNotNull(string3);
        this.mCertificates = Preconditions.checkNotNull(list);
        this.mCertificatesArray = 0;
        charSequence = new StringBuilder((String)charSequence);
        ((StringBuilder)charSequence).append("-");
        ((StringBuilder)charSequence).append(string2);
        ((StringBuilder)charSequence).append("-");
        ((StringBuilder)charSequence).append(string3);
        this.mIdentifier = ((StringBuilder)charSequence).toString();
    }

    public List<List<byte[]>> getCertificates() {
        return this.mCertificates;
    }

    public int getCertificatesArrayResId() {
        return this.mCertificatesArray;
    }

    public String getIdentifier() {
        return this.mIdentifier;
    }

    public String getProviderAuthority() {
        return this.mProviderAuthority;
    }

    public String getProviderPackage() {
        return this.mProviderPackage;
    }

    public String getQuery() {
        return this.mQuery;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Object object = new StringBuilder();
        ((StringBuilder)object).append("FontRequest {mProviderAuthority: ");
        ((StringBuilder)object).append(this.mProviderAuthority);
        ((StringBuilder)object).append(", mProviderPackage: ");
        ((StringBuilder)object).append(this.mProviderPackage);
        ((StringBuilder)object).append(", mQuery: ");
        ((StringBuilder)object).append(this.mQuery);
        ((StringBuilder)object).append(", mCertificates:");
        stringBuilder.append(((StringBuilder)object).toString());
        for (int i = 0; i < this.mCertificates.size(); ++i) {
            stringBuilder.append(" [");
            object = this.mCertificates.get(i);
            for (int j = 0; j < object.size(); ++j) {
                stringBuilder.append(" \"");
                stringBuilder.append(Base64.encodeToString((byte[])((byte[])object.get(j)), (int)0));
                stringBuilder.append("\"");
            }
            stringBuilder.append(" ]");
        }
        stringBuilder.append("}");
        object = new StringBuilder();
        ((StringBuilder)object).append("mCertificatesArray: ");
        ((StringBuilder)object).append(this.mCertificatesArray);
        stringBuilder.append(((StringBuilder)object).toString());
        return stringBuilder.toString();
    }
}

