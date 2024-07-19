package com.tean.supergame.repository;

import com.tean.supergame.model.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,String> {
    Optional<UserModel> findByUsername(String username);

    boolean existsUserModelByUsername(String username);
}