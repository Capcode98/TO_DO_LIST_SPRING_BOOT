package br.com.joaoluisberute.todolist.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

@RestController
@RequestMapping("/Task")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/CreateTask")
    public ResponseEntity<TaskModel> CreatTask(@NonNull @RequestBody TaskModel taskModel, HttpServletRequest request) {

        // Adicionanado a task antes de salvar o id do Usuario que a criou
        taskModel.setIdUser((UUID) request.getAttribute("idUser"));

        // Salvando a task no DB e em uma variavel para ser retornada na response
        var taskCreated = this.taskRepository.save(taskModel);
        
        // Retornando um response com um estatus "OK" e as informações da task criada
        return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
    }

    //Criar um metodo Get
    
    //Criar um metodo Put

    //Criar um metodo Delete

    //Criar um metodo Update
}
