package src;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {

    Logger logger = Logger.getLogger(User.class.getName());
    Pattern PATTERN_CAPITAL = Pattern.compile("[A-Z]");
    Pattern PATTERN_NUMBER = Pattern.compile("[0-9]");
    Pattern PATTERN_SPECIAL = Pattern.compile("[^a-zA-Z\\d\\s:]");

    private String userName;
    private String password;
    private String firstName = null;
    private String lastName = null;

    public User(String userName, String password, String firstName, String lastName) { //TODO: is this constructor necessary?
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
        //TODO: see above
    }

    public String getUserName() {//TODO: Username changing/viewing?
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() { //TODO: Is this necessary?
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    @Override
    public String toString() {
        return "src.User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    boolean checkUserName(){ //checks if the username is valid
        boolean lessThan5 = this.userName.length()<=5;
        boolean containsUnderscore = this.userName.contains("_");
        return lessThan5 && containsUnderscore;
    }
    boolean checkPasswordComplexity(){ //checks if the password is valid
        Matcher matcherCapital = PATTERN_CAPITAL.matcher(this.password);
        Matcher matcherNumber = PATTERN_NUMBER.matcher(this.password);
        Matcher matcherSpecial = PATTERN_SPECIAL.matcher(this.password);
        boolean moreThan8 = this.password.length()>=8;
        boolean containsCapital = matcherCapital.find();
        boolean containsNumber = matcherNumber.find();
        boolean containsSpecial = matcherSpecial.find();
        logger.info("Password complexity: "+moreThan8+" "+containsCapital+" "+containsNumber+" "+containsSpecial);
        return moreThan8 && containsCapital && containsNumber && containsSpecial;
    }
}
