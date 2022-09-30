package kernel.repository;

import kernel.entity.BaseAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminsRepoJPA extends JpaRepository<BaseAdmin, String> // Магия для обращения к бд
{
    public List<BaseAdmin> findAll();
}
