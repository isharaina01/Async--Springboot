package com.example.async.service;

import com.example.async.entity.User;
import com.example.async.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    Logger logger= LoggerFactory.getLogger(UserService.class);

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file){
        long start= System.currentTimeMillis();
        List<User> users=parseCsvFile(file);
        logger.info("Saving list of users of size {}", users.size(), Thread.currentThread().getName());
        userRepository.saveAll(users);
        long end=System.currentTimeMillis();
        logger.info("Total Time taken {}",(end-start));
        return CompletableFuture.completedFuture(users);


    }

    @Async
    public CompletableFuture<List<User>> fetchListOfUsers(){
        logger.info("Get list of users by"+ Thread.currentThread().getName());

        List<User> users= userRepository.findAll();
        return CompletableFuture.completedFuture((users));
    }
//Sorting
   public List<User> getListOfUsersWithSorting(String field){
        return userRepository.findAll(Sort.by(Sort.Direction.ASC,field));
   }

   //Pagination
   public Page<User> getListOfUsersWithPagination(int offset,int pageSize){
       return userRepository.findAll(PageRequest.of(offset, pageSize));

   }

    List<User> parseCsvFile(final MultipartFile file){
        List<User> users=new ArrayList<>();
        try(final BufferedReader br=new BufferedReader(new InputStreamReader(file.getInputStream()))) {
        String line;
        while ((line=br.readLine())!=null){
            final String [] data=line.split(",");
            final User user=new User();
            user.setName(data[0]);
            user.setEmailId(data[1]);
            user.setGender(data[2]);
            users.add(user);


        }
        return  users;
        } catch (IOException e) {
            logger.error("Failed to parse csv file ",e);
            throw new RuntimeException(e);
        }
    }
}
