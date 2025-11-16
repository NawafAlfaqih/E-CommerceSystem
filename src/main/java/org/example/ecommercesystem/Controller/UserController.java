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
}
