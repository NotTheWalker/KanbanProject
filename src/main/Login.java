package src.main;

import javax.swing.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.*;

public class Login {
    Logger logger = Logger.getLogger(Login.class.getName());
    Pattern PATTERN_CAPITAL = Pattern.compile("[A-Z]");
    Pattern PATTERN_NUMBER = Pattern.compile("[0-9]");
    Pattern PATTERN_SPECIAL = Pattern.compile("[^a-zA-Z\\d\\s:]");

    private final String[] SUCCESS_OPTIONS = {"Continue", "Exit"};
    private final String[] FAILURE_OPTIONS = {"Try Again", "Exit"};
    private User user;
    private String response;

    public User getUser() {
        return user;
    }

    public Login() {
    }

    public int loginUser(User[] allUsers) {
        boolean goodLogin = false;

        User providedUser = new User("foo", "bar");
        User referenceUser = new User("foo", "bar");

        JTextField userField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        String registerPrompt = "Please enter your username and password";
        while (true) {
            Object[] registerMessage = {
                    registerPrompt, "Username:", userField,
                    "Password:", passwordField
            };
            int loginCode = showOptionDialog(
                    null, registerMessage, "Login",
                    OK_CANCEL_OPTION, PLAIN_MESSAGE,
                    null, null, null);
            logger.info("Login pane: "+loginCode);

            if(loginCode==CANCEL_OPTION) {return loginCode;}

            providedUser.setUserName(userField.getText());
            providedUser.setPassword(new String(passwordField.getPassword()));
            this.user = providedUser;

            for(User u: allUsers){
                logger.info(u.toString());
                if(loginUser(this.user, u)) {
                    referenceUser = u;
                    goodLogin = true;
                    break;
                }
            }
            response = returnLoginStatus(user, referenceUser);

            if (goodLogin) {
                showMessageDialog(null, response, "Success", PLAIN_MESSAGE);
                return OK_OPTION;
            } else {
                int failureCode = showOptionDialog(
                        null, response, "Failure",
                        YES_NO_OPTION, PLAIN_MESSAGE,
                        null, FAILURE_OPTIONS, FAILURE_OPTIONS[0]);
                logger.info("Failure pane: "+failureCode);
                if (failureCode == CANCEL_OPTION || failureCode == NO_OPTION) {
                    return failureCode;
                }
            }
        }
    }

    public int registerUser() {
        User providedUser = new User("foo", "bar");
        int registerCode;

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
            registerCode = showOptionDialog(
                    null, registerMessage, "Register",
                    OK_CANCEL_OPTION, PLAIN_MESSAGE,
                    null, null, null);
            logger.info("Register pane: "+registerCode);

            if(registerCode==2) {break;}

            providedUser.setUserName(userField.getText());
            providedUser.setPassword(new String(passwordField.getPassword()));
            response = returnRegistrationStatus(providedUser);

            if(checkUserName(providedUser.getUserName()) && checkPasswordComplexity(providedUser.getPassword())) {
                Object[] detailsMessage = {
                        detailsPrompt, "First name:", firstNameField,
                        "Last name:", lastNameField
                };
                registerCode = showOptionDialog(
                        null, detailsMessage, "Register",
                        YES_NO_OPTION, PLAIN_MESSAGE,
                        null, null, null);
                logger.info("Details pane: "+registerCode);
                if(registerCode==0) {

                    providedUser.setFirstName(firstNameField.getText());
                    providedUser.setLastName(lastNameField.getText());
                    this.user = providedUser;
                }
                registerCode = showOptionDialog(
                        null, response, "Registration Successful",
                        DEFAULT_OPTION, QUESTION_MESSAGE,
                        null, SUCCESS_OPTIONS, SUCCESS_OPTIONS[0]);
                logger.info("Success pane: "+registerCode);
                break;
            } else {
                registerCode = showOptionDialog(
                        null, response, "Registration Failed",
                        DEFAULT_OPTION, QUESTION_MESSAGE,
                        null, FAILURE_OPTIONS, FAILURE_OPTIONS[0]);
                logger.info("Failure pane: "+registerCode);
                if(registerCode==2) {break;}
            }
        }
        return registerCode;
    }


    public String returnRegistrationStatus(User user) {
        boolean goodUsername = checkUserName();
        boolean goodPassword = checkPasswordComplexity();
        String response = "";
        if(goodUsername) {
            response += "Username successfully captured";
        } else {
            response += """
                    Username is not correctly formatted,\s
                    please ensure that your username contains an underscore and\s
                    is no more than 5 characters in length .""";
        }
        response += "\n";
        if(goodPassword) {
            response += "Password successfully captured";
        } else {
            response += """
                    Password is not correctly formatted,\s
                    please ensure that the password contains at least 8 characters,\s
                    a capital letter, a number and a special character.""";
        }
        return response;
    }

    public boolean loginUser(User providedUser, User referenceUser) { //TODO: Review if this API is necessary
        return providedUser.equals(referenceUser);
    }

    public String returnLoginStatus(User providedUser, User referenceUser) {
        if(providedUser.equals(referenceUser)) {
            return "Welcome " + referenceUser.getFirstName() + " " + referenceUser.getLastName() + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again";
        }
    }

    public boolean checkUserName(String userName){ //checks if the username is valid
        if(userName==null) {return false;}
        boolean lessThan5 = userName.length()<=5;
        boolean containsUnderscore = userName.contains("_");
        logger.info("Username: "+lessThan5+" "+containsUnderscore);
        return lessThan5 && containsUnderscore;
    }
    public boolean checkUserName() {
        return checkUserName(this.user.getUserName());
    }
    public boolean checkPasswordComplexity(String password){ //checks if the password is valid
        if(password==null) {return false;}
        Matcher matcherCapital = PATTERN_CAPITAL.matcher(password);
        Matcher matcherNumber = PATTERN_NUMBER.matcher(password);
        Matcher matcherSpecial = PATTERN_SPECIAL.matcher(password);
        boolean moreThan8 = password.length()>=8;
        boolean containsCapital = matcherCapital.find();
        boolean containsNumber = matcherNumber.find();
        boolean containsSpecial = matcherSpecial.find();
        logger.info("Password complexity: "+moreThan8+" "+containsCapital+" "+containsNumber+" "+containsSpecial);
        return moreThan8 && containsCapital && containsNumber && containsSpecial;
    }
    public boolean checkPasswordComplexity() {
        return checkPasswordComplexity(this.user.getPassword());
    }
}
