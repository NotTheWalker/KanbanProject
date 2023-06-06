import javax.swing.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.*;

public class LoginClass {

    //@ValueSource(strings = {"wsoo_", "inep_", "mcar_", "hnak_", "eros_"})
    //@ValueSource(strings = {"caleb", "jacques", "ethan", "kayla", "chad"})
    Logger logger = Logger.getLogger(LoginClass.class.getName());
    Pattern PATTERN_CAPITAL = Pattern.compile("[A-Z]");
    Pattern PATTERN_NUMBER = Pattern.compile("[0-9]");
    Pattern PATTERN_SPECIAL = Pattern.compile("[^a-zA-Z\\d\\s:]");

    private final String[] SUCCESS_OPTIONS = {"Continue", "Exit"};
    private final String[] FAILURE_OPTIONS = {"Try Again", "Exit"};
    private UserClass userClass;
    private String response;

    public UserClass getUser() {
        return userClass;
    }

    public LoginClass() {
    }

    public int loginUser(UserClass[] allUserClasses) {
        boolean goodLogin = false;

        UserClass providedUserClass = new UserClass("foo", "bar");
        UserClass referenceUserClass = new UserClass("foo", "bar");

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

            if(loginCode==2) {return loginCode;}

            providedUserClass.setUserName(userField.getText());
            providedUserClass.setPassword(new String(passwordField.getPassword()));
            this.userClass = providedUserClass;

            for(UserClass u: allUserClasses){
                logger.info(u.toString());
                if(loginUser(this.userClass, u)) {
                    referenceUserClass = u;
                    goodLogin = true;
                    break;
                }
            }
            response = returnLoginStatus(userClass, referenceUserClass);

            if (goodLogin) {
                showMessageDialog(null, response, "Success", PLAIN_MESSAGE);
                return 0;
            } else {
                int failureCode = showOptionDialog(
                        null, response, "Failure",
                        YES_NO_OPTION, PLAIN_MESSAGE,
                        null, FAILURE_OPTIONS, FAILURE_OPTIONS[0]);
                logger.info("Failure pane: "+failureCode);
                if (failureCode == 2) {
                    return failureCode;
                }
            }
        }
    }

    public int registerUser() {
        UserClass providedUserClass = new UserClass("foo", "bar");
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

            if(registerCode==2) {return registerCode;}

            providedUserClass.setUserName(userField.getText());
            providedUserClass.setPassword(new String(passwordField.getPassword()));
            response = returnRegistrationStatus(providedUserClass);

            if(checkUserName(providedUserClass.getUserName()) && checkPasswordComplexity(providedUserClass.getPassword())) {
                Object[] detailsMessage = {
                        detailsPrompt, "First name:", firstNameField,
                        "Last name:", lastNameField
                };
                registerCode = showOptionDialog(
                        null, detailsMessage, "Register",
                        OK_CANCEL_OPTION, PLAIN_MESSAGE,
                        null, null, null);
                logger.info("Details pane: "+registerCode);
                if(registerCode==0) {
                    providedUserClass.setFirstName(firstNameField.getText());
                    providedUserClass.setLastName(lastNameField.getText());
                    this.userClass = providedUserClass;
                } else {
                    return registerCode;
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


    public String returnRegistrationStatus(UserClass userClass) {
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

    public boolean loginUser(UserClass providedUserClass, UserClass referenceUserClass) { //TODO: Review if this API is necessary
        return providedUserClass.equals(referenceUserClass);
    }

    public String returnLoginStatus(UserClass providedUserClass, UserClass referenceUserClass) {
        if(providedUserClass.equals(referenceUserClass)) {
            return "Welcome " + referenceUserClass.getFirstName() + " " + referenceUserClass.getLastName() + " it is great to see you again.";
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
        return checkUserName(this.userClass.getUserName());
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
        return checkPasswordComplexity(this.userClass.getPassword());
    }
}
