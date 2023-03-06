import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

/**
 * DailyController class is responsible for the activity in the dailyScene.
 */

public class DailyController implements Initializable {

    private TaskDataBase tdb;
    private List<Task> dailyTasks = new ArrayList<>();

    @FXML
    private GridPane dailyScene;

    @FXML
    private TreeView<String> treeViewDaily;

    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tdb = new TaskDataBase();

        LocalDateTime date = LocalDateTime.now();
        for(Object obj : tdb.getTaskQueue()){
            if(obj.getClass() == Task.class){
                Task task = (Task) obj;
                if(task.getDeadline().getYear()==date.getYear()
                        &&task.getDeadline().getDayOfYear()== date.getDayOfYear()){
                    dailyTasks.add(task);
                }
            }
        }

        TreeItem<String> root = new TreeItem<>(" ");
        for(Task task : dailyTasks){
            TreeItem<String> item = new TreeItem<>(task.toString());
            root.getChildren().add(item);
            if (task.getThisTaskDependsOn()!=null)
                for(Task subTask : task.getThisTaskDependsOn()){
                    TreeItem<String> subItem = new TreeItem<>(subTask.toString());
                    item.getChildren().add(subItem);
                }
        }
        treeViewDaily.setRoot(root);
        treeViewDaily.getRoot().setExpanded(true);
    }

    /**
     * showAll method is responsible for changing the current scene to scene with all tasks.
     * @throws IOException
     */
    @FXML
    private void showAll() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/allScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) dailyScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);

    }

    /**
     * showCompleted method is responsible for changing the current scene to scene with completed tasks.
     * @throws IOException
     */
    @FXML
    private void showCompleted() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/completedScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) dailyScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);

    }
}
