import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DisplayClass extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JButton addTasksButton;
    private JButton viewTasksButton;
    private JButton logoutButton;

    private TaskFormPanel taskFormPanel;

    public DisplayClass() {
        setTitle("Kanban Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create the main panel
        mainPanel = new JPanel(new BorderLayout());

        // Create the content panel
        contentPanel = new JPanel(new BorderLayout());
        Border panelBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        contentPanel.setBorder(panelBorder);

        // Create the task form panel
        taskFormPanel = new TaskFormPanel();
        taskFormPanel.setFormListener(new TaskFormListener() {
            @Override
            public void taskFormSubmitted(TaskClass task) {
                // Handle the submitted task
                // Must throw error if
            }
        });


        // Create the buttons
        addTasksButton = new JButton("Add Tasks");
        viewTasksButton = new JButton("View Tasks");
        logoutButton = new JButton("Log Out");

        // Set the alignment of buttons to WEST
        addTasksButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewTasksButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Attach action listeners to the buttons
        addTasksButton.addActionListener(e -> {
            // Perform the action when "Add Tasks" button is clicked
            contentPanel.removeAll();
            contentPanel.add(new TaskFormPanel());
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        viewTasksButton.addActionListener(e -> {
            // Perform the action when "View Tasks" button is clicked
            contentPanel.removeAll();
            contentPanel.add(new JLabel("View Tasks Panel"), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        logoutButton.addActionListener(e -> {
            // Perform the action when "Log Out" button is clicked
            dispose();
        });

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add the buttons to the button panel
        buttonPanel.add(addTasksButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(viewTasksButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(logoutButton);

        // Add the button panel and content panel to the main panel
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
    }

}
