package it.unisalento.recproject.taskservice.Service;

import it.unisalento.recproject.taskservice.domain.Task;
import it.unisalento.recproject.taskservice.domain.TaskStatus;
import it.unisalento.recproject.taskservice.dto.ListTaskDTO;
import it.unisalento.recproject.taskservice.dto.TaskDTO;
import it.unisalento.recproject.taskservice.exceptions.TaskNotFoundException;
import it.unisalento.recproject.taskservice.repository.TaskRepository;
import it.unisalento.recproject.taskservice.security.AuthorizationAPI;
import it.unisalento.recproject.taskservice.security.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    JwtUtilities jwtUtilities;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO, HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        Task task = new Task();
        task.setDescrizione(taskDTO.getDescrizione());
        task.setAssignedUserId(taskDTO.getAssignedUserId());
        task.setLinkGitHub(taskDTO.getLinkGitHub());
        task.setDataCreazione(LocalDateTime.now());
        task.setStatus(TaskStatus.DAFARE);
        task.setTitolo(taskDTO.getTitolo());

        task = taskRepository.save(task);
        taskDTO.setId(task.getId());

        return ResponseEntity.ok("Task creato con successo");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TaskDTO getById(@PathVariable String id) throws TaskNotFoundException {

        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty()) {
            throw new TaskNotFoundException();
        }

        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setId(task.get().getId());
        taskDTO.setDescrizione(task.get().getDescrizione());
        taskDTO.setAssignedUserId(task.get().getAssignedUserId());
        taskDTO.setLinkGitHub(task.get().getLinkGitHub());
        taskDTO.setDataCreazione(task.get().getDataCreazione());
        taskDTO.setStatus(task.get().getStatus());
        taskDTO.setTitolo(task.get().getTitolo());

        return taskDTO;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ListTaskDTO getAll() {

        ArrayList<TaskDTO> list = new ArrayList<>();
        ListTaskDTO taskList = new ListTaskDTO();

        List<Task> tasks = taskRepository.findAll();

        List<Task> filteredTasks = tasks.stream().filter
                (task -> task.getStatus().equals
                        (TaskStatus.DAFARE)).toList();

        for (Task task : filteredTasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setDescrizione(task.getDescrizione());
            taskDTO.setAssignedUserId(task.getAssignedUserId());
            taskDTO.setLinkGitHub(task.getLinkGitHub());
            taskDTO.setDataCreazione(task.getDataCreazione());
            taskDTO.setStatus(task.getStatus());
            taskDTO.setTitolo(task.getTitolo());
            list.add(taskDTO);
        }
        taskList.setList(list);

        return taskList;
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTask(@RequestBody TaskDTO taskDTO, @PathVariable String id, HttpServletRequest request) throws TaskNotFoundException {

        TaskDTO task = getById(id);

        if (task.getTitolo() != null) {
            task.setTitolo(taskDTO.getTitolo());
        }
        if (task.getDescrizione() != null) {
            task.setDescrizione(taskDTO.getDescrizione());
        }
        if (task.getAssignedUserId() != null) {
            task.setAssignedUserId(taskDTO.getAssignedUserId());
        }
        if (task.getLinkGitHub() != null) {
            task.setLinkGitHub(taskDTO.getLinkGitHub());
        }
        if (task.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }

        return ResponseEntity.ok("Task updated successfully");
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public String deleteTask(@PathVariable String id) throws TaskNotFoundException {

        if (getById(id) == null) {
            throw new TaskNotFoundException();
        }
        else
            taskRepository.deleteById(id);

        return "Task deleted successfully";
    }

    @RequestMapping(value = "assign/{id}", method = RequestMethod.PUT)
    public String assignTask(@PathVariable String id, String userId) throws TaskNotFoundException {

        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        task.setAssignedUserId(userId);
        task.setStatus(TaskStatus.COMPLETATO);

        task = taskRepository.save(task);

        return "Task assigned successfully";
    }

    @RequestMapping(value = "complete/{id}", method = RequestMethod.PUT)
    public String completeTask(@PathVariable String id) throws TaskNotFoundException {

        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        task.setStatus(TaskStatus.COMPLETATO);

        task = taskRepository.save(task);

        return "Task completed successfully";
    }

    @RequestMapping(value = "assignedTasks/{userId}", method = RequestMethod.GET)
    public ListTaskDTO getAssignedTasks(@PathVariable String userId) {

        ArrayList<TaskDTO> list = new ArrayList<>();
        ListTaskDTO taskList = new ListTaskDTO();

        List<Task> tasks = taskRepository.findByAssignedUserId(userId);

        List<Task> filteredTasks = tasks.stream().filter
                (task -> task.getStatus().equals
                        (TaskStatus.DAFARE)).toList();

        for (Task task : filteredTasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setDescrizione(task.getDescrizione());
            taskDTO.setAssignedUserId(task.getAssignedUserId());
            taskDTO.setLinkGitHub(task.getLinkGitHub());
            taskDTO.setDataCreazione(task.getDataCreazione());
            taskDTO.setStatus(task.getStatus());
            taskDTO.setTitolo(task.getTitolo());
            list.add(taskDTO);
        }
        taskList.setList(list);

        return taskList;
    }
}

