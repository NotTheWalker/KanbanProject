import javax.swing.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.*;

public class LoginClass {

    static Logger logger = Logger.getLogger(LoginClass.class.getName());

    private static final String[] SUCCESS_OPTIONS = {"Continue", "Exit"};
    private static final String[] FAILURE_OPTIONS = {"Try Again", "Exit"};

    private static JTextField userField = new JTextField();
    private static JPasswordField passwordField = new JPasswordField();
    private static JTextField firstNameField = new JTextField();
    private static JTextField lastNameField = new JTextField();
    private static UserClass providedUser;

    public static boolean isGoodLogin() {
        return goodLogin;
    }

    public static boolean isGoodRegister() {
        return goodRegister;
    }

    private static boolean goodLogin = false;
    private static boolean goodRegister = false;
    private static boolean cancelOperation = false;
    private static String response;

    public static UserClass getUser() {
        return providedUser;
    }

    public static boolean isCancelled() {
        return cancelOperation;
    }

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

            providedUser = new UserClass(userField.getText(), new String(passwordField.getPassword()));

            for(UserClass referenceUser: allUserClasses){
                if(loginUser(providedUser, referenceUser)) {
                    providedUser.setFirstName(referenceUser.getFirstName());
                    providedUser.setLastName(referenceUser.getLastName());
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

    private static int loginFailure() {
        int responseCode = showOptionDialog(
                null, response, "Failure",
                YES_NO_OPTION, PLAIN_MESSAGE,
                null, FAILURE_OPTIONS, FAILURE_OPTIONS[0]);
        return responseCode;
    }

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

            if(checkUserName() && checkPasswordComplexity()) {
                goodRegister = true;

                if (registerDetailsPrompt() != 0) {
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

    private static int registerFailed() {
        int responseCode = showOptionDialog(
                null, response, "Registration Failed",
                DEFAULT_OPTION, QUESTION_MESSAGE,
                null, FAILURE_OPTIONS, FAILURE_OPTIONS[0]);
        return responseCode;
    }


    public static String returnRegistrationStatus(UserClass userClass) {
        boolean goodUsername = checkUserName(userClass.getUserName());
        boolean goodPassword = checkPasswordComplexity(userClass.getPassword());
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

    public static boolean loginUser(UserClass providedUserClass, UserClass referenceUserClass) { //TODO: Review if this API is necessary
        return providedUserClass.equals(referenceUserClass);
    }

    public static String returnLoginStatus() {
        if(goodLogin) {
            return "Welcome " + providedUser.getFirstName() + " " + providedUser.getLastName() + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again";
        }
    }

    public static boolean checkUserName(String userName){ //checks if the username is valid
        if(userName==null) {return false;}
        boolean lessThan5 = userName.length()<=5;
        boolean containsUnderscore = userName.contains("_");
        return lessThan5 && containsUnderscore;
    }

    public static boolean checkUserName() {
        return checkUserName(providedUser.getUserName());
    }

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

    public static boolean checkPasswordComplexity() {
        return checkPasswordComplexity(providedUser.getPassword());
    }
}
