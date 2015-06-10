package controllers;

import java.util.List;

import models.Anunciante;
import models.Anuncio;
import models.ContadorDeInformacao;
import models.Estilo;
import models.Instrumento;
import models.repository.AnuncianteRepositorio;
import models.repository.AnuncioRepositorio;
import models.repository.EstiloRepositorio;
import models.repository.GenericDAO;
import models.repository.GenericRepositoryImpl;
import models.repository.InstrumentoRepositorio;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controlador Principal do Sistema Vinicius
 */
public class Application extends Controller {
	private static Form<Anuncio> anuncioForm = Form.form(Anuncio.class);
	private static final GenericRepositoryImpl<Anuncio> anuncioRepositorio = AnuncioRepositorio
			.getInstance();
	private static final GenericRepositoryImpl<Anunciante> anuncianteRepositorio = AnuncianteRepositorio
			.getInstance();
	private static final GenericRepositoryImpl<Instrumento> instrumentoRepositorio = InstrumentoRepositorio
			.getInstance();
	private static final GenericRepositoryImpl<Estilo> estilosRepositorio = EstiloRepositorio
			.getInstance();
	private static ContadorDeInformacao contador = new ContadorDeInformacao();
	private static final GenericDAO dao = new GenericDAO();

	public static Result index() {
		return redirect(routes.Application.anuncio());
	}

	/*
	 * public static Result criarAnuncio() { return
	 * redirect(routes.Application.criar()); }
	 */

	@Transactional
	public static Result criar() {
		// Todos os Objetos do Banco de Dados
		List<Anuncio> result = dao.findAllByClass(Anuncio.class);

		// List<Instrumento> result2 = instrumentoRepositorio.findAll();
		// List<Estilo> result3 = estilosRepositorio.findAll();
		return ok(views.html.criar.render(result));
	}

	/*
	 * A Anotação transactional é necessária em todas as Actions que usarem o
	 * BD.
	 */
	@Transactional
	public static Result anuncio() {
		// Todos os Anuncios do Banco de Dados
		List<Anuncio> result = dao.findAllByClass(Anuncio.class);
		return ok(views.html.index.render(result, contador));
	}

	@Transactional
	public static Result newAnuncio() {
		// O formulário dos Anuncio Preenchidos
		DynamicForm filledForm = Form.form().bindFromRequest();
		String objetivo = "";

		if (filledForm.get("objetivo").equals("Tocar Ocasionalmente")) {
			objetivo = "Tocar Ocasionalmente";
		} else if (filledForm.get("objetivo").equals("Procuro banda")) {
			objetivo = "Procuro banda";
		} else if (filledForm.get("objetivo").equals("Procuro musico")) {
			objetivo = "Procuro musico";
		}

		if (filledForm.hasErrors()) {
			List<Anuncio> result = dao.findAllByClass(Anuncio.class);
			// TODO falta colocar na interface mensagem de erro.
			return badRequest(views.html.index.render(result, contador));
		} else {
			Anuncio novoAnuncio = new Anuncio(filledForm.get("nome"),
					filledForm.get("cidade"), filledForm.get("bairro"),
					filledForm.get("instrumentos"), filledForm.get("email"),
					filledForm.get("facebook"), objetivo,
					filledForm.get("estiloGosta"),
					filledForm.get("estiloNaoGosta"), filledForm.get("codigo"));
			Logger.debug("Criando anúncio: " + filledForm.data().toString()
					+ " como " + novoAnuncio.getNome());
			// Persiste o Anuncio criado
			dao.persist(novoAnuncio);
			// Espelha no Banco de Dados
			dao.flush();
			/*
			 * Usar routes.Application.<uma action> é uma forma de evitar
			 * colocar rotas literais (ex: "/books") hard-coded no código. Dessa
			 * forma, se mudamos no arquivo routes, continua funcionando.
			 */

			return redirect(routes.Application.anuncio());
		}
	}

	/*
	 * @Transactional public static Result addAnunciante(Long id, String nome) {
	 * criaAnunciante(id, nome); return redirect(routes.Application.anuncio());
	 * }
	 */

	/*
	 * private static void criaAnunciante(Long id, String nome) { // Cria um
	 * novo Anunciante para um Anuncio de {@code id} Anunciante anunciante = new
	 * Anunciante(nome); // Procura um objeto da classe Anuncio com o {@code id}
	 * Anuncio anuncio = anuncioRepositorio.findByEntityId(id); // Faz o
	 * direcionamento de cada um anuncio.addAnunciante(anunciante);
	 * anunciante.addAnuncio(anuncio); // Persiste o Novo Anunciante
	 * anuncianteRepositorio.persist(anunciante);
	 * 
	 * 
	 * // Espelha no Banco de Dados anuncioRepositorio.flush(); }
	 */

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result deleteAnuncio(Long id) {
		DynamicForm deleteForm = Form.form().bindFromRequest();
		if (deleteForm.get("codigo").equals(
				dao.findByEntityId(Anuncio.class, id).getCodigo())) {
			// Remove o Anuncio pelo Id
			dao.removeById(Anuncio.class, id);
			// Espelha no banco de dados
		}
		if (deleteForm.get("optradio").equals("1")) {
			contador.setResolvido(contador.getResolvido() + 1);

		} else {
			contador.setNaoResolvido(contador.getNaoResolvido() + 1);
		}
		if (contador.getId() == null) {
			dao.persist(contador);
		} else {
			dao.merge(contador);
		}

		dao.flush();
		return redirect(routes.Application.anuncio());
	}

	@Transactional
	public static Result pesquisa() {
		List<Anuncio> result = dao.findAllByClass(Anuncio.class);
		return ok(views.html.pesquisar.render(result));
	}

	@Transactional
	public static Result pesquisarAnuncio() {
		DynamicForm pesquisarForm = Form.form().bindFromRequest();
		List<Anuncio> result = null;
		if (pesquisarForm.get("optradio").equals("1")) {
			result = dao.findByAttributeName("Anuncio", "nome",
					pesquisarForm.get("pesquisa"));
		} else if (pesquisarForm.get("optradio").equals("2")) {
			result = dao.findByAttributeInstumento(pesquisarForm
					.get("pesquisa"));
		} else if (pesquisarForm.get("optradio").equals("3")) {
			result = dao.findByAttributeEstilo(pesquisarForm.get("pesquisa"));
		} else if (pesquisarForm.get("optradio").equals("4")) {
			if (pesquisarForm.get("objetivo").equals("Tocar Ocasionalmente")) {
				result = dao.findByAttributeName("Anuncio", "objetivo",
						"Tocar Ocasionalmente");
			} else if (pesquisarForm.get("objetivo").equals("Procuro Banda")) {
				result = dao.findByAttributeName("Anuncio", "objetivo",
						"Procuro Banda");
			} else if (pesquisarForm.get("objetivo").equals("Procuro musico")) {
				result = dao.findByAttributeName("Anuncio", "objetivo",
						"Procuro musico");
			}
		} else if (pesquisarForm.get("optradio").equals("5")) {
			result = dao.findByAttributeName("Anuncio", "objetivo",
					"Procuro Banda");
		} else if (pesquisarForm.get("optradio").equals("6")) {
			result = dao.findByAttributeName("Anuncio", "objetivo",
					"Procuro musico");
		}
		if (result == null) {
			return redirect(routes.Application.anuncio());
		}

		return ok(views.html.pesquisar.render(result));
	}
}
