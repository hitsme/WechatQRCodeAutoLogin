package com.hitsme.wechatqrcodeautologin.webviewclient;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import de.robv.android.xposed.XposedBridge;

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        XposedBridge.log(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // Execute your javascript below
        String jsLine = "var i=2;alert(i);var x=document.getElementById(\"js_allow\");alert(x)";
        XposedBridge.log("jsLine->>"+jsLine);
        view.loadUrl("javascript:"+jsLine);
    }
}