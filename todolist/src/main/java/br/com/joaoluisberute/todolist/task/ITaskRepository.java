package br.com.joaoluisberute.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ITaskRepository extends JpaRepository<TaskModel, UUID>{

    //Adicionar um metodo para encontrar uma task por ID
 
}
