package com.tean.supergame.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity(name = "point_transaction")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT(11)")
    private Integer id;

    @Column(name = "user_id", columnDefinition = "VARCHAR(20)")
    private String userId;

    @Column(name = "amount", columnDefinition = "INT(11)")
    private Integer amount;

    @Column(name = "balance", columnDefinition = "INT(11)")
    private Integer balance;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "datetime")
    private ZonedDateTime createdAt;
}