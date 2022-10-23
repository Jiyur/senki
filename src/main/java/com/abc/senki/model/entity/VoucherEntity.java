package com.abc.senki.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "\"voucher\"")
public class VoucherEntity {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "voucher_id")
    private UUID id;

    @Column(name = "amount")
    private int amount;
    @Column(name = "status")
    private boolean status;
    @Column(name = "value")
    private String value;
    @Column(name = "type")
    private String type;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "expired_date")
    private Date expiredDate;
}
