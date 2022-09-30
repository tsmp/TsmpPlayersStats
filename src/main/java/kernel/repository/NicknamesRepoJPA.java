package kernel.repository;

import kernel.entity.IpAddress;
import kernel.entity.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NicknamesRepoJPA extends JpaRepository<Nickname, Integer> // Магия для обращения к бд
{
    public List<Nickname> findAll();

    @Query(value = "select player_id from k_nicknames where position(:NickName in LOWER(nickname))>0", nativeQuery = true)
    public List<Integer> SearchByNickname(@Param("NickName") String name);
}
