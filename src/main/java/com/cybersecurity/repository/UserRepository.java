package com.cybersecurity.repository;

import com.cybersecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Override
    List<User> findAll();
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
