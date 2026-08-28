//package com.shopora.ecommerce.config;
//
//import com.shopora.ecommerce.admin.features.auth.entities.Admin;
//import com.shopora.ecommerce.admin.features.auth.repository.AdminRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AdminDataLoader implements CommandLineRunner {
//
//    private final AdminRepository adminRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) {
//        if (!adminRepository.existsByEmail("shubhamranjane16@gmail.com")) {
//            Admin admin = Admin.builder()
//                    .email("shubhamranjane16@gmail.com")
//                    .password(passwordEncoder.encode("Admin@123"))
//                    .fullName("Shubham Ranjane")
//                    .isActive(true)
//                    .build();
//
//            adminRepository.save(admin);
//            System.out.println("Default admin created successfully!");
//        }
//    }
//}