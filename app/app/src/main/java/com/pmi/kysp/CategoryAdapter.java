package com.pmi.kysp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private static List<Category> categories = new ArrayList<>();
    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, Category category);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CategoryAdapter.clickListener = clickListener;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView categoryTextView;
        public CategoryViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            categoryTextView = itemView.findViewById(R.id.category_text);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
            clickListener.onItemClick(position, categories.get(position));
        }

        public void bind(Category category)
        {
            categoryTextView.setText(category.getName());
            categoryTextView.setActivated(category.isChosen);
        }
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(Collection<Category> newCategories)
    {
        categories.clear();
        categories.addAll(newCategories);
        notifyDataSetChanged();
    }

    public void changeStateCategory(int position)
    {
        boolean isChosen = categories.get(position).isChosen;
        categories.get(position).isChosen = !isChosen;
        notifyDataSetChanged();
    }
}
