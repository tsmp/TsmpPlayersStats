package kernel.services;

import kernel.entity.ActiveSession;
import kernel.entity.Game;
import kernel.entity.Hit;
import kernel.entity.Weapon;
import kernel.repository.GamesRepoJPA;
import kernel.repository.HitRepoJpa;
import kernel.repository.PlayersRepoJPA;
import kernel.repository.WeaponRepoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class StatsServServices
{
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PlayersRepoJPA playersRepoJPA;

    @Autowired
    private GamesRepoJPA gamesRepoJPA;

    @Autowired
    private WeaponRepoJpa weaponRepoJPA;

    @Autowired
    private HitRepoJpa hitsRepoJPA;

    public String GenNewPlayerUID(int key)
    {
        ActiveSession session = sessionManager.GetSession(key);

        if (session == null)
            return "Error: not authorized!";

        return playersRepoJPA.NextValueUID().toString();
    }

    final String[] keys = { "frags:", "fragsNpc:", "headshot:", "deaths:", "arts:", "maxFrags:", "time:", "weaponCnt:" };

    int ExtractInt(String statsStr, int idx)
    {
        String key = keys[idx];
        String nextKey = keys[idx+1];
        int start = statsStr.indexOf(key) + key.length();
        int end = statsStr.indexOf(nextKey);

        String val = statsStr.substring(start, end);
        return Integer.parseInt(val);
    }

    int ExtractWeaponCnt(String statsStr)
    {
        String key = "weaponCnt:";
        String keyNext = "wpn:";

        int start = statsStr.indexOf(key) + key.length();
        int end = statsStr.indexOf(keyNext);

        if(end == -1)
            return 0;

        String val = statsStr.substring(start, end);
        return Integer.parseInt(val);
    }

    public void test()
    {
        String statsStr = "frags:1fragsNpc:2headshot:3deaths:4arts:5maxFrags:6time:7weaponCnt:2wpn:diglhits:5wpn:akhits:9";
        SaveGameStats(1,1,statsStr);
    }

    void SaveWpnStats(String statsStr, int weaponsCnt, int gameId)
    {
        int searchFrom = 0;
        final String keyWpn = "wpn:";
        final String keyCnt = "hits:";

        for(int i = 0; i < weaponsCnt; i++)
        {
            Hit hit = new Hit();

            int namePos = statsStr.indexOf(keyWpn, searchFrom) + keyWpn.length();
            int hitsStart = statsStr.indexOf(keyCnt, searchFrom);
            int hitsPos = hitsStart + keyCnt.length();
            searchFrom = hitsPos;

            int hitsEnd = statsStr.indexOf(keyWpn, searchFrom);

            if(hitsEnd == -1)
                hitsEnd = statsStr.length();

            String wpnName = statsStr.substring(namePos, hitsStart);
            String cnt = statsStr.substring(hitsPos, hitsEnd);
            List<Integer> wpnId = weaponRepoJPA.SearchByName(wpnName);

            if(wpnId.isEmpty())
            {
                Weapon wpn = new Weapon();
                wpn.setName(wpnName);
                weaponRepoJPA.save(wpn);
                wpnId.add(wpn.getId());
            }

            hit.setWpn(wpnId.get(0));
            hit.setHits(Integer.parseInt(cnt));
            hit.setGame(gameId);

            hitsRepoJPA.save(hit);
        }
    }

    void SaveGameStats(int srvNameId, int playerUID, String statsStr)
    {
        Game game = new Game();

        game.setKills(ExtractInt(statsStr, 0));
        game.setKillsAi(ExtractInt(statsStr, 1));
        game.setHeadshots(ExtractInt(statsStr, 2));
        game.setDeaths(ExtractInt(statsStr, 3));
        game.setArtefacts(ExtractInt(statsStr, 4));
        game.setMaxKillsOneLife(ExtractInt(statsStr, 5));
        game.setGameTimeMinutes(ExtractInt(statsStr, 6));

        game.setGameDate(new Date());
        game.setServerNameId(srvNameId);
        game.setPlayerUID(playerUID);

        gamesRepoJPA.save(game);

        int weapons = ExtractWeaponCnt(statsStr);
        SaveWpnStats(statsStr, weapons, game.getGameId());
    }

    public String ReportPlayerStats(int key, int playerUID, String statsStr)
    {
        ActiveSession session = sessionManager.GetSession(key);

        if (session == null)
            return "Error: not authorized!";

        SaveGameStats(session.getSrvNameId(), playerUID, statsStr);
        return "success";
    }
}
