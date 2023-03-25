import ru.yandex.enums.Status;
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
        Task task2 = new Task("Поспать", "Храп-храп");
        Task epic = new Epic("Погулять", "Прыг-прыг");
        Task epic2 = new Epic("Убрать комнату", "Чик-пых");

        TaskManager taskManager = Managers.getDefault();
        taskManager.createTask(task);
        taskManager.createEpic((Epic) epic);
        Task subTask1 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        taskManager.createSubTask((SubTask) subTask1);
        Task subTask2 = new SubTask("Выйти из квартиры", "Топ-топ", epic.getId());
        taskManager.createSubTask((SubTask) subTask2);
        Task subTask3 = new SubTask("Закрыть дверь", "Быщ-быщ", epic.getId());
        taskManager.createSubTask((SubTask) subTask3);
        taskManager.createTask(task2);
        taskManager.createEpic((Epic) epic2);

        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после создания: ");
        System.out.println("////////////////////////////////////////");
        for (Task historyTask : taskManager.getHistory()) {
            System.out.println(historyTask);
        }

        taskManager.deleteTaskById(task.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления таски: ");
        System.out.println("////////////////////////////////////////");
        for (Task historyTask : taskManager.getHistory()) {
            System.out.println(historyTask);
        }

        taskManager.deleteEpicById(epic.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления эпика с тремя подзадачами: ");
        System.out.println("////////////////////////////////////////");
        for (Task historyTask : taskManager.getHistory()) {
            System.out.println(historyTask);
        }

        taskManager.deleteEpicById(epic2.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления эпика без подзадач: ");
        System.out.println("////////////////////////////////////////");
        for (Task historyTask : taskManager.getHistory()) {
            System.out.println(historyTask);
        }

        Task task4 = new Task("Крякать", "Кряк-кряк");
        Task epic3 = new Epic("Улететь", "Хлоп-хлоп");
        taskManager.createTask(task4);
        taskManager.createEpic((Epic) epic3);
        Task subTask4 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic3.getId());
        subTask4.setStatus(Status.DONE);
        taskManager.createSubTask((SubTask) subTask4);
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после создания новой таски и эпика: ");
        System.out.println("////////////////////////////////////////");
        for (Task historyTask : taskManager.getHistory()) {
            System.out.println(historyTask);
        }
    }
}
