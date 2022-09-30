package kernel.controllers;

import kernel.entity.*;
import kernel.repository.ServerNamesRepoJPA;
import kernel.services.PlayersBase;
import kernel.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("PlayersBase/v1")
public class PlayersBaseControllerV1 // Обработка запросов HTTP
{
//    @Autowired
//    private BannedRepositoryJPA bannedRepositoryJPA;
//
//    @Autowired
//    private PlayersRepositoryJPA playersRepositoryJPA;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PlayersBase playersBase;

    @Autowired
    private ServerNamesRepoJPA serverNamesRepoJPA;

    private void PrintRequestData(HttpServletRequest request)
    {
        System.out.print(request.getRemoteAddr() + " requests ");
    }

    @GetMapping("/hello")
    public String getHello(HttpServletRequest request)
    {
        PrintRequestData(request);
        System.out.println("Hello");
        return "Hello world! 12345. Привет, нубик)";
    }

    @GetMapping("/test")
    public void getTest()
    {
        playersBase.Test();
    }

    @GetMapping("/hash")
    public int getHash(@RequestParam("str") String str, HttpServletRequest request)
    {
        PrintRequestData(request);
        System.out.println("hash from " + str);
        return sessionManager.Hash(str);
    }

    @GetMapping("/StartSession")
    public String startSession(
            @RequestParam("srv") String serverName,
            @RequestParam("key") int secretKey,
            @RequestParam("ver") String srvVer,
            HttpServletRequest request)
    {
        PrintRequestData(request);
        String key = Integer.toString(secretKey);
        System.out.println("start session, srv = " + serverName + ", ver = " + srvVer + ", key = " + key);
        return sessionManager.StartSessionServer(serverName,srvVer,secretKey);
    }

    @GetMapping("/BanCheater")
    public String banCheater(
            @RequestParam("ip") String ip,
            @RequestParam("reasonId") Integer reasonId,
            @RequestParam("nick") String nickname,
            @RequestParam("key") int key,
            HttpServletRequest request)
    {
        PrintRequestData(request);
        System.out.println("BanCheater. ip = " + ip + ", name = " + nickname + ", reasonId = " + reasonId + ", key = " + key);

//        SessionManager.CSession session = sessionManager.GetSession(key);
//
//        if (session == null)
//            return "session not found";

//        BannedCheater cheater = new BannedCheater();
//        cheater.setIpAddress(ip);
//        cheater.setNickname(nickname);
//        cheater.setBanReasonId(reasonId);
//        cheater.setBannedSrvName(session.serverName);
//        cheater.setBannedSrvVer(session.serverVer);
//        bannedRepositoryJPA.save(cheater);
        return "banned";
    }

    private Integer GetSrvNameId(String name)
    {
        ServerName srvName = null;
        List<ServerName> lst = serverNamesRepoJPA.findAll();

        for (ServerName srvn: lst)
        {
            if(srvn.getName().equals(name))
            {
                srvName = srvn;
                break;
            }
        }

        if(srvName == null)
        {
            //System.out.println(name+" not found, created");
            srvName = new ServerName();
            srvName.setName(name);
            serverNamesRepoJPA.save(srvName);
            return srvName.getId();
        }

        //System.out.println(name+"found");
        return srvName.getId();
    }

    void AddPlayerToBase(String name, String ip, ActiveSession session, String hwid)
    {
        Player player = new Player();
        player.setHwid(hwid);

        IpAddress ipAddress = new IpAddress();
        ipAddress.setAddress(ip);

        Nickname nickname = new Nickname();
        nickname.setNickname(name);
        nickname.setAddedDate(new Date());

        Game game = new Game();
        game.setGameDate(new Date());
        game.setServerNameId(GetSrvNameId(session.getName())); // TODO: optimize

        playersBase.AddPlayer(ipAddress, player, nickname, game);
    }

    @GetMapping("/IsBanned")
    public String setupForm1(
            @RequestParam("ip") String ip,
            @RequestParam("name") String name,
            @RequestParam("key") int key,
            HttpServletRequest request)
    {
        PrintRequestData(request);
        System.out.println("IsBanned for " + ip + ", name " + name+ ", key " + Integer.toString(key));

        ActiveSession session = sessionManager.GetSession(key);

        if (session == null)
            return "Error: not authorized!";

        AddPlayerToBase(name,ip,session,Player.NoHwidStr);
        return "0"; // Integer.toString(bannedRepositoryJPA.isRecordExist(ip));
    }

    @GetMapping("/IsBanned2")
    public String IsBanned(
            @RequestParam("ip") String ip,
            @RequestParam("name") String name,
            @RequestParam("HWID") String hwid,
            @RequestParam("key") int key,
            HttpServletRequest request)
    {
        PrintRequestData(request);
        System.out.println("IsBanned for " + ip + ", name " + name+ ", key "+ Integer.toString(key));

        ActiveSession session = sessionManager.GetSession(key);

        if (session == null)
            return "Error: not authorized!";

        AddPlayerToBase(name,ip,session, hwid);
        return "0"; // Integer.toString(bannedRepositoryJPA.isRecordExist(ip));
    }

    @GetMapping("/ListAll")
    public String getDateToIsNull(HttpServletRequest request)
    {
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
        return "";
    }

//    @CrossOrigin(origins = "*")
//    @GetMapping("/ListAllJSON")
//    public List<BannedCheater> listAllJS(HttpServletRequest request)
//    {
////        System.out.println(request.getRemoteAddr() + " requests ListAllJS");
////        List<BannedCheater> lst = bannedRepositoryJPA.findAll();
////        return lst;
//        return null;
//    }

    @GetMapping("/ListPlayers")
    public String listPlayers(HttpServletRequest request)
    {
        System.out.println(request.getRemoteAddr() + " requests ListPlayers");
        String Tab = "&emsp; &emsp; &emsp; &emsp; &emsp; &emsp; &emsp; &emsp;";
        String NewLine = "<br>";

        // Можно еще красивее вывести для html но пока и так сойдет))
        List<Player> lst = playersBase.GetAllPlayers();
        String res="";

        res+="Players in base: "+Integer.toString(lst.size());
        res+=NewLine+NewLine;

        for(int i = 0; i < lst.size(); i++)
        {
            Player player = lst.get(i);

            res+="names: ";
            Set<Nickname> namesSet = player.getNicknames();
            List<Nickname> names = new ArrayList<>(namesSet);

            for(int ii = 0; ii<names.size();ii++)
                res+=NewLine+names.get(ii).getNickname()+" ";

            res+=NewLine+ "ip: ";
            Set<IpAddress> ipSet = player.getAddresses();
            List<IpAddress> ips = new ArrayList<>(ipSet);

            for(int ii = 0; ii<ips.size();ii++)
                res+=ips.get(ii).getAddress()+" ";

//            res+=NewLine;
//            res+="games: ";
//            Set<Game> gamesSet = player.getGames();
//            List<Game> games = new ArrayList<>(gamesSet);
//
//            for(int ii = 0; ii<games.size();ii++)
//                res+=NewLine+games.get(ii).getServerName()+" "+games.get(ii).getEnterGameDate().toString();
//
//            res+=NewLine;
            res+="hwid: ";
            res+=player.getHwid();
            res+=NewLine+NewLine;
        }

        return res;
    }
}

