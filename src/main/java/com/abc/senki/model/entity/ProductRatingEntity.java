package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"product_ratings\"")
public class ProductRatingEntity {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private int rating;

    private String comment;

    @Column(name = "\"created_at\"")
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "\"product_id\"")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name ="\"user_id\"")
    private UserEntity user;

    @OneToMany(mappedBy = "productRating",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<RatingCommentEntity> commentList;
}
