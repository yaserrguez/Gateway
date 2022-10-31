package bg.musala.test.yrm.restController;

import bg.musala.test.yrm.api.GateweyApi;
import bg.musala.test.yrm.businessService.DeviceBusinessService;
import bg.musala.test.yrm.businessService.GatewayBusinessService;
import bg.musala.test.yrm.component.ErrorResponse;
import bg.musala.test.yrm.exception.CustomException;
import bg.musala.test.yrm.jpa.model.PageWrapper;
import bg.musala.test.yrm.jpa.entity.DeviceEntity;
import bg.musala.test.yrm.jpa.entity.GatewayEntity;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Gateway controller implement rest service to GatewayApi.
 */
@RestController
@RequestMapping(value = "/gateweys")
public class GatewayRestController implements GateweyApi
{
    private static final Log LOGGER = LogFactory.getLog(GatewayRestController.class);

    @Autowired
    @Qualifier("gatewayBusinessService")
    private GatewayBusinessService gatewayBusinessService;

    @Autowired
    @Qualifier("deviceBusinessService")
    private DeviceBusinessService deviceBusinessService;

    private EntityModel gatewayResource(GatewayEntity gateway) throws CustomException
    {
        EntityModel<GatewayEntity> resource = EntityModel.of(gateway);
        Link linkToSelf = WebMvcLinkBuilder.linkTo(methodOn(GatewayRestController.class)
                                                           .findById(null, gateway.getId()))
                                           .withSelfRel();
        resource.add(linkToSelf);
        Link linkToDevices = WebMvcLinkBuilder.linkTo(methodOn(GatewayRestController.class)
                                                              .getDevices(null, gateway.getId()))
                                              .withRel("devices");
        resource.add(linkToDevices);
        return resource;
    }

    private List<EntityModel> gatewayResource(List<GatewayEntity> gatewayList) throws CustomException
    {
        List<EntityModel> resourceList = new ArrayList<EntityModel>();
        for (GatewayEntity gateway : gatewayList)
        {
            resourceList.add(gatewayResource(gateway));
        }
        return resourceList;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity find(@CurrentSecurityContext HttpServletRequest request,
                               @RequestParam(name = "serialNumber", required = false) String serialNumber,
                               @RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "ipv4Address", required = false) String ipv4Address,
                               @RequestParam(name = "order", required = false) String order,
                               @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                               @RequestParam(name = "itemsPerPage", required = false) Integer itemsPerPage)
            throws CustomException
    {
        try
        {
            List<String> filterList = new ArrayList<String>();
            if (serialNumber != null && !serialNumber.isEmpty())
            {
                filterList.add("serialNumber=" + serialNumber);
            }
            if (name != null && !name.isEmpty())
            {
                filterList.add("name=" + name);
            }
            if (ipv4Address != null && !ipv4Address.isEmpty())
            {
                filterList.add("ipv4Address=" + ipv4Address);
            }

            if (pageNumber == null && itemsPerPage == null)
            {
                List<GatewayEntity> entityList = (filterList.isEmpty())? this.gatewayBusinessService.findAll(order) :
                                                 this.gatewayBusinessService.findFilters(filterList, order);
                if (entityList == null || entityList.isEmpty())
                {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(gatewayResource(entityList));
            }

            pageNumber = (pageNumber == null) ? 0 : pageNumber; // valores por defecto
            itemsPerPage = (itemsPerPage == null) ? 5 : itemsPerPage; // valores por defecto
            if (pageNumber < 0 || itemsPerPage <= 0)
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "Not paginated been worth."));
            }
            Page<GatewayEntity> entityPage = (filterList.isEmpty())? this.gatewayBusinessService.findAll(order, pageNumber, itemsPerPage) :
                                             this.gatewayBusinessService.findFilters(filterList, order, pageNumber, itemsPerPage);

            List<EntityModel> entityModelList = gatewayResource(entityPage.getContent());
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
            GatewayEntity entity = this.gatewayBusinessService.findById(id);
            if (entity == null)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                                                            "Not found Gateway with id " + id));
            }
            return ResponseEntity.ok(gatewayResource(entity));
        }
        catch (Throwable error)
        {
            LOGGER.error("GET:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @GetMapping(value = "/{id}/devices")
    public ResponseEntity getDevices(@CurrentSecurityContext HttpServletRequest request,
                                     @PathVariable(name = "id") long id) throws CustomException
    {
        try
        {
            if (gatewayBusinessService.existsId(id))
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                                                             "Not found Gateway with id " + id));
            }
            return ResponseEntity.ok(DeviceRestController.deviceResource(new ArrayList<>(gatewayBusinessService.getDevices(id))));
        }
        catch (Throwable error)
        {
            LOGGER.error("GET:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @PutMapping(value = "/{id}/devices")
    public ResponseEntity addDevices(@CurrentSecurityContext HttpServletRequest request,
                                     @PathVariable(name = "id") long id,
                                     @Validated @RequestBody DeviceEntity device,
                                     BindingResult result) throws CustomException
    {
        try
        {
            if (result.hasErrors())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, result.getFieldError().getDefaultMessage()));
            }
            if (!gatewayBusinessService.existsId(id))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey id '" + id + "' doesn't exist."));
            }
            if (gatewayBusinessService.numberOfDevices(id) + 1 > gatewayBusinessService.MAX_NUMBER_OF_DEVICES)
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "It exceeds the number maximum of devices (" + gatewayBusinessService.MAX_NUMBER_OF_DEVICES + ")."));
            }
            if (gatewayBusinessService.existsDeviceByUID(device.getUid()))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The device UID '" + device.getUid() + "' already exists."));
            }
            return ResponseEntity.ok(DeviceRestController.deviceResource((gatewayBusinessService.addDevices(id, device))));
        }
        catch (Throwable error)
        {
            LOGGER.error("GET:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @PutMapping(value = "/{id}/devices/several")
    public ResponseEntity addDevices(@CurrentSecurityContext HttpServletRequest request,
                                     @PathVariable(name = "id") long id,
                                     @Validated @RequestBody List<DeviceEntity> deviceList,
                                     BindingResult result) throws CustomException
    {
        try
        {
            if (result.hasErrors())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, result.getFieldError().getDefaultMessage()));
            }
            if (!gatewayBusinessService.existsId(id))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey id '" + id + "' doesn't exist."));
            }
            if (gatewayBusinessService.numberOfDevices(id) + deviceList.size() > gatewayBusinessService.MAX_NUMBER_OF_DEVICES)
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "It exceeds the number maximum of devices (" + gatewayBusinessService.MAX_NUMBER_OF_DEVICES + ")."));
            }
            for (DeviceEntity device : deviceList)
            {
                if (gatewayBusinessService.existsDeviceByUID(device.getUid()))
                {
                    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                            body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED,
                                                   "The device UID '" + device.getUid() + "' already exists."));
                }
            }

            return ResponseEntity.ok(DeviceRestController.deviceResource((gatewayBusinessService.addDevices(id, deviceList))));
        }
        catch (Throwable error)
        {
            LOGGER.error("GET:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity create(@CurrentSecurityContext HttpServletRequest request,
                                 @Validated @RequestBody GatewayEntity gateway,
                                 BindingResult result) throws CustomException
    {
        try
        {
            if (result.hasErrors())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, result.getFieldError().getDefaultMessage()));
            }
            if (gatewayBusinessService.existsSerialNumber(gateway.getSerialNumber()))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey serial number '" + gateway.getSerialNumber() + "' already exists."));
            }
            if (gatewayBusinessService.existsIpv4Address(gateway.getIpv4Address()))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey Ip v4 Address '" + gateway.getIpv4Address() + "' already exists."));
            }
            if (gatewayBusinessService.existsName(gateway.getName()))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey name '" + gateway.getName() + "' already exists."));
            }
            if (gatewayBusinessService.numberOfDevices(gateway.getId()) > gatewayBusinessService.MAX_NUMBER_OF_DEVICES)
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "It exceeds the number maximum of devices (" + gatewayBusinessService.MAX_NUMBER_OF_DEVICES + ")."));
            }

            for (DeviceEntity device: gateway.getDevices())
            {
                if (gatewayBusinessService.existsDeviceByUID(device.getUid()))
                {
                    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                            body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The device UID '" + device.getUid() + "' already exists."));
                }
            }

            GatewayEntity newGateway = this.gatewayBusinessService.save(gateway);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                                                               .buildAndExpand(newGateway.getId()).toUri();
            return ResponseEntity.created(location).body(gatewayResource(newGateway));
        }
        catch (Throwable error)
        {
            LOGGER.error("POST:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity update(@CurrentSecurityContext HttpServletRequest request,
                                 @PathVariable("id") long id,
                                 @Validated @RequestBody GatewayEntity gateway, BindingResult result)
            throws CustomException
    {
        try
        {
            if (result.hasErrors())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, result.getFieldError().getDefaultMessage()));
            }
            if (!gatewayBusinessService.existsId(id))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey id '" + id + "' doesn't exist."));
            }
            GatewayEntity oldGateway = this.gatewayBusinessService.findBySerialNumber(gateway.getSerialNumber());
            if (oldGateway != null && oldGateway.getId() != gateway.getId())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey serial number '" + gateway.getSerialNumber() + "' already exists."));
            }
            oldGateway = this.gatewayBusinessService.findByIpv4Address(gateway.getIpv4Address());
            if (oldGateway != null && oldGateway.getId() != gateway.getId())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey Ip v4 Address '" + gateway.getIpv4Address() + "' already exists."));
            }
            oldGateway = this.gatewayBusinessService.findByName(gateway.getName());
            if (oldGateway != null && oldGateway.getId() != gateway.getId())
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey name '" + gateway.getName() + "' already exists."));
            }
            if (gatewayBusinessService.numberOfDevices(gateway.getId()) > gatewayBusinessService.MAX_NUMBER_OF_DEVICES)
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "It exceeds the number maximum of devices (" + gatewayBusinessService.MAX_NUMBER_OF_DEVICES + ")."));
            }

            for (DeviceEntity device : gateway.getDevices())
            {
                DeviceEntity oldDevice = deviceBusinessService.findDeviceByUID(device.getUid());
                if (oldDevice != null && oldDevice.getId() != device.getId())
                {
                    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                            body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The device UID '" + device.getUid() + "' already exists."));
                }
            }

            return ResponseEntity.ok(gatewayResource(gatewayBusinessService.save(gateway)));
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
            if (!gatewayBusinessService.existsId(id))
            {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).
                        body(new ErrorResponse(HttpStatus.PRECONDITION_FAILED, "The gatewey id '" + id + "' doesn't exist."));
            }
            gatewayBusinessService.delete(id);
            return ResponseEntity.ok().build();
        }
        catch (Throwable error)
        {
            LOGGER.error("DELETE:" + request.getRequestURI() + " - " + error.getMessage());
            throw error;
        }
    }

}
