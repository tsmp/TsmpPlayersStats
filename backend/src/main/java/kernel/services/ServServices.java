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

    public String Hello()
    {
        return "Hello world! 12345. Привет, нубик)";
    }

    public String StartSession(String serverName, int secretKey, String srvVer, String mapName)
    {
        return sessionManager.StartSessionServer(serverName,srvVer,secretKey, mapName);
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

    void AddPlayerToBase(String name, String ip, ActiveSession session, int uid)
    {
        Player player = new Player();
        player.setUID(uid);

        IpAddress ipAddress = new IpAddress();
        ipAddress.setAddress(ip);

        Nickname nickname = new Nickname();
        nickname.setNickname(name);

        playersBase.AddPlayer(ipAddress, player, nickname);
    }

    public String IsBanned(String ip, String name, int key, int uid)
    {
        ActiveSession session = sessionManager.GetSession(key);

        if (session == null)
            return "Error: not authorized!";

        AddPlayerToBase(name,ip,session, uid);
        return "0"; // Integer.toString(bannedRepositoryJPA.isRecordExist(ip));
    }
}
