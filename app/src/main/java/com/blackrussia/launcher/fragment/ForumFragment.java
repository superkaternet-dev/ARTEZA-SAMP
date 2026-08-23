/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.webkit.WebView
 *  android.webkit.WebViewClient
 */
package com.blackrussia.launcher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;

public class ForumFragment
extends Fragment {
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        viewGroup = layoutInflater.inflate(2131558467, viewGroup, false);
        layoutInflater = (WebView)viewGroup.findViewById(2131362515);
        this.webView = layoutInflater;
        layoutInflater.setWebViewClient(new WebViewClient());
        this.webView.loadUrl("https://forum-blackrussiabonus.com/");
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setLoadWithOverviewMode(false);
        this.webView.getSettings().setUseWideViewPort(false);
        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setAllowContentAccess(true);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setDisplayZoomControls(true);
        this.webView.getSettings().setSupportZoom(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setGeolocationEnabled(true);
        this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        this.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        this.webView.getSettings().setCacheMode(2);
        this.webView.getSettings().setDatabaseEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        return viewGroup;
    }
}

