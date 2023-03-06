import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

public class ProgramTest {

    /**
     * Test in which we add task1 which depends on task2 and then check if task1 was created in 'TasksThatDependOnThis' of task1
     */
    @Test
   public void idLoadSaveTest(){
       TaskDataBase tdb = new TaskDataBase();

       LocalDateTime date1 = LocalDateTime.of(2022,12,7,14,10);
       List<Task> testList = new ArrayList<>();

       Task task1 = new Task("TEST WCZYTYWANIA","Tutaj testujemy wczytywanie razem z ID",date1,1,testList,null);
       Task task2 = new Task("Zalezny","Tutaj testujemy wczytywanie razem z ID",date1,1.5,null,null);

       testList.add(task2);
       tdb.addNewTask(task2);
       tdb.addNewTask(task1);

       Task task2InTaskQueue = null;
       for (Object t:tdb.getTaskQueue()) {
           Task task = (Task)t;
           if(task.equals(task2)){
               task2InTaskQueue = task2;
           }
       }

        assertEquals("[TEST WCZYTYWANIA:\n" +
                "Tutaj testujemy wczytywanie razem z ID\n" +
                "Date: 7-12-2022\n" +
                "Time: 14:10\n" +
                "]", task2InTaskQueue.getTasksThatDependOnThis().toString());

   }


    /**
     * ---------------------WARNING--------------------------
     * Running this test will cause overriding toDo.json file
     */
    @Test
    public void dataBaseSavingTest(){
        TaskDataBase tdb = new TaskDataBase();
        tdb.loadData();
        LocalDateTime date1 = LocalDateTime.of(2020,1,7,14,10);
        LocalDateTime date2 = LocalDateTime.of(2021,1,7,15,25);
        LocalDateTime date3 = LocalDateTime.of(2022,1,7,17,10);
        Task task1 = new Task("Pierszy","Pozdrawiam",date1,1,null,null);
        Task task2= new Task("Drugi" ,"Pozdrowienia",date2,2,null,null);
        Task task3 = new Task("Trzeci Task","Tutaj nie pozdrawiaj",date3,3,null,null);
        tdb.countPriority(task1);
        tdb.countPriority(task2);
        tdb.countPriority(task3);
        tdb.addNewTask(task2);
        tdb.addNewTask(task3);
        tdb.addNewTask(task1);

        tdb.saveData();
    }


    @Test
    public void completeTaskDataBaseTest() {
        TaskDataBase tdb = new TaskDataBase();

        LocalDateTime date1 = LocalDateTime.of(2020,1,7,14,10);
        LocalDateTime date2 = LocalDateTime.of(2021,1,7,15,25);
        LocalDateTime date3 = LocalDateTime.of(2022,1,7,17,10);
        Task task1 = new Task("Pierszy","Pozdrawiam",date1,1,null,null);
        Task task2= new Task("Drugi" ,"Pozdrowienia",date2,2,null,null);
        Task task3 = new Task("Trzeci Task","Tutaj nie pozdrawiaj",date3,3,null,null);
        tdb.addNewTask(task1);
        tdb.addNewTask(task2);
        tdb.addNewTask(task3);

        tdb.completeTask(task1);

        assertEquals("[Drugi:\n" +
                "Pozdrowienia\n" +
                "Date: 7-1-2021\n" +
                "Time: 15:25\n" +
                ", Trzeci Task:\n" +
                "Tutaj nie pozdrawiaj\n" +
                "Date: 7-1-2022\n" +
                "Time: 17:10\n" +
                "]",tdb.getTaskQueue().toString());

        assertEquals("[Pierszy:\n" +
                "Pozdrawiam\n" +
                "Date: 7-1-2020\n" +
                "Time: 14:10\n" +
                "]",tdb.getCompletedTasks().toString());

    }

    @Test
    public void countingPriorityTest1(){
        TaskDataBase tdb = new TaskDataBase();

        LocalDateTime date1 = LocalDateTime.of(2022,12,1,12,00);
        LocalDateTime date2 = LocalDateTime.of(2023,12,1,12,00);
        LocalDateTime date3 = LocalDateTime.of(2024,12,1,12,00);
        Task task1 = new Task("Pierszy","Pierwsza",date1,1,null,null);
        Task task2= new Task("Drugi" ,"Druga",date2,1.5,null,null);
        Task task3 = new Task("Trzeci ","Trzecia",date3,2,null,null);

        tdb.addNewTask(task1);
        tdb.addNewTask(task3);
        tdb.addNewTask(task2);
        System.out.println(tdb.getTaskQueue());

        assertEquals("[Pierszy:\n" +
                "Pierwsza\n" +
                "Date: 1-12-2022\n" +
                "Time: 12:0\n" +
                ", Drugi:\n" +
                "Druga\n" +
                "Date: 1-12-2023\n" +
                "Time: 12:0\n" +
                ", Trzeci :\n" +
                "Trzecia\n" +
                "Date: 1-12-2024\n" +
                "Time: 12:0\n" +
                "]",tdb.getTaskQueue().toString());
    }


    @Test
    public void countingPriorityTest2(){
        TaskDataBase tdb = new TaskDataBase();

        LocalDateTime date1 = LocalDateTime.of(2022,12,1,12,00);
        LocalDateTime date2 = LocalDateTime.of(2022,12,2,12,00);
        LocalDateTime date3 = LocalDateTime.of(2022,12,3,12,00);
        Task task1 = new Task("Pierszy","Pierwsza",date1,1,null,null);
        Task task2= new Task("Drugi" ,"Druga",date2,1,null,null);
        Task task3 = new Task("Trzeci ","Trzecia",date3,1,null,null);

        tdb.addNewTask(task2);
        tdb.addNewTask(task3);
        tdb.addNewTask(task1);


        System.out.println(tdb.getTaskQueue());

        assertEquals("[Pierszy:\n" +
                "Pierwsza\n" +
                "Date: 1-12-2022\n" +
                "Time: 12:0\n" +
                ", Drugi:\n" +
                "Druga\n" +
                "Date: 2-12-2022\n" +
                "Time: 12:0\n" +
                ", Trzeci :\n" +
                "Trzecia\n" +
                "Date: 3-12-2022\n" +
                "Time: 12:0\n" +
                "]",tdb.getTaskQueue().toString());
    }


    @Test
    public void countingPriorityTest3(){
        TaskDataBase tdb = new TaskDataBase();

        LocalDateTime date1 = LocalDateTime.of(2022,12,1,12,10);
        LocalDateTime date2 = LocalDateTime.of(2022,12,1,13,15);
        LocalDateTime date3 = LocalDateTime.of(2022,12,1,13,30);
        Task task1 = new Task("Pierszy","Pierwsza",date1,1,null,null);
        Task task2= new Task("Drugi" ,"Druga",date2,1,null,null);
        Task task3 = new Task("Trzeci ","Trzecia",date3,1,null,null);

        tdb.addNewTask(task2);
        tdb.addNewTask(task1);
        tdb.addNewTask(task3);

        System.out.println(tdb.getTaskQueue());

        assertEquals("[Pierszy:\n" +
                "Pierwsza\n" +
                "Date: 1-12-2022\n" +
                "Time: 12:10\n" +
                ", Drugi:\n" +
                "Druga\n" +
                "Date: 1-12-2022\n" +
                "Time: 13:15\n" +
                ", Trzeci :\n" +
                "Trzecia\n" +
                "Date: 1-12-2022\n" +
                "Time: 13:30\n" +
                "]",tdb.getTaskQueue().toString());
    }



    @Test
    public void loadTest(){
        TaskDataBase tdb = new TaskDataBase();

        tdb.loadData();
        System.out.println(tdb.getTaskQueue());
    }


    /**
     * ---------------------WARNING--------------------------
     * Running this test will cause overriding toDo.json file
     */
    @Test
    public void addingTaskWithIDLists(){
        TaskDataBase tdb = new TaskDataBase();
        tdb.loadData();
        LocalDateTime date1 = LocalDateTime.of(2020,1,1,12,00);
        LocalDateTime date2 = LocalDateTime.of(2021,1,1,12,00);
        List<Task> taskList = new ArrayList<>();

        Task taskZalezny= new Task("Task Zalezny" ,"Ten task jest taskiem zaleznym",date2,1.5,null,null);
        taskList.add(taskZalezny);
        Task task1 = new Task("Test","Dodawania taska w lista zaleznych taskow",date1,1,taskList,null);
        tdb.addNewTask(task1);

        tdb.saveData();

    }


    @Test
    public void isTaskSame(){
        LocalDateTime date = LocalDateTime.of(2022,12,1,12,00);
       Task task1 = new Task("TaskPierwszy" ,"TaskPierwszy",date,1.5,null,null);
       Task task2 = new Task("TaskDrugi" ,"TaskDrugi",date,1.5,null,null);
        assertEquals(false,task1.equals(task2));
        assertEquals(1,task1.compareTo(task2));
    }


    @Test
    public void cyclingCheckTest(){
       TaskDataBase tdb = new TaskDataBase();
       tdb.loadData();

       Task a = (Task) tdb.getTaskQueue().first();
       Task last = (Task) tdb.getTaskQueue().last();

       assertEquals(true,tdb.isCyclic(a));
       assertEquals(false,tdb.isCyclic(last));
    }

}
