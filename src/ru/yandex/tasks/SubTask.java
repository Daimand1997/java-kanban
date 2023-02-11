package ru.yandex.tasks;

public class SubTask extends Task {

    private int idEpic;

    public SubTask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "epic={"
                + "name='" + getName() + "', "
                + "status='" + getStatus() + "', "
                + "description='" + getDescription() + "', "
                + "id='" + getId() + "', "
                + "idEpic='" + getIdEpic() + "'"
                + "}";
    }
}