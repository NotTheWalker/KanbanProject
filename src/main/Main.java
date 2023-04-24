package src.main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.*;

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());
    static final int LOGIN_OPTION = 0;
    static final int REGISTER_OPTION = 1;

    public static void main(String[] args) {
        boolean endProgram = false;
        boolean continueFlag=false;
        String[] options = {"Login", "Register", "Exit"};
        User[] allUsers = new User[0];
        User currentUser;
        File usersFile = new File("src/main/resources/users.xml");

        //TESTING AREA
        User testUser1 = new User("foo1_", "Bar@1234", "John", "Doe");
        User testUser2 = new User("foo2_", "Bar@1234", "Jane", "Doe");

        allUsers = addUser(allUsers, testUser1);
        allUsers = addUser(allUsers, testUser2);

        writeUsersToXML(allUsers);
        User[] tempAllUsers = allUsers;
        allUsers = readUsersFromXML(usersFile);
        for(int i=0; i<allUsers.length; i++){
            logger.info(allUsers[i].toString());
            logger.info(tempAllUsers[i].toString());
            assert(allUsers[i].equals(tempAllUsers[i]));
        }

        //END TESTING AREA


        while (!endProgram) {
            int returnOption = showOptionDialog(
                    null,
                    "Please select an option",
                    "Login/Register",
                    DEFAULT_OPTION,
                    INFORMATION_MESSAGE,
                    null, options, options[0]);
            switch (returnOption) {
                case LOGIN_OPTION -> {
                    Login login = new Login();
                    int loginCode = login.loginUser(allUsers);
                    if (loginCode == 0) {
                        logger.info("Login successful");
                    } else {
                        continueFlag = true;
                        logger.info("loginCode: " + loginCode);
                        currentUser = login.getUser();
                    }
                    logger.info(login.getUser().toString());
                }
                case REGISTER_OPTION -> {
                    Login register = new Login();
                    int registerCode = register.registerUser();
                    if (registerCode == 0) {
                        allUsers = addUser(allUsers, register.getUser());
                    } else {
                        continueFlag = true;
                        logger.info("registerCode: " + registerCode);
                    }
                    logger.info(register.getUser().toString());
                }
                case CANCEL_OPTION, CLOSED_OPTION -> endProgram = true;
            }
            if(continueFlag){
                continueFlag=false;
                continue;
            }

        }
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

    public static void writeUsersToXML(User[] allUsers) {
        File file = new File("src/main/resources/users.xml");
        if (file.exists()) {
            file.delete();
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

                Element firstName = doc.createElement("firstName");
                firstName.appendChild(doc.createTextNode(u.getFirstName()));
                user.appendChild(firstName);

                Element lastName = doc.createElement("lastName");
                lastName.appendChild(doc.createTextNode(u.getLastName()));
                user.appendChild(lastName);

                Element userName = doc.createElement("userName");
                userName.appendChild(doc.createTextNode(u.getUserName()));
                user.appendChild(userName);

                Element password = doc.createElement("password");
                password.appendChild(doc.createTextNode(u.getPassword()));
                user.appendChild(password);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User[] readUsersFromXML(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
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
            Element user = (Element) rootElement.getChildNodes().item(i);
            String firstName = user.getElementsByTagName("firstName").item(0).getTextContent();
            String lastName = user.getElementsByTagName("lastName").item(0).getTextContent();
            String userName = user.getElementsByTagName("userName").item(0).getTextContent();
            String password = user.getElementsByTagName("password").item(0).getTextContent();
            allUsers[i] = new User(userName, password, firstName, lastName);
        }
        return allUsers;
    }
}