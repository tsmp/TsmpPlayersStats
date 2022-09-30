package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "hwid")
    private String Hwid;

    @OneToMany(mappedBy="Player")
    private Set<IpAddress> Addresses;

    @OneToMany(mappedBy="PlayerId")
    private Set<Nickname> Nicknames;

//    @OneToMany(mappedBy="PlayerId")
//    private Set<Game> Games;

//    @OneToMany
//    @JoinColumn(name = "ip_address",referencedColumnName = "ip_address",nullable = false)
//    private IpAddress ipAddress;
}
