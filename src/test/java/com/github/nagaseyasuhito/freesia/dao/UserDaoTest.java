package com.github.nagaseyasuhito.freesia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaDelete;

import mockit.Deencapsulation;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.nagaseyasuhito.freesia.entity.User;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

public class UserDaoTest {

	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	private UserDao userDao = new UserDao();

	@BeforeClass
	public static void beforeClass() {
		Map<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
		properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:");

		properties.put("eclipselink.logging.level" + ".sql", "FINE");
		properties.put("eclipselink.logging.parameters", "true");

		entityManagerFactory = Persistence.createEntityManagerFactory("freesia", properties);
	}

	@AfterClass
	public static void afterClass() {
		entityManagerFactory.close();
	}

	@Before
	public void before() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		User user = new User();
		user.setMailAddress("freesia@example.com");
		user.setInvitationCode("0123456789");
		user.setNumberOfInvitees(0);
		entityManager.persist(user);
		entityManager.getTransaction().commit();
		entityManager.close();

		entityManagerFactory.getCache().evictAll();

		this.entityManager = entityManagerFactory.createEntityManager();
		Deencapsulation.setField(this.userDao, this.entityManager);
		this.entityManager.getTransaction().begin();
	}

	@After
	public void after() {
		this.entityManager.getTransaction().commit();

		// delete all users
		this.entityManager.getTransaction().begin();
		CriteriaDelete<User> criteriaDelete = this.entityManager.getCriteriaBuilder().createCriteriaDelete(User.class);
		this.entityManager.createQuery(criteriaDelete).executeUpdate();
		this.entityManager.getTransaction().commit();

		this.entityManager.close();
	}

	@Test
	public void findByInvitationCodeSuccess() {
		assertThat(this.userDao.findByInvitationCode("unknown invitation code").orElse(null), nullValue());
		assertThat(this.userDao.findByInvitationCode("0123456789").orElse(null), CoreMatchers.notNullValue());
	}
}
