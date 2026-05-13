package com.ecol.reviewService.repository;

import com.ecol.reviewService.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository < Review, String> {

}
