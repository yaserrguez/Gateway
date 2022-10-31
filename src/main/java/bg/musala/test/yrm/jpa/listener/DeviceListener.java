package bg.musala.test.yrm.jpa.listener;

import bg.musala.test.yrm.jpa.entity.DeviceEntity;

import javax.persistence.PrePersist;
import java.sql.Timestamp;

/**
 * The type Device listener.
 */
public class DeviceListener
{
    /**
     * Prepersit.
     *
     * @param entity the entity
     */
    @PrePersist
    public void prepersit(DeviceEntity entity)
    {
        entity.setCreated(new Timestamp(System.currentTimeMillis()));
    }
}
