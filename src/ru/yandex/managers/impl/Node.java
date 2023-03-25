package ru.yandex.managers.impl;

import ru.yandex.tasks.Task;

import java.util.Objects;

public class Node <T extends Task> {

    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return data.equals(node.data) && next.equals(node.next) && prev.equals(node.prev);
    }

    @Override
    public int hashCode() {
        int totalHashCode = 7;
        totalHashCode = totalHashCode * 7 + Objects.hashCode(data);
        totalHashCode = totalHashCode * 7 + Objects.hashCode(next);
        totalHashCode = totalHashCode * 7 + Objects.hashCode(prev);
        return totalHashCode;
    }
}