import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * LoadSaveFile class is responsible for reading from and writing to a json file.
 */

public class LoadSaveFile {

    private List<Task> toDo,done;
    private String toDoFile = "toDo.json";
    private String doneFile = "done.json";

    /**
     * LocalDateTimeSerializer class is responsible for serializing the date.
     */
    class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDateTime));
        }
    }

    /**
     * LocalDateTimeDeserializer class is responsible for deserializing the date.
     */
    class LocalDateTimeDeserializer implements JsonDeserializer <LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(),
                    DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss"));
        }
    }

    /**
     * loadFromFile method is responsible for reading the data from the file.
     */

    public Task taskFinder(UUID id){
        for (Task t:toDo) {
            if(id.equals(t.getId())){
                return t;
            }
        }
        return null;
    }

    public void loadFromFile(){
        try (Reader reader = Files.newBufferedReader(Paths.get(toDoFile))){

            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
            toDo = gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try (Reader reader = Files.newBufferedReader(Paths.get(doneFile))){

            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
            done = gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (Task t:toDo) {
            if(t.getIdListDependsOn()!=null){
                for (UUID id:t.getIdListDependsOn()) {
                    if(t.getThisTaskDependsOn()==null){
                        t.setThisTaskDependsOn(new ArrayList<>());
                    }
                    t.getThisTaskDependsOn().add(taskFinder(id));
                }
            }
            if(t.getIdListThatDependOnThis()!=null){
                for (UUID id:t.getIdListThatDependOnThis()) {
                    if(t.getTasksThatDependOnThis()==null){
                        t.setTasksThatDependOnThis(new ArrayList<>());
                    }
                    t.getTasksThatDependOnThis().add(taskFinder(id));
                }
            }
        }

        for (Task t:toDo) {
            t.setIdListDependsOn(new ArrayList<>());
            t.setIdListThatDependOnThis(new ArrayList<>());
        }

    }

    /**
     * saveToFile method is responsible for writing the data to the file.
     * @param toDo
     * @param done
     * @return
     */
    public String saveToFile(List<Task> toDo,List<Task> done){
        String ret = "";


        for (Task t:toDo) {

            if(t.getTasksThatDependOnThis()!=null){
                for (Task task:t.getTasksThatDependOnThis()) {
                    if(task.getIdListDependsOn()==null){
                        task.setIdListDependsOn(new ArrayList<>());
                    }
                    task.getIdListDependsOn().add(t.getId());
                }
            }
            if(t.getThisTaskDependsOn()!=null){
                for (Task task:t.getThisTaskDependsOn()) {
                    if(task.getIdListThatDependOnThis()==null){
                        task.setIdListThatDependOnThis(new ArrayList<>());
                    }
                    task.getIdListThatDependOnThis().add(t.getId());
                }
            }

        }


        try (Writer writer = Files.newBufferedWriter(Paths.get(toDoFile))) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).setPrettyPrinting().create();
            gson.toJson(toDo, writer);
        } catch (IOException e) {
            ret += "Error, cannot save to file: " + toDoFile +'\n';
        }
        ret += "Successfully written to file: "  + toDoFile +'\n';

        try (Writer writer = Files.newBufferedWriter(Paths.get(doneFile))) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).setPrettyPrinting().create();
            gson.toJson(done, writer);
        } catch (IOException e) {
            ret += "Error, cannot save to file: " + doneFile +'\n';
        }
        ret += "Successfully written to file: "  + doneFile +'\n';
        return ret;
    }

    public List<Task> getToDo() {
        return toDo;
    }

    public List<Task> getDone() {
        return done;
    }



}
