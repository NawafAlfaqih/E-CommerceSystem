package org.example.ecommercesystem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.ApiResponse.ApiResponse;
import org.example.ecommercesystem.Model.Product;
import org.example.ecommercesystem.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
        if (!productService.checkCategoryID(product)) {
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
}
