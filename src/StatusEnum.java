/**
 * Enumerated type for the status of a task.
 */
public enum StatusEnum {
    TO_DO, DOING, DONE;

    /**
     * Returns a string describing the status.
     * @return String describing the status.
     */
    public String describe() {
        return switch (this) {
            case TO_DO -> "To Do";
            case DOING -> "Doing";
            case DONE -> "Done";
        };
    }

    public StatusEnum encode(String status) {
        return switch (status) {
            case "To Do" -> TO_DO;
            case "Doing" -> DOING;
            case "Done" -> DONE;
            default -> null;
        };
    }
}