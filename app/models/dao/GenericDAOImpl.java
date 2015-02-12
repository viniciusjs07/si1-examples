package models.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import play.db.jpa.JPA;

/**
 * Camada genérica para acesso ao Banco de Dados
 */
public class GenericDAOImpl implements GenericDAO {
	// Resultados por página
	public static final int DEFAULT_RESULTS = 50;

	@Override
	public boolean persist(Object e) {
		JPA.em().persist(e);
		return true;
	}

	@Override
	public void flush() {
		JPA.em().flush();
	}

	@Override
	public void merge(Object e) {
		JPA.em().merge(e);
	}

	@Override
	public <T> T findByEntityId(Class<T> clazz, Long id) {
		return JPA.em().find(clazz, id);
	}

	@Override
	public <T> List<T> findAllByClassName(String className) {
		String hql = "FROM " + className;
		Query hqlQuery = JPA.em().createQuery(hql);
		return hqlQuery.getResultList();
	}

	@Override
	public <T> void removeById(Class<T> classe, Long id) {
		JPA.em().remove(findByEntityId(classe, id));
	}

	@Override
	public void remove(Object objeto) {
		JPA.em().remove(objeto);
	}

	@Override
	public <T> List<T> findByAttributeName(String className,
			String attributeName, String attributeValue) {
		String hql = "FROM " + className + " c" + " WHERE c." + attributeName
				+ " = '" + attributeValue + "'";
		Query hqlQuery = JPA.em().createQuery(hql);
		return hqlQuery.getResultList();
	}

	@Override
	public Query createQuery(String query) {
		return JPA.em().createQuery(query);
	}

	@Override
	public <T> Long countAllByClass(Class<T> clazz) {
		// Total de entidades
		CriteriaBuilder qb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(clazz)));
		return JPA.em().createQuery(cq).getSingleResult();
	}

	@Override
	public <T> List<T> findAllByClass(Class<T> clazz, int pageNumber,
			int pageSize) {
		CriteriaBuilder criteriaBuilder = JPA.em().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
		Root<T> from = criteriaQuery.from(clazz);
		CriteriaQuery<T> select = criteriaQuery.select(from);
		TypedQuery<T> typedQuery = JPA.em().createQuery(select);
		typedQuery.setFirstResult((pageNumber - 1) * pageSize);
		typedQuery.setMaxResults(pageSize);
		return typedQuery.getResultList();
	}
}
