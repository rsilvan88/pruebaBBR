package com.evertec.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evertec.springboot2.crud.exception.ResourceNotFoundException;
import com.evertec.springboot2.crud.model.Tarea;
import com.evertec.springboot2.crud.repository.TareaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Tareas", description = "Operaciones relacionadas con el CRUD tareas.")
public class TareaController {
	@Autowired
	private TareaRepository tareaRepository;

	@GetMapping("/tareas")
	@ApiOperation("Obtener todas las tareas")
	public List<Tarea> getAllTareas() {
		return tareaRepository.findAll();
	}

	@GetMapping("/tareas/{id}")
	@ApiOperation("Obtener una tarea por su ID")
	public ResponseEntity<Tarea> getTareaById(@PathVariable(value = "id") Long tareaId)
			throws ResourceNotFoundException {
		@SuppressWarnings("null")
		Tarea tarea = tareaRepository.findById(tareaId)
				.orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada para este id :: " + tareaId));
		return ResponseEntity.ok().body(tarea);
	}

	@SuppressWarnings("null")
	@PostMapping("/tareas")
	@ApiOperation("Crear una nueva tarea")
	public Tarea createTarea(@Valid @RequestBody Tarea tarea) {
		return tareaRepository.save(tarea);
	}

	@PutMapping("/tareas/{id}")
	@ApiOperation("Actualizar una tarea existente")
	public ResponseEntity<Tarea> updateTarea(@PathVariable(value = "id") Long tareaId,
			@Valid @RequestBody Tarea tareaDetails) throws ResourceNotFoundException {
		@SuppressWarnings("null")
		Tarea tarea = tareaRepository.findById(tareaId)
				.orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada para este id :: " + tareaId));

		tarea.setDescripcion(tareaDetails.getDescripcion());
		tarea.setFechaModificacion(tareaDetails.getFechaModificacion());
		tarea.setVigente(tareaDetails.isVigente());

		final Tarea updatedTarea = tareaRepository.save(tarea);
		return ResponseEntity.ok(updatedTarea);
	}

	@SuppressWarnings("null")
	@DeleteMapping("/tareas/{id}")
	@ApiOperation("Eliminar una tarea existente")
	public Map<String, Boolean> deleteTarea(@PathVariable(value = "id") Long tareaId)
			throws ResourceNotFoundException {
		Tarea tarea = tareaRepository.findById(tareaId)
				.orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada para este id :: " + tareaId));

				tareaRepository.delete(tarea);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
