package com.example.beauty_salon_rest.repository;

import com.example.beauty_salon_rest.entity.UserEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
