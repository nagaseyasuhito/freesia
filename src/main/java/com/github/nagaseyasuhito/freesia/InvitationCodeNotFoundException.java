package com.github.nagaseyasuhito.freesia;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvitationCodeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 2451320016989753436L;

	private String invitationCode;
}
