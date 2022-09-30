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

    public void AddPlayer(IpAddress ip, Player player, Nickname nickname, Game game)
    {
        Optional<IpAddress> findIp = ipAddressesRepoJPA.findById(ip.getAddress());

        int playerId;
        boolean exist = !findIp.isEmpty();

        if(exist)
        {
            IpAddress addr = findIp.get();
            Integer iPlayer = addr.getPlayer();

            if(iPlayer == null)
            {
                System.out.println("error! player by ip not found!");
                return;
            }

            playerId = iPlayer;
        }
        else
        {
            ipAddressesRepoJPA.save(ip);
            Player saved = playersRepoJPA.save(player);
            System.out.println("saved new player with id " + Integer.toString(saved.getPlayerId()));

            playerId = saved.getPlayerId();
            ip.setPlayer(playerId);
            ipAddressesRepoJPA.save(ip);
        }

        Integer gameId = GetGameId(game); // TODO: Optimize

        PlayerGame playerGame = new PlayerGame();
        playerGame.setPlayerId(playerId);
        playerGame.setGameId(gameId);

        List<PlayerGame> pgList = playerGameRepoJPA.findAll();
        boolean found = false;

        for(PlayerGame pg: pgList) // TODO: Optimize
        {
            if(pg.getGameId().equals(gameId) && pg.getPlayerId().equals(playerGame.getPlayerId()))
                found=true;
        }

        if(!found)
            playerGameRepoJPA.save(playerGame);

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
