package com.github.nagaseyasuhito.freesia.resource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.Test;

import com.github.nagaseyasuhito.freesia.entity.User;

public class UserResourceST {
	private URL url;

	private Client client = ClientBuilder.newClient();
	private List<String> invitationCodes = new ArrayList<>();

	@Before
	public void before() throws MalformedURLException {
		this.url = new URL(ResourceBundle.getBundle("stress-test").getString("url"));
	}

	@Test
	public void createAndShowLoop() {
		for (int i = 0; i < 1000; i++) {
			String mailAddress = UUID.randomUUID() + "@example.com";

			WebTarget target;
			target = this.client.target(this.url.toString());
			target = target.path("api/user");
			target = target.queryParam("mailAddress", mailAddress);
			if (!this.invitationCodes.isEmpty() && ThreadLocalRandom.current().nextBoolean()) {
				target = target.queryParam("invitationCode", this.invitationCodes.get(ThreadLocalRandom.current().nextInt(this.invitationCodes.size())));
			}

			User user = target.request().method("POST", User.class);
			this.invitationCodes.add(user.getInvitationCode());

			for (int j = 0; j < 10; j++) {
				target.path(user.getId().toString()).request().get(User.class);
			}
		}
	}
}
