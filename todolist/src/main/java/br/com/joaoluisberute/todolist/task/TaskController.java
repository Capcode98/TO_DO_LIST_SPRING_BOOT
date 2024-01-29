package br.com.joaoluisberute.todolist.task;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;


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
    public TaskModel EditTask(@PathVariable @NonNull UUID id, @RequestBody TaskModel taskModel, HttpServletRequest request) {

        var entity = this.taskRepository.findById(id);

        if (taskModel != null) {
            BeanUtils.copyProperties(entity, taskModel, "id", "idUser", "createdAt");
        }

        System.out.println("entity: "+entity);
        System.out.println("taskModel: "+taskModel);

        TaskModel taskSaved = this.taskRepository.saveAndFlush(entity);
        
        System.out.println("taskSaved: "+taskSaved);
        
        return taskSaved;
    }

    //Criar um metodo Delete
    @DeleteMapping("/DeleteTask/{id}")
    public void deleteTask(@NonNull @PathVariable UUID id){
        try{
            this.taskRepository.deleteById(id);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Erro ao deletar a task");
        }
    }
    

    //Criar um metodo Update
}
