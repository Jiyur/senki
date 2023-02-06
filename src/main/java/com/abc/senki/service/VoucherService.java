package com.abc.senki.service;

import com.abc.senki.model.entity.VoucherEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Component
public interface VoucherService {
    VoucherEntity findByCode(String code);
    List<VoucherEntity> getAllVoucher(Pageable pageable);
    void deleteVoucher(UUID id);

    void saveVoucher(VoucherEntity voucherEntity);
}
