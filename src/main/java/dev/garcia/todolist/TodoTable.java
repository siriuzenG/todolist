package dev.garcia.todolist;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class TodoTable {
    private final TodoDB database;
    private final TableView<Todo> tableView;
    public TodoTable(TodoDB database) {
        this.database = database;
        this.tableView = new TableView<>();

        createTable();
    }
    private void createTable() {
        // ID Column
        TableColumn<Todo, Integer> doneColumn = new TableColumn<>("Done?");
        doneColumn.setCellValueFactory(new PropertyValueFactory<>("done"));
        doneColumn.setPrefWidth(75);
        doneColumn.setResizable(false);

        // Name Column
        TableColumn<Todo, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setResizable(false);

        // add columns to table
        tableView.getColumns().addAll(doneColumn, nameColumn);

        tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
            double tableWidth = newValue.doubleValue();
            nameColumn.setPrefWidth(tableWidth - doneColumn.getWidth() - 5);
        });

        refreshList();
    }
    public Todo getTodo() {
        if (!tableView.getSelectionModel().isEmpty()) {
            return tableView.getSelectionModel().getSelectedItem();
        }
        return null;
    }
    public TableView<Todo> getTableView() {
        return this.tableView;
    }
    public void refreshList() {
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void refresh() throws SQLException {
        tableView.getItems().clear();
        tableView.getItems().addAll(database.list());
        if(tableView.getItems().isEmpty()) {
            database.add(new Todo("Welcome", "First task sample for empty list. Try Adding or Editing new To Do"));
            refreshList();
        } else {
            tableView.getSelectionModel().selectFirst();
        }
    }
}

