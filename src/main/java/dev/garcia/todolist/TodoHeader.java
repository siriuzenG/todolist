package dev.garcia.todolist;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.sql.SQLException;

public class TodoHeader {
    private final TodoDB database;
    private final TodoTable table;
    private final TodoPopup pop;
    private final BorderPane layout;
    public TodoHeader(TodoDB database, TodoTable table) {
        this.database = database;
        this.table = table;
        this.layout = new BorderPane();
        this.pop = new TodoPopup(table, database);

        createHeader();
    }
    public BorderPane getLayout() {
        return layout;
    }
    private void createHeader() {
        Label title = new Label("To Do List");
        title.setFont(new Font("Arial", 20));
        title.setPadding(new Insets(3));

        HBox menu = new HBox();
        Button btnView = new Button("View");
        Button btnAdd = new Button("Add");
        Button btnEdit = new Button("Edit");
        Button btnRemove = new Button("Remove");

        menu.getChildren().addAll(btnView, btnAdd, btnEdit, btnRemove);
        menu.setSpacing(3);
        menu.setPadding(new Insets(3));

        layout.setLeft(title);
        layout.setRight(menu);
        layout.setPadding(new Insets(3));

        btnView.setOnMouseClicked(e -> pop.view());
        btnAdd.setOnMouseClicked(e -> pop.add());
        btnEdit.setOnMouseClicked(e -> pop.edit());
        btnRemove.setOnMouseClicked(e -> remove());
    }
    private void remove() {
        Todo todo = table.getTodo();
        try {
            database.remove(todo.getId());
            table.refreshList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
