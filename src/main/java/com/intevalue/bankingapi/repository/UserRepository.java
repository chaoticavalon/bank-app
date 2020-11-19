package com.intevalue.bankingapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.intevalue.bankingapi.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> getUserByEmail(String email);
}
