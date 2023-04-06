import java.util.regex.*;

public class Login {


    public boolean checkUserName(User user) {
        boolean lessThan5 = user.getUserName().length()<=5;
        boolean containsUnderscore = user.getUserName().contains("_");
        return lessThan5 && containsUnderscore;
    }
    public boolean checkPasswordComplexity(User user) {

        Pattern patternCapital = Pattern.compile("[A-Z]");
        Matcher matcherCapital = patternCapital.matcher(user.getPassword());

        Pattern patternNumber = Pattern.compile("[0-9]");
        Matcher matcherNumber = patternNumber.matcher(user.getPassword());

        Pattern patternSpecial = Pattern.compile("[^a-zA-Z\\d\\s:]");
        Matcher matcherSpecial = patternSpecial.matcher(user.getPassword());

        boolean moreThan8 = user.getPassword().length()>=8;
        boolean containsCapital = matcherCapital.find();
        boolean containsNumber = matcherNumber.find();
        boolean containsSpecial = matcherSpecial.find();
        return moreThan8 && containsCapital && containsNumber && containsSpecial;
    }

    public String registerUser(User user) {
        boolean goodUsername = checkUserName(user);
        boolean goodPassword = checkPasswordComplexity(user);
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
