package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.request.auth.UserRegistration;
import com.nvd.footballmanager.dto.response.CloudinaryResponse;
import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.exceptions.*;
import com.nvd.footballmanager.mappers.UserMapper;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.model.enums.UserRole;
import com.nvd.footballmanager.repository.UserRepository;
import com.nvd.footballmanager.service.auth.MailService;
import com.nvd.footballmanager.service.auth.TokenService;
import com.nvd.footballmanager.service.cloud.CloudinaryService;
import com.nvd.footballmanager.utils.Constants;
import com.nvd.footballmanager.utils.FileUploadUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService extends BaseService<User, UserDTO, UUID> implements UserDetailsService {
    //    @Value("${app.upload.dir}")
//    private static String uploadDir;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final TokenService tokenService;
    private final CloudinaryService cloudinaryService;


    protected UserService(UserRepository userRepository,
                          UserMapper userMapper,
                          PasswordEncoder passwordEncoder,
                          MailService mailService,
                          TokenService tokenService,
                          CloudinaryService cloudinaryService
    ) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.tokenService = tokenService;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public UserDTO registerUser(UserRegistration userRegistration) {
        User user = userMapper.convertForRegister(userRegistration);
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.setRole(UserRole.USER);

        String name = user.getName();
        boolean nameTaken = true;
        String tempName = "";
        while (nameTaken) {
            tempName = generateUserName(name);
            if (userRepository.findByUsername(tempName).isEmpty()) {
                nameTaken = false;
            }
        }
        user.setUsername(tempName);

        try {
            userRepository.save(user);
            return userMapper.convertToDTO(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public static String generateUserName(String name) {
        String temp = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String[] parts = pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("đ", "d").split(" ");
        long generateNumber = (long) Math.floor(Math.random() * 1_000_000);
        return parts[parts.length - 1] + generateNumber;
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        return userMapper.convertToDTO(user);
    }

    @Transactional
    public void generateEmailVerification(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        user.setVerificationCode(generateVerificationNumber());

        try {
            mailService.sendMail(user.getEmail(), "Your verification code",
                    "Here is your verification code: " + user.getVerificationCode());
            userRepository.save(user);
        } catch (Exception e) {
            throw new EmailFaildToSendException();
        }
    }


    private Long generateVerificationNumber() {
        return (long) Math.floor(Math.random() * 100_000_000);
    }

    @Transactional
    public UserDTO verifyEmail(String username, Long code) {
        User user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        if (code.equals(user.getVerificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return userMapper.convertToDTO(user);
        } else {
            throw new IncorrectVerificationCodeException();
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Chuyển đổi UserRole sang GrantedAuthority
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // Tạo UserDetails từ thông tin user (auth)
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );

        return userDetails;
    }

    private void validateUserUpdate(UUID id, UserDTO userRequest) {
        User existingUser = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);

        if (!existingUser.getEnabled()) {
            throw new BadRequestException(Constants.NOT_ACTIVE);
        }

        validateEmail(existingUser, userRequest.getEmail());
        validateUsername(existingUser, userRequest.getUsername());
        validatePhoneNumber(existingUser, userRequest.getPhoneNumber());
    }

    private void validateEmail(User existingUser, String newEmail) {
        if (!existingUser.getEmail().equals(newEmail)) {
            User userWithEmail = userRepository.findByEmail(newEmail);
            if (userWithEmail != null) {
                throw new BadRequestException(Constants.EMAIL_ALREADY_EXISTS);
            }
        }
    }

    private void validateUsername(User existingUser, String newUsername) {
        if (!existingUser.getUsername().equals(newUsername)) {
            Optional<User> userWithUsername = userRepository.findByUsername(newUsername);
            if (userWithUsername.isPresent()) {
                throw new BadRequestException(Constants.USERNAME_ALREADY_EXISTS);
            }
        }
    }

    private void validatePhoneNumber(User existingUser, String phoneNumber) {
        Optional<User> userWithPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);

        if (existingUser.getPhoneNumber() == null && userWithPhoneNumber.isPresent())
            throw new BadRequestException(Constants.PHONE_NUMBER_IS_ALREADY_IN_USE);
        if (!existingUser.getPhoneNumber().equals(phoneNumber) && userWithPhoneNumber.isPresent())
            throw new BadRequestException(Constants.PHONE_NUMBER_IS_ALREADY_IN_USE);
    }


    @Override
    @Transactional
    public UserDTO update(UUID uuid, UserDTO request) {
        validateUserUpdate(uuid, request);
        return super.update(uuid, request);
    }

    private static final File DIRECTORY = new File("D:\\WorkSpace\\Spring_Project\\FootballManager\\img");

//    @Transactional(rollbackOn = Exception.class)
//    public UserDTO saveImage(UUID id, MultipartFile file) throws UnableToUpLoadPhotoException {
//        User currentUser = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);
//        if (!DIRECTORY.exists())
//            DIRECTORY.mkdirs();
//        try {
//            // the content type form the request looks something like this img/jpeg
//            String extension = "." + file.getContentType().split("/")[1];
//            File img = File.createTempFile("avatar_" + id, extension, DIRECTORY);
//            String iname = img.getName();
//            file.transferTo(img);
////                String imageURL = URL + img.getName();
//            // Cập nhật avatar cho user
//            currentUser.setAvatar(img.getName());
//            User updatedUser = userRepository.save(currentUser);
//
//            return userMapper.convertToDTO(updatedUser);
//        } catch (IOException e) {
//            throw new UnableToUpLoadPhotoException();
//        }
//    }

    @Transactional
    public UserDTO uploadImage(UUID id, MultipartFile file) {
        User currentUser = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);

        FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
        final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
        final CloudinaryResponse response = this.cloudinaryService.uploadImage(file, "user/avatar/" + fileName);
        currentUser.setAvatar(response.getUrl());
        return userMapper.convertToDTO(userRepository.save(currentUser));
    }

    public UserDTO getCurrentUser() {
        UserDTO user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username != null) {
            try {
                user = userMapper.convertToDTO(userRepository.findByUsername(username).orElse(null));
            } catch (Exception e) {
                user = null;
            }
        }
        return user;
    }
}
