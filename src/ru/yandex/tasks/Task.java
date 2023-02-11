package ru.yandex.tasks;

public class Task {

    private String name;
    private String description;
    private int id;
    private String status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals("NEW") || status.equals("IN_PROGRESS") || status.equals("DONE")) {
            this.status = status;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
