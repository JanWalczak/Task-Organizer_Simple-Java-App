import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Task is used to create new objects storing information about the tasks.
 */

public class Task implements Comparable<Task> {
    private String taskName, description;
    private LocalDateTime deadline;
    private double basePriority;
    private transient long countedPriority;
    private transient List<Task> thisTaskDependsOn, tasksThatDependOnThis;
    private List<UUID> idListDependsOn, idListThatDependOnThis;
    private UUID id;

    public Task(String taskName, String description, LocalDateTime deadline,
                double basePriority, List<Task> thisTaskDependsOn, List<Task> tasksThatDependOnThis){
        this.taskName = taskName;
        this.description = description;
        this.deadline = deadline;
        this.basePriority = basePriority;
        this.thisTaskDependsOn = thisTaskDependsOn;
        this.tasksThatDependOnThis = tasksThatDependOnThis;
        this.id = UUID.randomUUID();
    }
    public Task(String name, String dec){
        this.taskName=name;
        this.description=dec;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public double getBasePriority() {
        return basePriority;
    }

    public void setBasePriority(int basePriority) {
        this.basePriority = basePriority;
    }

    public long getCountedPriority() {
        return countedPriority;
    }

    public void setCountedPriority(long countedPriority) {
        this.countedPriority = countedPriority;
    }

    public String getDescription() {
        return description;
    }

    public List<UUID> getIdListDependsOn() {
        return idListDependsOn;
    }

    public List<UUID> getIdListThatDependOnThis() {
        return idListThatDependOnThis;
    }

    public void setIdListThatDependOnThis(List<UUID> idListThatDependOnThis) {
        this.idListThatDependOnThis = idListThatDependOnThis;
    }

    public void setIdListDependsOn(List<UUID> idListDependsOn) {
        this.idListDependsOn = idListDependsOn;
    }

    public void setTasksThatDependOnThis(List<Task> tasksThatDependOnThis) {
        this.tasksThatDependOnThis = tasksThatDependOnThis;
    }

    public void setThisTaskDependsOn(List<Task> thisTaskDependsOn) {
        this.thisTaskDependsOn = thisTaskDependsOn;
    }

    public UUID getId() {
        return id;
    }

    public List<Task> getTasksThatDependOnThis() {
        return tasksThatDependOnThis;
    }

    public List<Task> getThisTaskDependsOn() {
        return thisTaskDependsOn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
    public void countPriority(){
        long diff = Duration.between(LocalDateTime.now(),deadline).toMinutes();
        countedPriority = diff^(3/2) * (long)basePriority;
    }*/

    @Override
    public int compareTo(Task otherTask) {
        if(this.equals(otherTask)){
            return 0;
        }else if(this.countedPriority == otherTask.countedPriority){
            return 1;
        }
        return (int) (getCountedPriority() - otherTask.getCountedPriority());
    }

    @Override
    public String toString() {
        return  taskName + ":" +
                "\n" + description +
                "\nDate: " + deadline.getDayOfMonth() + "-" + deadline.getMonthValue() +"-"+ deadline.getYear() +'\n'+
                "Time: " + deadline.getHour()+":"+deadline.getMinute()+'\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Double.compare(task.basePriority, basePriority) == 0 && countedPriority == task.countedPriority && Objects.equals(taskName, task.taskName) && Objects.equals(description, task.description) && Objects.equals(deadline, task.deadline) && Objects.equals(thisTaskDependsOn, task.thisTaskDependsOn) && Objects.equals(tasksThatDependOnThis, task.tasksThatDependOnThis) && Objects.equals(idListDependsOn, task.idListDependsOn) && Objects.equals(idListThatDependOnThis, task.idListThatDependOnThis) && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, description, deadline, basePriority, countedPriority, thisTaskDependsOn, tasksThatDependOnThis, idListDependsOn, idListThatDependOnThis, id);
    }
}