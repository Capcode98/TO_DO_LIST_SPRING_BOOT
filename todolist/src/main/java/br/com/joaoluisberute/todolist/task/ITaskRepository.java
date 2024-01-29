package br.com.joaoluisberute.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;


public interface ITaskRepository extends JpaRepository<TaskModel, UUID>{
    @NonNull Optional<TaskModel> findById(@NonNull UUID id);
    List<TaskModel> findByIdUser(UUID idUser);
    TaskModel saveAndFlush(@NonNull Optional<TaskModel> taskModel);
    void deleteById(@NonNull UUID id);
}
