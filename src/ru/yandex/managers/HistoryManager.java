package ru.yandex.managers;

import ru.yandex.tasks.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
