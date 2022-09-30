//package kernel.repository;
//
//import kernel.entity.BannedCheater;
//import kernel.entity.Player;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface PlayersRepositoryJPA extends JpaRepository<Player, Integer> // Магия для обращения к бд
//{
//    //@Query(value = "select count(bc) from k_banned_cheaters bc where ip_address = :IpAddress",nativeQuery = true)
//    //public Integer isRecordExist(@Param("IpAddress") String IpAddress);
//
//    List<Player> findAll();
//}
