package com.oasys.repository;

import com.oasys.entities.Flock;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface FlockRepository extends PagingAndSortingRepository<Flock, Long> {
    @Override
    Optional<Flock> findById(Long fid);
}
