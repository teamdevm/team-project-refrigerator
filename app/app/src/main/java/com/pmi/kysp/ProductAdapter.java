package com.pmi.kysp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private static List<Product> products = new ArrayList<>();
    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, Product product);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ProductAdapter.clickListener = clickListener;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productNameTextView;
        ImageView productImageView;
        AppCompatButton productExpDate;
        public ProductViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            productImageView = itemView.findViewById(R.id.product_item__image);
            productNameTextView = itemView.findViewById(R.id.product_item__name);
            productExpDate = itemView.findViewById(R.id.product_item__exp_date);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
            clickListener.onItemClick(position, products.get(position));
        }

        public void bind(Product product)
        {
            productNameTextView.setText(product.getProductName());

            byte[] decodedString = Base64.decode(product.getImage(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            productImageView.setImageBitmap(bitmap);

            productExpDate.setText(product.getExpDateString());

            if (product.isExpired())
                productExpDate.setActivated(true);
            else
                productExpDate.setActivated(false);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(Collection<Product> newCategories)
    {
        products.clear();
        products.addAll(newCategories);
        notifyDataSetChanged();
    }
}
