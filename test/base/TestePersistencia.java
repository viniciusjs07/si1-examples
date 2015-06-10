package base;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

import java.util.ArrayList;
import java.util.List;

import models.Anuncio;
import models.Estilo;
import models.Instrumento;
import models.repository.AnuncioRepositorio;
import models.repository.GenericRepositoryImpl;

import org.junit.Test;

import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.F.Callback;
import play.test.*;
/**
 * 
 * @author Vinicius
 *
 */
public class TestePersistencia extends WithBrowser {

	@Override
	public FakeApplication provideFakeApplication() {
		return fakeApplication(inMemoryDatabase());
	}

	@Test
	public void test() {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {

				Anuncio anuncio = new Anuncio("vinicius", "timbauba", "centro",
						"guitarra", "vinicius@gmail", "face.vinicius",
						"tocar numa banda", "rock", "samba", "1234");
				GenericRepositoryImpl<Anuncio> anuncioRepositorio = AnuncioRepositorio
						.getInstance();

				anuncioRepositorio.persist(anuncio);
				List<Anuncio> anuncioLista;

				anuncioLista = anuncioRepositorio.findAll(); // carrega os
																// livros com
																// seu autor
				assertThat(anuncioLista.size()).isEqualTo(1);
				assertThat(anuncioLista.get(0).getNome()).isEqualTo("vinicius");
				assertThat(anuncioLista.get(0).getCidade()).isEqualTo(
						"timbauba");
				assertThat(anuncioLista.get(0).getBairro()).isEqualTo("centro");
				assertThat(anuncioLista.get(0).getInstrumentos()).isEqualTo(
						"guitarra");
				assertThat(anuncioLista.get(0).getEstiloGosta()).isEqualTo(
						"rock");
				assertThat(anuncioLista.get(0).getFace()).isEqualTo(
						"face.vinicius");
				assertThat(anuncioLista.get(0).getObjetivo()).isEqualTo(
						"tocar numa banda");
			}
		});

	}

}
