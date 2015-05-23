package models.repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import models.Autor;

/**
 * Repositório de Autor, que prove as funções básicas para o autor
 */
public class AutorRepository extends GenericRepositoryImpl<Autor> {

	private static AutorRepository instance;

	private AutorRepository() {
		super(Autor.class);
	}

	/**
	 * Retorna a instância única do repositório de livros
	 */
	public static AutorRepository getInstance() {
		if (instance == null) {
			instance = new AutorRepository();
		}
		return instance;
	}

	/**
	 * Procura o autor pelo {@code nome}
	 */
	public Autor findByName(String nome) {
		String hql = "FROM Autor a WHERE a.nome = :nome";
		TypedQuery<Autor> query = super.getEm().createQuery(hql, Autor.class);
		query.setParameter("nome", nome);
		try {
			return query.getSingleResult();
			// Caso não haja ninguém com o nome procurado
		} catch (NoResultException exception) {
			return null;
		}
	}
}
