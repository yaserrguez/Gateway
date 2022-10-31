package bg.musala.test.yrm.businessService;

import bg.musala.test.yrm.exception.CustomException;
import bg.musala.test.yrm.jpa.entity.DeviceEntity;
import bg.musala.test.yrm.jpa.entity.GatewayEntity;
import bg.musala.test.yrm.jpa.repository.DeviceRepository;
import bg.musala.test.yrm.jpa.repository.GatewayRepository;
import bg.musala.test.yrm.jpa.specification.DeviceSpecification;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("deviceBusinessService")
@CacheConfig(cacheNames = "device")
public class DeviceBusinessService
{
    private static final Log LOGGER = LogFactory.getLog(DeviceBusinessService.class);

    @Autowired
    @Qualifier("deviceRepository")
    private DeviceRepository deviceRepository;

    @Autowired
    @Qualifier("gatewayRepository")
    private GatewayRepository gatewayRepository;

    public boolean existsId(Long id)
    {
        return deviceRepository.existsById(id);
    }

    public DeviceEntity findDeviceByUID(Long uid)
    {
        return deviceRepository.findByUid(uid);
    }

    @Cacheable(key = "'device_'+#root.methodName+#order")
    public List<DeviceEntity> findAll(String order) throws CustomException
    {
        try
        {
            if (order == null || order.isEmpty())
            {
                return deviceRepository.findAll();
            }

            Sort sortOrder = GatewayBusinessService.getSortOrder(order);
            return deviceRepository.findAll(sortOrder);
        }
        catch (Throwable error)
        {
            LOGGER.error("DeviceBusinessService.findAll" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findAll", "Error find device - " + error.getMessage(), error.getCause());
        }
    }

    @Cacheable(key = "'device_'+#root.methodName+#order+#pageNumber+#itemsPerPage")
    public Page<DeviceEntity> findAll(String order, Integer pageNumber, Integer itemsPerPage) throws CustomException
    {
        try
        {
            if (order == null || order.isEmpty())
            {
                return deviceRepository.findAll(PageRequest.of(pageNumber, itemsPerPage));
            }

            Sort sortOrder = GatewayBusinessService.getSortOrder(order);
            return deviceRepository.findAll(PageRequest.of(pageNumber, itemsPerPage, sortOrder));
        }
        catch (Throwable error)
        {
            LOGGER.error("DeviceBusinessService.findAll" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findAll", "Error find device - " + error.getMessage(), error.getCause());
        }
    }

    public List<DeviceEntity> findFilters(List<String> filterList, String order) throws CustomException
    {
        try
        {
            Specification specification = new DeviceSpecification().getSpecificationByAndOperator(filterList);

            if (order == null || order.isEmpty())
            {
                return deviceRepository.findAll(specification);
            }

            Sort sortOrder = GatewayBusinessService.getSortOrder(order);
            return deviceRepository.findAll(specification, sortOrder);
        }
        catch (Throwable error)
        {
            LOGGER.error("DeviceBusinessService.findFilters" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findAll", "Error find device - " + error.getMessage(), error.getCause());
        }
    }

    public Page<DeviceEntity> findFilters(List<String> filterList, String order, Integer pageNumber, Integer itemsPerPage) throws CustomException
    {
        try
        {
            Specification specification = new DeviceSpecification().getSpecificationByAndOperator(filterList);

            if (order == null || order.isEmpty())
            {
                return deviceRepository.findAll(specification, PageRequest.of(pageNumber, itemsPerPage));
            }

            Sort sortOrder = GatewayBusinessService.getSortOrder(order);
            return deviceRepository.findAll(specification, PageRequest.of(pageNumber, itemsPerPage, sortOrder));
        }
        catch (Throwable error)
        {
            LOGGER.error("DeviceBusinessService.findFilters" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findAll", "Error find device - " + error.getMessage(), error.getCause());
        }
    }

    @Cacheable(key = "'device_'+#id")
    public DeviceEntity findById(long id) throws CustomException
    {
        try
        {
            Optional<DeviceEntity> device = this.deviceRepository.findById(id);
            return (device.isPresent())? device.get() : null;
        }
        catch (Throwable error)
        {
            LOGGER.error("DeviceBusinessService.findById" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "findById", "Error find device - id = '" + id + "' - " + error.getMessage(), error.getCause());
        }
    }

    @Transactional
    @Caching(evict={@CacheEvict(value = "gateway", allEntries = true),
                    @CacheEvict(value = "device", allEntries = true)})
    public DeviceEntity save(DeviceEntity device) throws CustomException
    {
        try
        {
            DeviceEntity oldDevice = findById(device.getId());
            device.setDiviceGateway(oldDevice.getDiviceGateway());
            return this.deviceRepository.save(device);
        }
        catch (Throwable error)
        {
            LOGGER.error("DeviceBusinessService.save" + " - " + error.getMessage());
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
            DeviceEntity device = findById(id);
            GatewayEntity gateway = device.getDiviceGateway();
            gateway.getDevices().remove(device);

            gatewayRepository.save(gateway);
        }
        catch (Throwable error)
        {
            LOGGER.error("DeviceBusinessService.delete" + " - " + error.getMessage());
            throw new CustomException(LOGGER, "delete", "Error delete device - id = '" + id + "' - " + error.getMessage(), error.getCause());
        }
    }
}
