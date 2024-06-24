package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.exceptions.BadRequestException;
import com.nvd.footballmanager.exceptions.FileErrorException;
import com.nvd.footballmanager.exceptions.UnableToUpLoadPhotoException;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")  // swagger api require JWT
@Tag(name = "Users Controller", description = "User APIs")
public class UserController
        extends BaseController<User, UserDTO, UUID> {
    private final UserService userService;

    protected UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> handleEmailTaken(BadRequestException ex) {
        return new ResponseEntity<>("error-users:" + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/me")
    public CustomApiResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = userService.getUserByUsername(authentication.getName());
//        Object c = authentication.getPrincipal();
        return CustomApiResponse.success(user);
    }


    @ExceptionHandler({UnableToUpLoadPhotoException.class})
    public ResponseEntity<String> handleEmailTaken() {
        return new ResponseEntity<>("Săm thing roong to upload avatar!", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({FileErrorException.class})
    public ResponseEntity<String> handleEmailTaken(FileErrorException ex) {
        return new ResponseEntity<>("error:" + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/upload-image")
    @PreAuthorize("isAuthenticated()")
    public CustomApiResponse upload(
            @RequestParam("image") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByUsername(authentication.getName());
        return CustomApiResponse.success(userService.uploadImage(userDTO.getId(), file));
    }


    //    @Operation(summary = "Retrieve all Users", tags = {"users", "get"}, security = @SecurityRequirement(name = "bearerAuth"))
    @Override
    @Operation(summary = "Retrieve all Users", tags = {"users", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users", content = {
                    @Content(schema = @Schema(implementation = CustomApiResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "There are no Users", content = {
                    @Content(schema = @Schema(implementation = CustomApiResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Forbidden for non-admin users", content = {
                    @Content(schema = @Schema(implementation = CustomApiResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema(implementation = CustomApiResponse.class), mediaType = "application/json")})
    })
    @GetMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomApiResponse> findAll() {
        return super.findAll();
    }

}
