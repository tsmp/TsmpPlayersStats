package kernel.repository;

import kernel.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRepoJPA extends JpaRepository<MapEntity, Integer> // Магия для обращения к бд
{
    public List<MapEntity> findAll();

    @Query(value = "select map_id from k_maps where position(:Name in name)>0", nativeQuery = true)
    public List<Integer> SearchByName(@Param("Name") String name);
}
