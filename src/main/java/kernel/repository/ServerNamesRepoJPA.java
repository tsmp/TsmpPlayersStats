package kernel.repository;

import kernel.entity.Player;
import kernel.entity.ServerName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ServerNamesRepoJPA extends JpaRepository<ServerName, Integer> // Магия для обращения к бд
{
    public List<ServerName> findAll();
}
