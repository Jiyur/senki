package com.abc.senki.handler;

import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.security.jwt.JwtUtils;
import com.abc.senki.service.UserService;
import com.abc.senki.util.CookieUtils;
import com.abc.senki.util.CookieOAuth2RequestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2Handler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;

    private final CookieOAuth2RequestRepository cookieOAuth2RequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response,authentication);
        //frontend-host/redirect?token=xxxx
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, CookieOAuth2RequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        System.out.println("redirectUri: " + redirectUri);
        if (redirectUri.isEmpty()) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");

        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        System.out.println(targetUrl);
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        System.out.println(email);


        String accessToken = jwtUtils.generateEmailJwtToken(email);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build().toUriString();

    }
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieOAuth2RequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
