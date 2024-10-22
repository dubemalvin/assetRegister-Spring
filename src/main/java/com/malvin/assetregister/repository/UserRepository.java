package com.malvin.assetregister.repository;

import com.malvin.assetregister.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
