package com.shopora.ecommerce.admin.features.auth.repository;
import com.shopora.ecommerce.admin.features.auth.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    // SELECT * FROM admins WHERE email = ?
    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Admin> findByResetPasswordToken(String token);
}
