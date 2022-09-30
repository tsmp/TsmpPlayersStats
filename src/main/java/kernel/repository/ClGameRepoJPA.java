package kernel.repository;

import kernel.entity.ClGame;
import kernel.entity.ClGameKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClGameRepoJPA extends JpaRepository<ClGame, ClGameKey>
{
    @Query(value = "select * from get_player_games(:PlayerId)", nativeQuery = true)
    public List<ClGame> getGamesByPlayer(@Param("PlayerId")Integer playerId);
}

