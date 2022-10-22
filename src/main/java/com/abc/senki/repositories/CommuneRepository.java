package com.abc.senki.repositories;

import com.abc.senki.model.entity.CommuneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
@EnableJpaRepositories
public interface CommuneRepository extends JpaRepository<CommuneEntity,String> {
    List<CommuneEntity> findAllByDistrict(String districtId);
}
