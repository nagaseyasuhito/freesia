package com.github.nagaseyasuhito.freesia.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import com.github.nagaseyasuhito.freesia.InvitationCodeNotFoundException;
import com.github.nagaseyasuhito.freesia.UserNotFoundException;
import com.github.nagaseyasuhito.freesia.dao.UserDao;
import com.github.nagaseyasuhito.freesia.entity.User;
import com.github.nagaseyasuhito.freesia.util.InvitationCodes;

@Transactional
public class UserService {

	@Inject
	private UserDao userDao;

	public User create(@NotNull String mailAddress, Optional<String> invitationCode) {
		User user = new User();
		user.setMailAddress(mailAddress);
		user.setInvitationCode(InvitationCodes.generate());
		user.setNumberOfInvitees(0);
		invitationCode.ifPresent(this::incrementNumberOfInvitees);

		this.userDao.persist(user);
		return user;
	}

	public User show(@NotNull Integer id) {
		return this.userDao.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	protected void incrementNumberOfInvitees(String invitationCode) {
		User inviter = this.userDao.findByInvitationCode(invitationCode).orElseThrow(() -> new InvitationCodeNotFoundException(invitationCode));

		inviter.setNumberOfInvitees(inviter.getNumberOfInvitees() + 1);
	}
}
