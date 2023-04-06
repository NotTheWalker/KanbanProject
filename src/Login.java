package src;

import javax.swing.*;
import java.util.Arrays;
import java.util.logging.Logger;

import static javax.swing.JOptionPane.*;

public class Login {

    Logger logger = Logger.getLogger(Login.class.getName());
    private final String[] SUCCESS_OPTIONS = {"Continue", "Exit"};
    private final String[] FAILURE_OPTIONS = {"Try Again", "Exit"};
    private User user;
    private String response;

    public User getUser() {
        return user;
    }

    public Login() {
    }

    public User login() {
        user.setUserName(showInputDialog("Please enter your username"));
        user.setPassword(showInputDialog("Please enter your password"));
        User referenceUser = new User();
        referenceUser.setUserName("test_user");
        referenceUser.setPassword("TestPassword1!");
        if(loginUser(user, referenceUser)) {
            return referenceUser;
        } else {
            return null;
        }
    }

    public int register() {
        User providedUser = new User("foo", "bar");
        int selected = 0;
        JTextField userField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        String registerPrompt = "Please enter your username and password";
        String detailsPrompt = "Please enter your first and last name";
        while (true) {
            Object[] registerMessage = {
                    registerPrompt, "Username:", userField,
                    "Password:", passwordField
            };
            selected = showOptionDialog(
                    null, registerMessage, "Register",
                    OK_CANCEL_OPTION, PLAIN_MESSAGE,
                    null, null, null);
            logger.info("Register pane: "+selected);

            if(selected==2) {break;}

            providedUser.setUserName(userField.getText());
            providedUser.setPassword(Arrays.toString(passwordField.getPassword()));
            this.user = providedUser;
            response = registerResponse(user);

            if(user.checkUserName() && user.checkPasswordComplexity()) {
                Object[] detailsMessage = {
                        detailsPrompt, "First name:", firstNameField,
                        "Last name:", lastNameField
                };
                selected = showOptionDialog(
                        null, detailsMessage, "Register",
                        YES_NO_OPTION, PLAIN_MESSAGE,
                        null, null, null);
                logger.info("Details pane: "+selected);
                if(selected==0) {
                    user.setFirstName(firstNameField.getText());
                    user.setLastName(lastNameField.getText());
                }
                selected = showOptionDialog(
                        null, response, "Registration Successful",
                        DEFAULT_OPTION, QUESTION_MESSAGE,
                        null, SUCCESS_OPTIONS, SUCCESS_OPTIONS[0]);
                logger.info("Success pane: "+selected);
                break;
            } else {
                selected = showOptionDialog(
                        null, response, "Registration Failed",
                        DEFAULT_OPTION, QUESTION_MESSAGE,
                        null, FAILURE_OPTIONS, FAILURE_OPTIONS[0]);
                logger.info("Failure pane: "+selected);
                if(selected==2) {break;}
            }
        }
        return selected;
    }


    public String registerResponse(User user) {
        boolean goodUsername = user.checkUserName();
        boolean goodPassword = user.checkPasswordComplexity();
        String response = "";
        if(goodUsername) {
            response += "Username successfully captured";
        } else {
            response += "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length .";
        }
        response += "\n";
        if(goodPassword) {
            response += "Password successfully captured";
        } else {
            response += "Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        }
        return response;
    }

    public boolean loginUser(User providedUser, User referenceUser) {
        return providedUser.equals(referenceUser);
    }

    public String returnLoginStatus(User providedUser, User referenceUser) {
        if(loginUser(providedUser, referenceUser)) {
            return "Welcome " + referenceUser.getFirstName() + " " + referenceUser.getLastName() + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again";
        }
    }
}
