package models.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import play.db.jpa.JPA;

/**
 * Camada genérica para acesso ao Banco de Dados
 */
public abstract class GenericRepositoryImpl<Entidade> {
	// Resultados por página
	public static final int DEFAULT_RESULTS = 50;

	private Class<Entidade> clazz;

	public GenericRepositoryImpl(Class<Entidade> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Persiste uma entidade no Banco de Dados.
	 */
	public void persist(Entidade e) {
		getEm().persist(e);
	}

	/**
	 * Espelha o estado do DAO com o banco de Dados, deve ser feito após um
	 * persist, ou merge.
	 */
	public void flush() {
		getEm().flush();
	}

	/**
	 * Atualiza a informação da entidade do código com a do banco de dados.
	 */
	public void merge(Entidade e) {
		getEm().merge(e);
	}

	/**
	 * Procura uma certa {@code clazz} pelo seu {@code id}.
	 */
	public Entidade findByEntityId(Long id) {
		return getEm().find(clazz, id);
	}

	/**
	 * Procura todos os objetos de uma certa classe pelo seu {@code className}
	 * descrito na Entidade.
	 */
	public List<Entidade> findAll() {
		String hql = "FROM " + clazz.getName();
		Query hqlQuery = getEm().createQuery(hql);
		return hqlQuery.getResultList();
	}

	/**
	 * Deleta do banco de dados uma {@code classe} referenciada pelo seu
	 * {@code id}.
	 */
	public void removeById(Long id) {
		getEm().remove(findByEntityId(id));
	}

	/**
	 * Remove o respectivo {@code objeto} do banco de dados.
	 */
	public void remove(Entidade objeto) {
		getEm().remove(objeto);
	}

	/**
	 * Procura uma certa {@code className} pelo seu {@code attributeName}.
	 */
	public List<Entidade> findByAttributeName(String attributeName,
			String attributeValue) {
		String hql = "FROM " + clazz.getName() + " c" + " WHERE c."
				+ attributeName + " = '" + attributeValue + "'";
		Query hqlQuery = getEm().createQuery(hql);
		return hqlQuery.getResultList();
	}

	/**
	 * Cria uma Query HQL
	 */
	public Query createQuery(String query) {
		return getEm().createQuery(query);
	}

	/**
	 * Retorna quantas entidades da {@code clazz} estão no banco de dados
	 */
	public Long countAll() {
		// Total de entidades
		CriteriaBuilder qb = getEm().getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(clazz)));
		return getEm().createQuery(cq).getSingleResult();
	}

	/**
	 * Retorna todos as entidades da {@code clazz} paginado pelo
	 * {@code pageNumber} com o tamanho da {@code pageSize}
	 */
	public List<Entidade> findAll(int pageNumber, int pageSize) {
		CriteriaBuilder criteriaBuilder = getEm().getCriteriaBuilder();
		CriteriaQuery<Entidade> criteriaQuery = criteriaBuilder
				.createQuery(clazz);
		Root<Entidade> from = criteriaQuery.from(clazz);
		CriteriaQuery<Entidade> select = criteriaQuery.select(from);
		TypedQuery<Entidade> typedQuery = getEm().createQuery(select);
		typedQuery.setFirstResult((pageNumber - 1) * pageSize);
		typedQuery.setMaxResults(pageSize);
		return typedQuery.getResultList();
	}

	public EntityManager getEm() {
		return JPA.em();
	}
}
