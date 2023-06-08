package kernel.repository;

import kernel.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamesRepoJPA extends JpaRepository<Game, Integer> // Магия для обращения к бд
{
    public List<Game> findAll();

    @Query(value = "select * from k_games where :pluid = player_uid", nativeQuery = true)
    public List<Game> findByPlayerUID(@Param("pluid") int PlayerUID);

    @Query(value = "select sum(kills) from k_games where :pluid = player_uid", nativeQuery = true)
    public Integer sumPlayerFrags(@Param("pluid") int PlayerUID);

    @Query(value = "select sum(deaths) from k_games where :pluid = player_uid", nativeQuery = true)
    public Integer sumPlayerDeaths(@Param("pluid") int PlayerUID);

    @Query(value = "select sum(artefacts) from k_games where :pluid = player_uid", nativeQuery = true)
    public Integer sumPlayerArts(@Param("pluid") int PlayerUID);

    @Query(value = "select sum(game_time_minutes) from k_games where :pluid = player_uid", nativeQuery = true)
    public Integer sumPlayerMinutesIngame(@Param("pluid") int PlayerUID);
}
