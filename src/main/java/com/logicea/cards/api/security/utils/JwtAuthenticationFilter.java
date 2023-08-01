package com.logicea.cards.api.security.utils;


import com.logicea.cards.api.payloads.CardsApiError;
import com.logicea.cards.api.security.services.JwtProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //TODO: create missing errors
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN = "Bearer ";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtProviderService jwtProviderService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
        String email = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER_TOKEN)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                email = jwtProviderService.getEmailFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.info(CardsApiError.INVALID_TOKEN.getError());
            } catch (Exception e) {
                log.debug(CardsApiError.EXPIRED_TOKEN.getError());
            }
        } else {
            log.debug(CardsApiError.MISSING_TOKEN.getError());
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            request.setAttribute("email", email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (userDetails != null && jwtToken != null && jwtProviderService.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//        String header = req.getHeader(AUTHORIZATION_HEADER);
//        String email = null;
//        String jwtToken = null;
//        if (header != null && header.startsWith(BEARER_TOKEN)) {
//            jwtToken = header.replace(BEARER_TOKEN,"");
//            try {
//                email = jwtProviderService.getEmailFromToken(jwtToken);
//                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//                    if (jwtProviderService.validateToken(jwtToken, userDetails)) {
//                        UsernamePasswordAuthenticationToken authentication = jwtProviderService.getAuthenticationToken(jwtToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
//                        logger.info("authenticated user " + email + ", setting security context");
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }
//                }
//            } catch (IllegalArgumentException e) {
//                log.error("An error occurred while fetching Username from Token", e);
//            } catch (ExpiredJwtException e) {
//                log.warn("The token has expired", e);
//            }
//        } else {
//            log.warn("Couldn't find bearer string, header will be ignored");
//        }
//        chain.doFilter(req, res);
//    }
}