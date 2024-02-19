//package kernel.controllers;
//
//import kernel.entity.BannedCheater;
//import kernel.repository.BannedRepositoryJPA;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import jakarta.servlet.http.HttpServletRequestHttpServletRequest;
//import java.util.List;
//
//@RestController
//@RequestMapping("BanSyncService/v1")
//public class PlayersBanControllerV1 // Обработка запросов HTTP
//{
//    @Autowired
//    private BannedRepositoryJPA bannedRepositoryJPA;
//
//    @GetMapping("/hello")
//    public String getHello(HttpServletRequest request)
//    {
//        System.out.println(request.getRemoteAddr()+" requests Hello");
//        return "Hello world! 12345. Привет, нубик)";
//    }
//
//    @GetMapping("/BanCheater")
//    public void banCheater(
//            @RequestParam("ip") String ip,
//            @RequestParam("reasonId") Integer reasonId,
//            @RequestParam("srvName") String srvName,
//            @RequestParam("srvVer") String srvVer,
//            @RequestParam("nick") String nickname,
//            HttpServletRequest request)
//    {
//        System.out.println(request.getRemoteAddr() + " requests BanCheater. ip = " + ip + ", name = " + nickname + ", srv = " + srvName);
//
//        BannedCheater cheater = new BannedCheater();
//        cheater.setIpAddress(ip);
//        cheater.setNickname(nickname);
//        cheater.setBanReasonId(reasonId);
//        cheater.setBannedSrvName(srvName);
//        cheater.setBannedSrvVer(srvVer);
//        bannedRepositoryJPA.save(cheater);
//    }
//
//    @GetMapping("/IsBanned")
//    public String setupForm1(@RequestParam("ip") String ip, HttpServletRequest request)
//    {
//        System.out.println(request.getRemoteAddr() + " requests IsBanned for " + ip);
//        return Integer.toString(bannedRepositoryJPA.isRecordExist(ip));
//    }
//
//    @GetMapping("/ListAll")
//    public String getDateToIsNull(HttpServletRequest request)
//    {
//        System.out.println(request.getRemoteAddr() + " requests ListAll");
//
//        // Можно еще красивее вывести для html но пока и так сойдет))
//        List<BannedCheater> lst = bannedRepositoryJPA.findAll();
//        String str="";
//
//        String TabHTML = "&emsp; &emsp; &emsp; &emsp; &emsp; &emsp; &emsp; &emsp;";
//
//        str+="ip address ";
//        str+=TabHTML;
//
//        str+="nickname ";
//        str+=TabHTML;
//
//        str+="ban reason id";
//        str+=TabHTML;
//
//        str+="banned srv name";
//        str+=TabHTML;
//
//        str+="srv ver";
//        str+=TabHTML;
//
//        for(int i = 0; i < lst.size(); i++)
//        {
//            str+="<br>";
//            BannedCheater cheater = lst.get(i);
//
//            str+=cheater.getIpAddress();
//            str+=TabHTML;
//
//            str+=cheater.getNickname();
//            str+=TabHTML;
//
//            str+=Integer.toString(cheater.getBanReasonId());
//            str+=TabHTML;
//
//            str+=cheater.getBannedSrvName();
//            str+=TabHTML;
//
//            str+=cheater.getBannedSrvVer();
//            str+=TabHTML;
//        }
//
//        return str;
//    }
//}
