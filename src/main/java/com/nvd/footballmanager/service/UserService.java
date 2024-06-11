package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.request.auth.UserRegistration;
import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.exceptions.EmailAlreadyTakenException;
import com.nvd.footballmanager.exceptions.EmailFaildToSendException;
import com.nvd.footballmanager.exceptions.IncorrectVerificationCodeException;
import com.nvd.footballmanager.exceptions.UserDoesNotExistException;
import com.nvd.footballmanager.mappers.UserMapper;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.model.enums.UserRole;
import com.nvd.footballmanager.repository.UserRepository;
import com.nvd.footballmanager.service.auth.MailService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService extends BaseService<User, UserDTO, UUID> implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;


    protected UserService(UserRepository userRepository,
                          UserMapper userMapper,
                          PasswordEncoder passwordEncoder,
                          MailService mailService
    ) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

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

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
    }


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


    @Override
    public UserDTO update(UUID uuid, UserDTO request) {
//        validateEmailUpdate(uuid, request);

        return super.update(uuid, request);
    }
}
