package kernel.repository;

import kernel.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayersRepoJPA extends JpaRepository<Player, Integer> // Магия для обращения к бд
{
//    @Query(value = "select count(ac) from k_active_sessions ac where ip_address = :SessionId", nativeQuery = true)
//    public Integer isRecordExist(@Param("SessionId") Integer SessionId);

    public List<Player> findAll();

    public List<Player> findByUIDNotNull();

    Optional<Player> findById(Integer id);
    @Query(value = "select player_id from k_players where :Uid = uid", nativeQuery = true)
    public List<Integer> SearchByUID(@Param("Uid") int uid);

    @Query(value = "SELECT nextval('player_unique_id_seq');", nativeQuery = true)
    public Integer NextValueUID();
}

