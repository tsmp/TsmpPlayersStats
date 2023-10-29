package kernel.services;

import kernel.entity.*;
import kernel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class PlayersBase
{
    @Autowired
    private IpAddressesRepoJPA ipAddressesRepoJPA;

    @Autowired
    private PlayersRepoJPA playersRepoJPA;

    @Autowired
    private PlayerGameRepoJPA playerGameRepoJPA;

    @Autowired
    private GamesRepoJPA gamesRepoJPA;

    @Autowired
    private NicknamesRepoJPA nicknamesRepoJPA;

    private Integer GetGameId(Game go)
    {
        List<Game> lst = gamesRepoJPA.findAll();

        for (Game game: lst)
        {
            if(game.getServerNameId().equals(go.getServerNameId()) && go.getGameDate().equals(game.getGameDate()))
            {
                return game.getGameId();
            }
        }

        gamesRepoJPA.save(go);
        return go.getGameId();
    }

    private Integer FindPlayerByIp(String ip)
    {
        Optional<IpAddress> findIp = ipAddressesRepoJPA.findById(ip);

        if(findIp.isEmpty())
            return 0;

        IpAddress addr = findIp.get();
        Integer iPlayer = addr.getPlayer();

        if(iPlayer == null)
            throw new RuntimeException("error! player by ip not found!");

        return iPlayer;
    }

    private Integer FindPlayerByUid(int Uid)
    {
        if(Uid == 0)
            return 0;

        List<Integer> players = playersRepoJPA.SearchByUID(Uid);

        if(players.isEmpty())
            return 0;

        return players.get(0);
    }

    private Integer SaveNewPlayer(Player player, IpAddress ip)
    {
        if(player.getUID() == 0)
            player.setUID(null);

        Player saved = playersRepoJPA.save(player);
        System.out.println("saved new player with id " + Integer.toString(saved.getPlayerId()));
        int playerId = saved.getPlayerId();

        // save ip
        ip.setPlayer(playerId);
        ipAddressesRepoJPA.save(ip);

        return playerId;
    }

    private Integer GetIdOfPlayerWithoutUid(IpAddress ip, Player player)
    {
        int playerId = FindPlayerByIp(ip.getAddress());

        if(playerId != 0)
            return playerId;

        return SaveNewPlayer(player, ip);
    }

    public int GetIdOfPlayerWithUid(IpAddress ip, Player player)
    {
        final int uid = player.getUID();
        final int playerByIp = FindPlayerByIp(ip.getAddress());
        final int playerByUid = FindPlayerByUid(uid);

        if(playerByIp == 0 && playerByUid == 0)
            return SaveNewPlayer(player, ip);

        if(playerByUid != 0)
        {
            if(playerByIp != 0 && playerByIp != playerByUid)
                System.out.println("WARNING: player by ip is not equal to player by uid");

            return playerByUid;
        }

        // playerByIp != 0
        Player pl = playersRepoJPA.findById(playerByIp).get();

        // У него уже есть UID и он отличается, интересно, значит точно другой игрок (ip ему перетрем)
        if(pl.getUID() != null && pl.getUID() != uid)
        {
            System.out.println("WARNING: player by ip has different uid");
            return SaveNewPlayer(player, ip);
        }

        // Cчитаем что тот же товарищ, если нашелся по ip и у него нет uid (или совпадает с нашим)
        pl.setUID(uid);
        playersRepoJPA.save(pl); // Зададим ему UID
        return playerByIp;
    }

    public void AddPlayer(IpAddress ip, Player player, Nickname nickname)
    {
        final boolean hasUid = player.getUID() != null && player.getUID() != 0;
        final int playerId = hasUid ? GetIdOfPlayerWithUid(ip, player) : GetIdOfPlayerWithoutUid(ip, player);

        nickname.setPlayerId(playerId);

        Player fromBase = playersRepoJPA.getById(playerId);
        Set<Nickname> namesSet = fromBase.getNicknames();

        if(namesSet != null)
        {
            List<Nickname> names = new ArrayList<>(namesSet);

            for (int ii = 0; ii < names.size(); ii++) // TODO: Optimize
            {
                String name1 = names.get(ii).getNickname();
                String name2 = nickname.getNickname();

                if(name1.equals(name2))
                    return;
            }
        }

        nicknamesRepoJPA.save(nickname);
    }

    public boolean IsPlayerBanned(IpAddress ip)
    {
        return false;
    }

    public void Test()
    {
        List<Player> lst = playersRepoJPA.findAll();
        System.out.println("12345");
    }

    public List<Player> GetAllPlayers()
    {
        return playersRepoJPA.findAll();
    }
}
