package com.example.tvtube.network

object UserAgents {
    const val TV_HTML5 = "Mozilla/5.0 (SMART-TV; Linux; Tizen 6.0) AppleWebKit/537.36 (KHTML, like Gecko) Version/6.0 TV Safari/537.36"
    const val ANDROID_TV = "Mozilla/5.0 (Linux; Android 10; ATV) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    const val CHROME_DESKTOP = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
    const val FIREFOX_DESKTOP = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:133.0) Gecko/20100101 Firefox/133.0"
    const val SAFARI_MAC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.1.1 Safari/605.1.15"
    
    val list = listOf(
        TV_HTML5,
        ANDROID_TV,
        CHROME_DESKTOP,
        FIREFOX_DESKTOP,
        SAFARI_MAC
    )

    fun random(): String = list.random()
}
