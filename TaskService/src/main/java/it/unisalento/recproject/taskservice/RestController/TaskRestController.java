package it.unisalento.recproject.taskservice.RestController;

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
        String username = jwtUtilities.extractUsername(request.getHeader("Authorization").substring(7));

        Task task = new Task();
        task.setCreatoDa(username);
        task.setDescrizione(taskDTO.getDescrizione());
        task.setAssignedUserEmail(taskDTO.getAssignedUserEmail());
        task.setLinkGitHub(taskDTO.getLinkGitHub());
        task.setDataCreazione(LocalDateTime.now());
        task.setStatus(TaskStatus.DAFARE);
        task.setTitolo(taskDTO.getTitolo());

        task = taskRepository.save(task);
        taskDTO.setId(task.getId());

        return ResponseEntity.ok("Task creato con successo");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TaskDTO getById(@PathVariable String id, HttpServletRequest request) throws TaskNotFoundException {
        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty()) {
            throw new TaskNotFoundException();
        }

        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setId(task.get().getId());
        taskDTO.setDescrizione(task.get().getDescrizione());
        taskDTO.setCreatoDa(task.get().getCreatoDa());
        taskDTO.setAssignedUserEmail(task.get().getAssignedUserEmail());
        taskDTO.setLinkGitHub(task.get().getLinkGitHub());
        taskDTO.setDataCreazione(task.get().getDataCreazione());
        taskDTO.setStatus(task.get().getStatus());
        taskDTO.setTitolo(task.get().getTitolo());

        return taskDTO;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ListTaskDTO getAll(HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        ArrayList<TaskDTO> list = new ArrayList<>();
        ListTaskDTO taskList = new ListTaskDTO();

        List<Task> tasks = taskRepository.findAll();

        List<Task> filteredTasks = tasks.stream().filter
                (task -> task.getStatus().equals
                        (TaskStatus.DAFARE)).toList();

        for (Task task : filteredTasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setCreatoDa(task.getCreatoDa());
            taskDTO.setDescrizione(task.getDescrizione());
            taskDTO.setAssignedUserEmail(task.getAssignedUserEmail());
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

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        TaskDTO task = getById(id, request);

        if (task.getTitolo() != null) {
            task.setTitolo(taskDTO.getTitolo());
        }
        if (task.getDescrizione() != null) {
            task.setDescrizione(taskDTO.getDescrizione());
        }
        if (task.getAssignedUserEmail() != null) {
            task.setAssignedUserEmail(taskDTO.getAssignedUserEmail());
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
    public String deleteTask(@PathVariable String id, HttpServletRequest request) throws TaskNotFoundException {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        if (getById(id,request) == null) {
            throw new TaskNotFoundException();
        }
        else
            taskRepository.deleteById(id);

        return "Task deleted successfully";
    }

    @RequestMapping(value = "complete/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String completeTask(@PathVariable String id, HttpServletRequest request) throws TaskNotFoundException {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        task.setStatus(TaskStatus.COMPLETATO);

        taskRepository.save(task);

        return "Task completed successfully";
    }

    @RequestMapping(value = "assignedTasks/{email}", method = RequestMethod.GET)
    public ListTaskDTO getAssignedTasks(@PathVariable String email, HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        ArrayList<TaskDTO> list = new ArrayList<>();
        ListTaskDTO taskList = new ListTaskDTO();

        List<Task> tasks = taskRepository.findByAssignedUserEmail(email);

        List<Task> filteredTasks = tasks.stream().filter
                (task -> task.getStatus().equals
                        (TaskStatus.DAFARE)).toList();

        for (Task task : filteredTasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setDescrizione(task.getDescrizione());
            taskDTO.setCreatoDa(task.getCreatoDa());
            taskDTO.setAssignedUserEmail(task.getAssignedUserEmail());
            taskDTO.setLinkGitHub(task.getLinkGitHub());
            taskDTO.setDataCreazione(task.getDataCreazione());
            taskDTO.setStatus(task.getStatus());
            taskDTO.setTitolo(task.getTitolo());
            list.add(taskDTO);
        }
        taskList.setList(list);

        return taskList;
    }

    //Ritorna tutti i task che ha assegnato l'utente che fa questa chiamata API
    @RequestMapping(value = "assignedByMe", method = RequestMethod.GET)
    public ListTaskDTO getTaskCreateBy(HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        ArrayList<TaskDTO> list = new ArrayList<>();
        ListTaskDTO taskList = new ListTaskDTO();

        String email = jwtUtilities.extractUsername(request.getHeader("Authorization").substring(7));
        List<Task> tasks = taskRepository.findByCreatoDa(email);


        List<Task> filteredTasks = tasks.stream().filter
                (task -> task.getStatus().equals
                        (TaskStatus.DAFARE)).toList();

        for (Task task : filteredTasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setDescrizione(task.getDescrizione());
            taskDTO.setCreatoDa(task.getCreatoDa());
            taskDTO.setAssignedUserEmail(task.getAssignedUserEmail());
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

