package bg.musala.test.yrm.jpa.entity;

import bg.musala.test.yrm.jpa.listener.DeviceListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The type Device entity.
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(DeviceListener.class)
@Table(name = "DEVICE")
@SequenceGenerator(name = "DEVICE_SEQ", sequenceName = "DEVICE_SEQ", initialValue = 1, allocationSize = 1)
public class DeviceEntity implements Serializable
{
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEVICE_SEQ")
    @Column(name = "ID", nullable = false, updatable = false, columnDefinition = "numeric")
    private long id;

    @Basic
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "numeric")
    private Long version;

    @NotNull(message = "'UID' cannot be null")
    @Basic
    @Column(name = "U_I_D", nullable = false, unique = true, columnDefinition = "numeric")
    private Long uid;

    @NotNull(message = "'vendor' cannot be null")
    @Size(min = 2, max = 50, message = "Invalid 'vendor' (between 2 and 50 characters)")
    @Basic
    @Column(name = "VENDOR", nullable = false, length = 50, columnDefinition = "text")
    private String vendor;

    @CreatedDate
    @Basic
    @Column(name = "CREATED", nullable = false, updatable = false, columnDefinition = "date")
    private Timestamp created;

    @Basic
    @Column(name = "STATUS", nullable = false, columnDefinition = "boolean")
    private Boolean status = true;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GATEWAY_ID", referencedColumnName = "ID", nullable = false)
    private GatewayEntity diviceGateway;
}
