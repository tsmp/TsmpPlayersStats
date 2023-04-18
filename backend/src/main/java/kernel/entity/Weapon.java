package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "k_weapons")
@NoArgsConstructor
@Getter
@Setter
public class Weapon
{
    @Id
    @Column(name = "wpn_id", nullable = false)
    private Integer Id;

    @Column(name = "name", nullable = false)
    private String Name;
}
