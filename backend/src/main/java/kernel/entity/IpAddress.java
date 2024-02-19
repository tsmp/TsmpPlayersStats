package kernel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
