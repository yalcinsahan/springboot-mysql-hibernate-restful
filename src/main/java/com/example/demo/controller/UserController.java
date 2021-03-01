package com.example.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/first/{firstname}")
    public List<User> getByFirsyName(@PathVariable("firstname") String firstname){
        return userRepository.findAllByFirstName(firstname);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable(value = "id") Long userId) throws ResourceNotFoundException{
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceClosedException("User not found on :: "+ userId));

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(value = "id") Long userId,
            @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: "+ userId));

        user.setEmailId(userDetails.getEmailId());
        user.setLastName(userDetails.getLastName());
        user.setFirstName(userDetails.getFirstName());
        user.setUpdatedAt(new Date());
        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{id}")
    public Map<String, Boolean> deleteUser(
            @PathVariable(value = "id") Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: "+ userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


}
