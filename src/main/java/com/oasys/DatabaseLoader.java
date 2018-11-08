package com.oasys;

import com.oasys.entities.Person;
import com.oasys.entities.PersonRepository;
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
    }
}
