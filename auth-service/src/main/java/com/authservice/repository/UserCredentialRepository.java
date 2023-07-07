package com.authservice.repository;

import com.authservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialRepository  extends JpaRepository<UserCredential,Integer> {

    UserCredential findByUsername(String username);
}
