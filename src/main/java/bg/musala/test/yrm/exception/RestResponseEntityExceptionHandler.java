package bg.musala.test.yrm.exception;

import bg.musala.test.yrm.component.ErrorResponse;
import bg.musala.test.yrm.component.ResponseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.InvocationTargetException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    private static final Log LOGGER = LogFactory.getLog(RestResponseEntityExceptionHandler.class);

    @Autowired
    @Qualifier("responseUtil")
    protected ResponseUtil responseUtil;

    /**
     * Catch all for any other exceptions...
     */
    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public ResponseEntity<?> handleAnyException(Exception e)
    {
        return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle failures commonly thrown from code
     */
    @ExceptionHandler({ InvocationTargetException.class,
            IllegalArgumentException.class,
            ClassCastException.class,
            ConversionFailedException.class })
    @ResponseBody
    public ResponseEntity handleMiscFailures(Throwable t)
    {
        return errorResponse(t, HttpStatus.BAD_REQUEST);
    }

    /**
     * Send a 409 Conflict in case of concurrent modification
     */
    @ExceptionHandler({ ObjectOptimisticLockingFailureException.class,
                        OptimisticLockingFailureException.class})
    @ResponseBody
    public ResponseEntity handleConflictOptimisticLocking(Exception ex)
    {
        return errorResponse(ex, HttpStatus.CONFLICT, "OptimisticLockingFailureException");
    }

    /**
     * Send a 409 Conflict in case of concurrent modification
     */
    @ExceptionHandler({ DataIntegrityViolationException.class })
    @ResponseBody
    public ResponseEntity handleConflictDataIntegrity(Exception ex)
    {
        return errorResponse(ex, HttpStatus.CONFLICT, "DataIntegrityViolationException");
    }

    @ExceptionHandler({ CustomException.class })
    @ResponseBody
    public ResponseEntity handleCustomException(CustomException ex)
    {
        return errorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    protected ResponseEntity errorResponse(Throwable throwable, HttpStatus status, String message)
    {
        if (null != throwable)
        {
            LOGGER.error("error caught: " + throwable.getMessage(), throwable);
            return ResponseEntity.status(status).body(new ErrorResponse(status, message, throwable.getMessage()));
        }
        else
        {
            LOGGER.error("unknown error caught in RESTController, {} " + status);
            return ResponseEntity.status(status).body(new ErrorResponse(status, "Unknown error caught in RESTController, {} "));
        }
    }

    protected ResponseEntity errorResponse(Throwable throwable, HttpStatus status)
    {
        if (null != throwable)
        {
            LOGGER.error("error caught: " + throwable.getMessage(), throwable);
            return ResponseEntity.status(status).body(new ErrorResponse(status, throwable.getMessage()));
        }
        else
        {
            LOGGER.error("unknown error caught in RESTController, {} " + status);
            return ResponseEntity.status(status).body(new ErrorResponse(status, "Unknown error caught in RESTController, {} "));
        }
    }

    protected ResponseEntity<Exception> errorResponse(Throwable throwable)
    {
        if (null != throwable)
        {
            LOGGER.error("error caught: " + throwable.getMessage(), throwable);
            return this.responseUtil.internalErrorResponse((Exception) throwable);
        }
        else
        {
            LOGGER.error("unknown error caught in RESTController, {} ");
            return this.responseUtil.internalErrorResponse((Exception) throwable);
        }
    }

    protected <T> ResponseEntity<T> response(T body, HttpStatus status)
    {
        LOGGER.debug("Responding with a status of {}" + status);
        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }
}
