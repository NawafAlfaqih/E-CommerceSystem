package org.example.ecommercesystem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.ApiResponse.ApiResponse;
import org.example.ecommercesystem.Model.Product;
import org.example.ecommercesystem.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.status(200).body(productService.getProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        if (productService.addProduct(product)) {
            String message = "Product added successfully (ID: " + product.getID() + ").";
            return ResponseEntity.status(201).body(new ApiResponse(message));
        }
        String message = "Product categoryID was not found (categoryID: " + product.getCategoryID() + ").";
        return ResponseEntity.status(404).body(new ApiResponse(message));
    }

    @PutMapping("/update/id/{ID}")
    public ResponseEntity<?> updateProduct(@PathVariable String ID, @RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        if (!productService.checkCategoryID(product.getCategoryID())) {
            String message = "Product categoryID was not found (categoryID: " + product.getCategoryID() + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        if (productService.updateProduct(ID,product)) {
            String message = "Product updated successfully (ID: " + product.getID() + ").";
            return ResponseEntity.status(200).body(new ApiResponse(message));
        }
        String message = "Product was not found (ID: " + product.getID() + ").";
        return ResponseEntity.status(404).body(new ApiResponse(message));
    }

    @DeleteMapping("/delete/id/{ID}")
    public ResponseEntity<?> deleteProduct(@PathVariable String ID) {
        if (productService.deleteProduct(ID)) {
            String message = "Product deleted successfully (ID: " + ID + ").";
            return ResponseEntity.status(200).body(new ApiResponse(message));
        }
        String message = "Product was not found (ID: " + ID + ").";
        return ResponseEntity.status(404).body(new ApiResponse(message));
    }

    @GetMapping("/get/lowest-price/category/{categoryID}")
    public ResponseEntity<?> getLowestProductsInPrice(@PathVariable String categoryID) {
        if (!productService.checkCategoryID(categoryID)) {
            String message = "Product categoryID was not found (categoryID: " + categoryID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        return ResponseEntity.status(200).body(productService.getLowestProductsInPrice(categoryID));
    }

    @GetMapping("/get/highest-price/category/{categoryID}")
    public ResponseEntity<?> getHighestProductsInPrice(@PathVariable String categoryID) {
        if (!productService.checkCategoryID(categoryID)) {
            String message = "Product categoryID was not found (categoryID: " + categoryID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        return ResponseEntity.status(200).body(productService.getHighestProductsInPrice(categoryID));
    }

    @GetMapping("/get/in-range/{categoryID}/price/{min}/{max}")
    public ResponseEntity<?> getProductsInPriceRange(@PathVariable String categoryID, @PathVariable double min, @PathVariable double max) {
            if (!productService.checkCategoryID(categoryID)) {
                String message = "Product categoryID was not found (categoryID: " + categoryID + ").";
                return ResponseEntity.status(404).body(new ApiResponse(message));
            }
            ArrayList<Product> products = productService.getProductsInPriceRange(categoryID, min, max);
            if (products.isEmpty()){
                String message = "Products in range ('"+min+"', '"+max+"') was not found (categoryID: " + categoryID + ").";
                return ResponseEntity.status(404).body(new ApiResponse(message));
            }
            return ResponseEntity.status(200).body(products);
    }

    @GetMapping("/get/avg-price/category/{categoryID}")
    public ResponseEntity<?> getAvgPriceInCategory(@PathVariable String categoryID) {
        if (!productService.checkCategoryID(categoryID)) {
            String message = "Product categoryID was not found (categoryID: " + categoryID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        String message = "The average price in this category is '"+productService.getAvgPriceInCategory(categoryID)+"' (categoryID: "+ categoryID +"),";
        return ResponseEntity.status(200).body(new ApiResponse(message));
    }

    @GetMapping("/recommend/user/{userID}/category/{categoryID}")
    public ResponseEntity<?> recommendProductsFromCategory(@PathVariable String userID, @PathVariable String categoryID) {
        if(!productService.checkUserID(userID)) {
            String message = "userID was not found (userID: " + userID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        if (!productService.checkCategoryID(categoryID)) {
            String message = "Product categoryID was not found (categoryID: " + categoryID + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        return ResponseEntity.status(200).body(productService.recommendProductsFromCategory(userID, categoryID));
    }

    @GetMapping("/best-deals")
    public ResponseEntity<?> bestDealsFromCategories() {
        ArrayList<Product> products = productService.bestDealsFromCategories();
        if (products.isEmpty()) {
            String message = "No Best Deals to be shown.";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        return ResponseEntity.status(200).body(products);
    }
}
