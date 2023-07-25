package dev.garcia.todolist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDB {
    private final String database = "jdbc:sqlite:todo_list.db";
    public TodoDB() {}
    public List<Todo> list() throws SQLException {
        List<Todo> todoList = new ArrayList<>();
        try (
                Connection connection = dbConnect();
                ResultSet results = connection.prepareStatement("SELECT * FROM Todo").executeQuery()
        ) {
            while (results.next()) {
                todoList.add(new Todo(
                        results.getInt("id"),
                        results.getString("name"),
                        results.getString("description"),
                        results.getBoolean("done")
                ));
            }
        }
        return todoList;
    }
    public void add(Todo todo) throws SQLException{
        try (Connection connection = dbConnect()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Todo (name, description, done) VALUES (?, ?, ?)");
            stmt.setString(1, todo.getName());
            stmt.setString(2, todo.getDescription());
            stmt.setBoolean(3, todo.isDone());
            stmt.executeUpdate();
        }
    }
    public void remove(int id) throws SQLException{
        try (Connection connection = dbConnect()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Todo WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public void markAsDone(int id) throws SQLException{
        try (Connection connection = dbConnect()) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE Todo SET done = ? WHERE id = ?");
            stmt.setBoolean(1, true);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
    public void updateTask(int id, String name, String description, Boolean done) throws SQLException {
        try (Connection connection = dbConnect()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE Todo SET name = ?, description = ?, done = ? WHERE id = ?"
            );
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setBoolean(3, done);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }
    private Connection dbConnect() throws SQLException {
        Connection connection = DriverManager.getConnection(database);
        try {
            connection.prepareStatement(
                    "CREATE TABLE Todo (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT," +
                            "description TEXT," +
                            "done BOOLEAN" +
                            ")"
            ).executeUpdate();
        } catch (SQLException e) {
        }
        return connection;
    }
}
