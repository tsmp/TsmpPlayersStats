package kernel.repository;

import kernel.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamesRepoJPA extends JpaRepository<Game, Integer> // Магия для обращения к бд
{
    public List<Game> findAll();
}
