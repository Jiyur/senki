package com.abc.senki.repositories;

import com.abc.senki.model.entity.AttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface AttributeValueRepository extends JpaRepository<AttributeValueEntity, UUID> {
    Optional<AttributeValueEntity> findByValue(String value);
    List<AttributeValueEntity> findAllByAttributeEntityId(UUID id);


}
