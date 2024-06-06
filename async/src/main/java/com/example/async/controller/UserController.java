package com.example.async.controller;

import com.example.async.entity.User;
import com.example.async.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(value = "/users" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam (value = "files") MultipartFile[] files){

        for(MultipartFile file:files){
            userService.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();


    }

    @GetMapping(value="/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAll(){
        return userService.fetchListOfUsers().thenApply(ResponseEntity::ok);

    }
}
