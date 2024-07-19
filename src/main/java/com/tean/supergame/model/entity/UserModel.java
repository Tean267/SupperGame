package com.tean.supergame.model.entity;

import com.tean.supergame.until.CheckInListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@EntityListeners(CheckInListener.class)
@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModel {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(20)")
    private String id;

    @Column(name = "username", columnDefinition = "VARCHAR(20)")
    private String username;

    @Column(name = "password", columnDefinition = "VARCHAR(100)")
    private String password;

    @Column(name = "point", columnDefinition = "INT(11)")
    private Integer point;
}