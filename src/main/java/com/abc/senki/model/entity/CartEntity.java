package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"carts\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private int id;



    @Column(name = "\"status\"")
    private Boolean status;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "\"user_id\"")
    private UserEntity user;



    @OneToMany(mappedBy = "cart",targetEntity = CartItemEntity.class)
    private List<CartItemEntity> cartItems;

    public CartEntity(UserEntity user) {
        this.user = user;
        this.status = true;
    }
}
