package ru.yandex.managers.impl;

import ru.yandex.managers.HistoryManager;
import ru.yandex.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final Map<Integer, Node<Task>> historyIdAndNodeTaskMap = new LinkedHashMap<>();
    private static final CustomLinkedList<Task> historyList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int id = task.getId();
        remove(id);
        Node<Task> createdNodeTask = historyList.linkLast(task);
        if(createdNodeTask != null) {
            historyIdAndNodeTaskMap.put(id, createdNodeTask);
        }
    }

    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int id) {
        historyList.removeNode(historyIdAndNodeTaskMap.get(id));
        historyIdAndNodeTaskMap.remove(id);
    }

    public static class CustomLinkedList<T extends Task> {
        protected int size = 0;
        private Node<T> head;
        private Node<T> tail;

        public Node<T> linkLast(T data) {
            if (data == null) {
                return null;
            }
            final Node<T> oldTail = tail;
            final Node<T> createdNode = new Node<>(tail, data, null);
            tail = createdNode;

            if (oldTail == null)
                head = createdNode;
            else
                oldTail.next = createdNode;
            size++;
            return createdNode;
        }

        private List<T> getTasks() {
            ArrayList<T> listTask = new ArrayList<>();
            Node<T> startHead = head;
            while (startHead != null) {
                listTask.add(startHead.data);
                startHead = startHead.next;
            }
            return listTask;
        }

        private void removeNode(Node<T> nodeTask) {
            if (nodeTask == null || head == null || tail == null) {
                return;
            }
            Node<T> prevRemoveTask = nodeTask.prev;
            Node<T> nextRemoveTask = nodeTask.next;

            if(head.equals(nodeTask) && tail.equals(nodeTask)) {
                head = null;
                tail = null;
            } else if(head.equals(nodeTask)) {
                head = nextRemoveTask;
                head.prev = null;
            } else if(tail.equals(nodeTask)) {
                tail = prevRemoveTask;
                tail.next = null;
            } else {
                prevRemoveTask.next = nextRemoveTask;
                nextRemoveTask.prev = prevRemoveTask;
            }
            size--;
        }
    }
}