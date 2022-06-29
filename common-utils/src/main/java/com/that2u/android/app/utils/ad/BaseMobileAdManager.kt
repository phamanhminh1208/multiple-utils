package com.that2u.android.app.utils.ad

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

abstract class BaseMobileAdManager(var activity: Activity?) {
    protected var adNetworkManagers: MutableList<BaseAdNetworkManager?> = ArrayList()

    //live data
    var interstitialAdLoadedLiveData = MutableLiveData(false)
    var rewardedAdLoadedLiveData = MutableLiveData(false)

    //listener
    var onRewardedReceived: ((Double) -> Unit)? = null

    private var numberInitializedAdManager: Int = 0;

    fun initialize(adManagerClassList: List<Class<out BaseAdNetworkManager>>?) {
        if (activity != null && adManagerClassList != null && adManagerClassList.isNotEmpty()) {
            //create
            var networkIdx = 0
            for (clz in adManagerClassList) {
                try {
                    val adManager = clz.newInstance()
                    adManager.adStatusChangeListener =
                        createAdStatusChangedListener(activity, networkIdx++)
                    adNetworkManagers.add(adManager)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                }
            }

            //initialize
            for (adManager in adNetworkManagers) {
                adManager?.initialize(activity)
            }
        }
    }

    protected open fun createAdStatusChangedListener(
        context: Activity?,
        networkIndex: Int
    ): AdStatusChangeListener {
        return object : AdStatusChangeListener {
            override fun onInitialized() {
                numberInitializedAdManager++;
                if(numberInitializedAdManager == adNetworkManagers.size){
                    if(loadBannerAdAfterInitialized()){
                        loadBannerAd()
                    }
                }
            }

            override fun onBannerAdStatusChanged(isLoaded: Boolean) {
                if(!isLoaded){
                    if (adNetworkManagers.size > networkIndex + 1) {
                        if (adNetworkManagers[networkIndex + 1] != null) {
                            adNetworkManagers[networkIndex + 1]!!.loadBannerAd(context)
                        }
                    }
                }
            }

            override fun onInterstitialAdStatusChanged(isLoaded: Boolean) {
                interstitialAdLoadedLiveData.postValue(isLoaded)
            }

            override fun onRewardedVideoAdStatusChanged(isLoaded: Boolean) {
                rewardedAdLoadedLiveData.postValue(isLoaded)
            }

            override fun onBannerAdFailToLoad() {
                if (adNetworkManagers.size > networkIndex + 1) {
                    if (adNetworkManagers[networkIndex + 1] != null) {
                        adNetworkManagers[networkIndex + 1]!!.loadBannerAd(context)
                    }
                }
            }

            override fun onInterstitialAdFailToLoad() {
                if (adNetworkManagers.size > networkIndex + 1) {
                    if (adNetworkManagers[networkIndex + 1] != null) {
                        adNetworkManagers[networkIndex + 1]!!.loadInterstitialAd(context)
                    }
                }
            }

            override fun onRewardedVideoAdFailToLoad() {
                if (adNetworkManagers.size > networkIndex + 1) {
                    if (adNetworkManagers[networkIndex + 1] != null) {
                        adNetworkManagers[networkIndex + 1]!!.loadRewardedVideoAd(context)
                    }
                }
            }

            override fun onRewardedVideoAdClosed() {
                loadRewardedVideoAd()
            }

            override fun onRewardReceived(amount: Double) {
                onRewardedReceived?.invoke(amount)
            }
        }
    }

    open fun onResume() {
        if (activity != null) {
            for (adManager in adNetworkManagers) {
                adManager?.onResume(activity)
            }
        }
    }

    open fun onDestroy() {
        onRewardedReceived = null

        for (adNetworkManager in adNetworkManagers) {
            adNetworkManager?.onDestroy()
        }

        activity = null
    }

    fun finish(){
        for (adManager in this.adNetworkManagers){
            adManager?.finish()
        }
    }

    // -- interstitial --
    open fun canShowInterstitialAd(): Boolean{
        activity?.let { context ->
            for (adNetworkManager in adNetworkManagers) {
                if(adNetworkManager?.canShowInterstitialAd(context) == true){
                    return true
                }
            }
        }
        return false
    }

    open fun showInterstitialAd() {
        activity?.let { context ->
            for (adNetworkManager in adNetworkManagers) {
                if (adNetworkManager?.canShowInterstitialAd(context) == true) {
                    adNetworkManager.showInterstitialAd(context)
                    break
                }
            }
        }
    }

    open fun loadInterstitialAd() {
        activity?.let { context ->
            if (adNetworkManagers.size > 0) {
                adNetworkManagers[0]?.loadInterstitialAd(context)
            }
        }
    }

    // -- rewarded --
    open fun canShowRewardedVideoAd(): Boolean{
        activity?.let { context ->
            for (adNetworkManager in adNetworkManagers) {
                if(adNetworkManager?.canShowRewardedVideoAd(context) == true){
                    return true
                }
            }
        }
        return false
    }

    open fun showRewardedVideoAd() {
        activity?.let { context ->
            for (adNetworkManager in adNetworkManagers) {
                if (adNetworkManager?.canShowRewardedVideoAd(context) == true) {
                    adNetworkManager.showRewardedVideoAd(context)
                    break
                }
            }
        }
    }

    open fun loadRewardedVideoAd() {
        activity?.let { context ->
            if (adNetworkManagers.size > 0) {
                adNetworkManagers[0]?.loadRewardedVideoAd(context)
            }
        }
    }

    // -- banner --
    open fun canShowRandomBannerAd(): Boolean = false
    open fun loadBannerAdAfterInitialized(): Boolean = false

    open fun loadBannerAd() {
        activity?.let { context ->
            if(canShowRandomBannerAd()){
                var count = 0;
                for (adNetwork in adNetworkManagers){
                    if(adNetwork?.canShowBannerAd(context) == true){
                        count++
                    }
                }

                val showIdx = Random.nextInt(count)
                var idx = 0;
                for (adNetwork in adNetworkManagers){
                    if(adNetwork?.canShowBannerAd(context) == true){
                        if(idx == showIdx){
                            adNetwork.loadBannerAd(context)
                        }else{
                            adNetwork.hideBannerAd(context)
                        }
                        idx++
                    }
                }
            }else{
                for (adNetwork in adNetworkManagers){
                    if (adNetwork?.canShowBannerAd(context) == true) {
                        adNetwork.loadBannerAd(context)
                    }
                }
            }
        }
    }
}