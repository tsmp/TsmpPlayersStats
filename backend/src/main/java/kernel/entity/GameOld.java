//package kernel.entity;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import jakarta.persistence.*;
//import java.util.Date;
//
//@Entity
//@Table(name = "k_games_old")
//@NoArgsConstructor
//@Getter
//@Setter
//public class GameOld // Класс, соответствующий записи в бд
//{
//    public static String TYPE_NAME = "game_t";
//
//    @Id
//    @Column(name = "game_id", nullable = false)
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "game_id_seq_old")
//    @SequenceGenerator(name = "game_id_seq_old",sequenceName = "game_id_seq_old", allocationSize = 1)
//
//    private Integer GameId;
//
//    @Column(name = "game_date")
//    private Date EnterGameDate;
//
//    @Column(name = "srv_name")
//    private String ServerName;
//
//    @Column(name = "player_id")
//    private Integer PlayerId;
//}
