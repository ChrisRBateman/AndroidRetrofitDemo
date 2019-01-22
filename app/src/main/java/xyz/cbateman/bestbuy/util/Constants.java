package xyz.cbateman.bestbuy.util;

/**
 * Global constants.
 */
@SuppressWarnings("WeakerAccess")
public class Constants {

    public static final String TAG = "BestBuyRetrofitDemoTag";
    public static final String BASE_URL = "http://www.bestbuy.ca/";
    public static final String SKU = "xyz.cbateman.bestbuy.SKU";

    // These are the urls from the previous version.
    public static final String DOMAIN = "http://www.bestbuy.ca";
    public static final String SEARCH_URL = DOMAIN + "/api/v2/json/search?lang=%s&query=%s";
    public static final String PRODUCT_URL = DOMAIN + "/api/v2/json/product/%s?lang=%s";
}
