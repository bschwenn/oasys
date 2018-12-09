package com.oasys.repository;

import com.oasys.entities.Flock;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface FlockRepository extends PagingAndSortingRepository<Flock, Long> {
    @Override
    Optional<Flock> findById(Long fid);

    Flock findByNameIgnoreCase(String name);
}