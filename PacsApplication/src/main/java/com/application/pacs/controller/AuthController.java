package com.application.pacs.controller;

import com.application.pacs.exception.AppException;
import com.application.pacs.model.Organization;
import com.application.pacs.model.Role;
import com.application.pacs.model.RoleName;
import com.application.pacs.model.User;
import com.application.pacs.payload.ApiResponse;
import com.application.pacs.payload.security.JwtAuthenticationResponse;
import com.application.pacs.payload.security.LoginRequest;
import com.application.pacs.payload.security.SignUpRequest;
import com.application.pacs.repository.OrganizationRepository;
import com.application.pacs.repository.RoleRepository;
import com.application.pacs.repository.UserRepository;
import com.application.pacs.security.CurrentUser;
import com.application.pacs.security.JwtTokenProvider;
import com.application.pacs.security.UserPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.EmailAddressGrantee;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.S3Object;

import javax.validation.Valid;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;
	@Autowired
	OrganizationRepository organizationRepository;


	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/getAWSURL")
	public ResponseEntity<?> getAWSURL(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "objectname") String objectname,
			@RequestParam(value = "contentType") String contentType) {
		logger.info("User :" + currentUser.getUsername() + " trying to retrieve AWS signed URL for:"+objectname);
		Regions clientRegion = Regions.AP_SOUTH_1;
		logger.info("Organization code of current user is"+currentUser.getOrganizationcode());
		String bucketName = "panacea-pacs-app-test/"+currentUser.getOrganizationcode()+"/"+currentUser.getUsername();
		ProfileCredentialsProvider profile = new ProfileCredentialsProvider("AWS_S3_Profile.conf", "default");

		logger.info("access key id is :" + profile.getCredentials().getAWSAccessKeyId() + "::");
		logger.info("secret key is :" + profile.getCredentials().getAWSSecretKey() + "::");
		logger.info("File name is" + objectname);
		logger.info("content type is" + contentType);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(profile).withRegion(clientRegion).build();
	
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 240;
		expiration.setTime(expTimeMillis);

		System.out.println("Generating pre-signed URL.");
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
				objectname).withMethod(HttpMethod.PUT).withContentType(contentType).withExpiration(expiration);
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
		URL publicURL=s3Client.getUrl(bucketName, "");
		logger.info("PResigned URL genereated is" + url);
		logger.info("Public URL genereated is" + publicURL+objectname);
		return ResponseEntity.ok(url);

	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		
		
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByPhonenumber(signUpRequest.getPhonenumber())) {
			return new ResponseEntity(new ApiResponse(false, "Phone number already in use!"), HttpStatus.BAD_REQUEST);
		}

		if (!organizationRepository.existsByOrganizationcode(signUpRequest.getOrganizationcode())) {
			return new ResponseEntity(new ApiResponse(false, "Organization code doesnt exist in the system!"),
					HttpStatus.BAD_REQUEST);
		}

		// Creating user's account
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getOrganizationcode(),
				signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getPhonenumber(), true); // user
																												// enabled
																												// flag
																												// default
																												// is
																												// true
																												// for
																												// signup
																												// requests

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		logger.info("signup request User role  is" + signUpRequest.getUserrole());
		Role userRole = roleRepository.findByName(signUpRequest.getUserrole())
				.orElseThrow(() -> new AppException("User Role does not exist. Please contact administrator."));
		
		logger.info("signup request Supervisor name  is" + signUpRequest.getSupervisorname());
		User supervisorUser=userRepository.findByUsername(signUpRequest.getSupervisorname()).get();
		
		user.setSupervisors(Collections.singleton(supervisorUser));
		user.setSubordinates(Collections.singleton(user));
		user.setRoles(Collections.singleton(userRole));
		Organization org = organizationRepository.findByOrganizationcode(signUpRequest.getOrganizationcode())
				.orElseThrow(() -> new AppException("Organization code not found."));
		user.setOrganization(org);

		User result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}

}
