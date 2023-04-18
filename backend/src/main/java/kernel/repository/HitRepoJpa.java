package kernel.repository;

import kernel.entity.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HitRepoJpa extends JpaRepository<Hit, Integer> // Магия для обращения к бд
{
    public List<Hit> findAll();
}

