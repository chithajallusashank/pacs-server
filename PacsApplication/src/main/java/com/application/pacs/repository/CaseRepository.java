package com.application.pacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.pacs.model.Case;
import com.application.pacs.model.Organization;

import java.util.List;
import java.util.Optional;

/**
 * Used for finding users
 */
@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
	Optional<Case> findById(Long id);

	Optional<Case> findByCasestatus(String casestatus);

	

	

	
	
}
