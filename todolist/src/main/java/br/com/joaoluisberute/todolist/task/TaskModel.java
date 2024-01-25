package br.com.joaoluisberute.todolist.task;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id 
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(length = 200)
    private String title;
    private String description;

    @Column(length = 20)
    private String status;
    
    @Column(length = 20)
    private String priority;
    private LocalDateTime initTask;
    private LocalDateTime finalTask;
    private Time timeInTask;


    @CreationTimestamp
    private LocalDateTime createdAt;

    private UUID idUser;
}
