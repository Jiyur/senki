package com.abc.senki.service.implement;

import com.abc.senki.model.entity.AttributeEntity;
import com.abc.senki.repositories.AttributeRepository;
import com.abc.senki.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AttributeServiceImpl implements AttributeService {
    @Autowired
    AttributeRepository attributeRepository;
    @Override
    public void saveAttribute(AttributeEntity attribute) {
        attributeRepository.save(attribute);
    }

    @Override
    public AttributeEntity findAttributeByName(String name) {
       return attributeRepository.findByName(name).orElse(null);
    }

    @Override
    public AttributeEntity findAttributeById(String id) {
        return attributeRepository.findById(UUID.fromString(id)).orElse(null);
    }

    @Override
    public List<AttributeEntity> getAllAttribute() {
        return attributeRepository.findAll();
    }
}
