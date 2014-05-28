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

	public static Result index() {
		return redirect(routes.Application.books());
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result books() {
		// Todos os Livros do Banco de Dados
		List<Livro> result = getDao().findAllByClassName("Livro");
		return ok(views.html.index.render(result, bookForm));
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result newBook() {
		// Todos os Livros do Banco de Dados
		List<Livro> result = getDao().findAllByClassName("Livro");
		// O formulário dos Livros Preenchidos
		Form<Livro> filledForm = bookForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(result, filledForm));
		} else {
			// Persiste o Livro criado
			getDao().persist(filledForm.get());
			// Espelha no Banco de Dados
			getDao().flush();
			return redirect(routes.Application.books());
		}
	}

	@Transactional
	public static Result addAutor(Long id, String nome) {
		criaAutorDoLivro(id, nome);
		List<Livro> result = getDao().findAllByClassName("Livro");
		return ok(views.html.index.render(result, bookForm));
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
		return redirect(routes.Application.books());
	}

	public static GenericDAO getDao() {
		return dao;
	}

	public static void setDao(GenericDAO dao) {
		Application.dao = dao;
	}
}
