package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "\"users\"")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "\"user_role\"", joinColumns = @JoinColumn(name = "\"user_id\""), inverseJoinColumns = @JoinColumn(name = "\"role_id\""))
    private Set<RoleEntity> roles;

    @Column(name = "\"full_name\"")
    private String fullName;

    private String email;

    private String phone;


    @JsonIgnore
    private String password;

    private String gender;

    @Column(name = "\"img\"")
    private String img;

    @Column(name = "\"nick_name\"")
    private String nickName;

    @Column(name = "\"create_at\"")
    private LocalDateTime createAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user",targetEntity = ProductRatingEntity.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ProductRatingEntity> productRating;

    private boolean status;
    private boolean active;

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserEntity() {
        }
}
