package src.main;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Task {
    Logger logger = Logger.getLogger(Task.class.getName());
    public static final HashMap<String, Integer> mapDetailsInstance = new HashMap<>();
    public static final HashMap<String, Integer> mapDetailsHours = new HashMap<>();
    private String name;
    private int taskNumber;
    private String description;
    private String developerDetails;
    private int estimatedDuration;
    private String taskID;
    private Status taskStatus;

    public Task() {
    }

    public Task(String taskName, String description, String developerDetails, int estimatedDuration) {
        this.name = taskName;
        this.description = description;
        this.developerDetails = developerDetails;
        this.estimatedDuration = estimatedDuration;
        this.taskStatus = Status.TO_DO;
        int latest = latestInstance();
        mapDetailsInstance.put(this.developerDetails, latest);
        this.taskNumber = latest;
        this.taskID = createTaskID();
        mapDetailsHours.put(this.developerDetails, this.estimatedDuration);
    }

    public Task(String[] allDeveloperDetails) {

        final JFrame frame = new JFrame("New Task");

        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String nameMessage = "Task name:";
        JTextField nameField = new JTextField();

        String descriptionMessage = "Task description: ";
        JTextField descriptionField = new JTextField();
        descriptionField.createToolTip();
        descriptionField.setToolTipText("Maximum 50 characters");

        String durationMessage = "Estimated duration (hours):";
        JTextField durationField = new JTextField();

        JScrollPane developerDetailsScroller = new JScrollPane();
        Object[] taskComponents = {
                nameMessage, nameField,
                descriptionMessage, descriptionField,
                durationMessage, durationField
        };

        FlowLayout taskLayout = new FlowLayout();
        frame.setLayout(taskLayout);

        int latest = latestInstance();
        int returnCode = JOptionPane.showOptionDialog(frame, taskComponents, "Task "+latest, JOptionPane.OK_CANCEL_OPTION ,JOptionPane.PLAIN_MESSAGE, null, null, null);
        logger.info("Task pane: "+returnCode);
        this.name = nameField.getText();
        this.description = descriptionField.getText();
        this.developerDetails = allDeveloperDetails[0]; //FIXME: Testing value
        this.estimatedDuration = Integer.parseInt(durationField.getText());
        this.taskStatus = Status.TO_DO;
        mapDetailsInstance.put(this.developerDetails, latest);
        this.taskNumber = latest;
        this.taskID = createTaskID();
        mapDetailsHours.put(this.developerDetails, this.estimatedDuration);
    }

    public boolean checkTaskDescription() {
        return checkTaskDescription(this.description);
    }

    public boolean checkTaskDescription(String description) {
        return description.length()<=50;
    }

    public String createTaskID() {
        return createTaskID(this.name, this.taskNumber, this.developerDetails);
    }
    public String createTaskID(String name, int taskNumber, String devDetails) {
        String firstTwo = name.length() < 2 ? name : name.substring(0, 2);
        String lastThree = devDetails.length() < 3 ? devDetails : devDetails.substring(devDetails.length()-3);
        return firstTwo.toUpperCase()+":"+taskNumber+":"+lastThree.toUpperCase();
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

    public void setTaskID() {
        this.taskID = createTaskID();
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public int getTaskStatusMenu() {
        return taskStatus.ordinal();
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setTaskStatus(int menuOption) {
        switch (menuOption) {
            case 0 -> this.taskStatus = Status.TO_DO;
            case 1 -> this.taskStatus = Status.DOING;
            case 2 -> this.taskStatus = Status.DONE;
        }
    }

    public int latestInstance() {
        if(mapDetailsInstance.size()==0) {
            return 0;
        } else {
            OptionalInt latest = mapDetailsInstance.values().stream().mapToInt(Integer::intValue).max();
            return latest.getAsInt();
        }
    }

    public static int getTotalHours() {
        return mapDetailsHours.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static int getFilteredHours(String filterName) {
        Map<String, Integer> filteredMap = mapDetailsHours.entrySet()
                .stream().filter(x-> Objects.equals(x.getKey(), filterName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return filteredMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void addDetailsHours() {
        mapDetailsHours.put(this.developerDetails, this.estimatedDuration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", taskNumber=" + taskNumber +
                ", description='" + description + '\'' +
                ", developerDetails='" + developerDetails + '\'' +
                ", estimatedDuration=" + estimatedDuration +
                ", taskID='" + taskID + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
