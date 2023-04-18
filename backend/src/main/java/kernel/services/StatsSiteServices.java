package kernel.services;

import kernel.entity.*;
import kernel.repository.*;
import kernel.response.PlayerInfoResponse;
import kernel.response.PlayersStatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StatsSiteServices
{
    @Autowired
    private PlayersRepoJPA playersRepoJPA;

    @Autowired
    private NicknamesRepoJPA nicknamesRepoJpa;

    @Autowired
    private GamesRepoJPA gamesRepoJpa;

    @Autowired
    private ServerNamesRepoJPA serverNamesRepoJPA;

    @Autowired
    private HitRepoJpa hitRepoJpa;

    @Autowired
    private WeaponRepoJpa weaponRepoJpa;

    public List<PlayersStatsResponse> getPlayers()
    {
        // TODO: переписать все нафиг) для демо подходит, для продакшена - нет

        List<Player> players = playersRepoJPA.findAll();

        for(int i= 0; i< players.size(); i++)
        {
            if(players.get(i).getUID() == null)
            {
                players.remove(i);
                i--;
            }
        }

        List<Nickname> names = nicknamesRepoJpa.findAll();
        List<Game> games = gamesRepoJpa.findAll();
        List<PlayersStatsResponse> res = new ArrayList<>();

        int counter = 1;

        for(Player player: players)
        {
            PlayersStatsResponse response = new PlayersStatsResponse();

            String namesStr = "";
            Integer pid = player.getPlayerId();
            Integer puid = player.getUID();

            for(Nickname nickname: names)
            {
                if(nickname.getPlayerId() == pid) {
                    if (!namesStr.isEmpty())
                        namesStr += ", ";

                    namesStr += nickname.getNickname();
                }
            }

            response.setNicknames(namesStr);
            int deaths = 0;

            for(Game game: games)
            {
                if(game.getPlayerUID() == puid)
                {
                    response.setArts(response.getArts()+game.getArtefacts());
                    response.setFrags(response.getFrags() + game.getKills());
                    response.setHoursIngame(response.getHoursIngame() + game.getGameTimeMinutes());
                    deaths+= game.getDeaths();
                }
            }

            if(deaths!=0)
                response.setKd((float)response.getFrags() / deaths);

            response.setHoursIngame(response.getHoursIngame() / 60);
            response.setNum(counter);
            response.setId(player.getPlayerId());
            res.add(response);
            counter++;
        }

        return res;
    }

    public PlayerInfoResponse getPlayerStat(Integer playerId)
    {
        // TODO: переписать все нафиг) для демо подходит, для продакшена - нет

        List<Player> players = playersRepoJPA.findAll();
        Integer puid = 0;

        for(int i= 0; i< players.size(); i++)
        {
            if(players.get(i).getPlayerId() == playerId)
            {
                puid = players.get(i).getUID();
                break;
            }
        }

        if(puid == 0)
            return null;

        List<Nickname> names = nicknamesRepoJpa.findAll();
        List<Game> games = gamesRepoJpa.findAll();
        PlayerInfoResponse res = new PlayerInfoResponse();
        List<PlayerInfoResponse.GameStruct> gamess = new ArrayList<>();
        String namesStr = "";

        for (Nickname nickname : names)
        {
            if (nickname.getPlayerId() == playerId)
            {
                if (!namesStr.isEmpty())
                    namesStr += ", ";

                namesStr += nickname.getNickname();
            }
        }

        res.setNicknames(namesStr);

        // TODO: реализовать
        res.setFavouriteWeapon("lr300");
        int deaths = 0;

        List<Weapon> weapons = weaponRepoJpa.findAll();
        List<Hit> hits = hitRepoJpa.findAll();
        List<ServerName> srvNames = serverNamesRepoJPA.findAll();
        Map<Integer, Integer> wpnUsages = new HashMap<>();

        for (Game game : games)
        {
            if (game.getPlayerUID() == puid)
            {
                PlayerInfoResponse.GameStruct gameStruct = new PlayerInfoResponse.GameStruct();
                gameStruct.setDate(game.getGameDate().toString());
                // TODO: MAP name
                gameStruct.setMapName("Бассеин");
                gameStruct.setId(game.getGameId());

                for(ServerName srv: srvNames)
                {
                    if(srv.getId() == game.getServerNameId())
                    {
                        gameStruct.setServerName(srv.getName());
                        break;
                    }
                }

                for(Hit hit: hits)
                {
                    if(hit.getGame() == game.getGameId())
                    {
                        Integer wpn = hit.getWpn();
                        Integer count = hit.getHits();

                        if(wpnUsages.get(wpn) != null)
                            count += wpnUsages.get(wpn);

                        wpnUsages.put(wpn, count);
                    }
                }

                gamess.add(gameStruct);

                res.setArts(res.getArts() + game.getArtefacts());
                res.setFrags(res.getFrags() + game.getKills());
                res.setHoursIngame(res.getHoursIngame() + game.getGameTimeMinutes());
                deaths += game.getDeaths();
            }
        }

        AtomicInteger maxCnt = new AtomicInteger(0);
        AtomicInteger wpnId = new AtomicInteger(0);

        wpnUsages.forEach((id, cnt) ->
        {
            if(cnt > maxCnt.get())
            {
                maxCnt.set(cnt);
                wpnId.set(id);
            }
        });

        for (Weapon wp: weapons)
        {
            if(wp.getId() == wpnId.get())
            {
                res.setFavouriteWeapon(wp.getName());
                break;
            }
        }

        res.setGames(gamess);

        if (deaths != 0)
            res.setKd((float) res.getFrags() / deaths);

        res.setHoursIngame(res.getHoursIngame() / 60);

        return res;
    }
}
