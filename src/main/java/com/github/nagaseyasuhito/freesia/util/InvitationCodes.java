package com.github.nagaseyasuhito.freesia.util;

import java.util.UUID;

public class InvitationCodes {

	public static String generate() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
