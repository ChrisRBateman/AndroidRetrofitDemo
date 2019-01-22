package xyz.cbateman.bestbuy.model;

import android.support.annotation.NonNull;

/**
 * Class stores product info of search.
 */
@SuppressWarnings("WeakerAccess")
public class Product {

    public String sku = null;
    public String name = null;
    public String thumbnailImage = null;
    public Double regularPrice = 0.0;

    private Product() {
    }

    @Override
    @NonNull
    public String toString() {
        return "sku : " + sku + " name : " + name +
                " thumbnailImage : " + thumbnailImage + " regularPrice : " + regularPrice;
    }
}
