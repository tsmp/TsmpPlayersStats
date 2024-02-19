package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Set;



@Entity
@Table(name = "k_players")
@NoArgsConstructor
@Getter
@Setter
public class Player // Класс, соответствующий записи в бд
{
    public static String TYPE_NAME = "player_t";
    public static String NoHwidStr = "- No hwid -";

    @Id
    @Column(name = "player_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "player_id_seq")
    @SequenceGenerator(name = "player_id_seq",sequenceName = "player_id_seq", allocationSize = 1)
    private Integer PlayerId;

    @Column(name = "uid")
    private Integer UID;
}
