import java.util.ArrayList;
import java.util.List;

import models.Livro;
import models.repository.LivroRepository;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;

public class Global extends GlobalSettings {

	private static LivroRepository livroRepository = LivroRepository
			.getInstance();

	private List<Livro> livros = new ArrayList<>();

	@Override
	public void onStart(Application app) {
		Logger.info("Aplicação inicializada...");

		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				try {
					livros = livroRepository.findAll();
					if (livros.size() == 0) {
						for (int i = 0; i < 3000; i++) {
							livroRepository.persist(new Livro("Harry Potter "
									+ i));
						}
						livroRepository.flush();
					}
				} catch (Exception ex) {
					Logger.debug(ex.getMessage());
				}
			}
		});
	}

	@Override
	public void onStop(Application app) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Logger.info("Aplicação finalizando...");
				try {
					livros = livroRepository.findAll();
					for (Livro livro : livros) {
						livroRepository.removeById(livro.getId());
					}
				} catch (Exception ex) {
					Logger.debug("Problema na finalização: " + ex.getMessage());
				}
			}
		});
	}

}