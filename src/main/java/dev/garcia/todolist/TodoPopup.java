package dev.garcia.todolist;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TodoPopup {
    private Stage pop;
    private final TodoTable table;
    private final TodoDB database;
    public TodoPopup(TodoTable table, TodoDB database) {
        this.table = table;
        this.database = database;
    }
    public void edit() {
        Todo todo = table.getTodo();

        GridPane detailsLayout = new GridPane();
        Label lblName = new Label("Name: ");
        detailsLayout.add(lblName, 0,0);

        TextField name = new TextField(todo.getName());
        detailsLayout.add(name, 1, 0);

        Label lblDesc = new Label("Description: ");
        detailsLayout.add(lblDesc, 0, 1);
        GridPane.setColumnSpan(lblDesc, 2);

        TextArea description = new TextArea(todo.getDescription());
        description.setWrapText(true);
        detailsLayout.add(description, 0, 2);
        GridPane.setColumnSpan(description, 2);

        CheckBox done = new CheckBox("Done?");
        done.setSelected(todo.isDone());
        detailsLayout.add(done, 0, 3);
        GridPane.setColumnSpan(done, 2);

        Button btnSave = new Button("Save");
        Button btnClose = new Button("Close");
        detailsLayout.add(btnSave, 0, 4);
        detailsLayout.add(btnClose, 1, 4);

        showPop(detailsLayout, "Edit To Do");

        btnSave.setOnMouseClicked(e -> {
            if(checkInput(name)) {
                try {
                    database.updateTask(
                            todo.getId(),
                            name.getText(),
                            description.getText(),
                            done.isSelected()
                    );
                    table.refreshList();
                    pop.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnClose.setOnMouseClicked(e -> pop.close());
    }
    public void view() {
        Todo todo = table.getTodo();

        GridPane detailsLayout = new GridPane();
        Label name = new Label("Name: " + todo.getName());
        detailsLayout.add(name, 0,0);
        GridPane.setColumnSpan(name, 2);

        Label lblDesc = new Label("Description: ");
        detailsLayout.add(lblDesc, 0, 1);

        TextArea description = new TextArea(todo.getDescription());
        description.setEditable(false);
        description.setWrapText(true);
        detailsLayout.add(description, 0, 2);
        GridPane.setColumnSpan(description, 2);

        Label done = new Label("Done? " + (todo.isDone() ? "Yes" : "No"));
        detailsLayout.add(done, 0, 3);

        Button btnDone = new Button("Mark as Done");
        Button btnClose = new Button("Close");
        detailsLayout.add(btnDone, 0, 4);
        detailsLayout.add(btnClose, 1, 4);

        showPop(detailsLayout, "View To Do");

        btnDone.setOnMouseClicked(e -> {
            try {
                database.markAsDone(todo.getId());
                table.refreshList();
                pop.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnClose.setOnMouseClicked(e -> pop.close());
    }
    public void add() {
        GridPane detailsLayout = new GridPane();
        Label lblName = new Label("Name: ");
        detailsLayout.add(lblName, 0,0);
        TextField name = new TextField();
        detailsLayout.add(name, 1, 0);

        Label lblDesc = new Label("Description: ");
        detailsLayout.add(lblDesc, 0, 1);
        GridPane.setColumnSpan(lblDesc, 2);
        TextArea description = new TextArea();
        detailsLayout.add(description, 0, 2);
        GridPane.setColumnSpan(description, 2);

        Button btnAdd = new Button("Add");
        Button btnClose = new Button("Close");
        detailsLayout.add(btnAdd, 0, 3);
        detailsLayout.add(btnClose, 1, 3);

        showPop(detailsLayout, "Add To Do");

        btnAdd.setOnMouseClicked(e -> {
            if (checkInput(name)) {
                try {
                    database.add(new Todo(name.getText(), description.getText()));
                    table.refreshList();
                    pop.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        btnClose.setOnMouseClicked(e -> pop.close());
    }
    private void showPop(GridPane layout, String title) {
        this.pop = new Stage();
        layout.setVgap(8);
        layout.setHgap(8);
        layout.setPadding(new Insets(10));

        pop.setTitle(title);
        pop.initModality(Modality.APPLICATION_MODAL);
        pop.setScene(new Scene(layout));
        pop.setResizable(false);
        pop.show();
    }

    private boolean checkInput(TextField textField) {
        if (textField.getText().isEmpty() || textField.getText().isBlank()) {
            showPromptAlert();
            return false;
        }
        return true;
    }
    private void showPromptAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please enter task name.");
        alert.showAndWait();
    }
}
