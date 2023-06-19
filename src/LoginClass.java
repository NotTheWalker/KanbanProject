import javax.swing.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.*;

/**
 * This class handles the login and registration of users
 */
public class LoginClass {

    static Logger logger = Logger.getLogger(LoginClass.class.getName());

    private static final String[] SUCCESS_OPTIONS = {"Continue", "Exit"};
    private static final String[] FAILURE_OPTIONS = {"Try Again", "Exit"};

    private static JTextField userField = new JTextField();
    private static JPasswordField passwordField = new JPasswordField();
    private static JTextField firstNameField = new JTextField();
    private static JTextField lastNameField = new JTextField();
    private static UserClass providedUser;
    
    private static boolean goodLogin = false;
    private static boolean goodRegister = false;
    private static boolean cancelOperation = false;
    private static String response;

    /**
     * @return the current UserClass object as provided by the user
     */
    public static UserClass getUser() {
        return providedUser;
    }

    /**
     * @return The boolean flag for whether the login was successful
     */
    public static boolean isGoodLogin() {
        return goodLogin;
    }

    /**
     * @return The boolean flag for whether the registration was successful
     */
    public static boolean isGoodRegister() {
        return goodRegister;
    }

    /**
     * @return The boolean flag for whether the login process has been cancelled
     */
    public static boolean isCancelled() {
        return cancelOperation;
    }

    /**
     * This method is the starting point for the login process
     * It prompts the user for their username and password, then loops through the login process
     * @param allUserClasses The array of all users currently known to the program
     */
    public static void login(UserClass[] allUserClasses) {
        logger.info("Login initiated");
        goodLogin = false;
        cancelOperation = false;

        while (true) {
            int loginCode = loginPrompt();

            if(loginCode==2) { //cancel
                cancelOperation = true;
                logger.info("Login cancellation signalled");
                return;
            }
            //creates user object from provided username and password to compare with all users
            providedUser = new UserClass(userField.getText(), new String(passwordField.getPassword()));

            for(UserClass referenceUser: WorkerClass.getAllUsers()){
                if(loginUser(providedUser, referenceUser)) {
                    providedUser.setFirstName(referenceUser.getFirstName());
                    providedUser.setLastName(referenceUser.getLastName());
                    providedUser.setTaskIds(referenceUser.getTaskIds());
                    goodLogin = true;
                    logger.info("Login success signalled");
                    break;
                }
            }
            response = returnLoginStatus();

            if (goodLogin || loginFailure() == 2) { //if login is successful or user chooses to exit
                logger.info("Return to menu signalled");
                return; //return to menu
            }

        }
    }

    /**
     * This method prompts the user for their username and password
     * @return The response from the user
     */
    private static int loginPrompt() {
        userField.setText("");
        passwordField.setText("");
        Object[] loginMessage = {
                "Please enter your username and password",
                "Username:", userField,
                "Password:", passwordField
        };
        int responseCode = showOptionDialog( //0: ok, 2: cancel
                null, loginMessage, "Login",
                OK_CANCEL_OPTION, PLAIN_MESSAGE,
                null, null, null);
        return responseCode;
    }

    /**
     * This method prompts the user for their desired course of action after a failed login
     * @return The response from the user
     */
    private static int loginFailure() {
        int responseCode = showOptionDialog(
                null, response, "Failure",
                YES_NO_OPTION, PLAIN_MESSAGE,
                null, FAILURE_OPTIONS, FAILURE_OPTIONS[0]);
        return responseCode;
    }

    /**
     * This method is the starting point for the registration process
     * It prompts the user for their desired username and password, then loops through the registration process
     * If the registration is successful, it prompts the user for their first and last name
     */
    public static void registerUser() {
        logger.info("Registration initiated");
        goodRegister = false;
        cancelOperation = false;

        while (true) {
            int registerCode = registerPrompt();

            if(registerCode==2) { //cancel
                cancelOperation = true;
                logger.info("Registration cancellation signalled");
                return;
            }

            providedUser = new UserClass(userField.getText(), new String(passwordField.getPassword()));
            response = returnRegistrationStatus(providedUser);

            if(checkUserName() && checkPasswordComplexity() && checkUserNameDistinct()) {
                goodRegister = true;
                int registerDetailsCode = registerDetailsPrompt();

                if (registerDetailsCode != 0) {
                    cancelOperation = true;
                    goodRegister = false;
                    logger.info("Registration cancellation signalled at line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
                } else {
                    providedUser.setFirstName(firstNameField.getText());
                    providedUser.setLastName(lastNameField.getText());
                    logger.info("Registration details successfully captured");
                }
                return;
            } else if(registerFailed()==1) {
                cancelOperation = true;
                logger.info("Registration cancellation signalled at line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
                return;
            }
        }
    }

    /**
     * This method prompts the user for their desired username and password
     * @return The response from the user
     */
    private static int registerPrompt() {
        String registerPrompt = "Please enter your username and password";
        Object[] registerMessage = {
                registerPrompt, "Username:", userField,
                "Password:", passwordField
        };
        int responseCode = showOptionDialog(
                null, registerMessage, "Register",
                OK_CANCEL_OPTION, PLAIN_MESSAGE,
                null, null, null);
        return responseCode;
    }

    /**
     * This method prompts the user for their first and last name
     * @return The response from the user
     */
    private static int registerDetailsPrompt() {
        String detailsPrompt = "Please enter your first and last name";
        Object[] detailsMessage = {
                detailsPrompt, "First name:", firstNameField,
                "Last name:", lastNameField
        };
        int responseCode = showOptionDialog(
                null, detailsMessage, "Register",
                DEFAULT_OPTION, PLAIN_MESSAGE,
                null, null, null);
        return responseCode;
    }

    /**
     * This method prompts the user for their desired course of action after a failed registration
     * @return The response from the user
     */
    private static int registerFailed() {
        int responseCode = showOptionDialog(
                null, response, "Registration Failed",
                DEFAULT_OPTION, QUESTION_MESSAGE,
                null, FAILURE_OPTIONS, FAILURE_OPTIONS[0]);
        return responseCode;
    }

    /**
     * This method checks the username for correct formatting
     * However, it just creates the String response, it does not return a boolean
     * @param user The user object to be checked
     * @return The response message from the program
     */
    public static String returnRegistrationStatus(UserClass user) {
        boolean goodUsername = checkUserName(user.getUserName());
        boolean goodPassword = checkPasswordComplexity(user.getPassword());
        boolean distinctUsername = checkUserNameDistinct(user.getUserName());
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
        response += "\n";
        if(distinctUsername) {
            response += "Username is distinct";
        } else {
            response += "Username already exists";
        }
        return response;
    }

    /**
     * This method is just a wrapper for the equals method in the UserClass
     * @param providedUser The user object provided by the user
     * @param referenceUser The user object to be compared against
     * @return The result of the comparison
     */
    public static boolean loginUser(UserClass providedUser, UserClass referenceUser) { //TODO: Review if this API is necessary
        return providedUser.equals(referenceUser);
    }

    /**
     * This method checks if the username and password provided by the user match the username and password of the provided user
     * However, it just returns the String response, it does not return a boolean
     * @return The response message from the program, either a welcome message or an error message
     */
    public static String returnLoginStatus() {
        if(goodLogin) {
            return "Welcome " + providedUser.getFirstName() + " " + providedUser.getLastName() + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again";
        }
    }

    /**
     * This method checks a username for correct formatting
     * @param userName The username to be checked
     * @return The result of the check
     */
    public static boolean checkUserName(String userName){ //checks if the username is valid
        if(userName==null) {return false;}
        boolean lessThan5 = userName.length()<=5;
        boolean containsUnderscore = userName.contains("_");
        return lessThan5 && containsUnderscore;
    }

    /**
     * This method checks the class level user's username for correct formatting
     * @return The result of the check
     */
    public static boolean checkUserName() {
        return checkUserName(providedUser.getUserName());
    }

    public static boolean checkUserNameDistinct(String userName) {
        for(UserClass existingUser : WorkerClass.getAllUsers()) {
            if(existingUser.getUserName().equals(userName)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean checkUserNameDistinct() {
        return checkUserNameDistinct(providedUser.getUserName());
    }
    
    /**
     * This method checks a password for correct formatting
     * @param password The password to be checked
     * @return The result of the check
     */
    public static boolean checkPasswordComplexity(String password){ //checks if the password is valid
        if(password==null) {return false;}
        Pattern PATTERN_CAPITAL = Pattern.compile("[A-Z]"); //capital letter regex
        Pattern PATTERN_NUMBER = Pattern.compile("[0-9]"); //number regex
        Pattern PATTERN_SPECIAL = Pattern.compile("[^a-zA-Z\\d\\s:]"); //special character regex
        Matcher matcherCapital = PATTERN_CAPITAL.matcher(password);
        Matcher matcherNumber = PATTERN_NUMBER.matcher(password);
        Matcher matcherSpecial = PATTERN_SPECIAL.matcher(password);
        boolean moreThan8 = password.length()>=8;
        boolean containsCapital = matcherCapital.find();
        boolean containsNumber = matcherNumber.find();
        boolean containsSpecial = matcherSpecial.find();
        return moreThan8 && containsCapital && containsNumber && containsSpecial;
    }

    /**
     * This method checks the class level user's password for correct formatting
     * @return The result of the check
     */
    public static boolean checkPasswordComplexity() {
        return checkPasswordComplexity(providedUser.getPassword());
    }
}
