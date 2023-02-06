package com.abc.senki.service.implement;

import com.abc.senki.model.entity.VoucherEntity;
import com.abc.senki.repositories.VoucherRepository;
import com.abc.senki.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;


    @Override
    public VoucherEntity findByCode(String code) {
        return voucherRepository.findByCode(code).orElse(null);
    }

    @Override
    public List<VoucherEntity> getAllVoucher(Pageable pageable) {
        return voucherRepository.findAll(pageable).getContent();
    }

    @Override
    public void deleteVoucher(UUID id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public void saveVoucher(VoucherEntity voucherEntity) {
        voucherRepository.save(voucherEntity);
    }
}
