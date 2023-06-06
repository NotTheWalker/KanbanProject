import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
    public static File USERS_FILE = new File("src/main/resources/users.xml");

    private static UserClass[] allUsers = new UserClass[0];
    private static UserClass currentUserClass;
    public static final String[] OPTIONS = {"Login", "Register", "Exit"};
    private static boolean endProgram = false;

    public static void start() {

        if(USERS_FILE.exists()) { //if the file exists, read from it
            allUsers = readUsersFromXML();
        }

        while (!endProgram) { //initialise control loop
            entry();
            if(endProgram) break;
        }
        writeUsersToXML(); //write all users to XML upon exit
        //TODO: KANBAN BOARD
        //TODO: ASSOCIATE USERS WITH TASKS
    }

    private static void entry() {
        while(true) {
            int returnOption = menuPrompt(); //0 = login, 1 = register, 2 = exit
            switch (returnOption) {
                case 0 -> { //login
                    LoginClass.login(allUsers);
                    if (!LoginClass.isCancelled()&&LoginClass.isGoodLogin()) {
                        currentUserClass = LoginClass.getUser();
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
                        allUsers = addUser(allUsers, LoginClass.getUser());
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
        return showOptionDialog(
                null,
                "Please select an option",
                "Login/Register",
                DEFAULT_OPTION,
                INFORMATION_MESSAGE,
                null, OPTIONS, OPTIONS[0]);
    }

    public static String[] allUserDetails(UserClass[] allUserClasses){
        String[] allUserNames = new String[allUserClasses.length];
        for(int i = 0; i< allUserClasses.length; i++){
            allUserNames[i] = allUserClasses[i].getFirstName()+" "+ allUserClasses[i].getLastName();
        }
        return allUserNames;
    }

    public static UserClass[] addUser(UserClass[] allUserClasses, UserClass newUserClass) {
        UserClass[] newAllUserClasses = new UserClass[allUserClasses.length + 1];
        System.arraycopy(allUserClasses, 0, newAllUserClasses, 0, allUserClasses.length);
        newAllUserClasses[newAllUserClasses.length - 1] = newUserClass;
        return newAllUserClasses;
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
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convertToBackup(){
        File backup = new File("src/main/resources/users_"+System.currentTimeMillis()+".xml");
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
            Document doc = builder.parse(USERS_FILE);
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
        for (int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
            Node user = rootElement.getChildNodes().item(i);
            NamedNodeMap userAttributes = user.getAttributes();
            Node userNameNode = userAttributes.getNamedItem("userName");
            String userName = userNameNode.getTextContent();
            Node passwordNode = userAttributes.getNamedItem("password");
            String password = passwordNode.getTextContent();
            Node firstNameNode = userAttributes.getNamedItem("firstName");
            String firstName = firstNameNode.getTextContent();
            Node lastNameNode = userAttributes.getNamedItem("lastName");
            String lastName = lastNameNode.getTextContent();
            allUserClasses[i] = new UserClass(userName, password, firstName, lastName);
        }
        return allUserClasses;
    }
}
