package bg.musala.test.yrm.jpa.repository;

import bg.musala.test.yrm.jpa.entity.DeviceEntity;
import bg.musala.test.yrm.jpa.entity.GatewayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The interface Gateway repository.
 */
@Repository("gatewayRepository")
public interface GatewayRepository extends CrudRepository<GatewayEntity, Long>,
                                            JpaRepository<GatewayEntity, Long>,
                                            PagingAndSortingRepository<GatewayEntity, Long>,
                                            JpaSpecificationExecutor<GatewayEntity>
{
    /**
     * Exists by serial number boolean.
     *
     * @param serialNumber the serial number
     * @return the boolean
     */
    boolean existsBySerialNumber(String serialNumber);

    /**
     * Exists by ipv 4 address boolean.
     *
     * @param ipv4Address the ipv 4 address
     * @return the boolean
     */
    boolean existsByIpv4Address(String ipv4Address);

    /**
     * Exists by name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean existsByName(String name);

    /**
     * Find by serial number gateway entity.
     *
     * @param serialNumber the serial number
     * @return the gateway entity
     */
    GatewayEntity findBySerialNumber(String serialNumber);

    /**
     * Find by ipv 4 address gateway entity.
     *
     * @param ipv4Address the ipv 4 address
     * @return the gateway entity
     */
    GatewayEntity findByIpv4Address(String ipv4Address);

    /**
     * Find by name gateway entity.
     *
     * @param name the name
     * @return the gateway entity
     */
    GatewayEntity findByName(String name);

    /**
     * Number of devices long.
     *
     * @param id the id
     * @return the long
     */
    @Query("SELECT COUNT(d) FROM DeviceEntity d WHERE d.diviceGateway.id = :id")
    long numberOfDevices(@Param("id") long id);
}
