package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "k_addresses")
@NoArgsConstructor
@Getter
@Setter
public class IpAddress // Класс, соответствующий записи в бд
{
    public static String TYPE_NAME = "ip_t";

    @Id
    @Column(name = "ip_address", nullable = false)
    private String Address;

    @Column(name = "player")
    private Integer Player;
}
