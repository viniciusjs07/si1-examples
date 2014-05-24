package controllers;

import java.util.List;

import javax.persistence.Query;

import models.GenericDAO;
import models.Livro;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
	static Form<Livro> bookForm = Form.form(Livro.class);
	private static GenericDAO dao = new GenericDAO();

	@Transactional
	public static Result index() {
		return redirect(routes.Application.books());
	}

	@Transactional
	public static Result books() {
		String hql = "from Livro";
		Query query = JPA.em().createQuery(hql);
		List<Livro> result = query.getResultList();
		return ok(views.html.index.render(result, bookForm));
	}

	@Transactional
	public static Result newBook() {
		String hql = "from Livro";
		Query query = JPA.em().createQuery(hql);
		List<Livro> result = query.getResultList();
		Form<Livro> filledForm = bookForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(result, filledForm));
		} else {
			getDao().salvar(filledForm.get());
			return redirect(routes.Application.books());
		}
	}

	@Transactional
	public static Result deleteBook(Long id) {
		getDao().deletaPeloId(Livro.class, id);
		getDao().espelhar();
		return redirect(routes.Application.books());
	}

	public static GenericDAO getDao() {
		return dao;
	}

	public static void setDao(GenericDAO dao) {
		Application.dao = dao;
	}
}
