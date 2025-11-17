package org.example.ecommercesystem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.ApiResponse.ApiResponse;
import org.example.ecommercesystem.Model.User;
import org.example.ecommercesystem.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        userService.addUser(user);
        String message = "User added successfully (ID: "+user.getID()+").";
        return ResponseEntity.status(201).body(new ApiResponse(message));
    }

    @PutMapping("/update/id/{ID}")
    public ResponseEntity<?> updateUser(@PathVariable String ID, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        if (userService.updateUser(ID, user)) {
            String message = "User updated successfully (ID: " + user.getID() + ").";
            return ResponseEntity.status(200).body(new ApiResponse(message));
        }
        String message = "User was not found (ID: " + user.getID() + ").";
        return ResponseEntity.status(404).body(new ApiResponse(message));
    }

    @DeleteMapping("/delete/id/{ID}")
    public ResponseEntity<?> deleteUser(@PathVariable String ID) {
        if (userService.deleteUser(ID)) {
            String message = "User deleted successfully (ID: " + ID + ").";
            return ResponseEntity.status(200).body(new ApiResponse(message));
        }
        String message = "User was not found (ID: " + ID + ").";
        return ResponseEntity.status(404).body(new ApiResponse(message));
    }

    @GetMapping("/get/otp/email/{email}")
    public ResponseEntity<?> generateOTP(@PathVariable String email) {
        String OTP = userService.generateOTP(email);
        if (OTP.isBlank()) {
            String message = "User was not found (email: " + email + ").";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        String message = "OTP for password reset issued successfully (OTP: " + OTP + ").";
        return ResponseEntity.status(200).body(new ApiResponse(message));
    }

    @PutMapping("/reset/otp/{OTP}/password/{password}")
    public ResponseEntity<?> resetPassword(@PathVariable String OTP, @PathVariable String password) {
        String message = "";
        switch (userService.resetPassword(OTP, password)) {
            case -1:
                message = "OTP was not correct (OTP: " + OTP + ").";
                return ResponseEntity.status(404).body(new ApiResponse(message));
            case -2:
                message = "provided password is the same as the old one.";
                return ResponseEntity.status(404).body(new ApiResponse(message));
            case -3:
                message = "password must be at least 7 characters and include: uppercase, lowercase, digit, and special character (@$!%*?&)";
                return ResponseEntity.status(404).body(new ApiResponse(message));
            default:
                message = "password reset is successful (OTP: " + OTP + ").";
                return ResponseEntity.status(200).body(new ApiResponse(message));
        }
    }
}
