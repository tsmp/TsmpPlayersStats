package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "k_active_sessions")
@NoArgsConstructor
@Getter
@Setter
public class ActiveSession // Класс, соответствующий записи в бд
{
    public static String TYPE_NAME = "session_t";

    @Id
    @Column(name = "session_key", nullable = false)
    private int SessionKey;

    @Column(name = "srv_name_id")
    private Integer SrvNameId;

    @Column(name = "srv_map_id")
    private Integer SrvMapId;

    @Column(name = "srv_ver")
    private String SrvVer;
}