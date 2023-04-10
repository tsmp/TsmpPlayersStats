package kernel.repository;

import kernel.entity.PlayerGame;
import kernel.entity.ServerName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerGameRepoJPA extends JpaRepository<PlayerGame, Integer> // Магия для обращения к бд
{
    public List<PlayerGame> findAll();
}
