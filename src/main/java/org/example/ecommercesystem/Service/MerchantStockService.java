package org.example.ecommercesystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.Model.*;
import org.example.ecommercesystem.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    private final ProductService productService;
    private final MerchantService merchantService;
    private final UserService userService;

    public ArrayList<MerchantStock> getMerchantStocks() {
        return merchantStocks;
    }

    public void addMerchantStock(MerchantStock merchantStock) {
            merchantStocks.add(merchantStock);
    }

    public boolean updateMerchantStock(String ID, MerchantStock merchantStock) {
        for(MerchantStock p: merchantStocks) {
            if (ID.equals(p.getID())) {
                merchantStocks.set(merchantStocks.indexOf(p), merchantStock);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchantStock(String ID) {
        for(MerchantStock p: merchantStocks) {
            if (ID.equals(p.getID())) {
                merchantStocks.remove(p);
                return true;
            }
        }
        return false;
    }

    public Product getProductByID(String productID) { //helper Method for productID validation and object search
        for (Product p: productService.products) {
            if(productID.equals(p.getID())) {
                return p;
            }
        }
        return null;
    }

    public Merchant getMerchantByID(String merchantID) { //helper Method for merchantID validation and object search
        for (Merchant m: merchantService.merchants) {
            if(merchantID.equals(m.getID())) {
                return m;
            }
        }
        return null;
    }

    public User getUserByID(String userID) { //helper Method for userID validation and object search
        for (User u: userService.users) {
            if(userID.equals(u.getID())) {
                return u;
            }
        }
        return null;
    }

    public boolean checkStock(String merchantID, String productID) {
        for (MerchantStock m: merchantStocks) {
            if (m.getMerchantID().equals(merchantID) && m.getProductID().equals(productID)) {
                if (m.getStock() > 0)
                    return true;
            }
        }
        return false;
    }

    public boolean checkLowInStock(MerchantStock merchantStock) {
        return merchantStock.getStock() < 6 && merchantStock.getStock() > 0;
    }

    public boolean checkEmptyInStock(MerchantStock merchantStock) {
        return merchantStock.getStock() <= 0;
    }

    public ArrayList<Product> getLowInStock(String categoryID) {
        ArrayList<Product> products = new ArrayList<>();

        for (MerchantStock m: merchantStocks) {
            if (checkLowInStock(m)) {
                for (Product p: productService.products)
                    if (m.getProductID().equals(p.getID())) {
                        products.add(p);
                        break;
                    }
            }
        }
        products.removeIf(p -> !categoryID.equals(p.getCategoryID()));
        return products;
    }

    public ArrayList<Product> getEmptyInStock(String categoryID) {
        ArrayList<Product> products = new ArrayList<>();

        for (MerchantStock m: merchantStocks) {
            if (checkEmptyInStock(m)) {
                for (Product p: productService.products)
                    if (m.getProductID().equals(p.getID())) {
                        products.add(p);
                        break;
                    }
            }
        }
        products.removeIf(p -> !categoryID.equals(p.getCategoryID()));
        return products;
    }

    public Merchant getBestMerchantForProduct(String productID) {
        Merchant bestMerchant = null;
        int highestStock = -1;

        for (MerchantStock m : merchantStocks) {
            if (productID.equals(m.getProductID())) {

                if (m.getStock() > highestStock) {
                    highestStock = m.getStock();
                    bestMerchant = getMerchantByID(m.getMerchantID());
                }
            }
        }
        return bestMerchant;
    }


    public int addStock(String merchantID, String productID, int stock) {

        if (stock <= 0)
            return -1;

        Product product = getProductByID(productID);
        if (product == null)
            return -2;

        if (getMerchantByID(merchantID) == null)
            return -3;

        for (MerchantStock m: merchantStocks) {

            if (m.getMerchantID().equals(merchantID) && m.getProductID().equals(productID)) {
                m.setStock(m.getStock() + stock);

                return 1;
            }
        }
        return -4;
    }

    public int buyProduct(String userID, String productID, String merchantID) {

        User user = getUserByID(userID);
        if (user == null)
            return -1;

        Product product = getProductByID(productID);
        if (product == null)
            return -2;

        if (getMerchantByID(merchantID) == null)
            return -3;

        if (user.getBalance() < product.getPrice())
            return -4;

        if (!checkStock(merchantID, productID))
            return -5;

        for (MerchantStock m : merchantStocks) {
            if (m.getMerchantID().equals(merchantID) && m.getProductID().equals(productID)) {

                user.setBalance(user.getBalance() - product.getPrice());
                m.setStock(m.getStock() - 1);

                return 1;
            }
        }
        return -6;
    }
}
