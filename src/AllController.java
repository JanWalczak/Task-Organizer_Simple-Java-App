import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * AllController class is responsible for the activity in the allScene.
 */

public class AllController implements Initializable {

    private TaskDataBase tdb;

    @FXML
    private GridPane allScene;

    @FXML
    private TreeView<String> treeViewAll;

    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tdb = new TaskDataBase();

        TreeItem<String> root = new TreeItem<>(" ");
        for(Object obj : tdb.getTaskQueue()){
            if(obj.getClass()==Task.class){
                Task task = (Task) obj;
                TreeItem<String> item = new TreeItem<>(task.toString());
                root.getChildren().add(item);
                if (task.getThisTaskDependsOn()!=null)
                    for(Task subTask : task.getThisTaskDependsOn()){
                        TreeItem<String> subItem = new TreeItem<>(subTask.toString());
                        item.getChildren().add(subItem);
                    }
            }
        }
        treeViewAll.setRoot(root);
        treeViewAll.getRoot().setExpanded(true);
    }

    /**
     * showDaily method is responsible for changing the current scene to scene with tasks for today.
     * @throws IOException
     */
    @FXML
    private void showDaily() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/dailyScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) allScene.getScene().getWindow();

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

        Stage stage = (Stage) allScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);

    }

    /**
     * showAdd method is responsible for changing the current scene
     * into a scene with the possibility of adding a new task.
     * @throws IOException
     */
    @FXML
    private void showAdd() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/addScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) allScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);
    }

    /**
     * showRemove method is responsible for changing the current scene
     * into a scene with the possibility of removing a chosen task.
     * @throws IOException
     */
    @FXML
    private void showRemove() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/removeScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) allScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);
    }
}
