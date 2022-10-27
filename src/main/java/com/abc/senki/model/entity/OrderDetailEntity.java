package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name="\"order_details\"")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private int id;

    @ManyToOne
    @JoinColumn(name = "\"order_id\"")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name="\"product_id\"")
    private ProductEntity product;



    @ManyToOne
    @JoinColumn(name="\"attribute_value_id\"")
    private AttributeValueEntity attributeValue;

    private Double price;

    @Column(name = "\"quantity\"")
    private int quantity;

    public void setInfo(OrderEntity order, ProductEntity product, AttributeValueEntity attributeValue, int quantity,double price) {
        this.order = order;
        this.product = product;
        this.attributeValue = attributeValue;
        this.quantity = quantity;
        this.price = price;
    }

}
