package ru.yandex.managers.impl;

import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
