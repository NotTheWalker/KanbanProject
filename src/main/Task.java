package src.main;

public class Task {
    private String name;
    private int taskNumber;
    private String description;
    private String developerDetails;
    private int estimatedDuration;
    private String taskID;
    private Status taskStatus;

    public Task(String name, int taskNumber, String description, String developerDetails, int estimatedDuration) {
        this.name = name;
        this.taskNumber = taskNumber;
        this.description = description;
        this.developerDetails = developerDetails;
        this.estimatedDuration = estimatedDuration;
        this.taskID = firstTwo(this.name)+":"+lastThree(this.developerDetails);
        this.taskStatus = Status.TO_DO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeveloperDetails() {
        return developerDetails;
    }

    public void setDeveloperDetails(String developerDetails) {
        this.developerDetails = developerDetails;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    private String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }
    private String lastThree(String str) {
        return str.length() < 3 ? str : str.substring(str.length()-3);
    }
}
