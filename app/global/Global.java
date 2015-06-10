package global;

import models.Estilo;
import models.Instrumento;
import models.repository.EstiloRepositorio;
import models.repository.InstrumentoRepositorio;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;
/**
 * 
 * @author Vinicius
 *
 */
public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		super.onStart(app);

		Logger.info("Application has started");

		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {

				String[] instrumentos = new String[] { "Guitarra", "Bateria",
						"Contra-Baixo", "Violão" };
				String[] estilos = new String[] { "Rock", "Pop", "Forró",
						"Jazz", "Country", "Samba", "Pagode", "Hip-Hop" };
				for (String instrumento : instrumentos) {
					Instrumento inst = new Instrumento(instrumento);
					InstrumentoRepositorio.getInstance().persist(inst);

				}
				InstrumentoRepositorio.getInstance().flush();
				for (String estilo : estilos) {
					Estilo est = new Estilo(estilo);
					EstiloRepositorio.getInstance().persist(est);
				}
				EstiloRepositorio.getInstance().flush();

			}
		});
	}

	@Override
	public void onStop(Application app) {
		super.onStop(app);

		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Logger.info("Application shutdown");
			}
		});
	}

}
