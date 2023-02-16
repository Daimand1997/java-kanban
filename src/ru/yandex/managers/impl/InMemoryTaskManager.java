package ru.yandex.managers.impl;

import ru.yandex.enums.Status;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.TaskManager;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private HistoryManager historyManager = Managers.getDefaultHistory();
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private int idNewTask = 1;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            checkStatusEpicById(subTask.getIdEpic());
        }
    }

    @Override
    public void createTask(Task task) {
        task.setId(idNewTask);
        tasks.put(idNewTask, task);
        idNewTask++;
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(idNewTask);
        subTasks.put(idNewTask, subTask);
        checkStatusEpicById(subTask.getIdEpic());
        idNewTask++;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(idNewTask);
        epics.put(idNewTask, epic);
        idNewTask++;
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        deleteSubTaskByIdEpic(id);
        epics.remove(id);
    }

    @Override
    public void deleteSubTaskByIdEpic(int id) {
        for (int i = subTasks.size() - 1; i > 0; i--) {
            if (subTasks.get(i).getIdEpic() == id) {
                subTasks.remove(i);
            }
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            int idEpicBySubTask = subTasks.get(id).getIdEpic();
            subTasks.remove(id);
            checkStatusEpicById(idEpicBySubTask);
        }

    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    private void checkStatusEpicById(int id) {
        Status status = Status.NEW;

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getIdEpic() == id) {
                if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                    status = subTask.getStatus();
                    break;
                } else if (subTask.getStatus().equals(Status.DONE)) {
                    status = subTask.getStatus();
                }
            }
        }
        if (!epics.get(id).getStatus().equals(status)) {
            epics.get(id).setStatus(status);
        }
    }
}
