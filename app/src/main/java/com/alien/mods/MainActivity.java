package com.alien.mods;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private WebView web;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setBuiltInZoomControls(false);
        web.setBackgroundColor(0xFF06070C);
        web.setWebViewClient(new WebViewClient());
        web.addJavascriptInterface(new Bridge(), "Android");
        setContentView(web);
        web.loadUrl("file:///android_asset/panel.html");
    }

    private boolean hasRoot() {
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = r.readLine();
            p.waitFor();
            return line != null && line.contains("uid=0");
        } catch (Exception e) { return false; }
    }

    private String sh(String cmd) {
        StringBuilder sb = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String l;
            while ((l = r.readLine()) != null) sb.append(l).append('\n');
            p.waitFor();
        } catch (Exception e) { return "err"; }
        return sb.toString();
    }

    private void js(final String code) {
        runOnUiThread(new Runnable() { public void run() { web.evaluateJavascript(code, null); } });
    }

    class Bridge {

        @JavascriptInterface public void checkRoot() {
            new Thread(new Runnable() { public void run() {
                final boolean ok = hasRoot();
                js("window.onRoot('" + (ok ? "yes" : "no") + "')");
            } }).start();
        }

        @JavascriptInterface public String shell(String cmd) { return sh(cmd); }

        @JavascriptInterface public String androidId() {
            return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        @JavascriptInterface public String model() { return Build.MANUFACTURER + " " + Build.MODEL; }

        @JavascriptInterface public void toast(String m) {
            runOnUiThread(new Runnable() { public void run() {
                Toast.makeText(MainActivity.this, m, Toast.LENGTH_SHORT).show();
            } });
        }

        @JavascriptInterface public void setClip(String t) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) cm.setPrimaryClip(ClipData.newPlainText("id", t));
        }

        @JavascriptInterface public boolean launchFF() {
            PackageManager pm = getPackageManager();
            String[] pk = {"com.dts.freefiremax", "com.dts.freefireth"};
            for (String p : pk) {
                try {
                    Intent i = pm.getLaunchIntentForPackage(p);
                    if (i != null) { i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i); return true; }
                } catch (Exception e) { }
            }
            return false;
        }
    }
}
