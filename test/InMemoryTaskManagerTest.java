import org.junit.jupiter.api.BeforeEach;
import ru.yandex.managers.impl.InMemoryHistoryManager;
import ru.yandex.managers.impl.InMemoryTasksManager;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

public class InMemoryTaskManagerTest extends TaskManagerTest{

    @BeforeEach
    void init() throws Exception {
        tasksManager = new InMemoryTasksManager();
        historyManager = new InMemoryHistoryManager();
        task = new Task("Покушать", "Ням-ням");
        tasksManager.createTask(task);

        epic = new Epic("Погулять", "Прыг-прыг");
        tasksManager.createEpic((Epic) epic);

        subTask = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        tasksManager.createSubTask((SubTask) subTask);
    }

}
