package ru.yandex.managers.impl;

import ru.yandex.enums.Status;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.TasksManager;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTasksManager implements TasksManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private int idNewTask = 1;

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
        if(task.getId() == 0) {
            while (checkContainsIdInAllObject(idNewTask)) {
                idNewTask++;
            }
            task.setId(idNewTask);
        }
        tasks.put(task.getId(), task);
        idNewTask++;
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if(subTask.getId() == 0) {
            while (checkContainsIdInAllObject(idNewTask)) {
                idNewTask++;
            }
            subTask.setId(idNewTask);
        }
        subTasks.put(subTask.getId(), subTask);
        checkStatusEpicById(subTask.getIdEpic());
        idNewTask++;
    }

    @Override
    public void createEpic(Epic epic) {
        if(epic.getId() == 0) {
            while (checkContainsIdInAllObject(idNewTask)) {
                idNewTask++;
            }
            epic.setId(idNewTask);
        }
        epics.put(epic.getId(), epic);
        idNewTask++;
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        deleteSubTaskByIdEpic(id);
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTaskByIdEpic(int id) {
        List<Integer> idRemoveSubTask = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            int idEpicSubTask = subTask.getIdEpic();
            if (idEpicSubTask == id) {
                idRemoveSubTask.add(subTask.getId());
            }
        }

        for (Integer idSubtaskFromDelete : idRemoveSubTask) {
            historyManager.remove(idSubtaskFromDelete);
            subTasks.remove(idSubtaskFromDelete);
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

    public HistoryManager getHistory() {
        return historyManager;
    }

    @Override
    public void setHistory(HistoryManager historyManager) {
        this.historyManager = historyManager;
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

    private boolean checkContainsIdInAllObject(Integer id) {
        if(tasks.containsKey(id)) {
            return true;
        }
        if(subTasks.containsKey(id)) {
            return true;
        }
        if(epics.containsKey(id)) {
            return true;
        }
        return false;
    }
}
