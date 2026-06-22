package com.jasper.dungeontrackerbackend.config;

import com.jasper.dungeontrackerbackend.config.annotations.AuthenticatedUser;
import com.jasper.dungeontrackerbackend.entities.User;
import com.jasper.dungeontrackerbackend.services.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthenticatedUserResolver implements HandlerMethodArgumentResolver {
    private final JWTService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // This resolver targets parameters explicitly marked with our annotation and matching the User type
        return parameter.hasParameterAnnotation(AuthenticatedUser.class)
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Optional<User> userOpt = jwtService.extractUser(token);

        // If token is expired, forged, or user was deleted from DB, bounce them back with a 401
        return userOpt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired authentication token"));
    }
}
