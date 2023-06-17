package ru.yandex.managers.impl;

import ru.yandex.enums.Status;
import ru.yandex.enums.TypeTask;
import ru.yandex.exceptions.ManagerLoadException;
import ru.yandex.exceptions.ManagerSaveException;
import ru.yandex.managers.HistoryManager;
import ru.yandex.tasks.Epic;
import ru.yandex.tasks.SubTask;
import ru.yandex.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTasksManager {
    private final File file;

    private final String headerFile = "id,type,name,status,description,epic,startTime,duration,endTime";

    public FileBackedTasksManager(String path) {
        this.file = new File(path);
    }

    public static void main(String[] args) throws Exception {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager("resources/saveTasks.csv");
        tasksManager = loadFromFile(tasksManager.file.getPath());
        Task task = new Task("Покушать", "Ням-ням");
        task.setDuration(1000);
        task.setStartTime(LocalDateTime.now());
        Task task2 = new Task("Поспать", "Храп-храп");
        task2.setDuration(1000);
        task2.setStartTime(LocalDateTime.now().plusSeconds(2000));
        Task epic = new Epic("Погулять", "Прыг-прыг");
        Task epic2 = new Epic("Убрать комнату", "Чик-пых");

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
        for (Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for (Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for (Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        tasksManager.deleteTaskById(task.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления таски: ");
        System.out.println("////////////////////////////////////////");
        for (Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for (Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for (Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        tasksManager.deleteEpicById(epic.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления эпика с тремя подзадачами: ");
        System.out.println("////////////////////////////////////////");
        for (Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for (Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for (Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        tasksManager.deleteEpicById(epic2.getId());
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после удаления эпика без подзадач: ");
        System.out.println("////////////////////////////////////////");
        for (Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for (Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for (Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        Task task4 = new Task("Крякать", "Кряк-кряк");
        Task epic3 = new Epic("Улететь", "Хлоп-хлоп");
        tasksManager.createTask(task4);
        tasksManager.createEpic((Epic) epic3);
        Task subTask4 = new SubTask("Открыть дверь", "Хлоп-хлоп", epic3.getId());
        subTask4.setStatus(Status.DONE);
        tasksManager.createSubTask((SubTask) subTask4);
        System.out.println("////////////////////////////////////////");
        System.out.println("Вывод после создания новой таски и эпика: ");
        System.out.println("////////////////////////////////////////");
        for (Task taskOwn : tasksManager.getAllTask()) {
            System.out.println(taskOwn);
        }
        for (Task subTaskOwn : tasksManager.getAllSubTask()) {
            System.out.println(subTaskOwn);
        }
        for (Task epicOwn : tasksManager.getAllEpic()) {
            System.out.println(epicOwn);
        }

        tasksManager.getTaskById(task4.getId());
        tasksManager.deleteTaskById(task4.getId());
        tasksManager.getEpicById(epic3.getId());
        tasksManager.getSubTaskById(subTask4.getId());
    }

    private static FileBackedTasksManager loadFromFile(String path) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
        Map<Integer, TypeTask> idWithType = new LinkedHashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
            while (bufferedReader.ready()) {
                String lineInfoTask = bufferedReader.readLine();
                if (lineInfoTask.isBlank()) {
                    break;
                }

                Task task = taskFromString(lineInfoTask);
                if (task instanceof Epic) {
                    fileBackedTasksManager.createEpicWithoutSave((Epic) task);
                    idWithType.put(task.getId(), TypeTask.EPIC);
                } else if (task instanceof SubTask) {
                    fileBackedTasksManager.createSubTaskWithoutSave((SubTask) task);
                    idWithType.put(task.getId(), TypeTask.SUBTASK);
                } else if (task != null) {
                    fileBackedTasksManager.createTaskWithoutSave(task);
                    idWithType.put(task.getId(), TypeTask.TASK);
                }
            }

            String history = bufferedReader.readLine();
            if (history != null && history.length() > 0) {
                List<Integer> listIdHistory = historyFromString(history);
                for (Integer id : listIdHistory) {
                    switch (idWithType.get(id)) {
                        case EPIC: {
                            historyManager.add(fileBackedTasksManager.getEpicByIdWithoutSave(id));
                        }
                        case SUBTASK: {
                            historyManager.add(fileBackedTasksManager.getSubTaskByIdWithoutSave(id));
                        }
                        case TASK: {
                            historyManager.add(fileBackedTasksManager.getTaskByIdWithoutSave(id));
                        }
                    }
                }
            }

            fileBackedTasksManager.setHistory(historyManager);

        } catch (Exception e) {
            throw new ManagerLoadException("Ошибка загрузки данных из файла. " + e.getMessage());
        }

        return fileBackedTasksManager;
    }

    private static Task taskFromString(String value) {
        String[] taskList = value.split(",");
        Task resultTask = null;

        switch (TypeTask.valueOf(taskList[1])) {
            case TASK: {
                resultTask = new Task(taskList[2], taskList[4]);
                resultTask.setId(Integer.parseInt(taskList[0]));
                resultTask.setStatus(Status.valueOf(taskList[3]));
                break;
            }
            case EPIC: {
                resultTask = new Epic(taskList[2], taskList[4]);
                resultTask.setId(Integer.parseInt(taskList[0]));
                resultTask.setStatus(Status.valueOf(taskList[3]));
                break;
            }
            case SUBTASK: {
                resultTask = new SubTask(taskList[2], taskList[4], Integer.parseInt(taskList[5]));
                resultTask.setId(Integer.parseInt(taskList[0]));
                resultTask.setStatus(Status.valueOf(taskList[3]));
                break;
            }
        }
        return resultTask;
    }

    private static List<Integer> historyFromString(String value) {
        return Arrays.stream(value.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder idHistory = new StringBuilder();
        for (Task task : history) {
            idHistory.append(task.getId()).append(",");
        }

        if (idHistory.length() > 0) {
            idHistory.delete(idHistory.length() - 1, idHistory.length());
        }

        return idHistory.toString();
    }

    @Override
    public HistoryManager getHistory() {
        return super.getHistory();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) throws Exception {
        super.updateTask(task);
        save();
    }

    @Override
    public void createTask(Task task) throws Exception {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskByIdEpic(int id) {
        super.deleteSubTaskByIdEpic(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    private String toString(Task task) {
        String epicId = "";

        if (task != null && task.getClass().getSimpleName().toUpperCase().equals(TypeTask.SUBTASK.name())) {
            epicId = String.valueOf(((SubTask) task).getIdEpic());
        }
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s", task.getId(),
                TypeTask.valueOf(task.getClass().getSimpleName().toUpperCase()),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                epicId,
                task.getStartTime(),
                task.getDuration(),
                task.getEndTime());
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(headerFile);
            writer.newLine();
            List<Task> tasks = getAllTask();
            for (Task task : tasks) {
                writer.write(toString(task));
                writer.newLine();
            }

            List<Epic> epics = getAllEpic();
            for (Epic epic : epics) {
                writer.write(toString(epic));
                writer.newLine();
            }

            List<SubTask> subTasks = getAllSubTask();
            for (SubTask subTask : subTasks) {
                writer.write(toString(subTask));
                writer.newLine();
            }

            writer.newLine();
            writer.write(historyToString(getHistory()));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи в файл: " + e.getMessage());
        }
    }

    private void createTaskWithoutSave(Task task) throws Exception {
        super.createTask(task);
    }

    private void createSubTaskWithoutSave(SubTask subTask) {
        super.createSubTask(subTask);
    }

    private void createEpicWithoutSave(Epic epic) {
        super.createEpic(epic);
    }

    private Task getTaskByIdWithoutSave(int id) {
        Task task = super.getTaskById(id);
        return task;
    }

    private SubTask getSubTaskByIdWithoutSave(int id) {
        SubTask subTask = super.getSubTaskById(id);
        return subTask;
    }

    private Epic getEpicByIdWithoutSave(int id) {
        Epic epic = super.getEpicById(id);
        return epic;
    }
}
