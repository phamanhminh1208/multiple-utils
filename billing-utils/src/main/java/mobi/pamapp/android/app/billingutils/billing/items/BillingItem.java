package mobi.pamapp.android.app.billingutils.billing.items;

import com.android.billingclient.api.BillingClient;

/**
 * Created by phama on 3/24/2018.
 */

public class BillingItem {
    private String name;
    private String sku;
    private @BillingClient.SkuType
    String type;
    private float price;
    private float numProduct;

    public BillingItem(String name, String sku, @BillingClient.SkuType String type, float price, float numProduct) {
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.type = type;
        this.numProduct = numProduct;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public @BillingClient.SkuType
    String getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public float getNumProduct() {
        return numProduct;
    }
}
