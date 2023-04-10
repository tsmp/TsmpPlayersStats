package kernel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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

