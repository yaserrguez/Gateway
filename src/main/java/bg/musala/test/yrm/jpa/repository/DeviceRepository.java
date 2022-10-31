package bg.musala.test.yrm.jpa.repository;

import bg.musala.test.yrm.jpa.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Device repository.
 */
@Repository("deviceRepository")
public interface DeviceRepository extends CrudRepository<DeviceEntity, Long>,
                                            JpaRepository<DeviceEntity, Long>,
                                            PagingAndSortingRepository<DeviceEntity, Long>,
                                            JpaSpecificationExecutor<DeviceEntity>
{
    /**
     * Exists by uid boolean.
     *
     * @param uid the uid
     * @return the boolean
     */
    boolean existsByUid(Long uid);

    /**
     * Find by uid device entity.
     *
     * @param uid the uid
     * @return the device entity
     */
    DeviceEntity findByUid(Long uid);
}
