import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

import org.junit.Test;

import play.GlobalSettings;
import play.libs.F.Callback;
import play.test.*;

public class IntegrationTest extends WithBrowser {

    @Override
    public FakeApplication provideFakeApplication(){
        return fakeApplication(inMemoryDatabase());
    }

    @Test
    public void deveCarregarPaginaPrincipal() {
        // O Global.java
        //será carregado, e o onStart() executará. Daí o fato de
        //já termos livros para serem visualizados.
        browser.goTo("http://localhost:" + testServer.port());
        assertThat(browser.pageSource()).contains("Adiciona um livro");
        assertThat(browser.pageSource()).contains("Harry Potter");
        // aqui testamos também se o Global.java conseguiu adicionar os livros no BD
        assertThat(browser.pageSource()).contains("50 livro(s)");
    }

    @Test
    public void deveDeletarLivro() {
        browser.goTo("http://localhost:" + testServer.port());

        assertThat(browser.pageSource()).contains("Harry Potter 49");
        //Deletando o livro.
        browser.click("#deleteLivro50");
        //O livro foi deletado.
        assertThat(browser.pageSource()).doesNotContain("Harry Potter 49");
    }

    @Test
    public void deveAparecerLivroAdicionado() {
        browser.goTo("http://localhost:" + testServer.port());
        //O livro ainda não foi adicionado.
        assertThat(browser.pageSource()).doesNotContain("Os Irmãos Karamázov");
        browser.fill("#inputNome").with("Os Irmãos Karamázov");
        browser.click("#submitNewBook");
        browser.goTo("http://localhost:" + testServer.port() + "/books?page=61");
        //O livro foi adicionado, mas está na última página
        assertThat(browser.pageSource()).contains("Os Irmãos Karamázov");
    }
}
