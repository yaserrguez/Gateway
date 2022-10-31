package bg.musala.test.yrm.api;

import bg.musala.test.yrm.exception.CustomException;
import bg.musala.test.yrm.jpa.entity.DeviceEntity;
import bg.musala.test.yrm.jpa.entity.GatewayEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * The interface Gatewey api.
 */
public interface GateweyApi
{
    /**
     * Recover all the entities Gatewey.
     *
     * The one paginated is specified with 'pageNumber' and 'itemsPerPage', if one is specified and other not
     * this he/she took value for defect (pageNumber = 0) (itemsPerPage = 5).
     *
     * @param request      the request
     * @param serialNumber Filter to recover all the entities with 'serialNumber' similar to the one specified.
     * @param name         Filter to recover all the entities with 'nama' similar to the one specified.
     * @param ipv4Address  Filter to recover all the entities with 'ipv4Address' similar to the one specified.
     * @param order        Attribute for which the result will be ordered, if it is prefixed - it will be ordered
     *                     descendingly and if it is prefixed + upwardly, if anything is not prefixed it will
     *                     be ordered upwardly by defect.
     * @param pageNumber   I number of page of the one paginated, the first page is 0.
     * @param itemsPerPage Quantity of elements for page.
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity find(HttpServletRequest request,
                        String serialNumber,
                        String name,
                        String ipv4Address,
                        String order,
                        Integer pageNumber,
                        Integer itemsPerPage) throws CustomException;

    /**
     * Recover the entity Gatewey with the 'id' specified.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity findById(HttpServletRequest request, long id) throws CustomException;

    /**
     * Recover the Device related with the Gatewey with the 'id' specified.
     *
     * @param request the request
     * @param id      the id
     * @return the devices
     * @throws CustomException the custom exception
     */
    ResponseEntity getDevices(HttpServletRequest request, long id) throws CustomException;

    /**
     * Relate (to create) a Device with the Gatewey with the 'id' specified.
     *
     * @param request the request
     * @param id      the id
     * @param device  the device
     * @param result  the result
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity addDevices(HttpServletRequest request, long id, DeviceEntity device, BindingResult result)
            throws CustomException;

    /**
     * Relate (to create) several Device with the Gatewey with the 'you go' specified.
     *
     * @param request    the request
     * @param id         the id
     * @param deviceList the device list
     * @param result     the result
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity addDevices(HttpServletRequest request, long id, List<DeviceEntity> deviceList, BindingResult result)
            throws CustomException;

    /**
     * Create an entity Gatewey.
     *
     * @param request the request
     * @param gateway the gateway
     * @param result  the result
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity create(HttpServletRequest request, GatewayEntity gateway, BindingResult result)
            throws CustomException;

    /**
     * Upgrade an entity Gatewey with the 'id' specified.
     *
     * @param request the request
     * @param id      the id
     * @param gateway the gateway
     * @param result  the result
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity update(HttpServletRequest request, long id, GatewayEntity gateway, BindingResult result)
            throws CustomException;

    /**
     * Delete an entity Gatewey with the 'id' specified, also eliminating the Device related with this.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     * @throws CustomException the custom exception
     */
    ResponseEntity delete(HttpServletRequest request, long id) throws CustomException;
}
