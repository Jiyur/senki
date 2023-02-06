package com.abc.senki.repositories;

import com.abc.senki.model.entity.BrandEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface BrandService {
    BrandEntity getBrandById(int id);
}
