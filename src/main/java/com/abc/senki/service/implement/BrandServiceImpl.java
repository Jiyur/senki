package com.abc.senki.service.implement;

import com.abc.senki.model.entity.BrandEntity;
import com.abc.senki.repositories.BrandRepository;
import com.abc.senki.repositories.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Override
    public BrandEntity getBrandById(int id) {
        return brandRepository.findById(id).orElse(null);
    }
}
