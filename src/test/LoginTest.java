package src.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import src.main.Login;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void loginUser() {
    }

    @ParameterizedTest
    @ValueSource(strings = {"wsoo_", "inep_", "mcar_", "hnak_", "eros_"})
    public void checkUserName_withValidUserName(String userName) {
        Login login = new Login();
        assertTrue(login.checkUserName(userName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"caleb", "jacques", "ethan", "kayla", "chad"})
    public void checkUserName_withInvalidUserName(String userName) {
        Login login = new Login();
        assertFalse(login.checkUserName(userName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"P@ssword1", "FooBar123!", "$peciaLised1", "Adm!n999", "Unlit_Eye3"})
    void checkPasswordComplexity_withValidPassword(String password) {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "foobar123", "$pecialised", "Adm!n9", "UnlitEye3"})
    void checkPasswordComplexity_withInvalidPassword(String password) {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity(password));
    }
}