package com.example.network.repositories;

import com.example.network.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository <Rating,Long> {
}
