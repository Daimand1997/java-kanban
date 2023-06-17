import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.managers.TasksManager;
import ru.yandex.managers.impl.FileBackedTasksManager;
import ru.yandex.managers.impl.InMemoryHistoryManager;
import ru.yandex.managers.impl.InMemoryTasksManager;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void init() throws Exception {
        tasksManager = new FileBackedTasksManager("resources/saveTasks.csv");

        historyManager = new InMemoryHistoryManager();

        task = new Task("Покушать", "Ням-ням");
        tasksManager.createTask(task);

        epic = new Epic("Погулять", "Прыг-прыг");
        tasksManager.createEpic((Epic) epic);

        subTask = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        tasksManager.createSubTask((SubTask) subTask);
    }

    @Test
    void setHistory() {
        TasksManager secondManager = new FileBackedTasksManager("resources/saveTasksTest.csv");

        secondManager.getTaskById(task.getId());
        tasksManager.setHistory(secondManager.getHistory());
        assertEquals(2, tasksManager.getHistory().getHistory().size());
    }

    @Test
    void emptyTaskList() {
        assertEquals(tasksManager.getAllTask().size(), 1);
        tasksManager.deleteAllTask();
        assertEquals(tasksManager.getAllTask().size(), 0);
    }

    @Test
    void epicWithoutSubtasks() {
        assertEquals(1, tasksManager.getAllSubTask().size());
        tasksManager.deleteSubTaskByIdEpic(epic.getId());
        assertEquals(0, tasksManager.getAllSubTask().size());
    }

    @Test
    void historyEmptyList() throws Exception {
        assertEquals(2, tasksManager.getHistory().getHistory().size());

        Task task2 = new Task("Покушать", "Ням-ням");
        tasksManager.createTask(task2);
        tasksManager.getTaskById(task2.getId());

        assertEquals(2, tasksManager.getHistory().getHistory().size());

        tasksManager.getTaskById(task2.getId());
        assertEquals(2, tasksManager.getHistory().getHistory().size());

        tasksManager.getTaskById(task.getId());
        assertEquals(2, tasksManager.getHistory().getHistory().size());
    }

}
