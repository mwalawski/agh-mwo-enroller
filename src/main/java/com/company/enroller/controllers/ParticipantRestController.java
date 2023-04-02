package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(
			@RequestParam("sortBy") String sortBy,
			@RequestParam("sortOrder") String sortOrder) {

		Collection<Participant> participants;
		if (sortBy != null) {
			participants = participantService.getAllSorted(sortBy, sortOrder);
		} else {
			participants = participantService.getAll();
		}
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}

		Participant newParticipant = participantService.addNew(participant);

		return new ResponseEntity<Participant>(newParticipant, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> putParticipant(
			@PathVariable("id") String login,
			@RequestBody Participant insertParticipant) {
		insertParticipant.setLogin(login);
		Participant existingParticipant = participantService.findByLogin(login);

		if (existingParticipant == null) {
			existingParticipant = participantService.addNew(insertParticipant);
		} else {
			existingParticipant = participantService.updateOne(existingParticipant, insertParticipant);
		}
		return new ResponseEntity<Participant>(existingParticipant, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<String>(
					"Participant with given login does not exist",
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participantService.removeOne(participant);
		return new ResponseEntity<String>(
				"Successfully removed participant",
				HttpStatus.OK);
	}

}
