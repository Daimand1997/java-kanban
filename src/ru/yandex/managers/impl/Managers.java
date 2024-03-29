package ru.yandex.managers.impl;

import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.TasksManager;

public class Managers {

    public static TasksManager getDefault() {
        return new FileBackedTasksManager("resources/saveTasks.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
