package com.techmave.fisherville.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class Utility {

    public static Spanned fromHtml(String html) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }

        else {

            return Html.fromHtml(html);
        }
    }
}
