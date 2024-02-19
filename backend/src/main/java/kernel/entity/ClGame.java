package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import java.util.Date;

@Entity
@IdClass( ClGameKey.class)
@Getter
@Setter
@NoArgsConstructor
public class ClGame
{
    @Id
    private String SrvName;
    @Id
    private Date GameDate;
}

