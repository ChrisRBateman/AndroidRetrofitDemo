package xyz.cbateman.bestbuy.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import xyz.cbateman.bestbuy.model.ProductDetail;
import xyz.cbateman.bestbuy.model.SearchResults;

/**
 * BestBuyService defines methods for retrieving list of products and product detail.
 */
public interface BestBuyService {

    /**
     * The options map should contain 'lang' and 'query' values.
     */
    @GET("api/v2/json/search")
    Call<SearchResults> getProducts(@QueryMap Map<String, String> options);

    @GET("api/v2/json/product/{sku}")
    Call<ProductDetail> getProductDetail(@Path("sku") String sku, @Query("lang") String lang);
}
