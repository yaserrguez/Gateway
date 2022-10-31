package bg.musala.test.yrm.restController;

import bg.musala.test.yrm.api.DeviceApi;
import bg.musala.test.yrm.businessService.DeviceBusinessService;
import bg.musala.test.yrm.component.ErrorResponse;
import bg.musala.test.yrm.exception.CustomException;
import bg.musala.test.yrm.jpa.model.PageWrapper;
import bg.musala.test.yrm.jpa.entity.DeviceEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Device controller implement rest service to DeviceApi.
 */
@RestController
@RequestMapping(value = "/devices")
public class DeviceRestController implements DeviceApi
{
    private static final Log LOGGER = LogFactory.getLog(DeviceRestController.class);

    @Autowired
    @Qualifier("deviceBusinessService")
    private DeviceBusinessService deviceBusinessService;

    /**
     * It implements hateoas for the entity device
     *
     * @param device the device
     * @return the entity model
     * @throws CustomException the custom exception
     */
    public static EntityModel deviceResource(DeviceEntity device) throws CustomException
    {
        EntityModel<DeviceEntity> resource = EntityModel.of(device);
        Link linkToSelf = WebMvcLinkBuilder.linkTo(methodOn(DeviceRestController.class)
                                                           .findById(null, device.getId()))
                                           .withSelfRel();
        resource.add(linkToSelf);
        Link linkToGatewey = WebMvcLinkBuilder.linkTo(methodOn(GatewayRestController.class)
                                                              .findById(null, device.getDiviceGateway().getId()))
                                              .withRel("gatewey");
        resource.add(linkToGatewey);
        return resource;
    }

    /**
     * It implements hateoas for a list of entities device
     *
     * @param deviceList the device list
     * @return the list
     * @throws CustomException the custom exception
     */
    public static List<EntityModel> deviceResource(List<DeviceEntity> deviceList) throws CustomException
    {
        List<EntityModel> resourceList = new ArrayList<EntityModel>();
        for (DeviceEntity device : deviceList)
        {
            resourceList.add(deviceResource(device));
        }
        return resourceList;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity find(@CurrentSecurityContext HttpServletRequest request,
                               @RequestParam(name = "uid", required = false) Long uid,
                               @RequestParam(name = "vendor", required = false, defaultValue = "") String vendor,
                               @RequestParam(name = "order", required = false) String order,
                               @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                               @RequestParam(name = "itemsPerPage", required = false) Integer itemsPerPage) throws CustomException
    {
        try
        {
            List<String> filterList = new ArrayList<String>();
            if (uid != null)
            {
                filterList.add("uid=" + uid);
            }
            if (vendor != null && !vendor.isEmpty())
            {
                filterList.add("vendor=" + vendor);
            }

            if (pageNumber == null && itemsPerPage == null)
            {
                List<DeviceEntity> entityList = (filterList.isEmpty())? this.deviceBusinessService.findAll(order) :
                                                this.deviceBusinessService.findFilters(filterList, order);
                if (entityList == null || entityList.isEmpty())
                {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(deviceResource(entityList));
            }

            pageNumber = (pageNumber == null) ? 0 : pageNumber; // valores por defecto
            itemsPerPage = (itemsPerPage == null) ? 5 : itemsPerPage; // valores por defecto
            if (pageNumber < 0 || itemsPerPage <= 0)
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "Not paginated been worth."));
            }
            Page<DeviceEntity> entityPage = (filterList.isEmpty())? this.deviceBusinessService.findAll(order, pageNumber, itemsPerPage) :
                                            this.deviceBusinessService.findFilters(filterList, order, pageNumber, itemsPerPage);

            List<EntityModel> entityModelList = deviceResource(entityPage.getContent());
            PageWrapper<EntityModel> entityModelPage = new PageWrapper(entityModelList, entityPage);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(entityModelPage);
        }
        catch (Throwable error)
        {
            LOGGER.error("GET:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@CurrentSecurityContext HttpServletRequest request,
                                   @PathVariable(name = "id") long id) throws CustomException
    {
        try
        {
            DeviceEntity entity = this.deviceBusinessService.findById(id);
            if (entity == null)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                                                             "Not found Device with id " + id));
            }
            return ResponseEntity.ok(deviceResource(entity));
        }
        catch (Throwable error)
        {
            LOGGER.error("GET:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity update(@CurrentSecurityContext HttpServletRequest request,
                                 @PathVariable("id") long id,
                                 @Validated @RequestBody DeviceEntity device, BindingResult result) throws CustomException
    {
        try
        {
            if (result.hasErrors())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, result.getFieldError().getDefaultMessage()));
            }
            if (!deviceBusinessService.existsId(id))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The device id '" + id + "' doesn't exist."));
            }
            DeviceEntity oldDevice = this.deviceBusinessService.findDeviceByUID(device.getUid());
            if (oldDevice != null && oldDevice.getId() != device.getId())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The device UID '" + device.getUid() + "' already exists."));
            }

            return ResponseEntity.ok(deviceResource(deviceBusinessService.save(device)));
        }
        catch (Throwable error)
        {
            LOGGER.error("PUT:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @PutMapping(value = "/{id}/online")
    public ResponseEntity online(@CurrentSecurityContext HttpServletRequest request,
                                 @PathVariable("id") long id)
            throws CustomException
    {
        try
        {
            DeviceEntity device = this.deviceBusinessService.findById(id);
            device.setStatus(true);
            deviceBusinessService.save(device);
            return ResponseEntity.ok().build();
        }
        catch (Throwable error)
        {
            LOGGER.error("PUT:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @PutMapping(value = "/{id}/offline")
    public ResponseEntity offline(@CurrentSecurityContext HttpServletRequest request,
                                 @PathVariable("id") long id) throws CustomException
    {
        try
        {
            DeviceEntity device = this.deviceBusinessService.findById(id);
            device.setStatus(false);
            deviceBusinessService.save(device);
            return ResponseEntity.ok().build();
        }
        catch (Throwable error)
        {
            LOGGER.error("PUT:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@CurrentSecurityContext HttpServletRequest request,
                                 @PathVariable("id") long id) throws CustomException

    {
        try
        {
            if (!deviceBusinessService.existsId(id))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The device id '" + id + "' doesn't exist."));
            }
            deviceBusinessService.delete(id);
            return ResponseEntity.ok().build();
        }
        catch (Throwable error)
        {
            LOGGER.error("DELETE:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }
}
