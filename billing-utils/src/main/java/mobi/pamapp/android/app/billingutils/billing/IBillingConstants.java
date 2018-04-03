package mobi.pamapp.android.app.billingutils.billing;

import com.android.billingclient.api.BillingClient;

import java.util.List;

import mobi.pamapp.android.app.billingutils.billing.items.BillingItem;

/**
 * Created by phama on 3/24/2018.
 */

public interface IBillingConstants {
    List<String> getSkuList(@BillingClient.SkuType String billingType);

    List<BillingItem> getBillingItemList(@BillingClient.SkuType String billingType);

    /* BASE_64_ENCODED_PUBLIC_KEY should be YOUR APPLICATION'S PUBLIC KEY
     * (that you got from the Google Play developer console). This is not your
     * developer public key, it's the *app-specific* public key.
     *
     * Instead of just storing the entire literal string here embedded in the
     * program,  construct the key at runtime from pieces or
     * use bit manipulation (for example, XOR with some other string) to hide
     * the actual key.  The key itself is not secret information, but we don't
     * want to make it easy for an attacker to replace the public key with one
     * of their own and then fake messages from the server.
     */
    String getBillingAPIPublicKey();
}
