package com.that2u.android.app.utils.ad

interface AdStatusChangeListener {
    fun onInterstitialAdStatusChanged(isLoaded: Boolean)
    fun onRewardedVideoAdStatusChanged(isLoaded: Boolean)

    fun onInterstitialAdFailToLoad()
    fun onRewardedVideoAdFailToLoad()

    fun onRewardedVideoAdClosed()
    fun onRewardReceived(amount: Double)
}