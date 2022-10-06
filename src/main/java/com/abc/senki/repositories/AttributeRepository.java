package com.abc.senki.repositories;

import com.abc.senki.model.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface AttributeRepository extends JpaRepository<AttributeEntity, UUID> {
    Optional<AttributeEntity> findByName(String name);
}
