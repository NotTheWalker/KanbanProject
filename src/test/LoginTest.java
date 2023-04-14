package src.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import src.main.Login;
import src.main.User;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {
    Logger logger = Logger.getLogger(LoginTest.class.getName());

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/ValidUsernames.csv")
    void checkUserName_withValidUserName(String userName) {
        Login login = new Login();
        logger.info("Testing username: "+userName);
        assertTrue(login.checkUserName(userName));
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src/test/resources/InvalidUsernames.csv"})
    void checkUserName_withInvalidUserName(String userName) {
        Login login = new Login();
        logger.info("Testing username: "+userName);
        assertFalse(login.checkUserName(userName));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void checkUserName_withNullOrEmptySource(String username) {
        Login login = new Login();
        logger.info("Testing username: "+username);
        assertFalse(login.checkUserName(username));
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src/test/resources/ValidPasswords.csv"})
    void checkPasswordComplexity_withValidPassword(String password) {
        Login login = new Login();
        logger.info("Testing password: "+password);
        assertTrue(login.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/InvalidPasswords.csv")
    void checkPasswordComplexity_withInvalidPassword(String password) {
        Login login = new Login();
        logger.info("Testing password: "+password);
        assertFalse(login.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void checkPassword_withNullOrEmptySource(String password) {
        Login login = new Login();
        logger.info("Testing password: "+password);
        assertFalse(login.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/EqualUserPairings.csv")
    void loginUser_withEqualPairings(String username1, String password1, String username2, String password2) {
        Login login = new Login();
        logger.info("Testing user1: "+username1+" - "+password1+" and user2: "+username2+" - "+password2);
        User user1 = new User(username1, password1);
        User user2 = new User(username2, password2);
        assertTrue(login.loginUser(user1, user2));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/UnequalUserPairings.csv")
    void loginUser_withUnequalPairings(String username1, String password1, String username2, String password2) {
        Login login = new Login();
        logger.info("Testing user1: "+username1+" - "+password1+" and user2: "+username2+" - "+password2);
        User user1 = new User(username1, password1);
        User user2 = new User(username2, password2);
        assertFalse(login.loginUser(user1, user2));
    }
}