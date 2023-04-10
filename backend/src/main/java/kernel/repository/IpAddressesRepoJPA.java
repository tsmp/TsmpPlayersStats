package kernel.repository;

import kernel.entity.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpAddressesRepoJPA extends JpaRepository<IpAddress, String> // Магия для обращения к бд
{
    public List<IpAddress> findAll();

    @Query(value = "select player from k_addresses where position(:StrToSearch in ip_address)>0", nativeQuery = true)
    public List<Integer> SearchByStr(@Param("StrToSearch") String str);
}
