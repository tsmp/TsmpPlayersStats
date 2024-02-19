//package kernel.entity;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "k_banned_cheaters")
//@NoArgsConstructor
//@Getter
//@Setter
//public class BannedCheater // Класс, соответствующий записи в бд
//{
//    public static String TYPE_NAME = "Забаненный читер";
//
//    @Id
//    @Column(name = "ip_address",nullable = false)
//    private String IpAddress;
//
//    @Column(name = "nickname")
//    private String Nickname;
//
//    @Column(name = "ban_reason_id")
//    private Integer BanReasonId;
//
//    @Column(name = "banned_srv_name")
//    private String BannedSrvName;
//
//    @Column(name = "banned_srv_ver")
//    private String BannedSrvVer;
//}
