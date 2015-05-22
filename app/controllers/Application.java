package controllers;

import java.util.List;

import models.Autor;
import models.Livro;
import models.dao.GenericRepository;
import models.dao.GenericRepositoryImpl;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.collect.Iterables;

/**
 * Controlador Principal do Sistema
 */
public class Application extends Controller {
	static Form<Livro> bookForm = Form.form(Livro.class);
	private static GenericRepository dao = new GenericRepositoryImpl();
	private static final int FIRST_PAGE = 1;

	// Regex de um inteiro positivo
	public static Result index() {
		return redirect(routes.Application.books(FIRST_PAGE,
				GenericRepositoryImpl.DEFAULT_RESULTS));
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result books(int page, int pageSize) {
		page = page >= FIRST_PAGE ? page : FIRST_PAGE;
		pageSize = pageSize >= FIRST_PAGE ? pageSize
				: GenericRepositoryImpl.DEFAULT_RESULTS;
		Long entityNumber = dao.countAllByClass(Livro.class);
		// Se a página pedida for maior que o número de entidades
		if (page > (entityNumber / pageSize)) {
			// A última página
			page = (int) (Math.ceil(entityNumber
					/ Float.parseFloat(String.valueOf(pageSize))));
		}
		session("actualPage", String.valueOf(page));
		return ok(views.html.index.render(
				getDao().findAllByClass(Livro.class, page, pageSize), bookForm));
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result newBook() {
		// O formulário dos Livros Preenchidos
		Form<Livro> filledForm = bookForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(firstPage(), filledForm));
		} else {
			Livro livro = filledForm.get();
			// Persiste o Livro criado
			getDao().persist(livro);
			// Espelha no Banco de Dados
			getDao().flush();
			return redirect(routes.Application.books(FIRST_PAGE,
					GenericRepositoryImpl.DEFAULT_RESULTS));
		}
	}

	@Transactional
	public static Result addAutor(Long id, String nome) {
		criaAutorDoLivro(id, nome);
		return ok(views.html.index.render(firstPage(), bookForm));
	}

	private static void criaAutorDoLivro(Long id, String nome) {
		Query query = getDao().createQuery("from autor a where a.nome=:nome");
		query.setParameter("nome", nome);
		
		Autor autor = (Autor) query.uniqueResult();
		
		// Procura um objeto da classe Livro com o {@code id}
		Livro livroDaListagem = getDao().findByEntityId(Livro.class, id);
		// Cria um novo Autor para um livro de {@code id}
		if (autor == null) {
			autor = new Autor();
			autor.setNome(nome);
			// Persiste o Novo Autor
			getDao().persist(autor);
		}
		autor.getLivros().add(livroDaListagem);
		livroDaListagem.getAutores().add(autor);
		// Atualiza as informações do livro
		getDao().merge(livroDaListagem);
		// Espelha no Banco de Dados
		getDao().flush();
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result deleteBook(Long id) {
		// Remove o Livro pelo Id
		getDao().removeById(Livro.class, id);
		// Espelha no banco de dados
		getDao().flush();
		return redirect(routes.Application.books(FIRST_PAGE,
				GenericRepositoryImpl.DEFAULT_RESULTS));
	}

	/**
	 * Retorna a primeira página do banco de dados
	 */
	private static List<Livro> firstPage() {
		return getDao().findAllByClass(Livro.class, FIRST_PAGE,
				GenericRepositoryImpl.DEFAULT_RESULTS);
	}

	public static GenericRepository getDao() {
		return dao;
	}

	public static void setDao(GenericRepository dao) {
		Application.dao = dao;
	}
}
