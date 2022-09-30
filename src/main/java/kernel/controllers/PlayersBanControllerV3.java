//package kernel.controllers;
//
//import kernel.entity.BannedCheater;
//import kernel.entity.Player;
//import kernel.repository.BannedRepositoryJPA;
//import kernel.repository.PlayersRepositoryJPA;
//import kernel.services.SessionManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@RestController
//@RequestMapping("BanSyncService/v3")
//public class PlayersBanControllerV3 // Обработка запросов HTTP
//{
//    @Autowired
//    private BannedRepositoryJPA bannedRepositoryJPA;
//
//    @Autowired
//    private PlayersRepositoryJPA playersRepositoryJPA;
//
//    @Autowired
//    private SessionManager sessionManager;
//
//    private void PrintRequestData(HttpServletRequest request)
//    {
//        System.out.print(request.getRemoteAddr() + " requests ");
//    }
//
//    @GetMapping("/hello")
//    public String getHello(HttpServletRequest request)
//    {
//        PrintRequestData(request);
//        System.out.println("Hello");
//        return "Hello world! 12345. Привет, нубик)";
//    }
//
//    @GetMapping("/hash")
//    public int getHash(@RequestParam("str") String str, HttpServletRequest request)
//    {
//        PrintRequestData(request);
//        System.out.println("hash from " + str);
//        return sessionManager.Hash(str);
//    }
//
//    @GetMapping("/StartSession")
//    public String startSession(
//            @RequestParam("srv") String serverName,
//            @RequestParam("key") int secretKey,
//            @RequestParam("ver") String srvVer,
//            HttpServletRequest request)
//    {
//        PrintRequestData(request);
//        String key = Integer.toString(secretKey);
//        System.out.println("start session, srv = " + serverName + ", ver = " + srvVer + ", key = " + key);
//        return sessionManager.StartSession(serverName,srvVer,secretKey);
//    }
//
//    @GetMapping("/BanCheater")
//    public String banCheater(
//            @RequestParam("ip") String ip,
//            @RequestParam("reasonId") Integer reasonId,
//            @RequestParam("nick") String nickname,
//            @RequestParam("key") int key,
//            HttpServletRequest request)
//    {
//        PrintRequestData(request);
//        System.out.println("BanCheater. ip = " + ip + ", name = " + nickname + ", reasonId = " + reasonId + ", key = " + key);
//
//        SessionManager.CSession session = sessionManager.GetSession(key);
//
//        if (session == null)
//            return "session not found";
//
//        BannedCheater cheater = new BannedCheater();
//        cheater.setIpAddress(ip);
//        cheater.setNickname(nickname);
//        cheater.setBanReasonId(reasonId);
//        cheater.setBannedSrvName(session.serverName);
//        cheater.setBannedSrvVer(session.serverVer);
//        bannedRepositoryJPA.save(cheater);
//        return "banned";
//    }
//
//    @GetMapping("/IsBanned")
//    public String setupForm1(
//            @RequestParam("ip") String ip,
//            @RequestParam("name") String name,
//            @RequestParam("key") int key,
//            HttpServletRequest request)
//    {
//        PrintRequestData(request);
//        System.out.println("IsBanned for " + ip + ", name " + name+ ", key "+Integer.toString(key));
//
//        SessionManager.CSession session = sessionManager.GetSession(key);
//
//        if (session == null)
//            return "session not found";
//
//        return Integer.toString(bannedRepositoryJPA.isRecordExist(ip));
//    }
//
//    @GetMapping("/IsBanned2")
//    public String IsBanned(
//            @RequestParam("ip") String ip,
//            @RequestParam("name") String name,
//            @RequestParam("HWID") String hwid,
//            @RequestParam("key") int key,
//            HttpServletRequest request)
//    {
//        PrintRequestData(request);
//        System.out.println("IsBanned for " + ip + ", name " + name+ ", key "+Integer.toString(key));
//
//        SessionManager.CSession session = sessionManager.GetSession(key);
//
//        if (session == null)
//            return "session not found";
//
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
//
//    @CrossOrigin(origins = "*")
//    @GetMapping("/ListAllJSON")
//    public List<BannedCheater> listAllJS(HttpServletRequest request)
//    {
//        System.out.println(request.getRemoteAddr() + " requests ListAllJS");
//        List<BannedCheater> lst = bannedRepositoryJPA.findAll();
//        return lst;
//    }
//
//    @GetMapping("/ListPlayers")
//    public String listPlayers(HttpServletRequest request)
//    {
//        System.out.println(request.getRemoteAddr() + " requests ListPlayers");
//
//        // Можно еще красивее вывести для html но пока и так сойдет))
//        List<Player> lst = playersRepositoryJPA.findAll();
//        String str="";
//
//        String TabHTML = "&emsp; &emsp; &emsp; &emsp; &emsp; &emsp; &emsp; &emsp;";
//
//        for(int i = 0; i < lst.size(); i++)
//        {
//            str+="<br>";
//            Player player = lst.get(i);
//            str+=player.getName();
//            str+=TabHTML;
//            str+=player.getIpAddress();
//        }
//
//        return str;
//    }
//}
