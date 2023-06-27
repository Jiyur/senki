package com.abc.senki.service;

import com.abc.senki.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface UserService {
    UserEntity findByFullName(String fullname);
    UserEntity findById(UUID id);
    List<UserEntity> getAllUser();
    Boolean existsByFullName(String fullname);
    UserEntity findByPhone(String phone);
    Boolean existsByPhone(String phone);
    UserEntity saveInfo(UserEntity user);
    UserEntity findByEmail(String email);
    UserEntity updateActive(UserEntity user,Boolean active);
    void setStatus(String id,Boolean status);
    void deleteById(String id);

    UserEntity saveUser(UserEntity user, String roleName);

    Boolean existsByEmail(String email);

    List<UserEntity> getSellerByNickname(String nickname);





}
