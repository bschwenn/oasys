package com.oasys.controllers;

import com.oasys.entities.Flock;
import com.oasys.entities.Interest;
import com.oasys.entities.Person;
import com.oasys.entities.StudyRecord;
import com.oasys.repository.FlockRepository;
import com.oasys.repository.InterestRepository;
import com.oasys.repository.PersonRepository;
import com.oasys.repository.StudyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FlockRepository flockRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private StudyRecordRepository studyRecordRepository;

    @RequestMapping("/persons/{username}")
    public Person getPerson(@PathVariable String username) {
        return personRepository.findByUsername(username);
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person) {
        personRepository.save(person);
        return person;
    }

    @RequestMapping("/persons/{username}/majors")
    public Set<Interest> getMajors(@PathVariable String username) {
        return personRepository.findByUsername(username).getMajors();
    }

    @RequestMapping("/persons/{username}/minors")
    public Set<Interest> getMinors(@PathVariable String username) {
        return personRepository.findByUsername(username).getMinors();
    }

    @RequestMapping("/persons/{username}/flocks")
    public Set<Flock> getPersonFlocks(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getFlocks();
    }

    @RequestMapping("/persons/{username}/follows")
    public Set<Flock> getPersonFollows(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getFollowedFlocks();
    }

    @PostMapping("/persons/{username}/follows/{fid}")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Flock followFlock(@PathVariable String username, @PathVariable Long fid) {
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
    public Set<Interest> getPersonInterests(@PathVariable String username) {
        Person user = personRepository.findByUsername(username);
        return user.getInterests();
    }

    @PostMapping("/persons/{username}/interests/{iid}")
    @PreAuthorize("#username.equals(authentication.principal)")
    public Person addInterest(@PathVariable String username, @PathVariable Long iid, String kind) {
        Person user = personRepository.findByUsername(username);
        Optional<Interest> interestBox = interestRepository.findById(iid);
        if (!interestBox.isPresent()) {
            return user;
        }
        Interest interest = interestBox.get();
        if (kind != null) {
            user.addStudy(interest, kind, studyRecordRepository);
        } else {
            user.addInterest(interest);
        }
        personRepository.save(user);
        return user;
    }
}
