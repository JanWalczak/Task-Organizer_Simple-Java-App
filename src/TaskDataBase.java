import javafx.scene.control.Alert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TaskDataBase class is responsible for creating a new object to store the data read from the file.
 */

public class TaskDataBase {

    private LoadSaveFile fileOperator = new LoadSaveFile();
    private static SortedSet<Task> taskQueue = new TreeSet<>();
    private static List<Task> completedTasks = new ArrayList<>(); //list

    /**
     * upDate method is responsible for updating the priority - recalculating it with the current time.
     */
    public void upDate(){
        taskQueue.forEach(task -> countPriority(task));
    }

    /**
     * countPriority method is responsible for counting the priority based on the given base priority
     * and the time remaining to the deadline.
     * @param task
     */
    public void countPriority(Task task){
        long diff = Duration.between(LocalDateTime.now(),task.getDeadline()).toMinutes();
        task.setCountedPriority((diff^(3/2)) * (long)task.getBasePriority());
    }

    /**
     * loadData method is responsible for reading the data from the file using LoadSaveFile object.
     */
    public void loadData(){
        fileOperator.loadFromFile();
        for (Task t:fileOperator.getToDo()) {
            countPriority(t);
        }

        taskQueue.addAll(fileOperator.getToDo());
        if(fileOperator.getDone()!=null){
            completedTasks.addAll(fileOperator.getDone());
        }
    }

    /**
     * addNewTask method is responsible for adding a new task to the database.
     * @param task
     */
    public boolean addNewTask(Task task){

        if(task.getThisTaskDependsOn()!=null){
            for (Task t:task.getThisTaskDependsOn()) {
                if(t.getTasksThatDependOnThis()==null){
                    t.setTasksThatDependOnThis(new ArrayList<>());
                }
                t.getTasksThatDependOnThis().add(task);
            }
        }
        if(task.getTasksThatDependOnThis()!=null) {
            for (Task t : task.getTasksThatDependOnThis()) {
                if(t.getThisTaskDependsOn()==null){
                    t.setThisTaskDependsOn(new ArrayList<>());
                }
                t.getThisTaskDependsOn().add(task);
            }
        }

        countPriority(task);
        upDate();
        taskQueue.add(task);

        if(isCyclic(task)){
            removeTask(task);
            return true;
        }
        return false;

    }


    private boolean isCyclicUtil(Task task, Task target) {

        boolean test = false;
        if(task.equals(target)){
            return true;
        }
        if(task.getThisTaskDependsOn()==null){
            return false;
        }else if(task.getThisTaskDependsOn().size()==0){
            return false;
        }

        for (Task t: task.getTasksThatDependOnThis()) {
            if(isCyclicUtil(t,target)){
                test = true;
            }
        }
        return test;
    }



    public boolean isCyclic(Task task) {
        boolean test = false;

        if(task.getThisTaskDependsOn()==null){
            return false;
        }else if(task.getThisTaskDependsOn().size()==0){
            return false;
        }
        for (Task t: task.getThisTaskDependsOn()) {
            if(t.getThisTaskDependsOn()!=null){
                if(isCyclicUtil(t,task)){
                    test = true;
                }
            }

        }

        return test;
    }



    /**
     * addNewTask method is responsible for removing a task from the database.
     * @param task
     */
    public void removeTask(Task task){
        if(task.getThisTaskDependsOn()!=null){
            for (Task t:task.getThisTaskDependsOn()) {
                if(t != null){
                    t.getTasksThatDependOnThis().remove(task);
                }
            }
        }

        if(task.getTasksThatDependOnThis()!=null){
            for (Task t:task.getTasksThatDependOnThis()) {
                if(t != null){
                    t.getThisTaskDependsOn().remove(task);
                }
            }
        }
        taskQueue.remove(task);
    }

    /**
     * completeTask method is responsible for moving the task from list of to-do tasks to list of completed tasks.
     * @param task
     */
    public void completeTask(Task task){

        if(task.getThisTaskDependsOn()!=null){
             for (Task t:task.getThisTaskDependsOn()) {
                 if(t != null){
                     t.getTasksThatDependOnThis().remove(task);
                 }
             }
        }

        if(task.getTasksThatDependOnThis()!=null){
            for (Task t:task.getTasksThatDependOnThis()) {
                if(t != null){
                    t.getThisTaskDependsOn().remove(task);
                }
            }
        }


        completedTasks.add(task);
        taskQueue.remove(task);
    }

    /**
     * saveData method is responsible for writing data to a file using LoadSaveFile object.
     */
    public void saveData(){
        fileOperator.saveToFile(taskQueue.stream().toList(),completedTasks);
    }

    public SortedSet getTaskQueue() {
        return taskQueue;
    }

    public List<Task> getCompletedTasks() {
        return completedTasks;
    }
}
