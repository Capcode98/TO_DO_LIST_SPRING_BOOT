package br.com.joaoluisberute.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;

@RestController
@RequestMapping("/Task")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/CreateTask")
    public ResponseEntity<TaskModel> CreatTask(@NonNull @RequestBody TaskModel taskModel) {

        var taskCreated = this.taskRepository.save(taskModel);
        
        return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
    }
}
