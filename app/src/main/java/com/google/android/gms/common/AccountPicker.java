/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Parcelable
 */
package com.google.android.gms.common;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.zza;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class AccountPicker {
    private AccountPicker() {
    }

    @Deprecated
    public static Intent newChooseAccountIntent(Account account, ArrayList<Account> arrayList, String[] stringArray, boolean bl, String string2, String string3, String[] stringArray2, Bundle bundle) {
        Intent intent = new Intent();
        Preconditions.checkArgument(true, "We only support hostedDomain filter for account chip styled account picker");
        intent.setAction("com.google.android.gms.common.account.CHOOSE_ACCOUNT");
        intent.setPackage("com.google.android.gms");
        intent.putExtra("allowableAccounts", arrayList);
        intent.putExtra("allowableAccountTypes", stringArray);
        intent.putExtra("addAccountOptions", bundle);
        intent.putExtra("selectedAccount", (Parcelable)account);
        intent.putExtra("alwaysPromptForAccount", bl);
        intent.putExtra("descriptionTextOverride", string2);
        intent.putExtra("authTokenType", string3);
        intent.putExtra("addAccountRequiredFeatures", stringArray2);
        intent.putExtra("setGmsCoreAccount", false);
        intent.putExtra("overrideTheme", 0);
        intent.putExtra("overrideCustomTheme", 0);
        intent.putExtra("hostedDomainFilter", null);
        return intent;
    }

    public static Intent newChooseAccountIntent(AccountChooserOptions accountChooserOptions) {
        Intent intent = new Intent();
        AccountChooserOptions.zzD(accountChooserOptions);
        AccountChooserOptions.zze(accountChooserOptions);
        Preconditions.checkArgument(true, "We only support hostedDomain filter for account chip styled account picker");
        AccountChooserOptions.zzd(accountChooserOptions);
        Preconditions.checkArgument(true, "Consent is only valid for account chip styled account picker");
        AccountChooserOptions.zzB(accountChooserOptions);
        Preconditions.checkArgument(true, "Making the selected account non-clickable is only supported for the theme THEME_DAY_NIGHT_GOOGLE_MATERIAL2");
        AccountChooserOptions.zzD(accountChooserOptions);
        intent.setAction("com.google.android.gms.common.account.CHOOSE_ACCOUNT");
        intent.setPackage("com.google.android.gms");
        intent.putExtra("allowableAccounts", (Serializable)accountChooserOptions.zzc);
        if (accountChooserOptions.zzd != null) {
            intent.putExtra("allowableAccountTypes", accountChooserOptions.zzd.toArray(new String[0]));
        }
        intent.putExtra("addAccountOptions", accountChooserOptions.zzg);
        intent.putExtra("selectedAccount", (Parcelable)accountChooserOptions.zza);
        AccountChooserOptions.zzB(accountChooserOptions);
        intent.putExtra("selectedAccountIsNotClickable", false);
        intent.putExtra("alwaysPromptForAccount", accountChooserOptions.zze);
        intent.putExtra("descriptionTextOverride", accountChooserOptions.zzf);
        AccountChooserOptions.zzC(accountChooserOptions);
        intent.putExtra("setGmsCoreAccount", false);
        AccountChooserOptions.zzf(accountChooserOptions);
        intent.putExtra("realClientPackage", null);
        AccountChooserOptions.zza(accountChooserOptions);
        intent.putExtra("overrideTheme", 0);
        AccountChooserOptions.zzD(accountChooserOptions);
        intent.putExtra("overrideCustomTheme", 0);
        AccountChooserOptions.zze(accountChooserOptions);
        intent.putExtra("hostedDomainFilter", null);
        Bundle bundle = new Bundle();
        AccountChooserOptions.zzD(accountChooserOptions);
        AccountChooserOptions.zzd(accountChooserOptions);
        AccountChooserOptions.zzz(accountChooserOptions);
        AccountChooserOptions.zzA(accountChooserOptions);
        if (!bundle.isEmpty()) {
            intent.putExtra("first_party_options_bundle", bundle);
        }
        return intent;
    }

    public static class AccountChooserOptions {
        private Account zza;
        private boolean zzb;
        private ArrayList<Account> zzc;
        private ArrayList<String> zzd;
        private boolean zze;
        private String zzf;
        private Bundle zzg;
        private boolean zzh;
        private int zzi;
        private String zzj;
        private boolean zzk;
        private zza zzl;
        private String zzm;
        private boolean zzn;
        private boolean zzo;

        static /* bridge */ /* synthetic */ boolean zzA(AccountChooserOptions accountChooserOptions) {
            boolean bl = accountChooserOptions.zzo;
            return false;
        }

        static /* bridge */ /* synthetic */ boolean zzB(AccountChooserOptions accountChooserOptions) {
            boolean bl = accountChooserOptions.zzb;
            return false;
        }

        static /* bridge */ /* synthetic */ boolean zzC(AccountChooserOptions accountChooserOptions) {
            boolean bl = accountChooserOptions.zzh;
            return false;
        }

        static /* bridge */ /* synthetic */ boolean zzD(AccountChooserOptions accountChooserOptions) {
            boolean bl = accountChooserOptions.zzk;
            return false;
        }

        static /* bridge */ /* synthetic */ int zza(AccountChooserOptions accountChooserOptions) {
            int n = accountChooserOptions.zzi;
            return 0;
        }

        static /* bridge */ /* synthetic */ zza zzd(AccountChooserOptions object) {
            object = ((AccountChooserOptions)object).zzl;
            return null;
        }

        static /* bridge */ /* synthetic */ String zze(AccountChooserOptions object) {
            object = ((AccountChooserOptions)object).zzj;
            return null;
        }

        static /* bridge */ /* synthetic */ String zzf(AccountChooserOptions object) {
            object = ((AccountChooserOptions)object).zzm;
            return null;
        }

        static /* bridge */ /* synthetic */ boolean zzz(AccountChooserOptions accountChooserOptions) {
            boolean bl = accountChooserOptions.zzn;
            return false;
        }

        public static class Builder {
            private Account zza;
            private ArrayList<Account> zzb;
            private ArrayList<String> zzc;
            private boolean zzd = false;
            private String zze;
            private Bundle zzf;

            public AccountChooserOptions build() {
                Preconditions.checkArgument(true, "We only support hostedDomain filter for account chip styled account picker");
                Preconditions.checkArgument(true, "Consent is only valid for account chip styled account picker");
                AccountChooserOptions accountChooserOptions = new AccountChooserOptions();
                accountChooserOptions.zzd = this.zzc;
                accountChooserOptions.zzc = this.zzb;
                accountChooserOptions.zze = this.zzd;
                accountChooserOptions.zzl = null;
                accountChooserOptions.zzj = null;
                accountChooserOptions.zzg = this.zzf;
                accountChooserOptions.zza = this.zza;
                accountChooserOptions.zzb = false;
                accountChooserOptions.zzh = false;
                accountChooserOptions.zzm = null;
                accountChooserOptions.zzi = 0;
                accountChooserOptions.zzf = this.zze;
                accountChooserOptions.zzk = false;
                accountChooserOptions.zzn = false;
                accountChooserOptions.zzo = false;
                return accountChooserOptions;
            }

            public Builder setAllowableAccounts(List<Account> list) {
                list = list == null ? null : new ArrayList<Account>(list);
                this.zzb = list;
                return this;
            }

            public Builder setAllowableAccountsTypes(List<String> list) {
                list = list == null ? null : new ArrayList<String>(list);
                this.zzc = list;
                return this;
            }

            public Builder setAlwaysShowAccountPicker(boolean bl) {
                this.zzd = bl;
                return this;
            }

            public Builder setOptionsForAddingAccount(Bundle bundle) {
                this.zzf = bundle;
                return this;
            }

            public Builder setSelectedAccount(Account account) {
                this.zza = account;
                return this;
            }

            public Builder setTitleOverrideText(String string2) {
                this.zze = string2;
                return this;
            }
        }
    }
}

