package src.main;

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

        //TESTING AREA
//        User[] testUsers = new User[] {
//                new User("foo", "bar", "testFirstName1", "testLastName1"),
//                new User("bar", "foo", "testFirstName2", "testLastName2")
//        };
//        Task testTask = new Task(1, allUserDetails(testUsers));
//        logger.info(testTask.toString());
//        logger.info("End of test");
        //TESTING AREA FINISHED

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
                case CANCEL_OPTION -> endProgram = true;
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
}