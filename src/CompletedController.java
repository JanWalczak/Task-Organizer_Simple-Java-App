import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * CompletedController class is responsible for the activity in the completedScene.
 */

public class CompletedController implements Initializable {

    private TaskDataBase tdb;

    CheckBox selected = new CheckBox();

    @FXML
    private GridPane completedScene;

    //@FXML
    //private TreeView<Task> treeViewCompleted;

    @FXML
    private TableColumn<TableSetterGetter, Task> task;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> check;
    @FXML
    private TableView<TableSetterGetter> tableViewToComplete;
    @FXML
    ObservableList<TableSetterGetter> list = FXCollections.observableArrayList();

    @FXML
    private ListView<Task> listViewCompleted;





    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tdb = new TaskDataBase();

        CheckBox[] array = new CheckBox[tdb.getTaskQueue().size()];


        int i = 0;
        for (Object o : tdb.getTaskQueue()){
            if(o.getClass()==Task.class) {
                Task newTask = (Task) o;
                CheckBox cb = new CheckBox(" "+(i+1));
                array[i] = cb;
                list.add(new TableSetterGetter(newTask, cb));
            }
            i++;
        }

        for (int j = 0;j < array.length;j++){
            int finalJ = j;
            array[j].selectedProperty().addListener((o, oldV, newV) -> {
            if(selected.equals(null)){
                selected = array[finalJ];
            }if(selected.equals(array[finalJ])){
                    selected = array[finalJ];
                }else{
                selected.setSelected(false);
                selected = array[finalJ];
            }
            });
        }

        tableViewToComplete.setItems(list);
        task.setCellValueFactory(new PropertyValueFactory<>("Task"));
        check.setCellValueFactory(new PropertyValueFactory<>("Checkbox"));

        ArrayList<Task> temp = new ArrayList<>();
        for (Object o : tdb.getCompletedTasks()){
            if(o.getClass()==Task.class) {
                Task newTask = (Task) o;
                temp.add(newTask);
            }
        }
        listViewCompleted.getItems().addAll(temp);
    }

    /**
     * showAll method is responsible for changing the current scene to scene with all tasks.
     * @throws IOException
     */
    @FXML
    private void showAll() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/allScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) completedScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);

    }

    /**
     * showDaily method is responsible for changing the current scene to scene with tasks for today.
     * @throws IOException
     */
    @FXML
    private void showDaily() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/dailyScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) completedScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);

    }

    /**
     * completeTask method is responsible for moving the task from list of to-do tasks to list of completed tasks.
     * @throws IOException
     */
    @FXML
    private void completeTask() throws IOException {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        for (int i = 0; i<tdb.getTaskQueue().size(); i++){
            if(tableViewToComplete.getItems().get(i).getCheckbox().isSelected()){

                if (tableViewToComplete.getItems().get(i).getTask().getThisTaskDependsOn()==null || tableViewToComplete.getItems().get(i).getTask().getThisTaskDependsOn().isEmpty()){
                    tdb.completeTask(tableViewToComplete.getItems().get(i).getTask());
                    tableViewToComplete.getItems().remove(tableViewToComplete.getItems().get(i));
                }

                else if(!tableViewToComplete.getItems().get(i).getTask().getThisTaskDependsOn().isEmpty()){
                    alert.setContentText("You cannot complete task before completing related tasks.");
                    alert.showAndWait();
                }


            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/completedScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) completedScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);
    }
}
