package com.ibradecode.whatscloneweb;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showNotification(String title, String message) {
        NotificationHelper.showNotification(mContext, title, message);
    }
}


