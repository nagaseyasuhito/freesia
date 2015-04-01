package com.github.nagaseyasuhito.freesia.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

public abstract class BaseDao<T, I> {
	protected interface Query<T, R> {
		CriteriaQuery<R> execute(CriteriaBuilder builder, CriteriaQuery<R> query, Root<T> root);
	}

	protected interface EntityQuery<T> extends Query<T, T> {
	}

	@PersistenceContext
	private EntityManager entityManager;

	protected abstract Class<T> getEntityClass();

	protected List<T> getResultList(EntityQuery<T> query) {
		return this.createQuery(query, this.getEntityClass()).getResultList();
	}

	protected List<T> getResultList(EntityQuery<T> query, int firstResult, int maxResults) {
		return this.createQuery(query, this.getEntityClass()).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	protected Optional<T> getSingleResult(EntityQuery<T> query) {
		return this.getSingleResult(query, this.getEntityClass());
	}

	protected <R> Optional<R> getSingleResult(Query<T, R> query, Class<R> resultClass) {
		try {
			return Optional.of(this.createQuery(query, resultClass).getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	private <R> TypedQuery<R> createQuery(Query<T, R> query, Class<R> resultClass) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<R> criteriaQuery = criteriaBuilder.createQuery(resultClass);
		Root<T> root = criteriaQuery.from(this.getEntityClass());

		try {
			this.entityManager.unwrap(Connection.class).setReadOnly(true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return this.entityManager.createQuery(query.execute(criteriaBuilder, criteriaQuery, root));
	}

	public Optional<T> findById(I id) {
		try {
			this.entityManager.unwrap(Connection.class).setReadOnly(true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return Optional.ofNullable(this.entityManager.find(this.getEntityClass(), id));
	}

	public T merge(@Valid T entity) {
		return this.entityManager.merge(entity);
	}

	public void persist(@Valid T entity) {
		this.entityManager.persist(entity);
	}

	public void remove(@Valid T entity) {
		this.entityManager.remove(entity);
	}
}
