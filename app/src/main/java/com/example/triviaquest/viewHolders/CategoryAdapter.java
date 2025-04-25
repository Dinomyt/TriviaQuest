package com.example.triviaquest.viewHolders;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triviaquest.R;
import com.example.triviaquest.database.entities.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList, onCategoryClickListener listener) {
        this.categoryList = categoryList;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(row);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        // Bind data to your ViewHolder
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        // Return the size of your data set
        return categoryList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Category> categories) {
        this.categoryList = categories;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryNameTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
        }

        public void bind(Category category) {
            categoryNameTextView.setText(category.getName());

        }
    }

    public interface onCategoryClickListener {
        void onCategoryClick(Category category);
    }
}
