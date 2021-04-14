package com.application.pacs.controller;

import com.application.pacs.exception.ResourceNotFoundException;
import com.application.pacs.model.RoleName;
import com.application.pacs.model.User;
import com.application.pacs.payload.ApiResponse;
import com.application.pacs.payload.PagedResponse;
import com.application.pacs.payload.PollResponse;
import com.application.pacs.payload.cases.AssigneeResponse;
import com.application.pacs.payload.users.UserProfile;
import com.application.pacs.payload.users.SupervisorListResponse;
import com.application.pacs.payload.users.UserIdentityAvailability;
import com.application.pacs.payload.users.UserSummary;
import com.application.pacs.repository.PollRepository;
import com.application.pacs.repository.UserRepository;
import com.application.pacs.repository.VoteRepository;
import com.application.pacs.security.CurrentUser;
import com.application.pacs.security.UserPrincipal;
import com.application.pacs.service.PollService;
import com.application.pacs.util.AppConstants;
<<<<<<< HEAD

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

=======
//updated file with comments to be committed
>>>>>>> branch 'master' of https://github.com/pacsapplication/pacs-server
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
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }
    
    
    @GetMapping("/user/getSupervisorListForOrg")
    public List<SupervisorListResponse> getSupervisorListForOrg(@RequestParam(value = "orgValue") String orgValue) {
    	logger.info("Finding the supervisors for the given organization: " +orgValue);
       
        List<User> supervisorList=userRepository.findByOrganizationcodeAndUserenabled(orgValue,true);
    	List <SupervisorListResponse> supervisors=new ArrayList<SupervisorListResponse>();
    	if(!(supervisorList.isEmpty()))
    	{
    		logger.info("Fetched some records");
    	for (User obj : supervisorList) {
    	   
    		logger.info("Fetched assignee's name is:"+obj.getName());
    		logger.debug("Fetched assignee's username is:"+ obj.getUsername());
    		SupervisorListResponse supervisor=new SupervisorListResponse();
    		supervisor.setName(obj.getName());
    		supervisor.setUsername(obj.getUsername());
    		supervisors.add(supervisor);
    		
    	}
    	}
    	else {
    		logger.info("No records fetched");
    		SupervisorListResponse supervisor=new SupervisorListResponse();
    		supervisor.setName("No active supervisors found");
    		supervisor.setUsername("");
    		supervisors.add(supervisor);
    	}
    	return supervisors;
       
    }
    
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/enabledisableUsername")
    public ResponseEntity<?> disableUsername(@RequestParam(value = "username") String username,
    												@RequestParam(value = "enable") Boolean enable) {
        int updatedRecord = userRepository.enabledisableUsername(username,enable);
       
        if(updatedRecord>0)
       return new ResponseEntity(new ApiResponse(true, "Username has been disabled!"),
                HttpStatus.OK);
       return new ResponseEntity(new ApiResponse(false, "Username disable failed!"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }
    
    @GetMapping("/user/checkPhonenumberAvailability")
    public UserIdentityAvailability checkPhonenumberAvailability(@RequestParam(value = "phonenumber") Long phonenumber) {
        Boolean isAvailable = !userRepository.existsByPhonenumber(phonenumber);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);

        return userProfile;
    }

    @GetMapping("/users/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }


    @GetMapping("/users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsVotedBy(username, currentUser, page, size);
    }

}
