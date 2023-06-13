package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is the task object class
 */
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

    /**
     * Constructor for TaskClass
     * @param taskName The name of the task
     * @param taskDescription The description of the task
     * @param developerDetails The developer details of the task
     * @param taskDuration The duration of the task
     */
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

    /**
     * This method checks that the provided task name is valid
     * @return True if the task name is valid, false otherwise
     */
    public boolean checkTaskName() {
        return this.taskName.length()>=2; //task name must be at least 2 characters long
    }

    /**
     * This method checks that the provided developer details are valid
     * @return True if the developer details are valid, false otherwise
     */
    public boolean checkDeveloperDetails() {
        return this.developerDetails.length()>=3; //developer name must be at least 3 characters long
    }

    /**
     * This method checks that the provided task duration is valid
     * @return True if the task duration is valid, false otherwise
     */
    public boolean checkTaskDescription() {
        return this.taskDescription.length()<=50; //task description must be less than 50 characters long
    }

    /**
     * This method creates the task ID
     * @return The task ID
     */
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

    /**
     * This method returns the task details
     * It also formats the task description to be more readable
     * @return The task details
     */
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

    /**
     * This method gets the total hours of all tasks
     * @return The total hours of all tasks
     */
    public static int getTotalHours() {
        return mapDetailsHours.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * This method gets the total hours of all tasks for a specific developer
     * @param developerDetails The developer details
     * @return The total hours of all tasks for a specific developer
     */
    public static int getFilteredHours(String developerDetails) {
        //FIXME: can't filter by name due to duplicate names
        //TODO: be able to filter using mapDetailsNames
        Map<String, Integer> filteredMap = mapDetailsHours.entrySet()
                .stream().filter(x-> Objects.equals(x.getKey(), developerDetails))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return filteredMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * This method adds the hours of a task to the map
     * @param taskID The task ID
     * @param duration The duration of the task
     */
    private void addHours(String taskID, int duration) {
        mapDetailsHours.put(taskID, duration);
    }

    /**
     * This method adds the hours of this task to the map
     */
    private void addHours() {
        addHours(this.taskID, this.taskDuration);
    }

    /**
     * This method edits the hours of a task in the map
     * @param taskID The task ID
     * @param duration The new duration of the task
     */
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

    /**
     * This method formats the task description to be more readable
     * @param DESC The task description
     * @param maxLineLength The maximum length of a line
     * @return The formatted task description
     */
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
