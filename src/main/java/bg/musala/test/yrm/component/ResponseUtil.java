package bg.musala.test.yrm.component;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Qualifier("responseUtil")
public class ResponseUtil
{
    // === 2xx ======

    public ResponseEntity okResponse(Object entity)
    {
        if (entity == null)
        {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(entity);
    }

    public ResponseEntity okResponse()
    {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity createdResponse(URI location)
    {
        return ResponseEntity.created(location).build();
    }

    public ResponseEntity createdResponse(Object entity)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    public ResponseEntity createdResponse(URI location, Object entity)
    {
        return ResponseEntity.created(location).body(entity);
    }

    public ResponseEntity noContentResponse()
    {
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity partialContentResponse(Object entity)
    {
        if (entity == null)
        {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(entity);
    }

    //=== 3xx ======

    //=== 4xx ======

    public ResponseEntity noData2ProcessResponse()
    {
        return ResponseEntity.unprocessableEntity().body(new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    public ResponseEntity unauthorizedResponse()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED));
    }

    public ResponseEntity unauthorizedTokenResponse(String details)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "El token es inválido",
                                                                                     details));
    }

    /**
     * No tiene suficientes privilegios.
     * <p>
     * Errores del cliente, codigo de retorno 403.
     *
     * @return the response
     */
    public ResponseEntity insufficientPrivilegesResponse()
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(HttpStatus.FORBIDDEN));
    }

    /**
     * No tiene suficientes privilegios.
     * <p>
     * Errores del cliente, codigo de retorno 403.
     *
     * @return the response
     */
    public ResponseEntity insufficientPrivilegesResponse(String details)
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Insuficientes privilegios",
                                                                                  details));
    }

    public ResponseEntity notFoundResponse()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity notFoundResponse(String enity, String field, String value)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND,
                                                                                  String.format("No se a encontrado %s con %s '%s'", enity, field, value)));
    }

    /**
     * No se cumple alguna precondition.
     * <p>
     * Error del cliente, codigo de retorno 412.
     *
     * @param details Detalles de la condicion que falla
     * @return the response
     */
    public ResponseEntity preconditionFailResponse(String details)
    {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, details));
    }
//
//    /**
//     * No se cumple alguna precondition.
//     * <p>
//     * Error del cliente, codigo de retorno 412.
//     *
//     * @return the response
//     */
//    public Response preconditionFailResponse()
//    {
//        return Response.status(Response.Status.PRECONDITION_FAILED)
//                       .entity(new ErrorResponse(Response.Status.PRECONDITION_FAILED.getStatusCode(),
//                                                 Response.Status.PRECONDITION_FAILED.getReasonPhrase())).build();
//    }
//
//    //=== 5xx ======
//
    /**
     * Error interno.
     * <p>
     * Error del servidor, codigo de retorno 500.
     *
     * @param error Error
     * @return the response
     */
    public ResponseEntity internalErrorResponse(Exception error)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage()));
    }

    /**
     * Error interno.
     * <p>
     * Error del servidor, codigo de retorno 500.
     *
     * @return the response
     */
    public ResponseEntity internalErrorResponse()
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR));
    }
//
//    /**
//     * Error interno.
//     * <p>
//     * Error del servidor, codigo de retorno 500.
//     *
//     * @param error Error
//     * @return the response
//     */
//    public Response jsonInternalErrorResponse(@NotNull Exception error)
//    {
//        return Response.serverError().entity(new ErrorResponse(error.toString()).toJson()).build();
//    }
//
//    //=== URL ======
//
//    public java.net.URI getFindByIdUri(@NotNull HttpServletRequest request, @NotNull String findUrlResource)
//    {
//        try
//        {
//            String findByIdStrUri = "";
//
//            String scheme = request.getScheme();
//            String serverName = request.getServerName();
//            int serverPort = request.getServerPort();
//            String contextPath = request.getContextPath();
//
//            findByIdStrUri = scheme + "://" + serverName + ":" + serverPort + contextPath + findUrlResource;
//
//            return new java.net.URI(findByIdStrUri);
//        }
//        catch (Exception e)
//        {
//            return null;
//        }
//    }


}
