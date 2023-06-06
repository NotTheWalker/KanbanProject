import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LoginClassTest {
    static Logger logger = Logger.getLogger(LoginClassTest.class.getName());

    @ParameterizedTest
    @CsvFileSource(files = "src/testResources/ValidUsernames.csv")
    void checkUserName_withValidUserName(String userName) {
        logger.info("Testing username: "+userName);
        assertTrue(LoginClass.checkUserName(userName));
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src/testResources/InvalidUsernames.csv"})
    void checkUserName_withInvalidUserName(String userName) {
        logger.info("Testing username: "+userName);
        assertFalse(LoginClass.checkUserName(userName));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void checkUserName_withNullOrEmptySource(String username) {
        logger.info("Testing username: null or empty");
        assertFalse(LoginClass.checkUserName(username));
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src/testResources/ValidPasswords.csv"})
    void checkPasswordComplexity_withValidPassword(String password) {
        logger.info("Testing password: "+password);
        assertTrue(LoginClass.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/testResources/InvalidPasswords.csv")
    void checkPasswordComplexity_withInvalidPassword(String password) {
        logger.info("Testing password: "+password);
        assertFalse(LoginClass.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void checkPassword_withNullOrEmptySource(String password) {
        logger.info("Testing password: null or empty");
        assertFalse(LoginClass.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/testResources/EqualUserPairings.csv")
    void loginUser_withEqualPairings(String username1, String password1, String username2, String password2) {
        logger.info("Testing user1: "+username1+" - "+password1+" and user2: "+username2+" - "+password2);
        assertTrue(LoginClass.loginUser(new UserClass(username1, password1), new UserClass(username2, password2)));
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/testResources/UnequalUserPairings.csv")
    void loginUser_withUnequalPairings(String username1, String password1, String username2, String password2) {
        logger.info("Testing user1: "+username1+" - "+password1+" and user2: "+username2+" - "+password2);
        assertFalse(LoginClass.loginUser(new UserClass(username1, password1), new UserClass(username2, password2)));
    }
}