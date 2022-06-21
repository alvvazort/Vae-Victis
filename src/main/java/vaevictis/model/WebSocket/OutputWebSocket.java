package vaevictis.model.WebSocket;

public class OutputWebSocket {

    private String message;
    private String error;
    private boolean isError;

    public OutputWebSocket(final String message, boolean isError) {
        this.isError = isError;
        this.error = isError ? message : "";
        this.message = !isError ? message : ""; 
    }

    public OutputWebSocket(final String message) {
        this.isError = false;
        this.message = message;
        this.error = "";
    }

    public String getError() {
        return this.error;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean getIsError() {
        return this.isError;
    }
}