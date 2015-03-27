package com.github.nagaseyasuhito.freesia.dao;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.github.nagaseyasuhito.freesia.entity.User;
import com.github.nagaseyasuhito.freesia.entity.User_;

public class UserDao extends BaseDao<User, Integer> {

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	public Optional<User> findByInvitationCode(@NotNull String invitationCode) {
		return this.getSingleResult((b, q, r) -> q.select(r).where(b.equal(r.get(User_.invitationCode), invitationCode)));
	}

	public Optional<Long> countNumberOfInvitees(@NotNull Integer id) {
		return this.getSingleResult((b, q, r) -> q.select(b.count(r)).where(b.equal(r.get(User_.id), id)), Long.class);
	}
}
