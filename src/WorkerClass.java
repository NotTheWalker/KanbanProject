import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static javax.swing.JOptionPane.*;

/**
 * This class is the main class of the program
 * It contains a method start() which initializes the control loop
 * It also contains the methods for reading and writing to the XML file
 */
public class WorkerClass {
    static Logger logger = Logger.getLogger(WorkerClass.class.getName());
    public static File USERS_FILE = new File("src/resources/users.xml");
    public static File TASKS_FILE = new File("src/resources/tasks.xml");

    private static UserClass currentUser;
    private static UserClass[] allUsers = new UserClass[0];
    private static TaskClass[] allTasks = new TaskClass[0];

    private static boolean endProgram = false;

    /**
     * This is the starting point of the program
     */
    public static void start() {

        if (USERS_FILE.exists()) { //if the file exists, read from it
            WorkerClass.allUsers = readUsersFromXML();
        }

        if (TASKS_FILE.exists()) { //if the file exists, read from it
            WorkerClass.allTasks = readTasksFromXML();
            TaskClass.setTaskCount(WorkerClass.allTasks.length);
        }

        while (!endProgram) { //initialise control loop
            entry();
            if (endProgram) continue;
            if (!LoginClass.isGoodLogin()) continue;
            userSession();
        }

    }

    /**
     * This method handles the logging in and registering of users
     */
    private static void entry() {
        while (true) {
            int returnOption = menuPrompt(); //0 = login, 1 = register, 2 = exit
            switch (returnOption) {
                case 0 -> { //login
                    LoginClass.login(allUsers);
                    if (!LoginClass.isCancelled() && LoginClass.isGoodLogin()) {
                        currentUser = LoginClass.getUser();
                        logger.info("User logged in");
                        return;
                    } else if (LoginClass.isCancelled()) {
                        logger.info("Login cancelled");
                        return;
                    }
                }
                case 1 -> { //register
                    LoginClass.registerUser();
                    if (!LoginClass.isCancelled() && LoginClass.isGoodRegister()) {
                        addUser();
                        logger.info("User registered");
                    }
                }
                case 2 -> { //exit
                    endProgram = true;
                    logger.info("Program exited");
                    return;
                }
            }
        }
    }

    /**
     * This method displays the login/register menu and returns the user's choice
     *
     * @return an integer representing the user's choice
     */
    private static int menuPrompt() {
        String[] options = {"Login", "Register", "Exit"};
        return showOptionDialog(
                null,
                "Please select an option",
                "Login/Register",
                DEFAULT_OPTION,
                INFORMATION_MESSAGE,
                null, options, options[0]);
    }

    /**
     * This method displays the session menu
     */
    private static void userSession() {
        TaskHandlerClass.setProvidedUser(currentUser);
        String message = "";
        while (true) {
            int sessionOption = sessionPrompt(); //0 = add tasks, 1 = view tasks, 2 = logout
            switch (sessionOption) {
                case 0 -> { //add tasks
                    TaskHandlerClass.build();
                    if (!TaskHandlerClass.isCancelled()) {
                        TaskClass[] newTasks = TaskHandlerClass.getAllTasks();
                        addTasks(newTasks);
                        logger.info("User added tasks");
                        for(TaskClass task : newTasks) {
                            currentUser.addTaskId(task.getTaskID());
                        }
                    }
                }
                case 1 -> { //view tasks
                    message = ""; //clear message
                    //temporary
                    for (TaskClass task : allTasks) {
                        message += task.getTaskDetails();
                        message += "\n";
                        message += "═".repeat(20);
                        message += "\n";
                    }
                    message += "Total hours: " + TaskClass.getTotalHours();
                    logger.info("User viewed tasks");
                    logger.info(message);
                    showMessageDialog(null, message, "Tasks", INFORMATION_MESSAGE);
                }
                case 2 -> { //logout
                    logger.info("User logged out");
                    return;
                }
            }
        }
    }

    /**
     * This method reads all users from the XML file and returns them as an array of UserClass objects
     *
     * @return an integer representing the user's choice
     */
    private static int sessionPrompt() { //returns the option selected
        String[] options = {"Add tasks", "View tasks", "Logout"};
        return showOptionDialog(
                null,
                "Please select an option",
                "Session",
                DEFAULT_OPTION,
                INFORMATION_MESSAGE,
                null, options, options[0]);
    }


    /**
     * This method adds a user to an array of UserClass objects
     *
     * @param allUsers     an array of UserClass objects
     * @param incomingUser a UserClass object
     * @return an array of UserClass objects
     */
    private static UserClass[] addUser(UserClass[] allUsers, UserClass incomingUser) {
        UserClass[] newAllUsers = new UserClass[allUsers.length + 1];
        System.arraycopy(allUsers, 0, newAllUsers, 0, allUsers.length);
        newAllUsers[newAllUsers.length - 1] = incomingUser;
        return newAllUsers;
    }

    /**
     * This method adds a user to the static allUsers array
     */
    private static void addUser() {
        WorkerClass.allUsers = addUser(WorkerClass.allUsers, LoginClass.getUser());
    }

    public static UserClass[] getAllUsers() {
        return allUsers;
    }

    public static void setAllUsers(UserClass[] allUsers) {
        WorkerClass.allUsers = allUsers;
    }

    /**
     * This method adds an array of tasks to an array of TaskClass objects
     *
     * @param allTasks      an array of TaskClass objects
     * @param incomingTasks an array of TaskClass objects
     * @return an array of TaskClass objects
     */
    private static TaskClass[] addTasks(TaskClass[] allTasks, TaskClass[] incomingTasks) {
        TaskClass[] newAllTasks = new TaskClass[allTasks.length + incomingTasks.length];
        System.arraycopy(allTasks, 0, newAllTasks, 0, allTasks.length);
        System.arraycopy(incomingTasks, 0, newAllTasks, allTasks.length, incomingTasks.length);
        return newAllTasks;
    }

    /**
     * This method adds an array of tasks to the static allTasks array
     *
     * @param incomingTasks an array of TaskClass objects
     */
    private static void addTasks(TaskClass[] incomingTasks) {
        WorkerClass.allTasks = addTasks(WorkerClass.allTasks, incomingTasks);
    }

    public static TaskClass[] getAllTasks() {
        return allTasks;
    }

    public static void setAllTasks(TaskClass[] allTasks) {
        WorkerClass.allTasks = allTasks;
    }

    /**
     * This method calls the writeUsersToXML method upon exit
     */
    public static void exit() {
        writeUsersToXML(); //write all users to XML upon exit
        writeTasksToXML(); //write all tasks to XML upon exit
    }

    //Region XML methods

    /**
     * This method writes all users to the XML file
     */
    public static void writeUsersToXML() {
        if (USERS_FILE.exists()) {
            convertToBackupUserFile();
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement = doc.createElement("users");
            doc.appendChild(rootElement);

            for (UserClass u : allUsers) {
                Element user = doc.createElement("user");
                rootElement.appendChild(user);
                user.setAttribute("firstName", u.getFirstName());
                user.setAttribute("lastName", u.getLastName());
                user.setAttribute("password", u.getPassword());
                user.setAttribute("userName", u.getUserName());
                Element tasks = doc.createElement("tasks");
                user.appendChild(tasks);
                ArrayList<String> userTaskIDs = u.getTaskIds();
                for (String taskID : userTaskIDs) {
                    Element task = doc.createElement("task");
                    tasks.appendChild(task);
                    task.setTextContent(taskID);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(USERS_FILE);
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeTasksToXML() {
        if(TASKS_FILE.exists()) {
            convertToBackupTaskFile();
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement = doc.createElement("tasks");
            doc.appendChild(rootElement);

            for(TaskClass task : allTasks) {
                Element taskElement = doc.createElement("task");
                rootElement.appendChild(taskElement);
                taskElement.setAttribute("developerDetails", task.getDeveloperDetails());
                taskElement.setAttribute("taskDescription", task.getTaskDescription());
                taskElement.setAttribute("taskDuration", Integer.toString(task.getTaskDuration()));
                taskElement.setAttribute("taskID", task.getTaskID());
                taskElement.setAttribute("taskName", task.getTaskName());
                taskElement.setAttribute("taskNumber", Integer.toString(task.getTaskNumber()));
                taskElement.setAttribute("taskStatus", task.getTaskStatus().toString());
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(TASKS_FILE);
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method converts the users.xml file to a backup file
     */
    private static void convertToBackupUserFile() {
        File backup = new File("src/resources/users_" + System.currentTimeMillis() + ".xml");
        boolean renameStatus = USERS_FILE.renameTo(backup);
        boolean deleteStatus = USERS_FILE.delete();
        if (renameStatus && deleteStatus) {
            logger.info("Users backup created successfully");
        }
    }

    private  static void convertToBackupTaskFile() {
        File backup = new File("src/resources/tasks_" + System.currentTimeMillis() + ".xml");
        boolean renameStatus = TASKS_FILE.renameTo(backup);
        boolean deleteStatus = TASKS_FILE.delete();
        if (renameStatus && deleteStatus) {
            logger.info("Tasks backup created successfully");
        }
    }

    /**
     * This method reads all users from the XML file and returns them as an array of UserClass objects
     *
     * @return an array of UserClass objects
     */
    public static UserClass[] readUsersFromXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("src/resources/users.xml"));
            return getAllUsersFromXML(doc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        logger.warning("No users found");
        return new UserClass[0];
    }

    public static TaskClass[] readTasksFromXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("src/resources/tasks.xml"));
            return getAllTasksFromXML(doc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        logger.warning("No tasks found");
        return new TaskClass[0];
    }

    /**
     * This method retrieves all users from a Document object and returns them as an array of UserClass objects
     *
     * @param doc the Document object to retrieve users from
     * @return an array of UserClass objects
     */
    private static UserClass[] getAllUsersFromXML(Document doc) {
        Element rootElement = doc.getDocumentElement();
        NodeList usersList = rootElement.getChildNodes();
        UserClass[] allUsers = new UserClass[usersList.getLength()];
        for (int i = 0; i < usersList.getLength(); i++) {
            Node userNode = usersList.item(i);
            if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                Element userElement = (Element) userNode;
                String firstName = userElement.getAttribute("firstName");
                String lastName = userElement.getAttribute("lastName");
                String password = userElement.getAttribute("password");
                String userName = userElement.getAttribute("userName");
                Node tasksNode = userElement.getChildNodes().item(0);
                NodeList tasksList = tasksNode.getChildNodes();
                ArrayList<String> userTaskIDs = new ArrayList<>();
                for (int j = 0; j < tasksList.getLength(); j++) {
                    Node taskNode = tasksList.item(j);
                    if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element taskElement = (Element) taskNode;
                        userTaskIDs.add(taskElement.getTextContent());
                    }
                }
                allUsers[i] = new UserClass(userName, password, firstName, lastName, userTaskIDs);
            }
        }
        return allUsers;
    }

    private static TaskClass[] getAllTasksFromXML(Document doc) {
        Element rootElement = doc.getDocumentElement();
        TaskClass[] allTasks = new TaskClass[rootElement.getChildNodes().getLength()];
        int numberOfTasks = rootElement.getChildNodes().getLength();
        for (int i = 0; i < numberOfTasks; i++) {
            Node taskNode = rootElement.getChildNodes().item(i);
            if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                Element taskElement = (Element) taskNode;
                String developerDetails = taskElement.getAttribute("developerDetails");
                String taskDescription = taskElement.getAttribute("taskDescription");
                int taskDuration = Integer.parseInt(taskElement.getAttribute("taskDuration"));
                String taskID = taskElement.getAttribute("taskID");
                String taskName = taskElement.getAttribute("taskName");
                int taskNumber = Integer.parseInt(taskElement.getAttribute("taskNumber"));
                StatusEnum taskStatus = StatusEnum.valueOf(taskElement.getAttribute("taskStatus"));
                allTasks[i] = new TaskClass(taskName, taskNumber, taskDescription, developerDetails, taskDuration, taskID, taskStatus);
            }
        }
        return allTasks;
    }
}
