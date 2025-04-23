package br.campospadilhaa.tasks.apitest;

import java.net.MalformedURLException;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {

	// execuado uma vez antes de todos os teste
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}

	/*@Test
	public void teste() {

		// given: dado
		// when: quando
		// then: então

		RestAssured
			.given()
				.log().all()
			.when()
				.get("http://localhost:8001/tasks-backend/todo")
			.then()
				.log().all();
	}*/

	@Test
	public void retornarTarefas() {

		// given: dado
		// when: quando
		// then: então

		RestAssured
			.given()
			.when()
				.get("/todo")
			.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	public void deveAdicionarTarefaComSucesso() {

		// given: dado
		// when: quando
		// then: então

		RestAssured
			.given()
				.body("{\"task\": \"Nome da tarefa via API\", \"dueDate\": \"2030-12-30\"}")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.log().all()
				.statusCode(201);
	}

	@Test
	public void naoDeveAdicionarTarefaInvalida() {

		// given: dado
		// when: quando
		// then: então

		RestAssured
			.given()
				.body("{\"task\": \"Nome da tarefa via API\", \"dueDate\": \"2010-12-30\"}")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.log().all()
				.statusCode(400)
				.body("message", CoreMatchers.is("Due date must not be in past"));
	}

	@Test
	public void deveRemoverTarefaComSucesso() throws MalformedURLException {

		// given: dado
		// when: quando
		// then: então

		// inserindo tarefa
		Integer id = 
			RestAssured
				.given()
					.body("{\"task\": \"Nome da tarefa para remoção\", \"dueDate\": \"2030-12-30\"}")
					.contentType(ContentType.JSON)
				.when()
					.post("/todo")
				.then()
					.log().all()
					.statusCode(201)
	
					// obtenção do atributo 'id' para remover
					.extract().path("id");

		System.out.println(id);

		// remover a tarefa inserida
		RestAssured
			.given()
			.when()
				.delete("/todo/"+id)
			.then()
				.statusCode(204);
	}
}