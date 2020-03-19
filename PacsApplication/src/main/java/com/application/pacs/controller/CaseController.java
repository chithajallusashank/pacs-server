package com.application.pacs.controller;

import com.application.pacs.exception.AppException;
import com.application.pacs.payload.cases.AddCase;
import com.application.pacs.exception.ResourceNotFoundException;

import com.application.pacs.model.Case;
import com.application.pacs.model.CaseLog;
import com.application.pacs.model.CaseStatus;
import com.application.pacs.model.CaseType;
import com.application.pacs.model.Organization;
import com.application.pacs.model.Role;
import com.application.pacs.model.RoleName;
import com.application.pacs.model.User;
import com.application.pacs.payload.ApiResponse;
import com.application.pacs.payload.PagedResponse;
import com.application.pacs.payload.cases.AddCaseResponse;
import com.application.pacs.payload.cases.AssignCase;
import com.application.pacs.payload.cases.AssignCaseList;
import com.application.pacs.payload.cases.AssigneeResponse;
import com.application.pacs.payload.cases.CaseResponse;
import com.application.pacs.payload.cases.CaseTypes;
import com.application.pacs.payload.users.UserSummary;
import com.application.pacs.payload.organization.OrganizationCreateRequest;
import com.application.pacs.payload.organization.OrganizationIdentityAvailability;
import com.application.pacs.payload.organization.OrganizationProfile;
import com.application.pacs.payload.organization.OrganizationSummary;
import com.application.pacs.payload.security.SignUpRequest;
import com.application.pacs.repository.CaseRepository;
import com.application.pacs.repository.OrganizationRepository;
import com.application.pacs.security.CurrentUser;
import com.application.pacs.security.UserPrincipal;
import com.application.pacs.service.CaseService;
import com.application.pacs.util.AppConstants;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/case")
public class CaseController {

    @Autowired
    private CaseRepository caseRepository;
    
    @Autowired
    private CaseService caseService;
    
    @Autowired
    private OrganizationRepository orgRepo;

    private Case caseinfo;

    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);

    @GetMapping("/getcasetypes")
    @PreAuthorize("hasRole('USER')")
    public List<CaseType> getCaseTypes(@CurrentUser UserPrincipal currentUser) {
    	logger.info("User "+currentUser+" requested case types");
    	logger.debug("Returning case types"+CaseType.values());    	
    	return Arrays.asList(CaseType.values());
    }
    
    @GetMapping("/getAllCases")
  //  @PreAuthorize("hasRole('ASSIGNER')")
    public PagedResponse<CaseResponse> getAllCases(@CurrentUser UserPrincipal currentUser,@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size)
    {
    	logger.info("Called service to retrieve cases for user"+currentUser.getUsername());
    	return caseService.getCasesForUser(currentUser, page, size);
    }
    
    @GetMapping("/getAssigneesList")
    //@PreAuthorize("hasRole('USER')")
    public List<AssigneeResponse> getAssigneesList(@CurrentUser UserPrincipal currentUser,@RequestParam(value = "assigneetype") RoleName assigneeType)
    {
    	logger.info("Called service to retrieve assignees for user"+currentUser.getUsername());
    	if(assigneeType.equals(RoleName.ROLE_RADIOLOGIST))
    	{
    		logger.debug("Assignees requested for type: radiologists");
    		return caseService.getAssigneesForUser(currentUser,RoleName.ROLE_RADIOLOGIST);
    	}
    	
    	else if(assigneeType.equals(RoleName.ROLE_ASSIGNER))
    	{
    		logger.debug("Assignees requested for type: assigners");
    		return caseService.getAssigneesForUser(currentUser,RoleName.ROLE_ASSIGNER);
    	}
    	else
    	{
    		
    			return null;
    		
    	}
    	
    	
    }
    
    
    @PostMapping("/casestoassign")
    @PreAuthorize("hasRole('USER')||hasRole('ADMIN')")
    public PagedResponse<CaseResponse> CasesToAssign(@CurrentUser UserPrincipal currentUser,@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,@Valid @RequestBody List<AssignCase> assignCaseRequest) {
    	
    logger.debug("Assign cases requested");
    	return caseService.assignCasesToUser(currentUser,assignCaseRequest, page, size); 
    	
    }
    
    
    @PostMapping("/addcase")
    @PreAuthorize("hasRole('REPORT_UPLOADER')||hasRole('ADMIN')")
    public AddCaseResponse AddCase(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody AddCase addCaseRequest) {
    	logger.info("User - "+currentUser.getUsername()+" - posted add case request for "+addCaseRequest.getCasetype());
    	
    	Organization org;
    	logger.info("here at 1");
    	org=orgRepo.findByOrganizationcode(currentUser.getOrganizationcode()).get();
    	logger.info("here at 2");
    	Case newCase=new Case(addCaseRequest.getPatientname(), addCaseRequest.getFileuri(),addCaseRequest.getCasetype(),addCaseRequest.getBodyparttype(), addCaseRequest.getPatienthistory(), addCaseRequest.getPatientid(),addCaseRequest.getEmergency(),org);
    	AddCaseResponse response= new AddCaseResponse();
    	Case result = new Case();
    	logger.info("here at 3");
    	CaseLog caselog=new CaseLog(currentUser.getUsername(),"Added a new case", CaseStatus.CASESTATUS_OPEN);
    	logger.info("here at 4");
    	newCase.setCaselogs(Collections.singleton(caselog));
    	logger.info("here at 5");
    	try {
    	 result = caseRepository.save(newCase);
    	 logger.info("here at 6");
    	}catch (Exception e)
    	{
    		logger.info("The exception of the add case request is:"+e.getMessage());  
    		response.setCaseid(result.getId());
    		response.setError(true);
    		response.setErrormessage(e.getMessage());
    		return response;
    	}
    	logger.info("The result of the add case request is:"+result.getId());   	
    	
    	
    	response.setCaseid(result.getId());
    	response.setError(false);
    	response.setErrormessage("");
    	return response;
    	
    	
    }

    
   

}
