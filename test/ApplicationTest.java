import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.Helpers;
import scala.Option;

import com.google.common.collect.ImmutableMap;

import controllers.Application;

/**
 * 
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 * 
 */
public class ApplicationTest {
	private int PAGE_NUMBER = 1;
	private int PAGE_SIZE = 50;
	private long ID_ONE = 1L;
	
	public EntityManager em;

    @Before
    public void setUp() {
    	FakeApplication app = Helpers.fakeApplication(new GlobalSettings());
    	Helpers.start(app);
        Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
        em = jpaPlugin.get().em("default");
        JPA.bindForCurrentThread(em);
        em.getTransaction().begin();
    }

	@Test
	public void indexDeveRedirecionar() {
		Result result = Application.index();
		
		//Como o index retorna um redirect, espera-se que o
		//status da requisição seja SEE_OTHER (303)
		assertThat(status(result)).isEqualTo(SEE_OTHER);
	}
	
	@Test
	public void deveConseguirAdicionarEListarLivros() {
		//Primeiro se deve criar um livro por meio do próprio controller. Se tentar criar pelo repositório, 
		//haverá um erro na transação. Em testes de controller você deve sempre usar somente o controller para
		//criar ou carregar dados.
		FakeRequest fakeRequest = new FakeRequest().withFormUrlEncodedBody(ImmutableMap.of("nome", "Sidarta"));
		
		Result resultPost = callAction(controllers.routes.ref.Application.newBook(), fakeRequest);
		assertThat(status(resultPost)).isEqualTo(SEE_OTHER);
		
		Result resultGet = callAction(controllers.routes.ref.Application.books(PAGE_NUMBER, PAGE_SIZE), new FakeRequest());
		assertThat(status(resultGet)).isEqualTo(OK);
		assertThat(contentType(resultGet)).isEqualTo("text/html");
		assertThat(contentAsString(resultGet)).contains("Sidarta");
	}
	
	@Test
	public void deveConseguirDeletarLivros() {
		//Criando dois livros: o primeiro será deletado, o segundo não
		FakeRequest fakeRequestSidarta = new FakeRequest().withFormUrlEncodedBody(ImmutableMap.of("nome", "Sidarta"));
		FakeRequest fakeRequestLoboDaEstepe = new FakeRequest().withFormUrlEncodedBody(ImmutableMap.of("nome", "O Lobo da Estepe"));
		
		Result resultPostSidarta = callAction(controllers.routes.ref.Application.newBook(), fakeRequestSidarta);
		assertThat(status(resultPostSidarta)).isEqualTo(SEE_OTHER);
		Result resultPostLoboDaEstepe = callAction(controllers.routes.ref.Application.newBook(), fakeRequestLoboDaEstepe);
		assertThat(status(resultPostLoboDaEstepe)).isEqualTo(SEE_OTHER);
		
		//Como o id de Livro está anotado com @GeneratedValue, ele segue a sequência 1, 2, 3...
		//Portanto, deletamos o livro com ID 1, que foi o primeiro a ser adicionado.
		Result resultDelete = callAction(controllers.routes.ref.Application.deleteBook(ID_ONE), new FakeRequest());
		assertThat(status(resultDelete)).isEqualTo(SEE_OTHER);
		
		Result resultGet = callAction(controllers.routes.ref.Application.books(PAGE_NUMBER, PAGE_SIZE), new FakeRequest());
		assertThat(status(resultGet)).isEqualTo(OK);
		//O livro deletado não aparece mais na view.
		assertThat(contentAsString(resultGet)).doesNotContain("Sidarta");
		//O outro livro continua lá.
		assertThat(contentAsString(resultGet)).contains("O Lobo da Estepe");
	}
	
	@After
    public void tearDown() {
        em.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        em.close();
    }
}
