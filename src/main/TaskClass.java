import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskClass {
    public static final HashMap<String, Integer> mapDetailsHours = new HashMap<>();
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
        mapDetailsHours.put(developerDetails.toUpperCase(), taskDuration);
        this.taskID = createTaskID();
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

    public boolean checkTaskDescription() {
        return this.taskDescription.length()<=50;
    }

    private String createTaskID() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.taskName, 0, 2);
        sb.append(":");
        sb.append(this.taskNumber);
        sb.append(":");
        sb.append(this.developerDetails, this.developerDetails.length()-3, this.developerDetails.length());
        return sb.toString().toUpperCase();
    }

    public String getTaskDetails() {
        return "TaskClass{" +
                "taskName='" + taskName + '\'' +
                ", taskNumber=" + taskNumber +
                ", taskDescription='" + taskDescription + '\'' +
                ", developerDetails='" + developerDetails + '\'' +
                ", taskDuration=" + taskDuration +
                ", taskID='" + taskID + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

    public static int getTotalHours() {
        return mapDetailsHours.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static int getFilteredHours(String developerDetails) {
        Map<String, Integer> filteredMap = mapDetailsHours.entrySet()
                .stream().filter(x-> Objects.equals(x.getKey(), developerDetails))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return filteredMap.values().stream().mapToInt(Integer::intValue).sum();
    }
}
