package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.exceptions.FileErrorException;
import com.nvd.footballmanager.exceptions.UnableToUpLoadPhotoException;
import com.nvd.footballmanager.filters.BaseFilter;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.repository.UserRepository;
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
public class UserController extends BaseController<User, UserDTO, BaseFilter, UUID> {
    private final UserService userService;
    private final UserRepository userRepository;

    protected UserController(UserService userService, UserRepository userRepository) {
        super(userService);
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public CustomApiResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = userService.getUserByUsername(authentication.getName());
//        Object c = authentication.getPrincipal();
        return CustomApiResponse.success(user);
    }


    @ExceptionHandler({UnableToUpLoadPhotoException.class})
    public ResponseEntity<CustomApiResponse> handleUploadImage() {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(CustomApiResponse
                .badRequest("Săm thing roong to upload avatar!"));
    }

    @ExceptionHandler({FileErrorException.class})
    public ResponseEntity<CustomApiResponse> handleEmailTaken(FileErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomApiResponse
                .badRequest(ex.getMessage()));
    }

    @PutMapping("/upload-image")
    @PreAuthorize("isAuthenticated()")
    public CustomApiResponse upload(
            @RequestParam("image") MultipartFile file,
            Authentication authentication) {
        UserDTO userDTO = userService.getUserByUsername(authentication.getName());

        return CustomApiResponse.success(userService.uploadImage(userDTO.getId(), file));
    }


    //    @Operation(summary = "Retrieve all Users", tags = {"users", "get"}, security = @SecurityRequirement(name = "bearerAuth"))
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
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomApiResponse> findAll(BaseFilter filter) {
        return super.findAll(filter);
    }

}
