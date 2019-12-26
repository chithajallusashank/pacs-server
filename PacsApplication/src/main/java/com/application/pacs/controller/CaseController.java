package com.application.pacs.controller;

import com.application.pacs.exception.AppException;
import com.application.pacs.exception.ResourceNotFoundException;
import com.application.pacs.model.Case;
import com.application.pacs.model.CaseType;
import com.application.pacs.model.Organization;
import com.application.pacs.model.Role;
import com.application.pacs.model.RoleName;
import com.application.pacs.model.User;
import com.application.pacs.payload.ApiResponse;
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
import com.application.pacs.util.AppConstants;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    private Case caseinfo;

    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);

    @GetMapping("/getcasetypes")
    @PreAuthorize("hasRole('USER')")
    public List<CaseType> getCaseTypes(@CurrentUser UserPrincipal currentUser) {
    	logger.info("User "+currentUser+" requested case types");
    	logger.debug("Returning case types"+CaseType.values());    	
    	return Arrays.asList(CaseType.values());
    }

    
   

}
