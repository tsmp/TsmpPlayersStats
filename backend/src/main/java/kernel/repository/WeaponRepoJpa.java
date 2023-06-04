package kernel.repository;

import kernel.entity.ServerName;
import kernel.entity.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeaponRepoJpa extends JpaRepository<Weapon, Integer> // Магия для обращения к бд
{
    public List<Weapon> findAll();

    @Query(value = "select wpn_id from k_weapons where position(:Name in name)>0", nativeQuery = true)
    public List<Integer> SearchByName(@Param("Name") String name);
}
