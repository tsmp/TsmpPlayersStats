package kernel.controllers;

import kernel.controllers.RequestsLogger.BaseRequestLogger;

import kernel.entity.*;
import kernel.repository.ServerNamesRepoJPA;
import kernel.services.PlayersBase;
import kernel.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("PlayersBase/v1")
public class PlayersBaseServControllerV1
{
    @Autowired
    @Qualifier("ServReqLogger")
    private BaseRequestLogger requestsLogger;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PlayersBase playersBase;

    @Autowired
    private ServerNamesRepoJPA serverNamesRepoJPA;

    public PlayersBaseServControllerV1(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @GetMapping("/hello")
    public String getHello(HttpServletRequest request)
    {
        requestsLogger.Log(request,"","Hello");
        return "Hello world! 12345. Привет, нубик)";
    }

    @GetMapping("/hash")
    public int getHash(@RequestParam("str") String str, HttpServletRequest request)
    {
        String params = "From: " + str;
        requestsLogger.Log(request, params, "Hash");
        return sessionManager.Hash(str);
    }

    @GetMapping("/StartSession")
    public String startSession(
            @RequestParam("srv") String serverName,
            @RequestParam("key") int secretKey,
            @RequestParam("ver") String srvVer,
            HttpServletRequest request)
    {
        String params = "SrvName: "+ serverName + ", Ver: " + srvVer + ", Key: "+ Integer.toString(secretKey);
        requestsLogger.Log(request,params,"StartSession");
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
        String params = "Ip: " + ip + ", Name: " + nickname + ", ReasonId: " + reasonId + ", Key: " + key;
        requestsLogger.Log(request, params, "BanCheater");

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
        String params = "Ip: "+ip+", Name: "+name+", Key: " + Integer.toString(key);
        requestsLogger.Log(request,params,"IsBanned");

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
        String params = "Ip: "+ip+", Name: "+name+"Hwis: "+hwid+", Key: " + Integer.toString(key);
        requestsLogger.Log(request,params,"IsBanned2");

        ActiveSession session = sessionManager.GetSession(key);

        if (session == null)
            return "Error: not authorized!";

        AddPlayerToBase(name,ip,session, hwid);
        return "0"; // Integer.toString(bannedRepositoryJPA.isRecordExist(ip));
    }
}
