package it.unisalento.recproject.taskservice.dto;

import it.unisalento.recproject.taskservice.domain.Task;

import java.util.ArrayList;

public class ListTaskDTO {
    ArrayList<TaskDTO> list;

    public ArrayList<TaskDTO> getList() {
        return list;
    }

    public void setList(ArrayList<TaskDTO> list) {
        this.list = list;
    }
}
