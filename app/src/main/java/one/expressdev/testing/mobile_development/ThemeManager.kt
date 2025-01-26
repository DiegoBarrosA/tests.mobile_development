package one.expressdev.testing.mobile_development

import android.webkit.WebView

class ThemeManager(private val webView: WebView) {

    fun updateTheme(isDarkTheme: Boolean) {
        val themeCss = if (isDarkTheme) {
            "body { background-color: #212121; color: #E0E0E0; }"
        } else {
            "body { background-color: #FFFFFF; color: #000000; }"
        }

        webView.loadUrl("javascript:(function() { " +
                "document.getElementsByTagName('head')[0].innerHTML += '<style>$themeCss</style>'; " +
                "})()")
    }
}
