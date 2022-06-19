package com.that2u.android.app.utils.ad

import com.that2u.android.app.utils.ad.AdStatusChangeListener
import android.app.Activity

abstract class BaseAdNetworkManager {
    var adStatusChangeListener: AdStatusChangeListener? = null

    constructor() {}
    constructor(adStatusChangeListener: AdStatusChangeListener?) {
        this.adStatusChangeListener = adStatusChangeListener
    }

    abstract fun initialize(context: Activity?)
    abstract fun onResume(context: Activity?)
    abstract fun destroy()
    abstract fun canShowInterstitialAd(context: Activity?): Boolean
    abstract fun loadInterstitialAd(context: Activity?)
    abstract fun showInterstitialAd(context: Activity?)
    abstract fun canShowRewardedVideoAd(context: Activity?): Boolean
    abstract fun loadRewardedVideoAd(context: Activity?)
    abstract fun showRewardedVideoAd(context: Activity?)
    abstract fun loadBannerAd(context: Activity?)
}