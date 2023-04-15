package ru.yandex.managers;

import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

import java.util.List;

public interface TasksManager {

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void createTask(Task task);

    void createSubTask(SubTask subTask);

    void createEpic(Epic epic);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskByIdEpic(int id);

    void deleteSubTaskById(int id);

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    void deleteAllTask();

    List<Task> getAllTask();

    List<SubTask> getAllSubTask();

    List<Epic> getAllEpic();

    HistoryManager getHistory();

    void setHistory(HistoryManager historyManager);
}
