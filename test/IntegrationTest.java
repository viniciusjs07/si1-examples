import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Test;

import play.GlobalSettings;
import play.libs.F.Callback;
import play.test.TestBrowser;
import play.test.WithBrowser;

//Classe para ser usada no fakeApplication, para que o Global.java
//não interfira nos testes.
class EmptyGlobal extends GlobalSettings {}

public class IntegrationTest extends WithBrowser{

    @Test
    public void deveCarregarPaginaPrincipal() {
    	//Não estamos usando o EmptyGlobal, então o Global.java
    	//será carregado, e o onStart() executará. Daí o fato de
    	//já termos livros para serem visualizados.
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                assertThat(browser.pageSource()).contains("Adiciona um livro");
                assertThat(browser.pageSource()).contains("Harry Potter");
                assertThat(browser.pageSource()).contains("50 livro(s)");
            }
        });
    }
    
    @Test
    public void deveAparecerLivroAdicionado() {
    	//Aqui usamos o EmptyGlobal
    	running(testServer(3333, fakeApplication(new EmptyGlobal())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                //O livro ainda não foi adicionado.
                assertThat(browser.pageSource()).doesNotContain("Os Irmãos Karamázov");
                browser.fill("#inputNome").with("Os Irmãos Karamázov");
                browser.click("#submitNewBook");
                //O livro foi adicionado.
                assertThat(browser.pageSource()).contains("Os Irmãos Karamázov");
            }
        });
    }
    
    @Test
    public void deveDeletarLivro() {
    	//Aqui usamos o EmptyGlobal
    	running(testServer(3333, fakeApplication(new EmptyGlobal())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                //O livro ainda não foi adicionado.
                assertThat(browser.pageSource()).doesNotContain("Os Irmãos Karamázov");
                browser.fill("#inputNome").with("Os Irmãos Karamázov");
                browser.click("#submitNewBook");
                //O livro foi adicionado.
                assertThat(browser.pageSource()).contains("Os Irmãos Karamázov");
                //Deletando o livro.
                browser.click("#deleteLivro1");
                //O livro foi deletado.
                assertThat(browser.pageSource()).doesNotContain("Os Irmãos Karamázov");
            }
        });
    }
}
