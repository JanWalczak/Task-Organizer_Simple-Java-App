import javafx.scene.control.CheckBox;

/**
 * TableSetterGetter class is responsible for manipulating the table in gui.
 */

public class TableSetterGetter {

    Task task;
    CheckBox checkbox;

    public TableSetterGetter(Task task, CheckBox checkbox) {
        this.task = task;
        this.checkbox = checkbox;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }
}
