package controllers;

import java.util.ArrayList;
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
	private static List<Livro> livros = new ArrayList<Livro>();
	public static Result index() {
		return redirect(routes.Application.books());
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result books() {
		// Todos os Livros do Banco de Dados de acordo com a {@code pageNumber}
		if(livros.isEmpty()){
			return redirect(routes.Application.moreBooks());
		}
		return ok(views.html.index.render(livros, bookForm));
	}
	
	@Transactional
	public static Result reload() {
		livros = new ArrayList<>();
		return redirect(routes.Application.books());
	}
	
	@Transactional
	public static Result moreBooks() {
		// Todos os Livros do Banco de Dados de acordo com a {@code pageNumber}
		int pageNumber = (int) Math.ceil((livros.size() + 1 )/ (float) GenericDAOImpl.MAX_RESULTS);
		livros.addAll(getDao().findAllByClass(Livro.class, pageNumber));
		return ok(views.html.index.render(livros, bookForm));
	}
	
	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result newBook() {
		// O formulário dos Livros Preenchidos
		Form<Livro> filledForm = bookForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(livros, filledForm));
		} else {
			Livro livro = filledForm.get();
			// Persiste o Livro criado
			getDao().persist(livro);
			// Espelha no Banco de Dados
			getDao().flush();
			livros.add(livro);
			return redirect(routes.Application.books());
		}
	}

	@Transactional
	public static Result addAutor(Long id, String nome) {
		criaAutorDoLivro(id, nome);
		return ok(views.html.index.render(livros, bookForm));
	}

	private static void criaAutorDoLivro(Long id, String nome) {
		// Cria um novo Autor para um livro de {@code id}
		Autor novoAutor = new Autor();
		novoAutor.setNome(nome);
		// Procura um objeto da classe Livro com o {@code id}
		Livro livroDaListagem = getDao().findByEntityId(Livro.class, id);
		int index = livros.indexOf(livroDaListagem);
		// Faz o direcionamento de cada um
		livroDaListagem.getAutores().add(novoAutor);
		novoAutor.getLivros().add(livroDaListagem);
		// Persiste o Novo Autor
		getDao().persist(novoAutor);
		// Atualiza as informações do livro
		getDao().merge(livroDaListagem);
		// Espelha no Banco de Dados
		getDao().flush();
		livros.set(index, livroDaListagem);
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result deleteBook(Long id) {
		Livro livroDaListagem = getDao().findByEntityId(Livro.class, id);
		livros.remove(livroDaListagem);
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
