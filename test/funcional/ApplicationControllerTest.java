package funcional;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.redirectLocation;
import static play.test.Helpers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import models.Anuncio;
import models.repository.GenericDAO;

import org.junit.Test;

import play.mvc.Http;
import play.mvc.Result;
import base.AbstractTest;

public class ApplicationControllerTest extends AbstractTest {

	Result result;
	EntityManager em;
	
	@Test
	public void callIndex() {
		// realiza a chamada ao método index() do Application
		result = callAction(controllers.routes.ref.Application.index(),
				fakeRequest());
		// ao chamar o metodo index do Application, ele redireciona para '/anuncios'
		assertThat(status(result)).isEqualTo(Http.Status.SEE_OTHER);
		assertThat(redirectLocation(result)).isEqualTo("/anuncio");
	}
	
	@Test
	public void callAnuncios() {
		// realiza a chamada ao método anuncioss() do Application
		result = callAction(controllers.routes.ref.Application.anuncio(),
				fakeRequest());
		// ao chamar o método index do Application, ele retora o html
		// correspondente.
		assertThat(status(result)).isEqualTo(Http.Status.OK);
		assertThat(charset(result)).isEqualTo("utf-8");
		assertThat(contentAsString(result)).contains("0 anuncios(s)");
	}
	
	@Test
	public void callNewAnuncio() {
		// Mapa com os dados do formulario para criação do anuncio
		Map<String, String> formData = new HashMap<String, String>();
		formData.put("nome", "Calculo I");
		formData.put("cidade", "Campina");
		formData.put("bairro", "Centro");
		formData.put("email","vinicius@ccc.br");
		formData.put("facebook","vinicius@ccc.br");
		formData.put("objetivo","Procuro Banda");
		formData.put("instrumentos","violao");
		formData.put("estiloGosta","rock");
		formData.put("estiloNaoGosta","brega");
		formData.put("codigo","3636");
		
		// realiza a chamada ao método newBook() do Application com o
		// formulário.
		result = callAction(
				controllers.routes.ref.Application.newAnuncio(), fakeRequest()
						.withFormUrlEncodedBody(formData));
		
		// ao chamar o método newBook do Application, ele adiciona o livro e
		// redireciona para a url '/books'
		assertThat(status(result)).isEqualTo(Http.Status.SEE_OTHER);
		assertThat(redirectLocation(result)).isEqualTo("/visualizarAnuncio");

		// testa se realmente adicionou o anuncio com nome "Calculo I" no banco de
		// dados.
        GenericDAO dao = new GenericDAO();
		List<Anuncio> anuncios = dao.findAllByClass(Anuncio.class);
		assertThat(anuncios.size()).isEqualTo(1);
        assertThat(anuncios.get(0).getNome()).isEqualTo("Calculo I");
		List<Anuncio> result2 = dao.findByAttributeName("Anuncio",
				"nome", "Calculo I");	
		assertThat(result2.size()).isEqualTo(1);
		
		// verifica o html gerado pela url '/anuncios' que é chamada dentro do newAnuncio
		result = callAction(controllers.routes.ref.Application.anuncio(),
				fakeRequest());
		assertThat(status(result)).isEqualTo(Http.Status.OK);
		assertThat(contentAsString(result)).contains("1 anuncio(s)");
	}
}