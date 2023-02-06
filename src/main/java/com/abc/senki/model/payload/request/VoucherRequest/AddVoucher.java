package com.abc.senki.model.payload.request.VoucherRequest;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddVoucher {
    private String code;
    private String name;
    private String type;
    private String description;
    private Double value;

    private LocalDateTime endDate;
}
