package com.oasys.controllers;

import com.oasys.entities.Flock;
import com.oasys.entities.Interest;
import com.oasys.entities.Person;
import com.oasys.repository.FlockRepository;
import com.oasys.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FlockRepository flockRepository;

    @RequestMapping("/persons/{username}")
    public Person getPerson(@PathVariable String username) {
        return personRepository.findByUsername(username);
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person) {
        personRepository.save(person);
        return person;
    }

    @RequestMapping("/persons/{username}/flocks")
    public List<Flock> getPersonFlocks(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getFlocks();
    }

    @PostMapping("/persons/{username}/flocks")
    // TODO (Ben): PreAuthorize that user is an admin in group
    public Flock addPersonToFlock(@PathVariable String username, Long fid) {
        Person user = personRepository.findByUsername(username);
        Optional<Flock> flockBox = flockRepository.findById(fid);
        if (flockBox.isPresent()) {
            Flock flock = flockBox.get();
            user.getFlocks().add(flock);
            // TODO (Ben): figure out how to deal with initiator uid
            // personRepository.save(user);
            return flock;
        } else {
            return null; // TODO (BEN): error handling :)
        }
    }

    @RequestMapping("/persons/{username}/follows")
    public List<Flock> getPersonFollows(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getFollowedFlocks();
    }

    @PostMapping("/persons/{username}/follows")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Flock followFlock(@PathVariable String username, Long fid) {
        Person user = personRepository.findByUsername(username);
        Optional<Flock> flockBox = flockRepository.findById(fid);
        if (flockBox.isPresent()) {
            Flock flock = flockBox.get();
            user.getFollowedFlocks().add(flock);
            personRepository.save(user);
            return flock;
        } else {
            return null; // TODO (BEN): error handling :)
        }
    }

    @RequestMapping("/persons/{username}/interests")
    public List<Interest> getPersonInterests(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getInterests();
    }
}
