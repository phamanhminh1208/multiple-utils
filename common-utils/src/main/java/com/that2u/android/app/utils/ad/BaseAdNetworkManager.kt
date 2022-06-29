package com.that2u.android.app.utils.ad

import android.app.Activity

abstract class BaseAdNetworkManager {
    var adStatusChangeListener: AdStatusChangeListener? = null

    constructor() {}
    constructor(adStatusChangeListener: AdStatusChangeListener?) {
        this.adStatusChangeListener = adStatusChangeListener
    }

    abstract fun initialize(context: Activity)
    abstract fun onResume(context: Activity)
    abstract fun onDestroy()
    abstract fun finish()

    abstract fun canShowInterstitialAd(context: Activity): Boolean
    abstract fun loadInterstitialAd(context: Activity)
    abstract fun showInterstitialAd(context: Activity)

    abstract fun canShowRewardedVideoAd(context: Activity): Boolean
    abstract fun loadRewardedVideoAd(context: Activity)
    abstract fun showRewardedVideoAd(context: Activity)

    abstract fun canShowBannerAd(context: Activity): Boolean
    abstract fun hideBannerAd(context: Activity)
    abstract fun loadBannerAd(context: Activity)
}