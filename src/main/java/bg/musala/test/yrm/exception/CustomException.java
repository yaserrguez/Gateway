package bg.musala.test.yrm.exception;

import org.apache.commons.logging.Log;

public class CustomException extends Exception
{
    private static final long serialVersionUID = -8460356990632230194L;
    private final int errorCode;

    /** Constructs a new CustomException. */
    public CustomException()
    {
        super("Error desconocido");
        errorCode = 0;
    }

    public CustomException(String message)
    {
        super(message);
        errorCode = 500;
    }

    public CustomException(int errorCode)
    {
        super();
        this.errorCode = errorCode;
    }

    public CustomException(String message, int errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String message, Throwable cause, int errorCode)
    {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CustomException(String message, Throwable cause)
    {
        super(message, cause);
        this.errorCode = 500;
    }

    public CustomException(Throwable cause, int errorCode)
    {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Getter for property 'errorCode'.
     *
     * @return Value for property 'errorCode'.
     */
    public int getErrorCode()
    {
        return errorCode;
    }

    public CustomException(Log logger, String method, String message, Throwable cause)
    {
        super(message, cause);
        this.errorCode = 500;
        logger.error(String.format("Method '%s' -  Exception ' %s'", method, message));
    }

    public CustomException(Log logger, String method, String message)
    {
        super(message);
        this.errorCode = 500;
        logger.error(String.format("Method '%s' -  Exception ' %s'", method, message));
    }

    public CustomException(Log logger, String method, String message, int errorCode)
    {
        super(message);
        this.errorCode = errorCode;
        logger.error(String.format("Method '%s' -  Exception ' %s'", method, message));
    }
}