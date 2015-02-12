package controllers;

import java.util.List;

import models.Autor;
import models.Livro;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controlador Principal do Sistema
 */
public class Application extends Controller {
	static Form<Livro> bookForm = Form.form(Livro.class);
	private static GenericDAO dao = new GenericDAOImpl();
	private static final int FIRST_PAGE = 1;

	// Regex de um inteiro positivo
	public static Result index() { 
		return redirect(routes.Application.books(FIRST_PAGE, GenericDAOImpl.DEFAULT_RESULTS));
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result books(int page, int pageSize) {
		page = page >= FIRST_PAGE ? page : FIRST_PAGE;
		pageSize = pageSize >= FIRST_PAGE ? pageSize : GenericDAOImpl.DEFAULT_RESULTS;
		Long entityNumber = dao.countAllByClass(Livro.class);
		// Se a página pedida for maior que o número de entidades
		if (page > (entityNumber / pageSize)) {
			// A última página
			page = (int) (Math.ceil(entityNumber / Float.parseFloat(String.valueOf(pageSize))));
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
			return redirect(routes.Application.books(FIRST_PAGE, GenericDAOImpl.DEFAULT_RESULTS));
		}
	}

	@Transactional
	public static Result addAutor(Long id, String nome) {
		criaAutorDoLivro(id, nome);
		return ok(views.html.index.render(firstPage(), bookForm));
	}

	private static void criaAutorDoLivro(Long id, String nome) {
		// Cria um novo Autor para um livro de {@code id}
		Autor novoAutor = new Autor();
		novoAutor.setNome(nome);
		// Procura um objeto da classe Livro com o {@code id}
		Livro livroDaListagem = getDao().findByEntityId(Livro.class, id);
		// Faz o direcionamento de cada um
		livroDaListagem.getAutores().add(novoAutor);
		novoAutor.getLivros().add(livroDaListagem);
		// Persiste o Novo Autor
		getDao().persist(novoAutor);
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
		return redirect(routes.Application.books(FIRST_PAGE, GenericDAOImpl.DEFAULT_RESULTS));
	}

	/**
	 * Retorna a primeira página do banco de dados
	 */
	private static List<Livro> firstPage() {
		return getDao().findAllByClass(Livro.class, FIRST_PAGE,
				GenericDAOImpl.DEFAULT_RESULTS);
	}

	public static GenericDAO getDao() {
		return dao;
	}

	public static void setDao(GenericDAO dao) {
		Application.dao = dao;
	}
}
