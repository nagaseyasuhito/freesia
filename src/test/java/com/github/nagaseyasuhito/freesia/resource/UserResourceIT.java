package com.github.nagaseyasuhito.freesia.resource;

import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.nagaseyasuhito.freesia.entity.User;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

@RunWith(Arquillian.class)
@RunAsClient
public class UserResourceIT {
	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive war = ShrinkWrap.create(WebArchive.class);
		war.addPackages(true, "com.github.nagaseyasuhito.freesia");
		war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		war.addAsResource("META-INF/persistence.xml");
		return war;
	}

	@ArquillianResource
	private URL url;

	private Client client = ClientBuilder.newClient();

	@Test
	public void createSuccess() {
		WebTarget target = this.client.target(this.url.toString()).path("api/user");

		User firstUser = target.queryParam("mailAddress", "first@example.com").request().method("POST", User.class);
		assertThat(firstUser.getId(), notNullValue());

		User secondUser = target.queryParam("mailAddress", "second@example.com").queryParam("invitationCode", firstUser.getInvitationCode()).request().method("POST", User.class);
		assertThat(secondUser.getId(), notNullValue());
	}

	@Test
	public void showSuccess() {
		WebTarget target = this.client.target(this.url.toString()).path("api/user");

		User user = target.queryParam("mailAddress", "show@example.com").request().method("POST", User.class);
		assertThat(user.getId(), notNullValue());

		User secondUser = target.path(user.getId().toString()).request().get(User.class);
		assertThat(secondUser.getId(), notNullValue());
	}
}
