package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "srv_ver")
    private String SrvVer;
}