package com.abc.senki.service.impl;

import com.abc.senki.model.entity.RoleEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.repositories.RoleRepository;
import com.abc.senki.repositories.UserRepository;
import com.abc.senki.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;

    final RoleRepository roleRepository;

    @Override
    public UserEntity findByFullName(String fullName) {
        Optional<UserEntity> user = userRepository.findByFullName(fullName);
        if(user.isEmpty())
            return null;
        return user.get();
    }

    @Override
    public UserEntity findById(UUID uuid){
        Optional<UserEntity> user = userRepository.findById(uuid);
        if(user.isEmpty())
            return null;
        return user.get();
    }

    @Override
    public List<UserEntity> getAllUser(){
        return userRepository.findAll();
    }



    @Override
    public UserEntity saveInfo(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean existsByFullName(String fullName) {
        return userRepository.existsByFullName(fullName);
    }

    @Override
    public UserEntity findByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            return null;
        }
        return user.get();    }

    @Override
    public UserEntity findByPhone(String phone) {
        Optional<UserEntity> user = userRepository.findByPhone(phone);
        if (user.isEmpty()){
            return null;
        }
        return user.get();
    }
    @Override
    public Boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public UserEntity updateActive(UserEntity user) {
        user.setActive(true);
        return userRepository.save(user);
    }
    @Override
    public UserEntity setStatus(UserEntity user,Boolean status) {
        user.setStatus(status);
        return userRepository.save(user);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(UUID.fromString((id)));
    }

    @Override
    public UserEntity saveUser(UserEntity user, String roleName) {
        Optional<RoleEntity> role=roleRepository.findByName(roleName);
        if(role.isPresent()){
            if(user.getRoles()==null){
                Set<RoleEntity> roleSet=new HashSet<>();
                roleSet.add(role.get());
                user.setRoles(roleSet);
            }
            else{
                user.getRoles().add(role.get());
            }
        }

        return userRepository.save(user);
    }
    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
