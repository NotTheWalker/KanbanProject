package src.main;

import javax.swing.*;
import java.util.logging.Logger;

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        boolean endProgram = false;
        boolean continueFlag=false;
        String[] options = {"Login", "Register", "Exit"};
        User[] allUsers = new User[0];
        User currentUser;
        while (!endProgram) {
            int selected = JOptionPane.showOptionDialog(
                    null,
                    "Please select an option",
                    "Login/Register",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);
            switch (selected) {
                case 0 -> {
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
                case 1 -> {
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
                case 2 -> endProgram = true;
            }
            if(continueFlag){
                continueFlag=false;
                continue;
            }
        }
        //TODO: KANBAN BOARD
        //TODO: ASSOCIATE USERS WITH TASKS
    }
    public static User[] addUser(User[] allUsers, User newUser) {
        User[] newAllUsers = new User[allUsers.length + 1];
        System.arraycopy(allUsers, 0, newAllUsers, 0, allUsers.length);
        newAllUsers[newAllUsers.length - 1] = newUser;
        return newAllUsers;
    }
}