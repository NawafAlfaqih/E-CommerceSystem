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

    public ArrayList<MerchantStock> getMerchantStocks() {
        return merchantStocks;
    }

    public void addMerchantStock(MerchantStock merchantStock) {
        if(checkMerchantID(merchantStock) && checkProductID(merchantStock))  {
            merchantStocks.add(merchantStock);
        }
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

    public boolean checkProductID(MerchantStock merchantStock) { //helper Method for productID validation
        for (Product p: productService.products) {
            if(merchantStock.getProductID().equals(p.getID())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkMerchantID(MerchantStock merchantStock) { //helper Method for merchantID validation
        for (Merchant m: merchantService.merchants) {
            if(merchantStock.getMerchantID().equals(m.getID())) {
                return true;
            }
        }
        return false;
    }
}
