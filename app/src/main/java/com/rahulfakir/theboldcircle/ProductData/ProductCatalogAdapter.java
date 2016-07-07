package com.rahulfakir.theboldcircle.ProductData;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahulfakir.theboldcircle.R;

import java.util.List;


public class ProductCatalogAdapter extends RecyclerView.Adapter<ProductCatalogAdapter.ViewHolder> {
    private List<ProductObject> productList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, brand, description, price, availableUnits, variantStatus;
        public ImageView imgProductButton;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvProductName);
            description = (TextView) view.findViewById(R.id.tvProductDescription);
            brand = (TextView) view.findViewById(R.id.tvBrandName);
            price = (TextView) view.findViewById(R.id.tvPrice);
            availableUnits = (TextView) view.findViewById(R.id.tvAvailableUnits);
            variantStatus = (TextView) view.findViewById(R.id.tvVariantStatus);
            imgProductButton = (ImageView) view.findViewById(R.id.imgProductThumbnail);
        }
    }


    public ProductCatalogAdapter(List<ProductObject> productList) {
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductObject product = productList.get(position);
        holder.name.setText(product.getName());
        holder.brand.setText(product.getBrand());
        //   holder.description.setText(product.getDescription());

        holder.price.setText("R" + product.getBasePrice());
        if (product.getType() == 0) {
            if (product.getStock() >= 10) {
                holder.availableUnits.setText("In Stock");
            } else {
                holder.availableUnits.setText("- " + String.valueOf(product.getStock()) + " units left");
            }
        } else {
            holder.availableUnits.setText("Service offered");
        }

        if (product.getVariantStatus()) {
            holder.variantStatus.setText("Different options available");
        } else {
            holder.variantStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


}
