package dev.garcia.todolist;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        TodoDB database = new TodoDB();
        TodoTable table = new TodoTable(database);
        TodoHeader header = new TodoHeader(database, table);

        BorderPane layout = new BorderPane();
        layout.setTop(header.getLayout());
        layout.setCenter(table.getTableView());

        stage.setMinWidth(400);
        stage.setMinHeight(600);
        stage.setScene(new Scene(layout));
        stage.setTitle("To Do List");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}