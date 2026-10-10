package com.github.lanagraf99.userservice.repository;

import com.github.lanagraf99.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}