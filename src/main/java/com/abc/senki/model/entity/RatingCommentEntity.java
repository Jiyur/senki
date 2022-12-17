package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"rating_comments\"")
public class RatingCommentEntity {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String comment;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "\"product_rating_id\"")
    private ProductRatingEntity productRating;

    @ManyToOne
    @JoinColumn(name = "\"user_id\"")
    private UserEntity user;

    @Column(name = "\"created_at\"")
    private LocalDateTime createdAt;

    public void setInfo(UserEntity user,ProductRatingEntity productRating){
        this.user=user;
        this.productRating=productRating;
        this.createdAt=LocalDateTime.now();
    }
}
