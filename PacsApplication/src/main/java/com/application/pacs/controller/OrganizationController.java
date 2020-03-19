package com.application.pacs.controller;

import com.application.pacs.exception.AppException;
import com.application.pacs.exception.ResourceNotFoundException;
import com.application.pacs.model.Organization;
import com.application.pacs.model.Role;
import com.application.pacs.model.RoleName;
import com.application.pacs.model.User;
import com.application.pacs.payload.ApiResponse;
import com.application.pacs.payload.users.UserSummary;
import com.application.pacs.payload.organization.OrganizationCreateRequest;
import com.application.pacs.payload.organization.OrganizationIdentityAvailability;
import com.application.pacs.payload.organization.OrganizationProfile;
import com.application.pacs.payload.organization.OrganizationSummary;
import com.application.pacs.payload.security.SignUpRequest;
import com.application.pacs.repository.OrganizationRepository;
import com.application.pacs.security.CurrentUser;
import com.application.pacs.security.UserPrincipal;
import com.application.pacs.util.AppConstants;

import java.net.URI;
import java.util.Collections;

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
@RequestMapping("/api/organization")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;

    private Organization organization;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public OrganizationSummary getCurrentOrganization(@CurrentUser UserPrincipal currentUser) {
    	//need to correct the function
        OrganizationSummary organizationSummary = null; //= new OrganizationSummary(organizationRepository.getId(), currentUser.getUsername(), currentUser.getName());
        return organizationSummary;
    }

  //  @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/checkOrganizationCodeAvailability")
    public OrganizationIdentityAvailability checkOrganizationcodeAvailability(@RequestParam(value = "organizationcode") String organizationcode,@CurrentUser UserPrincipal currentUser) {
    	logger.info("User - "+currentUser.getUsername()+" - Verifying organization code "+organizationcode+ " availability");
        Boolean isAvailable = !organizationRepository.existsByOrganizationcode(organizationcode);
        logger.debug("User - "+currentUser.getUsername()+" - Verified organization code "+organizationcode+ " availability to "+isAvailable);
        return new OrganizationIdentityAvailability(isAvailable);
    }
    
	/*
	 * @GetMapping("/checkOrganizationNameAvailability") public
	 * OrganizationIdentityAvailability
	 * checkOrganizationnameAvailability(@RequestParam(value = "organizationname")
	 * String organizationname) { Boolean isAvailable =
	 * !organizationRepository.existsByOrganizationname(organizationname); return
	 * new OrganizationIdentityAvailability(isAvailable); }
	 */
    
   // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/enabledisableOrganizationcode")
    public ResponseEntity<?> disableOrganizationcode(@RequestParam(value = "organizationcode") String organizationcode,
    												@RequestParam(value = "enable") Boolean enable) {
        int updatedRecord = organizationRepository.enabledisableOrganizationcode(organizationcode,enable);
       
        if(updatedRecord>0)
       return new ResponseEntity(new ApiResponse(true, "Organization code has been disabled!"),
                HttpStatus.OK);
       return new ResponseEntity(new ApiResponse(false, "Organization code disable failed!"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

  //  @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/checkOrganizationEmailAvailability")
    public OrganizationIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email,@CurrentUser UserPrincipal currentUser) {
    	logger.info("User - "+currentUser.getUsername()+" - Verifying email "+email+ " availability");
        Boolean isAvailable = !organizationRepository.existsByEmail(email);
        logger.debug("User - "+currentUser.getUsername()+" - Verified email "+email+ " availability to "+isAvailable);
        return new OrganizationIdentityAvailability(isAvailable);
    }
    
  //  @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/checkOrganizationPhonenumberAvailability")
    public OrganizationIdentityAvailability checkPhonenumberAvailability(@RequestParam(value = "phonenumber") Long phonenumber,@CurrentUser UserPrincipal currentUser) {
    	logger.info("User - "+currentUser.getUsername()+" - Verifying phonenumber "+phonenumber+ " availability");
    	Boolean isAvailable = !organizationRepository.existsByPhonenumber(phonenumber);
    	logger.debug("User - "+currentUser.getUsername()+" - Verified phonenumber "+phonenumber+ " availability to "+isAvailable);
        return new OrganizationIdentityAvailability(isAvailable);
    }
    
    @GetMapping("/checkOrganizationCodeExistence")
    public OrganizationIdentityAvailability checkOrganizationCodeExistence(@RequestParam(value = "organizationcode") String organizationcode) {
    	logger.info("User Verifying organization code "+organizationcode+ " exitence");
    	Boolean isAvailable = organizationRepository.existsByOrganizationcode(organizationcode);
    	logger.debug("User Verified organization code "+organizationcode+ " existence to "+isAvailable);
        return new OrganizationIdentityAvailability(isAvailable);
    }

    
    @GetMapping("/{organizationcode}")
    public OrganizationProfile getUserProfile(@PathVariable(value = "organizationcode") String organizationcode) {
        Organization organization = organizationRepository.findByOrganizationcode(organizationcode)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "organizationcode", organizationcode));

      

        OrganizationProfile orgProfile = new OrganizationProfile(organization.getId(), organization.getOrganizationcode(), organization.getOrganizationname(), organization.getCreatedAt());

        return orgProfile;
    }
    
    
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createorganization")
   public ResponseEntity<?> createOrganization(@Valid @RequestBody OrganizationCreateRequest orgCreateRequest,@CurrentUser UserPrincipal currentUser) {
    
    	logger.info("User - "+currentUser.getUsername()+" - posted create organization request for "+orgCreateRequest.getOrganizationCode());
        if(organizationRepository.existsByOrganizationcode(orgCreateRequest.getOrganizationCode())) {
      return new ResponseEntity(new ApiResponse(false, "Organization code is already taken!"),
                  HttpStatus.BAD_REQUEST);
        }

        if(organizationRepository.existsByEmail(orgCreateRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        
       if(organizationRepository.existsByPhonenumber(orgCreateRequest.getPhonenumber())) {
            return new ResponseEntity(new ApiResponse(false, "Phone number already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        
        

        // Creating user's account
        Organization org = new Organization(orgCreateRequest.getOrganizationName(), orgCreateRequest.getOrganizationCode(),
        		orgCreateRequest.getEmail(),orgCreateRequest.getPhonenumber(),true); //user enabled flag default is true for signup requests


		/*
		 * // Role userRole =
		 * roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new
		 * AppException("User Role not set."));
		 * 
		 * // user.setRoles(Collections.singleton(userRole));
		 */
      Organization result = organizationRepository.save(org);

       URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getOrganizationcode()).toUri();

       return ResponseEntity.created(location).body(new ApiResponse(true, "Organization registered successfully"));
    }

   

}
