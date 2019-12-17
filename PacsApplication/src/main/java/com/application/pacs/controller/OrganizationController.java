package com.application.pacs.controller;

import com.application.pacs.exception.ResourceNotFoundException;
import com.application.pacs.model.Organization;
import com.application.pacs.payload.ApiResponse;
import com.application.pacs.payload.users.UserSummary;
import com.application.pacs.payload.organization.OrganizationIdentityAvailability;
import com.application.pacs.payload.organization.OrganizationProfile;
import com.application.pacs.payload.organization.OrganizationSummary;
import com.application.pacs.repository.OrganizationRepository;
import com.application.pacs.security.CurrentUser;
import com.application.pacs.security.UserPrincipal;
import com.application.pacs.util.AppConstants;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;

    private Organization organization;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @GetMapping("/organization/me")
    @PreAuthorize("hasRole('USER')")
    public OrganizationSummary getCurrentOrganization(@CurrentUser UserPrincipal currentUser) {
    	//need to correct the function
        OrganizationSummary organizationSummary = null; //= new OrganizationSummary(organizationRepository.getId(), currentUser.getUsername(), currentUser.getName());
        return organizationSummary;
    }

    @GetMapping("/organization/checkOrganizationCodeAvailability")
    public OrganizationIdentityAvailability checkOrganizationcodeAvailability(@RequestParam(value = "organizationcode") String organizationcode) {
        Boolean isAvailable = !organizationRepository.existsByOrganizationcode(organizationcode);
        return new OrganizationIdentityAvailability(isAvailable);
    }
    
    @GetMapping("/organization/checkOrganizationNameAvailability")
    public OrganizationIdentityAvailability checkOrganizationnameAvailability(@RequestParam(value = "organizationname") String organizationname) {
        Boolean isAvailable = !organizationRepository.existsByOrganizationname(organizationname);
        return new OrganizationIdentityAvailability(isAvailable);
    }
    
   // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/organization/enabledisableOrganizationcode")
    public ResponseEntity<?> disableOrganizationcode(@RequestParam(value = "organizationcode") String organizationcode,
    												@RequestParam(value = "enable") Boolean enable) {
        int updatedRecord = organizationRepository.enabledisableOrganizationcode(organizationcode,enable);
       
        if(updatedRecord>0)
       return new ResponseEntity(new ApiResponse(true, "Organization code has been disabled!"),
                HttpStatus.OK);
       return new ResponseEntity(new ApiResponse(false, "Organization code disable failed!"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/organization/checkEmailAvailability")
    public OrganizationIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !organizationRepository.existsByEmail(email);
        return new OrganizationIdentityAvailability(isAvailable);
    }
    
    @GetMapping("/organization/checkPhonenumberAvailability")
    public OrganizationIdentityAvailability checkPhonenumberAvailability(@RequestParam(value = "phonenumber") Long phonenumber) {
        Boolean isAvailable = !organizationRepository.existsByPhonenumber(phonenumber);
        return new OrganizationIdentityAvailability(isAvailable);
    }

    @GetMapping("/organizations/{organizationcode}")
    public OrganizationProfile getUserProfile(@PathVariable(value = "organizationcode") String organizationcode) {
        Organization organization = organizationRepository.findByOrganizationcode(organizationcode)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "organizationcode", organizationcode));

      

        OrganizationProfile orgProfile = new OrganizationProfile(organization.getId(), organization.getOrganizationcode(), organization.getOrganizationname(), organization.getCreatedAt());

        return orgProfile;
    }

   

}
