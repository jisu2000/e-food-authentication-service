package com.subhadeep.e_food_authentication_service.security;

import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subhadeep.e_food_authentication_service.constant.UrlConstants;
import com.subhadeep.e_food_authentication_service.dto.ApiResponse;
import com.subhadeep.e_food_authentication_service.dto.ErrorResponse;
import com.subhadeep.e_food_authentication_service.exceptions.UnauthorizeException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(401)
                .error("Validation Fails")
                .subErrors(Arrays.asList("Missing Token"))
                .build();

        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            // looking good
            token = requestHeader.substring(7);
            if (jwtUtils.isTokenBlacklisted(token)) {
                throw new UnauthorizeException();
            }
            try {

                username = this.jwtUtils.getUsernameFromToken(token);

            } catch (IllegalArgumentException e) {
                errorResponse.setError("Validation Fails");
                errorResponse.setSubErrors(Arrays.asList(e.getMessage()));
                writeInResponse(errorResponse, response);
            } catch (ExpiredJwtException e) {
                errorResponse.setError("Validation Fails");
                errorResponse.setSubErrors(Arrays.asList(e.getMessage()));
                writeInResponse(errorResponse, response);
            } catch (MalformedJwtException e) {
                errorResponse.setError("Validation Fails");
                errorResponse.setSubErrors(Arrays.asList(e.getMessage()));
                writeInResponse(errorResponse, response);

            } catch (SignatureException e) {
                errorResponse.setError("Validation Fails");
                errorResponse.setSubErrors(Arrays.asList(e.getMessage()));
                writeInResponse(errorResponse, response);
            } catch (Exception e) {
                errorResponse.setError("Validation Fails");
                errorResponse.setSubErrors(Arrays.asList(e.getMessage()));
                writeInResponse(errorResponse, response);
            }

        } else {

            if (!isPublicUrl(request)) {
                writeInResponse(errorResponse, response);
            }

        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtUtils.validateToken(token, userDetails);
            if (validateToken) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {

                errorResponse.setStatus(403);
                errorResponse.setError("Validation Fails");
                errorResponse.setSubErrors(Arrays.asList("Unknown Source"));

                writeInResponse(errorResponse, response);

            }

        }

        filterChain.doFilter(request, response);

    }

    private void writeInResponse(ErrorResponse errorResponse, HttpServletResponse response) throws IOException {
        ApiResponse<Void> apiResponse = new ApiResponse<>(errorResponse);
        apiResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
        response.setHeader("ex", "success");

    }

    private boolean isPublicUrl(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return Arrays.asList(UrlConstants.PUBLIC_URL).stream()
                .filter(e -> pathMatcher.match(e, requestPath))
                .findAny().isPresent();

    }

}
