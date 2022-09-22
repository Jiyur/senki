package com.abc.senki.security.service;


import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.repositories.UserRepository;
import com.abc.senki.security.dto.AppUserDetail;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AppUserDetailService implements UserDetailsService {
    private static final Logger LOGGER = LogManager.getLogger(AppUserDetailService.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findById(UUID.fromString(id));
        if(userEntity.isEmpty())
        {
            throw new UsernameNotFoundException("User not found");
        }
        LOGGER.info(userEntity.get().getEmail());
        return AppUserDetail.build(userEntity.get());
    }
}
