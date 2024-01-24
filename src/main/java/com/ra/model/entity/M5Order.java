package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class M5Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Double totalPrice;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonIgnore
    private M5User user;
    @OneToMany(mappedBy = "order")
    private Set<M5OrderDetail> orderDetails;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
//    enum status
    private String receiver;
    private String receiverAddress;
    private String receiverPhone;
    private Date createAt;
    private Date receiveAt;
}
