package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "k_weapons")
@NoArgsConstructor
@Getter
@Setter
public class Weapon
{
    @Id
    @Column(name = "wpn_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "weapon_id_seq")
    @SequenceGenerator(name = "weapon_id_seq",sequenceName = "weapon_id_seq", allocationSize = 1)
    private Integer Id;

    @Column(name = "name", nullable = false)
    private String Name;
}
