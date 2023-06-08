public enum StatusEnum {
    TO_DO, DOING, DONE;

    public String describe() {
        return switch (this) {
            case TO_DO -> "To Do";
            case DOING -> "Doing";
            case DONE -> "Done";
        };
    }
}