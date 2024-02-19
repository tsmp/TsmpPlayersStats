package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "k_admins")
@NoArgsConstructor
@Getter
@Setter
public class BaseAdmin // Класс, соответствующий записи в бд
{
    public static String TYPE_NAME = "admin_t";

    @Id
    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "psw")
    private String password;
}