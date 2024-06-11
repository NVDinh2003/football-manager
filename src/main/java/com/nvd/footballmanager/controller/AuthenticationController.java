package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.ApiResponse;
import com.nvd.footballmanager.dto.request.auth.UserLogin;
import com.nvd.footballmanager.dto.request.auth.UserRegistration;
import com.nvd.footballmanager.dto.response.auth.LoginResponse;
import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.exceptions.EmailAlreadyTakenException;
import com.nvd.footballmanager.exceptions.EmailFaildToSendException;
import com.nvd.footballmanager.exceptions.IncorrectVerificationCodeException;
import com.nvd.footballmanager.exceptions.UserDoesNotExistException;
import com.nvd.footballmanager.service.UserService;
import com.nvd.footballmanager.service.auth.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<String> handleEmailTaken() {
        return new ResponseEntity<>("The email you provided is already in use", HttpStatus.CONFLICT);
    }

    @PostMapping("/register")
    public ApiResponse register(@RequestBody @Valid UserRegistration userRegistration) {
        UserDTO userDTO = userService.registerUser(userRegistration);

        return ApiResponse.created(userDTO);
    }

    @ExceptionHandler({UserDoesNotExistException.class})
    public ResponseEntity<String> handleUserDoesntExist() {
        return new ResponseEntity<String>("The user you are looking for does not exist!", HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({EmailFaildToSendException.class})
    public ResponseEntity<String> handleFaildEmail() {
        return new ResponseEntity<>("Email failed to send, try again in a moment!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String, String> body) {
        userService.generateEmailVerification(body.get("username"));
        return new ResponseEntity<>("Verification code generated, email sent!", HttpStatus.OK);
    }

    @ExceptionHandler({IncorrectVerificationCodeException.class})
    public ResponseEntity<String> incorrectCodeHandler() {
        return new ResponseEntity<>("The verification code provided is incorrect! ", HttpStatus.CONFLICT);
    }

    @PostMapping("/email/verify")
    public ApiResponse verifyEmail(@RequestBody LinkedHashMap<String, String> body) {
        Long code = Long.parseLong(body.get("code"));
        String username = body.get("username");
        UserDTO userDTO = userService.verifyEmail(username, code);
        return ApiResponse.created(userDTO);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid UserLogin userLogin) {

        LoginResponse loginResponse;
//
//        return ApiResponse.created(loginResponse);
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
            String token = tokenService.generateToken(auth);
            loginResponse = new LoginResponse(userService.getUserByUsername(userLogin.getUsername()), token);
        } catch (AuthenticationException e) {
            loginResponse = new LoginResponse(null, "");
        }
        return ApiResponse.created(loginResponse);
    }
}
