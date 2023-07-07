import javax.swing.*;
import java.awt.*;

public class TaskFormPanel extends JPanel {
    //designed to replace current task addition display method
    private JTextField taskNameField;
    private JTextArea taskDescriptionField; //TODO: Need to make sure this can scroll
    private JTextField developerField;
    private JTextField durationField;
    private JButton submitButton;
    private JButton cancelButton;

    private TaskFormListener formListener;

    public TaskFormPanel() {
        setLayout(new BorderLayout());

        // Create the input fields
        taskNameField = new JTextField(20);
        taskDescriptionField = new JTextArea(5, 20);
        developerField = new JTextField(20);
        durationField = new JTextField(20);

        // Create the submit and cancel buttons
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        // Attach action listeners to the buttons
        submitButton.addActionListener(e -> {
            String taskName = taskNameField.getText();
            String taskDescription = taskDescriptionField.getText();
            String developer = developerField.getText();
            String durationString = durationField.getText();

            // Perform validation and notify the form listener
            if (formListener != null) {
                try {
                    int duration = Integer.parseInt(durationString);
                    TaskClass submittedTask = new TaskClass(taskName, taskDescription, developer, duration);
                    if (isValidTask(submittedTask)) {
                        formListener.taskFormSubmitted(submittedTask);
                        clearForm();
                    } else {
                        JOptionPane.showMessageDialog(TaskFormPanel.this, "Invalid task details", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TaskFormPanel.this, "Invalid duration", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> clearForm());

        // Create a form panel and add the components
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Task Name:"));
        formPanel.add(taskNameField);
        formPanel.add(new JLabel("Task Description:"));
        formPanel.add(taskDescriptionField);
        formPanel.add(new JLabel("Developer:"));
        formPanel.add(developerField);
        formPanel.add(new JLabel("Duration:"));
        formPanel.add(durationField);
        this.add(formPanel, BorderLayout.CENTER);
        this.validate();
    }

    private boolean isValidTask(TaskClass task){
        boolean taskName = task.checkTaskName();
        boolean developerDetails = task.checkDeveloperDetails();
        boolean taskDescription = task.checkTaskDescription();
        return taskName && developerDetails && taskDescription;
    }

    private void clearForm() {
        taskNameField.setText("");
        taskDescriptionField.setText("");
        developerField.setText("");
        durationField.setText("");
    }

    public void setFormListener(TaskFormListener listener) {
        this.formListener = listener;
    }

    public String debugForm(){
        return taskNameField.getText() + "\n" + taskDescriptionField.getText() + "\n" + developerField.getText() + "\n" + durationField.getText();
    }

}
