package com.example.network.repositories;

import com.example.network.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User,Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByEmailAndUsername(String email,String username);
}
