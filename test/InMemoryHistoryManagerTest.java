import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.impl.FileBackedTasksManager;
import ru.yandex.managers.impl.InMemoryHistoryManager;
import ru.yandex.managers.impl.InMemoryTasksManager;
import ru.yandex.tasks.Task;

class InMemoryHistoryManagerTest {

    public InMemoryTasksManager tasksManager;
    public HistoryManager historyManager;
    public Task task;
    public Task taskSecond;
    public Task taskThird;

    @BeforeEach
    void init() throws Exception {
        tasksManager = new FileBackedTasksManager("resources/saveTasks.csv");
        historyManager = new InMemoryHistoryManager();

        task = new Task("Покушать", "Ням-ням");
        taskSecond = new Task("Погулять", "Кусь-кусь");
        taskThird = new Task("Завтрак", "Мяу-мяу");
        tasksManager.createTask(task);
        tasksManager.createTask(taskSecond);
        tasksManager.createTask(taskThird);
    }

    @Test
    void add() {
        Assertions.assertEquals(1, historyManager.getHistory().size());

        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        Assertions.assertEquals(2, historyManager.getHistory().size());

        historyManager.add(taskSecond);
        historyManager.add(taskThird);
        Assertions.assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void getHistory() {
        Assertions.assertEquals(3, historyManager.getHistory().size());

        historyManager.remove(taskSecond.getId());
        historyManager.remove(taskThird.getId());
        Assertions.assertEquals(1, historyManager.getHistory().size());

        historyManager.add(taskSecond);
        historyManager.add(taskThird);
        Assertions.assertEquals(3, historyManager.getHistory().size());

        historyManager.remove(taskThird.getId());
        Assertions.assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void remove() {
        Assertions.assertEquals(0, historyManager.getHistory().size());

        historyManager.remove(taskSecond.getId());
        historyManager.remove(taskThird.getId());
        Assertions.assertEquals(0, historyManager.getHistory().size());

        historyManager.add(taskSecond);
        historyManager.add(taskThird);
        Assertions.assertEquals(2, historyManager.getHistory().size());

        historyManager.remove(taskThird.getId());
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }
}