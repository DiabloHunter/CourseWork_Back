package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ResponseDto;
import com.example.project.dto.order.OrderDtoItem;
import com.example.project.dto.user.SignInDto;
import com.example.project.dto.user.SignInReponseDto;
import com.example.project.dto.user.SignupDto;
import com.example.project.dto.user.UserDto;
import com.example.project.model.Category;
import com.example.project.model.User;
import com.example.project.service.AuthenticationService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;

    // two apis

    // signup

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupDto signupDto) {
        return userService.signUp(signupDto);
    }


    // signin

    @PostMapping("/signin")
    public SignInReponseDto signIn(@RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }

    @GetMapping("/signinMob/{email}&{password}")
    public SignInReponseDto signInMob(@PathVariable("email") String email,@PathVariable("password") String password) {
        SignInDto signInDto = new SignInDto(email, password);
        return userService.signInMob(signInDto);
    }


    @GetMapping("/")
    public UserDto getUser(@RequestParam("token") String token){
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        return userService.getUserDto(user);
    }

    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateCategory(@RequestParam("token") String token, @RequestBody User changedUser) {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        userService.editUser(user, changedUser);
        return new ResponseEntity<>(new ApiResponse(true, "User has been updated"), HttpStatus.OK);
    }

    @PostMapping("/backup")
    public ResponseEntity<ApiResponse> backupDB(@RequestParam("token") String token){
        authenticationService.authenticate(token);
        try {
            userService.backup();
        } catch (IOException | InterruptedException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Error:" + e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Database has been successfully backuped!"), HttpStatus.OK);
    }

    @PostMapping("/restore")
    public ResponseEntity<ApiResponse> restoreDB(@RequestParam("token") String token){
        authenticationService.authenticate(token);
        try {
            userService.restore();
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Error:" + e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Database has been successfully restored!"), HttpStatus.OK);
    }



}
