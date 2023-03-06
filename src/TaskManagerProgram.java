import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TaskManagerProgram class is responsible for running the program.
 */

public class TaskManagerProgram extends Application {

    static TaskDataBase tbd = new TaskDataBase();
    Stage window;
    Scene scene;

    public static void main(String[] args) {

        Timer myTimer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run () {
                tbd.upDate();
                System.out.println("Updated...");
            }
        };

        myTimer.scheduleAtFixedRate(myTask , 0l, 10 * (60*1000)); // Runs every 10 mins


        launch(args);
    }

    /**
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {

            tbd = new TaskDataBase();
            tbd.loadData();

            window = primaryStage;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/FXML/dailyScene.fxml"));

            Parent root = loader.load();

            scene = new Scene(root);
            scene.getStylesheets().add("GUI/Style.css");
            window.setScene(scene);

            window.getIcons().add(new Image("GUI/icon.png"));
            systemTray();
            window.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * systemTray method is responsible for minimizing the program to system tray.
     */
    public void systemTray(){
        FXTrayIcon trayIcon = new FXTrayIcon(window, getClass().getResource("GUI/icon.png"));
        MenuItem openTaskManager = new MenuItem("Open Task Manager");
        MenuItem close = new MenuItem("Close");

        openTaskManager.setOnAction(e -> window.show());
        close.setOnAction(e -> {
            System.out.println("Stage is closing");
            tbd.saveData();
            System.exit(0);
        });

        trayIcon.addMenuItem(openTaskManager);
        trayIcon.addMenuItem(close);
        trayIcon.show();
    }
}