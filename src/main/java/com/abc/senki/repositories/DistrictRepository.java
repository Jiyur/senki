package com.abc.senki.repositories;

import com.abc.senki.model.entity.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
@EnableJpaRepositories
public interface DistrictRepository extends JpaRepository<DistrictEntity,String> {
    List<DistrictEntity> findAllByProvince(String provinceId);
}
