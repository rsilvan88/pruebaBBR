package com.evertec.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evertec.springboot2.crud.model.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long>{

}
