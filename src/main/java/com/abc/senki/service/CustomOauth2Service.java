package com.abc.senki.service;

import com.abc.senki.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2Service extends DefaultOAuth2UserService {
    @Autowired
    UserService userService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oUser = super.loadUser(userRequest);
        String email = oUser.getAttribute("email");
        String picture = oUser.getAttribute("picture");
        String name = oUser.getAttribute("name");
        if(userService.findByEmail(email)==null){
            UserEntity user = new UserEntity();
            user.setEmail(email);
            user.setStatus(true);
            user.setFullName(name);
            user.setImg(picture);
            user.setActive(true);

            userService.saveUser(user,"USER");
        }
        return oUser;
    }

}
