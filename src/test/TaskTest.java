package src.test;

import org.junit.jupiter.api.Test;
import src.main.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    private final Task TASK_1 = new Task(
            "Login Feature",
            "Create Login to authenticate users",
            "Robyn Harrison",
            8
    );
    private final Task TASK_2 = new Task(
            "Add Task Feature",
            "Create Add Task feature to add task users",
            "Mike Smith",
            10
    );

    @Test
    void checkTaskDescription_providedTasks() {
        assertTrue(TASK_1.checkTaskDescription());
        assertTrue(TASK_2.checkTaskDescription());
    }


    @Test
    void checkTaskNumber_providedTasks() {
        assertEquals(0, TASK_1.getTaskNumber());
        assertEquals(1, TASK_2.getTaskNumber());
    }

    @Test
    void checkTaskID_providedTasks() {
        assertEquals("LO:0:SON", TASK_1.getTaskID());
        assertEquals("AD:1:ITH", TASK_2.getTaskID());
    }

    @Test
    void checkAccumulatedHours_providedTasks() {
        assertEquals(18, Task.getTotalHours());
    }
}