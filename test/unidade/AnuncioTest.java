package unidade;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

import java.util.List;

import models.Anuncio;

import org.junit.Test;

import play.db.jpa.JPA;
import play.test.*;

import models.Anunciante;
import models.repository.GenericDAO;

import play.test.FakeApplication;

public class AnuncioTest extends WithBrowser {
	private GenericDAO dao = new GenericDAO();
	private List<Anuncio> anuncios;

	@Override
	public FakeApplication provideFakeApplication() {
		return fakeApplication(inMemoryDatabase());
	}

	@Test
	public void test() {
		JPA.withTransaction(new play.libs.F.Callback0() {

			@Override
			public void invoke() throws Throwable {
				anuncios = dao.findAllByClass(Anuncio.class); // consulta o bd
				assertThat(anuncios.size()).isEqualTo(0);

				Anuncio a1 = new Anuncio("Anuncio1", "campinaGRande",
						"BelaVista", "violao", "vinicius@com.br",
						"vinicius@com.br", "encontrar banda", "rock", "brega",
						"3636");
				dao.persist(a1);

				anuncios = dao.findAllByClass(Anuncio.class); // consulta o bd
				assertThat(anuncios.size()).isEqualTo(1);
				assertThat(anuncios.get(0).getNome()).isEqualTo("Anuncio1");
				assertThat(anuncios.get(0).getCidade()).isEqualTo(
						"campinaGRande");
				assertThat(anuncios.get(0).getBairro()).isEqualTo("BelaVista");
				assertThat(anuncios.get(0).getInstrumentos()).isEqualTo(
						"violao");
				assertThat(anuncios.get(0).getEmail()).isEqualTo(
						"vinicius@com.br");
				assertThat(anuncios.get(0).getFace()).isEqualTo(
						"vinicius@com.br");
				assertThat(anuncios.get(0).getObjetivo()).isEqualTo(
						"encontrar banda");
				assertThat(anuncios.get(0).getEstiloGosta()).isEqualTo("rock");
				assertThat(anuncios.get(0).getEstiloNaoGosta()).isEqualTo(
						"brega");
				assertThat(anuncios.get(0).getCodigo()).isEqualTo("3636");
			}

		});
	}
}
