package com.abc.senki.repositories;

import com.abc.senki.model.entity.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ProvinceRepository extends JpaRepository<ProvinceEntity,String> {
}
