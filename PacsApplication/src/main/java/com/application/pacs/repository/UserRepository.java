package com.application.pacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.application.pacs.model.RoleName;
import com.application.pacs.model.User;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *Used for finding users
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByOrganizationcodeAndUserenabled(String orgCode,Boolean enabled);
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
    
    
    @Transactional
    @Query(value= "with recursive hierarchy (subordinate_id, supervisor_id) as (select subordinate_id,supervisor_id from supervisor where supervisor_id = :user_id "
    		+ "union all select s.subordinate_id,s.supervisor_id from supervisor s inner join hierarchy on s.supervisor_id = hierarchy.subordinate_id) select users.name, username from hierarchy inner join users on subordinate_id=id inner join organizations using (organizationcode) inner join user_roles on user_id=users.id inner join roles on role_id=roles.id where organizationcode=:orgcode and userenabled=true and organizationenabled=true and roles.name=:assigneetype",nativeQuery=true)
	List<Object[]> getUsersInHierarchy(@Param("orgcode") String orgcode,@Param("user_id") Long user_id,@Param("assigneetype") String assigneetype);
}
