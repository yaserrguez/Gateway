package bg.musala.test.yrm.component;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Qualifier("errorResponse")
public class ErrorResponse implements Serializable
{
    private final int code;
    private final String message;
    private final String details;

    /** Constructs a new ErrorResponse. */
    public ErrorResponse()
    {
        this(500, "Error interno");
    }

    public ErrorResponse(String details)
    {
        this(500, "Error interno", details);
    }

    public ErrorResponse(int code, String message)
    {
        this(code, message, null);
    }

    public ErrorResponse(HttpStatus code, String details)
    {
        this(code.value(), code.toString(), details);
    }

    public ErrorResponse(HttpStatus code, String message, String details)
    {
        this(code.value(), message, details);
    }

    public ErrorResponse(HttpStatus code)
    {
        this(code.value(), code.toString(), null);
    }

    public ErrorResponse(int code, String message, String details)
    {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    /**
     * Getter for property 'code'.
     *
     * @return Value for property 'code'.
     */
    public Integer getCode()
    {
        return this.code;
    }

    /**
     * Getter for property 'message'.
     *
     * @return Value for property 'message'.
     */
    public String getMessage()
    {
        return this.message;
    }

    /**
     * Getter for property 'details'.
     *
     * @return Value for property 'details'.
     */
    public String getDetails()
    {
        return this.details;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ErrorResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
