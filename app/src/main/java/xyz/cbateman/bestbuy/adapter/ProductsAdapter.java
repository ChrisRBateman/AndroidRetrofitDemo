package xyz.cbateman.bestbuy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xyz.cbateman.bestbuy.R;
import xyz.cbateman.bestbuy.model.Product;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    public interface ClickListener {
        void onItemClicked(int position);
    }
    private ClickListener mClickListener;

    private Context mContext;
    private List<Product> mProducts;

    static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        private final ImageView productImageView;
        private final TextView nameTextView;
        private final TextView priceTextView;
        private ProductsAdapter adapter;

        ViewHolder(View v, ProductsAdapter adapter) {
            super(v);

            productImageView = v.findViewById(R.id.product_image);
            nameTextView = v.findViewById(R.id.name);
            priceTextView = v.findViewById(R.id.price);

            this.adapter = adapter;

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        ImageView getProductImageView() {
            return productImageView;
        }

        TextView getNameTextView() {
            return nameTextView;
        }

        TextView getPriceTextView() {
            return priceTextView;
        }

        @Override
        public void onClick(View v) {
            if (adapter != null) {
                adapter.onItemClicked(getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return (adapter != null) && adapter.onItemLongClicked(getLayoutPosition());
        }
    }

    // Constructors --------------------------------------------------------------------------------

    /**
     * ProductsAdapter constructor.
     *
     * @param products list of Product objects
     */
    public ProductsAdapter(@NonNull Context context, @NonNull ArrayList<Product> products) {
        mContext = context;
        mProducts = products;
    }

    // Public Methods ------------------------------------------------------------------------------

    /**
     * Set the ClickListener.
     *
     * @param listener the ClickListener object
     */
    public void setClickListener(ClickListener listener) {
        mClickListener = listener;
    }

    /**
     * Bind product data to the item view.
     *
     * @param holder holds item view
     * @param position product position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if ((0 <= position) && (position < mProducts.size())) {
            Product product = mProducts.get(position);

            holder.getNameTextView().setText(product.name);
            holder.getPriceTextView()
                    .setText(String.format(Locale.US, "$%.2f", product.regularPrice));

            Glide.with(mContext)
                    .load(product.thumbnailImage)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder_nofilter))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.getProductImageView());
        }
    }

    /**
     * Returns the number of items.
     *
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    /**
     * Returns a ProductsAdapter.ViewHolder object.
     *
     * @param parent the ViewGroup object
     * @param viewType the view type
     * @return a ProductsAdapter.ViewHolder object
     */
    @Override
    @NonNull
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);

        return new ViewHolder(v, this);
    }

    // Private Methods -----------------------------------------------------------------------------

    /**
     * Responds to item click.
     *
     * @param position the position of item clicked
     */
    private void onItemClicked(int position) {
        if (mClickListener != null) {
            mClickListener.onItemClicked(position);
        }
    }

    /**
     * Returns true if item is long clicked.
     *
     * @param position the position of item
     * @return always returns true
     */
    @SuppressWarnings("unused")
    private boolean onItemLongClicked(int position) {
        return true;
    }
}
