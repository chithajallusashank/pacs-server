package com.application.pacs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.pacs.model.Case;
import com.application.pacs.model.CaseStatus;
import com.application.pacs.model.Organization;
import com.application.pacs.model.Poll;
import com.application.pacs.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Used for finding users
 */
@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
	Optional<Case> findById(Long id);

	Optional<Case> findByCasestatus(String casestatus);
	
	Page<Case> findByCasestatusInAndOrg_Id(List<CaseStatus> casestatus,Long OrgId, Pageable pageable);
	
	Page<Case> findByUser_IdInAndOrg_Id(List<Long> userid,Long OrgId,Pageable pageable );

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update cases s set s.user_id = :userid where s.id in (:caseids)",
            nativeQuery = true)
	int assignCasesToUser(@Param("userid") Long userid,@Param("caseids") List<Long> caseids) ;
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update cases s set s.user_id = null where s.id in (:caseids)",
            nativeQuery = true)
	int assignCasesToUser(@Param("caseids") List<Long> caseids) ;
	
	
}
