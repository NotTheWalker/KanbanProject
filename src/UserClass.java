import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * This is the user object class
 */
public class UserClass {

    Logger logger = Logger.getLogger(UserClass.class.getName());

    private String userName;
    private String password;
    private String firstName = null;
    private String lastName = null;
    private ArrayList<String> taskIds = new ArrayList<>();

    /**
     * Constructor for UserClass
     * @param userName a string containing the username
     * @param password a string containing the password
     * @param firstName a string containing the first name
     * @param lastName a string containing the last name
     */
    public UserClass(String userName, String password, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Constructor for UserClass
     * @param userName a string containing the username
     * @param password a string containing the password
     * @param firstName a string containing the first name
     * @param lastName a string containing the last name
     * @param taskIds an array of strings containing the task ids
     */
    public UserClass(String userName, String password, String firstName, String lastName, ArrayList<String> taskIds) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.taskIds = taskIds;
    }

    /**
     * Constructor for UserClass
     * @param userName a string containing the username
     * @param password a string containing the password
     */
    public UserClass(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Constructor for UserClass, makes an empty user
     */
    public UserClass() {

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

    public ArrayList<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(ArrayList<String> taskIds) {
        this.taskIds = taskIds;
    }

    public void addTaskId(String taskId) {
        this.taskIds.add(taskId);
    }

    public TaskClass[] getTasksByID() {
        TaskClass[] tasks = new TaskClass[taskIds.size()];
        for (int i = 0; i < tasks.length; i++) {
            for(TaskClass task : WorkerClass.getAllTasks()) {
                if(task.getTaskID().equals(taskIds.get(i))) {
                    tasks[i] = task;
                    break;
                }
            }
        }
        return tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClass userClass = (UserClass) o;
        return Objects.equals(userName, userClass.userName) && Objects.equals(password, userClass.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    @Override
    public String toString() {
        return "src.main.User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
