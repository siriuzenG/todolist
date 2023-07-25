package dev.garcia.todolist;

public class Todo {
    private int id;
    private String name;
    private String description;
    private Boolean done;
    public Todo(int id, String name, String description, Boolean done) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
    }
    public Todo(String name, String description) {
        this(-1, name, description, false);
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public Boolean isDone() {
        return this.done;
    }
}
