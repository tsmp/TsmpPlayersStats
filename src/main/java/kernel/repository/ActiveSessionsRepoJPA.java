package kernel.repository;

import kernel.entity.ActiveSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveSessionsRepoJPA extends JpaRepository<ActiveSession, Integer> // Магия для обращения к бд
{
    @Query(value = "select count(ac) from k_active_sessions ac where ip_address = :SessionId", nativeQuery = true)
    public Integer isRecordExist(@Param("SessionId") Integer SessionId);

    public List<ActiveSession> findAll();
}

