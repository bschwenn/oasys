package com.oasys.repository;

import com.oasys.entities.Event;
import com.oasys.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContainingIgnoreCase(String name);
}
