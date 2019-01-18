package com.courseproject.movienightsapi.repositories;

import com.courseproject.movienightsapi.models.users.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Boolean existsByUserId(String userId);
    User findByUserId(String userId);
}
