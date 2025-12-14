package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.awt.print.Book;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {
	//	@Autowired
	//TestRestTemplate template;
	private final Gson gson = (new GsonBuilderForHTTP()).getGson();

//	@Test
//	void contextLoads() {
//	}

	@Test
	void addFilm() {
/*
	//	Film film = new Film("Эпик 1", "Описание Эпика 1");
		ResponseEntity<Film[]> entity = template.getForEntity("/films", Film[].class);

		assertEquals(HttpStatus.OK,entity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON,entity.getHeaders().getContentType());

		Film[] films = entity.getBody();
        Assertions.assertNotNull(films);
        assertEquals(0, films.length);
		//assertEquals("97 Things Every Java Programmer Should Know",books[0].getTitle());
		//assertEquals("Spring Boot: Up and Running",books[1].getTitle());
		//assertEquals("Hacking with Spring Boot 2.3: Reactive Edition",books[2].getTitle());

*/

		String epicJson = gson.toJson(film);
		// создаём HTTP-клиент и запрос
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/epics");
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(201, response.statusCode());
		assertEquals(1, taskManager.getEpics().size(), "Некорректное количество эпика");
		assertEquals("Эпик 1", taskManager.getEpics().get(0).getName(), "Некорректное имя эпика");



	}



}
