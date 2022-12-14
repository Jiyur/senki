package com.abc.senki.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static com.abc.senki.common.OrderStatus.*;
@Entity
@Table(name="\"orders\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    private UUID id;

    private Double total;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="\"address_id\"")
    private AddressEntity address;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="\"user_id\"")
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderDetailEntity> orderDetails;


    private String status;

    private String method;

    private LocalDateTime createdAt;

    private Double shipFee;

    public OrderEntity(UserEntity user) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.status=PENDING.getMesssage();
        this.createdAt = LocalDateTime.now();
        this.address = user.getAddress();
        this.total=0.0;
        this.shipFee=15000.0;
    }


}
