package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.UUID;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    @Column(name = "token_id")
    private UUID tokenId;

    @Getter
    @Column(name="confirmation_token")
    private String confirmationToken;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;


    @OneToOne(targetEntity = M5User.class, fetch = FetchType.EAGER,cascade=CascadeType.ALL)

    @JoinColumn( name = "user_id")

    private M5User user;



    public ConfirmationToken(M5User user) {
        this.user = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }



}