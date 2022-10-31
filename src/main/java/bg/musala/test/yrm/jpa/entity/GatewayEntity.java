package bg.musala.test.yrm.jpa.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Gateway entity.
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GATEWAY")
@SequenceGenerator(name = "GATEWAY_SEQ", sequenceName = "GATEWAY_SEQ", initialValue = 1, allocationSize = 1)
public class GatewayEntity implements Serializable
{
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GATEWAY_SEQ")
    @Column(name = "ID", nullable = false, updatable = false, columnDefinition = "numeric")
    private long id;

    @Version
    @Basic
    @Column(name = "VERSION", nullable = false, columnDefinition = "numeric")
    private Long version;

    @NotNull(message = "'serialNumber' cannot be null")
    @Basic
    @Column(name = "SERIAL_NUMBER", nullable = false, unique = true, columnDefinition = "text")
    private String serialNumber;

    @NotNull(message = "'name' cannot be null")
    @Basic
    @Column(name = "NAME", nullable = false, unique = true, columnDefinition = "text")
    private String name;

    @NotNull(message = "'ipv4Address' cannot be null")
    @Pattern(regexp = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$", message = "invalid 'ipv4Address'")
    @Basic
    @Column(name = "IP_V4_ADDRESS", nullable = false, unique = true, length = 12, columnDefinition = "text")
    private String ipv4Address;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "diviceGateway", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeviceEntity> devices = new HashSet<>();
}
