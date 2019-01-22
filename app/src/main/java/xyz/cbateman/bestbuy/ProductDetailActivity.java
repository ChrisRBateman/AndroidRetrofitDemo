package xyz.cbateman.bestbuy;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import xyz.cbateman.bestbuy.model.ProductDetail;
import xyz.cbateman.bestbuy.service.BestBuyService;
import xyz.cbateman.bestbuy.util.Constants;
import xyz.cbateman.bestbuy.util.DialogUtil;

/**
 * ProductDetailActivity uses sku from intent to load and display a product.
 */
@SuppressWarnings("ConstantConditions")
public class ProductDetailActivity extends AppCompatActivity implements
        View.OnClickListener,
        Callback<ProductDetail> {

    public static final String TAG = Constants.TAG;

    private TextView mNameTextView;
    private ImageView mProductImageView;
    private TextView mPriceTextView;
    private TextView mShortDescTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mNameTextView = findViewById(R.id.name);
        mProductImageView = findViewById(R.id.product_image);
        mPriceTextView = findViewById(R.id.price);
        mShortDescTextView = findViewById(R.id.description);
        Button addCartButton = findViewById(R.id.add_cart_button);
        if (addCartButton != null) {
            addCartButton.setOnClickListener(this);
        }
        mProgressBar = findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        String sku = intent.getStringExtra(Constants.SKU);

        Log.i(TAG, "ProductDetailActivity:onCreate : sku = " + sku);

        if ((sku != null) && (sku.length() > 0)) {
            performSearch(sku);
        }
    }

    // View.OnClickListener ------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.add_cart_button) {
            Resources res = getResources();
            DialogUtil.showOKMessage(this, res.getString(R.string.add_product_to_cart_text));
        }
    }

    // Callback<ProductDetail> ---------------------------------------------------------------------

    @Override
    public void onResponse(@NonNull Call<ProductDetail> call,
                           @NonNull Response<ProductDetail> response) {
        if(response.isSuccessful()) {
            mProgressBar.setVisibility(View.GONE);
            ProductDetail productDetail = response.body();
            Log.i(TAG, "ProductDetailActivity:onResponse productDetail = " + productDetail);

            mNameTextView.setText(productDetail.name);

            Glide.with(this)
                    .load(productDetail.thumbnailImage)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder_nofilter))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mProductImageView);

            mPriceTextView.setText(String.format(Locale.US, "$%.2f", productDetail.regularPrice));
            mShortDescTextView.setText(productDetail.shortDescription);
        } else {
            showLoadingError();
        }
    }

    @Override
    public void onFailure(@NonNull Call<ProductDetail> call, @NonNull Throwable t) {
        String trace = Log.getStackTraceString(t);
        Log.i(TAG, "ProductDetailActivity:onFailure trace = " + trace);

        showLoadingError();
    }

    // Private methods -----------------------------------------------------------------------------

    /**
     * Perform a search based on sku parameter.
     *
     * @param sku the search parameter
     */
    private void performSearch(String sku) {
        String lang = Locale.getDefault().getLanguage();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        BestBuyService service = retrofit.create(BestBuyService.class);

        Call<ProductDetail> call = service.getProductDetail(sku, lang);
        call.enqueue(this);
    }

    /**
     * Show the product loading error message.
     */
    private void showLoadingError() {
        mProgressBar.setVisibility(View.GONE);
        Resources res = getResources();
        DialogUtil.showOKMessage(this, res.getString(R.string.product_load_error_text));
    }
}
