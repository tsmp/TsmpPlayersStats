package kernel.services;

import kernel.entity.Game;
import kernel.entity.Nickname;
import kernel.entity.Player;
import kernel.repository.GamesRepoJPA;
import kernel.repository.NicknamesRepoJPA;
import kernel.repository.PlayersRepoJPA;
import kernel.response.PlayersStatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatsSiteServices
{
    @Autowired
    private PlayersRepoJPA playersRepoJPA;

    @Autowired
    private NicknamesRepoJPA nicknamesRepoJpa;

    @Autowired
    private GamesRepoJPA gamesRepoJpa;

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
            res.add(response);
        }

        return res;
    }
}
