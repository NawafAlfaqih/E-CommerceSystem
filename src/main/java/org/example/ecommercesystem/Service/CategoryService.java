package org.example.ecommercesystem.Service;

import org.example.ecommercesystem.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    ArrayList<Category> categories = new ArrayList<>();

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public boolean updateCategory(String ID, Category category) {
        for(Category c: categories) {
            if (ID.equals(c.getID())) {
                categories.set(categories.indexOf(c), category);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCategory(String ID) {
        for(Category c: categories) {
            if (ID.equals(c.getID())) {
                categories.remove(c);
                return true;
            }
        }
        return false;
    }

}
