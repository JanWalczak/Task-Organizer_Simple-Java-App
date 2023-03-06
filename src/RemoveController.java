import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * RemoveController class is responsible for the activity in the removeScene.
 */

public class RemoveController implements Initializable {

    private TaskDataBase tdb;
    CheckBox selected = new CheckBox();


    @FXML
    private Pane removeScene;


    @FXML
    private TableColumn<TableSetterGetter, Task> task;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> check;
    @FXML
    private TableView<TableSetterGetter> tableViewTasks;
    @FXML
    ObservableList<TableSetterGetter> list = FXCollections.observableArrayList();


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


        tableViewTasks.setItems(list);
        task.setCellValueFactory(new PropertyValueFactory<>("Task"));
        check.setCellValueFactory(new PropertyValueFactory<>("Checkbox"));
    }

    /**
     * remove method is responsible for removing the selected task from the database
     * @throws IOException
     */
    @FXML
    private void remove() throws IOException {
        for (int i = 0; i<tdb.getTaskQueue().size(); i++){
            if(tableViewTasks.getItems().get(i).getCheckbox().isSelected()){
                tdb.removeTask(tableViewTasks.getItems().get(i).getTask());
                tableViewTasks.getItems().remove(tableViewTasks.getItems().get(i));
            }
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/removeScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) removeScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);
    }

    /**
     * back method is responsible for returning to the scene with all tasks.
     * @throws IOException
     */
    @FXML
    private void back() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/allScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) removeScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);
    }
}