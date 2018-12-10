package com.oasys.controllers;

import com.oasys.entities.Event;
import com.oasys.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class EventController {
    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/event/{eid}")
    public Event getEvent(@PathVariable Long eid) {
        Optional<Event> eventBox = eventRepository.findById(eid);
        if (eventBox.isPresent()) {
            return eventBox.get();
        } else {
            return null;
        }
    }

    @PostMapping("/events")
    public Event createEvent(@RequestBody Event event) {
        eventRepository.save(event);
        return event;
    }
}
