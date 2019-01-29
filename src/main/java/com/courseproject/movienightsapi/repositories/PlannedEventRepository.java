package com.courseproject.movienightsapi.repositories;


import com.courseproject.movienightsapi.models.calendars.PlannedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedEventRepository extends MongoRepository<PlannedEvent, String> {
}
