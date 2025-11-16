package org.example.ecommercesystem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.ApiResponse.ApiResponse;
import org.example.ecommercesystem.Model.MerchantStock;
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
        if (!merchantStockService.checkProductID(merchantStock)) {
            String message = "Merchant Stock productID was not found (productID: " + merchantStock.getProductID() + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        if (!merchantStockService.checkMerchantID(merchantStock)) {
            String message = "Merchant Stock merchantID was not found (merchantID: " + merchantStock.getMerchantID() + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
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
        if (!merchantStockService.checkProductID(merchantStock)) {
            String message = "Merchant Stock productID was not found (productID: " + merchantStock.getProductID() + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        if (!merchantStockService.checkMerchantID(merchantStock)) {
            String message = "Merchant Stock merchantID was not found (merchantID: " + merchantStock.getMerchantID() + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
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

}
