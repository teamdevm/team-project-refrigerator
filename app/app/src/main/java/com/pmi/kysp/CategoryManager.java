package com.pmi.kysp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryManager {
    private static int ALL_CATEGORY_ID = 0;
    private List<Product> products;
    private List<Category> categories;
    private HashMap<Integer, Boolean> checkedCategories;

    public CategoryManager(List<Product> products, List<Category> categories)
    {
        this.products = new ArrayList<>();
        this.categories = new ArrayList<>();

        this.products.addAll(products);
        this.categories.addAll(categories);

        checkedCategories = new HashMap<>();

        categories.forEach(c -> {
            checkedCategories.put(c.getCategoryId(), false);
        });

        checkedCategories.put(ALL_CATEGORY_ID, true);
    }

    public void changeStateOfCategory(int categoryId)
    {
        categories.get(categoryId).changeState();
        boolean isChecked = checkedCategories.get(categoryId);
        checkedCategories.put(categoryId, !isChecked);
    }

    public List<Product> getProducts()
    {
        List<Product> result = new ArrayList<>();
        boolean isAllChecked = checkedCategories.get(ALL_CATEGORY_ID);
        if (isAllChecked)
        {
            result.addAll(products);
            return result;
        }

        products.forEach(p -> {
            int productCategoryId = p.getCategoryId();
            if (checkedCategories.get(productCategoryId))
            {
                result.add(p);
            }
        });

        return result;
    }

    public List<Category> getCategories()
    {
        return categories;
    }
}
