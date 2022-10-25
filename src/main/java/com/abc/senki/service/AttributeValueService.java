package com.abc.senki.service;

import com.abc.senki.model.entity.AttributeValueEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Component
@Service
public interface AttributeValueService {
    void saveValue(AttributeValueEntity value);
    AttributeValueEntity getValueById(UUID id);
}
