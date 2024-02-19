package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "k_nicknames")
@NoArgsConstructor
@Getter
@Setter
public class Nickname // Класс, соответствующий записи в бд
{
    public static String TYPE_NAME = "nickname_t";

    @Id
    @Column(name = "name_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "nickname_id_seq")
    @SequenceGenerator(name = "nickname_id_seq",sequenceName = "nickname_id_seq", allocationSize = 1)
    private Integer NameId;

    @Column(name = "nickname")
    private String Nickname;

    @Column(name = "player_id")
    private Integer PlayerId;
}
