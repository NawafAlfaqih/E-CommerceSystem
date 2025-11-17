package org.example.ecommercesystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.Model.Category;
import org.example.ecommercesystem.Model.Product;
import org.example.ecommercesystem.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ProductService {

    ArrayList<Product> products = new ArrayList<>();

    private final CategoryService categoryService;
    private final UserService userService;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public boolean addProduct(Product product) {
        if(checkCategoryID(product.getCategoryID()))  {
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

    public boolean checkCategoryID(String categoryID) { //helper Method for categoryID validation
        for (Category c: categoryService.categories) {
            if(categoryID.equals(c.getID())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkUserID(String userID) {
        for (User u: userService.users) {
            if (userID.equals(u.getID())){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Product> getLowestProductsInPrice(String categoryID) {
        ArrayList<Product> products1 = new ArrayList<>(products);
        products1.removeIf(p -> !categoryID.equals(p.getCategoryID()));
        products1.sort(Comparator.comparingDouble(Product::getPrice)); //sorting from the lowest price to highest
        return products1;
    }

    public ArrayList<Product> getHighestProductsInPrice(String categoryID) {
        ArrayList<Product> products1 = new ArrayList<>(products);
        products1.removeIf(p -> !categoryID.equals(p.getCategoryID()));
        products1.sort(Comparator.comparingDouble(Product::getPrice).reversed()); //sorting from the lowest price to highest then reversing them
        return products1;
    }

    public ArrayList<Product> getProductsInPriceRange(String categoryID, double min, double max) {
        ArrayList<Product> products1 = new ArrayList<>(products);
        products1.removeIf(p -> !categoryID.equals(p.getCategoryID()));
        products1.removeIf(p -> p.getPrice() > max || p.getPrice() < min);
        return products1;
    }

    public double getAvgPriceInCategory(String categoryID) {

        double avg = 0;
        for (Product p: products){
            avg += p.getPrice();
        }
        return avg;
    }

    public ArrayList<Product> recommendProductsFromCategory(String userID, String categoryID) {

        double balance = userService.getUserByID(userID).getBalance();
        ArrayList<Product> products1 = new ArrayList<>(products);
        products1.removeIf(p -> !categoryID.equals(p.getCategoryID()));

        if (balance > 1000){
            products1.removeIf(p -> p.getPrice() > balance || p.getPrice() < getAvgPriceInCategory(categoryID));
        } else if (balance > 200) {
            products1.removeIf(p -> p.getPrice() > balance || p.getPrice() < (getAvgPriceInCategory(categoryID)/ 1.2));
        } else {
            products1.removeIf(p -> p.getPrice() > balance);
        }

        return products1;
    }
}
