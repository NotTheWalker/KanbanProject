import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class TaskClassTest {

    Logger logger = Logger.getLogger(TaskClassTest.class.getName());

    TaskClass task1 = null;
    TaskClass task2 = null;
    @BeforeEach
    void setUp() {
        task1 = new TaskClass(
                "Login Feature",
                "Create Login to authenticate users",
                "Robyn Harrison",
                8
        );
        task2 = new TaskClass(
                "Add Task Feature",
                "Create Add Task feature to add task users",
                "Mike Smith",
                10
        );
        task2.setTaskStatus(StatusEnum.DOING);
    }

    @AfterEach
    void tearDown() {
        task1 = null;
        task2 = null;
        TaskClass.setTaskCount(0);
    }

    @Test
    void checkTaskID() {
        logger.info("Comparing task IDs: " + task1.getTaskID() + " and LO:1:SON");
        assertEquals("LO:1:SON", task1.getTaskID());
        logger.info("Comparing task IDs: " + task2.getTaskID() + " and AD:2:ITH");
        assertEquals("AD:2:ITH", task2.getTaskID());
    }

    @Test
    void checkTaskDescription() {
        logger.info("Checking task descriptions: " + task1.getTaskDescription());
        assertTrue(task1.checkTaskDescription());
        logger.info("Checking task descriptions: " + task2.getTaskDescription());
        assertTrue(task2.checkTaskDescription());
    }

    @Test
    void getTotalHours() {
        logger.info("Checking total hours: Task 1 = " + task1.getTaskDuration() + " Task 2 = " + task2.getTaskDuration());
        int totalHours = TaskClass.getTotalHours();
        assertEquals(18, totalHours);
    }
}