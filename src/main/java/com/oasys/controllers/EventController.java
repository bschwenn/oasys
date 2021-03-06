package com.oasys.controllers;

import com.oasys.config.Constants;
import com.oasys.entities.Event;
import com.oasys.entities.GoingRecord;
import com.oasys.entities.Person;
import com.oasys.entities.Post;
import com.oasys.repository.EventRepository;
import com.oasys.repository.GoingRecordRepository;
import com.oasys.repository.PersonRepository;
import com.oasys.service.GroupPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class EventController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private GroupPermissionService groupPermissionService;
    @Autowired
    private GoingRecordRepository goingRecordRepository;

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

    @GetMapping("/search/events/{name}")
    public List<Event> searchByName(@PathVariable String name, Principal principal) {
        List<Event> eventList = eventRepository.findByNameContainingIgnoreCase(name);

        List<Event> results = new ArrayList<>();
        for (Event e : eventList) {
            if (groupPermissionService.isInGroup(principal.getName(), e.getGroupId()) || e.isPublic()) {
                results.add(e);
            }
        }

        return results;
    }

    @GetMapping(value = "/current_user/goingBool/{eid}")
    public Event goingBool(Principal principal, @PathVariable Long eid) {
        Person user = personRepository.findByUsername(principal.getName());
        Optional<Event> eventBox = eventRepository.findById(eid);
        if (!eventBox.isPresent()) {
            return null;
        }

        Event event = eventBox.get();
        Set<Person> attendList = event.getGoingRecords().stream().map(GoingRecord::getPerson).collect(Collectors.toSet());
        if (attendList.contains(user)){
            return event;
        }else {
            return null;
        }
    }

    @RequestMapping(value = "/current_user/going/{eid}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public Event modifyRsvp(Principal principal, @PathVariable Long eid,
                            HttpServletRequest request) {
        if (principal == null) return null;
        Optional<Event> eventBox = eventRepository.findById(eid);
        if (!eventBox.isPresent()) {
            return null;
        }

        Person user = personRepository.findByUsername(principal.getName());
        Event event = eventBox.get();
        if (request.getMethod().equals("POST")) {
            user.addGoing(event, goingRecordRepository);
        } else {
            user.removeGoing(event, goingRecordRepository);
        }
        return event;
    }

    @GetMapping("/events/going_list/{eid}")
    public Set<Person> getGoingList(@PathVariable Long eid) {
        Optional<Event> eventBox = eventRepository.findById(eid);
        if (!eventBox.isPresent()) {
            return new HashSet<>();
        }

        Event event = eventBox.get();
        return event.getGoingRecords().stream().map(GoingRecord::getPerson).collect(Collectors.toSet());
    }
}
