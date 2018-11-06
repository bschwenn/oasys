package com.oasys;

import com.oasys.tables.Person;
import com.oasys.tables.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// TODO (Ben): Remove... this is temporary and used for testing purposes
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final PersonRepository repository;

    @Autowired
    public DatabaseLoader(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
        this.repository.save(new Person(
                "tim.cook@duke.edu",
                "Tim Cook",
                1980,
                null,
                null)
        );
        this.repository.save(new Person(
                "steve.cook@duke.edu",
                "Steve Cook",
                1980,
                null,
                null)
        );
    }
}
