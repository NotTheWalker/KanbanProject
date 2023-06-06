package src.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.logging.Logger;


class LoginClassTest {
    Logger logger = Logger.getLogger(src.test.LoginClassTest.class.getName());

    @ParameterizedTest
    @CsvFileSource(files = "src/testResources/ValidUsernames.csv")
    void checkUserName_withValidUserName(String userName) {
        LoginClass login = new LoginClass();
        logger.info("Testing username: "+userName);
        assertTrue(login.checkUserName(userName));
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src/testResources/InvalidUsernames.csv"})
    void checkUserName_withInvalidUserName(String userName) {
        LoginClass login = new LoginClass();
        logger.info("Testing username: "+userName);
        assertFalse(login.checkUserName(userName));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void checkUserName_withNullOrEmptySource(String username) {
        LoginClass login = new LoginClass();
        logger.info("Testing username: "+username);
        assertFalse(login.checkUserName(username));
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src/testResources/ValidPasswords.csv"})
    void checkPasswordComplexity_withValidPassword(String password) {
        LoginClass login = new LoginClass();
        logger.info("Testing password: "+password);
        assertTrue(login.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/testResources/InvalidPasswords.csv")
    void checkPasswordComplexity_withInvalidPassword(String password) {
        LoginClass login = new LoginClass();
        logger.info("Testing password: "+password);
        assertFalse(login.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void checkPassword_withNullOrEmptySource(String password) {
        LoginClass login = new LoginClass();
        logger.info("Testing password: "+password);
        assertFalse(login.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/testResources/EqualUserPairings.csv")
    void loginUser_withEqualPairings(String username1, String password1, String username2, String password2) {
        LoginClass login = new LoginClass();
        logger.info("Testing user1: "+username1+" - "+password1+" and user2: "+username2+" - "+password2);
        UserClass user1 = new UserClass(username1, password1);
        UserClass user2 = new UserClass(username2, password2);
        assertTrue(login.loginUser(user1, user2));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/testResources/UnequalUserPairings.csv")
    void loginUser_withUnequalPairings(String username1, String password1, String username2, String password2) {
        LoginClass login = new LoginClass();
        logger.info("Testing user1: "+username1+" - "+password1+" and user2: "+username2+" - "+password2);
        UserClass user1 = new UserClass(username1, password1);
        UserClass user2 = new UserClass(username2, password2);
        assertFalse(login.loginUser(user1, user2));
    }
}