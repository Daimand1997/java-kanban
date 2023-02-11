package ru.yandex.managers;

import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    HashMap<Integer, SubTask> subTasks = new HashMap<Integer, SubTask>();
    HashMap<Integer, Epic> epics = new HashMap<Integer, Epic>();
    int idNewTask = 1;

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            checkStatusEpicById(subTask.getIdEpic());
        }
    }

    public void createTask(Task task) {
        task.setId(idNewTask);
        tasks.put(idNewTask, task);
        idNewTask++;
    }

    public void createSubTask(SubTask subTask) {
        subTask.setId(idNewTask);
        subTasks.put(idNewTask, subTask);
        checkStatusEpicById(subTask.getIdEpic());
        idNewTask++;
    }

    public void createEpic(Epic epic) {
        epic.setId(idNewTask);
        epics.put(idNewTask, epic);
        idNewTask++;
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        deleteSubTaskByIdEpic(id);
        epics.remove(id);
    }

    public void deleteSubTaskByIdEpic(int id) {
        for (int i = subTasks.size() - 1; i > 0; i--) {
            if (subTasks.get(i).getIdEpic() == id) {
                subTasks.remove(i);
            }
        }
    }

    public void deleteSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            int idEpicBySubTask = subTasks.get(id).getIdEpic();
            subTasks.remove(id);
            checkStatusEpicById(idEpicBySubTask);
        }

    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void deleteAllTask() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public void checkStatusEpicById(int id) {
        String status = "NEW";

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getIdEpic() == id) {
                if (subTask.getStatus().equals("IN_PROGRESS")) {
                    status = subTask.getStatus();
                    break;
                } else if (subTask.getStatus().equals("DONE")) {
                    status = subTask.getStatus();
                }
            }
        }
        if (!epics.get(id).getStatus().equals(status)) {
            epics.get(id).setStatus(status);
        }
    }
}
