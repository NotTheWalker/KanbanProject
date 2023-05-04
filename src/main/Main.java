import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
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

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());
    public static User[] allUsers = new User[0];
    public static File USERS_FILE = new File("src/main/resources/users.xml");
    public static final String[] OPTIONS = {"Login", "Register", "Exit"};
    public static void main(String[] args) {
        boolean endProgram = false;
        User currentUser;

        if(USERS_FILE.exists()) { //if the file exists, read from it
            allUsers = readUsersFromXML();
        }

        while (!endProgram) { //initialise control loop
            int returnOption = showOptionDialog(
                    null,
                    "Please select an option",
                    "Login/Register",
                    DEFAULT_OPTION,
                    INFORMATION_MESSAGE,
                    null, OPTIONS, OPTIONS[0]);
            //login/register/exit pane
            switch (returnOption) {
                case 0 -> { //login
                    Login login = new Login();
                    int loginCode = login.loginUser(allUsers);
                    if (loginCode == 0) {
                        logger.info("Login successful");
                        currentUser = login.getUser();
                    } else {
                        logger.info("loginCode: " + loginCode);
                    }
                }
                case 1 -> { //register
                    Login register = new Login();
                    int registerCode = register.registerUser();
                    if (registerCode == 2) {
                        logger.info("registerCode: " + registerCode);
                    } else {
                        allUsers = addUser(allUsers, register.getUser());
                    }
                }
                case 2 -> endProgram = true; //exit
            }
        }
        writeUsersToXML(); //write all users to XML upon exit
        //TODO: KANBAN BOARD
        //TODO: ASSOCIATE USERS WITH TASKS
    }

    public static String[] allUserDetails(User[] allUsers){
        String[] allUserNames = new String[allUsers.length];
        for(int i=0; i<allUsers.length; i++){
            allUserNames[i] = allUsers[i].getFirstName()+" "+allUsers[i].getLastName();
        }
        return allUserNames;
    }

    public static User[] addUser(User[] allUsers, User newUser) {
        User[] newAllUsers = new User[allUsers.length + 1];
        System.arraycopy(allUsers, 0, newAllUsers, 0, allUsers.length);
        newAllUsers[newAllUsers.length - 1] = newUser;
        return newAllUsers;
    }

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

            for (User u : allUsers) {
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
        boolean renameStatus = Main.USERS_FILE.renameTo(backup);
        boolean deleteStatus = Main.USERS_FILE.delete();
        if(renameStatus && deleteStatus) {
            logger.info("Backup created successfully");
        }
    }

    public static User[] readUsersFromXML() {
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
        return new User[0];
    }

    private static User[] getAllUsers(Document doc) {
        Element rootElement = doc.getDocumentElement();
        User[] allUsers = new User[rootElement.getChildNodes().getLength()];
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
            allUsers[i] = new User(userName, password, firstName, lastName);
        }
        return allUsers;
    }

}