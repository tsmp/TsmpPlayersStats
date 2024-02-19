package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "k_player_game")
@NoArgsConstructor
@Getter
@Setter
public class PlayerGame // Класс, соответствующий записи в бд
{
    public static String TYPE_NAME = "name_t";

    @jakarta.persistence.Id
    @Column(name = "player_game_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "player_game_id_seq")
    @SequenceGenerator(name = "player_game_id_seq",sequenceName = "player_game_id_seq", allocationSize = 1)
    private Integer PlayerGameId;

    @Column(name = "player_id", nullable = false)
    private Integer PlayerId;

    @Column(name = "game_id", nullable = false)
    private Integer GameId;
}
