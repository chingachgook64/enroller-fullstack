package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
	if (meeting == null) { 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	} 

	return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); 
	}
	
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
	if (meeting == null) { 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	} 
	
	Collection<Participant> participants = meeting.getParticipants();
	return new ResponseEntity<Collection>(participants, HttpStatus.OK); 
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
		
		if(meetingService.findById(meeting.getId())!=null ) {
			
			return new ResponseEntity("Unable to create. A meeting with id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
		}
		
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.PUT) 
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @PathVariable("login") String login) {
		
		ParticipantService participantService = new ParticipantService();
		Meeting meeting = meetingService.findById(id);
		
		if(meeting == null) {
			
			return new ResponseEntity("Unable to add participant to meeting . A meeting with id " + id + " does not exist.", HttpStatus.NOT_FOUND);
		}
		
		Participant participant = participantService.findByLogin(login);
		
		if(participant == null) {
			
			return new ResponseEntity("Unable to add participant to meeting . A participant  with login " + login + " does not exist.", HttpStatus.NOT_FOUND);
		}
		
		meeting.addParticipant(participant);
		meetingService.updateMeeting(meeting);
		
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT) 
	public ResponseEntity<?> updateMeeting(@RequestBody Meeting meeting) {
		
		if(meetingService.findById(meeting.getId()) == null ) {
			
			return new ResponseEntity("Unable to update. A meeting with id " + meeting.getId() + " does not exist.", HttpStatus.NOT_FOUND);
		}
		
		meetingService.updateMeeting(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") long id, @PathVariable("login") String login) {
	    Meeting meeting = meetingService.findById(id);
	
	if (meeting == null) { 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	}
	
	ParticipantService participantService = new ParticipantService();
	Participant participant = participantService.findByLogin(login);
	
	if (participant == null) { 
		return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

	meeting.removeParticipant(participant);
	meetingService.updateMeeting(meeting);
	
	return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT); 
	
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
	
	if (meeting == null) { 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	} 

	meetingService.delete(meeting);
	return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT); 
	}
	
}
