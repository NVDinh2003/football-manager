package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.config.CustomLogoutHandler;
import com.nvd.footballmanager.dto.CustomApiResponse;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final CustomLogoutHandler customLogoutHandler;
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<String> handleEmailTaken() {
        return new ResponseEntity<>("The email you provided is already in use!", HttpStatus.CONFLICT);
    }

    @PostMapping("/register")
    public CustomApiResponse register(@RequestBody @Valid UserRegistration userRegistration) {
        UserDTO userDTO = userService.registerUser(userRegistration);

        return CustomApiResponse.created(userDTO);
    }

    @ExceptionHandler({UserDoesNotExistException.class})
    public ResponseEntity<String> handleUserDoesntExist() {
        return new ResponseEntity<>("The user you are looking for does not exist!", HttpStatus.NOT_FOUND);
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
    public CustomApiResponse verifyEmail(@RequestBody LinkedHashMap<String, String> body) {
        Long code = Long.parseLong(body.get("code"));
        String username = body.get("username");
        UserDTO userDTO = userService.verifyEmail(username, code);
        return CustomApiResponse.created(userDTO);
    }

    @PostMapping("/login")
    public CustomApiResponse login(@RequestBody @Valid UserLogin userLogin) {

        LoginResponse loginResponse;
//
//        return ApiResponse.created(loginResponse);
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
//            UserDTO userDTO = userService.getUserByUsername(userLogin.getUsername());
            String accessToken = tokenService.generateAccessToken(auth);
            String refreshToken = tokenService.generateRefreshToken(auth);
            loginResponse = new LoginResponse(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            loginResponse = new LoginResponse("", "");
        }
        return CustomApiResponse.created(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody LinkedHashMap<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (tokenService.validateToken(refreshToken)) {
            String username = tokenService.getUsernameFromToken(refreshToken);

            // Create a new Authentication object with the username from the refresh token
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, null);

            // Generate new access token
            String newAccessToken = tokenService.generateAccessToken(authentication);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        customLogoutHandler.logout(request, response, null);
    }
}
