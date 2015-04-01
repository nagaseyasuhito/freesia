package com.github.nagaseyasuhito.freesia.controller;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.github.nagaseyasuhito.freesia.service.UserService;

@Named("registerBean")
@RequestScoped
@Data
public class RegisterController {

	@NotNull
	private String mailAddress;

	@Inject
	private UserService userService;

	public String register() {
		this.userService.register(this.mailAddress, Optional.empty());

		return "registrationFinished";
	}
}
