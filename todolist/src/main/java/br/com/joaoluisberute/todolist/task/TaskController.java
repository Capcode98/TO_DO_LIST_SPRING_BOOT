package br.com.joaoluisberute.todolist.task;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.joaoluisberute.todolist.Utils.Utils;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import lombok.var;


@RestController
@RequestMapping("/Task")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    //Metodo para criar uma task
    @PostMapping("/CreateTask")
    public ResponseEntity<TaskModel> CreatTask(@NonNull @RequestBody TaskModel taskModel, HttpServletRequest request) {

        // Adicionanado a task antes de salvar o id do Usuario que a criou
        taskModel.setIdUser((UUID) request.getAttribute("idUser"));

        // Adiciona a data e hora atual no momento da criação da task
        var currentDateTime = LocalDateTime.now();

        if(taskModel.getTimeToInitTaskAt()==null){

            taskModel.setTimeToInitTaskAt(currentDateTime);

        }

        if(currentDateTime.isAfter(taskModel.getTimeToInitTaskAt()) || currentDateTime.isAfter(taskModel.getTimeToFinalTaskAt())){

            taskModel.setIdUser(null);

            return ResponseEntity.status(401).body(taskModel);
        }

        if (taskModel.getTimeToInitTaskAt().isAfter(taskModel.getTimeToFinalTaskAt())) {

            taskModel.setIdUser(null);

            return ResponseEntity.status(401).body(taskModel);
        }

        // Salvando a task no DB e em uma variavel para ser retornada na response
        var taskCreated = this.taskRepository.save(taskModel);
        
        // Retornando um response com um estatus "OK" e as informações da task criada
        return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
    }

    //Metodo para ver todas as tasks
    @GetMapping("/SeeAllMyTasks")
    public List<TaskModel> SeeAllMyTasks(HttpServletRequest request) {

        var idUser = request.getAttribute("idUser");
        
        // Buscando a task no DB e em uma variavel para ser retornada na response
        var listOfTasksCreated = this.taskRepository.findByIdUser((UUID) idUser);
        
        // Retornando um response com um estatus "OK" e as informações da task criada
        return listOfTasksCreated;
    }

    //Metodo para ver uma task especifica
    @GetMapping("/SeeMySpecificTask/{id}")
    public TaskModel SeeMySpecificTask(@PathVariable UUID id, HttpServletRequest request) {

        var idUser = request.getAttribute("idUser");
        
        // Buscando a task no DB e em uma variavel para ser retornada na response
        var listOfTasksCreated = this.taskRepository.findByIdUser((UUID) idUser);

        for (TaskModel taskModel : listOfTasksCreated) {

            if(taskModel.getId().equals(id)){

                return taskModel;
            }
        }
        return null;
    }
    
    //Metodo para editar uma task
    @PutMapping("/EditTask/{id}")
    public ResponseEntity<TaskModel> EditTask(@PathVariable @NonNull UUID id,@NonNull @RequestBody TaskModel taskModel, @NonNull HttpServletRequest request) {

        var idUser = request.getAttribute("idUser");
        
        // Buscando a task no DB e em uma variavel para ser retornada na response
        var listOfTasksCreated = this.taskRepository.findByIdUser((UUID) idUser);

        for (TaskModel entity : listOfTasksCreated) {

            if(entity.getId().equals(id)){

                Utils.copyNonNullProperties(taskModel, entity);
        
                TaskModel taskSaved = this.taskRepository.save(entity);
        
                // Retornando um response com um estatus "OK" e as informações da task criada
                return ResponseEntity.status(HttpStatus.OK).body(taskSaved);
            }
        }

        // Retornando um response com um estatus "OK" e as informações da task criada
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(taskModel);

    }

    //Criar um metodo Delete
    @DeleteMapping("/DeleteTask/{id}")
    public void deleteTask(@NonNull @PathVariable UUID id, @NonNull HttpServletRequest request){

        var idUser = request.getAttribute("idUser");
        
        // Buscando a task no DB e em uma variavel para ser retornada na response
        TaskModel task = this.taskRepository.findByIdAndIdUser((UUID)id, (UUID) idUser);

        if(task == null){

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Task não encontrada ou não pertence ao seu usuario");

        }
        
        else{
            
            try{

                this.taskRepository.deleteById(id);

            }catch(Exception e){

                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Erro ao deletar a task");
            }
        }
    }
}
