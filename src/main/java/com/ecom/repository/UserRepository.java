package com.ecom.repository;

import com.ecom.model.UserDtl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDtl, Integer> {

    public UserDtl findByEmail(String email);
    public boolean existsByEmail(String email);
    public UserDtl findByToken(String token);
}
