package com.oasys.repository;


import com.oasys.entities.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RepositoryRestResource()
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
//    @Query( "SELECT post FROM Post post, Person person " +
//            "WHERE person.username = :username " +
//            "AND post.gid IN (SELECT f.gid FROM Follows f WHERE f.uid = person.uid)")
//    @PreAuthorize("#username.equals(authentication.principal)")
//    List<Post> getUserFeed(@Param("username") String username);

    @Query("SELECT p FROM Post p WHERE p.gid = :gid ORDER BY p.timestamp")
    @PreAuthorize("@groupPermissionEvaluator.isInGroup(authentication, #gid)")
    List<Post> getGroupFeed(@Param("gid") long gid);
}
