package com.abc.senki.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "\"order_details\"")
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

    private String productName;
    private String productImage;
    private UUID productId;


    private Double price;

    @Column(name = "\"quantity\"")
    private int quantity;

    public void setInfo(OrderEntity order, ProductEntity product, int quantity, double price) {
        this.order = order;
        this.productName = product.getName();
        this.productId = product.getId();
        if (product.getImageList().size() > 0) {
            this.productImage = product.getImageList().get(0).getUrl();
        }
        this.quantity = quantity;
        this.price = price;
    }
    public void setInfo(OrderEntity order, String name,UUID id,String imageUrl, int quantity, double price) {
        this.order = order;
        this.productName = name;
        this.productId = id;
       this.productImage = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }

}
