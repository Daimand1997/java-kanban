import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.TaskManager;
import ru.yandex.managers.impl.InMemoryHistoryManager;
import ru.yandex.managers.impl.InMemoryTaskManager;
import ru.yandex.managers.impl.Managers;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("Покушать", "Ням-ням");
        Task epic = new Epic("Погулять", "Прыг-прыг");

        TaskManager taskManager = Managers.getDefault();
        taskManager.createTask(task);
        taskManager.createEpic((Epic) epic);
        Task subTask = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        taskManager.createSubTask((SubTask) subTask);

        taskManager.getEpicById(epic.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getSubTaskById(subTask.getId());

        for (Task historyTask : taskManager.getHistory()) {
            System.out.println(historyTask);
        }
    }
}
