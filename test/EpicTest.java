import org.junit.jupiter.api.Test;
import ru.yandex.enums.Status;
import ru.yandex.managers.TasksManager;
import ru.yandex.managers.impl.InMemoryTasksManager;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void emptySubTasks() {
        TasksManager tasksManager = new InMemoryTasksManager();
        assertEquals(0, tasksManager.getAllSubTask().size());
    }

    @Test
    void newAllSubTasks() {
        TasksManager tasksManager = new InMemoryTasksManager();
        Task epic = new Epic("Погулять", "Прыг-прыг");
        tasksManager.createEpic((Epic) epic);
        Task subTask1 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        tasksManager.createSubTask((SubTask) subTask1);
        List<SubTask> allSubTask = tasksManager.getAllSubTask();
        for (SubTask subTask : allSubTask) {
            assertEquals(subTask.getStatus(), Status.NEW);
        }
    }

    @Test
    void doneAllSubTasks() {
        TasksManager tasksManager = new InMemoryTasksManager();
        Task epic = new Epic("Погулять", "Прыг-прыг");
        tasksManager.createEpic((Epic) epic);
        Task subTask1 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        subTask1.setStatus(Status.DONE);
        tasksManager.createSubTask((SubTask) subTask1);
        List<SubTask> allSubTask = tasksManager.getAllSubTask();
        for (SubTask subTask : allSubTask) {
            assertEquals(subTask.getStatus(), Status.DONE);
        }
    }

    @Test
    void doneAndNewAllSubTasks() {
        TasksManager tasksManager = new InMemoryTasksManager();
        Task epic = new Epic("Погулять", "Прыг-прыг");
        tasksManager.createEpic((Epic) epic);
        Task subTask1 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        subTask1.setStatus(Status.DONE);
        Task subTask2 = new SubTask("Выйти из квартиры", "Топ-топ", epic.getId());
        tasksManager.createSubTask((SubTask) subTask1);
        tasksManager.createSubTask((SubTask) subTask2);
        List<SubTask> allSubTask = tasksManager.getAllSubTask();
        for (SubTask subTask : allSubTask) {
            assertTrue(subTask.getStatus() == Status.DONE || subTask.getStatus() == Status.NEW);
        }
    }

    @Test
    void inProgressAllSubTasks() {
        TasksManager tasksManager = new InMemoryTasksManager();
        Task epic = new Epic("Погулять", "Прыг-прыг");
        tasksManager.createEpic((Epic) epic);
        Task subTask1 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic.getId());
        subTask1.setStatus(Status.IN_PROGRESS);
        tasksManager.createSubTask((SubTask) subTask1);
        List<SubTask> allSubTask = tasksManager.getAllSubTask();
        for (SubTask subTask : allSubTask) {
            assertSame(subTask.getStatus(), Status.IN_PROGRESS);
        }
    }
}