package com.application.pacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.pacs.model.Organization;

import java.util.List;
import java.util.Optional;

/**
 * Used for finding users
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	Optional<Organization> findByEmail(String email);

	Optional<Organization> findByOrganizationname(String organizationname);

	Optional<Organization> findByOrganizationcodeOrEmail(String organizationcode, String email);

	List<Organization> findByIdIn(List<Long> userIds);

	Optional<Organization> findByOrganizationcode(String organizationcode);

	Boolean existsByOrganizationcode(String organizationcode);

	Boolean existsByOrganizationname(String organizationname);

	Boolean existsByEmail(String email);

	Boolean existsByPhonenumber(Long phonenumber);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE organizations o set o.organizationenabled =:enable where o.organizationcode = :organizationcode", nativeQuery = true)
	int enabledisableOrganizationcode(@Param("organizationcode") String organizationcode,
			@Param("enable") boolean enable);

	
	 @Transactional
	 @Modifying(clearAutomatically = true)
	 @Query("SELECT o FROM Organization o where o.User.") Optional<Organization>
	  findByUserId(@Param("userId") String userId); //need to correct the function
	 
}
