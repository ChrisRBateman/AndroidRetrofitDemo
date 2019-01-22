package xyz.cbateman.bestbuy;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import xyz.cbateman.bestbuy.adapter.ProductsAdapter;
import xyz.cbateman.bestbuy.model.Product;
import xyz.cbateman.bestbuy.model.SearchResults;
import xyz.cbateman.bestbuy.service.BestBuyService;
import xyz.cbateman.bestbuy.util.Constants;
import xyz.cbateman.bestbuy.util.DialogUtil;

/**
 * Application entry point.
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        Callback<SearchResults>,
        ProductsAdapter.ClickListener {

    public static final String TAG = Constants.TAG;

    private Button mSearchButton;
    private EditText mSearchEdit;
    private ProgressBar mProgressBar;

    private ArrayList<Product> mProducts = new ArrayList<>();
    private ProductsAdapter mProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "MainActivity:onCreate");

        mSearchButton = findViewById(R.id.search_button);
        if (mSearchButton != null) {
            mSearchButton.setOnClickListener(this);
        }
        mSearchEdit = findViewById(R.id.search_edit);
        mProgressBar = findViewById(R.id.progress_bar);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mProductsAdapter = new ProductsAdapter(this, mProducts);
        mProductsAdapter.setClickListener(this);
        recyclerView.setAdapter(mProductsAdapter);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    // View.OnClickListener ------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_button) {
            mProgressBar.setVisibility(View.VISIBLE);
            mSearchButton.setEnabled(false);

            String searchTerm = mSearchEdit.getText().toString().trim();
            performSearch(searchTerm);
        }
    }

    // Callback<SearchResults> ---------------------------------------------------------------------

    @Override
    public void onResponse(@NonNull Call<SearchResults> call,
                           @NonNull Response<SearchResults> response) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mSearchButton.setEnabled(true);
        if(response.isSuccessful()) {
            SearchResults searchResults = response.body();
            Log.i(TAG, "MainActivity:onResponse searchResults = " + searchResults);
            if (searchResults != null) {
                List<Product> products = Arrays.asList(searchResults.products);
                mProducts.clear();
                mProducts.addAll(products);
                mProductsAdapter.notifyDataSetChanged();
            }
        } else {
            showLoadingError();
        }
    }

    @Override
    public void onFailure(@NonNull Call<SearchResults> call, @NonNull Throwable t) {
        String trace = Log.getStackTraceString(t);
        Log.i(TAG, "MainActivity:onFailure trace = " + trace);

        mProgressBar.setVisibility(View.INVISIBLE);
        mSearchButton.setEnabled(true);
        showLoadingError();
    }

    // ProductsAdapter.ClickListener ---------------------------------------------------------------

    @Override
    public void onItemClicked(int position) {
        Log.i(TAG, "MainActivity:onItemClicked position = " + position);

        Product product = mProducts.get(position);
        String sku = product.sku;
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(Constants.SKU, sku);
        startActivity(intent);
    }

    // Private methods -----------------------------------------------------------------------------

    /**
     * Perform a search based on search parameter.
     *
     * @param searchTerm the search parameter
     */
    private void performSearch(String searchTerm) {
        String lang = Locale.getDefault().getLanguage();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        BestBuyService service = retrofit.create(BestBuyService.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("lang", lang);
        map.put("query", searchTerm);
        Call<SearchResults> call = service.getProducts(map);

        Log.i(TAG, "MainActivity:performSearch url = " + call.request().url().toString());
        call.enqueue(this);
    }

    /**
     * Show the products loading error message.
     */
    private void showLoadingError() {
        Resources res = getResources();
        DialogUtil.showOKMessage(this, res.getString(R.string.products_load_error_text));
    }
}
