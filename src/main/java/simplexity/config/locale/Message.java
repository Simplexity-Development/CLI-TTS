package simplexity.config.locale;

public enum Message {

    private final String path;
    private String message;
    private Message(String path, String message) {
        this.path = path;
        this.message = message;
    }
    public String getPath() {
        return path;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
