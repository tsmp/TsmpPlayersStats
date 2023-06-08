package kernel.repository;

import kernel.entity.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HitRepoJpa extends JpaRepository<Hit, Integer> // Магия для обращения к бд
{
    public List<Hit> findAll();

    @Query(value = "select weapon_id from\n" +
            "(\n" +
            "select sum(hits_count) as cnt, weapon_id from\n" +
            "(\n" +
            "select hits_count, weapon_id from k_hits where game_id in\n" +
            "(select game_id from k_games where :pluid = player_uid)\n" +
            ") as g\n" +
            "group by weapon_id order by cnt desc limit 1\n" +
            "\t) as e", nativeQuery = true)
    public Integer bestWeaponId(@Param("pluid") int PlayerUID);

    @Query(value = "select * from k_hits where :Game = game_id", nativeQuery = true)
    public List<Hit> SearchByGame(@Param("Game") Integer gameId);
}

