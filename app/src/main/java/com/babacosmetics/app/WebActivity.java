package com.babacosmetics.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/** In-app browser used for Shopify checkout and the customer account. */
public class WebActivity extends BaseActivity {

    public static void open(Activity a, String url, String title, boolean clearCartOnOrder) {
        Intent i = new Intent(a, WebActivity.class);
        i.putExtra("url", url);
        i.putExtra("title", title);
        i.putExtra("clearCart", clearCartOnOrder);
        a.startActivity(i);
    }

    private WebView web;
    private ProgressBar progress;
    private boolean clearCart;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        clearCart = getIntent().getBooleanExtra("clearCart", false);

        LinearLayout root = U.vertical(this);
        root.setBackgroundColor(0xFFFFFFFF);
        root.addView(U.topBar(this, title == null ? "Baba Cosmetics" : title, true), U.lp(U.MATCH, U.WRAP));

        progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100);
        progress.setProgressTintList(android.content.res.ColorStateList.valueOf(Config.BRAND_DARK));
        root.addView(progress, U.lp(U.MATCH, U.dp(this, 3)));

        web = new WebView(this);
        root.addView(web, U.lp(U.MATCH, 0, 1));
        setContentView(root);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setUserAgentString(s.getUserAgentString() + " BabaCosmeticsApp/1.0");
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int p) {
                progress.setProgress(p);
                progress.setVisibility(p >= 100 ? View.INVISIBLE : View.VISIBLE);
            }
        });
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handle(request.getUrl().toString());
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String u) {
                return handle(u);
            }

            @Override
            public void onPageStarted(WebView view, String u, Bitmap favicon) {
                checkOrderDone(u);
            }

            @Override
            public void onPageFinished(WebView view, String u) {
                checkOrderDone(u);
            }
        });

        if (url != null) web.loadUrl(url);
    }

    /** Opens phone, WhatsApp, email etc. links in their own apps; web pages stay in the app. */
    private boolean handle(String u) {
        if (u == null) return false;
        checkOrderDone(u);
        if (u.startsWith("http://") || u.startsWith("https://")) return false;
        try {
            Intent i;
            if (u.startsWith("intent:")) {
                i = Intent.parseUri(u, Intent.URI_INTENT_SCHEME);
            } else {
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(u));
            }
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            U.toast(this, "No app found to open this link");
        } catch (Exception ignored) {
        }
        return true;
    }

    /** When Shopify shows the "thank you" page, the order is placed, so empty the app's cart. */
    private void checkOrderDone(String u) {
        if (!clearCart || u == null) return;
        if (u.contains("thank_you") || u.contains("thank-you") || u.contains("/orders/")) {
            Cart.clear();
            clearCart = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.stopLoading();
            web.destroy();
        }
        super.onDestroy();
    }
}
