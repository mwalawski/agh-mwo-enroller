package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

import javax.servlet.http.Part;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Collection<Participant> getAllSorted(String sortBy, String sortOrder) {
		String hql = "FROM Participant ORDER BY :sortBy :sortOrder";
		Query query = connector.getSession().createQuery(hql);
		query.setParameter("sortBy", sortBy);
		query.setParameter("sortOrder", sortOrder);
		return query.list();
	}

	public Participant findByLogin(String login) {
		String hql = "FROM Participant where login = :login";
		Query<Participant> query = connector.getSession().createQuery(hql, Participant.class);
		query.setParameter("login", login);
		return query.uniqueResult();
	}

	public Participant addNew(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();

		return participant;
	}

	public Participant updateOne(
			Participant existingParticipant,
			Participant updateParticipant) {
		Transaction transaction = connector.getSession().beginTransaction();
		existingParticipant.setPassword(updateParticipant.getPassword());
		transaction.commit();

		return existingParticipant;
	}

	public void removeOne(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}
}
