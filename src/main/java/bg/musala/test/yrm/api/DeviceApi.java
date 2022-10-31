package bg.musala.test.yrm.api;

import bg.musala.test.yrm.exception.CustomException;
import bg.musala.test.yrm.jpa.entity.DeviceEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

/**
 * The interface Device api.
 */
public interface DeviceApi
{
    /**
     * Recover all the entities Device.
     *
     * The one paginated is specified with 'pageNumber' and 'itemsPerPage', if one is specified and
     * other not this he/she took value for defect (pageNumber = 0) (itemsPerPage = 5).
     *
     * @param request      the request
     * @param uid          Filter to recover all the entities with 'uid' similar to the one specified.
     * @param vendor       Filter to recover all the entities with 'vendor' similar to the one specified.
     * @param order        Attribute for which the result will be ordered, if it is prefixed - it will be ordered
     *                     descendingly and if it is prefixed + upwardly, if anything is not prefixed it will be
     *                     ordered upwardly by defect.
     * @param pageNumber   I number of page of the one paginated, the first page is 0.
     * @param itemsPerPage Quantity of elements for page.
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity find(HttpServletRequest request,
                        Long uid,
                        String vendor,
                        String order,
                        Integer pageNumber,
                        Integer itemsPerPage) throws CustomException;

    /**
     * Recover the entity Device with the 'id' specified.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity findById(HttpServletRequest request, long id) throws CustomException;

    /**
     * Ppgrade an entity Device with the 'id' specified.
     *
     * @param request the request
     * @param id      the id
     * @param device  the device
     * @param result  the result
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity update(HttpServletRequest request, long id, DeviceEntity device, BindingResult result)
            throws CustomException;

    /**
     * It establishes the state ('status') 'online' to the Device with the 'id' specified.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity online(HttpServletRequest request, long id) throws CustomException;

    /**
     * It establishes the state ('status') 'offline' to the Device with the 'id' specified.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity offline(HttpServletRequest request, long id) throws CustomException;

    /**
     * It eliminates an entity Device with the 'you go' specified.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity delete(HttpServletRequest request, long id) throws CustomException;
}
