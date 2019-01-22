package xyz.cbateman.bestbuy.model;

import android.support.annotation.NonNull;

/**
 * Class stores product detail info.
 */
@SuppressWarnings("WeakerAccess")
public class ProductDetail {

    public String sku = null;
    public String name = null;
    public Double regularPrice = 0.0;
    public String shortDescription = null;
    public String thumbnailImage = null;

    private ProductDetail() {
    }

    @Override
    @NonNull
    public String toString() {
        return "sku : " + sku + " name : " + name +
                " thumbnailImage : " + thumbnailImage + " regularPrice : " + regularPrice +
                " shortDescription : " + shortDescription;
    }
}
