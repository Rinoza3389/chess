package server;

import java.util.Objects;

public final class ErrorResponse {
    private transient final Integer status;
    private final String message;

    public ErrorResponse(
            Integer status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

    public Integer status() {
        return status;
    }

    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {return true;}
        if (obj == null || obj.getClass() != this.getClass()) {return false;}
        var that = (ErrorResponse) obj;
        return Objects.equals(this.status, that.status) &&
                Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message);
    }

    @Override
    public String toString() {
        return "ErrorResponse[" +
                "status=" + status + ", " +
                "message=" + message + ']';
    }

}
