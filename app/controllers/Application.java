package controllers;

import java.util.List;

import models.Autor;
import models.Livro;
import models.repository.AutorRepository;
import models.repository.LivroRepository;
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
	private static LivroRepository livroRepository = LivroRepository
			.getInstance();
	private static AutorRepository autorRepository = AutorRepository
			.getInstance();
	private static final int FIRST_PAGE = 1;

	// Regex de um inteiro positivo
	public static Result index() {
		return redirect(routes.Application.books(FIRST_PAGE,
				LivroRepository.DEFAULT_RESULTS));
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result books(int page, int pageSize) {
		page = page >= FIRST_PAGE ? page : FIRST_PAGE;
		pageSize = pageSize >= FIRST_PAGE ? pageSize
				: LivroRepository.DEFAULT_RESULTS;
		Long entityNumber = livroRepository.countAll();
		// Se a página pedida for maior que o número de entidades
		if (page > (entityNumber / pageSize)) {
			// A última página
			page = (int) (Math.ceil(entityNumber
					/ Float.parseFloat(String.valueOf(pageSize))));
		}
		session("actualPage", String.valueOf(page));
		return ok(views.html.index.render(
				livroRepository.findAll(page, pageSize), bookForm));
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
			livroRepository.persist(livro);
			// Espelha no Banco de Dados
			livroRepository.flush();
			return redirect(routes.Application.books(FIRST_PAGE,
					LivroRepository.DEFAULT_RESULTS));
		}
	}

	@Transactional
	public static Result addAutor(Long id, String nome) {
		criaAutorDoLivro(id, nome);
		return ok(views.html.index.render(firstPage(), bookForm));
	}

	private static void criaAutorDoLivro(Long id, String nome) {
		Autor autor = autorRepository.findByName(nome);
		// Procura um objeto da classe Livro com o {@code id}
		Livro livroDaListagem = livroRepository.findByEntityId(id);
		// Cria um novo Autor para um livro de {@code id}
		if (autor == null) {
			autor = new Autor();
			autor.setNome(nome);
			// Persiste o Novo Autor
			autorRepository.persist(autor);
		}
		autor.getLivros().add(livroDaListagem);
		livroDaListagem.getAutores().add(autor);
		// Atualiza as informações do livro
		livroRepository.merge(livroDaListagem);
		// Espelha no Banco de Dados
		livroRepository.flush();
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result deleteBook(Long id) {
		// Remove o Livro pelo Id
		livroRepository.removeById(id);
		// Espelha no banco de dados
		livroRepository.flush();
		return redirect(routes.Application.books(FIRST_PAGE,
				LivroRepository.DEFAULT_RESULTS));
	}

	/**
	 * Retorna a primeira página do banco de dados
	 */
	private static List<Livro> firstPage() {
		return livroRepository.findAll(FIRST_PAGE,
				LivroRepository.DEFAULT_RESULTS);
	}
}
