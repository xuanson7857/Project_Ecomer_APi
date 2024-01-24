package com.ra.controller;

import com.ra.model.dto.request.SignInRequest;
import com.ra.model.dto.request.SignUpRequest;
import com.ra.model.entity.M5User;
import com.ra.service.UserService;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import com.ra.util.exception.UnAuthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp( @Valid @RequestBody SignUpRequest signUp , BindingResult bindingResult) throws NotFoundException, BadRequestException {
       if( bindingResult.hasErrors()){
           List<ObjectError> errors = bindingResult.getAllErrors();
           List<String> errorMessages = new ArrayList<>();
           for (ObjectError error : errors) {
               errorMessages.add(error.getDefaultMessage());
           }

           return ResponseEntity.badRequest().body(errorMessages);
       }

        if(signUp.getPassword().equals(signUp.getConfirmPassword()) ){
            return new ResponseEntity<>(userService.signUp(signUp), HttpStatus.CREATED);

        }
        else {    return ResponseEntity.badRequest().body("Mật khẩu không trung khớp với comfixPassword");

        }


    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest,BindingResult bindingResult) throws BadRequestException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : errors) {
                errorMessages.add(error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errorMessages);
        }

        if (signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            return new ResponseEntity<>(userService.saveUser(signUpRequest), HttpStatus.OK);

        } else {
            return ResponseEntity.badRequest().body("Mật khẩu không trung khớp với comfixPassword");

        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signIn) throws NotFoundException, UnAuthorizedException {

        return new ResponseEntity<>(userService.signIn(signIn),HttpStatus.OK);
    }
    @GetMapping(value="/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        return userService.confirmEmail(confirmationToken);
    }

}

