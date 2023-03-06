import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * AddController class is responsible for the activity in the addScene.
 */

public class AddController implements Initializable {

    private TaskDataBase tdb;
    private double choiceValue;

    @FXML
    private TextField name;
    @FXML
    private TextField description;
    @FXML
    private DatePicker deadlineDay;
    @FXML
    private ChoiceBox<Integer> hourChoice;
    @FXML
    private ChoiceBox<Integer> minutesChoice;


    @FXML
    private GridPane addScene;

    @FXML
    private ChoiceBox<String> choiceBox;

    String[] options = {"High","Medium","Low"};


    @FXML
    private TableColumn<TableSetterGetter, Task> task1;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> check1;
    @FXML
    private TableView<TableSetterGetter> tableViewThisDependsOn;
    @FXML
    ObservableList<TableSetterGetter> list1 = FXCollections.observableArrayList();

    @FXML
    private TableColumn<TableSetterGetter, Task> task2;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> check2;
    @FXML
    private TableView<TableSetterGetter> tableViewThatDependOnThis;
    @FXML
    ObservableList<TableSetterGetter> list2 = FXCollections.observableArrayList();

    private LocalDateTime deadline;
    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tdb = new TaskDataBase();

        choiceBox.getItems().addAll(options);
        choiceBox.setOnAction(this::getBasePriority);

        int i = 0, j = 0;
        for (Object o : tdb.getTaskQueue()){
            if(o.getClass()==Task.class) {
                Task newTask = (Task) o;
                CheckBox cb = new CheckBox(" "+(i+1));
                list1.add(new TableSetterGetter(newTask, cb));
            }
            i++;
        }
        for (Object o : tdb.getTaskQueue()){
            if(o.getClass()==Task.class) {
                Task newTask = (Task) o;
                CheckBox cb = new CheckBox(" "+(j+1));
                list2.add(new TableSetterGetter(newTask, cb));
            }
            j++;
        }

        tableViewThisDependsOn.setItems(list1);
        tableViewThatDependOnThis.setItems(list2);
        task1.setCellValueFactory(new PropertyValueFactory<>("Task"));
        check1.setCellValueFactory(new PropertyValueFactory<>("Checkbox"));
        task2.setCellValueFactory(new PropertyValueFactory<>("Task"));
        check2.setCellValueFactory(new PropertyValueFactory<>("Checkbox"));

        for (int k=0; k<60; k++){
            if(k<24)
            hourChoice.getItems().add(k);
            minutesChoice.getItems().add(k);
        }
    }

    /**
     * getBasePriority method is responsible for getting the selected base priority.
     * @param actionEvent
     */
    private void getBasePriority(ActionEvent actionEvent) {
        String choice = choiceBox.getValue();
        BasePriorityChoice priorityChoice = BasePriorityChoice.valueOf(choice);
        choiceValue = priorityChoice.getBaseValue();
    }

    /**
     * validateInput method is responsible for validating the entered data.
     * @return
     */
    private boolean validateInput(){
        Instant instant = deadline.atZone(ZoneId.systemDefault()).toInstant();
        long deadlineInMilis = instant.toEpochMilli();
        Instant currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        long currentTimeInMillis = currentTime.toEpochMilli();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if(name.getText().equals(null) || name.getText().trim().isEmpty()){
           alert.setContentText("You cannot leave empty name field");
           alert.showAndWait();
           return false;
        }
        else if(deadlineInMilis-currentTimeInMillis<0){
            alert.setContentText("Invalid deadline, please enter it again");
            alert.showAndWait();
            return false;
        }
        else if(description.getText().equals(null)|| description.getText().trim().isEmpty()){
            alert.setContentText("You cannot leave empty description field");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * addTask method is responsible for fetching data from GUI and adding a new task to the database.
     * @throws IOException
     */
    @FXML
    private void addTask() throws IOException {
        if(deadlineDay.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You cannot leave empty deadline date");
            alert.showAndWait();
            resetScene();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder deadlineHour = new StringBuilder(" ");

        if(hourChoice.getValue()<10) deadlineHour.append("0"+hourChoice.getValue()+":");
        else deadlineHour.append(hourChoice.getValue()+":");

        if(minutesChoice.getValue()<10) deadlineHour.append("0"+minutesChoice.getValue());
        else deadlineHour.append(minutesChoice.getValue());

        String deadlineString = deadlineDay.getValue().toString() + deadlineHour;
        deadline = LocalDateTime.parse(deadlineString, formatter);


        List<Task> thisTaskDependsOn = new ArrayList<>();
        List<Task> tasksThatDependOnThis = new ArrayList<>();

        for (int i = 0; i<tdb.getTaskQueue().size(); i++){
            if(tableViewThisDependsOn.getItems().get(i).getCheckbox().isSelected()){
                thisTaskDependsOn.add(tableViewThisDependsOn.getItems().get(i).getTask());
            }
            if(tableViewThatDependOnThis.getItems().get(i).getCheckbox().isSelected()){
                tasksThatDependOnThis.add(tableViewThatDependOnThis.getItems().get(i).getTask());
            }
        }
        if(validateInput()){
            Task task = new Task(name.getText(),description.getText(),deadline,choiceValue,
                    thisTaskDependsOn,tasksThatDependOnThis);
            boolean t = tdb.addNewTask(task);
            if(t){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot create cycles!");
                alert.showAndWait();
                resetScene();
            }else{
                back();
            }
        }else{
            resetScene();
        }
    }

    /**
     * resetScene method is responsible for clearing all fields and returning to the initial state of this scene.
     * @throws IOException
     */
    @FXML
    private void resetScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/addScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) addScene.getScene().getWindow();

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

        Stage stage = (Stage) addScene.getScene().getWindow();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Style.css");
        stage.setScene(scene);
    }
}