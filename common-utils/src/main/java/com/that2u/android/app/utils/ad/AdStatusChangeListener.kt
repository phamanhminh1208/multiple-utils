package com.that2u.android.app.utils.ad

interface AdStatusChangeListener {
    fun onInitialized()

    fun onBannerAdStatusChanged(isLoaded: Boolean)
    fun onInterstitialAdStatusChanged(isLoaded: Boolean)
    fun onRewardedVideoAdStatusChanged(isLoaded: Boolean)

    fun onBannerAdFailToLoad()
    fun onInterstitialAdFailToLoad()
    fun onRewardedVideoAdFailToLoad()

    fun onRewardedVideoAdClosed()
    fun onRewardReceived(amount: Double)
}