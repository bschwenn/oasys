package com.oasys.controllers;

import com.oasys.config.Constants;
import com.oasys.entities.Event;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.EventRepository;
import com.oasys.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class EventController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EntityManager em;

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

    @GetMapping("/current_user/events/{page}")
    public List<Event> getAllEvents(Principal principal, @PathVariable int page) {
        Person user = personRepository.findByUsername(principal.getName());
        // TODO (Ben): we're probably going to want the feed to be more customizable
        TypedQuery<Event> eventQuery = em.createQuery(
                String.format(
                        "SELECT e " +
                                "FROM Event e, MemberRecord m " +
                                "WHERE m.member.uid = %s " +
                                "AND e.groupId = m.flock.gid " +
                                "ORDER BY e.time DESC",
                        user.getUid()
                ),
                Event.class
        );
        eventQuery.setFirstResult(page * Constants.PAGE_SIZE);
        eventQuery.setMaxResults(Constants.PAGE_SIZE);
        return eventQuery.getResultList();
    }
}
