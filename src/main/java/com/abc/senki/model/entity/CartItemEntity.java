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


    @ManyToOne
    @JoinColumn(name="\"attribute_value_id\"")
    private AttributeValueEntity attributeValue;



    @Column(name = "\"quantity\"")
    private int quantity;

    public void setInfo(CartEntity cart, ProductEntity product, AttributeValueEntity attributeValue, int quantity) {
        this.cart = cart;
        this.product = product;
        this.attributeValue = attributeValue;
        this.quantity = quantity;

    }
    public void setInfo(CartEntity cart, ProductEntity product,  int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;

    }


}
