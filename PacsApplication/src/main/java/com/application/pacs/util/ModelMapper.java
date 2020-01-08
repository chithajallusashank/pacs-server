package com.application.pacs.util;

import com.application.pacs.controller.CaseController;
import com.application.pacs.model.Case;
import com.application.pacs.model.Poll;
import com.application.pacs.model.User;
import com.application.pacs.payload.ChoiceResponse;
import com.application.pacs.payload.PollResponse;
import com.application.pacs.payload.cases.CaseResponse;
import com.application.pacs.payload.users.UserSummary;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelMapper {
	private static final Logger logger = LoggerFactory.getLogger(ModelMapper.class);
    public static CaseResponse mapCaseToCaseResponse(Case cases) {
    	CaseResponse caseResponse = new CaseResponse();
        caseResponse.setAssignedto(cases.getUser()!= null? cases.getUser().getName() : "NA");
        caseResponse.setBodyparttype(cases.getBodyparttype());
        caseResponse.setCasestatus(cases.getCasestatus());
        caseResponse.setCasetype(cases.getCasetype());
        caseResponse.setEmergency(cases.getEmergency());
        caseResponse.setFileuri(cases.getFileuri());
        caseResponse.setPatienthistory(cases.getPatienthistory());
        caseResponse.setPatientid(cases.getPatientid());
        caseResponse.setPatientname(cases.getPatientname());
        return caseResponse;
    }

    
    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator, Long userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());

            if(choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        pollResponse.setCreatedBy(creatorSummary);

        if(userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);

        return pollResponse;
    }

    
    
}
