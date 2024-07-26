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
import com.nvd.footballmanager.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<CustomApiResponse> handleEmailTaken() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(CustomApiResponse.forbidden("The email you provided is already in use!"));
    }

    @PostMapping("/register")
    public CustomApiResponse register(@RequestBody @Valid UserRegistration userRegistration) {
        UserDTO userDTO = userService.registerUser(userRegistration);

        return CustomApiResponse.created(userDTO);
    }


    @ExceptionHandler({UserDoesNotExistException.class})
    public ResponseEntity<CustomApiResponse> handleUserDoesntExist() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomApiResponse
                .forbidden("The user you are looking for does not exist!"));
    }


    @ExceptionHandler({EmailFaildToSendException.class})
    public ResponseEntity<CustomApiResponse> handleFaildEmail() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomApiResponse
                .forbidden("Email failed to send, try again in a moment!"));
    }

    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody Map<String, String> body) {
        userService.generateEmailVerification(body.get("username"));
        return new ResponseEntity<>("Verification code generated, email sent!", HttpStatus.OK);
    }


    @ExceptionHandler({IncorrectVerificationCodeException.class})
    public ResponseEntity<CustomApiResponse> incorrectCodeHandler() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(CustomApiResponse
                .badRequest("The verification code provided is incorrect "));
    }

    @PostMapping("/email/verify")
    public CustomApiResponse verifyEmail(@RequestBody Map<String, String> body) {
        Long code = Long.parseLong(body.get("code"));
        String username = body.get("username");
        UserDTO userDTO = userService.verifyEmail(username, code);
        return CustomApiResponse.created(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse> login(@RequestBody @Valid UserLogin userLogin) {

        LoginResponse loginResponse;
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
            String accessToken = tokenService.generateAccessToken(auth);
            String refreshToken = tokenService.generateRefreshToken(auth);
            loginResponse = new LoginResponse(accessToken, refreshToken);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .body(CustomApiResponse.success(loginResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.unauthorized(Constants.INVALID_USERNAME_PASSWORD));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
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

//    @PostMapping("/logout")
//    public ResponseEntity<CustomApiResponse> logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            customLogoutHandler.logout(request, response, auth);
//        }
//        return ResponseEntity.ok(CustomApiResponse.success("Logged out successfully"));
//    }
}
