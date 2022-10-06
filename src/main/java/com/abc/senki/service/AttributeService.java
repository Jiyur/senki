package com.abc.senki.service;

import com.abc.senki.model.entity.AttributeEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface AttributeService {
    void saveAttribute(AttributeEntity attribute);
    AttributeEntity findAttributeByName(String name);
    AttributeEntity findAttributeById(String id);
    List<AttributeEntity> getAllAttribute();
}
