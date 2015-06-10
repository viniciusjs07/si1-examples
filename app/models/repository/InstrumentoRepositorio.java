package models.repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import models.Instrumento;

public class InstrumentoRepositorio extends GenericRepositoryImpl<Instrumento> {

	private static InstrumentoRepositorio instance;

	private InstrumentoRepositorio() {
		super(Instrumento.class);
	}

	public static InstrumentoRepositorio getInstance() {
		if (instance == null) {
			instance = new InstrumentoRepositorio();
		}
		return instance;
	}

	/**
	 * Procura o instrumento pelo {@code nome}
	 */
	public Instrumento findByName(String nome) {
		String hql = "FROM Instrumento a WHERE a.nome = :nome";
		TypedQuery<Instrumento> query = super.getEm().createQuery(hql,
				Instrumento.class);
		query.setParameter("nome", nome);
		try {
			return query.getSingleResult();
			// Caso não haja ninguém com o nome procurado
		} catch (NoResultException exception) {
			return null;
		}
	}
}
