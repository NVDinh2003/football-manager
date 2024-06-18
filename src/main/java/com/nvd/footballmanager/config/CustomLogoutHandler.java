package com.nvd.footballmanager.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;


@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Clear the security context
        SecurityContextHolder.clearContext();

        // Additional logic to invalidate JWT token, if applicable
        // For example, add the token to a blacklist or update the user record to invalidate token

        response.setStatus(HttpServletResponse.SC_OK);
    }
}