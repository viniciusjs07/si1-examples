package models.repository;

import models.Livro;

/**
 * Repositório de Livros, que prove as funções básicas para o livro
 */
public class LivroRepository extends GenericRepositoryImpl<Livro> {

	private static LivroRepository instance;

	private LivroRepository() {
		super(Livro.class);
	}

	/**
	 * Retorna a instância única do repositório de livros
	 */
	public static LivroRepository getInstance() {
		if (instance == null) {
			instance = new LivroRepository();
		}
		return instance;
	}
}
