package mobi.pamapp.android.app.billingutils.billing;

import com.android.billingclient.api.Purchase;

import java.util.List;

/**
 * Created by phama on 3/24/2018.
 */

public interface BillingStatusListener {
    void onBillingClientSetupSuccess();

    void onBillingStartFail(String sku);

    void onConsumeSuccess(String sku, String token, int responseCode);

    void onConsumeFail(String token, int responseCode);

    void onPurchaseFail(List<Purchase> token);

    void onPurchaseSuccess(String sku, String token);

    void onPurchasesUpdated(List<Purchase> purchaseList);
}
