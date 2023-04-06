package src;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        boolean endProgram = false;
        boolean continueFlag=false;
        String[] options = {"Login", "Register", "Exit"};
        User[] allUsers = new User[0];
        while (!endProgram) {
            int selected = JOptionPane.showOptionDialog(
                    null,
                    "Please select an option",
                    "Login/Register",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);
            switch (selected) {
                case 0:
                    Login login = new Login();
                    User user = login.login();
                    for (User allUser : allUsers) {
                        if (allUser.equals(user)) {
                            JOptionPane.showMessageDialog(null, "Welcome " + allUser.getFirstName() + " " + allUser.getLastName() + " it is great to see you again.");
                            continueFlag = true;
                            break;
                        }
                    }
                    break;
                case 1:
                    Login register = new Login();
                    int registerCode = register.register();
                    System.out.println(register.getUser().toString());
                    break;
                case 2:
                    endProgram = true;
                    break;
            }
            if(continueFlag){
                continueFlag=false;
                continue;
            }
        }
    }
    public static User[] addUser(User[] allUsers, User newUser) {
        User[] newAllUsers = new User[allUsers.length + 1];
        System.arraycopy(allUsers, 0, newAllUsers, 0, allUsers.length);
        newAllUsers[newAllUsers.length - 1] = newUser;
        return newAllUsers;
    }
}