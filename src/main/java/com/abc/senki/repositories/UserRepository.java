package com.abc.senki.repositories;

import com.abc.senki.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByFullName(String fullName);
    Boolean existsByFullName(String fullName);
    @Override
    List<UserEntity> findAll();
    Optional<UserEntity> findByPhone(String phone);
    Boolean existsByPhone(String phone);
    Optional<UserEntity> findByEmail(String email);
    void deleteById(UUID id);
    @Modifying
    @Query(value = "select * from users where nick_name LIKE ?1 AND user_id IN (SELECT user_id IN user_role where role_id=3) "
            , nativeQuery = true)
    Optional<UserEntity> getUserByNickname(String nickname);

    @Modifying
    @Query(value = "update users set status = ?2 where id = ?1", nativeQuery = true)
    void setStatus(UUID id,Boolean status);



    Boolean existsByEmail(String email);
}
