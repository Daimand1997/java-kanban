package ru.yandex.tasks;

public class Epic extends Task {

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "epic={"
                + "name='" + getName() + "', "
                + "status='" + getStatus() + "', "
                + "description='" + getDescription() + "', "
                + "id='" + getId() + "'"
                + "}";
    }
}