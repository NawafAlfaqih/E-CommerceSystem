package org.example.ecommercesystem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.ApiResponse.ApiResponse;
import org.example.ecommercesystem.Model.Merchant;
import org.example.ecommercesystem.Model.MerchantStock;
import org.example.ecommercesystem.Model.Product;
import org.example.ecommercesystem.Model.User;
import org.example.ecommercesystem.Service.MerchantStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchantStocks() {
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStocks());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        if (merchantStockService.getProductByID(merchantStock.getProductID()) == null)
            return ResponseEntity.status(404).body(
                    new ApiResponse("Merchant Stock 'productID' was not found (productID: " + merchantStock.getProductID() + ")."));

        if (merchantStockService.getMerchantByID(merchantStock.getMerchantID()) == null)
            return ResponseEntity.status(404).body(
                    new ApiResponse("Merchant Stock 'merchantID' was not found (merchantID: " + merchantStock.getMerchantID() + ")."));

        merchantStockService.addMerchantStock(merchantStock);
        String message = "Merchant Stock added successfully (ID: " + merchantStock.getID() + ").";
        return ResponseEntity.status(201).body(new ApiResponse(message));
    }

    @PutMapping("/update/id/{ID}")
    public ResponseEntity<?> updateMerchantStock(@PathVariable String ID, @RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        if (merchantStockService.getProductByID(merchantStock.getProductID()) == null)
            return ResponseEntity.status(404).body(
                    new ApiResponse("Merchant Stock 'productID' was not found (productID: " + merchantStock.getProductID() + ")."));

        if (merchantStockService.getMerchantByID(merchantStock.getMerchantID()) == null)
            return ResponseEntity.status(404).body(
                    new ApiResponse("Merchant Stock 'merchantID' was not found (merchantID: " + merchantStock.getMerchantID() + ")."));

        if (merchantStockService.updateMerchantStock(ID,merchantStock)) {
            String message = "MerchantStock updated successfully (ID: " + merchantStock.getID() + ").";
            return ResponseEntity.status(200).body(new ApiResponse(message));
        }
        String message = "MerchantStock was not found (ID: " + merchantStock.getID() + ").";
        return ResponseEntity.status(404).body(new ApiResponse(message));
    }

    @DeleteMapping("/delete/id/{ID}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String ID) {
        if (merchantStockService.deleteMerchantStock(ID)) {
            String message = "Merchant Stock deleted successfully (ID: " + ID + ").";
            return ResponseEntity.status(200).body(new ApiResponse(message));
        }
        String message = "Merchant Stock was not found (ID: " + ID + ").";
        return ResponseEntity.status(404).body(new ApiResponse(message));
    }

    @PutMapping("/update/stock/{stock}/{merchantID}/{productID}")
    public ResponseEntity<?> addStock(@PathVariable String merchantID, @PathVariable String productID, @PathVariable int stock) {
        return switch (merchantStockService.addStock(merchantID, productID, stock)) {
            case -1 ->
                    ResponseEntity.status(400).body(new ApiResponse("Stock must be more than '0' (stock: " + stock + ")."));
            case -2 ->
                    ResponseEntity.status(404).body(new ApiResponse("Product not found (productID: " + productID + ")."));
            case -3 ->
                    ResponseEntity.status(404).body(new ApiResponse("Merchant not found (merchantID: " + merchantID + ")."));
            case -4 ->
                    ResponseEntity.status(404).body(new ApiResponse("Merchant Stock was not found (pID: " + productID + ", mID: " + merchantID + ")."));
            default ->
                    ResponseEntity.status(200).body(new ApiResponse("Stock of '"+stock+"' added successfully (pID: " + productID + ", mID: " + merchantID + ")."));
        };
    }

    @PutMapping("/buy/{userID}/{merchantID}/{productID}")
    public ResponseEntity<?> buyProduct(@PathVariable String userID, @PathVariable String productID, @PathVariable String merchantID) {
        return switch (merchantStockService.buyProduct(userID, productID, merchantID)) {
            case -1 ->
                    ResponseEntity.status(404).body(new ApiResponse("User not found (userID: " + userID + ")."));
            case -2 ->
                    ResponseEntity.status(404).body(new ApiResponse("Product not found (productID: " + productID + ")."));
            case -3 ->
                    ResponseEntity.status(404).body(new ApiResponse("Merchant not found (merchantID: " + merchantID + ")."));
            case -4 ->
                    ResponseEntity.status(400).body(new ApiResponse("Insufficient balance (balance: " + merchantStockService.getUserByID(userID).getBalance() + ")."));
            case -5 ->
                    ResponseEntity.status(400).body(new ApiResponse("Stock is insufficient."));
            case -6 ->
                    ResponseEntity.status(404).body(new ApiResponse("Merchant stock not found."));
            default ->
                    ResponseEntity.status(200).body(new ApiResponse("Product bought successfully."));
        };
    }

    @GetMapping("/get/low-stock/{categoryID}")
    public ResponseEntity<?> getLowInStock(@PathVariable String categoryID) {
        ArrayList<Product> products = merchantStockService.getLowInStock(categoryID);
        if (products.isEmpty()) {
            String message = "No Products are low in stock in given category (categoryID: " + categoryID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        return ResponseEntity.status(200).body(products);
    }

    @GetMapping("/get/empty-stock/{categoryID}")
    public ResponseEntity<?> getEmptyInStock(@PathVariable String categoryID) {
        ArrayList<Product> products = merchantStockService.getEmptyInStock(categoryID);
        if (products.isEmpty()) {
            String message = "No Products have empty stock in given category (categoryID: " + categoryID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        return ResponseEntity.status(200).body(products);
    }

    @GetMapping("/get/best-merchant/product/{productID}")
    public ResponseEntity<?> getBestMerchant(@PathVariable String productID) {
        if (merchantStockService.getProductByID(productID) == null) {
            String message = "Product was not found (productID: " + productID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        if (merchantStockService.getBestMerchantForProduct(productID) == null) {
            String message = "No merchant is selling this product with available stock (productID: " + productID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        return ResponseEntity.status(200).body(merchantStockService.getBestMerchantForProduct(productID));
    }
}
