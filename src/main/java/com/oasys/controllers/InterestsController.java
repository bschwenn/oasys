package com.oasys.controllers;

import com.oasys.entities.Interest;
import com.oasys.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class InterestsController {
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private EntityManager em;

    @GetMapping("/interest/all")
    public List<String> getAllInterests() {
        Iterator<Interest> iterableInterests = interestRepository.findAll().iterator();
        List<String> interests = new ArrayList<>();
        iterableInterests.forEachRemaining(i -> interests.add(i.getName()));
        return interests;
    }

    @GetMapping("/study/all")
    public List<String> getAllStudies() {
        TypedQuery<Interest> studyQuery = em.createQuery(
                "SELECT i FROM Interest i WHERE i.isStudy = true",
                Interest.class
        );
        Stream<Interest> stream = studyQuery.getResultStream();
        return stream.map(Interest::getName).collect(Collectors.toList());
    }
}