package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "k_infos")
@NoArgsConstructor
@Getter
@Setter
public class Note
{
    @Id
    @Column(name = "info_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "info_id_seq")
    @SequenceGenerator(name = "info_id_seq",sequenceName = "info_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "player_id")
    private Integer PlayerId;

    @Column(name = "info")
    private String Info;

    @Column(name = "author")
    private String author;
}