package funcional;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.GET;

import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;
import play.test.Helpers;
import base.AbstractTest;

public class IndexRouteTest extends AbstractTest{

	@Test
	public void rootRoute() {
		Result result = Helpers.route(new FakeRequest(GET, "/"));
		// testa se a resultado da requisição à url "/" não é nula
		assertThat(result).isNotNull();
	}

	@Test
	public void badRoute() {
		Result result = Helpers.route(new FakeRequest(GET, "/bad"));
		// com não existe uma route "/bad" o resultado deve ser nulo
		assertThat(result).isNull();
	}
}
