package com.ra.service;

import com.ra.model.dto.request.AccountRequest;
import com.ra.model.dto.request.SignInRequest;
import com.ra.model.dto.request.SignUpRequest;
import com.ra.model.dto.response.AccountResponse;
import com.ra.model.dto.response.SignInResponse;
import com.ra.model.entity.M5User;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import com.ra.util.exception.UnAuthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
   String signUp(SignUpRequest signUpRequest) throws NotFoundException, BadRequestException;
   SignInResponse signIn (SignInRequest signInRequest) throws NotFoundException, UnAuthorizedException;
   AccountResponse account(M5User user);
   AccountResponse updateAccount(M5User user, AccountRequest account);
   AccountResponse changePassword(M5User user,AccountRequest accountRequest);

   Page<M5User> findAll(Pageable pageable, String sort, String col);
   Page<M5User> findAllByUserName(Pageable pageable, String sort, String col,String key);
   M5User findUserById(Long uid) throws NotFoundException;

   M5User updateUserStatus(Long uid) throws NotFoundException, UnAuthorizedException;


   ResponseEntity<?> confirmEmail(String confirmationToken);


   ResponseEntity<?> saveUser(SignUpRequest signUpRequest) throws BadRequestException;
}
