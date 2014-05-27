package controllers;

import java.util.List;

import models.Livro;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

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
		List<Livro> result = getDao().findAllByClassName("Livro");
		return ok(views.html.index.render(result, bookForm));
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result newBook() {
		List<Livro> result = getDao().findAllByClassName("Livro");
		Form<Livro> filledForm = bookForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(result, filledForm));
		} else {
			getDao().persist(filledForm.get());
			getDao().flush();
			return redirect(routes.Application.books());
		}
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result deleteBook(Long id) {
		getDao().removeById(Livro.class, id);
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
