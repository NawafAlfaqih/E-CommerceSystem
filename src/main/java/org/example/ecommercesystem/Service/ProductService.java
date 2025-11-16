package org.example.ecommercesystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.Model.Category;
import org.example.ecommercesystem.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
@RequiredArgsConstructor
public class ProductService {

    ArrayList<Product> products = new ArrayList<>();

    private final CategoryService categoryService;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public boolean addProduct(Product product) {
        if(checkCategoryID(product))  {
            products.add(product);
            return true;
        }
        return false;
    }

    public boolean updateProduct(String ID, Product product) {
        for(Product p: products) {
            if (ID.equals(p.getID())) {
                products.set(products.indexOf(p), product);
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(String ID) {
        for(Product p: products) {
            if (ID.equals(p.getID())) {
                products.remove(p);
                return true;
            }
        }
        return false;
    }

    public boolean checkCategoryID(Product product) { //helper Method for categoryID validation
        for (Category c: categoryService.categories) {
            if(product.getCategoryID().equals(c.getID())) {
                return true;
            }
        }
        return false;
    }
}
