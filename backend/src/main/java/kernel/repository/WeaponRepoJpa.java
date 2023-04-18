package kernel.repository;

import kernel.entity.ServerName;
import kernel.entity.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeaponRepoJpa extends JpaRepository<Weapon, Integer> // Магия для обращения к бд
{
    public List<Weapon> findAll();
}
