package com.github.nagaseyasuhito.freesia.resource;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.github.nagaseyasuhito.freesia.entity.User;
import com.github.nagaseyasuhito.freesia.service.UserService;

@Path("user")
public class UserResource {
	@Inject
	private UserService userService;

	@POST
	public User register(@QueryParam("mailAddress") String mailAddress, @QueryParam("invitationCode") String invitationCode) {
		return this.userService.register(mailAddress, Optional.ofNullable(invitationCode));
	}

	@Path("{id}")
	@GET
	public User show(@PathParam("id") Integer id) {
		return this.userService.show(id);
	}
}
