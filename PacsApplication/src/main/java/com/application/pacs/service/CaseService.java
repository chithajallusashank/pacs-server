package com.application.pacs.service;

import com.application.pacs.exception.BadRequestException;
import com.application.pacs.exception.ResourceNotFoundException;
import com.application.pacs.model.*;
import com.application.pacs.payload.PagedResponse;
import com.application.pacs.payload.PollRequest;
import com.application.pacs.payload.PollResponse;
import com.application.pacs.payload.VoteRequest;
import com.application.pacs.payload.cases.AssignCaseIdRequest;
import com.application.pacs.payload.cases.AssignUserRequest;
import com.application.pacs.payload.cases.CaseResponse;
import com.application.pacs.repository.CaseRepository;
import com.application.pacs.repository.PollRepository;
import com.application.pacs.repository.UserRepository;
import com.application.pacs.repository.VoteRepository;
import com.application.pacs.security.UserPrincipal;
import com.application.pacs.util.AppConstants;
import com.application.pacs.util.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CaseService {

	
	
    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CaseService.class);

    public PagedResponse<CaseResponse> getCasesForUser(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        String username=currentUser.getUsername();
        // Retrieve cases
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        logger.info("Cases list requested by user:"+username);

        Set<Role> userRoles=user.getRoles();
    		if(userRoles.isEmpty())
    		{
    			logger.debug("User does not have any roles assigned:"+username);
    			throw new BadRequestException("The user does not have any roles assigned. No cases can be viewed");
    		}
        			
    		Page<Case> cases=null;
    		for (Iterator<Role> it = userRoles.iterator(); it.hasNext(); ) {
	        	Role f = it.next();
	            if (f.equals(RoleName.ROLE_DOCTOR)||f.equals(RoleName.ROLE_DOC_ASSISTANT))
	            {
	            	List<Long> userids=Arrays.asList(user.getId());
	            	cases = caseRepository.findByUser_IdIn(userids,pageable);
	                logger.info("Cases list requested by a doctor/ doctor's assistant:"+username);
	            }
	            if (f.equals(RoleName.ROLE_ASSIGNER)||true) //remove true- only for testing
	            {
	            	List<CaseStatus> caseStatuses=Arrays.asList(CaseStatus.CASESTATUS_OPEN, CaseStatus.CASESTATUS_ASSIGNED, CaseStatus.CASESTATUS_INPROGRESS,CaseStatus.CASESTATUS_DOWNLOADED);
	            	cases = caseRepository.findByCasestatusIn(caseStatuses,pageable);
	            	if(cases.isEmpty())logger.info("returned cases are empty");
	                logger.info("Cases list requested by a assigner"+username);
	            }else {
	            	logger.info("User role doesnt allow for viewing of the cases:"+username);
	    			throw new BadRequestException("Your role doesnt allow for viewing of the cases");
	            }
        }
        
        
        
        
        if(cases.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), cases.getNumber(),
            		cases.getSize(), cases.getTotalElements(), cases.getTotalPages(), cases.isLast());
        }

        
        List<CaseResponse> caseResponses = cases.stream().map(eachcase -> {
            return ModelMapper.mapCaseToCaseResponse(eachcase);
        }).collect(Collectors.toList());
        
        
       return new PagedResponse<>(caseResponses, cases.getNumber(),
        		cases.getSize(), cases.getTotalElements(), cases.getTotalPages(), cases.isLast());
    }
    
    
    
    public PagedResponse<CaseResponse>  assignCasesToUser(List<AssignUserRequest> assignToUser,List<AssignCaseIdRequest> caseIdsToAssign, int page, int size){
    	
    	 User user = userRepository.findByUsername(assignToUser.get(0).getUserName()).orElseThrow(() -> new ResourceNotFoundException("User", "username", assignToUser.get(0).getUserName()));
    	 List<Long> caseIds=new ArrayList<Long>();
    	 for (AssignCaseIdRequest temp : caseIdsToAssign) {
    		 
    		 caseIds.add(temp.getCaseId());
 			//System.out.println(temp);
 		}
    	
    	int rowsUpdated= caseRepository.assignCasesToUser(user.getId(),caseIds);
    	
    	if(rowsUpdated==0)
    	{
    		throw new BadRequestException("No cases were found for assignment");
    	}else
    	{
    		//What to do here?
    	}
    	
    	return null;
    	
    }

    
    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

  
}
