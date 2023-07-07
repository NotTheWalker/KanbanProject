import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DisplayClass KanbanBoard = new DisplayClass();
            KanbanBoard.setVisible(true);
        });
//        WorkerClass.start();
//        WorkerClass.exit();
    }

}