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
        Product product = merchantStockService.getProductByID(merchantStock.getProductID());
        if (product == null)
            return ResponseEntity.status(404).body(
                    new ApiResponse("Merchant Stock 'productID' was not found (productID: " + merchantStock.getProductID() + ")."));

        Merchant merchant = merchantStockService.getMerchantByID(merchantStock.getMerchantID());
        if (merchant == null)
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
        Product product = merchantStockService.getProductByID(merchantStock.getProductID());
        if (product == null)
            return ResponseEntity.status(404).body(
                    new ApiResponse("Merchant Stock 'productID' was not found (productID: " + merchantStock.getProductID() + ")."));

        Merchant merchant = merchantStockService.getMerchantByID(merchantStock.getMerchantID());
        if (merchant == null)
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
        if (stock <= 0)
            return ResponseEntity.status(400).body(new ApiResponse("Stock must be more than '0' (stock: " + stock + ")."));

        Product product = merchantStockService.getProductByID(productID);
        if (product == null)
            return ResponseEntity.status(404).body(new ApiResponse("Merchant Stock 'productID' was not found (productID: " + productID + ")."));

        Merchant merchant = merchantStockService.getMerchantByID(merchantID);
        if (merchant == null)
            return ResponseEntity.status(404).body(new ApiResponse("Merchant Stock 'merchantID' was not found (merchantID: " + merchantID + ")."));

        if (merchantStockService.addStock(merchantID, productID, stock))
            return ResponseEntity.status(200).body(new ApiResponse("Stock of '"+stock+"' added successfully (pID: " + productID + ", mID: " + merchantID + ")."));

        return ResponseEntity.status(404).body(new ApiResponse("Merchant Stock was not found (pID: " + productID + ", mID: " + merchantID + ")."));
    }

    @PutMapping("/buy/{userID}/{merchantID}/{productID}")
    public ResponseEntity<?> buyProduct(@PathVariable String userID, @PathVariable String productID, @PathVariable String merchantID) {
        User user = merchantStockService.getUserByID(userID);
        if (user == null)
            return ResponseEntity.status(404).body(new ApiResponse("User not found (userID: " + userID + ")."));

        Product product = merchantStockService.getProductByID(productID);
        if (product == null)
            return ResponseEntity.status(404).body(new ApiResponse("Product not found (productID: " + productID + ")."));

        Merchant merchant = merchantStockService.getMerchantByID(merchantID);
        if (merchant == null)
            return ResponseEntity.status(404).body(new ApiResponse("Merchant not found (merchantID: " + merchantID + ")."));

        if (user.getBalance() < product.getPrice())
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance (balance: " + user.getBalance() + ")."));

        if (!merchantStockService.checkStock(merchantID, productID))
            return ResponseEntity.status(400).body(new ApiResponse("Stock is insufficient."));

        if (merchantStockService.buyProduct(userID, productID, merchantID))
            return ResponseEntity.ok(new ApiResponse("Product bought successfully."));

        return ResponseEntity.status(404).body(new ApiResponse("Merchant stock not found."));
    }

    @GetMapping("/get/low-stock")
    public ResponseEntity<?> getLowInStock() {
        return ResponseEntity.status(200).body(merchantStockService.getLowInStock());
    }

    @GetMapping("/get/empty-stock")
    public ResponseEntity<?> getEmptyInStock() {
        return ResponseEntity.status(200).body(merchantStockService.getEmptyInStock());
    }

}
