package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;


@Entity
@Table(name = "k_hits")
@NoArgsConstructor
@Getter
@Setter
public class Hit
{
    @Id
    @Column(name = "hits_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hit_id_seq")
    @SequenceGenerator(name = "hit_id_seq",sequenceName = "hit_id_seq", allocationSize = 1)
    private Integer Id;

    @Column(name = "game_id", nullable = false)
    private Integer game;

    @Column(name = "weapon_id", nullable = false)
    private Integer wpn;

    @Column(name = "hits_count", nullable = false)
    private Integer hits;
}
