import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import static java.lang.Long.sum;
import static javax.swing.JOptionPane.*;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class TaskHandlerClass {
    static Logger logger = Logger.getLogger(WorkerClass.class.getName());

    private static JTextField taskNameField = new JTextField();
    private static JTextArea taskDescriptionField = new JTextArea();
    private static JTextField developerField = new JTextField();
    private static JTextField durationField = new JTextField();
    private static TaskClass[] allTasks = new TaskClass[0];
    private static UserClass providedUser;

    private static boolean goodTask = false;
    private static boolean cancelOperation = false;

    public static void setProvidedUser(UserClass providedUser) {
        TaskHandlerClass.providedUser = providedUser;
    }

    public static boolean isGoodTask() {
        return goodTask;
    }

    public static boolean isCancelled() {
        return cancelOperation;
    }

    public static TaskClass[] getAllTasks() {
        return allTasks;
    }

    public static void build() {
        goodTask = false;
        cancelOperation = false;
        int amount = taskAmountPrompt(); //get amount of tasks to add
        ArrayList<TaskClass> taskList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            goodTask = false;
            resetFields();
            do {
                int responseCode = taskPrompt();
                if(responseCode==1&&i==amount-1&&taskList.isEmpty()) responseCode = 2;
                //if the user has manually cancelled all tasks, signal all tasks cancellation
                switch (responseCode) {
                    case 0 -> { //add
                        String taskName = taskNameField.getText();
                        String taskDescription = taskDescriptionField.getText();
                        String developer = developerField.getText();
                        int duration = Integer.parseInt(durationField.getText());
                        TaskClass providedTask = new TaskClass(taskName, taskDescription, developer, duration);
                        if (isValidTask(providedTask)) {
                            goodTask = true;
                            taskList.add(providedTask);
                            logger.info("Task addition signalled " + sum(i,1) + " of " + amount);
                        } else {
                            showMessageDialog(null, "Task invalid", "Error", ERROR_MESSAGE);
                            logger.info("Task invalid");
                        }
                    }
                    case 1 -> { //cancel
                        logger.info("Task cancellation signalled");
                        return;
                    }
                    case 2 -> { //cancel all
                        logger.info("All tasks cancellation signalled");
                        cancelOperation = true;
                        return;
                    }
                }
            } while (!goodTask);
        }
        allTasks = taskList.toArray(new TaskClass[0]);
    }

    private static boolean isValidTask(TaskClass task){
        boolean taskName = task.checkTaskName();
        boolean developerDetails = task.checkDeveloperDetails();
        boolean taskDescription = task.checkTaskDescription();
        return taskName && developerDetails && taskDescription;
    }

    private static int taskAmountPrompt() { //returns amount of tasks to add
        String defaultMessage = "How many tasks would you like to add?";
        String message = "";
        int amount;
        while (true) {
            message = defaultMessage + "\n" + message; //add the default message to the top of the message
            try {
                amount = Integer.parseInt(showInputDialog(message));
                if (amount < 1) { //if the amount is less than 1, ask again
                    message = "Please enter a positive integer";
                } else {
                    return amount; //if the amount is valid, return it
                }
            } catch (NumberFormatException e) { //if the input is not a number, ask again
                message = "Please enter a positive integer";
            }
        }
    }

    private static int taskPrompt() {
        //asks user for task details
        //does not check if the task is valid
        Object[] message = {
                "Task name:", taskNameField,
                "Task description:", taskDescriptionField,
                "Developer:", developerField,
                "Duration:", durationField
        };
        String[] options = {"Add", "Cancel", "Cancel All"}; //0 = add, 1 = cancel, 2 = cancel all
        int responseCode = showOptionDialog(
                null,
                message,
                "Add task",
                DEFAULT_OPTION,
                INFORMATION_MESSAGE,
                null, options, options[0]);
        return responseCode;
    }

    private static void resetFields(){
        taskNameField.setText("");
        taskDescriptionField.setText("");
        developerField.setText(providedUser.getFirstName()+" "+providedUser.getLastName());
        durationField.setText("");
    }
}