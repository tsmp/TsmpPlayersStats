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

    public int PlayerId(IpAddress ip, Player player, Nickname nickname)
    {
        Optional<IpAddress> findIp = ipAddressesRepoJPA.findById(ip.getAddress());
        int playerId = 0;
        int uid = 0;

        if(player.getUID() != null)
            uid = player.getUID();

        if(!findIp.isEmpty())
        {
            IpAddress addr = findIp.get();
            Integer iPlayer = addr.getPlayer();

            if(iPlayer == null)
                throw new RuntimeException("error! player by ip not found!");

            playerId = iPlayer;
        }

        // если есть с таким uid но нет такого ip
        if(uid != 0)
        {
            List<Integer> players = playersRepoJPA.SearchByUID(uid);

            if(!players.isEmpty())
            {
                if(playerId != 0 && playerId != players.get(0))
                {
                    IpAddress addr = findIp.get(); // переназначаем ip на игрока с uid
                    addr.setPlayer(players.get(0));
                    ipAddressesRepoJPA.save(addr);
                }

                playerId = players.get(0);
            }
        }

        if(playerId !=0)
        {
            Player pl = playersRepoJPA.getById(playerId);

            if(pl.getUID() == null && uid !=0)
            {
                pl.setUID(uid);
                playersRepoJPA.save(pl); // update entry with UID
            }

            return playerId; // already have player and ip
        }
        else
            player.setUID(null);

        // add new player
        if(playerId == 0)
        {
            Player saved = playersRepoJPA.save(player);
            System.out.println("saved new player with id " + Integer.toString(saved.getPlayerId()));
            playerId = saved.getPlayerId();
        }

        // save ip
        ip.setPlayer(playerId);
        ipAddressesRepoJPA.save(ip);
        return playerId;
    }

    public void AddPlayer(IpAddress ip, Player player, Nickname nickname)
    {
        int playerId = PlayerId(ip, player, nickname);

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
