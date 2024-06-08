package com.example.async.controller;

import com.example.async.dto.ApiResponse;
import com.example.async.entity.User;
import com.example.async.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(value="/users/{field}", produces = "application/json")
    public ApiResponse<List<User>> findAll(@PathVariable String field){
        List<User> listOfUsersWithSorting = userService.getListOfUsersWithSorting(field);
        return new ApiResponse<>(listOfUsersWithSorting.size(),listOfUsersWithSorting);

    }

    @GetMapping(value="/users/{offset}/{pageSize}", produces = "application/json")
    private ApiResponse<Page<User>> findAll(@PathVariable int offset, @PathVariable int pageSize){
        Page<User> listOfUsersWithPagination = userService.getListOfUsersWithPagination(offset, pageSize);
        return  new ApiResponse<>(listOfUsersWithPagination.getSize(),listOfUsersWithPagination);

    }
}
