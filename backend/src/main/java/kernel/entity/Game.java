package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "k_games")
@NoArgsConstructor
@Getter
@Setter
public class Game // Класс, соответствующий записи в бд
{
    public static String TYPE_NAME = "game_t";

    @Id
    @Column(name = "game_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "game_id_seq")
    @SequenceGenerator(name = "game_id_seq",sequenceName = "game_id_seq", allocationSize = 1)
    private Integer GameId;

    @Column(name = "game_date")
    private Date GameDate;

    @Column(name = "srv_name_id")
    private Integer ServerNameId;

    @Column(name = "map_name_id")
    private Integer MapNameId;

    @Column(name = "player_uid")
    private Integer PlayerUID;

    @Column(name = "game_time_minutes")
    private Integer GameTimeMinutes;

    @Column(name = "kills")
    private Integer Kills;

    @Column(name = "kills_ai")
    private Integer KillsAi;

    @Column(name = "artefacts")
    private Integer Artefacts;

    @Column(name = "deaths")
    private Integer Deaths;

    @Column(name = "headshots")
    private Integer Headshots;

    @Column(name = "max_kills_one_life")
    private Integer MaxKillsOneLife;
}
