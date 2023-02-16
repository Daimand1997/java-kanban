package ru.yandex.managers.impl;

import ru.yandex.managers.HistoryManager;
import ru.yandex.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static List<Task> historyList = new ArrayList<>();
    private final static int HISTORY_LIMIT = 10;

    @Override
    public void add(Task task) {
        historyList.add(0, task);
        deleteElementByHistory();
    }

    public List<Task> getHistory() {
        return historyList;
    }

    private void deleteElementByHistory() {
        if(historyList.size() >= HISTORY_LIMIT) {
            historyList.remove(historyList.size() - 1);
        }
    }
}
