package com.application.pacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.pacs.model.User;

import java.util.List;
import java.util.Optional;

/**
 *Used for finding users
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    Boolean existsByPhonenumber(Long phonenumber);
    
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE users u set u.userenabled =:enable where u.username = :username",
            nativeQuery = true)
int enabledisableUsername(@Param("username") String username, @Param("enable") boolean enable);
}
