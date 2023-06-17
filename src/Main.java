import ru.yandex.enums.Status;
import ru.yandex.managers.TasksManager;
import ru.yandex.managers.impl.Managers;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

import javax.xml.datatype.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws Exception {
        Task task = new Task("Покушать", "Ням-ням");
        task.setDuration(1000);
        task.setStartTime(LocalDateTime.now());
        Task task2 = new Task("Поспать", "Храп-храп");
        task2.setDuration(500);
        task2.setStartTime(LocalDateTime.now().plusSeconds(2000));
        Task epic = new Epic("Погулять", "Прыг-прыг");
        Task epic2 = new Epic("Убрать комнату", "Чик-пых");

        TasksManager tasksManager = Managers.getDefault();
        tasksManager.createTask(task);
        tasksManager.createEpic((Epic) epic);
        Task subTask1 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        tasksManager.createSubTask((SubTask) subTask1);
        Task subTask2 = new SubTask("Выйти из квартиры", "Топ-топ", epic.getId());
        tasksManager.createSubTask((SubTask) subTask2);
        Task subTask3 = new SubTask("Закрыть дверь", "Быщ-быщ", epic.getId());
        tasksManager.createSubTask((SubTask) subTask3);
        tasksManager.createTask(task2);
        tasksManager.createEpic((Epic) epic2);

        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после создания: ");
        System.out.println("////////////////////////////////////////");
        for(Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for(Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for(Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        tasksManager.deleteTaskById(task.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления таски: ");
        System.out.println("////////////////////////////////////////");
        for(Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for(Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for(Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        tasksManager.deleteEpicById(epic.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления эпика с тремя подзадачами: ");
        System.out.println("////////////////////////////////////////");
        for(Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for(Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for(Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        tasksManager.deleteEpicById(epic2.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления эпика без подзадач: ");
        System.out.println("////////////////////////////////////////");
        for(Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for(Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for(Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        Task task4 = new Task("Крякать", "Кряк-кряк");
        task4.setDuration(400);
        task4.setStartTime(LocalDateTime.now().plusSeconds(11111));
        Task epic3 = new Epic("Улететь", "Хлоп-хлоп");
        tasksManager.createTask(task4);
        tasksManager.createEpic((Epic) epic3);
        Task subTask4 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic3.getId());
        subTask4.setStatus(Status.DONE);
        tasksManager.createSubTask((SubTask) subTask4);
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после создания новой таски и эпика: ");
        System.out.println("////////////////////////////////////////");
        for(Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for(Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for(Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        System.out.println("////////////////////////////////////////");
        System.out.println("Отсортированные задачи по дате: ");
        System.out.println("////////////////////////////////////////");
        System.out.println(tasksManager.getPrioritizedTasks());
    }
}
