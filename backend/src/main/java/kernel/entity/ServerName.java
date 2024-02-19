package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "k_server_names")
@NoArgsConstructor
@Getter
@Setter
public class ServerName // Класс, соответствующий записи в бд
{
    public static String TYPE_NAME = "name_t";

    @Id
    @Column(name = "srv_name_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "srv_name_id_seq")
    @SequenceGenerator(name = "srv_name_id_seq",sequenceName = "srv_name_id_seq", allocationSize = 1)
    private Integer Id;

    @Column(name = "srv_name", nullable = false)
    private String Name;
}
