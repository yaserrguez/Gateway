package bg.musala.test.yrm.businessService;

import bg.musala.test.yrm.exception.CustomException;
import bg.musala.test.yrm.jpa.entity.DeviceEntity;
import bg.musala.test.yrm.jpa.entity.GatewayEntity;
import bg.musala.test.yrm.jpa.repository.DeviceRepository;
import bg.musala.test.yrm.jpa.repository.GatewayRepository;
import bg.musala.test.yrm.jpa.specification.GateweySpecification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Service("gatewayBusinessService")
@CacheConfig(cacheNames = "gateway")
public class GatewayBusinessService
{
    private static final Log LOGGER = LogFactory.getLog(GatewayBusinessService.class);
    public static final int MAX_NUMBER_OF_DEVICES = 10;

    @Autowired
    @Qualifier("gatewayRepository")
    private GatewayRepository gatewayRepository;

    @Autowired
    @Qualifier("deviceRepository")
    private DeviceRepository deviceRepository;

    public boolean existsId(Long id)
    {
        return gatewayRepository.existsById(id);
    }

    public boolean existsSerialNumber(String serialNumber)
    {
        return gatewayRepository.existsBySerialNumber(serialNumber);
    }

    public GatewayEntity findBySerialNumber(String serialNumber)
    {
        return gatewayRepository.findBySerialNumber(serialNumber);
    }

    public boolean existsIpv4Address(String ipv4Address)
    {
        return gatewayRepository.existsByIpv4Address(ipv4Address);
    }

    public GatewayEntity findByIpv4Address(String ipv4Address)
    {
        return gatewayRepository.findByIpv4Address(ipv4Address);
    }

    public boolean existsName(String name)
    {
        return gatewayRepository.existsByName(name);
    }

    public GatewayEntity findByName(String name)
    {
        return gatewayRepository.findByName(name);
    }

    public boolean existsDeviceByUID(Long uid)
    {
        return deviceRepository.existsByUid(uid);
    }

    public long numberOfDevices(long id)
    {
        return gatewayRepository.numberOfDevices(id);
    }

    public static Sort getSortOrder(@NotNull String order)
    {
        if (order == null || order.isEmpty())
        {
            return null;
        }

        Sort.Direction direction = Sort.DEFAULT_DIRECTION;
        String fieldname = order;

        if (order.charAt(0) == '-')
        {
            direction = Sort.Direction.DESC;
            fieldname = order.substring(1);
        }
        else
        {
            if (order.charAt(0) == '+')
            {
                //direction = Sort.Direction.ASC;
                fieldname = order.substring(1);
            }
        }

        return Sort.by(direction, fieldname);
    }

    @Cacheable(key = "'gateway_'+#root.methodName+#order")
    public List<GatewayEntity> findAll(String order) throws CustomException
    {
        try
        {
            if (order == null || order.isEmpty())
            {
                return gatewayRepository.findAll();
            }

            Sort sortOrder = this.getSortOrder(order);
            return gatewayRepository.findAll(sortOrder);
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.findAll" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findAll", "Error find gateway - " + error.getMessage(), error.getCause());
        }
    }

    @Cacheable(key = "'gateway_'+#root.methodName+#order+#pageNumber+#itemsPerPage")
    public Page<GatewayEntity> findAll(String order, Integer pageNumber, Integer itemsPerPage) throws CustomException
    {
        try
        {
            if (order == null || order.isEmpty())
            {
                return gatewayRepository.findAll(PageRequest.of(pageNumber, itemsPerPage));
            }

            Sort sortOrder = this.getSortOrder(order);
            return gatewayRepository.findAll(PageRequest.of(pageNumber, itemsPerPage, sortOrder));
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.findAll" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findAll", "Error find gateway - " + error.getMessage(), error.getCause());
        }
    }

    public List<GatewayEntity> findFilters(List<String> filterList, String order) throws CustomException
    {
        try
        {
            Specification specification = new GateweySpecification().getSpecificationByAndOperator(filterList);

            if (order == null || order.isEmpty())
            {
                return gatewayRepository.findAll(specification);
            }

            Sort sortOrder = this.getSortOrder(order);
            return gatewayRepository.findAll(specification, sortOrder);
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.findFilters" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findAll", "Error find gateway - " + error.getMessage(), error.getCause());
        }
    }

    public Page<GatewayEntity> findFilters(List<String> filterList, String order, Integer pageNumber, Integer itemsPerPage) throws CustomException
    {
        try
        {
            Specification specification = new GateweySpecification().getSpecificationByAndOperator(filterList);

            if (order == null || order.isEmpty())
            {
                return gatewayRepository.findAll(specification, PageRequest.of(pageNumber, itemsPerPage));
            }

            Sort sortOrder = this.getSortOrder(order);
            return gatewayRepository.findAll(specification, PageRequest.of(pageNumber, itemsPerPage, sortOrder));
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.findFilters" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findAll", "Error find gateway - " + error.getMessage(), error.getCause());
        }
    }

    @Cacheable(key = "'gateway_'+#id")
    public GatewayEntity findById(long id) throws CustomException
    {
        try
        {
            Optional<GatewayEntity> gateway = this.gatewayRepository.findById(id);
            return (gateway.isPresent())? gateway.get() : null;
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.findById" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findById", "Error find gateway - id = '" + id + "' - " + error.getMessage(), error.getCause());
        }
    }

    @Cacheable(key = "'gateway_'+#root.methodName+#id")
    public Set<DeviceEntity> getDevices(long id) throws CustomException
    {
        try
        {
            Optional<GatewayEntity> gateway = this.gatewayRepository.findById(id);
            if (gateway.isPresent())
            {
                return  gateway.get().getDevices();
            }
            return null;
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.getDevices" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "getDevices", "Error find gateway devices - id = '" + id + "' - " + error.getMessage(), error.getCause());
        }
    }

    @CacheEvict(value = "gateway", allEntries = true )
    public DeviceEntity addDevices(long id, DeviceEntity device) throws CustomException
    {
        try
        {
            GatewayEntity gateway = this.gatewayRepository.findById(id).get();
            device.setDiviceGateway(gateway);

            return deviceRepository.save(device);
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.addDevices" + " - " + error.getMessage());
            throw error;
        }
    }

    @Caching(evict={@CacheEvict(value = "gateway", allEntries = true),
                    @CacheEvict(value = "device", allEntries = true)})
    public List<DeviceEntity> addDevices(long id, List<DeviceEntity> deviceList) throws CustomException
    {
        try
        {
            GatewayEntity gateway = this.gatewayRepository.findById(id).get();
            for (DeviceEntity device : deviceList)
            {
                device.setDiviceGateway(gateway);
            }

            return deviceRepository.saveAll(deviceList);
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.addDevices" + " - " + error.getMessage());
            throw error;
        }
    }

    @Transactional
    @Caching(evict={@CacheEvict(value = "gateway", allEntries = true),
                    @CacheEvict(value = "device", allEntries = true)})
    public GatewayEntity save(GatewayEntity gateway) throws CustomException
    {
        try
        {
            for (DeviceEntity device : gateway.getDevices())
            {
                device.setDiviceGateway(gateway);
            }

            return this.gatewayRepository.save(gateway);
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.save" + " - " + error.getMessage());
            throw error;
        }
    }

    @Transactional
    @Caching(evict={@CacheEvict(value = "gateway", allEntries = true),
                    @CacheEvict(value = "device", allEntries = true)})
    public void delete(long id) throws CustomException
    {
        try
        {
            this.gatewayRepository.deleteById(id);
        }
        catch (Throwable error)
        {
            LOGGER.error("GatewayBusinessService.delete" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "delete", "Error delete gateway - id = '" + id + "' - " + error.getMessage(), error.getCause());
        }
    }
}
