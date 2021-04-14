package com.application.pacs.service;

import com.application.pacs.exception.BadRequestException;
import com.application.pacs.exception.ResourceNotFoundException;
import com.application.pacs.model.*;
import com.application.pacs.payload.PagedResponse;
import com.application.pacs.payload.PollRequest;
import com.application.pacs.payload.PollResponse;
import com.application.pacs.payload.VoteRequest;
import com.application.pacs.payload.cases.AssignCase;
import com.application.pacs.payload.cases.AssignCaseIdRequest;
import com.application.pacs.payload.cases.AssignCaseList;
import com.application.pacs.payload.cases.AssignUserRequest;
import com.application.pacs.payload.cases.AssigneeResponse;
import com.application.pacs.payload.cases.CaseResponse;
import com.application.pacs.repository.CaseRepository;
import com.application.pacs.repository.OrganizationRepository;
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

import javax.validation.Valid;

@Service
public class CaseService {

	@Autowired
	private CaseRepository caseRepository;

	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(CaseService.class);

	public PagedResponse<CaseResponse> getCasesForUser(UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);

		String username = currentUser.getUsername();
		// Retrieve cases
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
		logger.info("Cases list requested by user:" + username);

		Set<Role> userRoles = user.getRoles();
		if (userRoles.isEmpty()) {
			logger.debug("User does not have any roles assigned:" + username);
			throw new BadRequestException("The user does not have any roles assigned. No cases can be viewed");
		}

		Page<Case> cases = null;
		for (Iterator<Role> it = userRoles.iterator(); it.hasNext();) {
			Role f = it.next();
			logger.info("Cases list requested with role: " + f.getName());
			if (f.getName().equals(RoleName.ROLE_RADIOLOGIST)) {
				Organization org;
				logger.info("here at 1");
				org = orgRepo.findByOrganizationcode(currentUser.getOrganizationcode()).get();
				List<Long> userids = Arrays.asList(user.getId());
				cases = caseRepository.findByUser_IdInAndOrg_Id(userids, org.getId(), pageable);
				logger.info("Cases list requested by a radiologist:" + username);
			}
			if (f.getName().equals(RoleName.ROLE_ASSIGNER) || f.getName().equals(RoleName.ROLE_ADMIN)) // remove true-
																										// only for
																										// testing
			{
				List<CaseStatus> caseStatuses = Arrays.asList(CaseStatus.CASESTATUS_OPEN,
						CaseStatus.CASESTATUS_ASSIGNED, CaseStatus.CASESTATUS_INPROGRESS,
						CaseStatus.CASESTATUS_DOWNLOADED);

				Organization org;
				logger.info("here at 1");
				org = orgRepo.findByOrganizationcode(currentUser.getOrganizationcode()).get();
				cases = caseRepository.findByCasestatusInAndOrg_Id(caseStatuses, org.getId(), pageable);
				if (cases.isEmpty())
					logger.info("returned cases are empty");
				logger.info("Cases list requested by a assigner" + username);
			} else {
				logger.info("User role doesnt allow for viewing of the cases:" + username);
				throw new BadRequestException("Your role doesnt allow for viewing of the cases");
			}
		}

		if (cases.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), cases.getNumber(), cases.getSize(),
					cases.getTotalElements(), cases.getTotalPages(), cases.isLast());
		}

		List<CaseResponse> caseResponses = cases.stream().map(eachcase -> {
			return ModelMapper.mapCaseToCaseResponse(eachcase);
		}).collect(Collectors.toList());

		return new PagedResponse<>(caseResponses, cases.getNumber(), cases.getSize(), cases.getTotalElements(),
				cases.getTotalPages(), cases.isLast());
	}

	public List<AssigneeResponse> getAssigneesForUser(UserPrincipal currentUser, RoleName assigneeType) {
		Long userId = currentUser.getId();
		String organizationCode = currentUser.getOrganizationcode();

		logger.info("Fetching assignee list for org:" + organizationCode + " and userId:" + userId);
		logger.info("convert to string " + RoleName.ROLE_USER.toString());
		List<Object[]> assigneeUsers = userRepository.getUsersInHierarchy(organizationCode, userId,
				RoleName.ROLE_USER.toString());
		List<AssigneeResponse> assignees = new ArrayList<AssigneeResponse>();
		if (!(assigneeUsers.isEmpty())) {
			logger.info("Fetched somne records");
			for (Object[] obj : assigneeUsers) {

				logger.info("Fetched assignee's username is:" + (String) obj[1]);
				logger.debug("Fetched assignee's username is:" + (String) obj[1]);
				AssigneeResponse assignee = new AssigneeResponse();
				assignee.setName((String) obj[0]);
				assignee.setUsername((String) obj[1]);
				assignee.setAssignedcasescount(10);
				assignees.add(assignee);
			}
		} else {
			logger.info("No records fetched");
			AssigneeResponse assignee = new AssigneeResponse();
			assignee.setName("No Active Users Found");
			assignee.setUsername(" ");
			assignee.setAssignedcasescount(0);
			assignees.add(assignee);
		}
		return assignees;
	}

	public PagedResponse<CaseResponse> assignCasesToUser(UserPrincipal currentUser,@Valid List<AssignCase> assignCaseRequest, int page,
			int size) {
		 int rowsUpdated=0;
		logger.info("Cases being assigned to user");
		 List<Long> caseIds=new ArrayList<Long>(); 
		  for (AssignCase temp : assignCaseRequest) {
			  
		  caseIds.add(temp.getCaseid()); //System.out.println(temp);
		  }
		if(assignCaseRequest.get(0).getAssignee().equalsIgnoreCase("unassign"))
		{
			logger.info("Unassigning the cases");
			 rowsUpdated= caseRepository.assignCasesToUser(caseIds);
		}else {
			//logger.info("Case id"+temp.getCaseid()+" being assigned to user:"+user.getId());
		  User user =userRepository.findByUsername(assignCaseRequest.get(0).getAssignee()).orElseThrow(() -> new ResourceNotFoundException("User", "username",assignCaseRequest.get(0).getAssignee()));
		 
		  
		   rowsUpdated= caseRepository.assignCasesToUser(user.getId(),caseIds);
		}
		  if(rowsUpdated==0) { throw new
		  BadRequestException("No cases were found for assignment"); }else { //What to do here? 
			  }
		  
		
		return getCasesForUser(currentUser, page, size);

	}

	private void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}

}
