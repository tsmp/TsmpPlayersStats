package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "k_maps")
@NoArgsConstructor
@Getter
@Setter
public class MapEntity
{
    @Id
    @Column(name = "map_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "map_id_seq")
    @SequenceGenerator(name = "map_id_seq",sequenceName = "map_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;
}
