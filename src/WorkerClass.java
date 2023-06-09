import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
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
import java.util.logging.Logger;

import static javax.swing.JOptionPane.*;

public class WorkerClass {
    static Logger logger = Logger.getLogger(WorkerClass.class.getName());
    public static File USERS_FILE = new File("src/resources/users.xml");

    private static UserClass[] allUsers = new UserClass[0];
    private static UserClass currentUser;
    private static TaskClass[] allTasks = new TaskClass[0];

    private static boolean endProgram = false;

    public static void start() {

        if(USERS_FILE.exists()) { //if the file exists, read from it
            WorkerClass.allUsers = readUsersFromXML();
        }

        while (!endProgram) { //initialise control loop
            entry();
            if(endProgram) continue;
            if(!LoginClass.isGoodLogin()) continue;
            userSession();
        }

    }

    private static void entry() {
        while(true) {
            int returnOption = menuPrompt(); //0 = login, 1 = register, 2 = exit
            switch (returnOption) {
                case 0 -> { //login
                    LoginClass.login(allUsers);
                    if (!LoginClass.isCancelled()&&LoginClass.isGoodLogin()) {
                        currentUser = LoginClass.getUser();
                        logger.info("User logged in");
                        return;
                    } else if(LoginClass.isCancelled()) {
                        logger.info("Login cancelled");
                        return;
                    }
                }
                case 1 -> { //register
                    LoginClass.registerUser();
                    if (!LoginClass.isCancelled()) {
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

    private static void userSession() {
        TaskHandlerClass.setProvidedUser(currentUser);
        String message = "";
        while (true) {
            int sessionOption = sessionPrompt(); //0 = add tasks, 1 = view tasks, 2 = logout
            switch (sessionOption) {
                case 0 -> { //add tasks
                    TaskHandlerClass.build();
                    if(!TaskHandlerClass.isCancelled()) {
                        addTasks(TaskHandlerClass.getAllTasks());
                    }
                }
                case 1 -> { //view tasks
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

    public static String[] allUserDetails(UserClass[] allUserClasses){
        String[] allUserNames = new String[allUserClasses.length];
        for(int i = 0; i< allUserClasses.length; i++){
            allUserNames[i] = allUserClasses[i].getFirstName()+" "+ allUserClasses[i].getLastName();
        }
        return allUserNames;
    }

    private static UserClass[] addUser(UserClass[] allUsers, UserClass incomingUser) {
        UserClass[] newAllUsers = new UserClass[allUsers.length + 1];
        System.arraycopy(allUsers, 0, newAllUsers, 0, allUsers.length);
        newAllUsers[newAllUsers.length - 1] = incomingUser;
        return newAllUsers;
    }

    private static void addUser() {
        WorkerClass.allUsers = addUser(WorkerClass.allUsers, LoginClass.getUser());
    }

    private static TaskClass[] addTasks(TaskClass[] allTasks, TaskClass[] incomingTasks) {
        TaskClass[] newAllTasks = new TaskClass[allTasks.length + incomingTasks.length];
        System.arraycopy(allTasks, 0, newAllTasks, 0, allTasks.length);
        System.arraycopy(incomingTasks, 0, newAllTasks, allTasks.length, incomingTasks.length);
        return newAllTasks;
    }

    private static void addTasks(TaskClass[] incomingTasks) {
        WorkerClass.allTasks = addTasks(allTasks, incomingTasks);
    }

    public static void exit() {
        writeUsersToXML(); //write all users to XML upon exit
    }

    //Region XML methods
    public static void writeUsersToXML() {
        if (USERS_FILE.exists()) {
            convertToBackup();
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
                user.setAttribute("userName", u.getUserName());
                user.setAttribute("password", u.getPassword());
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(USERS_FILE);
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convertToBackup(){
        File backup = new File("src/resources/users_"+System.currentTimeMillis()+".xml");
        boolean renameStatus = USERS_FILE.renameTo(backup);
        boolean deleteStatus = USERS_FILE.delete();
        if(renameStatus && deleteStatus) {
            logger.info("Backup created successfully");
        }
    }

    public static UserClass[] readUsersFromXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("src/resources/users.xml"));
            return getAllUsers(doc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        logger.warning("No users found");
        return new UserClass[0];
    }

    private static UserClass[] getAllUsers(Document doc) {
        Element rootElement = doc.getDocumentElement();
        UserClass[] allUserClasses = new UserClass[rootElement.getChildNodes().getLength()];
        int numberOfUsers = rootElement.getChildNodes().getLength();
        for (int i = 0; i < numberOfUsers; i++) {
            Node userNode = rootElement.getChildNodes().item(i);
            if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                Element userElement = (Element) userNode;
                String firstName = userElement.getAttribute("firstName");
                String lastName = userElement.getAttribute("lastName");
                String userName = userElement.getAttribute("userName");
                String password = userElement.getAttribute("password");
                allUserClasses[i] = new UserClass(firstName, lastName, userName, password);
            }
        }
        return allUserClasses;
    }
}
