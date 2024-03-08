package com.evertec.springboot2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Base64;

import com.evertec.springboot2.crud.Application;
import com.evertec.springboot2.crud.model.Tarea;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TareaControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		System.out.println("** Puerto:" + port);
		return "http://localhost:" + port + "/api/v1";
	}

	private String getBase64Credentials(){
		String pass = "user1" + ":" + "pass1";
		return Base64.getEncoder().encodeToString(pass.getBytes());
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGetAllTareas() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + getBase64Credentials());
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/tareas",
				HttpMethod.GET, entity, String.class);
		
		assertNotNull(response.getBody());
	}

	@SuppressWarnings("null")
	@Test
	public void testGetTareaById() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + getBase64Credentials());
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Tarea> response = restTemplate.exchange(
            getRootUrl() + "/tareas/20",
            HttpMethod.GET,
            entity,
            Tarea.class
    );
	Tarea tarea = response.getBody();
    System.out.println(tarea.getDescripcion());
    assertNotNull(tarea);
	}

	@Test
	public void testCreateTarea() {
		Tarea tarea = new Tarea();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + getBase64Credentials());
		HttpEntity<Tarea> entity = new HttpEntity<>(tarea, headers);
	
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		tarea.setDescripcion("Nueva tarea desde TEST.");
		tarea.setFechaCreacion(timestamp);
		tarea.setVigente(true);
	
		ResponseEntity<Tarea> postResponse = restTemplate.postForEntity(getRootUrl() + "/tareas", entity, Tarea.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdateTarea() {
		int id = 20;
		Tarea tarea = restTemplate.getForObject(getRootUrl() + "/tareas/" + id, Tarea.class);
		tarea.setDescripcion("Update Tarea");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + getBase64Credentials());
		HttpEntity<Tarea> entity = new HttpEntity<>(tarea, headers);

		restTemplate.put(getRootUrl() + "/tareas/" + id, entity, tarea);

		Tarea updatedTarea = restTemplate.getForObject(getRootUrl() + "/tareas/" + id, Tarea.class);
		assertNotNull(updatedTarea);
	}

	@Test
	public void testDeleteTarea() {
		int id = 1;
		Tarea tarea = restTemplate.getForObject(getRootUrl() + "/tareas/" + id, Tarea.class);
		assertNotNull(tarea);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + getBase64Credentials());
		HttpEntity<Tarea> entity = new HttpEntity<>(tarea, headers);

		restTemplate.delete(getRootUrl() + "/tareas/" + id, entity);

		try {
			tarea = restTemplate.getForObject(getRootUrl() + "/tareas/" + id, Tarea.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
