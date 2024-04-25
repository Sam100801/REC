package it.unisalento.recproject.taskservice.repository;

import it.unisalento.recproject.taskservice.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByAssignedUserEmail(String userEmail);
    List<Task> findByCreatoDa(String userEmail);
}
