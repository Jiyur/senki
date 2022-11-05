package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"cart_items\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private int id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name ="\"cart_id\"")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name="\"product_id\"")
    private ProductEntity product;






    @Column(name = "\"quantity\"")
    private int quantity;

    public void setInfo(CartEntity cart, ProductEntity product,  int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;

    }



}
