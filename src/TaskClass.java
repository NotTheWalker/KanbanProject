import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskClass {
    private static final HashMap<String, Integer> mapDetailsHours = new HashMap<>();
    public static final Map<String, String> mapDetailsNames = new HashMap<>();
    private String taskName;
    private static int taskCount = 0;
    private int taskNumber;
    private String taskDescription;
    private String developerDetails;
    private int taskDuration;
    private String taskID;
    private StatusEnum taskStatus;

    public TaskClass(String taskName, String taskDescription, String developerDetails, int taskDuration) {
        taskCount++;
        this.taskName = taskName;
        this.taskNumber = taskCount;
        this.taskDescription = taskDescription;
        this.developerDetails = developerDetails;
        this.taskDuration = taskDuration;
        this.taskID = createTaskID();
        this.addHours();
        this.taskStatus = StatusEnum.TO_DO;
    }

    public String getTaskName() {
        return taskName;
    }

    public static int getTaskCount() {
        return taskCount;
    }

    public static void setTaskCount(int taskCount) {
        TaskClass.taskCount = taskCount;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getDeveloperDetails() {
        return developerDetails;
    }

    public int getTaskDuration() {
        return taskDuration;
    }

    public String getTaskID() {
        return taskID;
    }

    public StatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(StatusEnum taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean checkTaskName() {
        return this.taskName.length()>=2; //task name must be at least 2 characters long
    }

    public boolean checkDeveloperDetails() {
        return this.developerDetails.length()>=3; //developer name must be at least 3 characters long
    }

    public boolean checkTaskDescription() {
        return this.taskDescription.length()<=50; //task description must be less than 50 characters long
    }

    private String createTaskID() {
        String details = this.developerDetails; //for readability
        StringBuilder sb = new StringBuilder();
        sb.append(this.taskName, 0, 2); //first two letters of task name
        sb.append(":");
        sb.append(this.taskNumber); //task number
        sb.append(":");
        sb.append(details.substring(details.length()-3)); //last three letters of developer name
        return sb.toString().toUpperCase();
    }

    public String getTaskDetails() {
        return "TASK\n" +
                "Name: " + this.taskName + "\n" +
                "Number: " + this.taskNumber + "\n" +
                "Description: \n" + formatDescription() + "\n" +
                "Developer: " + this.developerDetails + "\n" +
                "Duration: " + this.taskDuration + "\n" +
                "ID: " + this.taskID + "\n" +
                "Status: " + this.taskStatus.describe();
    }

    public static int getTotalHours() {
        return mapDetailsHours.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static int getFilteredHours(String developerDetails) {
        //FIXME: can't filter by name due to duplicate names
        //TODO: be able to filter using mapDetailsNames
        Map<String, Integer> filteredMap = mapDetailsHours.entrySet()
                .stream().filter(x-> Objects.equals(x.getKey(), developerDetails))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return filteredMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    private void addHours(String taskID, int duration) {
        mapDetailsHours.put(taskID, duration);
    }

    private void addHours() {
        addHours(this.taskID, this.taskDuration);
    }

    private void editHours(String taskID, int duration) {
        mapDetailsHours.replace(taskID, duration);
    }

    private void addName(String details, String name) {
        String nameInID = details.substring(details.length()-3).toUpperCase();
        mapDetailsNames.put(nameInID, name);
    }

    public void addName(String name) {
        addName(this.developerDetails, name);
    }

    private String formatDescription() {
        return formatDescription(this.taskDescription, 25);
    }

    private String formatDescription(int maxLineLength) {
        return formatDescription(this.taskDescription, maxLineLength);
    }

    private String formatDescription(String DESC, int maxLineLength) {
        String[] words = DESC.split(" "); // get list of works
        StringBuilder line = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (line.length() + word.length() > maxLineLength) { //add line to result if it full
                result.append(line).append("\n");
                line = new StringBuilder(); //reset line is empty
            }
            line.append(word).append(" ");
        }
        result.append(line);
        return result.toString();
    }

}
