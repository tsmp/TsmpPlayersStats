package kernel.services;

import kernel.entity.*;
import kernel.repository.ServerNamesRepoJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ServServices
{
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PlayersBase playersBase;

    @Autowired
    private ServerNamesRepoJPA serverNamesRepoJPA;

    public String Hello()
    {
        return "Hello world! 12345. Привет, нубик)";
    }

    public String StartSession(String serverName, int secretKey, String srvVer)
    {
        return sessionManager.StartSessionServer(serverName,srvVer,secretKey);
    }

    public String BanCheater(String ip, Integer reasonId, String nickname, int key)
    {
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

    public String IsBanned(String ip, String name, int key, String hwid)
    {
        ActiveSession session = sessionManager.GetSession(key);

        if (session == null)
            return "Error: not authorized!";

        AddPlayerToBase(name,ip,session, hwid.isEmpty() ? Player.NoHwidStr : hwid);
        return "0"; // Integer.toString(bannedRepositoryJPA.isRecordExist(ip));
    }
}
