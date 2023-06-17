import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.enums.Status;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.TasksManager;
import ru.yandex.managers.impl.InMemoryTasksManager;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;


import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TasksManager> {

    protected T tasksManager;
    public HistoryManager historyManager;
    public Task task;
    public Task epic;
    public Task subTask;

    @Test
    void updateTask() throws Exception {
        task.setStatus(Status.DONE);
        tasksManager.updateTask(task);
        assertEquals(tasksManager.getTaskById(task.getId()).getStatus(), Status.DONE);
    }

    @Test
    void updateEpic() {
        epic.setName("Hello");
        tasksManager.updateEpic((Epic) epic);
        assertEquals("Hello", epic.getName());
    }

    @Test
    void updateSubTask() {
        subTask.setDescription("Hello world");
        tasksManager.updateSubTask((SubTask) subTask);
        assertEquals("Hello world", tasksManager.getSubTaskById(subTask.getId()).getDescription());
        assertEquals("Погулять", tasksManager.getEpicById(
                tasksManager.getSubTaskById(subTask.getId()).getIdEpic()).getName());
    }

    @Test
    void createTask() throws Exception {
        assertEquals(1, task.getId());

        Task taskSecond = new Task("hhhh", "gggg");
        tasksManager.createTask(taskSecond);
        assertEquals(4, taskSecond.getId());
    }

    @Test
    void createSubTask() {
        assertEquals(3, subTask.getId());

        Task subtaskSecond = new SubTask("hhhh", "gggg", epic.getId());
        tasksManager.createSubTask((SubTask) subtaskSecond);
        assertEquals(4, subtaskSecond.getId());
        assertEquals("hhhh", tasksManager.getSubTaskById(4).getName());
    }

    @Test
    void createEpic() {
        assertEquals(2, epic.getId());

        Task epicSecond = new Epic("aaaa", "bbbb");
        tasksManager.createEpic((Epic) epicSecond);
        assertEquals(4, epicSecond.getId());
        assertEquals("aaaa", tasksManager.getEpicById(4).getName());
    }

    @Test
    void deleteTaskById() {
        tasksManager.deleteTaskById(task.getId());
        assertNull(tasksManager.getTaskById(task.getId()));
    }

    @Test
    void deleteEpicById() {
        tasksManager.deleteEpicById(epic.getId());
        assertNull(tasksManager.getEpicById(epic.getId()));
    }

    @Test
    void deleteSubTaskByIdEpic() {
        tasksManager.deleteSubTaskByIdEpic(epic.getId());
        assertNull(tasksManager.getSubTaskById(subTask.getId()));
    }

    @Test
    void deleteSubTaskById() {
        tasksManager.deleteSubTaskById(subTask.getId());
        assertNull(tasksManager.getSubTaskById(subTask.getId()));
    }

    @Test
    void getHistory() {
        assertEquals(1, tasksManager.getHistory().getHistory().size());

        tasksManager.getTaskById(task.getId());
        tasksManager.getSubTaskById(subTask.getId());

        assertEquals(3, tasksManager.getHistory().getHistory().size());
    }

    @Test
    void getTaskById() {
        assertEquals(1, tasksManager.getTaskById(task.getId()).getId());
        assertNull(tasksManager.getTaskById(10));
    }

    @Test
    void getSubTaskById() {
        assertEquals("Открыть дверь", tasksManager.getSubTaskById(subTask.getId()).getName());
        assertNull(tasksManager.getSubTaskById(10));
    }

    @Test
    void getEpicById() {
        assertEquals(2, tasksManager.getEpicById(epic.getId()).getId());
        assertNull(tasksManager.getEpicById(10));
    }

    @Test
    void deleteAllTask() {
        assertEquals(1, tasksManager.getAllTask().size());

        tasksManager.deleteAllTask();
        assertEquals(0, tasksManager.getAllTask().size());
    }

    @Test
    void getAllTask() {
        assertEquals(1, tasksManager.getAllTask().size());

        tasksManager.deleteAllTask();
        assertEquals(0, tasksManager.getAllTask().size());
    }

    @Test
    void getAllSubTask() {
        assertEquals(1, tasksManager.getAllSubTask().size());

        tasksManager.deleteSubTaskById(subTask.getId());
        assertEquals(0, tasksManager.getAllSubTask().size());
    }

    @Test
    void getAllEpic() {
        assertEquals(1, tasksManager.getAllEpic().size());

        tasksManager.deleteEpicById(epic.getId());
        assertEquals(0, tasksManager.getAllEpic().size());
    }
}